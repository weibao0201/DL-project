package gov.nist.itl.csd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ExpressionLexer;
import org.chocosolver.solver.constraints.ExpressionParser;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.Variable;

/**
 * Created by dyaga on 5/23/2016.
 */
public class ChocoHelper
{

    static org.chocosolver.solver.constraints.Constraint[] parser(final String expression, final IntVar[] vars)
    {
        final ExpressionLexer lexer = new ExpressionLexer(new ANTLRInputStream(expression));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final ExpressionParser p = new ExpressionParser(tokens);
        p.setBuildParseTree(true);
        return p.assignment(vars[0].getSolver(), vars).constraints;
    }

    public String                          IsFeasible;
    public HashMap<String, RelationOutput> results = new HashMap<String, RelationOutput>();
    private final String                   expression;

    private final ArrayList<String>        operands;

    public ChocoHelper(final String expr)
    {
        operands = new ArrayList<String>();
        expression = expr;
        IsFeasible = "Unknown";
    }

    private String Convert()
    {
        // System.err.println(String.format("Expression to Convert: %s",
        // expression));

        final Pattern operandPattern = Pattern.compile("\\s*\\w+\\s*");
        final Matcher operandMatcher = operandPattern.matcher(expression);
        while (operandMatcher.find())
        {
            final String tmp = operandMatcher.group(0).trim();
            if ((tmp.length() > 0) && !tryParseInt(tmp))
            {
                if (!operands.contains(tmp))
                {
                    operands.add(tmp);
                }
            }
        }
        String output = expression;
        int i = 0;
        for (final String s : operands)
        {
            output = output.replaceAll(s, String.format("{%d}", i));
            i++;
        }
        // System.err.println(String.format("Converted Expression: %s",
        // output));
        return output;
    }

    public ArrayList<String> getOperands()
    {
        return operands;
    }

    public void Solve()
    {
        final String convertedExpression = Convert();
        final int lowest = 0;// VariableFactory.MIN_INT_BOUND;
        final int highest = 100;// VariableFactory.MAX_INT_BOUND;
        final Solver solver = new Solver();
        final List<IntVar> vars = new ArrayList<IntVar>();

        for (final String var : getOperands())
        {
            vars.add(VF.bounded(var, lowest, highest, solver));
        }

        final IntVar[] array = vars.toArray(new IntVar[vars.size()]);
        final org.chocosolver.solver.constraints.Constraint[] constraints = parser(convertedExpression, array);
        // for (org.chocosolver.solver.constraints.Constraint c : constraints)
        // {
        // System.err.println(c);
        // }
        solver.post((constraints));
        solver.findAllSolutions();

        String passValue = "";
        String nonPassValue = "";
        // System.err.println(String.format("Is Solution Solvable: %s",
        // solver.isFeasible()));
        for (final Variable v : solver.getVars())
        {

            if (!getOperands().contains(v.getName()))
            {
                continue;
            }
            final IntVar tmp = (IntVar) v;
            final int min = tmp.getLB();
            final int max = tmp.getUB();

            if (min != max)
            {
                if (max == highest)
                {
                    // no upper bound difference
                    passValue += String.format("%s: [%d to %d] ", tmp.getName(), min, max);
                    nonPassValue += String.format("%s: [%d to %d] ", tmp.getName(), lowest, min - 1);
                }
                else if (min == lowest)
                {
                    // no lower bound difference
                    passValue += String.format("%s: [%d to %d] ", tmp.getName(), min, max);
                    nonPassValue += String.format("%s: [%d to %d] ", tmp.getName(), max + 1, highest);
                }
                else
                {
                    final String start = lowest == (min - 1) ? String.format("%d", lowest)
                            : String.format("%d to %d", lowest, min - 1);
                    final String end = (max + 1) == highest ? String.format("%d", highest)
                            : String.format("%d to %d", max + 1, highest);
                    final String inverse = String.format("[%s, and %s]", start, end);
                    passValue += String.format("%s: [%d to %d] ", tmp.getName(), min, max);
                    nonPassValue += String.format("%s: %s ", tmp.getName(), inverse);
                }
            }
            else
            {
                // min == max
                passValue += String.format("%s = %d ", tmp.getName(), tmp.getValue());
                nonPassValue += String.format("%s != %d ", tmp.getName(), tmp.getValue());
            }
            results.put(expression.trim(), new RelationOutput(passValue, nonPassValue));// .add(new
                                                                                        // RelationOutput(expression,
                                                                                        // passValue,
                                                                                        // nonPassValue));

        }
        IsFeasible = solver.isFeasible().toString();
    }

    private boolean tryParseInt(final String value)
    {
        try
        {
            Integer.parseInt(value);
            return true;
        }
        catch (final NumberFormatException e)
        {
            return false;
        }
    }
}
