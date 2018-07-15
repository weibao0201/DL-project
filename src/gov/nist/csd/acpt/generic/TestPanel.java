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

//import sun.misc.IOUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.model.ModelTreeUtil;
import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.Generalproperties;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements the (XACML) target editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class TestPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long     serialVersionUID         = 0;
    public static List            resultLst                = new ArrayList();

    /***************************************************************************
     * Variables
     **************************************************************************/

    // public String selectedModelType = null;
    // public String selectedCombineTreeType = GenericInfo.MODELCOMBINE;
    // public DefaultMutableTreeNode modeltreeRootNode = null;
    // public DefaultMutableTreeNode combinetreeRooNode = null;
    // public DefaultMutableTreeNode targetParentNode = null;

    DefaultMutableTreeNode        targetSubjectRootNode    = null;
    DefaultMutableTreeNode        targetResourceRootNode   = null;
    DefaultMutableTreeNode        targetActionRootNode     = null;
    DefaultMutableTreeNode        targetEnviromentRootNode = null;
    public DefaultMutableTreeNode modeltreeRootNode        = null;
    public DefaultMutableTreeNode combinetreeRootNode      = null;

    public int                    attrLevel                = 1;
    public int                    nodeLevel                = 2;

    public JButton                selectButton             = null;
    public JButton                viewResultButton         = null;

    public JButton                MergeNuSMVButton         = null;
    public JButton                CombinationNuSMVButton   = null;

    public JComboBox              MergeactionComboBox      = null;
    public JComboBox              CombineactionComboBox    = null;

    public JCheckBox              checkdefault             = null;

    public JTextField             valueField               = null;
    private ACPTFrame             frame                    = null;
    private JTree                 modelTree                = null;
    private JTree                 targetTree               = null;
    private JTree                 combineTree              = null;
    private GenericInfo           targetInfo               = null;
    private boolean               editable                 = false;
    private TitledBorder          titledBorder             = null;

    private String                Fireeye                  = null;
    private String                NuSMV                    = null;

    public JList                  list                     = null;
    public DefaultListModel       listmodel                = null;

    public JList                  grouplist                = null;
    public DefaultListModel       grouplistmodel           = null;

    public CheckCompLst           checkCompLst             = null;
    public CheckCompLst           checkCompLst2            = null;

    /* Adapters */
    public TestPanelActionAdapter testPanelActionAdapter   = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public TestPanel(ACPTFrame frame, JTree combineTree, GenericInfo targetInfo, boolean editable)
    {

        this.frame = frame;
        this.modelTree = frame.modelPanel.getModelTree();
        this.targetTree = frame.targetPanel.getTargetTree();
        // this.combineTree = combineTree;
        this.combineTree = frame.modelCombinePanel.getGenericTree();
        this.targetInfo = targetInfo;
        setVariables();
        setProperties();
        setAdapters();
        setPanels(targetInfo);
        setEditable(editable);

    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setProperties()
    {
        /*
         * Properties properties = new Properties(); try { properties.load(new
         * FileInputStream(GenericInfo.configfile)); } catch (Exception e) {
         * JOptionPane.showMessageDialog(null,
         * "Unable to load custom config properties", "Warning",
         * JOptionPane.INFORMATION_MESSAGE); return; }
         */
        Fireeye = Generalproperties.Fireeye;
        NuSMV = Generalproperties.NUSMV;
        combinetreeRootNode = GenericTreeUtil.getFirstMatchingNode(this.combineTree, GenericInfo.ROOT, 0);

        // properties.getProperty("Fireeye");
    }

    private void setVariables()
    {

        checkCompLst = new CheckCompLst();
        checkCompLst2 = new CheckCompLst();
        targetSubjectRootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.SUBJECTS, TargetInfo.Level);
        targetResourceRootNode =
                TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.RESOURCES, TargetInfo.Level);
        targetActionRootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.ACTIONS, TargetInfo.Level);
        targetEnviromentRootNode =
                TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.ENVIRONMENTS, TargetInfo.Level);

        modeltreeRootNode = ModelTreeUtil.getFirstMatchingNode(this.modelTree, ModelInfo.ROOT, 0);
        combinetreeRootNode = GenericTreeUtil.getFirstMatchingNode(this.combineTree, GenericInfo.ROOT, 0);

        // modeltreeRootNode =
        // ModelTreeUtil.getFirstMatchingNode(this.modelTree, ModelInfo.ROOT,
        // 0);

    }

    private void setAdapters()
    {

        this.testPanelActionAdapter = new TestPanelActionAdapter(this);

    }

    private void setPanels(GenericInfo targetInfo)
    {

        this.MergeNuSMVButton = new JButton("Test Generation for Merged Policies");
        this.MergeNuSMVButton.setPreferredSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.MergeNuSMVButton.setMaximumSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.MergeNuSMVButton.addActionListener(testPanelActionAdapter);
        this.MergeNuSMVButton.setActionCommand("MergeTest");

        this.CombinationNuSMVButton = new JButton("Test Generation for Combined Policies");
        this.CombinationNuSMVButton.setPreferredSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.CombinationNuSMVButton.setMaximumSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.CombinationNuSMVButton.addActionListener(testPanelActionAdapter);
        this.CombinationNuSMVButton.setActionCommand("CombinationTest");

        // this.selectButton = new JButton("Test Generation");
        // this.selectButton.setPreferredSize(
        // new Dimension(200, GraphicsUtil.FIELD_HEIGHT));
        // this.selectButton.setMaximumSize(
        // new Dimension(200, GraphicsUtil.FIELD_HEIGHT));
        // this.selectButton.addActionListener(testPanelActionAdapter);
        // this.selectButton.setActionCommand("Test");

        this.viewResultButton = new JButton("View Test");
        this.viewResultButton.setPreferredSize(new Dimension(100, GraphicsUtil.FIELD_HEIGHT));
        this.viewResultButton.setMaximumSize(new Dimension(100, GraphicsUtil.FIELD_HEIGHT));
        this.viewResultButton.addActionListener(testPanelActionAdapter);
        this.viewResultButton.setActionCommand("ViewResult");

        /******************** West panel *************************/

        int checkount = 0;

        JPanel Westpanel = new JPanel();
        Border Westborder = BorderFactory.createTitledBorder("Commands");
        Westpanel.setBorder(Westborder);
        ;
        Westpanel.setLayout(new BoxLayout(Westpanel, BoxLayout.Y_AXIS));
        // Westpanel.setPreferredSize(new Dimension(600, 300));
        // Westpanel.setMaximumSize(new Dimension(600, 300));

        JPanel panel = null;

        JPanel panelChkPols = new JPanel(new GridLayout(0, 2));
        Border border3 =
                BorderFactory.createTitledBorder("Select policies to be merged (without any order among policies)");
        panelChkPols.setBorder(border3);
        JCheckBox check = null;

        if (modeltreeRootNode != null)
        {

            String[] selectedType =
                    {
                            ModelInfo.RBAC, ModelInfo.ABAC, ModelInfo.MULTILEVEL, ModelInfo.WORKFLOW
            };

            for (int i = 0; i < selectedType.length; i++)
            {
                List choices =
                        ModelTreeUtil.getTargetTreeChildrenNode2List(modeltreeRootNode, selectedType[i], nodeLevel);

                for (int j = 0; j < choices.size(); j++)
                {
                    check = new JCheckBox(selectedType[i] + "#" + choices.get(j));
                    check.setActionCommand("Check:" + (checkount++));
                    check.addActionListener(testPanelActionAdapter);
                    checkCompLst.addCheck(check, selectedType[i] + "#" + choices.get(j));
                    panelChkPols.add(check);
                }
            }
        }

        Westpanel.add(panelChkPols);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel addActionLabel = new JLabel("t-way combinations: ", SwingConstants.CENTER);
        addActionLabel.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        addActionLabel.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        MergeactionComboBox = new JComboBox();

        // for (int i = 1; i < checkount; i++) {
        //Wei Bao  University of Arkansas
        for (int i = 1; i < 6; i++)
        {
            MergeactionComboBox.addItem(i + 1);
        }

        // actionComboBox.setSelectedIndex(0);
        MergeactionComboBox.setActionCommand("MergeComboBox");
        MergeactionComboBox.addActionListener(testPanelActionAdapter);
        MergeactionComboBox.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        MergeactionComboBox.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        panel.add(addActionLabel);
        panel.add(MergeactionComboBox);
        panel.add(MergeNuSMVButton);
        Westpanel.add(panel);
        Westpanel.add(Box.createRigidArea(new Dimension(250, GraphicsUtil.FIELD_HEIGHT)));

        // start addition

        int checkount2 = 0;

        JPanel panelChkPols2 = new JPanel(new GridLayout(0, 1));
        // JPanel panelChkPols2 = new JPanel();
        // panelChkPols2.setLayout(new BoxLayout(panelChkPols2,
        // BoxLayout.Y_AXIS));

        // GridBagConstraints c = new GridBagConstraints();
        // c.fill = GridBagConstraints.VERTICAL;
        // c.insets = new Insets(5,5,10,5);

        String CombineAlg = ((GenericInfo) combinetreeRootNode.getUserObject()).getCombiningAlgorithm();
        Border border4 = BorderFactory
                .createTitledBorder("Combined Policies (Precedence based on " + CombineAlg + " combination)");

        panelChkPols2.setBorder(border4);
        JCheckBox check2 = null;

        if (combinetreeRootNode != null)
        {

            // String[] selectedType = {ModelInfo.RBAC, ModelInfo.ABAC,
            // ModelInfo.MULTILEVEL, ModelInfo.WORKFLOW};
            //
            // for (int i = 0; i < selectedType.length; i++) {
            List choices =
                    GenericTreeUtil.getTargetTreeChildrenValues2List(combinetreeRootNode, GenericInfo.MODELCOMBINE, 1);

            for (int j = 0; j < choices.size(); j++)
            {

                JPanel temp = new JPanel();
                temp.setPreferredSize(new Dimension(100, GraphicsUtil.FIELD_HEIGHT));
                temp.setMaximumSize(new Dimension(120, GraphicsUtil.FIELD_HEIGHT));
                temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));

                check2 = new JCheckBox("");
                check2.setActionCommand("Check:" + (checkount++));
                check2.addActionListener(testPanelActionAdapter);
                checkCompLst2.addCheck(check2, choices.get(j).toString());
                // JLabel addResourceLabel = new JLabel("Resource: ",
                // JLabel.LEFT);
                // temp.add(new JLabel((j+1)+". " + choices.get(j).toString() +
                // " (default deny rule check", JLabel.LEFT));
                // temp.add(check2);

                JLabel valueLabel = new JLabel(
                        (j + 1) + ". " + choices.get(j).toString() + "  (default deny rule check", SwingConstants.LEFT);
                JLabel valueLabel2 = new JLabel(")", SwingConstants.LEFT);

                temp.add(valueLabel);
                temp.add(check2);
                temp.add(valueLabel2);

                panelChkPols2.add(temp);

            }
            // }
        }

        Westpanel.add(panelChkPols2);

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        addActionLabel = new JLabel("t-way combinations: ", SwingConstants.CENTER);
        addActionLabel.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        addActionLabel.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        CombineactionComboBox = new JComboBox();
        // for (int i = 1; i < checkount; i++) {
        // @Wei Bao University of Arkansas
        for (int i = 1; i < 6; i++)
        {
            CombineactionComboBox.addItem(i + 1);
        }

        CombineactionComboBox.setActionCommand("CombineComboBox");
        CombineactionComboBox.addActionListener(testPanelActionAdapter);
        CombineactionComboBox.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        CombineactionComboBox.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        panel.add(addActionLabel);
        panel.add(CombineactionComboBox);
        panel.add(CombinationNuSMVButton);

        Westpanel.add(panel);

        // Westpanel.add(Box.createRigidArea(new
        // Dimension(10,GraphicsUtil.DEFAULT_GAP)));
        // Westpanel.add(panel);
        // Westpanel.add(Box.createRigidArea(new
        // Dimension(10,GraphicsUtil.DEFAULT_GAP)));

        /******************** East panel *************************/
        // EAST Panel

        JPanel Eastpanel = new JPanel();
        Border Eastborder = BorderFactory.createTitledBorder("Results");
        Eastpanel.setBorder(Eastborder);
        Eastpanel.setPreferredSize(new Dimension(200, 10));
        Eastpanel.setMaximumSize(new Dimension(200, 10));
        Eastpanel.setLayout(new BoxLayout(Eastpanel, BoxLayout.Y_AXIS));
        grouplistmodel = new DefaultListModel();
        grouplist = new JList(grouplistmodel);

        for (int i = 0; i < resultLst.size(); i++)
        {
            grouplistmodel.addElement(resultLst.get(i));
        }

        grouplist.setEnabled(true);
        JScrollPane grouplistScroller = new JScrollPane(grouplist);
        // grouplistScroller.setAlignmentX(LEFT_ALIGNMENT);
        grouplistScroller.setPreferredSize(new Dimension(400, 250));// 400
        grouplistScroller.setMaximumSize(new Dimension(400, 250));

        Eastpanel.add(grouplistScroller, BorderLayout.CENTER);

        JPanel Btnpanel = new JPanel(new GridLayout(0, 2));
        Btnpanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));
        Btnpanel.add(this.viewResultButton);
        Eastpanel.add(Btnpanel);
        // Eastpanel.add(this.viewResultButton);

        /********************
         * Combine East and West panel
         *************************/

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(Westpanel);
        splitPane.setRightComponent(Eastpanel);
        splitPane.setDividerLocation(500);
        splitPane.setBorder(null);
        this.add(splitPane);
    }

    /***************************************************************************
     * Public methods
     **************************************************************************/

    public void setEditable(boolean editable)
    {

        this.editable = editable;
        // this.valueField.setEnabled(editable);
        // this.updateValueButton.setEnabled(editable);

    }

    /***************************************************************************
     * Inner classes
     **************************************************************************/

    /**
     * This inner class implements the target panel mouse adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class TestPanelActionAdapter implements ActionListener
    {

        TestPanel testPanel = null;

        TestPanelActionAdapter(TestPanel subjectsPanel)
        {

            this.testPanel = subjectsPanel;

        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getActionCommand().equals("MergeTest"))
            {
                // System.err.println("MergeTest");
                List<List> selectedNodeLstSet = new ArrayList<List>();
                List SelectedpolSet = checkCompLst.getSelectedNodeLst();
                selectedNodeLstSet = findselectedNodeLst(SelectedpolSet);

                JComboBox SelectedComboBox = MergeactionComboBox;

                List polSet = SelectedpolSet;

                int Rand = new Random().nextInt();
                String source = "test-" + Rand + ".txt";

                List<DefaultMutableTreeNode> NuSMVTestLst = new ArrayList<DefaultMutableTreeNode>();

                if (polSet.size() < 1)
                {
                    JOptionPane.showMessageDialog(null,
                            "Select more than one policies to be merged by checking checkbox", "Warning",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                for (int i = 0; i < selectedNodeLstSet.size(); i++)
                {
                    List<DefaultMutableTreeNode> selectedNodeLst = selectedNodeLstSet.get(i);
                    if (checkError(selectedNodeLst, SelectedComboBox) == -1)
                    {
                        return;
                    }
                    int pairwiseNum = Integer.parseInt(SelectedComboBox.getSelectedItem().toString());
                    NuSMVTestLst.addAll(
                            GenerateNuSMVTestLst(selectedNodeLst, pairwiseNum, "test-" + Rand + "_" + i + ".txt"));
                }

                JTree TestpropTree = GeneratePropertyTreeOfNuSMVTest(NuSMVTestLst);
                GenerateTestResult("Merge", polSet, SelectedComboBox, source, TestpropTree);

                frame.logPanel.append("Test oracle creation is finished....");
                String elm = "TEST: " + source;

                resultLst.add(elm);
                grouplistmodel.addElement(elm);
                grouplist.setSelectedIndex(grouplist.getLastVisibleIndex());

                // NUSMVPanelUtil.NUSMVGeneration("Merge", polSet, frame,
                // modelTree, targetTree, propertyTree , combineTree, selected,
                // "results\\"+ result, NuSMV, false);

            }
            else if (e.getActionCommand().equals("CombinationTest"))
            {

                List<List> selectedNodeLstSet = new ArrayList<List>();
                List SelectedpolSet = GenericTreeUtil.getTargetTreeChildrenValues2List(combinetreeRootNode,
                        GenericInfo.MODELCOMBINE, 1);

                selectedNodeLstSet = findselectedNodeLst(SelectedpolSet);

                // Check selected attribute size is out of bound or less than
                // selected numeric # of combo box.

                // public JComboBox MergeactionComboBox = null;
                // public JComboBox CombineactionComboBox = null;

                JComboBox SelectedComboBox = CombineactionComboBox;

                int Rand = new Random().nextInt();
                String source = "test-" + Rand + ".txt";

                List<DefaultMutableTreeNode> NuSMVTestLst = new ArrayList<DefaultMutableTreeNode>();
                for (int i = 0; i < selectedNodeLstSet.size(); i++)
                {

                    List<DefaultMutableTreeNode> selectedNodeLst = selectedNodeLstSet.get(i);
                    if (checkError(selectedNodeLst, SelectedComboBox) == -1)
                    {
                        return;
                    }
                    int pairwiseNum = Integer.parseInt(SelectedComboBox.getSelectedItem().toString());

                    // For combination, we add dummies
                    List<DefaultMutableTreeNode> initialNuSMVTestLst =
                            GenerateNuSMVTestLst(selectedNodeLst, pairwiseNum, "test-" + Rand + "_" + i + ".txt");
                    List<DefaultMutableTreeNode> DummyAddedNuSMVTestLst =
                            AddDummyValuesForTests(initialNuSMVTestLst, selectedNodeLst, selectedNodeLstSet);

                    NuSMVTestLst.addAll(DummyAddedNuSMVTestLst);
                }

                List polSet = GenericTreeUtil.getTargetTreeChildrenValues2List(combinetreeRootNode,
                        GenericInfo.MODELCOMBINE, 1);

                if (polSet.size() < 1)
                {
                    JOptionPane.showMessageDialog(null,
                            "There is no policies to be combined. Use a combination tab to combine policies.",
                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // add dummy values for unused
                // List<DefaultMutableTreeNode> DummyAddedNuSMVTestLst =
                // AddDummyValuesForTests(NuSMVTestLst, selectedNodeLstSet);

                JTree TestpropTree = GeneratePropertyTreeOfNuSMVTest(NuSMVTestLst);

                GenerateTestResult("Combination", polSet, SelectedComboBox, source, TestpropTree);

                frame.logPanel.append("Test oracle creation is finished....");
                String elm = "TEST: " + source;

                resultLst.add(elm);
                grouplistmodel.addElement(elm);
                grouplist.setSelectedIndex(grouplist.getLastVisibleIndex());

            }
            // if (e.getActionCommand().startsWith("Check")) {
            //
            // }
            // else if (e.getActionCommand().equals("Select All Attributes")) {
            // checkCompLst.selectAll();
            // }
            // else if (e.getActionCommand().equals("Deselect All Attributes"))
            // {
            // checkCompLst.deselectAll();
            // }
            /*
             * else if
             * (e.getActionCommand().equals("CombinationTest--------sample")) {
             *
             *
             * List<DefaultMutableTreeNode> selectedNodeLst = new
             * ArrayList<DefaultMutableTreeNode>(); List SelectedpolSet =
             * GenericTreeUtil.getTargetTreeChildrenValues2List(
             * combinetreeRootNode, GenericInfo.MODELCOMBINE, 1); for (int i =
             * 0; i < SelectedpolSet.size(); i++) {
             * System.out.println(SelectedpolSet.get(i)); // find rule and what
             * elements are used ok? String policyModelTypeName =
             * SelectedpolSet.get(i).toString(); String model[] =
             * policyModelTypeName.split("#"); String modeltype = model[0];
             * String modelvalue = model[1];
             *
             *
             * DefaultMutableTreeNode selectedModelRootNode =
             * ModelTreeUtil.getFirstMatchingNodeOnTypeValue (modelTree,
             * modeltype, modelvalue, 2); String[] rules =
             * ModelTreeUtil.getTargetTreeChildrenValues2StrArr(
             * selectedModelRootNode, modeltype+"RULES", 4);
             *
             * for (int j = 0; j < rules.length; j++) {
             *
             * //String string =
             * "ActionClass;String: [Write, Read, Access, Pay]"; StringTokenizer
             * st = new StringTokenizer(rules[j], "[,;:] "); int count = 0;
             * while (st.hasMoreTokens()) { System.out.println(st.nextToken());
             * // if (count == 0){name} // else // // count++; }
             *
             * } }
             *
             * // checkCompLst.getSelectedNodeLst(); //
             *
             * // check pair-wise is less than its value.
             *
             * System.err.println(selectedNodeLst.size());
             *
             * if (selectedNodeLst.size() < 1){
             * JOptionPane.showMessageDialog(null,
             * "Cannot generate tests. The number of attributes in given policies should be at least 2"
             * , "Warning", JOptionPane.INFORMATION_MESSAGE); return; }
             *
             * if (CombineactionComboBox.getSelectedIndex()<0 ||
             * selectedNodeLst.size() <
             * Integer.parseInt(CombineactionComboBox.getSelectedItem().toString
             * ())){ JOptionPane.showMessageDialog(null,
             * "Cannot generate tests. Selected # (t-way) should be at most the number of selected attributes"
             * , "Warning", JOptionPane.INFORMATION_MESSAGE); return; }
             *
             *
             * //File selected = TestPanelUtil.getDlgSelectedFileName("txt",
             * "TXT Files"); //File selected = new File ("test.txt");
             * //TestPanelUtil.CombinatorialTestGeneration(frame, Fireeye,
             * targetTree , selected);
             *
             * int randomInt = (new Random()).nextInt(); String source = "test-"
             * +randomInt+".txt";
             *
             * String testdata = source + ".test"; File selected = new File
             * ("results\\"+testdata);
             *
             *
             * JTree TestpropTree =
             * TestPanelUtil.CombinatorialTestGeneration(frame, Fireeye,
             * selectedNodeLst ,
             * Integer.parseInt(CombineactionComboBox.getSelectedItem().toString
             * ()), selected);
             *
             * if (TestpropTree == null){ JOptionPane.showMessageDialog(null,
             * "Cannot generate a property tree with generated tests.",
             * "Warning", JOptionPane.INFORMATION_MESSAGE); return; }
             *
             * List polSet = GenericTreeUtil.getTargetTreeChildrenValues2List(
             * combinetreeRootNode, GenericInfo.MODELCOMBINE, 1); File
             * selectedsmv = new File ("results\\"+source+".smv"); String result
             * = source+".smvOut"; boolean defaultDenyEnabled =
             * checkdefault.isSelected();
             * NUSMVPanelUtil.NUSMVGeneration("Combination", polSet, frame,
             * modelTree, targetTree, TestpropTree , combineTree, selectedsmv,
             * "results\\"+ result, NuSMV,defaultDenyEnabled);
             *
             * // wait Till a nusmv out file is created.. frame.logPanel.append(
             * "It would take time for generating Test oracles..."); boolean
             * exists = (new File("results\\"+ result)).exists(); int timecount
             * = 0; if (!exists) { // wait(1000);
             *
             * try {
             *
             * frame.logPanel.append("."); Thread.sleep(1000); } catch
             * (InterruptedException e1) { e1.printStackTrace(); } if (timecount
             * >= 100){ //frame.logPanel.append("time out..."); // return; }
             * exists = (new File("results\\"+ result)).exists(); timecount++; }
             * frame.logPanel.append("Test oracle creation is finished....");
             *
             *
             * // compare and getdata TestPanelUtil.compare2gentestsWithOracles(
             * "results\\"+testdata, TestpropTree, "results\\"+ result, "
             * results\\"+source);
             *
             *
             *
             * String elm = "TEST: "+source; // String elm = "TEST: "
             * +source+".smv"; // String elm = "TEST: "+source+".smvOut";
             *
             * resultLst.add(elm); grouplistmodel.addElement(elm);
             * grouplist.setSelectedIndex(grouplist.getLastVisibleIndex());
             *
             * }
             */
            else if (e.getActionCommand().equals("ViewResult"))
            {

                if (grouplist.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(null, "No testing results are found", "Warning",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                    // JOptionPane.showMessageDialog(null,
                    // "Please select an item from the list", "Warning",
                    // JOptionPane.INFORMATION_MESSAGE);
                    // return;
                }

                String selStr = grouplist.getSelectedValue().toString();
                StringTokenizer st = new StringTokenizer(selStr);
                while (st.hasMoreTokens())
                {
                    String tk = st.nextToken();

                    if (tk.endsWith("txt") || tk.endsWith("smv") || tk.endsWith("smvOut"))
                    {
                        viewDialog viewDig =
                                new viewDialog(frame, true, new File("results\\" + tk), GenericInfo.TXTFileType, true);
                    }
                }

            }
            else
            {

                // System.err.println("GroupPanel warning: command not
                // implemented: " +
                // e.getActionCommand());
            }

        }

        private List<DefaultMutableTreeNode> AddDummyValuesForTests(List<DefaultMutableTreeNode> initialNuSMVTestLst,
                List<DefaultMutableTreeNode> selectedNodeLst, List<List> selectedNodeLstSet)
        {

            List<DefaultMutableTreeNode> nonDuplicateAllusedNodeLst = new ArrayList<DefaultMutableTreeNode>();
            ;
            List<DefaultMutableTreeNode> addDummyNuSMVTestLst = new ArrayList<DefaultMutableTreeNode>();

            for (List<DefaultMutableTreeNode> NodeLst : selectedNodeLstSet)
            {
                for (DefaultMutableTreeNode node : NodeLst)
                {
                    if (!nonDuplicateAllusedNodeLst.contains(node))
                    {
                        nonDuplicateAllusedNodeLst.add(node);
                    }
                }
            }

            // find unused NodeLst and make a templates for tests
            String template = "";
            for (DefaultMutableTreeNode node : nonDuplicateAllusedNodeLst)
            {
                if (!selectedNodeLst.contains(node))
                {
                    template += "&(" + ((TargetInfo) node.getUserObject()).getvalueOfvalue() + " = dummy)";
                }
            }

            for (DefaultMutableTreeNode node : initialNuSMVTestLst)
            {

                String spc = ((GenericInfo) node.getUserObject()).getValue();
                spc = spc.substring(0, spc.indexOf('-')) + template + spc.substring(spc.indexOf('-'));
                addDummyNuSMVTestLst.add(new DefaultMutableTreeNode(new GenericInfo(GenericInfo.PROPERTY, spc)));
            }

            return addDummyNuSMVTestLst;
        }

        private JTree GeneratePropertyTreeOfNuSMVTest(List<DefaultMutableTreeNode> nuSMVTestLst)
        {

            DefaultMutableTreeNode root =
                    new DefaultMutableTreeNode(new GenericInfo(GenericInfo.ROOT, GenericInfo.PROPERTY));

            for (DefaultMutableTreeNode node : nuSMVTestLst)
            {
                root.add(node);
            }

            DefaultTreeModel treeModel = new DefaultTreeModel(root);
            JTree TestpropTree = new JTree(treeModel);

            return TestpropTree;
        }

        private void appendFile(String srFile, String dtFile)
        {
            try
            {

                File f1 = new File(srFile);
                File f2 = new File(dtFile);

                boolean exists = f1.exists();
                int timecount = 0;
                if (!exists)
                {
                    // wait(1000);
                    try
                    {

                        frame.logPanel.append(".");
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e1)
                    {
                        e1.printStackTrace();
                    }
                    if (timecount >= 100)
                    {
                        // frame.logPanel.append("time out...");
                        // return;
                    }
                    exists = f1.exists();
                    timecount++;
                }

                InputStream in = new FileInputStream(f1);

                // For Append the file.
                //

                OutputStream out = new FileOutputStream(f2, true);
                // For Overwrite the file.
                // OutputStream out = new FileOutputStream(f2);

                byte[] buf = new byte[1024];

                int len;

                while ((len = in.read(buf)) > 0)
                {

                    out.write(buf, 0, len);

                }

                in.close();

                out.close();

                System.out.println("File copied.");

            }

            catch (FileNotFoundException ex)
            {

                System.out.println(ex.getMessage() + " in the specified directory.");

                System.exit(0);

            }

            catch (IOException e)
            {

                System.out.println(e.getMessage());

            }

        }

        private List<List> findselectedNodeLst(List SelectedpolSet)
        {

            List<List> selectedNodeLst = new ArrayList<List>();

            List<DefaultMutableTreeNode> MLSselectedNodeLst = new ArrayList<DefaultMutableTreeNode>();
            List<DefaultMutableTreeNode> ABACWorkflowselectedNodeLst = new ArrayList<DefaultMutableTreeNode>();

            for (int i = 0; i < SelectedpolSet.size(); i++)
            {

                String polTypeNName = SelectedpolSet.get(i).toString();
                String model[] = polTypeNName.split("#");
                String modeltype = model[0];
                String modelvalue = model[1];
                DefaultMutableTreeNode selectedModelRootNode = ModelTreeUtil.getFirstMatchingNodeOnTypeValue(modelTree,
                        modeltype, modelvalue, ModelInfo.NodeLevelofmodel);

                // System.err.println(modelvalue);
                // System.err.println(selectedModelRootNode);

                List<DefaultMutableTreeNode> choices = new ArrayList<DefaultMutableTreeNode>();

                if (modeltype.equals(ModelInfo.MULTILEVEL))
                {

                    List<DefaultMutableTreeNode> sublist = ModelTreeUtil.getTargetTreeChildrenNode2List(
                            selectedModelRootNode, ModelInfo.MULTILEVELSUBJECTLEVELS, ModelInfo.NodeLevelofrule);
                    List<DefaultMutableTreeNode> reslist = ModelTreeUtil.getTargetTreeChildrenNode2List(
                            selectedModelRootNode, ModelInfo.MULTILEVELRESOURCELEVELS, ModelInfo.NodeLevelofrule);
                    DefaultMutableTreeNode defaultAction =
                            new DefaultMutableTreeNode(new ModelInfo(ModelInfo.MULTILEVELRule,
                                    TargetInfo.ACTION_ATTRIBUTES + ";MLSDefaultAction;String;read"));

                    for (int j = 0; j < sublist.size(); j++)
                    {
                        ModelInfo Subtarget = (ModelInfo) sublist.get(j).getUserObject();
                        DefaultMutableTreeNode SubtempNode = new DefaultMutableTreeNode(
                                new ModelInfo(ModelInfo.MULTILEVELRule, Subtarget.getvalueofvalue() + ";dummy"));

                        for (int k = 0; k < reslist.size(); k++)
                        {

                            MLSselectedNodeLst = new ArrayList<DefaultMutableTreeNode>();

                            ModelInfo Restarget = (ModelInfo) reslist.get(k).getUserObject();
                            DefaultMutableTreeNode RestempNode = new DefaultMutableTreeNode(
                                    new ModelInfo(ModelInfo.MULTILEVELRule, Restarget.getvalueofvalue() + ";dummy"));

                            choices.add(SubtempNode);
                            choices.add(RestempNode);
                            choices.add(defaultAction);

                            updateApplicableNodeLstfromChoices(choices, MLSselectedNodeLst);
                            choices.clear();
                            selectedNodeLst.add(MLSselectedNodeLst);

                        }
                    }

                }
                else if (modeltype.equals(ModelInfo.ABAC))
                {
                    choices = ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode, ModelInfo.ABACRule,
                            ModelInfo.NodeLevelofrule);
                }
                else if (modeltype.equals(ModelInfo.WORKFLOW))
                {
                    choices = ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode,
                            ModelInfo.WORKFLOWRule, ModelInfo.NodeLevelofrule);
                }

                updateApplicableNodeLstfromChoices(choices, ABACWorkflowselectedNodeLst);

            }

            if (ABACWorkflowselectedNodeLst.size() >= 2)
            {
                selectedNodeLst.add(ABACWorkflowselectedNodeLst);
            }
            return selectedNodeLst;
        }

        private List<DefaultMutableTreeNode> updateApplicableNodeLstfromChoices(List<DefaultMutableTreeNode> choices,
                List<DefaultMutableTreeNode> NodeLst)
        {

            // List<DefaultMutableTreeNode> NodeLst = new
            // ArrayList<DefaultMutableTreeNode>();

            for (int j = 0; j < choices.size(); j++)
            {
                ModelInfo target = (ModelInfo) choices.get(j).getUserObject();
                String tmp[] = target.getValue().split("#");

                for (int k = 0; k < tmp.length; k++)
                {

                    String elems[] = tmp[k].split(";");
                    if (elems.length == 4)
                    {
                        DefaultMutableTreeNode node = TargetTreeUtil.getFirstMatchingNode(targetTree, elems[1],
                                elems[0], TargetInfo.attrLevel);

                        if ((node != null) && !NodeLst.contains(node))
                        {
                            NodeLst.add(node);
                        }
                    }
                }

            }
            return NodeLst;

        }

        private List<DefaultMutableTreeNode> GenerateNuSMVTestLst(List<DefaultMutableTreeNode> selectedNodeLst,
                int pairwiseNum, String source)
        {

            // DefaultMutableTreeNode rootOfNode =
            String testdata = source + ".test";
            File selected = new File("results\\" + testdata);

            // generate combinatorial tests (and assign them as a property tree)

            List<DefaultMutableTreeNode> NuSMVtestLst =
                    TestPanelUtil.CombinatorialTestGeneration(frame, Fireeye, selectedNodeLst, pairwiseNum, selected);

            // List<DefaultMutableTreeNode> NuSMVtestLst
            // DefaultMutableTreeNode root = new DefaultMutableTreeNode(
            // new GenericInfo(GenericInfo.ROOT, GenericInfo.PROPERTY));
            // DefaultTreeModel treeModel = new DefaultTreeModel(root);
            // JTree TestpropTree = new JTree(treeModel);
            // return TestpropTree;

            // JTree TestpropTree

            // TestpropTree.getn

            // if (NuSMVtestLst.size() = 0){
            //
            // }
            // if (TestpropTree == null){
            // JOptionPane.showMessageDialog(null,
            // "Cannot generate a property tree with generated tests.",
            // "Warning",
            // JOptionPane.INFORMATION_MESSAGE);
            // return;
            // }
            return NuSMVtestLst;
        }

        private void GenerateTestResult(String option, List polSet, JComboBox SelectedComboBox, String source,
                JTree TestpropTree)
        {

            // Use NuSMV verification (with combination) using the property tree
            // which is test cases

            File selectedsmv = new File("results\\" + source + ".smv");
            String result = source + ".smvOut";

            //get NuSMV results
            List polSet_checked = checkCompLst2.getSelectedNodeLst();
            NUSMVPanelUtil.NUSMVGeneration(option, polSet, frame, modelTree, targetTree, TestpropTree, combineTree,
                    selectedsmv, "results\\" + result, NuSMV, polSet_checked);

            // wait Till a nusmv out file is created..Se
            frame.logPanel.append("It would take time for generating Test oracles...");
            boolean exists = (new File("results\\" + result)).exists();
            int timecount = 0;
            if (!exists)
            {
                // wait(1000);
                try
                {

                    frame.logPanel.append(".");
                    Thread.sleep(1000);
                }
                catch (InterruptedException e1)
                {
                    e1.printStackTrace();
                }
                if (timecount >= 100)
                {
                    // frame.logPanel.append("time out...");
                    // return;
                }
                exists = (new File("results\\" + result)).exists();
                timecount++;
            }

            // compare and getdata
            TestPanelUtil.compare2gentestsWithOracles(option, polSet, TestpropTree, "results\\" + result,
                    "results\\" + source);

            // TestPanelUtil.compare2gentestsWithOracles(option, polSet,
            // "results\\"+testdata, TestpropTree, "results\\"+ result,
            // "results\\"+source, Isappend);

        }

        private int checkError(List<DefaultMutableTreeNode> selectedNodeLst, JComboBox twayComboBox)
        {
            if (selectedNodeLst.size() < 1)
            {
                JOptionPane.showMessageDialog(null,
                        "Cannot generate tests. The number of attributes in the given policies is "
                                + selectedNodeLst.size() + ". The number should be at least 2",
                        "Warning", JOptionPane.INFORMATION_MESSAGE);
                return -1;
            }

            if ((twayComboBox.getSelectedIndex() < 0)
                    || (selectedNodeLst.size() < Integer.parseInt(twayComboBox.getSelectedItem().toString())))
            {
                JOptionPane.showMessageDialog(null,
                        "Cannot generate tests. Selected number in the combo box should be at most "
                                + selectedNodeLst.size() + " (that is the number of attributes in the given policies)",
                        "Warning", JOptionPane.INFORMATION_MESSAGE);
                return -1;
            }
            return 0;
        }

    }

}
