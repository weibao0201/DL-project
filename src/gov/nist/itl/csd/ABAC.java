package gov.nist.itl.csd;

import com.bpodgursky.jbool_expressions.ExprUtil;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.Not;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import edu.uta.cse.fireeye.common.*;
import edu.uta.cse.fireeye.common.TestGenProfile.ConstraintMode;
import edu.uta.cse.fireeye.service.engine.FireEye;
import edu.uta.cse.fireeye.service.exception.OperationServiceException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Attribute Based Access Control Boolean Expression Evaluator
// Developed by: dylan.yaga@nist.gov
// Based on http://csrc.nist.gov/groups/SNS/acts/documents/abac-pseudo-ex-iwct.pdf

public class ABAC
{
    public final static  String                tab_value       = "\t";
    // JBool Conversion Characters
    private final static String                jbool_not       = "!";
    private final static String                jbool_and       = "&";
    private final static String                jbool_or        = "|";
    private final static String                alternate_not   = "~";
    private final static String                alternate_and   = "&&";
    private final static String                alternate_or    = "||";
    private final static String                empty_string    = "";
    // Min/Max size for t-way
    private final static int                   doi_min         = 1;
    private final static int                   doi_max         = 6;
    public static        String                delimiter_value = ",";
    // Output Characters
    public static        String                false_value     = "0";
    public static        String                true_value      = "1";
    private final        ByteArrayOutputStream baos            = new ByteArrayOutputStream();
    public String ConsoleOutput;
    ArrayList<ResultOutput[]> DenyResults  = null;
    ArrayList<ResultOutput[]> GrantResults = null;
    // Variable to hold the determined t-Way max
    private int tWayMax;

    // List of individual elements broken apart from main expression
    private List<Expression<String>> elements;
    // The set of variables for the entire expression
    private Set<String>              variables;
    // Expression, as parsed by JBool
    private Expression<String>       expr;
    // R, or the DNF of the JBool Expression
    private Expression<String>       expr_r;
    // ~R, the inverse of R
    private Expression<String>       expr_r_inverse;
    // A String representation of a Fixed and JBool converted original input
    private String                   expression;
    // Copy of original input, for display purposes
    private String                   originalExpression;
    // Time Metric for DTest (Deny test)
    private long                     DTime;
    // Time Metric for GTest (Grant test)
    private long                     GTime;
    // Time Metric for Initialization
    private long                     ITime;
    private boolean                  wasFixed;
    private PrintStream stdout = System.out;
    private int MaxVarLength;
    private HashMap<String, String> Relations = new HashMap<String, String>();
    private ArrayList<ChocoHelper> Solvers;

    public ABAC(final String booleanExpression)
    {
        try
        {
        	Solvers = new ArrayList<ChocoHelper>();
            final PrintStream ps = new PrintStream(baos);
            System.setOut(ps);
            final long startTime = System.nanoTime();
            originalExpression = booleanExpression;
            // Try to convert the expression into a JBool compatible form:
            // Single Operators (& and |)
            // Negation sign of ~ instead of !
            final String compat = JBoolHelper.convertStringToJBoolCompat(booleanExpression);
            wasFixed = !originalExpression.equals(compat);
            expression = ParenthesesUtil.fixParentheses(compat);
            // Strip out relational expressions
            expression = StripRelational(expression);
            // Parse expression
            expr = ExprParser.parse(expression);
            // Convert to Distributed Normal Form, known as R
            expr_r = RuleSet.toDNF(expr);
            // Not it, and change it to Conjunctive Normal Form so that we get
            // ~R
            expr_r_inverse = RuleSet.toCNF(Not.of(expr_r));

            elements = JBoolHelper.splitJboolExpressionOnOr(expr_r);
            variables = new TreeSet<String>(ExprUtil.getVariables(expr_r));
            MaxVarLength = FindLargestHeader();

            tWayMax = determineTWayMax();
            final long endTime = System.nanoTime();
            ITime = nanoToMilliseconds(endTime - startTime);
        }
        catch (final Exception e)
        {
            System.out.println(e.toString());
        }
    }

    public static void setFalse_value(final String false_value)
    {
        ABAC.false_value = false_value;
    }

    public static void setTrue_value(final String true_value)
    {
        ABAC.true_value = true_value;
    }

