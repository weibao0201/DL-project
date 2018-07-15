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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;

/**
 * This class implements the (XACML) model editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class TestPanelUtil extends JPanel
{

    public static List<DefaultMutableTreeNode> CombinatorialTestGeneration(ACPTFrame frame, String Fireeye,
            List<DefaultMutableTreeNode> selectedNodeLst, int N_way, File selected)
    {

        PrintStream input = null;

        if (selected == null)
        {
            frame.logPanel.append("No file is selected for Test generation ");
            JOptionPane.showMessageDialog(null, "No file is selected for Test generation ", "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }

        String inputFileName = selected.getAbsoluteFile().toString() + ".tmp";
        String outputFileName = selected.getAbsoluteFile().toString();

        try
        {
            input = new PrintStream(new FileOutputStream(inputFileName));
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }

        List<TestAttribute> attributLst = new ArrayList<TestAttribute>();

        createInpufForFieeye(selectedNodeLst, attributLst, input);
        input.close();

        try
        {
            String line;
            //System.out.println("fireEye = " + Fireeye + "  inputFileName = " + inputFileName + " outputFileName = " + outputFileName);
            Process p = Runtime.getRuntime().exec("java -Ddoi=" + N_way + " -Doutput=csv -jar \"" + Fireeye
                    + "\" ActsConsoleManager  \"" + inputFileName + "\"" + " \"" + outputFileName + "\"");

            BufferedReader input2 = new BufferedReader(new InputStreamReader(p.getInputStream()));

            boolean IsAttribute = false;

            while ((line = input2.readLine()) != null)
            {

                // System.err.println("TestPanelUtil.CombinatorialTestGeneration():"+line);

                if (line.startsWith("Output file:"))
                {
                    frame.logPanel.append("Input file (created for test generation): " + inputFileName);

                }
                if (line.startsWith("After Output File") || line.startsWith("Coverage Check"))
                {
                    // last two lines may not necessary, and do not print them
                    // out.
                }
                else
                {
                    frame.logPanel.append(line);
                }

                // catch only lines to describe attribute elements. We make a
                // list for input attribute elements.
                // if (line.startsWith("Number of Params:")){
                if (line.startsWith("Parameters:"))
                {
                    IsAttribute = true;
                    // }else if(line.startsWith("Relations: Constraints:")){
                }
                else if (line.startsWith("Relations:") || line.startsWith("Relations: Constraints:"))
                {
                    IsAttribute = false;
                }
                else if (IsAttribute == true)
                {
                    // TestAttribute attr = new TestAttribute(line);
                    // attributLst.add(attr);
                }

            }

            // output.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println("I could not open " + outputFileName + "!");
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.err.println("File operations on " + outputFileName + "failed!");
        }

        return readTestPropertyTreeFromOutput(attributLst, outputFileName);
    }

    public static List<DefaultMutableTreeNode> readTestPropertyTreeFromOutput(List<TestAttribute> attributLst,
            String outputFileName)
    {

        List<DefaultMutableTreeNode> NuSMVtestSet = new ArrayList<DefaultMutableTreeNode>();
        BufferedReader br = null;
        String line = null;
        try
        {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(outputFileName)));
            line = br.readLine();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        List<String> SpecLst = new ArrayList<String>();

        int Matchinglinecount = 0;
        while (line != null)
        {
            // comments starts with # and some lines with empty string. avoid
            // those lines for parsing.
            if (!line.startsWith("#") && !line.equals(""))
            {

                StringTokenizer st = new StringTokenizer(line, ",");
                String SPEC = "SPEC ";
                int count = 0;
                int temp = 0;
                while (st.hasMoreTokens())
                {

                    String tk = st.nextToken();

                    // added to remove " in ACTS
                    tk = tk.replaceAll("\"", "").trim();


                    if (tk.equals("True"))
                    {
                        if (temp > 0)
                        {
                            SPEC += "&";
                        }
                        SPEC += "(" + attributLst.get(count).getName() + " = " + attributLst.get(count).getChoice() + ")";

                        temp++;

                    }
//                    if (tk.equals("*"))
//                    {
//                        SPEC += "(" + attributLst.get(count).getName() + " = "
//                                + attributLst.get(count).getElements().get(0) + ")";
//                    }
//                    else
//                    {
//                        SPEC += "(" + attributLst.get(count).getName() + " = " + tk + ")";
//                    }

                    count++;

                }

                // Line 0 describes a attribute set with their type info --
                // which is input (not output for combinatorial tests)
                if (Matchinglinecount != 0)
                {
                    SpecLst.add(SPEC + "->" + "decision = Permit");
                    SpecLst.add(SPEC + "->" + "decision = Deny");
                    SpecLst.add(SPEC + "->" + "decision = Non-applicable");
                }
                //
                // System.out.println(linecount);
                Matchinglinecount++;

            }

            try
            {
                line = br.readLine();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        for (String spc : SpecLst)
        {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(new GenericInfo(GenericInfo.PROPERTY, spc));
            // root.add(node);
            NuSMVtestSet.add(node);
        }

        return NuSMVtestSet;
    }

    public static void compare2gentestsWithOracles(String option, List polSet, JTree testpropTree, String nusmvOut,
            String out)
    {

        // System.err.println(out);

        PrintStream StreamOut = null;

        try
        {
            StreamOut = new PrintStream(new FileOutputStream(out), true);
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }

        // File f = new File(fireeyeOut);
        // while (!f.exists()){
        // }

        File f1 = new File(nusmvOut);
        while (!f1.exists())
        {

        }

        // endComment

        // check NuSMV out is done
        BufferedReader br = null;
        String line = null;

        Boolean pass = false;

        while (!pass)
        {
            try
            {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(nusmvOut)));
                line = br.readLine();
                while (line != null)
                {
                    // System.err.println(line);

                    line = NuSMVattrNameCovert.ConvertToFitNuSMFormatBack(line);
                    if (line.startsWith(NUSMVPanelUtil.endComment))
                    {
                        pass = true;
                    }
                    line = br.readLine();
                }
            }
            catch (FileNotFoundException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        /*
         * try { br = new BufferedReader (new InputStreamReader (new
         * FileInputStream (fireeyeOut))); line = br.readLine(); int
         * matchingLineCount = 0; while ( line!= null ){
         *
         * if (line.startsWith("#") || line.equals("")){
         * StreamOut.println(line); }else{ matchingLineCount++; }
         *
         * if (matchingLineCount == 1){ StreamOut.println("\n#Attributes");
         * StreamOut.println(line); StreamOut.println(
         * "# ****************************************************");
         * StreamOut.println("\n#Test Cases / Test Oracles"); }
         *
         * line = br.readLine();
         *
         * } } catch (IOException e) { e.printStackTrace(); }
         */

        // NuSMV output

        List<String> verificationResLst = new ArrayList<String>();
        try
        {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(nusmvOut)));
            line = br.readLine();
            // System.out.println("TestPanelUtil.compare2gentestsWithOracles():\t"
            // + line);
            while (line != null)
            {

                if (line.startsWith("-- specification"))
                {
                    if (line.endsWith("is false"))
                    {
                        verificationResLst.add("is false");
                    }
                    else if (line.endsWith("is true"))
                    {
                        verificationResLst.add("is true");
                    }
                }
                line = br.readLine();

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // JTREE

        DefaultMutableTreeNode propertyRootNode = GenericTreeUtil.getRootNode(testpropTree);
        // String[] property_set =
        // GenericTreeUtil.getTargetTreeChildrenValues2StrArr(propertyRootNode,
        // GenericInfo.PROPERTY, 1);
        List property_set = GenericTreeUtil.getTargetTreeChildrenNode2List(propertyRootNode, GenericInfo.PROPERTY, 1);

        // check verificationResLst is same

        if (option.equals("Combination"))
        {

            int polNum = 0;

            StreamOut.println("\n## The test result for the combined policies");
            StreamOut.println("## For the combined policies, we assign default dummy values for unused attributes.");

            PrintOutResult2File(property_set, verificationResLst, polNum, StreamOut);

        }
        else if (option.equals("Merge"))
        {
            for (int i = 0; i < polSet.size(); i++)
            {
                int polNum = i;

                StreamOut.println("\n## The test result for " + polSet.get(i));

                PrintOutResult2File(property_set, verificationResLst, polNum, StreamOut);
            }

        }

        StreamOut.close();

    }

    private static void PrintOutResult2File(List property_set, List<String> verificationResLst, int polNum,
            PrintStream StreamOut)
    {

        // if (property_set == null) {
        // System.err.println("null property Set.class....okay");
        // }
        // if (verificationResLst == null) {
        // System.err
        // .println("verificationResLst null property Set.class....okay");
        // }

        // // 1008 test//
        // System.out.println("TestPanelUtil.PrintOutResult2File()");
        // System.err.println(verificationResLst.size());
        // for (int i = 0; i < verificationResLst.size(); i++) {
        // System.err.println(i+verificationResLst.get(i));
        // }
        // //-------------//

        //@Wei Bao U of A
        int index = 0;
        for (int i = 0; i < property_set.size(); i++)
        {
            String prop = property_set.get(i).toString();
            String result = verificationResLst.get(i + (polNum * property_set.size()));
            // String decision =
            String request = prop.replaceAll("SPEC", "");

            if (result.equals("is true"))
            {
                StreamOut.println("\n");
                StreamOut.println((index + 1) + ":" + request);
                index++;
                //StreamOut.println(((i / 3) + 1) + ":" + request);
            }
        }

    }

    private static void createInpufForFieeye(List<DefaultMutableTreeNode> selectedNodeLst,
            List<TestAttribute> attributLst, PrintStream out)
    {

        out.println("[System]");
        out.println("Name: Fireeye Input\n");
        out.println();

        out.println();
        out.println("[Parameter]");

        //@Wei Bao U of A
        for (DefaultMutableTreeNode dm : selectedNodeLst)
        {

            String groupType =
                    ((TargetInfo) dm.getUserObject()).getTargetType().replaceAll("Attributes", "Attribute Values");
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(dm, groupType, TargetInfo.nodeLevel);

            String trueFalse = "True,False";

            for (int i = 0; i < choices.size(); i++)
            {
                out.println("-- The list of " + choices.get(i).toString());
                out.println("\t" + choices.get(i).toString() + " (enum) :" + trueFalse);
                out.println();

                TestAttribute attr = new TestAttribute(dm.toString(), choices.get(i).toString());
                attributLst.add(attr);
            }


//            String elements = "";
//            for (int j = 0; j < choices.size(); j++)
//            {
//                if (elements.equals(""))
//                {
//                    elements += choices.get(j).toString();
//                }
//                else
//                {
//                    elements += "," + choices.get(j).toString();
//                }
//            }
//
//            out.println("\t" + dm.toString() + " (enum) :" + elements);


            // System.out.println("TestPanelUtil.createInpufForFieeye()"+dm.toString());
            // System.out.println("TestPanelUtil.createInpufForFieeye()"+elements);

//            out.println();
        }


        out.println("[Relation]\n");
        out.println();
        out.println("[Constraint]\n");
        out.println();
        out.println("[Misc]\n");
        out.println();

        return;

    }

    private static void createInpufForFieeye(JTree targetTree, PrintStream out)
    {

        out.println("[System]");
        out.println("Name: Fireeye Input\n");
        out.println();

        String types[] =
                {
                        TargetInfo.SUBJECTS, TargetInfo.SUBJECT_ATTRIBUTES, TargetInfo.RESOURCES, TargetInfo.ACTIONS,
                        TargetInfo.ENVIRONMENTS
        };

        String elements[] = new String[types.length];

        DefaultMutableTreeNode rootNode = TargetTreeUtil.getRootNode(targetTree);

        out.println();
        out.println("[Parameter]");

        for (int i = 0; i < types.length; i++)
        {
            String choices[] = TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode, types[i], 2);
            elements[i] = reformat(choices);
            if (choices.length > 0)
            {
                out.println("-- The list of " + types[i]);
            }
            out.println("\t" + types[i] + ":" + elements[i]);
            out.println();
        }

        out.println("[Relation]\n");
        out.println();
        out.println("[Constraint]\n");
        out.println();
        out.println("[Misc]\n");
        out.println();

        return;

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

    public static File getDlgOpenFileName(String filetype, String filedesc)
    {

        File selected = null;
        JFileChooser fc = new JFileChooser();

        // Start in current directory
        fc.setCurrentDirectory(new File("."));

        FileNameExtensionFilter filter = new FileNameExtensionFilter(filedesc, filetype);
        fc.setFileFilter(filter);

        if (fc.showOpenDialog(null) == JFileChooser.OPEN_DIALOG)
        {
            File saveFile = fc.getSelectedFile();
            if (!saveFile.exists())
            {
                int resp = JOptionPane.showConfirmDialog(null, "Error: the File does not exist.", "Saving...",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

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

    public static File getDlgLoadingFileName(String filetype, String filedesc)
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
            if (!saveFile.exists())
            {
                int resp = JOptionPane.showConfirmDialog(null, "Error: the File does not exist.", "Saving...",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

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

    public static File getDlgSelectedFileName(String filename, String filetype, String filedesc)
    {

        File selected = null;
        JFileChooser fc = new JFileChooser();

        // Start in current directory
        fc.setCurrentDirectory(new File("."));
        fc.setSelectedFile(new File(filename));
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

}
