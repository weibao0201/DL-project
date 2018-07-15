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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.model.ModelTreeUtil;
import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.Generalproperties;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * NuSMV Panel
 *
 * @author JeeHyun Hwang
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class NUSMVPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long      serialVersionUID        = 0;
    public static List             resultLst               = new ArrayList();

    /***************************************************************************
     * Variables
     **************************************************************************/

    // public String selectedModelType = null;
    public String                  selectedCombineTreeType = GenericInfo.MODELCOMBINE;
    public DefaultMutableTreeNode  modeltreeRootNode       = null;
    public DefaultMutableTreeNode  combinetreeRootNode     = null;
    public DefaultMutableTreeNode  targetParentNode        = null;

    public int                     attrLevel               = 1;
    public int                     nodeLevel               = 2;
    /*
     * public JButton updateValueButton = null;
     *
     * public JButton addAttributeButton= null; public JButton
     * updateAttributeButton= null; public JButton deleteAttributeButton= null;
     * public JButton addGroupButton= null; public JButton updateGroupButton=
     * null; public JButton deleteGroupButton= null;
     */
    public JButton                 MergeNuSMVButton        = null;
    public JButton                 CombinationNuSMVButton  = null;
    public JButton                 viewSourceButton        = null;
    public JButton                 viewResultButton        = null;

    public JCheckBox               checkdefault            = null;
    /*
     * public JButton deselectButton= null; public JButton upButton= null;
     * public JButton downButton= null;
     */
    public JTextField              valueField              = null;
    private ACPTFrame              frame                   = null;
    private JTree                  modelTree               = null;
    private JTree                  targetTree              = null;
    private JTree                  propertyTree            = null;
    private JTree                  combineTree             = null;
    private JTree                  sodTree                 = null;

    private GenericInfo            targetInfo              = null;
    private boolean                editable                = false;
    private TitledBorder           titledBorder            = null;

    private String                 NuSMV                   = null;

    public JList                   list                    = null;
    public DefaultListModel        listmodel               = null;

    public JList                   grouplist               = null;
    public DefaultListModel        grouplistmodel          = null;

    public CheckCompLst            checkCompLst            = null;
    public CheckCompLst            checkCompLst2           = null;

    /* Adapters */
    public NUSMVPanelActionAdapter nusmvPanelActionAdapter = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public NUSMVPanel(ACPTFrame frame, JTree combineTree, GenericInfo targetInfo, boolean editable)
    {

        this.frame = frame;
        this.modelTree = frame.modelPanel.getModelTree();
        this.targetTree = frame.targetPanel.getTargetTree();
        this.propertyTree = frame.propertyGenerationPanel.getGenericTree();
        this.combineTree = frame.modelCombinePanel.getGenericTree();
        this.sodTree = frame.sodPanel.getGenericTree();
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
         *
         * Fireeye = properties.getProperty("Fireeye");
         */

        NuSMV = Generalproperties.NUSMV;
    }

    private void setVariables()
    {
        checkCompLst = new CheckCompLst();
        checkCompLst2 = new CheckCompLst();
        modeltreeRootNode = ModelTreeUtil.getFirstMatchingNode(this.modelTree, ModelInfo.ROOT, 0);
        combinetreeRootNode = GenericTreeUtil.getFirstMatchingNode(this.combineTree, GenericInfo.ROOT, 0);

    }

    private void setAdapters()
    {

        this.nusmvPanelActionAdapter = new NUSMVPanelActionAdapter(this);

    }

    private void setPanels(GenericInfo targetInfo)
    {

        this.MergeNuSMVButton = new JButton("NuSMV Verification for Merged Policies");
        this.MergeNuSMVButton.setPreferredSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.MergeNuSMVButton.setMaximumSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.MergeNuSMVButton.addActionListener(nusmvPanelActionAdapter);
        this.MergeNuSMVButton.setActionCommand("MergeNUSMV");

        this.CombinationNuSMVButton = new JButton("NuSMV Verification for Combined Policies");
        this.CombinationNuSMVButton.setPreferredSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.CombinationNuSMVButton.setMaximumSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.CombinationNuSMVButton.addActionListener(nusmvPanelActionAdapter);
        this.CombinationNuSMVButton.setActionCommand("CombinationNUSMV");

        this.viewSourceButton = new JButton("NuSMV");
        this.viewSourceButton.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        this.viewSourceButton.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        this.viewSourceButton.addActionListener(nusmvPanelActionAdapter);
        this.viewSourceButton.setActionCommand("ViewSource");

        this.viewResultButton = new JButton("View Result");
        this.viewResultButton.setPreferredSize(new Dimension(200, GraphicsUtil.FIELD_HEIGHT));
        this.viewResultButton.setMaximumSize(new Dimension(200, GraphicsUtil.FIELD_HEIGHT));
        this.viewResultButton.addActionListener(nusmvPanelActionAdapter);
        this.viewResultButton.setActionCommand("ViewResult");

        /********************
         * attribute properties panel
         *************************/

        JPanel Westpanel = new JPanel();
        Border border = BorderFactory.createTitledBorder("Commands");
        Westpanel.setBorder(border);

        // Westpanel.setLayout(new BorderLayout());
        Westpanel.setLayout(new BoxLayout(Westpanel, BoxLayout.Y_AXIS));
        // this.add(Box.createRigidArea(new
        // Dimension(250,GraphicsUtil.FIELD_HEIGHT)));

        /*********/

        int checkount = 0;

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
                    // listmodel.addElement(selectedType[i]+"#"+choices[j]);

                    check = new JCheckBox(selectedType[i] + "#" + choices.get(j));
                    check.setActionCommand("Check:" + (checkount++));
                    check.addActionListener(nusmvPanelActionAdapter);
                    checkCompLst.addCheck(check, selectedType[i] + "#" + choices.get(j));
                    panelChkPols.add(check);
                }
            }
        }

        Westpanel.add(panelChkPols);
        Westpanel.add(MergeNuSMVButton);
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
                check2.addActionListener(nusmvPanelActionAdapter);
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

        // ends addition

        // TextArea
        /*
         * JTextArea textArea = new JTextArea(); JScrollPane scrollPane = new
         * JScrollPane(textArea);
         *
         * String info =
         * "Combined Policies (Precedence based on first-applicable combination)"
         * ; if (combinetreeRootNode != null){ List choices = (List)
         * GenericTreeUtil.getTargetTreeChildrenValues2List(combinetreeRootNode,
         * GenericInfo.MODELCOMBINE, 1); for (int j = 0; j < choices.size();
         * j++) { info += "\n"+(j+1)+". "+choices.get(j).toString(); } }
         *
         *
         * textArea.append(info); textArea.setLineWrap(true);
         * textArea.setWrapStyleWord(true); textArea.setEditable(false);
         * textArea.setBackground(null); scrollPane.setPreferredSize(new
         * Dimension(550, 100)); scrollPane.setMaximumSize(new Dimension(850,
         * 100)); Westpanel.add(scrollPane);
         *
         * checkdefault = new JCheckBox(
         * "Default deny rule for each combined policy");
         * checkdefault.setSelected(true); Westpanel.add(checkdefault);
         */
        Westpanel.add(CombinationNuSMVButton);

        // EAST Panel

        JPanel Eastpanel = new JPanel();
        Border Eastborder = BorderFactory.createTitledBorder("Results");
        Eastpanel.setBorder(Eastborder);
        Eastpanel.setPreferredSize(new Dimension(250, 0));
        Eastpanel.setMaximumSize(new Dimension(250, 0));
        // Westpanel.setLayout(new BorderLayout());
        Eastpanel.setLayout(new BoxLayout(Eastpanel, BoxLayout.Y_AXIS));
        grouplistmodel = new DefaultListModel();
        grouplist = new JList(grouplistmodel);

        // if (combineTree != null){
        // String[] choices =
        // GenericTreeUtil.getTargetTreeChildrenValues2StrArr(combinetreeRooNode,
        // selectedCombineTreeType, 1);
        // for (int i = 0; i < choices.length; i++) {
        // grouplistmodel.addElement(choices[i]);
        // }
        // }

        for (int i = 0; i < resultLst.size(); i++)
        {
            grouplistmodel.addElement(resultLst.get(i));
        }

        grouplist.setEnabled(true);
        JScrollPane grouplistScroller = new JScrollPane(grouplist);
        // grouplistScroller.setAlignmentX(LEFT_ALIGNMENT);
        grouplistScroller.setPreferredSize(new Dimension(400, 250));
        grouplistScroller.setMaximumSize(new Dimension(400, 250));

        Eastpanel.add(grouplistScroller, BorderLayout.CENTER);

        JPanel Btnpanel = new JPanel(new GridLayout(0, 2));
        Btnpanel.add(this.viewSourceButton);
        Btnpanel.add(this.viewResultButton);
        Eastpanel.add(Btnpanel);

        // this.add(Westpanel);
        // this.add(Eastpanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(Westpanel);
        splitPane.setRightComponent(Eastpanel);
        //// splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(500);
        splitPane.setBorder(null);
        this.add(splitPane);
        // JPanel tPanel = new JPanel();
        // tPanel.setLayout( new BorderLayout() );
        // //getContentPane().add( tPanel );

        // tPanel.add(splitPane, BorderLayout.CENTER);

        // horizontalFrame.add(splitPane, BorderLayout.CENTER);
        // targetPropertiesPanel = null;
        // System.gc();
        // targetPropertiesPanel = new TargetPropertiesPanel(
        // frame, targetTree, targetInfo, editable);
        //
        //
        // this.horizontalSplitPane
        // .setRightComponent(targetPropertiesPanel);
        // targetPanel.horizontalSplitPane.setDividerLocation(400);

        // JPanel Wholepanel = new JPanel();
        // Wholepanel.JSplitPane (JSplitPane.HORIZONTAL_SPLIT);
        // Wholepanel.horizontalSplitPane
        // .setRightComponent(targetPropertiesPanel);
        // targetPanel.horizontalSplitPane.setDividerLocation(400);
        //
        // this.add(Westpanel);

        // /*** combination addition info */
        // JPanel panel = new JPanel(new GridLayout(0, 1));
        // Border border = BorderFactory.createTitledBorder("Combinations of
        // Models (click combination tab to chagne)");
        // panel.setBorder(border);
        //
        // //JLabel infoLabel = new JLabel("Click combination tab to change the
        // order of selected models or selections of given models");
        // //this.add(infoLabel);
        // //panel.add(Box.createRigidArea(new
        // Dimension(150,GraphicsUtil.GAP_ZERO)));
        //
        //
        // grouplistmodel = new DefaultListModel();
        // grouplist = new JList(grouplistmodel);
        //
        // if (combineTree != null){
        // String[] choices =
        // GenericTreeUtil.getTargetTreeChildrenValues2StrArr(combinetreeRooNode,
        // selectedCombineTreeType, 1);
        // for (int i = 0; i < choices.length; i++) {
        // grouplistmodel.addElement(choices[i]);
        // }
        // }
        // grouplist.setEnabled(false);
        // JScrollPane grouplistScroller = new JScrollPane(grouplist);
        //// grouplistScroller.setAlignmentX(LEFT_ALIGNMENT);
        // grouplistScroller.setPreferredSize(new Dimension(150,
        // 250));
        // grouplistScroller.setMaximumSize(new Dimension(150,
        // 250));
        //
        // panel.add(grouplistScroller);
        // this.add (panel);
        /*** end combination addition info */

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
    // class CheckCompLst {
    //
    // public List<JCheckBox> checksLst = null;
    // public ArrayList<String> checksNodeLst = null;
    //
    // CheckCompLst() {
    //
    // checksLst = new ArrayList<JCheckBox>();
    // checksNodeLst = new ArrayList<String>();
    // }
    //
    // public boolean addCheck(JCheckBox check, String node) {
    // checksLst.add(check);
    // checksNodeLst.add(node);
    // return true;
    // }
    //
    // public boolean selectAll() {
    // for (JCheckBox a: checksLst) {
    // a.setSelected(true);
    // }
    // return true;
    // }
    //
    // public boolean deselectAll() {
    // for (JCheckBox a: checksLst) {
    // a.setSelected(false);
    // }
    // return true;
    // }
    //
    // public List<String> getSelectedNodeLst() {
    //
    // List<String> selectedNodeLst = new ArrayList<String>();
    // for (int i = 0; i < checksLst.size(); i++) {
    //
    // if (((JCheckBox) checksLst.get(i)).isSelected()){
    // selectedNodeLst.add(checksNodeLst.get(i));
    // }
    // }
    // return selectedNodeLst;
    // }
    //
    // }
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
    class NUSMVPanelActionAdapter implements ActionListener
    {

        NUSMVPanel nusmvPanel = null;

        NUSMVPanelActionAdapter(NUSMVPanel subjectsPanel)
        {

            this.nusmvPanel = subjectsPanel;

        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getActionCommand().equals("CombinationNUSMV"))
            {

                long startTime1 = System.currentTimeMillis();
                int randomInt = (new Random()).nextInt();
                String source = "nu-src-" + randomInt + ".smv";
                String result = "nu-out-" + randomInt + ".txt";
                // File selected = TestPanelUtil.getDlgSelectedFileName("smv",
                // "NuSMV Files");
                File selected = new File("results\\" + source);
                // DefaultMutableTreeNode combinetreeRootNode =
                // GenericTreeUtil.getRootNode (combineTree);
                List polSet = GenericTreeUtil.getTargetTreeChildrenValues2List(combinetreeRootNode,
                        GenericInfo.MODELCOMBINE, 1);

                // boolean defaultDenyEnabled = checkdefault.isSelected();

                List polSet_checked = checkCompLst2.getSelectedNodeLst();

                if (HasAttrErrorsForVerification())
                {
                    return;
                }
                boolean hasresult = NUSMVPanelUtil.NUSMVGeneration("Combination", polSet, frame, modelTree, targetTree,
                        propertyTree, combineTree, selected, "results\\" + result, NuSMV, polSet_checked);

                if (hasresult == false)
                {
                    return;
                }

                // sod verification
                NUSMVPanelUtil.SoDGeneration("Combination", new File("results\\" + result), frame, sodTree, propertyTree);

                String elm = "Source: " + source + " & Result: " + result;
                resultLst.add(elm);
                grouplistmodel.addElement(elm);
                grouplist.setSelectedIndex(grouplist.getLastVisibleIndex());

                long endTime1 = System.currentTimeMillis();
                String runTime1 = Long.toString(endTime1 - startTime1);
                System.out.println("Run time is " + runTime1 + "ms");
                frame.logPanel.append("Run time is " + runTime1 + "ms");

            }
            else if (e.getActionCommand().equals("MergeNUSMV"))
            {

                // File selected = TestPanelUtil.getDlgSelectedFileName("smv",
                // "NuSMV Files");

                long startTime2 = System.currentTimeMillis();
                int randomInt = (new Random()).nextInt();
                String source = "nu-src-" + randomInt + ".smv";
                String result = "nu-out-" + randomInt + ".txt";

                File selected = new File("results\\" + source);

                List polSet = checkCompLst.getSelectedNodeLst();

                if (HasAttrErrorsForVerification())
                {
                    return;
                }

                boolean hasresult = NUSMVPanelUtil.NUSMVGeneration("Merge", polSet, frame, modelTree, targetTree,
                        propertyTree, combineTree, selected, "results\\" + result, NuSMV, null);
                if (hasresult == false)
                {
                    return;
                }

                // sod verification
                NUSMVPanelUtil.SoDGeneration("Merge", new File("results\\" + result), frame, sodTree, propertyTree);

                String elm = "Source: " + source + " & Result: " + result;
                resultLst.add(elm);
                grouplistmodel.addElement(elm);
                grouplist.setSelectedIndex(grouplist.getLastVisibleIndex());
                long endTime2 = System.currentTimeMillis();
                String runTime2 = Long.toString(endTime2 - startTime2);
                System.out.println("Run time is " + runTime2 + "ms");
                frame.logPanel.append("Run time is " + runTime2 + "ms");

            }
            else if (e.getActionCommand().equals("ViewSource"))
            {

                if (grouplist.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(null, "No verification results are found", "Warning",
                            JOptionPane.INFORMATION_MESSAGE);
                    // JOptionPane.showMessageDialog(null,
                    // "Please select an item from the list", "Warning",
                    // JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                String selStr = grouplist.getSelectedValue().toString();
                StringTokenizer st = new StringTokenizer(selStr);
                while (st.hasMoreTokens())
                {
                    String tk = st.nextToken();

                    if (tk.endsWith(".smv"))
                    {
                        viewDialog viewDig =
                                new viewDialog(frame, true, new File("results\\" + tk), GenericInfo.SMVFileType, false);
                    }
                }

            }
            else if (e.getActionCommand().equals("ViewResult"))
            {

                if (grouplist.getSelectedIndex() == -1)
                {
                    JOptionPane.showMessageDialog(null, "No verification results are found", "Warning",
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

                    if (tk.endsWith(".txt"))
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

        private boolean HasAttrErrorsForVerification()
        {

            List<String> attrTypes = new ArrayList<String>();
            attrTypes.add(TargetInfo.SUBJECT_ATTRIBUTES);
            attrTypes.add(TargetInfo.RESOURCE_ATTRIBUTES);
            attrTypes.add(TargetInfo.ACTION_ATTRIBUTES);
            attrTypes.add(TargetInfo.ENVIRONMENT_ATTRIBUTES);

            String errorMsg = "\n";
            errorMsg += "Errors: each attribute has at least two entities to prevent errors during verification.\n";

            boolean hasError = false;
            if (targetTree != null)
            {
                DefaultMutableTreeNode rootNode = TargetTreeUtil.getRootNode(targetTree);

                for (int i = 0; i < attrTypes.size(); i++)
                {
                    List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, attrTypes.get(i),
                            TargetInfo.attrLevel);

                    for (int j = 0; j < choices.size(); j++)
                    {
                        DefaultMutableTreeNode attNode = (DefaultMutableTreeNode) choices.get(j);
                        if (attNode.getChildCount() < 2)
                        {
                            errorMsg += " - " + attNode.toString() + " attribute in " + attrTypes.get(i) + " has "
                                    + attNode.getChildCount() + " entities\n";
                            hasError = true;
                        }
                    }
                }

                // System.err.println("dummy"+
                // Generalproperties.DummyEnabledForAttributesOn);
                if (hasError && (Generalproperties.DummyEnabledForAttributesOn == false))
                {
                    JOptionPane.showMessageDialog(null, errorMsg, "Warning", JOptionPane.INFORMATION_MESSAGE);
                    return true;
                }

            }
            return false;
        }

    }
}
