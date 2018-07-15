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
package gov.nist.csd.acpt.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nist.csd.acpt.ACPTFrame;
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

public class MULTILEVELPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long           serialVersionUID          = 0;

    /***************************************************************************
     * Variables
     **************************************************************************/

    public JButton                      updateValueButton         = null;
    public JButton                      updateAlgButton           = null;
    public JButton                      addAttributeButton        = null;
    public JButton                      addSubjectLevelButton     = null;
    public JButton                      addResourceLevelButton    = null;
    public JButton                      updateSubjectLevelButton  = null;
    public JButton                      updateResourceLevelButton = null;

    private JList                       subjectlevellist          = null;
    private DefaultListModel            subjectlevellistmodel     = null;
    private JList                       resourcelevellist         = null;
    private DefaultListModel            resourcelevellistmodel    = null;
    private JList                       rulelist                  = null;
    private DefaultListModel            rulelistmodel             = null;

    public JButton                      addRuleButton             = null;
    public JButton                      updateRuleButton          = null;
    public JButton                      removeRuleButton          = null;

    public JButton                      selectSubjectLevelButton  = null;
    public JButton                      selectResourceLevelButton = null;

    public JButton                      removeSubjectLevelButton  = null;
    public JButton                      removeResourceLevelButton = null;

    public JTextField                   valueField                = null;
    public JComboBox                    ComboBox                  = null;
    public JComboBox                    subjectComboBox           = null;
    public JComboBox                    resourceComboBox          = null;
    // public JComboBox actionComboBox = null;
    // public JComboBox levelComboBox1 = null;
    // public JComboBox levelComboBox2 = null;
    public JComboBox                    propertyComboBox          = null;

    public JComboBox                    subjectLevelComboBox      = null;
    public JComboBox                    resourceLevelComboBox     = null;
    public JComboBox                    actionPropertyComboBox    = null;

    // public JList list = null;
    // public DefaultListModel listmodel = null;
    DefaultMutableTreeNode              MLSModelRootnode          = null;
    DefaultMutableTreeNode              MLSModelSubLevelRootnode  = null;
    DefaultMutableTreeNode              MLSModelResLevelRootnode  = null;

    private ACPTFrame                   frame                     = null;
    private JTree                       modelTree                 = null;
    private JTree                       targetTree                = null;
    private ModelInfo                   modelInfo                 = null;
    private boolean                     editable                  = false;
    private TitledBorder                titledBorder              = null;

    /* Adapters */
    public MultiLevelPanelActionAdapter userPanelActionAdapter    = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public MULTILEVELPanel(ACPTFrame frame, JTree modelTree, ModelInfo modelInfo, boolean editable)
    {

        this.frame = frame;
        this.modelTree = modelTree;
        this.targetTree = frame.targetPanel.getTargetTree();
        this.modelInfo = modelInfo;
        setNodes();
        setAdapters();
        setPanels(modelInfo);
        setEditable(editable);
    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/
    private void setNodes()
    {

        MLSModelRootnode = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();
        // MLSModelSubLevelRootnode = ((List)
        // ModelTreeUtil.getTargetTreeChildrenNode2List(MLSModelRootnode,
        // ModelInfo.MULTILEVELSUBJECTLEVELS, 3)

        List choices =
                ModelTreeUtil.getTargetTreeChildrenNode2List(MLSModelRootnode, ModelInfo.MULTILEVELSUBJECTLEVELS, 3);
        if (choices.size() > 0)
        {
            MLSModelSubLevelRootnode = (DefaultMutableTreeNode) choices.get(0);
        }

        choices = ModelTreeUtil.getTargetTreeChildrenNode2List(MLSModelRootnode, ModelInfo.MULTILEVELRESOURCELEVELS, 3);
        if (choices.size() > 0)
        {
            MLSModelResLevelRootnode = (DefaultMutableTreeNode) choices.get(0);
        }

        // for (int i = 0; i < choices.size(); i++) {
        // subjectlevellistmodel.addElement((DefaultMutableTreeNode)
        // choices.get(i));
        // }
        //
        //
        // MLSModelResLevelRootnode =
        // ModelTreeUtil.getFirstMatchingNode(modelTree,
        // ModelInfo.MULTILEVELRESOURCELEVELS, 3);

        // System.err.println("&&"+MLSModelSubLevelRootnode.toString());
        // System.err.println(MLSModelResLevelRootnode.toString());

    }

    private void setAdapters()
    {

        this.userPanelActionAdapter = new MultiLevelPanelActionAdapter(this);

    }

    private void setPanels(ModelInfo modelInfo)
    {

        /******************** Name panel *************************/

        JLabel valueLabel = new JLabel("Name: ", SwingConstants.LEFT);
        valueLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        valueLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        this.valueField = new JTextField(modelInfo.getValue());
        this.valueField.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        this.valueField.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        this.updateValueButton = new JButton("Change Name");
        this.updateValueButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.updateValueButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        this.updateValueButton.addActionListener(userPanelActionAdapter);
        this.updateValueButton.setActionCommand("Update Title");

        JPanel namePanel = new JPanel();
        titledBorder = BorderFactory.createTitledBorder("Name");
        namePanel.setBorder(titledBorder);

        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        namePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        namePanel.add(valueLabel);
        namePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        namePanel.add(valueField);
        namePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        namePanel.add(updateValueButton);
        namePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        this.add(namePanel);

        /******************** Rule Combination Panel *************************/

        JLabel algLabel = new JLabel("Algorithm: ", SwingConstants.LEFT);
        algLabel.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        algLabel.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        this.ComboBox = new JComboBox(ModelInfo.RuleCombinationAlgs);
        this.ComboBox.setActionCommand("CombinationComboBox");
        this.ComboBox.addActionListener(userPanelActionAdapter);
        this.ComboBox.setMinimumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        this.ComboBox.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        this.ComboBox.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        this.updateAlgButton = new JButton("Update");
        this.updateAlgButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.updateAlgButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        this.updateAlgButton.addActionListener(userPanelActionAdapter);
        this.updateAlgButton.setActionCommand("Update Combination");

        JPanel PolComPanel = new JPanel();
        titledBorder = BorderFactory.createTitledBorder("Rule Combination Algorithm");
        PolComPanel.setBorder(titledBorder);

        PolComPanel.setLayout(new BoxLayout(PolComPanel, BoxLayout.X_AXIS));

        PolComPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        PolComPanel.add(algLabel);
        PolComPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        PolComPanel.add(ComboBox);
        PolComPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        PolComPanel.add(updateAlgButton);
        PolComPanel.add(Box.createRigidArea(new Dimension(80 + 15, GraphicsUtil.FIELD_HEIGHT)));

        // find rule combination
        if (modelTree != null)
        {
            List<DefaultMutableTreeNode> choices = ModelTreeUtil.getTargetTreeChildrenNode2List(
                    ModelTreeUtil.getCurrentNode(modelTree), ModelInfo.MULTILEVELRule, ModelInfo.NodeLevelofruleroot);
            DefaultMutableTreeNode RuleCombinationNode = choices.get(0);
            String CombinationType = ((ModelInfo) RuleCombinationNode.getUserObject()).getRuleCombiningAlgorithm();
            ComboBox.setSelectedItem(CombinationType);
        }

        // this.add(PolComPanel);

        /********************
         * Select MLS attribute Panel
         *************************/
        /*
         * subjectComboBox = new JComboBox();
         * subjectComboBox.setActionCommand("SubjectComboBox");
         * subjectComboBox.addActionListener(userPanelActionAdapter);
         * subjectComboBox.setPreferredSize(new Dimension(150,
         * GraphicsUtil.FIELD_HEIGHT)); subjectComboBox.setMaximumSize(new
         * Dimension(150, GraphicsUtil.FIELD_HEIGHT));
         *
         * DefaultMutableTreeNode attributeNode =
         * TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.SUBJECTS,
         * TargetInfo.Level);
         *
         * String existingSubSelection = null; if (MLSModelSubLevelRootnode !=
         * null){ existingSubSelection =
         * ((ModelInfo)MLSModelSubLevelRootnode.getUserObject()).getValue(); }
         *
         * if (targetTree != null){ List choices = (List)
         * TargetTreeUtil.getTargetTreeChildrenNode2List(attributeNode,
         * TargetInfo.SUBJECT_ATTRIBUTES , TargetInfo.attrLevel);
         *
         * for (int i = 0; i < choices.size(); i++) {
         *
         * //1. integer //2. only one resource //3. it should be within one
         * level.
         *
         * DefaultMutableTreeNode node = (DefaultMutableTreeNode)
         * choices.get(i); TargetInfo target = (TargetInfo)
         * node.getUserObject(); if (target.gettypeOfvalue().equals("Integer")
         * && node.getChildCount() == 1){
         * subjectComboBox.addItem((DefaultMutableTreeNode) choices.get(i));
         *
         * // select existing Selection if (existingSubSelection != null &&
         * existingSubSelection.endsWith(choices.get(i).toString())){
         * subjectComboBox.setSelectedItem(choices.get(i)); } } } }
         *
         * if
         * (subjectComboBox.getItemCount()>0){subjectComboBox.setSelectedIndex(0
         * );}
         *
         *
         *
         * resourceComboBox = new JComboBox(); //
         * resourceComboBox.setSelectedIndex(0);
         * resourceComboBox.setActionCommand("ResourceComboBox");
         * resourceComboBox.addActionListener(userPanelActionAdapter);
         * resourceComboBox.setPreferredSize(new Dimension(150,
         * GraphicsUtil.FIELD_HEIGHT)); resourceComboBox.setMaximumSize(new
         * Dimension(150, GraphicsUtil.FIELD_HEIGHT));
         *
         * DefaultMutableTreeNode attributeNode2 =
         * TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.RESOURCES,
         * TargetInfo.Level);
         *
         * String existingResSelection = null; if (MLSModelResLevelRootnode !=
         * null){ existingResSelection =
         * ((ModelInfo)MLSModelResLevelRootnode.getUserObject()).getValue(); }
         *
         *
         * if (targetTree != null){ List choices = (List)
         * TargetTreeUtil.getTargetTreeChildrenNode2List(attributeNode2,
         * TargetInfo.RESOURCE_ATTRIBUTES , TargetInfo.attrLevel);
         *
         * for (int i = 0; i < choices.size(); i++) {
         * resourceComboBox.addItem((DefaultMutableTreeNode) choices.get(i));
         *
         * // select existing Selection if (existingResSelection != null &&
         * existingResSelection.endsWith(choices.get(i).toString())){
         * resourceComboBox.setSelectedItem(choices.get(i)); } } }
         *
         * if
         * (resourceComboBox.getItemCount()>0){resourceComboBox.setSelectedIndex
         * (0);}
         */

        propertyComboBox = new JComboBox(new String[]
        {
                "up", "down"
        });
        // propertyComboBox.setSelectedIndex(0);
        propertyComboBox.setActionCommand("PropertyComboBox");
        propertyComboBox.addActionListener(userPanelActionAdapter);
        propertyComboBox.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        propertyComboBox.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        addSubjectLevelButton = new JButton("Add");
        addSubjectLevelButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        addSubjectLevelButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        addSubjectLevelButton.addActionListener(userPanelActionAdapter);
        addSubjectLevelButton.setActionCommand("Add Subject Level");

        updateSubjectLevelButton = new JButton("Update");
        updateSubjectLevelButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        updateSubjectLevelButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        updateSubjectLevelButton.addActionListener(userPanelActionAdapter);
        updateSubjectLevelButton.setActionCommand("Update Subject Level");

        // selectSubjectLevelButton = new JButton("Add");
        // selectSubjectLevelButton.setPreferredSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // selectSubjectLevelButton.setMaximumSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        //
        // selectSubjectLevelButton.addActionListener(userPanelActionAdapter);
        // selectSubjectLevelButton.setActionCommand("Add Subject Level");

        removeSubjectLevelButton = new JButton("Remove");
        removeSubjectLevelButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        removeSubjectLevelButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        removeSubjectLevelButton.addActionListener(userPanelActionAdapter);
        removeSubjectLevelButton.setActionCommand("Remove Subject Level");

        addResourceLevelButton = new JButton("Add");
        addResourceLevelButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        addResourceLevelButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        addResourceLevelButton.addActionListener(userPanelActionAdapter);
        addResourceLevelButton.setActionCommand("Add Resource Level");

        updateResourceLevelButton = new JButton("Update");
        updateResourceLevelButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        updateResourceLevelButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        updateResourceLevelButton.addActionListener(userPanelActionAdapter);
        updateResourceLevelButton.setActionCommand("Update Resource Level");

        // selectResourceLevelButton = new JButton("Add");
        // selectResourceLevelButton.setPreferredSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // selectResourceLevelButton.setMaximumSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        //
        // selectResourceLevelButton.addActionListener(userPanelActionAdapter);
        // selectResourceLevelButton.setActionCommand("Add Resource Level");

        removeResourceLevelButton = new JButton("Remove");
        removeResourceLevelButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        removeResourceLevelButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        removeResourceLevelButton.addActionListener(userPanelActionAdapter);
        removeResourceLevelButton.setActionCommand("Remove Resource Level");

        addRuleButton = new JButton("Add");
        addRuleButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        addRuleButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        addRuleButton.addActionListener(userPanelActionAdapter);
        addRuleButton.setActionCommand("Add Rule");

        updateRuleButton = new JButton("Update");
        updateRuleButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        updateRuleButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        updateRuleButton.addActionListener(userPanelActionAdapter);
        updateRuleButton.setActionCommand("Update Rule");

        removeRuleButton = new JButton("Remove");
        removeRuleButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        removeRuleButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        removeRuleButton.addActionListener(userPanelActionAdapter);
        removeRuleButton.setActionCommand("Remove Rule");

        /******************** Warning *************************/

        String myMessage =
                "* Note that multilevel attribute should include only one legitimate integer value, which should be smaller than the number of multilevel states specified in properties.";
        JTextArea propertytextArea = new JTextArea();
        // Font font = new Font("Serif", Font.ITALIC, 20);
        propertytextArea.setBackground(null);
        // propertytextArea.set
        // propertytextArea.setFont(new Font("Times Roman", Font.PLAIN, 12));
        propertytextArea.setFont(null);
        JScrollPane scrollPane = new JScrollPane(propertytextArea);
        propertytextArea.append(myMessage);
        propertytextArea.setLineWrap(true);
        propertytextArea.setWrapStyleWord(true);
        propertytextArea.setEditable(false);
        scrollPane.setPreferredSize(new Dimension(550, 150));
        scrollPane.setMaximumSize(new Dimension(550, 150));
        scrollPane.setBorder(null);
        this.add(scrollPane);
        this.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));

        /********************
         * (new) Selection Attribute addition panel
         *************************/

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Border border = BorderFactory.createTitledBorder("Select MLS Subject/Resource Attributes");
        panel.setBorder(border);

        // JLabel removeSubjectLevelLabel = new JLabel("Subject-Level: ",
        // JLabel.LEFT);
        JPanel subpanel = new JPanel();
        subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.X_AXIS));

        JLabel tempLabel = new JLabel("Subject Attribute: ", SwingConstants.LEFT);
        tempLabel.setPreferredSize(new Dimension(115, GraphicsUtil.FIELD_HEIGHT));
        tempLabel.setMaximumSize(new Dimension(115, GraphicsUtil.FIELD_HEIGHT));
        subpanel.add(tempLabel);
        // subpanel.add(subjectComboBox);
        subpanel.add(addSubjectLevelButton);
        // panel.add(subpanel);

        subpanel = new JPanel();
        subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.X_AXIS));
        tempLabel = new JLabel("Resource Attribute: ", SwingConstants.LEFT);
        tempLabel.setPreferredSize(new Dimension(115, GraphicsUtil.FIELD_HEIGHT));
        tempLabel.setMaximumSize(new Dimension(115, GraphicsUtil.FIELD_HEIGHT));
        subpanel.add(tempLabel);
        // subpanel.add(resourceComboBox);
        subpanel.add(addResourceLevelButton);
        // panel.add(subpanel);

        // this.add(panel);

        /********************
         * (new) Selection Attribute/ Levels addition panel
         *************************/

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        border = BorderFactory.createTitledBorder("MLS Subject/Resource Attribute value-level");
        panel.setBorder(border);

        subpanel = new JPanel(new GridLayout(0, 2));

        subjectlevellistmodel = new DefaultListModel();
        subjectlevellist = new JList(subjectlevellistmodel);

        if (modelTree != null)
        {

            List choices = ModelTreeUtil.getTargetTreeChildrenNode2List(MLSModelRootnode,
                    ModelInfo.MULTILEVELSUBJECTLEVELS, 4);

            for (int i = 0; i < choices.size(); i++)
            {
                subjectlevellistmodel.addElement(choices.get(i));
            }

        }
        // subjectlevellist.setEnabled(false);
        JScrollPane subScroller = new JScrollPane(subjectlevellist);
        subScroller.setPreferredSize(new Dimension(150, 250));
        subScroller.setMaximumSize(new Dimension(150, 250));

        resourcelevellistmodel = new DefaultListModel();
        resourcelevellist = new JList(resourcelevellistmodel);

        // DefaultMutableTreeNode node = (DefaultMutableTreeNode) modelTree
        // .getLastSelectedPathComponent();

        if (modelTree != null)
        {
            List choices = ModelTreeUtil.getTargetTreeChildrenNode2List(MLSModelRootnode,
                    ModelInfo.MULTILEVELRESOURCELEVELS, 4);

            for (int i = 0; i < choices.size(); i++)
            {
                resourcelevellistmodel.addElement(choices.get(i));
            }

        }
        // resourcelevellist.setEnabled(false);
        JScrollPane resScroller = new JScrollPane(resourcelevellist);
        resScroller.setPreferredSize(new Dimension(150, 250));
        resScroller.setMaximumSize(new Dimension(150, 250));

        subpanel.add(subScroller);
        subpanel.add(resScroller);
        panel.add(subpanel);
        // panel.add(addSubjectLevelButton);
        subpanel = new JPanel(new GridLayout(0, 2));

        JPanel btnpanel = new JPanel(new GridLayout(0, 2));
        // btnpanel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
        btnpanel.add(addSubjectLevelButton);
        btnpanel.add(removeSubjectLevelButton);
        // btnpanel.add(updateSubjectLevelButton);
        subpanel.add(btnpanel);

        btnpanel = new JPanel(new GridLayout(0, 2));
        // btnpanel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
        btnpanel.add(addResourceLevelButton);
        btnpanel.add(removeResourceLevelButton);
        // btnpanel.add(updateResourceLevelButton);
        subpanel.add(btnpanel);

        panel.add(subpanel);
        this.add(panel);

        /********************
         * (new) Add rule addition panel
         *************************/

        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        titledBorder = BorderFactory.createTitledBorder("Rule Properties");
        panel.setBorder(titledBorder);

        rulelistmodel = new DefaultListModel();
        rulelist = new JList(rulelistmodel);

        // DefaultMutableTreeNode node = (DefaultMutableTreeNode) modelTree
        // .getLastSelectedPathComponent();

        if (modelTree != null)
        {
            // String[] choices =
            // ModelTreeUtil.getTargetTreeChildrenValues2StrArr(MLSModelRootnode,
            // ModelInfo.MULTILEVELRule, 4);
            // for (int i = 0; i < choices.length; i++) {
            // rulelistmodel.addElement(choices[i]);
            // }

            List choices = ModelTreeUtil.getTargetTreeChildrenNode2List(MLSModelRootnode, ModelInfo.MULTILEVELRule, 4);

            for (int i = 0; i < choices.size(); i++)
            {
                rulelistmodel.addElement(choices.get(i));
            }
        }
        // rulelist.setEnabled(false);
        JScrollPane ruleScroller = new JScrollPane(rulelist);
        ruleScroller.setPreferredSize(new Dimension(350, 250));
        ruleScroller.setMaximumSize(new Dimension(350, 250));

        panel.add(ruleScroller);
        subpanel = new JPanel();
        subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.X_AXIS));
        subpanel.add(addRuleButton);
        subpanel.add(updateRuleButton);
        subpanel.add(removeRuleButton);
        panel.add(subpanel);
        this.add(panel);

        this.setLayout(new BorderLayout());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        /* Titled border for this panel. */
        titledBorder = BorderFactory.createTitledBorder("MLS Policy");
        this.setBorder(titledBorder);

        // this.add(namePanel);

    }

    /***************************************************************************
     * Public methods
     **************************************************************************/

    public void setEditable(boolean editable)
    {

        this.editable = editable;
        this.valueField.setEnabled(editable);
        // this.updateValueButton.setEnabled(editable);

    }

    public void updateTargetValue(String value)
    {
        /*
         * this.targetInfo.setValue(value); if
         * (targetInfo.getTargetType().equals(TargetInfo.ROOT)) {
         * this.frame.setProjectName(value); } titledBorder.setTitle(value +
         * " properties"); this.repaint(); this.targetTree.repaint();
         * System.out.println("Property button size: " +
         * updateValueButton.getSize().toString());
         */
        return;
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
    class MultiLevelPanelActionAdapter implements ActionListener
    {

        MULTILEVELPanel userPanel = null;

        MultiLevelPanelActionAdapter(MULTILEVELPanel userPanel)
        {

            this.userPanel = userPanel;

        }

        public void updateRuleElmChildNode(String type, JList selectedlist, DefaultListModel selectedlistmodel)
        {

            if (selectedlist.getSelectedIndex() < 0)
            {
                JOptionPane.showMessageDialog(null, "cannot update: select attribute value-level first", "Warning",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (selectedlist.getSelectedIndex() >= 0)
            {
                DefaultMutableTreeNode currentNode =
                        (DefaultMutableTreeNode) selectedlistmodel.get(selectedlist.getSelectedIndex());

                MLSRuleUpdateDialog myDialog = new MLSRuleUpdateDialog(frame, true, type, currentNode);

                if (myDialog.getAnswer())
                {

                    // String TargetAttrType =
                    // ((ModelInfo)currentNode.getUserObject()).getvalueofvalue();
                    String TargetAttrValue = myDialog.getMyProperty();

                    DefaultMutableTreeNode tempNode = new DefaultMutableTreeNode(new ModelInfo(type, TargetAttrValue));

                    // duplicate check
                    for (int i = 0; i < selectedlistmodel.size(); i++)
                    {
                        String iterString = ((DefaultMutableTreeNode) selectedlistmodel.get(i)).toString();
                        // check duplicate when it is not itself.
                        if (selectedlist.getSelectedIndex() != i)
                        {
                            if (iterString.equals(tempNode.toString()))
                            {
                                JOptionPane.showMessageDialog(null, "cannot update: duplicate name", "Warning",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        }
                    }

                    // String newTargetName = myDialog.getSelectedString();

                    // update tree
                    currentNode.setUserObject(new ModelInfo(type, TargetAttrValue));
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
                    ((DefaultTreeModel) modelTree.getModel()).reload(parentNode);

                    // update list
                    int selectedpos = selectedlist.getSelectedIndex();
                    selectedlistmodel.remove(selectedpos);
                    selectedlistmodel.add(selectedpos, currentNode);
                    selectedlist.setSelectedIndex(selectedpos);

                }

                myDialog.dispose();

                // String newTargetName = GraphicsUtil.showTextFieldInputDialog(
                // " name:\n", "New ",
                // "Name cannot be empty string", "Name error",
                // currentNode.toString(), frame);
                // if (newTargetName == null || newTargetName.equals(""))
                // {return;};

            }
        }

        public void updateChildNode(String type, JList selectedlist, DefaultListModel selectedlistmodel)
        {

            if (selectedlist.getSelectedIndex() < 0)
            {
                JOptionPane.showMessageDialog(null, "cannot update: select attribute value-level first", "Warning",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (selectedlist.getSelectedIndex() >= 0)
            {
                DefaultMutableTreeNode currentNode =
                        (DefaultMutableTreeNode) selectedlistmodel.get(selectedlist.getSelectedIndex());

                MLSupdateDialog myDialog = new MLSupdateDialog(frame, true, type, currentNode);

                if (myDialog.getAnswer())
                {

                    String TargetAttrType = ((ModelInfo) currentNode.getUserObject()).getvalueofvalue();
                    int TargetAttrValue = myDialog.getMyLevel();

                    DefaultMutableTreeNode tempNode =
                            new DefaultMutableTreeNode(new ModelInfo(type, TargetAttrType + "#" + TargetAttrValue));

                    // System.out
                    // .println("MultiLevelPanelActionAdapter.updateChildNode()"+type+TargetAttrType+currentNode);

                    // duplicate check
                    for (int i = 0; i < selectedlistmodel.size(); i++)
                    {
                        String iterString = ((DefaultMutableTreeNode) selectedlistmodel.get(i)).toString();
                        // check duplicate when it is not itself.
                        if (selectedlist.getSelectedIndex() != i)
                        {
                            if (iterString.equals(tempNode.toString()))
                            {
                                JOptionPane.showMessageDialog(null, "cannot update: duplicate name", "Warning",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        }
                    }

                    // String newTargetName = myDialog.getSelectedString();

                    // update tree
                    currentNode.setUserObject(new ModelInfo(type, TargetAttrType + "#" + TargetAttrValue));
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
                    ((DefaultTreeModel) modelTree.getModel()).reload(parentNode);

                    // update list
                    int selectedpos = selectedlist.getSelectedIndex();
                    selectedlistmodel.remove(selectedpos);
                    selectedlistmodel.add(selectedpos, currentNode);
                    selectedlist.setSelectedIndex(selectedpos);

                }

                myDialog.dispose();

            }
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getActionCommand().equals("Update"))
            {

                String value = valueField.getText();
                updateTargetValue(value);

            }
            else if (e.getActionCommand().equals("Update Title"))
            {

                NameChangeDialog myDialog = null;
                DefaultMutableTreeNode curNode = ModelTreeUtil.getCurrentNode(modelTree);
                myDialog = new NameChangeDialog(frame, true, modelTree, curNode);

                if (myDialog.getAnswer())
                {

                    valueField.setText(myDialog.getNewValue());
                    ((DefaultTreeModel) modelTree.getModel()).reload(curNode.getParent());
                    // sync this panel
                    // setAdapters();
                    // setPanels(modelInfo);
                    // setEditable(editable);

                    // refresh all subpanels
                    frame.refreshSubPanels();

                }

            }
            else if (e.getActionCommand().equals("Update Combination"))
            {

                // find rule combination
                if (modelTree != null)
                {
                    List<DefaultMutableTreeNode> choices =
                            ModelTreeUtil.getTargetTreeChildrenNode2List(ModelTreeUtil.getCurrentNode(modelTree),
                                    ModelInfo.MULTILEVELRule, ModelInfo.NodeLevelofruleroot);
                    DefaultMutableTreeNode RuleCombinationNode = choices.get(0);

                    RuleCombinationNode.setUserObject(
                            new ModelInfo(ModelInfo.MULTILEVELRule, "Rules: " + ComboBox.getSelectedItem().toString()));
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (RuleCombinationNode.getParent());
                    ((DefaultTreeModel) modelTree.getModel()).reload(parentNode);

                }
            }
            else if (e.getActionCommand().equals("Update Subject Level"))
            {

                DefaultMutableTreeNode targetAttrNode = null;
                updateChildNode(ModelInfo.MULTILEVELSUBJECTLEVELS, subjectlevellist, subjectlevellistmodel);

            }
            else if (e.getActionCommand().equals("Update Resource Level"))
            {

                DefaultMutableTreeNode targetAttrNode = null;
                updateChildNode(ModelInfo.MULTILEVELRESOURCELEVELS, resourcelevellist, resourcelevellistmodel);
            }
            else if (e.getActionCommand().equals("Update Rule Level"))
            {

                DefaultMutableTreeNode targetAttrNode = null;
                updateChildNode(ModelInfo.MULTILEVELRule, rulelist, rulelistmodel);
            }
            else if (e.getActionCommand().equals("Add Subject Level")
                    || e.getActionCommand().equals("Add Resource Level"))
            {

                // System.out.println("Got combo box");
                // DefaultMutableTreeNode targetAttrNode = null;
                String targetAttrType = null;
                String modelAttrType = null;
                String AttrIndex = null;
                DefaultListModel selectedListModel = null;
                DefaultMutableTreeNode modelAttrLevelNode = null;
                String targetType = null;
                String attributeType = null;

                if (e.getActionCommand().equals("Add Subject Level"))
                {
                    // targetAttrNode = (DefaultMutableTreeNode)
                    // subjectComboBox.getSelectedItem();
                    targetAttrType = TargetInfo.SUBJECT_ATTRIBUTES;
                    modelAttrType = ModelInfo.MULTILEVELSUBJECTLEVELS;
                    selectedListModel = subjectlevellistmodel;
                    modelAttrLevelNode = MLSModelSubLevelRootnode;
                    targetType = TargetInfo.SUBJECT_ATTR_VALUSES;
                    AttrIndex = "Subject Levels";
                    attributeType = TargetInfo.SUBJECTS;

                }
                else if (e.getActionCommand().equals("Add Resource Level"))
                {
                    // targetAttrNode = (DefaultMutableTreeNode)
                    // resourceComboBox.getSelectedItem();
                    targetAttrType = TargetInfo.RESOURCE_ATTRIBUTES;
                    modelAttrType = ModelInfo.MULTILEVELRESOURCELEVELS;
                    selectedListModel = resourcelevellistmodel;
                    modelAttrLevelNode = MLSModelResLevelRootnode;
                    targetType = TargetInfo.RESOURCE_ATTR_VALUSES;
                    AttrIndex = "Resource Levels";
                    attributeType = TargetInfo.RESOURCES;
                }

                DefaultMutableTreeNode attributeNode =
                        TargetTreeUtil.getFirstMatchingNode(targetTree, attributeType, TargetInfo.Level);

                // Select only attributes, which include one integer value
                // within its general property level values (0 <= value <
                // property level value)

                List Selectedchoices = new ArrayList();

                if (targetTree != null)
                {
                    List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(attributeNode, targetAttrType,
                            TargetInfo.attrLevel);

                    for (int i = 0; i < choices.size(); i++)
                    {

                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) choices.get(i);
                        TargetInfo target = (TargetInfo) node.getUserObject();

                        List childchoices = TargetTreeUtil.getTargetTreeChildrenNode2List(node, targetType,
                                TargetInfo.attrLevel + 1);

                        if (target.gettypeOfvalue().equals("Integer") && (node.getChildCount() == 1))
                        {

                            DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getFirstChild();
                            TargetInfo Childtarget = (TargetInfo) childNode.getUserObject();

                            int TargetChildInteger = Integer.parseInt(Childtarget.getvalueOfvalue());
                            if ((TargetChildInteger >= 0)
                                    && (TargetChildInteger <= Generalproperties.MultiLevelStateNumber))
                            {
                                Selectedchoices.add(node);
                            }
                        }
                    }
                }

                // for (int i = 0; i < Selectedchoices.size(); i++) {
                // System.err.println(Selectedchoices.get(i));
                // }

                if (Selectedchoices.size() == 0)
                {
                    JOptionPane.showMessageDialog(null,
                            "There are no attibues including only one legitimate integer value!", "Warning",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                MLSselectionDialog myDialog =
                        new MLSselectionDialog(frame, true, modelAttrType, Selectedchoices, targetTree);

                if (myDialog.getAnswer())
                {

                    // duplicate check

                    String targetInfo = targetAttrType + ";" + myDialog.getMyNode().toString();
                    // System.out
                    // .println("MULTILEVELPanel.MultiLevelPanelActionAdapter.actionPerformed():"+targetInfo);
                    //

                    // duplicate check
                    for (int i = 0; i < selectedListModel.size(); i++)
                    {
                        DefaultMutableTreeNode node = ((DefaultMutableTreeNode) selectedListModel.get(i));

                        // String iterString = ((DefaultMutableTreeNode)
                        // selectedListModel.get(i)).toString();
                        // check duplicate when it is not itself.

                        String TargetAttrType = ((ModelInfo) node.getUserObject()).getvalueofvalue();

                        if (TargetAttrType.equals(targetInfo))
                        {
                            JOptionPane.showMessageDialog(null, "cannot update: duplicate name", "Warning",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }

                    // if (modelAttrLevelNode != null &&
                    // !modelAttrLevelNode.isLeaf()){
                    // int result = JOptionPane.showConfirmDialog(null, "Confirm
                    // that your action deletes existing attribute level
                    // information and add new attribute level information.",
                    // "Deletion Information", JOptionPane.INFORMATION_MESSAGE);
                    //
                    // if (result != 0){
                    // return;
                    // }
                    // //System.err.println(result);
                    // }

                    // if middle node is missing

                    if (modelAttrLevelNode == null)
                    {

                        modelAttrLevelNode = new DefaultMutableTreeNode(new ModelInfo(modelAttrType, AttrIndex));
                        MLSModelRootnode.add(modelAttrLevelNode);
                        ((DefaultTreeModel) modelTree.getModel()).reload(MLSModelRootnode);
                    }
                    else
                    {
                        // modelAttrLevelNode.removeAllChildren();
                        // modelAttrLevelNode.setUserObject(new
                        // ModelInfo(modelAttrType, AttrIndex));
                    }

                    //
                    // selectedListModel.clear();

                    int myLevel = myDialog.getMyLevel();
                    DefaultMutableTreeNode newNode = ModelTreeUtil.addGrandChildNode(modelAttrType,
                            targetInfo + "#" + myLevel, modelTree, false);
                    selectedListModel.addElement(newNode);

                    ((DefaultTreeModel) modelTree.getModel()).reload(modelAttrLevelNode);

                }
                myDialog.dispose();

            }
            else if (e.getActionCommand().equals("Add Rule"))
            {

                // DefaultMutableTreeNode targetAttrNode = null;
                // updateChildNode(ModelInfo.MULTILEVELRule, rulelist,
                // rulelistmodel);
                //
                // DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)
                // selectedlistmodel.get(selectedlist.getSelectedIndex());

                MLSRuleDialog myDialog = new MLSRuleDialog(frame, true, ModelInfo.MULTILEVELRule, null);

                // RuleDialog MLSRuleDialog = new MLSRuleDialog(frame, true,
                // modelInfo.ABACRule , targetTree , "");
                String ruleValue = myDialog.getMyProperty();

                // System.err.println(ruleValue);

                if (myDialog.getAnswer())
                {

                    DefaultMutableTreeNode newNode =
                            ModelTreeUtil.addGrandChildNode(ModelInfo.MULTILEVELRule, ruleValue, modelTree, false);
                    rulelistmodel.addElement(newNode);
                }
                myDialog.dispose();

                // private JList rulelist = null;
                // private DefaultListModel rulelistmodel = null;

            }
            else if (e.getActionCommand().equals("Update Rule"))
            {

                DefaultMutableTreeNode targetAttrNode = null;
                updateRuleElmChildNode(ModelInfo.MULTILEVELRule, rulelist, rulelistmodel);
            }
            else if (e.getActionCommand().equals("Remove Rule"))
            {

                // DefaultMutableTreeNode targetAttrNode = null;
                // updateChildNode(ModelInfo.MULTILEVELRule, rulelist,
                // rulelistmodel);

                System.out.println("Got Remove Button");
                if (rulelist.getSelectedIndex() == -1)
                {
                    // Warning
                    GraphicsUtil.showWarningDialog("No rule is selected.", "Deletion Warning", null);
                    return;
                }

                if (rulelist.getSelectedIndex() >= 0)
                {

                    // remove from the list
                    DefaultMutableTreeNode currentNode =
                            (DefaultMutableTreeNode) rulelistmodel.get(rulelist.getSelectedIndex());
                    rulelistmodel.removeElement(currentNode);
                    if (rulelistmodel.capacity() > 0)
                    {
                        rulelist.setSelectedIndex(0);
                    }

                    // remove from the tree
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
                    currentNode.removeAllChildren();
                    parentNode.remove(currentNode);
                    ((DefaultTreeModel) modelTree.getModel()).reload(parentNode);
                }
            }
            else if (e.getActionCommand().equals("Remove Subject Level")
                    || e.getActionCommand().equals("Remove Resource Level"))
            {

                JList selectedList = null;
                DefaultListModel selectedListModel = null;
                if (e.getActionCommand().equals("Remove Subject Level"))
                {
                    selectedList = subjectlevellist;
                    selectedListModel = subjectlevellistmodel;

                }
                else if (e.getActionCommand().equals("Remove Resource Level"))
                {
                    selectedList = resourcelevellist;
                    selectedListModel = resourcelevellistmodel;
                }

                System.out.println("Got Subject or Resource Level Remove Button");
                if (selectedList.getSelectedIndex() == -1)
                {
                    // Warning
                    GraphicsUtil.showWarningDialog("No attribute is selected.", "Deletion Warning", null);
                    return;
                }

                if (selectedList.getSelectedIndex() >= 0)
                {

                    // remove from the list
                    DefaultMutableTreeNode currentNode =
                            (DefaultMutableTreeNode) selectedListModel.get(selectedList.getSelectedIndex());
                    selectedListModel.removeElement(currentNode);
                    if (selectedListModel.capacity() > 0)
                    {
                        selectedList.setSelectedIndex(0);
                    }

                    // remove from the tree
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
                    currentNode.removeAllChildren();
                    parentNode.remove(currentNode);
                    ((DefaultTreeModel) modelTree.getModel()).reload(parentNode);
                }

            }
            else if (e.getActionCommand().equals("Remove"))
            {

                System.out.println("Got Remove Button");
                if (actionPropertyComboBox.getSelectedIndex() == -1)
                {
                    // Warning
                    GraphicsUtil.showWarningDialog("Select rule to be removed first.", "Deletion Warning", null);
                    return;
                }
                String value = (String) actionPropertyComboBox.getSelectedItem();
                ModelTreeUtil.removeGrandChildNode(ModelInfo.MULTILEVELRule, value, modelTree);
                // reset combobox
                actionPropertyComboBox.removeAllItems();
                String choices[] = ModelTreeUtil.getTargetTreeChildrenValues2StrArr(
                        ModelTreeUtil.getCurrentNode(modelTree), ModelInfo.MULTILEVELRule, ModelInfo.NodeLevelofrule);
                for (int i = 0; i < choices.length; i++)
                {
                    actionPropertyComboBox.addItem(choices[i]);
                }
                actionPropertyComboBox.repaint();
            }

            else
            {

                // System.err.println("GroupPanel warning: command not
                // implemented: " +
                // e.getActionCommand());
            }

        }

        private void RefreshRemoveCombos()
        {
            // reset combobox
            subjectLevelComboBox.removeAllItems();
            String choices[] = ModelTreeUtil.getTargetTreeChildrenValues2StrArr(ModelTreeUtil.getCurrentNode(modelTree),
                    ModelInfo.MULTILEVELSUBJECTLEVELS, ModelInfo.NodeLevelofrule);
            for (int i = 0; i < choices.length; i++)
            {
                subjectLevelComboBox.addItem(choices[i]);
            }
            subjectLevelComboBox.repaint();

            // reset combobox
            resourceLevelComboBox.removeAllItems();
            choices = ModelTreeUtil.getTargetTreeChildrenValues2StrArr(ModelTreeUtil.getCurrentNode(modelTree),
                    ModelInfo.MULTILEVELRESOURCELEVELS, ModelInfo.NodeLevelofrule);
            for (int i = 0; i < choices.length; i++)
            {
                resourceLevelComboBox.addItem(choices[i]);
            }
            resourceLevelComboBox.repaint();

            // reset combobox
            actionPropertyComboBox.removeAllItems();
            choices = ModelTreeUtil.getTargetTreeChildrenValues2StrArr(ModelTreeUtil.getCurrentNode(modelTree),
                    ModelInfo.MULTILEVELRule, ModelInfo.NodeLevelofrule);
            for (int i = 0; i < choices.length; i++)
            {
                actionPropertyComboBox.addItem(choices[i]);
            }
            actionPropertyComboBox.repaint();

        }

    }

}