    private String StripRelational(String passedIn)
    {
        int counter = 0;
        Pattern p = Pattern.compile("((\\s*\\w+\\s*)((<|>|=|!=|>=|<=)(\\s*\\w+\\s*));)+");
        Matcher m = p.matcher(passedIn);

        while (m.find())
        {
            String current = m.group(0);
            String replacement = String.format(" e%d ", counter++);
            Relations.put(replacement.trim(), current.trim());
            passedIn = passedIn.replaceFirst(current, replacement);
            Solvers.add(new ChocoHelper(current));
            Solvers.get(counter-1).Solve();

        }
        return passedIn;
    }

    private int determineTWayMax()
    {

        int output = doi_min;
        for (final Expression<String> element : elements)
        {
            final String tmp = element.toString();
            final String[] mod =
                    tmp.replaceAll("\\(|\\)|\\!|\\&|\\||\\|\\||\\&\\&|\\~", empty_string).split("\\s+", -1);
            final int currentCount = mod.length;
            if (currentCount > output)
            {
                output = currentCount;
            }

        }
        // Sanity checks
        if (output < doi_min)
        {
            output = doi_min;
        }
        if (output > doi_max)
        {
            output = doi_max;
        }
        return output;
    }

    public String DTestOutput(boolean showExtras)
    {
        final StringBuilder b = new StringBuilder();
        if (showExtras)
        {
            b.append("#Deny Results\n");
            b.append("#");
            b.append(Headers());
            b.append("\n");
        }

        for (ResultOutput[] outputs : DenyResults)
        {
            b.append(" ");
            String whole = "";
            for (ResultOutput part : outputs)
            {
                String key = part.ColumnHeader;
                if (Relations.containsValue(key))
                {
                	for (ChocoHelper solver : Solvers)
                    {
                        if (solver.results.containsKey(key))
                        {
	                        RelationOutput relationOutput = solver.results.get(key);
	                        if (part.Result.equals(true_value))
	                        {
	                            whole += String.format("Grant: %s, ", relationOutput.PassingValues);
	                        }
	                        else
	                        {
	                            whole += String.format("Deny: %s, ", relationOutput.NonPassingValues);
	                        }
                        }
                    }
                }
                else
                {
                    whole += String.format("%s, ", part.Result);
                }
            }
            b.append(whole.replaceAll(", $", ""));
            b.append("\n");
        }

        return b.toString();
    }

    public void DTest()
    {
        final long startTime = System.nanoTime();
        DenyResults = new ArrayList<ResultOutput[]>();
        final String constraint = JBoolHelper.convertStringToActsCompat(expr_r_inverse.toString());

        final ArrayList<Parameter> params = new ArrayList<Parameter>();
        for (final String v : variables)
        {
            final Parameter tmp = new Parameter(v);
            tmp.setType(Parameter.PARAM_TYPE_BOOL);
            tmp.addValue("true");
            tmp.addValue("false");
            params.add(tmp);
        }

        final SUT sut = new SUT();
        sut.setName("ABAC TEST");
        sut.setParameters(params);
        sut.setOutputParameters(params);
        sut.addConstraint(new Constraint(constraint, params));

        final TestGenProfile tgProfile = TestGenProfile.instance();
        tgProfile.setAlgorithm(TestGenProfile.PV_IPOG);
        tgProfile.setDOI(tWayMax);
        tgProfile.setRandstar(true);
        tgProfile.setMode(TestGenProfile.PV_SCRATCH);
        tgProfile.setOutputFormat(TestGenProfile.PV_CSV);
        tgProfile.setConstraintMode(ConstraintMode.forbiddentuples);

        TestSet ts = new TestSet();
        try
        {
            // FireEye.verbose = true;
            ts = FireEye.generateTestSet(ts, sut);

            sut.setExistingTestSet(ts);

            final ArrayList<int[]> matrix = ts.getMatrix();
            for (final int[] row : matrix)
            {
                List<ResultOutput> results = new ArrayList<ResultOutput>();
                for (int i = 0; i < row.length; i++)
                {
                    final String val = sut.getParam(i).getValue(row[i]);
                    String value;
                    if (val.equals("true"))
                    {
                        value = true_value;
                    }
                    else
                    {
                        value = false_value;
                    }
                    String v = variables.toArray()[i].toString();
                    String insert = v;
                    if (Relations.containsKey(v))
                    {
                        insert = Relations.get(v).trim();
                    }
                    results.add(new ResultOutput(insert, value));
                }
                DenyResults.add(results.toArray(new ResultOutput[results.size()]));
            }
        }
        catch (final OperationServiceException e)
        {
            e.printStackTrace();
        }
        ConsoleOutput = baos.toString();
        final long endTime = System.nanoTime();
        DTime = nanoToMilliseconds(endTime - startTime);
        System.setOut(stdout);
    }

