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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements the (XACML) target editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class ABACPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long     serialVersionUID       = 0;

    /***************************************************************************
     * Variables
     **************************************************************************/

    public JButton                updateValueButton      = null;
    // public JButton updateAlgButton = null;
    public JButton                updateAttributeButton  = null;

    public JButton                addAttributeButton     = null;
    public JTextField             valueField             = null;
    // public JComboBox subjectComboBox = null;
    // public JComboBox resourceComboBox = null;
    // public JComboBox actionComboBox = null;
    public JComboBox              ComboBox               = null;

    public JComboBox              decisionComboBox       = null;
    public JList                  list                   = null;
    public DefaultListModel       listmodel              = null;

    private ACPTFrame             frame                  = null;
    private JTree                 modelTree              = null;
    private JTree                 targetTree             = null;
    private ModelInfo             modelInfo              = null;
    private boolean               editable               = false;
    private TitledBorder          titledBorder           = null;
    /* Bruce Batson June 2013 Edit */
    private int                   ruleNum                = 1;
                                                         /* End of Edit */

    /* Adapters */
    public ABACPanelActionAdapter userPanelActionAdapter = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public ABACPanel(ACPTFrame frame, JTree modelTree, ModelInfo modelInfo, boolean editable)
    {

        this.frame = frame;
        this.modelTree = modelTree;
        this.targetTree = frame.targetPanel.getTargetTree();
        this.modelInfo = modelInfo;
        setAdapters();
        setPanels(modelInfo);
        setEditable(editable);
    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setAdapters()
    {

        this.userPanelActionAdapter = new ABACPanelActionAdapter(this);

    }

    private void setPanels(ModelInfo modelInfo)
    {

        /******************** Name panel *************************/

        JLabel valueLabel = new JLabel("Name: ", SwingConstants.LEFT);
        valueLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        valueLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        this.valueField = new JTextField(modelInfo.getValue());
        this.valueField.setPreferredSize(new Dimension(250, GraphicsUtil.FIELD_HEIGHT));
        this.valueField.setMaximumSize(new Dimension(250, GraphicsUtil.FIELD_HEIGHT));

        this.updateValueButton = new JButton("Change Name");
        this.updateValueButton.setPreferredSize(new Dimension(120, GraphicsUtil.FIELD_HEIGHT));
        this.updateValueButton.setMaximumSize(new Dimension(120, GraphicsUtil.FIELD_HEIGHT));

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
        namePanel.add(Box.createRigidArea(new Dimension(80 + 15, GraphicsUtil.FIELD_HEIGHT)));

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

        // this.updateAlgButton = new JButton("Update");
        // this.updateAlgButton.setPreferredSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // this.updateAlgButton.setMaximumSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        //
        // this.updateAlgButton.addActionListener(userPanelActionAdapter);
        // this.updateAlgButton.setActionCommand("Update Combination");

        JPanel PolComPanel = new JPanel();
        titledBorder = BorderFactory.createTitledBorder("Rule Combination Algorithm");
        PolComPanel.setBorder(titledBorder);

        PolComPanel.setLayout(new BoxLayout(PolComPanel, BoxLayout.X_AXIS));

        PolComPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        PolComPanel.add(algLabel);
        PolComPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        PolComPanel.add(ComboBox);
        PolComPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        // PolComPanel.add(updateAlgButton);
        PolComPanel.add(Box.createRigidArea(new Dimension(80 + 15, GraphicsUtil.FIELD_HEIGHT)));

        // find rule combination
        if (modelTree != null)
        {
            List<DefaultMutableTreeNode> choices = ModelTreeUtil.getTargetTreeChildrenNode2List(
                    ModelTreeUtil.getCurrentNode(modelTree), ModelInfo.ABACRule, ModelInfo.NodeLevelofruleroot);
            DefaultMutableTreeNode RuleCombinationNode = choices.get(0);
            String CombinationType = ((ModelInfo) RuleCombinationNode.getUserObject()).getRuleCombiningAlgorithm();
            ComboBox.setSelectedItem(CombinationType);
        }

        /******************** Add attributes panel *************************/

        // JLabel addAttributeLabel = new JLabel("Attributes: ", JLabel.LEFT);
        // addAttributeLabel.setPreferredSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        // addAttributeLabel.setMaximumSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        /******************** List attributes panel *************************/

        JLabel listAttributesLabel = new JLabel("Rules: ", SwingConstants.LEFT);
        listAttributesLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        listAttributesLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        // String[] choices = { "A", "long", "array", "of", "strings" };

        listmodel = new DefaultListModel();
        list = new JList(listmodel);
        /*
         * Bruce Batson Edit July 2013 This makes it so that if a rule was added
         * by Inheritance then it will not show up in the panel.
         */
        String hold_num = "";
        if (modelTree != null)
        {
            List choices = ModelTreeUtil.getTargetTreeChildrenNode2List(ModelTreeUtil.getCurrentNode(modelTree),
                    ModelInfo.ABACRule, ModelInfo.NodeLevelofrule);
            /*
             * This if check to see if the file was created using a version
             * before Inheritance was added. If it was, it adds a rule number to
             * each rule.
             */
            if (choices.size() > 0)
            {
                if (((ModelInfo) ((DefaultMutableTreeNode) choices.get(0)).getUserObject()).getValue()
                        .startsWith("Rule"))
                {
                    for (int i = 0; i < choices.size(); i++)
                    {
                        if (i == 0)
                        {
                            listmodel.addElement(choices.get(i));
                            // Fix for when only 1 rule is available, previously crashes
                            hold_num = "Rule 1";
                        }
                        else
                        {
                            String rule_num = ((ModelInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject())
                                    .getRuleNum();
                            String pre_rule_num =
                                    ((ModelInfo) ((DefaultMutableTreeNode) choices.get(i - 1)).getUserObject())
                                            .getRuleNum();
                            if (!rule_num.equals(pre_rule_num))
                            {
                                listmodel.addElement(choices.get(i));
                            }
                            hold_num = rule_num;
                        }
                    }
                    this.ruleNum = Integer.parseInt(hold_num.substring(5)) + 1;
                }
                else
                {
                    for (int i = 0; i < choices.size(); i++)
                    {
                        String hold_val =
                                ((ModelInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getValue();
                        hold_val = "Rule " + this.ruleNum + "#" + hold_val;
                        ((ModelInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).setValue(hold_val);
                        listmodel.addElement(choices.get(i));
                        this.ruleNum++;
                    }
                }
            }
        }
        // End of Edit

        if (listmodel.capacity() > 0)
        {
            list.setSelectedIndex(0);
        }

        /******************** Buttons *************************/

        addAttributeButton = new JButton("Add");
        addAttributeButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        addAttributeButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        addAttributeButton.addActionListener(userPanelActionAdapter);
        addAttributeButton.setActionCommand("Add");

        updateAttributeButton = new JButton("Update");
        updateAttributeButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        updateAttributeButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        updateAttributeButton.addActionListener(userPanelActionAdapter);
        updateAttributeButton.setActionCommand("Update");
        // updateAttributeButton.setEnabled(false);

        JButton removeAttributeButton = new JButton("Remove");
        removeAttributeButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        removeAttributeButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        removeAttributeButton.addActionListener(userPanelActionAdapter);
        removeAttributeButton.setActionCommand("Remove");

        /******************** List of Rules *************************/

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setAlignmentX(CENTER_ALIGNMENT);

        listScroller.setPreferredSize(new Dimension(300, 200));
        listScroller.setMaximumSize(new Dimension(300, 200));

        /********************
         * Panel of Rule List & Edition
         *************************/

        JPanel listOfRulesPanel = new JPanel();

        listOfRulesPanel.setLayout(new BoxLayout(listOfRulesPanel, BoxLayout.Y_AXIS));
        listOfRulesPanel.add(listScroller);
        listOfRulesPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(addAttributeButton);
        panel.add(updateAttributeButton);
        panel.add(removeAttributeButton);

        listOfRulesPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));
        listOfRulesPanel.add(panel);

        JPanel attributePanel = new JPanel();
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder("Rules");
        attributePanel.setBorder(titledBorder2);
        attributePanel.setLayout(new BorderLayout());
        attributePanel.add(listOfRulesPanel, BorderLayout.CENTER);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // this.setLayout(new BorderLayout(0,1));
        this.add(namePanel);
        this.add(PolComPanel);
        this.add(attributePanel);

        // this.add(namePanel, BorderLayout.NORTH);
        // this.add(addRulePanel, BorderLayout.CENTER);
        // this.add(attributePanel, BorderLayout.SOUTH);

        /* Titled border for this panel. */
        titledBorder = BorderFactory.createTitledBorder(modelInfo.getValue() + " ABAC Policy");
        // BorderFactory.createTitledBorder(modelInfo.getValue() + "ABAC
        // Policy");
        this.setBorder(titledBorder);

    }

    /***************************************************************************
     * Public methods
     **************************************************************************/

    public void setEditable(boolean editable)
    {

        this.editable = editable;
        this.valueField.setEnabled(editable);
        // this.updateValueButton.setEnabled(editable);
        // this.updateAlgButton.setEnabled(editable);

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
            /* Bruce Batson June 2013 Edit */
            // Get the rule number for updated info
            String hold = ((ModelInfo) currentNode.getUserObject()).getRuleNum();
            hold = hold.substring(5);
            int holdNum = Integer.parseInt(hold);
            /* End of Edit */

            // MLSRuleUpdateDialog myDialog = new MLSRuleUpdateDialog(frame,
            // true, type, currentNode);

            RuleDialog myDialog =
                    new RuleDialog(frame, true, ModelInfo.ABACRule, targetTree, "", "Update", currentNode, holdNum);

            if (myDialog.getAnswer())
            {

                // //String TargetAttrType =
                // ((ModelInfo)currentNode.getUserObject()).getvalueofvalue();
                String TargetAttrValue = myDialog.getcheckCompLst().toString();

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
                /*
                 * Before Edit // update tree currentNode.setUserObject(new
                 * ModelInfo(type, TargetAttrValue)); DefaultMutableTreeNode
                 * parentNode =
                 * (DefaultMutableTreeNode)(currentNode.getParent());
                 * ((DefaultTreeModel)modelTree.getModel()).reload(parentNode);
                 *
                 * // add or update condition node if (currentNode != null){ if
                 * (myDialog.getCondition().trim().isEmpty()){
                 * currentNode.removeAllChildren(); }else if
                 * (currentNode.getChildCount() > 0){ // if condition node exits
                 * DefaultMutableTreeNode ConditionNode =
                 * (DefaultMutableTreeNode) currentNode.getFirstChild(); if
                 * (ConditionNode != null){ ConditionNode.setUserObject(new
                 * ModelInfo(ModelInfo.XACMLRuleCondition,
                 * myDialog.getCondition())); } }else{// if no condition node
                 * exits DefaultMutableTreeNode newCondition = new
                 * DefaultMutableTreeNode( new
                 * ModelInfo(ModelInfo.XACMLRuleCondition,
                 * myDialog.getCondition())); currentNode.add(newCondition); }
                 * ((DefaultTreeModel)modelTree.getModel()).reload(currentNode);
                 * }
                 *
                 *
                 * // update list int selectedpos =
                 * selectedlist.getSelectedIndex();
                 * selectedlistmodel.remove(selectedpos);
                 * selectedlistmodel.add(selectedpos, currentNode);
                 * selectedlist.setSelectedIndex(selectedpos);
                 */
                /* Bruce Batson June 2013 Edit */
                // Remove the old rule as well as all the inheritance rules.
                // listmodel.removeElement(currentNode);
                int selectedpos = selectedlist.getSelectedIndex();
                selectedlistmodel.remove(selectedpos);
                if (listmodel.capacity() > 0)
                {
                    list.setSelectedIndex(0);
                }
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
                currentNode.removeAllChildren();
                parentNode.remove(currentNode);
                List nodes = ModelTreeUtil.getTargetTreeChildrenNode2List(parentNode, ModelInfo.ABACRule, 4);
                String current_rul_num = ((ModelInfo) currentNode.getUserObject()).getRuleNum();
                for (int i = 0; i < nodes.size(); i++)
                {
                    String nodes_rul_num =
                            ((ModelInfo) ((DefaultMutableTreeNode) nodes.get(i)).getUserObject()).getRuleNum();
                    if (nodes_rul_num.equals(current_rul_num))
                    {
                        ((DefaultMutableTreeNode) nodes.get(i)).removeAllChildren();
                        parentNode.remove(((DefaultMutableTreeNode) nodes.get(i)));
                    }
                }

                currentNode = ModelTreeUtil.addGrandChildNode(ModelInfo.ABACRule, TargetAttrValue, modelTree, false);
                // Add in updated rule and updated inheritance rules.
                if ((currentNode != null) && !myDialog.getCondition().trim().isEmpty())
                {
                    DefaultMutableTreeNode newCondition = new DefaultMutableTreeNode(
                            new ModelInfo(ModelInfo.XACMLRuleCondition, myDialog.getCondition()));
                    currentNode.add(newCondition);
                    ((DefaultTreeModel) modelTree.getModel()).reload(currentNode);
                }

                // listmodel.addElement(currentNode);
                selectedlistmodel.add(selectedpos, currentNode);
                selectedlist.setSelectedIndex(selectedpos);

                DefaultMutableTreeNode rootNode =
                        TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.INHERITANCE, 1);
                List inheritance =
                        TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, TargetInfo.INHERITANCE_ATTR_VALUSES, 3);
                boolean newrule = false;
                // Goes through all the subject checked in the rule that was
                // created.
                for (int i = 0; i < myDialog.getcountSubject(); i++)
                {
                    // Goes through all the Inheritance attribute values and
                    // compares them
                    // to the subjects in the rule for match.
                    for (int j = 0; j < inheritance.size(); j++)
                    {
                        // If there is a match make a new rule using the
                        // beneficiary in replace of
                        // the inherited value.
                        if (myDialog.getcheckCompLst().getSubjectAttValue(i)
                                .equals(((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(j)).getUserObject())
                                        .getvalueOfvalue()))
                        {
                            String replace =
                                    ((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(j)).getUserObject())
                                            .getvalueOfvalue();
                            String replace2 =
                                    ((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(j)).getUserObject())
                                            .gettypeOfvalue();
                            rootNode =
                                    (DefaultMutableTreeNode) ((DefaultMutableTreeNode) inheritance.get(j)).getParent();
                            String TargetValue = ((TargetInfo) rootNode.getUserObject()).getvalueOfvalue();
                            String holdToV = ((TargetInfo) rootNode.getUserObject()).gettypeOfvalue();
                            DefaultMutableTreeNode subjectNode = TargetTreeUtil.getFirstMatchingNode(targetTree,
                                    holdToV, TargetInfo.SUBJECT_ATTRIBUTES, 2);
                            String TargetType = ((TargetInfo) subjectNode.getUserObject()).getTargetType();
                            String valueOfvalue = ((TargetInfo) subjectNode.getUserObject()).getvalueOfvalue();
                            String typeOfvalue = ((TargetInfo) subjectNode.getUserObject()).gettypeOfvalue();
                            String newRuleValue = myDialog.getcheckCompLst().InheritanceRule(TargetType, valueOfvalue,
                                    typeOfvalue, TargetValue, replace, replace2);
                            if (newRuleValue != "")
                            {
                                currentNode = ModelTreeUtil.addGrandChildNode(ModelInfo.ABACRule, newRuleValue,
                                        modelTree, true);

                                // add condition node
                                if ((currentNode != null) && !myDialog.getCondition().trim().isEmpty())
                                {
                                    DefaultMutableTreeNode newCondition = new DefaultMutableTreeNode(
                                            new ModelInfo(ModelInfo.XACMLRuleCondition, myDialog.getCondition()));
                                    currentNode.add(newCondition);
                                    ((DefaultTreeModel) modelTree.getModel()).reload(currentNode);
                                }

                                // listmodel.addElement(currentNode);
                            }
                            newrule = true;
                            // This checks if there is some inheritance in the
                            // new rule.
                            while (newrule)
                            {
                                newrule = false;
                                for (int x = 0; x < inheritance.size(); x++)
                                {
                                    if (TargetValue.equals(
                                            ((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(x)).getUserObject())
                                                    .getvalueOfvalue()))
                                    {
                                        rootNode =
                                                (DefaultMutableTreeNode) ((DefaultMutableTreeNode) inheritance.get(x))
                                                        .getParent();
                                        TargetValue = ((TargetInfo) rootNode.getUserObject()).getvalueOfvalue();
                                        holdToV = ((TargetInfo) rootNode.getUserObject()).gettypeOfvalue();
                                        subjectNode = TargetTreeUtil.getFirstMatchingNode(targetTree, holdToV,
                                                TargetInfo.SUBJECT_ATTRIBUTES, 2);
                                        TargetType = ((TargetInfo) subjectNode.getUserObject()).getTargetType();
                                        valueOfvalue = ((TargetInfo) subjectNode.getUserObject()).getvalueOfvalue();
                                        typeOfvalue = ((TargetInfo) subjectNode.getUserObject()).gettypeOfvalue();
                                        newRuleValue = myDialog.getcheckCompLst().InheritanceRule(TargetType,
                                                valueOfvalue, typeOfvalue, TargetValue, replace, replace2);
                                        if (newRuleValue != "")
                                        {
                                            currentNode = ModelTreeUtil.addGrandChildNode(ModelInfo.ABACRule,
                                                    newRuleValue, modelTree, true);

                                            // add condition node
                                            if ((currentNode != null) && !myDialog.getCondition().trim().isEmpty())
                                            {
                                                DefaultMutableTreeNode newCondition =
                                                        new DefaultMutableTreeNode(new ModelInfo(
                                                                ModelInfo.XACMLRuleCondition, myDialog.getCondition()));
                                                currentNode.add(newCondition);
                                                ((DefaultTreeModel) modelTree.getModel()).reload(currentNode);
                                            }

                                            // listmodel.addElement(currentNode);
                                        }
                                        newrule = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            myDialog.dispose();

            // String newTargetName = GraphicsUtil.showTextFieldInputDialog(
            // " name:\n", "New ",
            // "Name cannot be empty string", "Name error",
            // currentNode.toString(), frame);
            // if (newTargetName == null || newTargetName.equals("")) {return;};

        }
    }

    /**
     * This inner class implements the target panel mouse adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class ABACPanelActionAdapter implements ActionListener
    {

        ABACPanel userPanel = null;

        ABACPanelActionAdapter(ABACPanel userPanel)
        {

            this.userPanel = userPanel;

        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getActionCommand().equals("CombinationComboBox"))
            {

                // find rule combination
                if (modelTree != null)
                {
                    List<DefaultMutableTreeNode> choices = ModelTreeUtil.getTargetTreeChildrenNode2List(
                            ModelTreeUtil.getCurrentNode(modelTree), ModelInfo.ABACRule, ModelInfo.NodeLevelofruleroot);
                    DefaultMutableTreeNode RuleCombinationNode = choices.get(0);

                    RuleCombinationNode.setUserObject(
                            new ModelInfo(ModelInfo.ABACRule, "Rules: " + ComboBox.getSelectedItem().toString()));
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (RuleCombinationNode.getParent());
                    ((DefaultTreeModel) modelTree.getModel()).reload(parentNode);

                }

            }
            else if (e.getActionCommand().equals("Update"))
            {

                updateRuleElmChildNode(ModelInfo.ABACRule, list, listmodel);

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

            else if (e.getActionCommand().equals("Add"))
            {

                RuleDialog myDialog =
                        new RuleDialog(frame, true, ModelInfo.ABACRule, targetTree, "", "Add", null, ruleNum);
                String ruleValue = myDialog.getcheckCompLst().toString();
                // System.err.println(ruleValue);

                if (myDialog.getAnswer())
                {

                    DefaultMutableTreeNode newNode =
                            ModelTreeUtil.addGrandChildNode(ModelInfo.ABACRule, ruleValue, modelTree, false);

                    // add condition node
                    if ((newNode != null) && !myDialog.getCondition().trim().isEmpty())
                    {
                        DefaultMutableTreeNode newCondition = new DefaultMutableTreeNode(
                                new ModelInfo(ModelInfo.XACMLRuleCondition, myDialog.getCondition()));
                        newNode.add(newCondition);
                        ((DefaultTreeModel) modelTree.getModel()).reload(newNode);
                    }

                    listmodel.addElement(newNode);
                    /* Bruce Batson June 2013 Edit */

                    DefaultMutableTreeNode rootNode =
                            TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.INHERITANCE, 1);
                    List inheritance = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode,
                            TargetInfo.INHERITANCE_ATTR_VALUSES, 3);
                    boolean newrule = false;
                    // Goes through all the subject checked in the rule that was
                    // created.
                    for (int i = 0; i < myDialog.getcountSubject(); i++)
                    {
                        // Goes through all the Inheritance attribute values and
                        // compares them
                        // to the subjects in the rule for match.
                        for (int j = 0; j < inheritance.size(); j++)
                        {
                            // If there is a match make a new rule using the
                            // beneficiary in replace of
                            // the inherited value.
                            if (myDialog.getcheckCompLst().getSubjectAttValue(i)
                                    .equals(((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(j)).getUserObject())
                                            .getvalueOfvalue()))
                            {
                                String replace =
                                        ((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(j)).getUserObject())
                                                .getvalueOfvalue();
                                String replace2 =
                                        ((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(j)).getUserObject())
                                                .gettypeOfvalue();
                                rootNode = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) inheritance.get(j))
                                        .getParent();
                                String TargetValue = ((TargetInfo) rootNode.getUserObject()).getvalueOfvalue();
                                String holdToV = ((TargetInfo) rootNode.getUserObject()).gettypeOfvalue();
                                DefaultMutableTreeNode subjectNode = TargetTreeUtil.getFirstMatchingNode(targetTree,
                                        holdToV, TargetInfo.SUBJECT_ATTRIBUTES, 2);
                                String TargetType = ((TargetInfo) subjectNode.getUserObject()).getTargetType();
                                String valueOfvalue = ((TargetInfo) subjectNode.getUserObject()).getvalueOfvalue();
                                String typeOfvalue = ((TargetInfo) subjectNode.getUserObject()).gettypeOfvalue();
                                String newRuleValue = myDialog.getcheckCompLst().InheritanceRule(TargetType,
                                        valueOfvalue, typeOfvalue, TargetValue, replace, replace2);
                                if (newRuleValue != "")
                                {
                                    newNode = ModelTreeUtil.addGrandChildNode(ModelInfo.ABACRule, newRuleValue,
                                            modelTree, true);

                                    // add condition node
                                    if ((newNode != null) && !myDialog.getCondition().trim().isEmpty())
                                    {
                                        DefaultMutableTreeNode newCondition = new DefaultMutableTreeNode(
                                                new ModelInfo(ModelInfo.XACMLRuleCondition, myDialog.getCondition()));
                                        newNode.add(newCondition);
                                        ((DefaultTreeModel) modelTree.getModel()).reload(newNode);
                                    }

                                    // listmodel.addElement(newNode);
                                }
                                newrule = true;
                                // This checks if there is some inheritance in
                                // the new rule.
                                while (newrule)
                                {
                                    newrule = false;
                                    for (int x = 0; x < inheritance.size(); x++)
                                    {
                                        if (TargetValue
                                                .equals(((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(x))
                                                        .getUserObject()).getvalueOfvalue()))
                                        {
                                            rootNode = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) inheritance
                                                    .get(x)).getParent();
                                            TargetValue = ((TargetInfo) rootNode.getUserObject()).getvalueOfvalue();
                                            holdToV = ((TargetInfo) rootNode.getUserObject()).gettypeOfvalue();
                                            subjectNode = TargetTreeUtil.getFirstMatchingNode(targetTree, holdToV,
                                                    TargetInfo.SUBJECT_ATTRIBUTES, 2);
                                            TargetType = ((TargetInfo) subjectNode.getUserObject()).getTargetType();
                                            valueOfvalue = ((TargetInfo) subjectNode.getUserObject()).getvalueOfvalue();
                                            typeOfvalue = ((TargetInfo) subjectNode.getUserObject()).gettypeOfvalue();
                                            newRuleValue = myDialog.getcheckCompLst().InheritanceRule(TargetType,
                                                    valueOfvalue, typeOfvalue, TargetValue, replace, replace2);
                                            if (newRuleValue != "")
                                            {
                                                newNode = ModelTreeUtil.addGrandChildNode(ModelInfo.ABACRule,
                                                        newRuleValue, modelTree, true);

                                                // add condition node
                                                if ((newNode != null) && !myDialog.getCondition().trim().isEmpty())
                                                {
                                                    DefaultMutableTreeNode newCondition = new DefaultMutableTreeNode(
                                                            new ModelInfo(ModelInfo.XACMLRuleCondition,
                                                                    myDialog.getCondition()));
                                                    newNode.add(newCondition);
                                                    ((DefaultTreeModel) modelTree.getModel()).reload(newNode);
                                                }

                                                // listmodel.addElement(newNode);
                                            }
                                            newrule = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ruleNum++;
                    /* End of Edit */
                }
                myDialog.dispose();

            }
            else if (e.getActionCommand().equals("Remove"))
            {

                System.out.println("Got Remove Button");
                if (list.getSelectedIndex() == -1)
                {
                    // Warning
                    GraphicsUtil.showWarningDialog("No rule is selected.", "Deletion Warning", null);
                    return;
                }

                if (list.getSelectedIndex() >= 0)
                {

                    // remove from the list
                    DefaultMutableTreeNode currentNode =
                            (DefaultMutableTreeNode) listmodel.get(list.getSelectedIndex());
                    listmodel.removeElement(currentNode);
                    if (listmodel.capacity() > 0)
                    {
                        list.setSelectedIndex(0);
                    }
                    // remove from the tree
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
                    currentNode.removeAllChildren();
                    parentNode.remove(currentNode);
                    /* Bruce Batson June 2013 Edit */
                    /*
                     * This remove all the rule that where add because of
                     * Inheritance.
                     */
                    List nodes = ModelTreeUtil.getTargetTreeChildrenNode2List(parentNode, ModelInfo.ABACRule, 4);
                    String current_rul_num = ((ModelInfo) currentNode.getUserObject()).getRuleNum();
                    for (int i = 0; i < nodes.size(); i++)
                    {
                        String nodes_rul_num =
                                ((ModelInfo) ((DefaultMutableTreeNode) nodes.get(i)).getUserObject()).getRuleNum();
                        if (nodes_rul_num.equals(current_rul_num))
                        {
                            ((DefaultMutableTreeNode) nodes.get(i)).removeAllChildren();
                            parentNode.remove(((DefaultMutableTreeNode) nodes.get(i)));
                        }
                    }
                    /* End of Edit */
                    ((DefaultTreeModel) modelTree.getModel()).reload(parentNode);
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
