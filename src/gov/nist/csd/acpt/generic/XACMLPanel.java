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

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.model.ModelTreeUtil;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements the (XACML) target editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class XACMLPanel extends JPanel
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
    // public JButton selectButton= null;

    public JButton                 MergeNuSMVButton        = null;
    public JButton                 CombinationNuSMVButton  = null;

    public JComboBox               MergeactionComboBox     = null;
    public JComboBox               CombineactionComboBox   = null;

    // public JButton viewSourceButton= null;
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
    private JTree                  combineTree             = null;
    private JTree                  genericTree             = null;
    private GenericInfo            targetInfo              = null;
    private boolean                editable                = false;
    private TitledBorder           titledBorder            = null;

    // private String XACML = null;

    public CheckCompLst            checkCompLst            = null;
    public CheckCompLst            checkCompLst2           = null;

    public JList                   list                    = null;
    public DefaultListModel        listmodel               = null;

    public JList                   grouplist               = null;
    public DefaultListModel        grouplistmodel          = null;

    /* Adapters */
    public XACMLPanelActionAdapter xacmlPanelActionAdapter = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public XACMLPanel(ACPTFrame frame, JTree genericTree, GenericInfo targetInfo, boolean editable)
    {

        this.frame = frame;
        this.modelTree = frame.modelPanel.getModelTree();
        this.targetTree = frame.targetPanel.getTargetTree();
        this.combineTree = frame.modelCombinePanel.getGenericTree();
        this.genericTree = genericTree;

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
         *
         * XACML = properties.getProperty("XACML");
         */

        // XACML = Generalproperties.XACMLTemplates;
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

        this.xacmlPanelActionAdapter = new XACMLPanelActionAdapter(this);

    }

    private void setPanels(GenericInfo targetInfo)
    {

        /*
         * this.selectButton = new JButton("XACML Generation");
         * this.selectButton.setPreferredSize( new Dimension(250,
         * GraphicsUtil.FIELD_HEIGHT)); this.selectButton.setMaximumSize( new
         * Dimension(250, GraphicsUtil.FIELD_HEIGHT));
         * this.selectButton.addActionListener(xacmlPanelActionAdapter);
         * this.selectButton.setActionCommand("XACML");
         */
        this.MergeNuSMVButton = new JButton("XACML for Merged Policies");
        this.MergeNuSMVButton.setPreferredSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.MergeNuSMVButton.setMaximumSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.MergeNuSMVButton.addActionListener(xacmlPanelActionAdapter);
        this.MergeNuSMVButton.setActionCommand("MergeXACML");

        this.CombinationNuSMVButton = new JButton("XACML for Combined Policies");
        this.CombinationNuSMVButton.setPreferredSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.CombinationNuSMVButton.setMaximumSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        this.CombinationNuSMVButton.addActionListener(xacmlPanelActionAdapter);
        this.CombinationNuSMVButton.setActionCommand("CombinationXACML");

        this.viewResultButton = new JButton("View XACML");
        this.viewResultButton.setPreferredSize(new Dimension(200, GraphicsUtil.FIELD_HEIGHT));
        this.viewResultButton.setMaximumSize(new Dimension(200, GraphicsUtil.FIELD_HEIGHT));
        this.viewResultButton.addActionListener(xacmlPanelActionAdapter);
        this.viewResultButton.setActionCommand("ViewResult");

        /********************
         * attribute properties panel
         *************************/

        JPanel Westpanel = new JPanel();
        Border border = BorderFactory.createTitledBorder("Commands");
        Westpanel.setBorder(border);
        ;
        Westpanel.setLayout(new BoxLayout(Westpanel, BoxLayout.Y_AXIS));

        // titledBorder =
        // BorderFactory.createTitledBorder(targetInfo.getValue() + "
        // commands");
        // this.setBorder(titledBorder);
        // this.setLayout(new BorderLayout());
        // this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // this.add(Box.createRigidArea(new
        // Dimension(50,GraphicsUtil.FIELD_HEIGHT)));
        // this.add(attributePanel);
        // this.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));

        // this.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
        // this.add(groupPanel);

        /*** addition info */
        /*
         * JPanel panel = new JPanel(new GridLayout(0, 1)); Border border =
         * BorderFactory.createTitledBorder(
         * "Combinations of Models (click combination tab to chagne)");
         * panel.setBorder(border);
         *
         * //JLabel infoLabel = new JLabel(
         * "Click combination tab to change the order of selected models or selections of given models"
         * ); //this.add(infoLabel); //panel.add(Box.createRigidArea(new
         * Dimension(150,GraphicsUtil.GAP_ZERO)));
         *
         *
         * grouplistmodel = new DefaultListModel(); grouplist = new
         * JList(grouplistmodel);
         *
         * if (combineTree != null){ String[] choices =
         * GenericTreeUtil.getTargetTreeChildrenValues2StrArr(
         * combinetreeRooNode, selectedCombineTreeType, 1); for (int i = 0; i <
         * choices.length; i++) { grouplistmodel.addElement(choices[i]); } }
         * grouplist.setEnabled(false); JScrollPane grouplistScroller = new
         * JScrollPane(grouplist); //
         * grouplistScroller.setAlignmentX(LEFT_ALIGNMENT);
         * grouplistScroller.setPreferredSize(new Dimension(150, 250));
         * grouplistScroller.setMaximumSize(new Dimension(150, 250));
         *
         * panel.add(grouplistScroller); this.add (panel);
         */

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
                    check = new JCheckBox(selectedType[i] + "#" + choices.get(j));
                    check.setActionCommand("Check:" + (checkount++));
                    check.addActionListener(xacmlPanelActionAdapter);
                    checkCompLst.addCheck(check, selectedType[i] + "#" + choices.get(j));
                    panelChkPols.add(check);
                }
            }
        }

        Westpanel.add(panelChkPols);
        /*
         * JPanel panel = new JPanel(); panel.setLayout(new BoxLayout(panel,
         * BoxLayout.X_AXIS)); panel.setAlignmentX(Component.RIGHT_ALIGNMENT);
         * //panel.setAlignmentX(Component.LEFT_ALIGNMENT);
         * panel.add(this.MergeNuSMVButton); Westpanel.add(panel);
         */
        JLabel addActionLabel = new JLabel("Policy generation: ", SwingConstants.CENTER);
        addActionLabel.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        addActionLabel.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        MergeactionComboBox = new JComboBox();
        MergeactionComboBox.addItem(GenericInfo.XACMLVersion2);
        MergeactionComboBox.addItem(GenericInfo.XACMLVersion3);

        MergeactionComboBox.setActionCommand("MergeComboBox");
        MergeactionComboBox.addActionListener(xacmlPanelActionAdapter);
        MergeactionComboBox.setPreferredSize(new Dimension(100, GraphicsUtil.FIELD_HEIGHT));
        MergeactionComboBox.setMaximumSize(new Dimension(100, GraphicsUtil.FIELD_HEIGHT));

        // JPanel subpanel_btn = new JPanel(new GridLayout(0, 2));
        JPanel subpanel_btn = new JPanel();
        subpanel_btn.setLayout(new BoxLayout(subpanel_btn, BoxLayout.X_AXIS));

        subpanel_btn.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        subpanel_btn.add(addActionLabel);
        subpanel_btn.add(this.MergeactionComboBox);
        subpanel_btn.add(this.MergeNuSMVButton);
        Westpanel.add(subpanel_btn);

        /*
         * JPanel subpanel_btn = new JPanel(new GridLayout(0, 2));
         * subpanel_btn.add(Box.createRigidArea(new
         * Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
         * subpanel_btn.add(this.MergeNuSMVButton);
         *
         * panel = new JPanel(); panel.setLayout(new BoxLayout(panel,
         * BoxLayout.X_AXIS)); panel.add(addActionLabel);
         * panel.add(MergeactionComboBox); panel.add(MergeNuSMVButton);
         */

        // panel.add(this.MergeNuSMVButton);

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
                check2.addActionListener(xacmlPanelActionAdapter);
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

        /*
         *
         * JTextArea textArea = new JTextArea(); JScrollPane scrollPane = new
         * JScrollPane(textArea);
         *
         * String info =
         * "Combined Policies (Precedence based on first-applicable combination)"
         * ; if (combinetreeRootNode != null){ List choices = (List)
         * GenericTreeUtil.getTargetTreeChildrenValues2List(combinetreeRootNode,
         * GenericInfo.MODELCOMBINE, 1); for (int j = 0; j < choices.size();
         * j++) { info += "\n"+(j+1)+". "+choices.get(j).toString(); check = new
         * JCheckBox("default deny"); } }
         *
         *
         * textArea.append(info); textArea.setLineWrap(true);
         * textArea.setWrapStyleWord(true); textArea.setEditable(false);
         * textArea.setBackground(null); // scrollPane.setPreferredSize(new
         * Dimension(450, 170)); // scrollPane.setMaximumSize(new Dimension(750,
         * 170));
         *
         * scrollPane.setPreferredSize(new Dimension(550, 100));
         * scrollPane.setMaximumSize(new Dimension(850, 100));
         * Westpanel.add(scrollPane);
         */
        // Westpanel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));

        // checkdefault = new JCheckBox("Default deny rule for each combined
        // policy");
        // checkdefault.setSelected(true);
        // Westpanel.add(checkdefault);

        addActionLabel = new JLabel("Policy generation: ", SwingConstants.CENTER);
        addActionLabel.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        addActionLabel.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        CombineactionComboBox = new JComboBox();
        CombineactionComboBox.addItem(GenericInfo.XACMLVersion2);
        CombineactionComboBox.addItem(GenericInfo.XACMLVersion3);

        CombineactionComboBox.setActionCommand("CombineComboBox");
        CombineactionComboBox.addActionListener(xacmlPanelActionAdapter);
        CombineactionComboBox.setPreferredSize(new Dimension(100, GraphicsUtil.FIELD_HEIGHT));
        CombineactionComboBox.setMaximumSize(new Dimension(100, GraphicsUtil.FIELD_HEIGHT));

        // JPanel subpanel_btn = new JPanel(new GridLayout(0, 2));
        JPanel subpanel = new JPanel();
        subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.X_AXIS));

        subpanel.add(Box.createRigidArea(new Dimension(25, GraphicsUtil.FIELD_HEIGHT)));

        subpanel.add(addActionLabel);
        subpanel.add(this.CombineactionComboBox);
        subpanel.add(this.CombinationNuSMVButton);

        Westpanel.add(subpanel);

        // EAST Panel

        JPanel Eastpanel = new JPanel();
        Border Eastborder = BorderFactory.createTitledBorder("Results");
        Eastpanel.setBorder(Eastborder);
        Eastpanel.setPreferredSize(new Dimension(150, 10));
        Eastpanel.setMaximumSize(new Dimension(150, 10));
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
        grouplistScroller.setPreferredSize(new Dimension(400, 250));
        grouplistScroller.setMaximumSize(new Dimension(400, 250));

        Eastpanel.add(grouplistScroller, BorderLayout.CENTER);

        JPanel Btnpanel = new JPanel(new GridLayout(0, 2));
        Btnpanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));
        Btnpanel.add(this.viewResultButton);
        Eastpanel.add(Btnpanel);

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
    class XACMLPanelActionAdapter implements ActionListener
    {

        XACMLPanel xacmlPanel = null;

        XACMLPanelActionAdapter(XACMLPanel subjectsPanel)
        {

            this.xacmlPanel = subjectsPanel;

        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getActionCommand().equals("MergeXACML"))
            {

                // File selected = TestPanelUtil.getDlgSelectedFileName("xml",
                // "XACML Files");
                // File selected = new File ("test.xml");
                int randomInt = (new Random()).nextInt();
                String source = "xacml-" + randomInt + ".xml";
                File selected = new File("results\\" + source);

                /*
                 * boolean defaultDenyEnabled = checkdefault.isSelected();
                 * XACMLPanelUtil.XACMLGeneration(frame, modelTree ,
                 * combineTree, XACML, selected, defaultDenyEnabled);
                 */
                String XACMLVersion = MergeactionComboBox.getSelectedItem().toString();

                List polSet = checkCompLst.getSelectedNodeLst();
                XACMLPanelUtil.XACMLGeneration("Merge", frame, modelTree, combineTree, polSet, XACMLVersion, selected,
                        null);

                String elm = "XACML: " + source;
                resultLst.add(elm);
                grouplistmodel.addElement(elm);
                grouplist.setSelectedIndex(grouplist.getLastVisibleIndex());

            }
            else if (e.getActionCommand().equals("CombinationXACML"))
            {

                // File selected = TestPanelUtil.getDlgSelectedFileName("xml",
                // "XACML Files");
                // File selected = new File ("test.xml");
                int randomInt = (new Random()).nextInt();
                String source = "xacml-" + randomInt + ".xml";
                File selected = new File("results\\" + source);

                // boolean defaultDenyEnabled = checkdefault.isSelected();

                DefaultMutableTreeNode combinetreeRooNode = GenericTreeUtil.getRootNode(combineTree);
                List polSet = GenericTreeUtil.getTargetTreeChildrenValues2List(combinetreeRooNode,
                        GenericInfo.MODELCOMBINE, 1);
                List polSet_checked = checkCompLst2.getSelectedNodeLst();

                String XACMLVersion = CombineactionComboBox.getSelectedItem().toString();

                // XACMLPanelUtil.XACMLGeneration("Combination",frame, modelTree
                // , polSet, XACML, selected, defaultDenyEnabled);
                XACMLPanelUtil.XACMLGeneration("Combination", frame, modelTree, combineTree, polSet, XACMLVersion,
                        selected, polSet_checked);

                String elm = "XACML: " + source;
                resultLst.add(elm);
                grouplistmodel.addElement(elm);
                grouplist.setSelectedIndex(grouplist.getLastVisibleIndex());

            }
            else if (e.getActionCommand().equals("ViewResult"))
            {

                if (grouplist.getSelectedIndex() == -1)
                {

                    JOptionPane.showMessageDialog(null, "No generation results are found", "Warning",
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

                    if (tk.endsWith(".xml"))
                    {
                        viewDialog viewDig = new viewDialog(frame, true, new File("results\\" + tk),
                                GenericInfo.XACMLFileType, false);
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

    }

}
