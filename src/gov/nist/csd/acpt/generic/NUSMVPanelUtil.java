/* This software was developed by employees of the National Institute of
 * Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 15 United States Code Section 105, works of NIST
 * employees are not subject to copyright protection in the United States
 * and are considered to be in the public domain.  As a result, a formal
 * license is not needed to use the software.
 *
 * This software is provided by NIST as a service and is expressly
 * provided "AS IS".  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
 * OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
 * AND DATA ACCURACY.  NIST does not warrant or make any representations
 * regarding the use of the software or the results thereof including, but
 * not limited to, the correctness, accuracy, reliability or usefulness of
 * the software.
 *
 * Permission to use this software is contingent upon your acceptance
 * of the terms of this agreement.
 */
package gov.nist.csd.acpt.generic;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.model.ModelTreeUtil;
//import gov.nist.csd.acpt.target.SubjectsPanel;
import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.Generalproperties;
import gov.nist.csd.acpt.util.Property;
import org.apache.commons.lang.StringUtils;

/**
 * This class implements the (XACML) model editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class NUSMVPanelUtil extends JPanel
{

    static String                       smvTemplates                    = "";
    static String                       smvMainTemplatesWhenMerge       = "";
    static String                       smvSubPolTemplatesWhenMerge     = "";
    static String                       smvMultiLevelTempletes          = "";
    static String                       SUBJECTS                        = "";
    static String                       ACTIONS                         = "";
    static String                       RESOURCES                       = "";
    static String                       ENVIRONMENTS                    = "";
    static String                       SubPol_ARGUMENTS                = "";
    static String                       ADD_VAR                         = "";
    static String                       ADD_State_VAR                   = "";
    static String                       ADD_FunctionCallInVAR           = "";
    static String                       ADD_COMBINATION_POLICY          = "";
    static String                       ADD_RULES                       = "";
    static String                       ADD_Ordered_PERMITDENY_RULES    = "";
    static String                       ADD_PERMIT_RULES                = "";
    static String                       ADD_DENY_RULES                  = "";
    static String                       ADD_STATE_ASSIGNS               = "";
    static String                       ADD_WORKFLOW_STATE_ASSIGNS      = "";
    static String                       ADD_PROPERTY                    = "";
    static String                       ADD_REACHABILITY_INVARIANTS     = "";
    static String                       ADD_FUNCTION                    = "";
    static String                       Temp_WorkFlowRule               = "";
    static int                          MultiLevelcount                 = 1;
    static int                          WorkFlowcount                   = 1;
    static int                          rulecount                       = 1;
    static String                       endComment                      = "*** end of NuSMV verification";
    static final String                 newline                         = "\r\n";

    static String                       smvTempleteFileName             = "";
    static String                       NUSMVRunBatchfile               = "res\\run.bat";

    static DefaultMutableTreeNode       combinetreeRootNode             = null;
    static DefaultMutableTreeNode       propertyRootNode                = null;

    static List<NuSMVSubAttrLst>        attrLst                         = null;
    static List<DefaultMutableTreeNode> SubjecActionResourceAttrNodeLst = null;

    private static void initTempStrings()
    {

        smvTemplates = "";
        smvMultiLevelTempletes = "";
        smvMainTemplatesWhenMerge = "";
        smvSubPolTemplatesWhenMerge = "";
        ADD_VAR = "";
        ADD_State_VAR = "";
        ADD_FunctionCallInVAR = "";
        ADD_COMBINATION_POLICY = "";
        ADD_RULES = "";
        ADD_Ordered_PERMITDENY_RULES = "";
        ADD_PERMIT_RULES = "";
        ADD_DENY_RULES = "";
        ADD_STATE_ASSIGNS = "";
        ADD_WORKFLOW_STATE_ASSIGNS = "";
        ADD_PROPERTY = "";
        ADD_REACHABILITY_INVARIANTS = "";
        ADD_FUNCTION = "";
        SUBJECTS = "";
        ACTIONS = "";
        RESOURCES = "";
        ENVIRONMENTS = "";
        SubPol_ARGUMENTS = "";
        WorkFlowcount = 1;
        MultiLevelcount = 1;
        rulecount = 1;
    }

    public static boolean NUSMVGeneration(String type, List polSet, ACPTFrame frame, JTree modelTree, JTree targetTree,
            JTree propertyTree, JTree combineTree, File selected, String outResult, String NuSMV,
            List defaultDenyEnabledpolSet)
    {
        propertyRootNode = GenericTreeUtil.getRootNode(propertyTree);

        initTempStrings();
        setTemplate();
        setAttributes(targetTree);

        if (selected == null)
        {
            frame.logPanel.append("No file is selected for NuSMV generation ");
            return false;
        }
        // FIXME
        PrintStream output = null;
        String outputFileName = selected.getAbsoluteFile().toString();
        try
        {
            output = new PrintStream(new FileOutputStream(outputFileName));
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }

        parseProperty();

        // parseSelectedPolicy
        String DenyDecisionSet = "";
        String PermitDecisionSet = "";
        String NonApplicableDecisionSet = "";
        String FirstApplicableDecisionSet = "";

        if (type.equals("Combination"))
        {

            boolean hasWorkflowModel = false;

            // if statement

            DefaultMutableTreeNode combinetreeRootNode =
                    GenericTreeUtil.getFirstMatchingNode(combineTree, GenericInfo.ROOT, 0);
            String algo = ((GenericInfo) combinetreeRootNode.getUserObject()).getCombiningAlgorithm();

            // FIXME
            if (algo.equals("First-applicable"))
            {

                for (int i = 0; i < polSet.size(); i++)
                {
                    String policyModelTypeName = polSet.get(i).toString();

                    int index = i + 1;

                    DenyDecisionSet += "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    PermitDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;
                    NonApplicableDecisionSet +=
                            "\tdecision" + index + " = Non-applicable:	Non-applicable	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;

                    ADD_VAR += newline + "\tdecision" + index + " :\t{ Pending , Permit, Deny, Non-applicable}\t;\t";

                    boolean hasResult = parsePolicyModelCombination(index, modelTree, targetTree, policyModelTypeName,
                            defaultDenyEnabledpolSet);

                    String TempPolicy = " init (decision" + index + ") := Pending ;" + newline;
                    TempPolicy += " next (decision" + index + ") := case" + newline;
                    ;
                    TempPolicy += ADD_RULES;
                    if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                    {
                        TempPolicy += "\t\t1\t\t: Deny;\r\n";
                    }
                    else
                    {
                        TempPolicy += "\t\t1\t\t: Non-applicable;\r\n";
                    }
                    TempPolicy += "\t\tesac;" + newline;

                    ADD_COMBINATION_POLICY += TempPolicy;
                    // System.err.println(ADD_COMBINATION_POLICY);
                    ADD_RULES = "";

                    if (hasResult == false)
                    {
                        return false;
                    }

                    // check whether combinations includes workflow model
                    String model[] = policyModelTypeName.split("#");
                    String modeltype = model[0];
                    if (modeltype.equals(ModelInfo.WORKFLOW))
                    {
                        hasWorkflowModel = true;
                    }
                }

                ADD_RULES = FirstApplicableDecisionSet;

                output.println(getNuSMV(smvTemplates));

                output.close();

                frame.logPanel.append("NuSMV file is created");
                frame.logPanel.append("NuSMV file : " + outputFileName.toString());
                ;
                frame.logPanel.append("run NuSMV verification....");

                PrintStream batch = null;

                try
                {
                    batch = new PrintStream(new FileOutputStream(NUSMVRunBatchfile));
                }
                catch (FileNotFoundException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                batch.println(
                        "Echo *** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***"
                                + " >> " + outResult);

                batch.println("\"" + NuSMV + "\"" + " " + "\"" + outputFileName + "\"" + " >> " + outResult + " 2>&1");

                // String endComment = "*** end of NuSMV verification";
                batch.println("Echo " + endComment + " >> " + outResult);

                // batch.println("\""+NuSMV + "\""+ " " + "\"" + outputFileName
                // + "\"");

                batch.println("EXIT");
                batch.close();

                try
                {
                    Process p = Runtime.getRuntime().exec("cmd /C start " + NUSMVRunBatchfile);

                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            else if (algo.equals("Permit-overrides"))
            {

                for (int i = 0; i < polSet.size(); i++)
                {
                    String policyModelTypeName = polSet.get(i).toString();

                    int index = i + 1;

                    DenyDecisionSet += "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    PermitDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;
                    NonApplicableDecisionSet +=
                            "\tdecision" + index + " = Non-applicable:	Non-applicable	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;

                    ADD_VAR += newline + "\tdecision" + index + " :\t{ Pending , Permit, Deny, Non-applicable}\t;\t";

                    boolean hasResult = parsePolicyModelCombination(index, modelTree, targetTree, policyModelTypeName,
                            defaultDenyEnabledpolSet);

                    String TempPolicy = " init (decision" + index + ") := Pending ;" + newline;
                    TempPolicy += " next (decision" + index + ") := case" + newline;
                    ;
                    TempPolicy += ADD_RULES;
                    if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                    {
                        TempPolicy += "\t\t1\t\t: Deny;\r\n";
                    }
                    else
                    {
                        TempPolicy += "\t\t1\t\t: Non-applicable;\r\n";
                    }
                    TempPolicy += "\t\tesac;" + newline;

                    ADD_COMBINATION_POLICY += TempPolicy;
                    // System.err.println(ADD_COMBINATION_POLICY);
                    ADD_RULES = "";

                    if (hasResult == false)
                    {
                        return false;
                    }

                    // check whether combinations includes workflow model
                    String model[] = policyModelTypeName.split("#");
                    String modeltype = model[0];
                    if (modeltype.equals(ModelInfo.WORKFLOW))
                    {
                        hasWorkflowModel = true;
                    }
                }

                ADD_RULES = PermitDecisionSet;
                ADD_RULES += DenyDecisionSet;
                ADD_RULES += NonApplicableDecisionSet;

                output.println(getNuSMV(smvTemplates));

                output.close();

                frame.logPanel.append("NuSMV file is created");
                frame.logPanel.append("NuSMV file : " + outputFileName.toString());
                frame.logPanel.append("run NuSMV verification....");

                PrintStream batch = null;

                try
                {
                    batch = new PrintStream(new FileOutputStream(NUSMVRunBatchfile));
                }
                catch (FileNotFoundException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                batch.println(
                        "Echo *** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***"
                                + " >> " + outResult);

                batch.println("\"" + NuSMV + "\"" + " " + "\"" + outputFileName + "\"" + " >> " + outResult + " 2>&1");

                // String endComment = "*** end of NuSMV verification";
                batch.println("Echo " + endComment + " >> " + outResult);

                // batch.println("\""+NuSMV + "\""+ " " + "\"" + outputFileName
                // + "\"");

                batch.println("EXIT");
                batch.close();

                try
                {
                    Process p = Runtime.getRuntime().exec("cmd /C start " + NUSMVRunBatchfile);

                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if (algo.equals("Deny-overrides"))
            {

                for (int i = 0; i < polSet.size(); i++)
                {
                    String policyModelTypeName = polSet.get(i).toString();

                    int index = i + 1;

                    DenyDecisionSet += "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    PermitDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;
                    NonApplicableDecisionSet +=
                            "\tdecision" + index + " = Non-applicable:	Non-applicable	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;

                    ADD_VAR += newline + "\tdecision" + index + " :\t{ Pending , Permit, Deny, Non-applicable}\t;\t";

                    boolean hasResult = parsePolicyModelCombination(index, modelTree, targetTree, policyModelTypeName,
                            defaultDenyEnabledpolSet);

                    String TempPolicy = " init (decision" + index + ") := Pending ;" + newline;
                    TempPolicy += " next (decision" + index + ") := case" + newline;
                    ;
                    TempPolicy += ADD_RULES;
                    if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                    {
                        TempPolicy += "\t\t1\t\t: Deny;\r\n";
                    }
                    else
                    {
                        TempPolicy += "\t\t1\t\t: Non-applicable;\r\n";
                    }
                    TempPolicy += "\t\tesac;" + newline;

                    ADD_COMBINATION_POLICY += TempPolicy;
                    // System.err.println(ADD_COMBINATION_POLICY);
                    ADD_RULES = "";

                    if (hasResult == false)
                    {
                        return false;
                    }

                    // check whether combinations includes workflow model
                    String model[] = policyModelTypeName.split("#");
                    String modeltype = model[0];
                    if (modeltype.equals(ModelInfo.WORKFLOW))
                    {
                        hasWorkflowModel = true;
                    }
                }

                ADD_RULES = DenyDecisionSet;
                ADD_RULES += PermitDecisionSet;
                ADD_RULES += NonApplicableDecisionSet;

                output.println(getNuSMV(smvTemplates));

                output.close();

                frame.logPanel.append("NuSMV file is created");
                frame.logPanel.append("NuSMV file : " + outputFileName.toString());
                frame.logPanel.append("run NuSMV verification....");

                PrintStream batch = null;

                try
                {
                    batch = new PrintStream(new FileOutputStream(NUSMVRunBatchfile));
                }
                catch (FileNotFoundException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                batch.println(
                        "Echo *** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***"
                                + " >> " + outResult);

                batch.println("\"" + NuSMV + "\"" + " " + "\"" + outputFileName + "\"" + " >> " + outResult + " 2>&1");

                // String endComment = "*** end of NuSMV verification";
                batch.println("Echo " + endComment + " >> " + outResult);

                // batch.println("\""+NuSMV + "\""+ " " + "\"" + outputFileName
                // + "\"");

                batch.println("EXIT");
                batch.close();

                try
                {
                    Process p = Runtime.getRuntime().exec("cmd /C start " + NUSMVRunBatchfile);

                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            /**
             * This bracket implements weak-consensus combination algorithm.
             *
             * @author Ang Li(University of Arkansas), Qinghua Li(University of
             *         Arkansas) Vincent C. Hu(NIST) Jia Di (University of
             *         Arkansas)
             * @data 05/11/2015
             */
            else if (algo.equals("Weak-consensus"))
            {
                int count = 0;// calculate the number of "true" in the result
                              // file
                String add_var = ADD_VAR;

                for (int i = 0; i < polSet.size(); i++)
                {
                    String policyModelTypeName = polSet.get(i).toString();

                    int index = i + 1;

                    FirstApplicableDecisionSet = "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;

                    ADD_VAR += newline + "\tdecision" + index + " :\t{ Pending , Permit, Deny, Non-applicable}\t;\t";

                    ADD_RULES = "";

                    boolean hasResult = parsePolicyModelCombination(index, modelTree, targetTree, policyModelTypeName,
                            defaultDenyEnabledpolSet);

                    String TempPolicy = " init (decision" + index + ") := Pending ;" + newline;
                    TempPolicy += " next (decision" + index + ") := case" + newline;

                    TempPolicy += ADD_RULES;
                    if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                    {
                        TempPolicy += "\t\t1\t\t: Deny;\r\n";
                    }
                    else
                    {
                        TempPolicy += "\t\t1\t\t: Non-applicable;\r\n";
                    }
                    TempPolicy += "\t\tesac;" + newline;

                    ADD_COMBINATION_POLICY = TempPolicy;
                    // System.err.println(ADD_COMBINATION_POLICY);
                    ADD_RULES = "";

                    if (hasResult == false)
                    {
                        return false;
                    }

                    // check whether combinations includes workflow model
                    String model[] = policyModelTypeName.split("#");
                    String modeltype = model[0];
                    if (modeltype.equals(ModelInfo.WORKFLOW))
                    {
                        hasWorkflowModel = true;
                    }

                    ADD_RULES = FirstApplicableDecisionSet;

                    int randomInt = (new Random()).nextInt();
                    String temSource = "nu-src-" + randomInt + ".smv";// temporary
                                                                      // smv
                                                                      // file
                    String temResult = "results\\" + "nu-out-" + randomInt + ".txt";// temporary
                                                                                    // result
                                                                                    // file
                    File temSourceFile = new File("results\\" + temSource);

                    PrintStream tmpOutput = null;
                    String tmpOutputFileName = temSourceFile.getAbsoluteFile().toString();
                    try
                    {
                        tmpOutput = new PrintStream(new FileOutputStream(tmpOutputFileName));
                    }
                    catch (FileNotFoundException e)
                    {
                        e.getStackTrace();
                    }

                    tmpOutput.println(getNuSMV(smvTemplates));
                    tmpOutput.close();

                    ADD_VAR = add_var;

                    frame.logPanel.append("NuSMV file is created");
                    frame.logPanel.append("NuSMV file : " + tmpOutputFileName.toString());
                    frame.logPanel.append("run NuSMV verification....");

                    PrintStream batch = null;

                    try
                    {
                        batch = new PrintStream(new FileOutputStream(NUSMVRunBatchfile));
                    }
                    catch (FileNotFoundException e1)
                    {
                        e1.printStackTrace();
                    }

                    batch.println(
                            "Echo *** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                    + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                    + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***"
                                    + " >> " + temResult);

                    batch.println(
                            "\"" + NuSMV + "\"" + " " + "\"" + tmpOutputFileName + "\"" + " >> " + temResult + " 2>&1");

                    // String endComment = "*** end of NuSMV verification";
                    batch.println("Echo " + endComment + " >> " + temResult);

                    // batch.println("\""+NuSMV + "\""+ " " + "\"" +
                    // outputFileName + "\"");

                    batch.println("EXIT");
                    batch.close();
                    // printFile(temSourceFile);

                    try
                    {
                        Process p = Runtime.getRuntime().exec("cmd /C start " + NUSMVRunBatchfile);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    if (resultCount(new File(temResult)) == true)
                    {
                        count += 1;
                    }

                }

                // System.out.println(count);

                if (count == polSet.size())
                {

                    System.out.println("true");
                    PrintWriter out = null;
                    try
                    {
                        out = new PrintWriter(new OutputStreamWriter(
                                new BufferedOutputStream(new FileOutputStream(outResult)), "UTF-8"));

                        out.println(
                                "*** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                        + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                        + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***");

                        out.println("*** This is NuSMV 2.4.3 (compiled on Tue May 22 14:08:54 UTC 2007)");
                        out.println("*** For more information on NuSMV see <http://nusmv.irst.itc.it>");
                        out.println("*** or email to <nusmv-users@irst.itc.it>.");
                        out.println("*** Please report bugs to <nusmv@irst.itc.it>.");
                        out.println("*** This version of NuSMV is linked to the MiniSat SAT solver.");
                        out.println("*** See http://www.cs.chalmers.se/Cs/Research/FormalMethods/MiniSat");
                        out.println("*** Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson ");
                        out.println(ADD_PROPERTY + " is true");
                        out.println(endComment);

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (out != null)
                        {
                            out.flush();
                            out.close();
                        }
                    }

                }
                else
                {
                    System.out.println("false");

                    PrintWriter out = null;
                    try
                    {
                        out = new PrintWriter(new OutputStreamWriter(
                                new BufferedOutputStream(new FileOutputStream(outResult)), "UTF-8"));

                        out.println(
                                "*** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                        + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                        + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***");

                        out.println("*** This is NuSMV 2.4.3 (compiled on Tue May 22 14:08:54 UTC 2007)");
                        out.println("*** For more information on NuSMV see <http://nusmv.irst.itc.it>");
                        out.println("*** or email to <nusmv-users@irst.itc.it>.");
                        out.println("*** Please report bugs to <nusmv@irst.itc.it>.");
                        out.println("*** This version of NuSMV is linked to the MiniSat SAT solver.");
                        out.println("*** See http://www.cs.chalmers.se/Cs/Research/FormalMethods/MiniSat");
                        out.println("*** Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson ");
                        out.println(ADD_PROPERTY + " is false");
                        out.println(endComment);

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (out != null)
                        {
                            out.flush();
                            out.close();
                        }
                    }

                }
            }
            /**
             * This bracket implements strong-consensus combination algorithm.
             *
             * @author Ang Li(University of Arkansas), Qinghua Li(University of
             *         Arkansas) Vincent C. Hu(NIST) Jia Di (University of
             *         Arkansas)
             * @data 05/11/2015
             */
            else if (algo.equals("Strong-consensus"))
            {
                int count = 0;// calculate the number of "true" in the result
                              // file
                String add_var = ADD_VAR;

                for (int i = 0; i < polSet.size(); i++)
                {
                    String policyModelTypeName = polSet.get(i).toString();

                    int index = i + 1;

                    FirstApplicableDecisionSet = "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;

                    ADD_VAR += newline + "\tdecision" + index + " :\t{ Pending , Permit, Deny, Non-applicable}\t;\t";

                    ADD_RULES = "";

                    boolean hasResult = parsePolicyModelCombination(index, modelTree, targetTree, policyModelTypeName,
                            defaultDenyEnabledpolSet);

                    String TempPolicy = " init (decision" + index + ") := Pending ;" + newline;
                    TempPolicy += " next (decision" + index + ") := case" + newline;

                    TempPolicy += ADD_RULES;
                    if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                    {
                        TempPolicy += "\t\t1\t\t: Deny;\r\n";
                    }
                    else
                    {
                        TempPolicy += "\t\t1\t\t: Non-applicable;\r\n";
                    }
                    TempPolicy += "\t\tesac;" + newline;

                    ADD_COMBINATION_POLICY = TempPolicy;
                    // System.err.println(ADD_COMBINATION_POLICY);
                    ADD_RULES = "";

                    if (hasResult == false)
                    {
                        return false;
                    }

                    // check whether combinations includes workflow model
                    String model[] = policyModelTypeName.split("#");
                    String modeltype = model[0];
                    if (modeltype.equals(ModelInfo.WORKFLOW))
                    {
                        hasWorkflowModel = true;
                    }

                    ADD_RULES = FirstApplicableDecisionSet;

                    int randomInt = (new Random()).nextInt();
                    String temSource = "nu-src-" + randomInt + ".smv";// temporary
                                                                      // smv
                                                                      // file
                    String temResult = "results\\" + "nu-out-" + randomInt + ".txt";// temporary
                                                                                    // result
                                                                                    // file
                    File temSourceFile = new File("results\\" + temSource);

                    PrintStream tmpOutput = null;
                    String tmpOutputFileName = temSourceFile.getAbsoluteFile().toString();
                    try
                    {
                        tmpOutput = new PrintStream(new FileOutputStream(tmpOutputFileName));
                    }
                    catch (FileNotFoundException e)
                    {
                        e.getStackTrace();
                    }

                    tmpOutput.println(getNuSMV(smvTemplates));
                    tmpOutput.close();

                    ADD_VAR = add_var;

                    frame.logPanel.append("NuSMV file is created");
                    frame.logPanel.append("NuSMV file : " + tmpOutputFileName.toString());
                    frame.logPanel.append("run NuSMV verification....");

                    PrintStream batch = null;

                    try
                    {
                        batch = new PrintStream(new FileOutputStream(NUSMVRunBatchfile));
                    }
                    catch (FileNotFoundException e1)
                    {
                        e1.printStackTrace();
                    }

                    batch.println(
                            "Echo *** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                    + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                    + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***"
                                    + " >> " + temResult);

                    batch.println(
                            "\"" + NuSMV + "\"" + " " + "\"" + tmpOutputFileName + "\"" + " >> " + temResult + " 2>&1");

                    // String endComment = "*** end of NuSMV verification";
                    batch.println("Echo " + endComment + " >> " + temResult);

                    // batch.println("\""+NuSMV + "\""+ " " + "\"" +
                    // outputFileName + "\"");

                    batch.println("EXIT");
                    batch.close();
                    // printFile(temSourceFile);

                    try
                    {
                        Process p = Runtime.getRuntime().exec("cmd /C start " + NUSMVRunBatchfile);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    if (resultCount(new File(temResult)) == true)
                    {
                        count += 1;
                    }

                }

                // System.out.println(count);

                if (count == polSet.size())
                {

                    System.out.println("true");
                    PrintWriter out = null;
                    try
                    {
                        out = new PrintWriter(new OutputStreamWriter(
                                new BufferedOutputStream(new FileOutputStream(outResult)), "UTF-8"));

                        out.println(
                                "*** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                        + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                        + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***");

                        out.println("*** This is NuSMV 2.4.3 (compiled on Tue May 22 14:08:54 UTC 2007)");
                        out.println("*** For more information on NuSMV see <http://nusmv.irst.itc.it>");
                        out.println("*** or email to <nusmv-users@irst.itc.it>.");
                        out.println("*** Please report bugs to <nusmv@irst.itc.it>.");
                        out.println("*** This version of NuSMV is linked to the MiniSat SAT solver.");
                        out.println("*** See http://www.cs.chalmers.se/Cs/Research/FormalMethods/MiniSat");
                        out.println("*** Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson ");
                        out.println(ADD_PROPERTY + " is true");
                        out.println(endComment);

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (out != null)
                        {
                            out.flush();
                            out.close();
                        }
                    }

                }
                else
                {
                    System.out.println("false");

                    PrintWriter out = null;
                    try
                    {
                        out = new PrintWriter(new OutputStreamWriter(
                                new BufferedOutputStream(new FileOutputStream(outResult)), "UTF-8"));

                        out.println(
                                "*** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                        + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                        + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***");

                        out.println("*** This is NuSMV 2.4.3 (compiled on Tue May 22 14:08:54 UTC 2007)");
                        out.println("*** For more information on NuSMV see <http://nusmv.irst.itc.it>");
                        out.println("*** or email to <nusmv-users@irst.itc.it>.");
                        out.println("*** Please report bugs to <nusmv@irst.itc.it>.");
                        out.println("*** This version of NuSMV is linked to the MiniSat SAT solver.");
                        out.println("*** See http://www.cs.chalmers.se/Cs/Research/FormalMethods/MiniSat");
                        out.println("*** Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson ");
                        out.println(ADD_PROPERTY + " is false");
                        out.println(endComment);

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (out != null)
                        {
                            out.flush();
                            out.close();
                        }
                    }

                }

            }
            /**
             * This bracket implements weak-majority combination algorithm.
             *
             * @author Ang Li(University of Arkansas), Qinghua Li(University of
             *         Arkansas) Vincent C. Hu(NIST) Jia Di (University of
             *         Arkansas)
             * @data 05/11/2015
             */
            else if (algo.equals("Weak-majority"))
            {
                int count = 0;// calculate the number of "true" in the result
                              // file
                String add_var = ADD_VAR;

                for (int i = 0; i < polSet.size(); i++)
                {
                    String policyModelTypeName = polSet.get(i).toString();

                    int index = i + 1;

                    FirstApplicableDecisionSet = "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;

                    ADD_VAR += newline + "\tdecision" + index + " :\t{ Pending , Permit, Deny, Non-applicable}\t;\t";

                    ADD_RULES = "";

                    boolean hasResult = parsePolicyModelCombination(index, modelTree, targetTree, policyModelTypeName,
                            defaultDenyEnabledpolSet);

                    String TempPolicy = " init (decision" + index + ") := Pending ;" + newline;
                    TempPolicy += " next (decision" + index + ") := case" + newline;

                    TempPolicy += ADD_RULES;
                    if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                    {
                        TempPolicy += "\t\t1\t\t: Deny;\r\n";
                    }
                    else
                    {
                        TempPolicy += "\t\t1\t\t: Non-applicable;\r\n";
                    }
                    TempPolicy += "\t\tesac;" + newline;

                    ADD_COMBINATION_POLICY = TempPolicy;
                    // System.err.println(ADD_COMBINATION_POLICY);
                    ADD_RULES = "";

                    if (hasResult == false)
                    {
                        return false;
                    }

                    // check whether combinations includes workflow model
                    String model[] = policyModelTypeName.split("#");
                    String modeltype = model[0];
                    if (modeltype.equals(ModelInfo.WORKFLOW))
                    {
                        hasWorkflowModel = true;
                    }

                    ADD_RULES = FirstApplicableDecisionSet;

                    int randomInt = (new Random()).nextInt();
                    String temSource = "nu-src-" + randomInt + ".smv";// temporary
                                                                      // smv
                                                                      // file
                    String temResult = "results\\" + "nu-out-" + randomInt + ".txt";// temporary
                                                                                    // result
                                                                                    // file
                    File temSourceFile = new File("results\\" + temSource);

                    PrintStream tmpOutput = null;
                    String tmpOutputFileName = temSourceFile.getAbsoluteFile().toString();
                    try
                    {
                        tmpOutput = new PrintStream(new FileOutputStream(tmpOutputFileName));
                    }
                    catch (FileNotFoundException e)
                    {
                        e.getStackTrace();
                    }

                    tmpOutput.println(getNuSMV(smvTemplates));
                    tmpOutput.close();

                    ADD_VAR = add_var;

                    frame.logPanel.append("NuSMV file is created");
                    frame.logPanel.append("NuSMV file : " + tmpOutputFileName.toString());
                    frame.logPanel.append("run NuSMV verification....");

                    PrintStream batch = null;

                    try
                    {
                        batch = new PrintStream(new FileOutputStream(NUSMVRunBatchfile));
                    }
                    catch (FileNotFoundException e1)
                    {
                        e1.printStackTrace();
                    }

                    batch.println(
                            "Echo *** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                    + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                    + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***"
                                    + " >> " + temResult);

                    batch.println(
                            "\"" + NuSMV + "\"" + " " + "\"" + tmpOutputFileName + "\"" + " >> " + temResult + " 2>&1");

                    // String endComment = "*** end of NuSMV verification";
                    batch.println("Echo " + endComment + " >> " + temResult);

                    // batch.println("\""+NuSMV + "\""+ " " + "\"" +
                    // outputFileName + "\"");

                    batch.println("EXIT");
                    batch.close();
                    // printFile(temSourceFile);

                    try
                    {
                        Process p = Runtime.getRuntime().exec("cmd /C start " + NUSMVRunBatchfile);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    if (resultCount(new File(temResult)) == true)
                    {
                        count += 1;
                    }

                }

                // System.out.println(count);

                if (count > (polSet.size() - count))
                {

                    System.out.println("true");
                    PrintWriter out = null;
                    try
                    {
                        out = new PrintWriter(new OutputStreamWriter(
                                new BufferedOutputStream(new FileOutputStream(outResult)), "UTF-8"));

                        out.println(
                                "*** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                        + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                        + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***");

                        out.println("*** This is NuSMV 2.4.3 (compiled on Tue May 22 14:08:54 UTC 2007)");
                        out.println("*** For more information on NuSMV see <http://nusmv.irst.itc.it>");
                        out.println("*** or email to <nusmv-users@irst.itc.it>.");
                        out.println("*** Please report bugs to <nusmv@irst.itc.it>.");
                        out.println("*** This version of NuSMV is linked to the MiniSat SAT solver.");
                        out.println("*** See http://www.cs.chalmers.se/Cs/Research/FormalMethods/MiniSat");
                        out.println("*** Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson ");
                        out.println(ADD_PROPERTY + " is true");
                        out.println(endComment);

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (out != null)
                        {
                            out.flush();
                            out.close();
                        }
                    }

                }
                else
                {
                    System.out.println("false");

                    PrintWriter out = null;
                    try
                    {
                        out = new PrintWriter(new OutputStreamWriter(
                                new BufferedOutputStream(new FileOutputStream(outResult)), "UTF-8"));

                        out.println(
                                "*** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                        + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                        + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***");

                        out.println("*** This is NuSMV 2.4.3 (compiled on Tue May 22 14:08:54 UTC 2007)");
                        out.println("*** For more information on NuSMV see <http://nusmv.irst.itc.it>");
                        out.println("*** or email to <nusmv-users@irst.itc.it>.");
                        out.println("*** Please report bugs to <nusmv@irst.itc.it>.");
                        out.println("*** This version of NuSMV is linked to the MiniSat SAT solver.");
                        out.println("*** See http://www.cs.chalmers.se/Cs/Research/FormalMethods/MiniSat");
                        out.println("*** Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson ");
                        out.println(ADD_PROPERTY + " is false");
                        out.println(endComment);

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (out != null)
                        {
                            out.flush();
                            out.close();
                        }
                    }

                }

            }
            /**
             * This bracket implements strong-majority combination algorithm.
             *
             * @author Ang Li(University of Arkansas), Qinghua Li(University of
             *         Arkansas) Vincent C. Hu(NIST) Jia Di (University of
             *         Arkansas)
             * @data 05/11/2015
             */
            else if (algo.equals("Strong-majority"))
            {
                int count = 0;// calculate the number of "true" in the result
                              // file
                String add_var = ADD_VAR;

                for (int i = 0; i < polSet.size(); i++)
                {
                    String policyModelTypeName = polSet.get(i).toString();

                    int index = i + 1;

                    FirstApplicableDecisionSet = "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;

                    ADD_VAR += newline + "\tdecision" + index + " :\t{ Pending , Permit, Deny, Non-applicable}\t;\t";

                    ADD_RULES = "";

                    boolean hasResult = parsePolicyModelCombination(index, modelTree, targetTree, policyModelTypeName,
                            defaultDenyEnabledpolSet);

                    String TempPolicy = " init (decision" + index + ") := Pending ;" + newline;
                    TempPolicy += " next (decision" + index + ") := case" + newline;

                    TempPolicy += ADD_RULES;
                    if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                    {
                        TempPolicy += "\t\t1\t\t: Deny;\r\n";
                    }
                    else
                    {
                        TempPolicy += "\t\t1\t\t: Non-applicable;\r\n";
                    }
                    TempPolicy += "\t\tesac;" + newline;

                    ADD_COMBINATION_POLICY = TempPolicy;
                    // System.err.println(ADD_COMBINATION_POLICY);
                    ADD_RULES = "";

                    if (hasResult == false)
                    {
                        return false;
                    }

                    // check whether combinations includes workflow model
                    String model[] = policyModelTypeName.split("#");
                    String modeltype = model[0];
                    if (modeltype.equals(ModelInfo.WORKFLOW))
                    {
                        hasWorkflowModel = true;
                    }

                    ADD_RULES = FirstApplicableDecisionSet;

                    int randomInt = (new Random()).nextInt();
                    String temSource = "nu-src-" + randomInt + ".smv";// temporary
                                                                      // smv
                                                                      // file
                    String temResult = "results\\" + "nu-out-" + randomInt + ".txt";// temporary
                                                                                    // result
                                                                                    // file
                    File temSourceFile = new File("results\\" + temSource);

                    PrintStream tmpOutput = null;
                    String tmpOutputFileName = temSourceFile.getAbsoluteFile().toString();
                    try
                    {
                        tmpOutput = new PrintStream(new FileOutputStream(tmpOutputFileName));
                    }
                    catch (FileNotFoundException e)
                    {
                        e.getStackTrace();
                    }

                    tmpOutput.println(getNuSMV(smvTemplates));
                    tmpOutput.close();

                    ADD_VAR = add_var;

                    frame.logPanel.append("NuSMV file is created");
                    frame.logPanel.append("NuSMV file : " + tmpOutputFileName.toString());
                    frame.logPanel.append("run NuSMV verification....");

                    PrintStream batch = null;

                    try
                    {
                        batch = new PrintStream(new FileOutputStream(NUSMVRunBatchfile));
                    }
                    catch (FileNotFoundException e1)
                    {
                        e1.printStackTrace();
                    }

                    batch.println(
                            "Echo *** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                    + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                    + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***"
                                    + " >> " + temResult);

                    batch.println(
                            "\"" + NuSMV + "\"" + " " + "\"" + tmpOutputFileName + "\"" + " >> " + temResult + " 2>&1");

                    // String endComment = "*** end of NuSMV verification";
                    batch.println("Echo " + endComment + " >> " + temResult);

                    // batch.println("\""+NuSMV + "\""+ " " + "\"" +
                    // outputFileName + "\"");

                    batch.println("EXIT");
                    batch.close();
                    // printFile(temSourceFile);

                    try
                    {
                        Process p = Runtime.getRuntime().exec("cmd /C start " + NUSMVRunBatchfile);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    if (resultCount(new File(temResult)) == true)
                    {
                        count += 1;
                    }

                }

                // System.out.println(count);

                if (count >= ((polSet.size() / 2) + 1))
                {

                    System.out.println("true");
                    PrintWriter out = null;
                    try
                    {
                        out = new PrintWriter(new OutputStreamWriter(
                                new BufferedOutputStream(new FileOutputStream(outResult)), "UTF-8"));

                        out.println(
                                "*** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                        + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                        + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***");

                        out.println("*** This is NuSMV 2.4.3 (compiled on Tue May 22 14:08:54 UTC 2007)");
                        out.println("*** For more information on NuSMV see <http://nusmv.irst.itc.it>");
                        out.println("*** or email to <nusmv-users@irst.itc.it>.");
                        out.println("*** Please report bugs to <nusmv@irst.itc.it>.");
                        out.println("*** This version of NuSMV is linked to the MiniSat SAT solver.");
                        out.println("*** See http://www.cs.chalmers.se/Cs/Research/FormalMethods/MiniSat");
                        out.println("*** Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson ");
                        out.println(ADD_PROPERTY + " is true");
                        out.println(endComment);

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (out != null)
                        {
                            out.flush();
                            out.close();
                        }
                    }

                }
                else
                {
                    System.out.println("false");

                    PrintWriter out = null;
                    try
                    {
                        out = new PrintWriter(new OutputStreamWriter(
                                new BufferedOutputStream(new FileOutputStream(outResult)), "UTF-8"));

                        out.println(
                                "*** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                        + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                        + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***");

                        out.println("*** This is NuSMV 2.4.3 (compiled on Tue May 22 14:08:54 UTC 2007)");
                        out.println("*** For more information on NuSMV see <http://nusmv.irst.itc.it>");
                        out.println("*** or email to <nusmv-users@irst.itc.it>.");
                        out.println("*** Please report bugs to <nusmv@irst.itc.it>.");
                        out.println("*** This version of NuSMV is linked to the MiniSat SAT solver.");
                        out.println("*** See http://www.cs.chalmers.se/Cs/Research/FormalMethods/MiniSat");
                        out.println("*** Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson ");
                        out.println(ADD_PROPERTY + " is false");
                        out.println(endComment);

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (out != null)
                        {
                            out.flush();
                            out.close();
                        }
                    }

                }

            }
            /**
             * This method implements super-majority-permit combination
             * algorithm.
             *
             * @author Ang Li(University of Arkansas), Qinghua Li(University of
             *         Arkansas) Vincent C. Hu(NIST) Jia Di (University of
             *         Arkansas)
             * @data 05/11/2015
             */
            else if (algo.equals("Super-majority-permit"))
            {

                int count = 0;// calculate the number of "true" in the result
                              // file
                String add_var = ADD_VAR;

                for (int i = 0; i < polSet.size(); i++)
                {
                    String policyModelTypeName = polSet.get(i).toString();

                    int index = i + 1;

                    FirstApplicableDecisionSet = "\tdecision" + index + " = Deny:	Deny	;" + newline;
                    FirstApplicableDecisionSet += "\tdecision" + index + " = Permit:	Permit	;" + newline;

                    ADD_VAR += newline + "\tdecision" + index + " :\t{ Pending , Permit, Deny, Non-applicable}\t;\t";

                    ADD_RULES = "";

                    boolean hasResult = parsePolicyModelCombination(index, modelTree, targetTree, policyModelTypeName,
                            defaultDenyEnabledpolSet);

                    String TempPolicy = " init (decision" + index + ") := Pending ;" + newline;
                    TempPolicy += " next (decision" + index + ") := case" + newline;

                    TempPolicy += ADD_RULES;
                    if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                    {
                        TempPolicy += "\t\t1\t\t: Deny;\r\n";
                    }
                    else
                    {
                        TempPolicy += "\t\t1\t\t: Non-applicable;\r\n";
                    }
                    TempPolicy += "\t\tesac;" + newline;

                    ADD_COMBINATION_POLICY = TempPolicy;
                    // System.err.println(ADD_COMBINATION_POLICY);
                    ADD_RULES = "";

                    if (hasResult == false)
                    {
                        return false;
                    }

                    // check whether combinations includes workflow model
                    String model[] = policyModelTypeName.split("#");
                    String modeltype = model[0];
                    if (modeltype.equals(ModelInfo.WORKFLOW))
                    {
                        hasWorkflowModel = true;
                    }

                    ADD_RULES = FirstApplicableDecisionSet;

                    int randomInt = (new Random()).nextInt();
                    String temSource = "nu-src-" + randomInt + ".smv";// temporary
                                                                      // smv
                                                                      // file
                    String temResult = "results\\" + "nu-out-" + randomInt + ".txt";// temporary
                                                                                    // result
                                                                                    // file
                    File temSourceFile = new File("results\\" + temSource);

                    PrintStream tmpOutput = null;
                    String tmpOutputFileName = temSourceFile.getAbsoluteFile().toString();
                    try
                    {
                        tmpOutput = new PrintStream(new FileOutputStream(tmpOutputFileName));
                    }
                    catch (FileNotFoundException e)
                    {
                        e.getStackTrace();
                    }

                    tmpOutput.println(getNuSMV(smvTemplates));
                    tmpOutput.close();

                    ADD_VAR = add_var;

                    frame.logPanel.append("NuSMV file is created");
                    frame.logPanel.append("NuSMV file : " + tmpOutputFileName.toString());
                    frame.logPanel.append("run NuSMV verification....");

                    PrintStream batch = null;

                    try
                    {
                        batch = new PrintStream(new FileOutputStream(NUSMVRunBatchfile));
                    }
                    catch (FileNotFoundException e1)
                    {
                        e1.printStackTrace();
                    }

                    batch.println(
                            "Echo *** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                    + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                    + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***"
                                    + " >> " + temResult);

                    batch.println(
                            "\"" + NuSMV + "\"" + " " + "\"" + tmpOutputFileName + "\"" + " >> " + temResult + " 2>&1");

                    // String endComment = "*** end of NuSMV verification";
                    batch.println("Echo " + endComment + " >> " + temResult);

                    // batch.println("\""+NuSMV + "\""+ " " + "\"" +
                    // outputFileName + "\"");

                    batch.println("EXIT");
                    batch.close();
                    // printFile(temSourceFile);

                    try
                    {
                        Process p = Runtime.getRuntime().exec("cmd /C start " + NUSMVRunBatchfile);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    try
                    {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    if (resultCount(new File(temResult)) == true)
                    {
                        count += 1;
                    }

                }

                // System.out.println(count);

                if (count >= (((polSet.size() * 2) / 3) + 1))
                {

                    System.out.println("true");
                    PrintWriter out = null;
                    try
                    {
                        out = new PrintWriter(new OutputStreamWriter(
                                new BufferedOutputStream(new FileOutputStream(outResult)), "UTF-8"));

                        out.println(
                                "*** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                        + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                        + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***");

                        out.println("*** This is NuSMV 2.4.3 (compiled on Tue May 22 14:08:54 UTC 2007)");
                        out.println("*** For more information on NuSMV see <http://nusmv.irst.itc.it>");
                        out.println("*** or email to <nusmv-users@irst.itc.it>.");
                        out.println("*** Please report bugs to <nusmv@irst.itc.it>.");
                        out.println("*** This version of NuSMV is linked to the MiniSat SAT solver.");
                        out.println("*** See http://www.cs.chalmers.se/Cs/Research/FormalMethods/MiniSat");
                        out.println("*** Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson ");
                        out.println(ADD_PROPERTY + " is true");
                        out.println(endComment);

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (out != null)
                        {
                            out.flush();
                            out.close();
                        }
                    }

                }
                else
                {
                    System.out.println("false");

                    PrintWriter out = null;
                    try
                    {
                        out = new PrintWriter(new OutputStreamWriter(
                                new BufferedOutputStream(new FileOutputStream(outResult)), "UTF-8"));

                        out.println(
                                "*** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                                        + "For example, undefined subject attributes in a property may cause errors during verification process. "
                                        + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***");

                        out.println("*** This is NuSMV 2.4.3 (compiled on Tue May 22 14:08:54 UTC 2007)");
                        out.println("*** For more information on NuSMV see <http://nusmv.irst.itc.it>");
                        out.println("*** or email to <nusmv-users@irst.itc.it>.");
                        out.println("*** Please report bugs to <nusmv@irst.itc.it>.");
                        out.println("*** This version of NuSMV is linked to the MiniSat SAT solver.");
                        out.println("*** See http://www.cs.chalmers.se/Cs/Research/FormalMethods/MiniSat");
                        out.println("*** Copyright (c) 2003-2005, Niklas Een, Niklas Sorensson ");
                        out.println(ADD_PROPERTY + " is false");
                        out.println(endComment);

                    }
                    catch (UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                    finally
                    {
                        if (out != null)
                        {
                            out.flush();
                            out.close();
                        }
                    }

                }

            }

            if (hasWorkflowModel == false)
            {
                ADD_STATE_ASSIGNS += "\tnext (Process_State) :=\tProcess_State\t;\t" + newline;
            }

            //

        }
        else if (type.equals("Merge"))
        {

            // string subpolicies
            List subPolicies = new ArrayList();
            // System.err.println("To be merged....");

            String ListOfSubPolicyNameNArgs = "";

            // subpolicy generation
            for (int i = 0; i < polSet.size(); i++)
            {

                ADD_RULES = "";
                ADD_Ordered_PERMITDENY_RULES = "";
                ADD_PERMIT_RULES = "";
                ADD_DENY_RULES = "";
                // ADD_State_VAR = "";
                ADD_WORKFLOW_STATE_ASSIGNS = "";
                ADD_FunctionCallInVAR = "";
                ADD_FUNCTION = "";

                parsePolicyModel(modelTree, targetTree, polSet.get(i).toString(), defaultDenyEnabledpolSet);
                String subpolicyName = polSet.get(i).toString().replaceAll("#", "_");
                subpolicyName = subpolicyName.replaceAll(" ", "_");
                String subpolicyNameNArgs = subpolicyName + SubPol_ARGUMENTS;
                ListOfSubPolicyNameNArgs += "\r\n\t" + subpolicyName + " : " + subpolicyName + SubPol_ARGUMENTS + ";";
                String subpolicy = getNuSMV(smvSubPolTemplatesWhenMerge);
                subpolicy = subpolicy.replaceAll("#subpolName#", subpolicyNameNArgs);
                subPolicies.add(subpolicy);

            }

            // print out main
            ADD_VAR += ListOfSubPolicyNameNArgs;
            output.println(getNuSMV(smvMainTemplatesWhenMerge));
            // output.println(SubPol_ARGUMENTS);

            // print out subpolicies
            for (int i = 0; i < subPolicies.size(); i++)
            {
                String policStr = NuSMVattrNameCovert.ConvertToFitNuSMFormat(subPolicies.get(i).toString(),
                        SubjecActionResourceAttrNodeLst);
                output.println(policStr);
            }
            output.close();

            frame.logPanel.append("NuSMV file is created");
            frame.logPanel.append("NuSMV file : " + outputFileName.toString());
            ;
            frame.logPanel.append("run NuSMV verification....");

            PrintStream batch = null;

            try
            {
                batch = new PrintStream(new FileOutputStream(NUSMVRunBatchfile));
            }
            catch (FileNotFoundException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            batch.println(
                    "Echo *** Note that the verification tool may produce no results due to faults (in user-specified models or properties). "
                            + "For example, undefined subject attributes in a property may cause errors during verification process. "
                            + "If you cannot find any results, please check the correctness of models and properties. Besides, users should not define more than one workflow models that may introduce ambiguity of process state.***"
                            + " >> " + outResult);

            batch.println("\"" + NuSMV + "\"" + " " + "\"" + outputFileName + "\"" + " >> " + outResult + " 2>&1");

            // String endComment = "*** end of NuSMV verification";
            batch.println("Echo " + endComment + " >> " + outResult);

            // batch.println("\""+NuSMV + "\""+ " " + "\"" + outputFileName +
            // "\"");

            // batch.println("EXIT");
            batch.close();

            try
            {
                Process p = Runtime.getRuntime().exec("cmd /C start " + NUSMVRunBatchfile);

                // boolean hasRead = false;
                //
                // while (hasRead != true){
                // try {
                // BufferedReader br = new BufferedReader (new InputStreamReader
                // (new FileInputStream (outResult)));
                // String line = br.readLine();
                // while ( line != null){
                //
                // // command
                // frame.logPanel.append(line);
                // line = br.readLine();
                //
                // hasRead = true;
                // }
                // } catch(FileNotFoundException e) {
                // //System.err.println("Error: I could not open " + outResult +
                // "!");
                // } catch(IOException e){
                // System.err.println("Error: File operations on " + outResult +
                // "failed!");
                // }
                // }

                // BufferedReader input2 = new BufferedReader (new
                // InputStreamReader(p.getInputStream()));
                //
                // String line;
                // boolean IsAttribute = false;
                // while ((line = input2.readLine()) != null) {
                //
                // frame.logPanel.append(line);
                //
                // if (line.startsWith("Output file:")){
                // frame.logPanel.append("Input file (created for test
                // generation): ");
                //
                // }
                // if (line.startsWith("After Output File")||
                // line.startsWith("Coverage Check")){
                // // last two lines may not necessary, and do not print them
                // out.
                // } else {
                //
                // }
                // }
                // Process p =
                // Runtime.getRuntime().exec("cmd /C \"start "+"res\\run.bat");

                // try {
                // p.waitFor();
                // } catch (InterruptedException e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                // p.destroy();
                //

                // p.waitFor();

            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }



        // outpu.close();

        return true;
    }

    public static void printFile(File file)
    {

        try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath())))
        {
            String line = null;
            while ((line = br.readLine()) != null)
            {
                System.out.println(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * This method is used for scanning the result file to determine whether the
     * verification result is true or not.
     *
     * @author Ang Li(University of Arkansas), Qinghua Li(University of
     *         Arkansas) Vincent C. Hu(NIST) Jia Di (University of Arkansas)
     * @data 05/11/2015
     */

    public static boolean resultCount(File file)
    {
        String keyWord = "true";
        Boolean result = false;
        try
        {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext())
            {
                String line = scanner.next();
                if (line.equalsIgnoreCase(keyWord))
                {
                    result = true;
                    break;
                }
            }
            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    private static void parseProperty()
    {

        String[] property_set =
                GenericTreeUtil.getTargetTreeChildrenValues2StrArr(propertyRootNode, GenericInfo.PROPERTY, 1);
        for (int i = 0; i < property_set.length; i++)
        {
            String string = property_set[i];
            int indexS = string.indexOf('[');
            int indexE = string.indexOf(']');
            String propertySel = "";

            if ((indexS > 0) && (indexE > indexS))
            {
                List<String> NodeLst = new ArrayList<String>();
                String NodeLstStr = string.substring(indexS + 1, indexE);
                string = string.substring(0, indexS);
                String[] NodeLstSplits = NodeLstStr.split(",");
                for (int j = 0; j < NodeLstSplits.length; j++)
                {
                    // propertySel += "&" +NodeLstSplits[j].trim() + "= dummy";
                    // List<String> NodeLst = new ArrayList<String>();
                    NodeLst.add(NodeLstSplits[j].trim());
                }

                for (int j = 0; j < SubjecActionResourceAttrNodeLst.size(); j++)
                {
                    String valueOfvalue =
                            ((TargetInfo) SubjecActionResourceAttrNodeLst.get(j).getUserObject()).getvalueOfvalue();

                    if (!NodeLst.contains(valueOfvalue))
                    {
                        propertySel += "&(" + valueOfvalue + " = dummy)";
                    }

                }

            }

            if (string.startsWith("SPEC"))
            {

                String prop = string.replaceFirst("SPEC", "SPEC AG (");
                // prop = prop.substring(0, prop.indexOf("->")) + propertySel +
                // ;
                prop = prop.replaceAll("->", propertySel + " -> AF ");
                prop = prop + ")";
                ADD_PROPERTY = ADD_PROPERTY + "\r\n" + prop;
                /*
                 * Commented out by Bruce Batson July 2013 String invariant =
                 * string.replaceFirst("SPEC", "INVARSPEC !("); invariant =
                 * invariant.substring(0, invariant.indexOf("->")); invariant =
                 * invariant + propertySel +")"; ADD_REACHABILITY_INVARIANTS +=
                 * "\r\n"+ invariant;
                 */
            }
            else if (string.startsWith("LTLSPEC"))
            {
                String prop = string.replaceFirst("LTLSPEC", "LTLSPEC G (");
                prop = prop.replaceAll("->", propertySel + " -> X ");
                prop = prop + ")";
                ADD_PROPERTY = ADD_PROPERTY + "\r\n" + prop;
                /*
                 * Commented out by Bruce Batson July 2013 String invariant =
                 * string.replaceFirst("LTLSPEC", "INVARSPEC !("); invariant =
                 * invariant.substring(0, invariant.indexOf("->")); invariant =
                 * invariant + propertySel +")"; ADD_REACHABILITY_INVARIANTS +=
                 * "\r\n"+ invariant;
                 */
            }

        }
    }

    private static boolean parsePolicyModelCombination(int index, JTree modelTree, JTree targetTree,
            String policyModelTypeName, List defaultDenyEnabledpolSet)
    {

        String model[] = policyModelTypeName.split("#");
        String modeltype = model[0];
        String modelvalue = model[1];

        DefaultMutableTreeNode selectedModelRootNode =
                ModelTreeUtil.getFirstMatchingNodeOnTypeValue(modelTree, modeltype, modelvalue, 2);

        if (selectedModelRootNode == null)
        {
            JOptionPane.showMessageDialog(null,
                    "Cannot find a policy:" + policyModelTypeName + "\nPlease check names of policies for correctness",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        String[] rules =
                ModelTreeUtil.getTargetTreeChildrenValues2StrArr(selectedModelRootNode, modeltype + "RULES", 4);

        List<DefaultMutableTreeNode> ruleCombLst =
                ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode, modeltype + "RULES", 3);

        if (modeltype.equals(ModelInfo.ABAC))
        {

            String rulecombinations = ((ModelInfo) ruleCombLst.get(0).getUserObject()).getRuleCombiningAlgorithm();

            String comment = "\r\n\r\n  -- ABAC Model: " + modelvalue;
            for (int j = 0; j < rules.length; j++)
            {

                getABACRule("rule_" + (rulecount++), rules[j]);
            }

            RuleAdditon(rulecombinations);

            if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
            {
                ADD_RULES = ADD_RULES + "\t\t1\t\t: Deny;\r\n";
            }

        }
        else if (modeltype.equals(ModelInfo.WORKFLOW))
        {

            String rulecombinations = ((ModelInfo) ruleCombLst.get(0).getUserObject()).getRuleCombiningAlgorithm();

            String comment = "\r\n\r\n  -- WORKFLOW Model: " + modelvalue;
            ADD_RULES = ADD_RULES + comment;

            for (int j = 0; j < rules.length; j++)
            {

                getWorkFlowRule("rule_" + (rulecount++), rules[j]);

            }
            RuleAdditon(rulecombinations);

            ADD_STATE_ASSIGNS += "\tnext (Process_State) :=\tProcess_State\t;\t" + newline;

            WorkFlowcount++;
            if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
            {
                ADD_RULES = ADD_RULES + "\t\t1\t\t: Deny;\r\n";
            }

        }
        else if (modeltype.equals(ModelInfo.MULTILEVEL))
        {

            String rulecombinations = ((ModelInfo) ruleCombLst.get(0).getUserObject()).getRuleCombiningAlgorithm();

            String comment = "\r\n\r\n  -- MULTILEVEL Model: " + modelvalue;

            ADD_RULES = ADD_RULES + comment;
            ADD_State_VAR = ADD_State_VAR + comment;

            ADD_FUNCTION = ADD_FUNCTION + comment;

            // find
            String subAttr = "";
            String resAttr = "";
            List choicesSubLst = ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode,
                    ModelInfo.MULTILEVELSUBJECTLEVELS, 4);
            List choicesResLst = ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode,
                    ModelInfo.MULTILEVELRESOURCELEVELS, 4);

            for (int i = 0; i < choicesSubLst.size(); i++)
            {
                for (int j = 0; j < choicesResLst.size(); j++)
                {

                    DefaultMutableTreeNode Subnode = (DefaultMutableTreeNode) choicesSubLst.get(i);
                    DefaultMutableTreeNode Resnode = (DefaultMutableTreeNode) choicesResLst.get(j);
                    subAttr = ((ModelInfo) Subnode.getUserObject()).getSecondTargetvalueOfnodes();
                    resAttr = ((ModelInfo) Resnode.getUserObject()).getSecondTargetvalueOfnodes();

                    ADD_FunctionCallInVAR = ADD_FunctionCallInVAR + "\r\n" + "\r\n\tMultiLevel" + MultiLevelcount
                            + ": callLevelInfoFromMultiLevel" + MultiLevelcount + " (" + subAttr + ", " + resAttr
                            + ");";

                    for (int k = 0; k < rules.length; k++)
                    {
                        getMultiLevelRule("rule_" + (rulecount++), rules[k]);
                    }

                    DefaultMutableTreeNode rootNode = TargetTreeUtil.getRootNode(targetTree);

                    if (targetTree != null)
                    {
                        String t1 = ((ModelInfo) Subnode.getUserObject()).getSecondTargetvalueOfnodes();
                        String t2 = ((ModelInfo) Resnode.getUserObject()).getSecondTargetvalueOfnodes();
                        int t1_level = ((ModelInfo) Subnode.getUserObject()).getLevel();
                        int t2_level = ((ModelInfo) Resnode.getUserObject()).getLevel();

                        DefaultMutableTreeNode SubRootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, t1,
                                TargetInfo.SUBJECT_ATTRIBUTES, TargetInfo.attrLevel);

                        DefaultMutableTreeNode ResRottNode = TargetTreeUtil.getFirstMatchingNode(targetTree, t2,
                                TargetInfo.RESOURCE_ATTRIBUTES, TargetInfo.attrLevel);

                        List SubList = TargetTreeUtil.getTargetTreeChildrenNode2List(SubRootNode,
                                TargetInfo.SUBJECT_ATTR_VALUSES, TargetInfo.nodeLevel);
                        List ResList = TargetTreeUtil.getTargetTreeChildrenNode2List(ResRottNode,
                                TargetInfo.RESOURCE_ATTR_VALUSES, TargetInfo.nodeLevel);

                        String subjectLevelsMapInfo = "";
                        String resourceLevelsMapInfo = "";

                        for (int k = 0; k < SubList.size(); k++)
                        {
                            DefaultMutableTreeNode attNode = (DefaultMutableTreeNode) SubList.get(k);
                            String tName = attNode.toString();
                            String tLevel = t1_level + "";
                            String rcase = "\r\n\t\t\t\t\tsubject = " + tName + " : " + tLevel + " ;";
                            subjectLevelsMapInfo = subjectLevelsMapInfo + rcase;
                        }

                        for (int k = 0; k < ResList.size(); k++)
                        {
                            DefaultMutableTreeNode attNode = (DefaultMutableTreeNode) ResList.get(k);
                            String tName = attNode.toString();
                            String tLevel = t2_level + "";
                            String rcase = "\r\n\t\t\t\t\tresource = " + tName + " : " + tLevel + " ;";
                            resourceLevelsMapInfo = resourceLevelsMapInfo + rcase;
                        }

                        String ADD_Level = "";

                        for (int k = -2; k < Generalproperties.MultiLevelStateNumber; k++)
                        {
                            ADD_Level += (k + 1);
                            if (k < (Generalproperties.MultiLevelStateNumber - 1))
                            {
                                ADD_Level += ",";
                            }
                        }

                        String multiLevelFunc = smvMultiLevelTempletes;
                        multiLevelFunc = multiLevelFunc.replaceAll("#MultiLevelMappingFuncName#",
                                "callLevelInfoFromMultiLevel" + MultiLevelcount);
                        multiLevelFunc = multiLevelFunc.replaceAll("#ADD_Level#", ADD_Level);
                        multiLevelFunc = multiLevelFunc.replaceAll("#ADD_SubjectLevelMapping#", subjectLevelsMapInfo);
                        multiLevelFunc = multiLevelFunc.replaceAll("#ADD_ResourceLevelMapping#", resourceLevelsMapInfo);

                        ADD_FUNCTION = ADD_FUNCTION + multiLevelFunc;

                    }

                    MultiLevelcount++;

                }

            }

            String ADD_Level = "";
            for (int i = -2; i < Generalproperties.MultiLevelStateNumber; i++)
            {
                ADD_Level += (i + 1);
                if (i < (Generalproperties.MultiLevelStateNumber - 1))
                {
                    ADD_Level += ",";
                }
            }

            RuleAdditon(rulecombinations);
            if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
            {
                ADD_RULES = ADD_RULES + "\t\t1\t\t: Deny;\r\n";
            }

        }
        return true;
    }

    private static boolean parsePolicyModel(JTree modelTree, JTree targetTree, String policyModelTypeName,
            List defaultDenyEnabledpolSet)
    {

        String model[] = policyModelTypeName.split("#");
        String modeltype = model[0];
        String modelvalue = model[1];

        DefaultMutableTreeNode selectedModelRootNode =
                ModelTreeUtil.getFirstMatchingNodeOnTypeValue(modelTree, modeltype, modelvalue, 2);

        if (selectedModelRootNode == null)
        {
            JOptionPane.showMessageDialog(null,
                    "Cannot find a policy:" + policyModelTypeName + "\nPlease check names of policies for correctness",
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        String[] rules =
                ModelTreeUtil.getTargetTreeChildrenValues2StrArr(selectedModelRootNode, modeltype + "RULES", 4);

        List<DefaultMutableTreeNode> ruleCombLst =
                ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode, modeltype + "RULES", 3);

        // if (modeltype.equals(ModelInfo.RBAC)){
        //
        // String comment = "\r\n\r\n -- RBAC Model: "+ modelvalue;
        //
        // ADD_RULES = ADD_RULES + comment;
        // for (int j = 0; j < rules.length; j++) {
        // String tmp[] = rules[j].split("#");
        //
        // getRBACRule("rule_"+(rulecount++), tmp[0], tmp[1], tmp[2], tmp[3]);
        // //System.out.println(rules[j]+" To "+ xacmlrule);
        //
        // }
        // if (defaultDenyEnabledpolSet != null &&
        // defaultDenyEnabledpolSet.contains(policyModelTypeName)){ADD_RULES =
        // ADD_RULES + "\t\t1\t\t: Deny;\r\n";}
        //
        //
        //
        // }else
        if (modeltype.equals(ModelInfo.ABAC))
        {

            String rulecombinations = ((ModelInfo) ruleCombLst.get(0).getUserObject()).getRuleCombiningAlgorithm();

            String comment = "\r\n\r\n  -- ABAC Model: " + modelvalue;
            for (int j = 0; j < rules.length; j++)
            {
                // String tmp[] = rules[j].split("#");

                getABACRule("rule_" + (rulecount++), rules[j]);
                // System.out.println("ABAC for NuSMV is not applied yet");
            }

            RuleAdditon(rulecombinations);

            // System.err.println(ADD_RULES);

            if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
            {
                ADD_RULES = ADD_RULES + "\t\t1\t\t: Deny;\r\n";
            }

        }
        else if (modeltype.equals(ModelInfo.WORKFLOW))
        {

            String rulecombinations = ((ModelInfo) ruleCombLst.get(0).getUserObject()).getRuleCombiningAlgorithm();

            String comment = "\r\n\r\n  -- WORKFLOW Model: " + modelvalue;
            // ADD_WORKFLOW_STATE_ASSIGNS = ADD_WORKFLOW_STATE_ASSIGNS + comment
            // ;
            ADD_RULES = ADD_RULES + comment;

            // String NuSMVcase = "\r\n\tinit (Process_State" +") := 1;";
            // //+WorkFlowcount
            // NuSMVcase = NuSMVcase +
            // "\r\n\tnext (Process_State"+") := case";//+WorkFlowcount
            //
            // Temp_WorkFlowRule = "";

            for (int j = 0; j < rules.length; j++)
            {

                getWorkFlowRule("rule_" + (rulecount++), rules[j]);

            }
            RuleAdditon(rulecombinations);

            // NuSMVcase = NuSMVcase + Temp_WorkFlowRule + "\r\n\t\t-- else";
            // NuSMVcase = NuSMVcase +"\r\n\t\t1 : Process_State"+";";
            // //+WorkFlowcount
            // NuSMVcase = NuSMVcase +"\r\n\tesac;";
            //
            // ADD_WORKFLOW_STATE_ASSIGNS = ADD_WORKFLOW_STATE_ASSIGNS +
            // NuSMVcase;

            ADD_STATE_ASSIGNS += "\tnext (Process_State) :=\tProcess_State\t;\t" + newline;

            WorkFlowcount++;
            if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
            {
                ADD_RULES = ADD_RULES + "\t\t1\t\t: Deny;\r\n";
            }

        }
        else if (modeltype.equals(ModelInfo.MULTILEVEL))
        {

            String rulecombinations = ((ModelInfo) ruleCombLst.get(0).getUserObject()).getRuleCombiningAlgorithm();

            String comment = "\r\n\r\n  -- MULTILEVEL Model: " + modelvalue;

            ADD_RULES = ADD_RULES + comment;
            ADD_State_VAR = ADD_State_VAR + comment;

            ADD_FUNCTION = ADD_FUNCTION + comment;

            /*
             * String[] subjectLevels =
             * ModelTreeUtil.getTargetTreeChildrenValues2StrArr
             * (selectedModelRootNode, ModelInfo.MULTILEVELSUBJECTLEVELS, 4);
             * String[] resourceLevels =
             * ModelTreeUtil.getTargetTreeChildrenValues2StrArr
             * (selectedModelRootNode, ModelInfo.MULTILEVELRESOURCELEVELS, 4);
             *
             *
             * String subjectLevelsMapInfo = ""; String resourceLevelsMapInfo =
             * "";
             *
             *
             * for (int j = 0; j < subjectLevels.length; j++) { String tmp[] =
             * subjectLevels[j].split("#"); String tempAttr = ""; String[] tem2
             * = tmp[0].split(";"); if (tem2.length>3){ tempAttr = tem2[3]; }
             *
             *
             * //subject = Secretary: 1; --low String rcase =
             * "\r\n\t\t\t\t\tsubject = "+ tempAttr+" : "+ tmp[1] + " ;";
             *
             * // System.out.println(rcase); subjectLevelsMapInfo =
             * subjectLevelsMapInfo + rcase; }
             *
             * for (int j = 0; j < resourceLevels.length; j++) { String tmp[] =
             * resourceLevels[j].split("#"); String tempAttr = ""; String[] tem2
             * = tmp[0].split(";"); if (tem2.length>3){ tempAttr = tem2[3]; }
             * //subject = Secretary: 1; --low String rcase =
             * "\r\n\t\t\t\t\tresource = "+ tempAttr+" : "+ tmp[1] + " ;"; //
             * System.out.println(rcase); resourceLevelsMapInfo =
             * resourceLevelsMapInfo + rcase; }
             *
             * String multiLevelFunc = smvMultiLevelTempletes; //
             * System.out.println
             * ("NUSMVPanelUtil.parsePolicyModel()"+smvMultiLevelTempletes);
             */

            // find
            String subAttr = "";
            String resAttr = "";
            List choicesSubLst = ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode,
                    ModelInfo.MULTILEVELSUBJECTLEVELS, 4);
            List choicesResLst = ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode,
                    ModelInfo.MULTILEVELRESOURCELEVELS, 4);

            for (int i = 0; i < choicesSubLst.size(); i++)
            {
                for (int j = 0; j < choicesResLst.size(); j++)
                {

                    DefaultMutableTreeNode Subnode = (DefaultMutableTreeNode) choicesSubLst.get(i);
                    DefaultMutableTreeNode Resnode = (DefaultMutableTreeNode) choicesResLst.get(j);
                    subAttr = ((ModelInfo) Subnode.getUserObject()).getSecondTargetvalueOfnodes();
                    resAttr = ((ModelInfo) Resnode.getUserObject()).getSecondTargetvalueOfnodes();

                    ADD_FunctionCallInVAR = ADD_FunctionCallInVAR + "\r\n" + "\r\n\tMultiLevel" + MultiLevelcount
                            + ": callLevelInfoFromMultiLevel" + MultiLevelcount + " (" + subAttr + ", " + resAttr
                            + ");";

                    for (int k = 0; k < rules.length; k++)
                    {
                        getMultiLevelRule("rule_" + (rulecount++), rules[k]);
                    }

                    DefaultMutableTreeNode rootNode = TargetTreeUtil.getRootNode(targetTree);

                    if (targetTree != null)
                    {
                        // System.err.println("::" +
                        // TargetInfo.SUBJECT_ATTRIBUTES);
                        // System.err.println("::" +
                        // ((ModelInfo)Subnode.getUserObject()).getSecondTargetvalueOfnodes());
                        // System.err.println("::" + TargetInfo.attrLevel);

                        // DefaultMutableTreeNode SubNode =
                        // TargetTreeUtil.getFirstMatchingNode (targetTree,
                        // TargetInfo.SUBJECT_ATTRIBUTES,
                        // ((ModelInfo)Subnode.getUserObject()).getValue(),
                        // TargetInfo.attrLevel);
                        // DefaultMutableTreeNode SubNode =
                        // TargetTreeUtil.getFirstMatchingNode (targetTree,
                        // TargetInfo.SUBJECT_ATTRIBUTES,
                        // ((ModelInfo)Subnode.getUserObject()).getValue().substring(TargetInfo.SUBJECT_ATTRIBUTES.length()+1),
                        // 1);

                        String t1 = ((ModelInfo) Subnode.getUserObject()).getSecondTargetvalueOfnodes();
                        String t2 = ((ModelInfo) Resnode.getUserObject()).getSecondTargetvalueOfnodes();
                        int t1_level = ((ModelInfo) Subnode.getUserObject()).getLevel();
                        int t2_level = ((ModelInfo) Resnode.getUserObject()).getLevel();

                        DefaultMutableTreeNode SubRootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, t1,
                                TargetInfo.SUBJECT_ATTRIBUTES, TargetInfo.attrLevel);

                        DefaultMutableTreeNode ResRottNode = TargetTreeUtil.getFirstMatchingNode(targetTree, t2,
                                TargetInfo.RESOURCE_ATTRIBUTES, TargetInfo.attrLevel);

                        // System.err.println("::" + SubRootNode.toString());
                        // System.err.println("::" + ResRottNode.toString());

                        List SubList = TargetTreeUtil.getTargetTreeChildrenNode2List(SubRootNode,
                                TargetInfo.SUBJECT_ATTR_VALUSES, TargetInfo.nodeLevel);
                        List ResList = TargetTreeUtil.getTargetTreeChildrenNode2List(ResRottNode,
                                TargetInfo.RESOURCE_ATTR_VALUSES, TargetInfo.nodeLevel);

                        String subjectLevelsMapInfo = "";
                        String resourceLevelsMapInfo = "";

                        for (int k = 0; k < SubList.size(); k++)
                        {
                            DefaultMutableTreeNode attNode = (DefaultMutableTreeNode) SubList.get(k);
                            // System.err.println(attNode.toString());
                            String tName = attNode.toString();
                            String tLevel = t1_level + "";
                            String rcase = "\r\n\t\t\t\t\tsubject = " + tName + " : " + tLevel + " ;";
                            subjectLevelsMapInfo = subjectLevelsMapInfo + rcase;
                        }

                        for (int k = 0; k < ResList.size(); k++)
                        {
                            DefaultMutableTreeNode attNode = (DefaultMutableTreeNode) ResList.get(k);
                            // System.err.println(attNode.toString());
                            String tName = attNode.toString();
                            String tLevel = t2_level + "";
                            String rcase = "\r\n\t\t\t\t\tresource = " + tName + " : " + tLevel + " ;";
                            // System.out.println(rcase);
                            resourceLevelsMapInfo = resourceLevelsMapInfo + rcase;
                        }

                        String ADD_Level = "";

                        for (int k = -2; k < Generalproperties.MultiLevelStateNumber; k++)
                        {
                            ADD_Level += (k + 1);
                            if (k < (Generalproperties.MultiLevelStateNumber - 1))
                            {
                                ADD_Level += ",";
                            }
                        }

                        String multiLevelFunc = smvMultiLevelTempletes;
                        multiLevelFunc = multiLevelFunc.replaceAll("#MultiLevelMappingFuncName#",
                                "callLevelInfoFromMultiLevel" + MultiLevelcount);
                        multiLevelFunc = multiLevelFunc.replaceAll("#ADD_Level#", ADD_Level);
                        multiLevelFunc = multiLevelFunc.replaceAll("#ADD_SubjectLevelMapping#", subjectLevelsMapInfo);
                        multiLevelFunc = multiLevelFunc.replaceAll("#ADD_ResourceLevelMapping#", resourceLevelsMapInfo);

                        ADD_FUNCTION = ADD_FUNCTION + multiLevelFunc;

                        // System.out.println("NUSMVPanelUtil.parsePolicyModel()"+smvMultiLevelTempletes);

                        // for (int i = 0; i < choices.size(); i++) {
                        //
                        //
                        // NuSMVSubAttrLst subattrElm = new
                        // NuSMVSubAttrLst(attNode);
                        // Enumeration enumeration = attNode.children();
                        // while(enumeration.hasMoreElements())
                        // {
                        // DefaultMutableTreeNode attrValueNode =
                        // (DefaultMutableTreeNode)enumeration.nextElement();
                        // subattrElm.addchildNode2Lst(attrValueNode);
                        // }
                        // //SubAttrLst subattrElm = new
                        // SubAttrLst((DefaultMutableTreeNode) choices.get(i));
                        // attrLst.add(subattrElm);

                        // List choices = (List)
                        // TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode,
                        // TargetInfo.SUBJECT_ATTRIBUTES, TargetInfo.attrLevel);
                        //
                        // for (int i = 0; i < choices.size(); i++) {
                        // DefaultMutableTreeNode attNode =
                        // (DefaultMutableTreeNode) choices.get(i);
                        //
                        // NuSMVSubAttrLst subattrElm = new
                        // NuSMVSubAttrLst(attNode);
                        // Enumeration enumeration = attNode.children();
                        // while(enumeration.hasMoreElements())
                        // {
                        // DefaultMutableTreeNode attrValueNode =
                        // (DefaultMutableTreeNode)enumeration.nextElement();
                        // subattrElm.addchildNode2Lst(attrValueNode);
                        // }
                        // //SubAttrLst subattrElm = new
                        // SubAttrLst((DefaultMutableTreeNode) choices.get(i));
                        // attrLst.add(subattrElm);
                        // }
                    }

                    // ModelTreeUtil.getFirstMatchingNodeOnTypeValue
                    //
                    // String model[] = policyModelTypeName.split("#");
                    // String modeltype = model[0];
                    // String modelvalue = model[1];
                    //
                    //
                    //
                    // TargetTreeUtil.getFirstMatchingNodeOnTypeValue
                    // (targetTree, modeltype, modelvalue, 2);
                    //
                    MultiLevelcount++;

                }

            }

            String ADD_Level = "";
            for (int i = -2; i < Generalproperties.MultiLevelStateNumber; i++)
            {
                ADD_Level += (i + 1);
                if (i < (Generalproperties.MultiLevelStateNumber - 1))
                {
                    ADD_Level += ",";
                }
            }

            // multiLevelFunc =
            // multiLevelFunc.replaceAll("#MultiLevelMappingFuncName#",
            // "callLevelInfoFromMultiLevel"+MultiLevelcount );
            // multiLevelFunc = multiLevelFunc.replaceAll("#ADD_Level#",
            // ADD_Level);
            // multiLevelFunc =
            // multiLevelFunc.replaceAll("#ADD_SubjectLevelMapping#",
            // subjectLevelsMapInfo);
            // multiLevelFunc =
            // multiLevelFunc.replaceAll("#ADD_ResourceLevelMapping#",
            // resourceLevelsMapInfo);
            //
            // ADD_FUNCTION = ADD_FUNCTION + multiLevelFunc;

            RuleAdditon(rulecombinations);
            if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
            {
                ADD_RULES = ADD_RULES + "\t\t1\t\t: Deny;\r\n";
            }

        }
        return true;
    }

    // FIXME
    private static void RuleAdditon(String rulecombinations)
    {

        if (rulecombinations.equals(ModelInfo.FirstApplRuleCombinatoin))
        {
            ADD_RULES += ADD_Ordered_PERMITDENY_RULES;
        }
        else if (rulecombinations.equals(ModelInfo.PermitOverrideRuleCombination))
        {
            ADD_RULES += ADD_PERMIT_RULES + ADD_DENY_RULES;
        }
        else if (rulecombinations.equals(ModelInfo.DenyOverrideRuleCombination))
        {
            ADD_RULES += ADD_DENY_RULES + ADD_PERMIT_RULES;
        }
        else if (rulecombinations.equals(ModelInfo.WeakConsensusRuleCombination))
        {
            ADD_RULES += ADD_PERMIT_RULES + ADD_DENY_RULES;

        }
        else if (rulecombinations.equals(ModelInfo.StrongConsensusRuleCombination))
        {
            ADD_RULES += ADD_PERMIT_RULES + ADD_DENY_RULES;
        }
        else if (rulecombinations.equals(ModelInfo.WeakMajorityRuleCombination))
        {
            ADD_RULES += ADD_PERMIT_RULES + ADD_DENY_RULES;
        }
        else if (rulecombinations.equals(ModelInfo.StrongMajorityRuleCombination))
        {
            ADD_RULES += ADD_PERMIT_RULES + ADD_DENY_RULES;
        }
        else if (rulecombinations.equals(ModelInfo.SuperMajorityPermitRuleCombination))
        {
            ADD_RULES += ADD_PERMIT_RULES + ADD_DENY_RULES;
        }

        ADD_Ordered_PERMITDENY_RULES = "";
        ADD_PERMIT_RULES = "";
        ADD_DENY_RULES = "";
    }

    private static boolean getRBACRule(String RuleId, String subject, String resource, String action,
            String enviromnment, String effect)
    {

        String newrule = "\r\n\t\trole_subject = " + subject + " & resource = " + resource + " & action = " + action
                + " & enviromnment = " + enviromnment + " :" + effect + " ;";
        ADD_RULES = ADD_RULES + newrule;

        return true;
    }

    private static boolean getWorkFlowRule(String RuleId, String ABACRule)
    {

        String[] content = ABACRule.split("->");
        String effect = content[1];
        String[] elements = content[0].split("#");

        String temprule = "\t\t";
        // Changed i start from 0 to 1 so that it will skip the rule number
        for (int i = 1; i < elements.length; i++)
        {
            String tempString = getChangedElement(elements[i]);

            if (i == 1)
            {
                temprule += tempString;
            }
            else
            {
                temprule += "\t&\t" + tempString;
            }
        }

        if (effect.equals("Permit and go to Next State"))
        {

            String elms[] = elements[0].split(";");
            String stateNum = elms[0].replaceAll("Process_State", "").trim();

            String staterule = temprule + "\t:\t" + (Integer.parseInt(stateNum) + 1) + "\t;\t" + newline;
            Temp_WorkFlowRule = Temp_WorkFlowRule + staterule;

            String ruleWithDecison = temprule + "\t:\t" + "Permit" + "\t;\t" + newline;

            // ADD_RULES = ADD_RULES + ruleWithDecison;

            ADD_PERMIT_RULES += ruleWithDecison;

        }
        else
        {
            temprule += "\t:\t" + effect + "\t;\t" + newline;

            // ADD_RULES = ADD_RULES + temprule;
            ADD_Ordered_PERMITDENY_RULES = ADD_Ordered_PERMITDENY_RULES + temprule;

            if (effect.trim().equals("Permit"))
            {
                ADD_PERMIT_RULES = ADD_PERMIT_RULES + temprule;
            }
            else if (effect.trim().equals("Deny"))
            {
                ADD_DENY_RULES = ADD_DENY_RULES + temprule;
            }

        }
        return true;
        // }

        // return true;
    }

    private static boolean getABACRule(String RuleId, String ABACRule)
    {

        String[] content = ABACRule.split("->");
        String effect = content[1];
        String[] elements = content[0].split("#");
        String tmpSubjectString = "";
        String tmpResourceString = "";
        String tmpActionString = "";

        String temprule = "\t\t";
        /*
         * Bruce Batson June 2013 Edit: So that the NuSMV will not take the rule
         * number as part of the rule I changed the start value of i form 0 to 1
         */
        for (int i = 1; i < elements.length; i++)
        {
            String tempString = getChangedElement(elements[i]);

            if (i == 1)
            {
                temprule += tempString;
            }
            else
            {
                temprule += "\t&\t" + tempString;
            }
        }

        temprule += "\t:\t" + effect + "\t;\t" + newline;

        // ADD_RULES = ADD_RULES + temprule;

        ADD_Ordered_PERMITDENY_RULES = ADD_Ordered_PERMITDENY_RULES + temprule;

        if (effect.trim().equals("Permit"))
        {
            ADD_PERMIT_RULES = ADD_PERMIT_RULES + temprule;
        }
        else if (effect.trim().equals("Deny"))
        {
            ADD_DENY_RULES = ADD_DENY_RULES + temprule;
        }

        return true;
    }

    private static String getChangedElement(String elementSet)
    {

        String tempString = "";

        String elms[] = elementSet.split(";");
        if (elementSet.startsWith("Process_State"))
        {
            String stateNum = elms[0].replaceAll("Process_State", "").trim();
            return "\r\n\t\tProcess_State" + " = " + stateNum; // +WorkFlowcount
        }

        String RAttributeId = elms[1];
        // String RTypeEqual = elms[2].toLowerCase()+ "-equal";
        // String RTypeOfValue = elms[2].toLowerCase();
        String RAttributeValue = elms[3];

        return RAttributeId + " = " + RAttributeValue;
    }

    // private static boolean getWorkFlowRule(String RuleId, String state,
    // String subject,
    // String resource, String action, String effect) {
    //
    // if (effect.equals("Permit and go to Next State")){
    //
    // String NuSMVcase = "\r\n\t\tstate"+WorkFlowcount +" = "+
    // state+" & role_subject = "+ subject + " & resource = " + resource +
    // " & action = " + action + " :" + (Integer.parseInt(state)+1) + " ;";
    // Temp_WorkFlowRule = Temp_WorkFlowRule + NuSMVcase;
    //
    // // NuSMVcase = "\r\n\t\tstate"+WorkFlowcount +" = "+
    // state+" & role_subject = "+ subject + " & resource = " + resource +
    // " & action = " + action + " :" + "Permit1" + " ;";
    // // ADD_RULES = ADD_RULES + NuSMVcase;
    //
    // } else{
    // String NuSMVcase = "\r\n\t\tstate"+WorkFlowcount +" = "+
    // state+" & role_subject = "+ subject + " & resource = " + resource +
    // " & action = " + action + " :" + effect + " ;";
    // ADD_RULES = ADD_RULES + NuSMVcase;
    // }
    //
    // return true;
    // }

    private static boolean getMultiLevelRule(String RuleId, String property)
    {

        String IndexName = "MultiLevel" + MultiLevelcount;
        if (property.equals("no read up"))
        {
            String dNuSMVcase = "\r\n\t\t" + IndexName + ".ismatch = 1 & " + IndexName + ".subjlevel < " + IndexName
                    + ".reslevel & MLSDefaultAction = " + "read" + " : Deny;";
            String pNuSMVcase = "\r\n\t\t" + IndexName + ".ismatch = 1 & " + IndexName + ".subjlevel >= " + IndexName
                    + ".reslevel & MLSDefaultAction = " + "read" + " : Permit;";
            ADD_Ordered_PERMITDENY_RULES = ADD_Ordered_PERMITDENY_RULES + dNuSMVcase + pNuSMVcase;
            ADD_PERMIT_RULES = ADD_PERMIT_RULES + pNuSMVcase;
            ADD_DENY_RULES = ADD_DENY_RULES + dNuSMVcase;
        }
        else if (property.equals("no write down"))
        {
            String dNuSMVcase = "\r\n\t\t" + IndexName + ".ismatch = 1 & " + IndexName + ".subjlevel > " + IndexName
                    + ".reslevel & MLSDefaultAction = " + "write" + " : Deny;";
            String pNuSMVcase = "\r\n\t\t" + IndexName + ".ismatch = 1 & " + IndexName + ".subjlevel <= " + IndexName
                    + ".reslevel & MLSDefaultAction = " + "write" + " : Permit;";
            ADD_Ordered_PERMITDENY_RULES = ADD_Ordered_PERMITDENY_RULES + dNuSMVcase + pNuSMVcase;
            ADD_PERMIT_RULES = ADD_PERMIT_RULES + pNuSMVcase;
            ADD_DENY_RULES = ADD_DENY_RULES + dNuSMVcase;
        }

        return true;
    }

    // private static String getNuSMVCombination(String templateStr) {
    //
    // // String tmpString = smvTemplates;
    // // System.err.println(SUBJECTS);
    // String tmpString = templateStr;
    // tmpString = tmpString.replaceAll("#SUBJECTS#", SUBJECTS);
    //
    // tmpString = tmpString.replaceAll("#ACTIONS#", ACTIONS);
    // tmpString = tmpString.replaceAll("#RESOURCES#", RESOURCES);
    // tmpString = tmpString.replaceAll("#ADD_VAR#", ADD_VAR);
    // tmpString = tmpString.replaceAll("#ADD_State_VAR#", ADD_State_VAR);
    // tmpString = tmpString.replaceAll("#ADD_FunctionCallInVAR#",
    // ADD_FunctionCallInVAR);
    //
    // tmpString = tmpString.replaceAll("#ADD_RULES#", ADD_RULES);
    // tmpString = tmpString.replaceAll("#ADD_STATE_ASSIGNS#",
    // ADD_STATE_ASSIGNS);
    // tmpString = tmpString.replaceAll("#ADD_WORKFLOW_STATE_ASSIGNS#",
    // ADD_WORKFLOW_STATE_ASSIGNS);
    //
    //
    // ADD_PROPERTY = Matcher.quoteReplacement(ADD_PROPERTY); // $ signs cause
    // Illegal group reference. So, we change $ as quota
    // ADD_REACHABILITY_INVARIANTS =
    // Matcher.quoteReplacement(ADD_REACHABILITY_INVARIANTS); // $ signs cause
    // Illegal group reference. So, we change $ as quota
    //
    //
    // tmpString = tmpString.replaceAll("#ADD_PROPERTY#",
    // ADD_REACHABILITY_INVARIANTS + ADD_PROPERTY);
    // tmpString = tmpString.replaceAll("#ADD_FUNCTION#", ADD_FUNCTION);
    //
    // return tmpString;
    // }

    private static String getNuSMV(String templateStr)
    {

        // String tmpString = smvTemplates;
        // System.err.println(SUBJECTS);
        String tmpString = templateStr;
        tmpString = tmpString.replaceAll("#SUBJECTS#", SUBJECTS);

        tmpString = tmpString.replaceAll("#ACTIONS#", ACTIONS);
        tmpString = tmpString.replaceAll("#RESOURCES#", RESOURCES);
        tmpString = tmpString.replaceAll("#ADD_VAR#", ADD_VAR);
        tmpString = tmpString.replaceAll("#ADD_State_VAR#", ADD_State_VAR);
        tmpString = tmpString.replaceAll("#ADD_FunctionCallInVAR#", ADD_FunctionCallInVAR);

        tmpString = tmpString.replaceAll("#ADD_COMBINATION_POLICY#", ADD_COMBINATION_POLICY);
        tmpString = tmpString.replaceAll("#ADD_RULES#", ADD_RULES);
        tmpString = tmpString.replaceAll("#ADD_STATE_ASSIGNS#", ADD_STATE_ASSIGNS);
        tmpString = tmpString.replaceAll("#ADD_WORKFLOW_STATE_ASSIGNS#", ADD_WORKFLOW_STATE_ASSIGNS);

        ADD_PROPERTY = Matcher.quoteReplacement(ADD_PROPERTY); // $ signs cause
                                                               // Illegal group
                                                               // reference.
                                                               // So, we change
                                                               // $ as quota
        ADD_REACHABILITY_INVARIANTS = Matcher.quoteReplacement(ADD_REACHABILITY_INVARIANTS); // $
                                                                                             // signs
                                                                                             // cause
                                                                                             // Illegal
                                                                                             // group
                                                                                             // reference.
                                                                                             // So,
                                                                                             // we
                                                                                             // change
                                                                                             // $
                                                                                             // as
                                                                                             // quota

        tmpString = tmpString.replaceAll("#ADD_PROPERTY#", ADD_REACHABILITY_INVARIANTS + ADD_PROPERTY);
        tmpString = tmpString.replaceAll("#ADD_FUNCTION#", ADD_FUNCTION);

        tmpString = NuSMVattrNameCovert.ConvertToFitNuSMFormat(tmpString, SubjecActionResourceAttrNodeLst);

        return tmpString;
    }

    private static void setAttributes(JTree targetTree)
    {

        // SUBJECTS

        attrLst = new ArrayList<NuSMVSubAttrLst>();

        DefaultMutableTreeNode rootNode = TargetTreeUtil.getRootNode(targetTree);

        SubjecActionResourceAttrNodeLst = new ArrayList<DefaultMutableTreeNode>();

        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, TargetInfo.SUBJECT_ATTRIBUTES,
                    TargetInfo.attrLevel);
            SubjecActionResourceAttrNodeLst.addAll(choices);
            for (int i = 0; i < choices.size(); i++)
            {
                DefaultMutableTreeNode attNode = (DefaultMutableTreeNode) choices.get(i);

                NuSMVSubAttrLst subattrElm = new NuSMVSubAttrLst(attNode);
                Enumeration enumeration = attNode.children();
                while (enumeration.hasMoreElements())
                {
                    DefaultMutableTreeNode attrValueNode = (DefaultMutableTreeNode) enumeration.nextElement();
                    subattrElm.addchildNode2Lst(attrValueNode);
                }
                // SubAttrLst subattrElm = new
                // SubAttrLst((DefaultMutableTreeNode) choices.get(i));
                attrLst.add(subattrElm);
            }
        }

        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, TargetInfo.RESOURCE_ATTRIBUTES,
                    TargetInfo.attrLevel);
            SubjecActionResourceAttrNodeLst.addAll(choices);
            for (int i = 0; i < choices.size(); i++)
            {
                DefaultMutableTreeNode attNode = (DefaultMutableTreeNode) choices.get(i);

                NuSMVSubAttrLst subattrElm = new NuSMVSubAttrLst(attNode);
                Enumeration enumeration = attNode.children();
                while (enumeration.hasMoreElements())
                {
                    DefaultMutableTreeNode attrValueNode = (DefaultMutableTreeNode) enumeration.nextElement();
                    subattrElm.addchildNode2Lst(attrValueNode);
                }
                // SubAttrLst subattrElm = new
                // SubAttrLst((DefaultMutableTreeNode) choices.get(i));
                attrLst.add(subattrElm);
            }
        }

        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, TargetInfo.ACTION_ATTRIBUTES,
                    TargetInfo.attrLevel);
            SubjecActionResourceAttrNodeLst.addAll(choices);
            for (int i = 0; i < choices.size(); i++)
            {
                DefaultMutableTreeNode attNode = (DefaultMutableTreeNode) choices.get(i);

                NuSMVSubAttrLst subattrElm = new NuSMVSubAttrLst(attNode);
                Enumeration enumeration = attNode.children();
                while (enumeration.hasMoreElements())
                {
                    DefaultMutableTreeNode attrValueNode = (DefaultMutableTreeNode) enumeration.nextElement();
                    subattrElm.addchildNode2Lst(attrValueNode);
                }
                // SubAttrLst subattrElm = new
                // SubAttrLst((DefaultMutableTreeNode) choices.get(i));
                attrLst.add(subattrElm);
            }
        }

        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, TargetInfo.ENVIRONMENT_ATTRIBUTES,
                    TargetInfo.attrLevel);
            SubjecActionResourceAttrNodeLst.addAll(choices);
            for (int i = 0; i < choices.size(); i++)
            {
                DefaultMutableTreeNode attNode = (DefaultMutableTreeNode) choices.get(i);

                NuSMVSubAttrLst subattrElm = new NuSMVSubAttrLst(attNode);
                Enumeration enumeration = attNode.children();
                while (enumeration.hasMoreElements())
                {
                    DefaultMutableTreeNode attrValueNode = (DefaultMutableTreeNode) enumeration.nextElement();
                    subattrElm.addchildNode2Lst(attrValueNode);
                }
                // SubAttrLst subattrElm = new
                // SubAttrLst((DefaultMutableTreeNode) choices.get(i));
                attrLst.add(subattrElm);
            }
        }

        for (int i = 0; i < attrLst.size(); i++)
        {

            if (i == 0)
            {
                SubPol_ARGUMENTS += "(" + attrLst.get(i).toString();
            }
            else
            {
                SubPol_ARGUMENTS += "," + attrLst.get(i).toString();
            }
        }

        SubPol_ARGUMENTS += ", Process_State)";

        for (int i = 0; i < attrLst.size(); i++)
        {

            ADD_VAR += attrLst.get(i).toVARstring();
            ADD_STATE_ASSIGNS += attrLst.get(i).toNextStatestring();
        }

        // Add state information as global.
        ADD_VAR = ADD_VAR + "\r\n" + "\r\n\tProcess_State" + "	: {"; // +WorkFlowcount
        for (int i = 0; i < Generalproperties.ProcessStateNumber; i++)
        {
            ADD_VAR += (i + 1) + "";
            if (i < (Generalproperties.ProcessStateNumber - 1))
            {
                ADD_VAR += ",";
            }
        }
        ADD_VAR += "};";
        // ADD_STATE_ASSIGNS += "\tnext (Process_State) :=\tProcess_State\t;\t"
        // + newline;

    }

    private static void setTemplate()
    {

        smvTempleteFileName = Generalproperties.NUSMVTemplates;
        // System.err.println(smvTempleteFileName);

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(smvTempleteFileName));
            String line = br.readLine();
            int selection = -1;
            while (line != null)
            {

                if (line.startsWith("[mainModule]"))
                {
                    selection = 0;
                }
                else if (line.startsWith("[mainModuleWhenMerge]"))
                {
                    selection = 1;
                }
                else if (line.startsWith("[subpolModuleWhenMerge]"))
                {
                    selection = 2;
                }
                else if (line.startsWith("[MultiLevelSubFuncModule]"))
                {
                    selection = 3;
                }

                else
                {

                    if (selection == 0)
                    {
                        smvTemplates = smvTemplates + "\r\n" + line;
                    }
                    else if (selection == 1)
                    {
                        smvMainTemplatesWhenMerge = smvMainTemplatesWhenMerge + "\r\n" + line;
                    }
                    else if (selection == 2)
                    {
                        smvSubPolTemplatesWhenMerge = smvSubPolTemplatesWhenMerge + "\r\n" + line;
                    }
                    else if (selection == 3)
                    {
                        smvMultiLevelTempletes = smvMultiLevelTempletes + "\r\n" + line;
                    }
                }

                line = br.readLine();
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("error: I could not open " + "!");
        }
        catch (IOException e)
        {
            System.err.println("error: File operations on " + "failed!");
        }

    }

    private static String reformat(String[] choices)
    {

        String results = "";
        for (int i = 0; i < choices.length; i++)
        {
            if (i == 0)
            {
                results = choices[i];
            }
            else
            {
                results = results + "," + choices[i];
            }
        }
        return results;
    }

    public static File getDlgSelectedFileName(String filetype, String filedesc)
    {

        File selected = null;
        JFileChooser fc = new JFileChooser();

        // Start in current directory
        fc.setCurrentDirectory(new File("."));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(filedesc, filetype);
        fc.setFileFilter(filter);

        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File saveFile = fc.getSelectedFile();
            if (saveFile.exists())
            {
                if (filter.accept(saveFile))
                {
                    int resp = JOptionPane.showConfirmDialog(null,
                            "The File already exists. Do you wish to change (overwrite) it?", "Saving...",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (resp == JOptionPane.YES_OPTION)
                    {
                        selected = saveFile;
                    }
                    else
                    {
                        return null;
                    }
                }

            }
            else
            {
                if (saveFile.getAbsolutePath().endsWith("." + filetype))
                {
                    selected = saveFile;
                }
                else
                {
                    selected = new File(saveFile.getAbsolutePath() + "." + filetype);
                }
            }
        }
        return selected;

    }

    public static boolean SoDGeneration(String type, File outFile, ACPTFrame frame, JTree sodTree, JTree propertyTree) {
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        DefaultMutableTreeNode sodRootNode = GenericTreeUtil.getRootNode(sodTree);
        String[] sod_subjects_set =
                GenericTreeUtil.getTargetTreeChildrenValues2StrArr(sodRootNode, GenericInfo.SOD_SUBJECTS_VALUES, 1);
        String[] sod_resources_set =
                GenericTreeUtil.getTargetTreeChildrenValues2StrArr(sodRootNode, GenericInfo.SOD_RESOURCES_VALUES, 1);
        String[] property_set =
                GenericTreeUtil.getTargetTreeChildrenValues2StrArr(propertyRootNode, GenericInfo.PROPERTY, 1);

        frame.logPanel.append("run SoD verification....");

        if (sod_subjects_set.length == 0 || sod_resources_set.length == 0) {
            frame.logPanel.append("sod verification error: must select subjects and resources in SoD panel");
            return false;
        }

        BufferedWriter output = null;
        try
        {
            output = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile, true)));
            output.write("\n");
            output.write("*** start SoD verification" + "\n");
            SoDGeneration(type, output, sod_subjects_set, sod_resources_set, property_set, outFile.getAbsolutePath());
            output.write("*** end of SoD verification" + "\n");
            output.close();
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean SoDGeneration(String type, BufferedWriter output, String[] sod_subjects_set,
                                        String[] sod_resources_set, String[] property_set, String filePath)
            throws IOException {
        Map<String, List<Boolean>> results = fetchNUSMVResult(type, filePath);
        if (results == null || results.isEmpty()) {
            return false;
        }
        for (Map.Entry<String, List<Boolean>> e: results.entrySet()) {
            List<Boolean> isAccessList = e.getValue();
            // can not match
            if (isAccessList.size() != property_set.length) {
                return false;
            }
            List<String> properties = filtrateAccessProperty(property_set, isAccessList);

            output.write("start " + e.getKey() + " in SoD verification" + "\n");

            doSoDGeneration(output, sod_subjects_set, sod_resources_set, properties);
        }
        return true;
    }

    private static List<String> filtrateAccessProperty(String[] property_set, List<Boolean> isAccessList) {
        List<String> propertyList = new ArrayList<>();
        for (int i = 0; i < isAccessList.size(); i++) {
            Boolean isAccess =  isAccessList.get(i);
            if (isAccess) {
                propertyList.add(property_set[i]);
            }
        }
        return propertyList;
    }

    private static Map<String, List<Boolean>> fetchNUSMVResult(String type, String filePath) {
        Map<String, List<Boolean>> combinationMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {
            String line = null;
            while ((line = br.readLine()) != null)
            {
                if (line.startsWith("-- specification")) {
                    handleNUSMVResult(type, combinationMap, line);
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return combinationMap;
    }

    private static void handleNUSMVResult(String type, Map<String, List<Boolean>> combinationMap, String line) {
        String policy_str = null;
        if (type.equals("Combination")) {
            policy_str = "merged policies";
        }
        else if (type.equals("Merge")) {
            policy_str = StringUtils.substringBetween(line, "IN ", " is");
        }

        if (combinationMap.get(policy_str) == null) {
            List<Boolean> results = new ArrayList<>();
            results.add(getNUSMVResultByLine(line));
            combinationMap.put(policy_str, results);
        }
        else {
            List<Boolean> results = combinationMap.get(policy_str);
            results.add(getNUSMVResultByLine(line));
        }
    }

    private static boolean getNUSMVResultByLine(String line) {
        return Boolean.valueOf(StringUtils.substringAfterLast(line, "is ")) ;
    }


    public static boolean doSoDGeneration(BufferedWriter output, String[] sod_subjects_set, String[] sod_resources_set,
                                          List<String> property_set) throws IOException {
        // there is no need to do sod verification
        if (property_set.size() == 1 || property_set.size() == 0) {
            output.write("warn: only one true or all false, there is no need to do sod verification" + "\n");
            return true;
        }

        // {Man = li: true, Man = sun: true} means {subject: isAccess}
        Map<String, Boolean> subjectsMap = new HashMap<>();
        if (sod_subjects_set != null && sod_subjects_set.length != 0) {
            for (String subject : sod_subjects_set) {
                subjectsMap.put(parseSodValue(subject), Boolean.TRUE);
            }
        }

        // {File = 1: true, File = 2: true} means {resource: isAccess}
        Map<String, Boolean> resourcesMap = new HashMap<>();
        if (sod_resources_set != null && sod_resources_set.length != 0) {
            for (String resource : sod_resources_set) {
                resourcesMap.put(parseSodValue(resource), Boolean.TRUE);
            }
        }

        // handle properties
        List<Property> propertyList = parseProperty(property_set, sod_subjects_set, sod_resources_set);
        for (Property property : propertyList) {
            if (property.isMatch()) {
                boolean isAccess = subjectsMap.get(property.getSubject())
                        && resourcesMap.get(property.getResource());
                if (isAccess) {
                    subjectsMap.put(property.getSubject(), Boolean.FALSE);
                    resourcesMap.put(property.getResource(), Boolean.FALSE);
                    // access & write file
                    output.write(property.getSource() + ": " + Boolean.TRUE.toString() + "\n");
                }
                else {
                    // deny & write file
                    output.write(property.getSource() + ": " + Boolean.FALSE.toString() + "; reason: " +
                            "conflict with SoD" + "\n");
                }
            }
            else {
                // not match & write file
                output.write(property.getSource() + ": " + "not match" + "\n");
            }
        }
        return true;
    }

    private static List<Property> parseProperty(List<String> property_set, String[] sod_subjects_set, String[] sod_resources_set) {
        List<Property> properties = new ArrayList<>();
        for (String propertyStr : property_set) {
            properties.add(doParseProperty(propertyStr, sod_subjects_set, sod_resources_set));
        }
        return properties;
    }

    private static Property doParseProperty(String propertyStr, String[] sod_subjects_set, String[] sod_resources_set) {
        Property property = new Property();

        property.setSource(propertyStr);

        boolean match_subject = false;
        boolean match_resource = false;

        // begin match
        for (String subject : sod_subjects_set) {
            subject = parseSodValue(subject);
            if (propertyStr.indexOf(subject) > 0) {
                match_subject = true;
                property.setSubject(subject);
                break;
            }
        }

        for (String resource : sod_resources_set) {
            resource = parseSodValue(resource);
            if (propertyStr.indexOf(resource) > 0) {
                match_resource = true;
                property.setResource(resource);
                break;
            }
        }

        property.setMatch(match_subject && match_resource);
        return property;
    }

    private static String parseSodValue(String resource) {
        // eg: File;1 -> File = 1
        if (StringUtils.isNotEmpty(resource) && resource.contains(";")) {
            resource = StringUtils.replace(resource, ";", " = ");
        }
        return resource;
    }

    public static void main(String[] args) {
        // SPEC (Student = Bruce) &amp; (IsValidated = True) &amp; (IsTeachsAid = True) &amp; (HasTeacher = Ms_Lux) &amp; (File = 2) &amp; (MLSDefaultAction = write)  -> decision =  Permit
        String[] sod_subjects_set = {"man;sun", "man;li"};
        String[] sod_resources_set = {"book;a", "book;b"};
        String[] property_set = {
            "SPEC (man = sun) &amp; (book = a) &amp; (MLSDefaultAction = read)  -> decision =  Permit",
            "SPEC (man = sun) &amp; (book = a) &amp; (MLSDefaultAction = write)  -> decision =  Permit",
            "SPEC (man = li) &amp; (book = a) &amp; (MLSDefaultAction = read)  -> decision =  Permit",
            "SPEC (man = li) &amp; (book = b) &amp; (MLSDefaultAction = read)  -> decision =  Permit"
        };


        File outFile = new File("results\\nu-out-496808853.txt");

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(outFile, true)));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            out.append("\n");
            SoDGeneration("Merge", out, sod_subjects_set, sod_resources_set, property_set, outFile.getAbsolutePath());
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        //SoDGeneration("Merge", sod_subjects_set, sod_resources_set, property_set, "D:\\IdeaProjects\\ACPT-UARK-source-2\\results\\nu-out--1543322863.txt");
        //SoDGeneration("Merge", ,sod_subjects_set, sod_resources_set, property_set, "D:\\IdeaProjects\\ACPT-UARK-source-2\\results\\nu-out-1612785790.txt");
    }

}