    private int FindLargestHeader()
    {
        int output = 0;
        for (final String v : variables)
        {
            if (Relations.containsKey(v))
            {
                if (Relations.get(v).length() > output)
                {
                    output = Relations.get(v).length();
                }
            }
            else
            {
                if (v.length() > output)
                {
                    output = v.length();
                }
            }
        }

        return output;
    }

    public String Headers()
    {
        final StringBuilder b = new StringBuilder();
        for (final String v : variables)
        {
            String insert = v;
            if (Relations.containsKey(v))
            {
                insert = Relations.get(v).trim();
            }
            b.append(String.format("%s, ", insert));
        }
        return b.toString().replaceAll(", $", "");
    }


    public String GTestFileOutput(String delimiter_override)
    {
        return "";//GenerateTestFileOutput(GrantResults, delimiter_override);
    }

    public String DTestFileOutput(String delimiter_override)
    {
        return "";//GenerateTestFileOutput(DenyResults, delimiter_override);
    }

    private String GenerateTestFileOutput(ArrayList<String[]> ResultList, String delimeter_override)
    {
        StringBuilder b = new StringBuilder();
        for (String[] result : ResultList)
        {
            String current = "";
            for (String value : result)
            {
                current += String.format("%s%s", value, delimeter_override);

            }
            current = current.substring(0, current.lastIndexOf(delimeter_override));
            b.append(current);
            b.append(String.format("%n"));
        }

        return b.toString();
    }

    public String GTestOutput(boolean showExtras)
    {
        final StringBuilder b = new StringBuilder();
        
        if (showExtras)
        {
        	b.append("#Grant Results\n");
            b.append("#");
            b.append(Headers());
            b.append("\n");
        }
        for (int i = 0; i < GrantResults.size(); i++)
        {
            b.append(" ");
            ResultOutput[] line = GrantResults.get(i);
            String whole = "";
            for (ResultOutput ss : line)
            {
                String key = ss.ColumnHeader;
                if (Relations.containsValue(key))
                {
                	for (ChocoHelper solver : Solvers)
                    {
                        if (solver.results.containsKey(key))
                        {
	                        RelationOutput relationOutput = solver.results.get(key);
	                        if (ss.Result.equals(true_value))
	                        {
	                            whole += String.format("Grant: %s, ", relationOutput.PassingValues);
	                        }
	                        else
	                        {
	                            whole += String.format("Deny: %s, ", relationOutput.NonPassingValues);
	                        }
	                    }
                    }
                }
                else
                {
                    whole += String.format("%s, ", ss.Result);
                }
            }
            
            b.append(whole.replaceAll(", $", ""));
            if (showExtras)
            {
                String element = elements.get(i).toString();

                for (String s : Relations.keySet())
                {
                    if (element.contains(s))
                    {
                        element = String.format(
                                "%s%s", tab_value, element.replaceAll(
                                        s,
                                        Relations.get(s)));

                    }

                }
                b.append(element);
            }

            b.append("\n");
        }
        return b.toString();
    }

    public void GTest()
    {
        final long startTime = System.nanoTime();
        GrantResults = new ArrayList<ResultOutput[]>();
        for (Expression<String> element : elements)
        {
            List<ResultOutput> resultOutputs = new ArrayList<ResultOutput>();
            final String term = element.toString();
            for (final String v : variables)
            {
                String current_value;
                if (JBoolHelper.hasNegation(term, v))
                {
                    // For this term to pass, this variable must be false
                    current_value = false_value;
                }
                else if (JBoolHelper.justVariable(term, v))
                {
                    // For this term to pass, this variable must be true
                    current_value = true_value;
                }
                else
                {
                    // This variable was not present in the term
                    // for GTest it must be false for this round
                    current_value = false_value;
                }
                String insert = v;
                if (Relations.containsKey(v))
                {
                    insert = Relations.get(v).trim();
                }
                resultOutputs.add(new ResultOutput(insert, current_value));
            }
            GrantResults.add(resultOutputs.toArray(new ResultOutput[resultOutputs.size()]));
        }
        final long endTime = System.nanoTime();
        GTime = nanoToMilliseconds(endTime - startTime);
    }

    private int Spacer()
    {
        return ((MaxVarLength) * -1);
    }

    private long nanoToMilliseconds(final long nanoTime)
    {
        return nanoTime / 1000000;
    }

    public String Summary()
    {
        return String.format(
                "Initialization Time: %3dms\nRuntime of Grant Test: %3dms\nRuntime of Deny Test: %3dms\nDynamically Set DOI: %3d",
                ITime,
                GTime,
                DTime,
                tWayMax
                            );
    }

    @Override
    public String toString()
    {
        final StringBuilder b = new StringBuilder();
        // Display RelationOutput
        b.append(String.format("Original:\n\t%s\n", originalExpression));
        b.append(String.format("Converted to JBool Format:\n\t%s\n", expression));
        b.append(String.format("Parsed by JBool:\n\t%s\n", expr));
        b.append(String.format("Converted To DNF (R):\n\t%s\n", expr_r));
        b.append(String.format("Inverted (DNF -> Not -> to CNF) (~R):\n\t%s\n", expr_r_inverse));
        if(Solvers != null)
        {
        	for (ChocoHelper solver : Solvers)
            {
        		b.append(String.format("Were Relations Feasible: %s\n", solver.IsFeasible));
            }
        }
        return b.toString();
    }

    // Private static helper class for JBool_Expression library
    private static class JBoolHelper
    {
        private static String convertStringFromJBoolCompat(final String input)
        {
            return toAlternateNot(toAlternateOperators(input));
        }

        private static String convertStringToActsCompat(final String input)
        {
            return toJboolNot(toAlternateOperators(input));
        }

        private static String convertStringToJBoolCompat(final String input)
        {
            return toJboolNot(toJboolOperators(input)).toLowerCase();
        }

        private static boolean hasNegation(final String term, final String v)
        {
            // Check the term for any pattern that matches either ~ or ! then a
            // variable
            // This shows that the variable is negated
            return term.matches(String.format(".*[~|!]\\s*%s.*", v));
        }

        private static boolean justVariable(final String term, final String v)
        {
            return term.contains(v);
        }

        private static List<Expression<String>> splitJboolExpressionOnOr(final Expression<String> expression)
        {
            final List<Expression<String>> output = new ArrayList<Expression<String>>();
            // create local copy of string
            final String local = expression.toString();

            // have to use escape character for the Regular Expression split
            final String[] split = local.split(String.format("\\%s", jbool_or));

            for (String aSplit : split)
            {
                final String fixed = ParenthesesUtil.fixParentheses(aSplit.trim());
                final Expression<String> parsed = RuleSet.toDNF(ExprParser.parse(fixed));
                output.add(parsed);
            }
            return output;
        }

        private static String toAlternateNot(final String input)
        {
            return input.replace(jbool_not, alternate_not);
        }

        private static String toAlternateOperators(final String input)
        {
            return input.replace(jbool_and, alternate_and).replace(jbool_or, alternate_or);
        }

        private static String toJboolNot(final String input)
        {
            return input.replace(alternate_not, jbool_not);
        }

        private static String toJboolOperators(final String input)
        {
            return input.replace(alternate_and, jbool_and).replace(alternate_or, jbool_or);
        }

    }

    // Private static helper class for Parentheses
    private static class ParenthesesUtil
    {
        public static String fixParentheses(final String input)
        {
            String output = input;
            while (isParenthesesBalanced(output) != ParenthesesStatus.Ok)
            {
                if (isParenthesesBalanced(output) == ParenthesesStatus.MissingEndParen)
                {
                    output = String.format("%s)", output);
                }
                else if (isParenthesesBalanced(output) == ParenthesesStatus.MissingOpenParen)
                {
                    output = String.format("(%s", output);
                }
            }
            // remove empty paren pairs
            output = output.replaceAll("\\(\\s*\\)", empty_string);
            // Remove operators at start or end of line, except negation at
            // start
            output = output.replaceAll("((\\&|\\||\\!|\\&\\&|\\|\\||\\~)*)$", empty_string);
            output = output.replaceAll("^(\\&|\\|)*|((\\~|\\!)+((\\&|\\|)+))", empty_string);
            output = output.replaceAll("\\~\\~|\\!\\!", jbool_not);
            return output;
        }

        private static ParenthesesStatus isParenthesesBalanced(final String in)
        {
            final Stack<Character> st = new Stack<Character>();

            for (final char chr : in.toCharArray())
            {
                switch (chr)
                {
                    case '(':
                        st.push(chr);
                        break;
                    case ')':
                        if (st.isEmpty() || (st.pop() != '('))
                        {
                            return ParenthesesStatus.MissingOpenParen;
                        }
                        break;
                }
            }
            if (st.isEmpty())
            {
                return ParenthesesStatus.Ok;
            }
            return ParenthesesStatus.MissingEndParen;
        }

        private enum ParenthesesStatus
        {
            Ok, MissingOpenParen, MissingEndParen
        }
    }

}
