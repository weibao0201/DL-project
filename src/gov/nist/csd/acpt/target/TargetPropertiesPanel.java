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
package gov.nist.csd.acpt.target;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.generic.GenericTreeUtil;
import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.model.ModelTreeUtil;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements the (XACML) target editor panel.
 *
 * @author JeeHyun Hwang
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class TargetPropertiesPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long           serialVersionUID                   = 0;

    /***************************************************************************
     * Variables
     **************************************************************************/

    public String                       AttributeValueType                 = TargetInfo.SUBJECT_ATTR_VALUSES;
    public String                       AttributeType                      = TargetInfo.SUBJECT_ATTRIBUTES;
    public DefaultMutableTreeNode       attributeNode                      = null;
    public DefaultMutableTreeNode       groupNode                          = null;
    public DefaultMutableTreeNode       targetParentNode                   = null;

    // public int Level = 1;
    // public int attrLevel = 2;
    // public int nodeLevel = 3;

    public JButton                      updateValueButton                  = null;

    public JButton                      addAttributeButton                 = null;
    public JButton                      updateAttributeButton              = null;
    public JButton                      deleteAttributeButton              = null;
    public JButton                      addGroupButton                     = null;
    public JButton                      updateGroupButton                  = null;
    public JButton                      deleteGroupButton                  = null;

    public JTextField                   valueField                         = null;
    private ACPTFrame                   frame                              = null;
    public JTree                        targetTree                         = null;
    // private TargetInfo targetInfo = null;
    private String                      targetType                         = null;
    private boolean                     editable                           = false;
    private TitledBorder                titledBorder                       = null;

    public JList                        list                               = null;
    public DefaultListModel             listmodel                          = null;

    public JList                        grouplist                          = null;
    public DefaultListModel             grouplistmodel                     = null;

    /* Adapters */
    public SubjectsListSelectionAdapter targetListSelectionAdapter         = null;
    public SubjectsPanelActionAdapter   targetAttributesPanelActionAdapter = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public TargetPropertiesPanel(ACPTFrame frame, JTree targetTree, String targetType, boolean editable)
    {

        this.frame = frame;
        this.targetTree = targetTree;
        this.targetType = targetType;
        // this.targetInfo = targetInfo;

        setTargetAttrRootNode();
        setAdapters();
        setPanels();
        setEditable(editable);

    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setTargetAttrRootNode()
    {

        attributeNode = TargetTreeUtil.getFirstMatchingNode(targetTree, this.targetType, TargetInfo.Level);

        if (this.targetType.equals(TargetInfo.SUBJECTS))
        {
            this.AttributeType = TargetInfo.SUBJECT_ATTRIBUTES;
            this.AttributeValueType = TargetInfo.SUBJECT_ATTR_VALUSES;
        }
        else if (this.targetType.equals(TargetInfo.RESOURCES))
        {
            this.AttributeType = TargetInfo.RESOURCE_ATTRIBUTES;
            this.AttributeValueType = TargetInfo.RESOURCE_ATTR_VALUSES;
        }
        else if (this.targetType.equals(TargetInfo.ACTIONS))
        {
            this.AttributeType = TargetInfo.ACTION_ATTRIBUTES;
            this.AttributeValueType = TargetInfo.ACTION_ATTR_VALUSES;
        }
        else if (this.targetType.equals(TargetInfo.ENVIRONMENTS))
        {
            this.AttributeType = TargetInfo.ENVIRONMENT_ATTRIBUTES;
            this.AttributeValueType = TargetInfo.ENVIRONMENT_ATTR_VALUSES;
        }
        /* Bruce Batson June 2013 Edit */
        else if (this.targetType.equals(TargetInfo.INHERITANCE))
        {
            this.AttributeType = TargetInfo.INHERITANCE_ATTRIBUTES;
            this.AttributeValueType = TargetInfo.INHERITANCE_ATTR_VALUSES;
        }
        /* End of edit */

        // System.out.println("TargetPropertiesPanel.setTargetAttrRootNode()" +
        // this.AttributeType + this.AttributeValueType);

        // groupNode = TargetTreeUtil.getFirstMatchingNode(targetTree,
        // TargetInfo.SUBJECT_ATTRIBUTES, attrLevel);

    }

    private void setAdapters()
    {

        this.targetAttributesPanelActionAdapter = new SubjectsPanelActionAdapter(this);
        this.targetListSelectionAdapter = new SubjectsListSelectionAdapter(this);
    }

    private void setPanels()
    {

        listmodel = new TargetDefaultListModel();
        list = new JList(listmodel);

        // DefaultMutableTreeNode[]

        if (targetTree != null)
        {

            List choices =
                    TargetTreeUtil.getTargetTreeChildrenNode2List(attributeNode, AttributeType, TargetInfo.attrLevel);

            for (int i = 0; i < choices.size(); i++)
            {
                listmodel.addElement(choices.get(i));
            }

            /*
             * for (int i = 0; i < choices.getItemCount(); i++) {
             * listmodel.addElement(choices.getItem(i)); }
             */

            // for (DefaultMutableTreeNode s : choices.getItemCount()) {
            // System.out.println("Next item: " + s);
            // }
            //
            //
            // Enumeration e = choices.el
            // Collections.enumeration(choices);
            /*
             * # //enumerate through the ArrayList elements #
             * System.out.println("Enumerating through Java ArrayList"); #
             * while(e.hasMoreElements()) # System.out.println(e.nextElement());
             * # }
             *
             * choices.
             */
            /*
             * for (int i = 0; i < choices.get; i++) {
             * listmodel.addElement(choices[i]); }
             */

            if (listmodel.capacity() > 0)
            {
                list.setSelectedIndex(0);
            }
        }
        // this.list.addActionListener(targetAttributesPanelActionAdapter);
        this.list.addListSelectionListener(targetListSelectionAdapter);

        JScrollPane listScroller = new JScrollPane(list);
        // listScroller.setAlignmentX(LEFT_ALIGNMENT);
        listScroller.setPreferredSize(new Dimension(250, 300));
        listScroller.setMaximumSize(new Dimension(250, 300));

        grouplistmodel = new DefaultListModel();
        grouplist = new JList(grouplistmodel);

        if ((targetTree != null) && (this.list.getSelectedIndex() >= 0))
        {
            /*
             * String[] choices =
             * TargetTreeUtil.getTargetTreeChildrenValues2StrArr(groupNode,
             * groupType, nodeLevel); for (int i = 0; i < choices.length; i++) {
             * grouplistmodel.addElement(choices[i]); }
             */

            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(
                    (DefaultMutableTreeNode) listmodel.firstElement(), AttributeValueType, TargetInfo.nodeLevel);

            for (int i = 0; i < choices.size(); i++)
            {
                grouplistmodel.addElement(choices.get(i));
            }
            if (grouplistmodel.capacity() > 0)
            {
                grouplist.setSelectedIndex(0);
            }
        }

        this.grouplist.addListSelectionListener(targetListSelectionAdapter);
        JScrollPane grouplistScroller = new JScrollPane(grouplist);
        // grouplistScroller.setAlignmentX(LEFT_ALIGNMENT);
        grouplistScroller.setPreferredSize(new Dimension(250, 300));
        grouplistScroller.setMaximumSize(new Dimension(250, 300));

        this.addAttributeButton = new JButton("Add");
        this.addAttributeButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.addAttributeButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.addAttributeButton.addActionListener(targetAttributesPanelActionAdapter);
        this.addAttributeButton.setActionCommand("Add Attribute Value");

        this.updateAttributeButton = new JButton("Update");
        this.updateAttributeButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.updateAttributeButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.updateAttributeButton.addActionListener(targetAttributesPanelActionAdapter);
        this.updateAttributeButton.setActionCommand("Update Attribute Value");

        this.deleteAttributeButton = new JButton("Delete");
        this.deleteAttributeButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.deleteAttributeButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.deleteAttributeButton.addActionListener(targetAttributesPanelActionAdapter);
        this.deleteAttributeButton.setActionCommand("Delete Attribute Value");

        JButton inheritanceButton = new JButton("Inheritance");
        inheritanceButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        inheritanceButton.setMaximumSize(new Dimension(100, GraphicsUtil.FIELD_HEIGHT));
        inheritanceButton.addActionListener(targetAttributesPanelActionAdapter);
        inheritanceButton.setActionCommand("Call Inheritance");

        JButton subjectButton = new JButton("Subject");
        subjectButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        subjectButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        subjectButton.addActionListener(targetAttributesPanelActionAdapter);
        subjectButton.setActionCommand("Call Subject");


        this.addGroupButton = new JButton("Add");
        this.addGroupButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.addGroupButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.addGroupButton.addActionListener(targetAttributesPanelActionAdapter);
        this.addGroupButton.setActionCommand("Add Attribute");

        this.updateGroupButton = new JButton("Update");
        this.updateGroupButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.updateGroupButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.updateGroupButton.addActionListener(targetAttributesPanelActionAdapter);
        this.updateGroupButton.setActionCommand("Update Attribute");

        this.deleteGroupButton = new JButton("Delete");
        this.deleteGroupButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.deleteGroupButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.deleteGroupButton.addActionListener(targetAttributesPanelActionAdapter);
        this.deleteGroupButton.setActionCommand("Delete Attribute");

        /******************** attributePanel panel *************************/

        JPanel attributeButtonPanel = new JPanel();
        attributeButtonPanel.setLayout(new BoxLayout(attributeButtonPanel, BoxLayout.X_AXIS));
        attributeButtonPanel.add(addAttributeButton);
        if (!this.targetType.equals(TargetInfo.INHERITANCE))
        {
            attributeButtonPanel.add(updateAttributeButton);
        }
        //WB
        attributeButtonPanel.add(deleteAttributeButton);
        if (this.targetType.equals(TargetInfo.SUBJECTS))
        {
            attributeButtonPanel.add(inheritanceButton);
        }
        if (this.targetType.equals(TargetInfo.INHERITANCE))
        {
            attributeButtonPanel.add(subjectButton);
        }

        JPanel attributePanel = new JPanel();
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder("Attributes");

        /* Bruce Batson June 2013 Edit */
        // Changes the name of border1 for the Inheritance Tab
        if (this.targetType.equals(TargetInfo.INHERITANCE))
        {
            titledBorder1 = BorderFactory.createTitledBorder("Beneficiary");
        }
        /* End of Edit */

        attributePanel.setBorder(titledBorder1);
        attributePanel.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));
        attributePanel.add(listScroller);
        attributePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        attributePanel.add(attributeButtonPanel);

        /******************** group panel *************************/

        JPanel groupButtonPanel = new JPanel();
        groupButtonPanel.setLayout(new BoxLayout(groupButtonPanel, BoxLayout.X_AXIS));
        groupButtonPanel.add(addGroupButton);
        if (!this.targetType.equals(TargetInfo.INHERITANCE))
        {
            groupButtonPanel.add(updateGroupButton);
        }
        groupButtonPanel.add(deleteGroupButton);

        JPanel groupPanel = new JPanel();
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder("Attribute Values");

        /* Bruce Batson June 2013 Edit */
        // Changes the name of border2 for the Inheritance Tab
        if (this.targetType.equals(TargetInfo.INHERITANCE))
        {
            titledBorder2 = BorderFactory.createTitledBorder("Inherited Values");
        }
        /* End of Edit */

        groupPanel.setBorder(titledBorder2);
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.add(grouplistScroller);
        groupPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        groupPanel.add(groupButtonPanel);

        /********************
         * attribute properties panel
         *************************/

        titledBorder = BorderFactory.createTitledBorder("Properties");
        this.setBorder(titledBorder);
        this.setLayout(new BorderLayout());
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createRigidArea(new Dimension(100, GraphicsUtil.FIELD_HEIGHT)));
        this.add(attributePanel);
        this.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));
        this.add(groupPanel);

        /*
         * listmodel.clear(); listmodel.removeAllElements(); listmodel.clear();
         * list.repaint(); list.revalidate();
         */
        /*
         * for (int i = 0; i < list.getLastVisibleIndex(); i++) {
         * System.err.println(list.); }
         */
        /*
         * String[] choices =
         * TargetTreeUtil.getTargetTreeChildrenValues2StrArr(attributeNode,
         * attributeType, nodeLevel);
         *
         * for (int i = 0; i < choices.length; i++) {
         * System.err.println(choices[i]); }
         */
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

    /*
     * public String extractTypeOfAttribute(String val){ String[] type =
     * val.split(";"); return type[0]; } public String
     * extractValueOfAttribute(String val){ String[] type = val.split(";");
     * return type[1]; }
     */
    public void addChildNode(String type, JList selectedlist, DefaultListModel selectedlistmodel)
    {

        TargetDialog myDialog = null;
        InheritanceDialog myDialog2 = null;
        if (type.equals(AttributeType))
        {

            /* Bruce Batson June 2013 Edit */
            /*
             * This checks to see if the user is adding inheritance. If so it
             * open the InheritanceDialog. Else it opens TargetDialog.
             */
            if (this.targetType.equals(TargetInfo.INHERITANCE))
            {
                DefaultMutableTreeNode rootNode =
                        TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.SUBJECTS, TargetInfo.Level);
                List subjects = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, TargetInfo.SUBJECT_ATTR_VALUSES,
                        TargetInfo.nodeLevel);
                if (subjects.size() <= 0)
                {
                    frame.logPanel.append("Add a subject attribute value before giving Inheritance");
                    return;
                }
                myDialog2 = new InheritanceDialog(frame, targetTree, "Add", true, TargetInfo.AttrType, AttributeType,
                        "", "");
            }

            else
            {
                myDialog = new TargetDialog(frame, "Add", true, true, TargetInfo.AttrType, AttributeType, "", ""); // frame,
                                                                                                                   // type
                                                                                                                   // on-off,
                                                                                                                   // type,
                                                                                                                   // value
            }
            /* End of Edit */
        }
        else if (this.list.getSelectedIndex() < 0)
        {

            frame.logPanel.append("Error: add and select attributes first before adding attribute values");
            return;
        }
        else if (type.equals(AttributeValueType))
        {

            /* Bruce Batson June 2013 Edit */
            /*
             * This checks to see if the user is adding inheritance. If so it
             * open the InheritanceDialog. Else it opens TargetDialog.
             */
            String AttrType = ((TargetInfo) ((DefaultMutableTreeNode) this.list.getSelectedValue()).getUserObject())
                    .gettypeOfvalue();
            if (this.targetType.equals(TargetInfo.INHERITANCE))
            {
                DefaultMutableTreeNode rootNode =
                        TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.SUBJECTS, TargetInfo.Level);
                List subjects = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, TargetInfo.SUBJECT_ATTR_VALUSES,
                        TargetInfo.nodeLevel);
                if (subjects.size() <= 0)
                {
                    frame.logPanel.append("Add a subject attribute value before giving Inheritance");
                    return;
                }
                myDialog2 = new InheritanceDialog(frame, targetTree, "Add", true, TargetInfo.AttrValueType,
                        AttributeType, AttrType, "");
            }

            else
            {
                // String AttrType =
                // extractTypeOfAttribute(this.list.getSelectedValue().toString());
                myDialog = new TargetDialog(frame, "Add", true, false, TargetInfo.AttrValueType, AttributeType,
                        AttrType, ""); // frame, type
                // on-off, type,
                // value
            }

        }
        if (this.targetType.equals(TargetInfo.INHERITANCE))
        {
            if (myDialog2.getAnswer())
            {
                String TargetAttrType = myDialog2.getMyType();
                String TargetAttrValue = myDialog2.getMyValue();

                DefaultMutableTreeNode newNode =
                        new DefaultMutableTreeNode(new TargetInfo(type, TargetAttrType + ";" + TargetAttrValue));
                String holdType = ((TargetInfo) newNode.getUserObject()).getTargetType();
                // Checks for duplicates
                if (holdType.equals(TargetInfo.INHERITANCE_ATTRIBUTES))
                {
                    DefaultMutableTreeNode rootNode =
                            TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.INHERITANCE, 1);
                    List ia = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, TargetInfo.INHERITANCE_ATTRIBUTES,
                            2);
                    for (int i = 0; i < ia.size(); i++)
                    {
                        // If there is a duplicate don't add to list
                        if (((TargetInfo) ((DefaultMutableTreeNode) ia.get(i)).getUserObject()).getvalueOfvalue()
                                .equals(((TargetInfo) newNode.getUserObject()).getvalueOfvalue()))
                        {
                            JOptionPane.showMessageDialog(null, "Can not add a duplicate");
                            return;
                        }
                    }
                }

                // add to list
                selectedlistmodel.addElement(newNode);
                selectedlist.setSelectedIndex(selectedlist.getLastVisibleIndex());

                // add to tree
                if (selectedlist == this.list)
                {

                    // attribute value node with type is added
                    attributeNode.add(newNode);
                    ((DefaultTreeModel) targetTree.getModel()).reload(attributeNode);

                }
                else if ((selectedlist == this.grouplist) && (this.list.getSelectedIndex() >= 0))
                {
                    DefaultMutableTreeNode pnode =
                            (DefaultMutableTreeNode) this.listmodel.get(this.list.getSelectedIndex());
                    if (pnode != null)
                    {
                        pnode.add(newNode);
                        ((DefaultTreeModel) targetTree.getModel()).reload(attributeNode);
                        ((DefaultTreeModel) targetTree.getModel()).reload(pnode);
                    }
                }

                if (holdType.equals(TargetInfo.INHERITANCE_ATTR_VALUSES))
                {
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) newNode.getParent();
                    DefaultMutableTreeNode rootNode =
                            TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.INHERITANCE, 1);
                    // Checking for a duplicate
                    List iav = TargetTreeUtil.getTargetTreeChildrenNode2List(parentNode,
                            TargetInfo.INHERITANCE_ATTR_VALUSES, 3);
                    int count = 0;
                    for (int i = 0; i < iav.size(); i++)
                    {
                        if (((TargetInfo) ((DefaultMutableTreeNode) iav.get(i)).getUserObject()).getvalueOfvalue()
                                .equals(((TargetInfo) newNode.getUserObject()).getvalueOfvalue()))
                        {
                            count++;
                        }
                    }
                    // If there is a duplicate then remove it from the list and
                    // tree
                    if (count > 1)
                    {
                        // remove from the list
                        selectedlistmodel.removeElement(newNode);

                        // remove from the tree
                        parentNode = (DefaultMutableTreeNode) (newNode.getParent());
                        newNode.removeAllChildren();
                        parentNode.remove(newNode);
                        ((DefaultTreeModel) targetTree.getModel()).reload(parentNode);

                        if (selectedlist == this.list)
                        {
                            if (this.listmodel.capacity() > 0)
                            {
                                this.list.setSelectedIndex(0);
                            }
                            UpdateAttributeValueList();
                        }
                        JOptionPane.showMessageDialog(null, "Can not add a duplicate");
                        return;
                    }
                    // Check to see if there is a inheritance loop
                    List inheritance = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode,
                            TargetInfo.INHERITANCE_ATTR_VALUSES, 3);
                    boolean hasParent = false;
                    for (int i = 0; i < inheritance.size(); i++)
                    {
                        if (((TargetInfo) parentNode.getUserObject()).getvalueOfvalue()
                                .equals(((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(i)).getUserObject())
                                        .getvalueOfvalue()))
                        {
                            DefaultMutableTreeNode holdNode =
                                    (DefaultMutableTreeNode) ((DefaultMutableTreeNode) inheritance.get(i)).getParent();
                            if (((TargetInfo) holdNode.getUserObject()).getvalueOfvalue()
                                    .equals(((TargetInfo) newNode.getUserObject()).getvalueOfvalue()))
                            {
                                // remove from the list
                                selectedlistmodel.removeElement(newNode);

                                // remove from the tree
                                parentNode = (DefaultMutableTreeNode) (newNode.getParent());
                                newNode.removeAllChildren();
                                parentNode.remove(newNode);
                                ((DefaultTreeModel) targetTree.getModel()).reload(parentNode);

                                if (selectedlist == this.list)
                                {
                                    if (this.listmodel.capacity() > 0)
                                    {
                                        this.list.setSelectedIndex(0);
                                    }
                                    UpdateAttributeValueList();
                                }
                                JOptionPane.showMessageDialog(null,
                                        "Can not add, because it will make an inheritance loop");
                                return;
                            }
                            hasParent = true;
                            while (hasParent)
                            {
                                hasParent = false;
                                for (int j = 0; j < inheritance.size(); j++)
                                {
                                    if (((TargetInfo) holdNode.getUserObject()).getvalueOfvalue().equals(
                                            ((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(j)).getUserObject())
                                                    .getvalueOfvalue()))
                                    {
                                        holdNode =
                                                (DefaultMutableTreeNode) ((DefaultMutableTreeNode) inheritance.get(j))
                                                        .getParent();
                                        if (((TargetInfo) holdNode.getUserObject()).getvalueOfvalue()
                                                .equals(((TargetInfo) newNode.getUserObject()).getvalueOfvalue()))
                                        {
                                            // remove from the list
                                            selectedlistmodel.removeElement(newNode);

                                            // remove from the tree
                                            parentNode = (DefaultMutableTreeNode) (newNode.getParent());
                                            newNode.removeAllChildren();
                                            parentNode.remove(newNode);
                                            ((DefaultTreeModel) targetTree.getModel()).reload(parentNode);

                                            if (selectedlist == this.list)
                                            {
                                                if (this.listmodel.capacity() > 0)
                                                {
                                                    this.list.setSelectedIndex(0);
                                                }
                                                UpdateAttributeValueList();
                                            }
                                            JOptionPane.showMessageDialog(null,
                                                    "Can not add, because it will make an inheritance loop");
                                            return;
                                        }
                                        hasParent = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        else
        {

            /* End of Edit */
            if (myDialog.getAnswer())
            {

                // String childTargetName = myDialog.getSelectedString();
                String TargetAttrType = myDialog.getMyType();
                String TargetAttrValue = myDialog.getMyValue();

                DefaultMutableTreeNode newNode =
                        new DefaultMutableTreeNode(new TargetInfo(type, TargetAttrType + ";" + TargetAttrValue));

                // duplicate check
                if (type.equals(TargetInfo.SUBJECT_ATTRIBUTES) || type.equals(TargetInfo.RESOURCE_ATTRIBUTES)
                        || type.equals(TargetInfo.ACTION_ATTRIBUTES) || type.equals(TargetInfo.ENVIRONMENT_ATTRIBUTES))
                {

                    if (targetTree != null)
                    {

                        DefaultMutableTreeNode rootNode = TargetTreeUtil.getRootNode(targetTree);
                        List<String> typelist = new ArrayList<String>();
                        typelist.add(TargetInfo.SUBJECT_ATTRIBUTES);
                        typelist.add(TargetInfo.RESOURCE_ATTRIBUTES);
                        typelist.add(TargetInfo.ACTION_ATTRIBUTES);
                        typelist.add(TargetInfo.ENVIRONMENT_ATTRIBUTES);

                        for (int i = 0; i < typelist.size(); i++)
                        {

                            System.out.println("TargetPropertiesPanel.addChildNode()" + rootNode + "------------ "
                                    + typelist.get(i));
                            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode,
                                    typelist.get(i).toString(), TargetInfo.attrLevel);

                            for (int j = 0; j < choices.size(); j++)
                            {
                                String valueOfvalue =
                                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(j)).getUserObject())
                                                .getvalueOfvalue();
                                if (TargetAttrValue.equalsIgnoreCase(valueOfvalue))
                                {
                                    JOptionPane.showMessageDialog(null,
                                            "cannot add: duplicate name exist in the " + typelist.get(i).toString(),
                                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                                    return;
                                }
                            }
                        }
                    }

                }
                else
                {
                    for (int i = 0; i < selectedlistmodel.size(); i++)
                    {
                        String iterString = ((DefaultMutableTreeNode) selectedlistmodel.get(i)).toString();
                        if (iterString.equalsIgnoreCase(newNode.toString()))
                        {
                            JOptionPane.showMessageDialog(null, "cannot add: duplicate name", "Warning",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                }

                selectedlistmodel.addElement(newNode);
                selectedlist.setSelectedIndex(selectedlist.getLastVisibleIndex());

                if (selectedlist == this.list)
                {

                    // attribute value node with type is added
                    attributeNode.add(newNode);
                    ((DefaultTreeModel) targetTree.getModel()).reload(attributeNode);

                    // add True/False attribute element nodes automatically when
                    // boolean attribute is added
                    if (TargetAttrType.equals("Boolean"))
                    {

                        DefaultMutableTreeNode trueNode = new DefaultMutableTreeNode(
                                new TargetInfo(this.AttributeValueType, TargetAttrType + ";" + "True"));

                        DefaultMutableTreeNode falseNode = new DefaultMutableTreeNode(
                                new TargetInfo(this.AttributeValueType, TargetAttrType + ";" + "False"));

                        grouplistmodel.addElement(trueNode);
                        grouplistmodel.addElement(falseNode);
                        grouplist.setSelectedIndex(grouplist.getLastVisibleIndex());

                        // refresh
                        newNode.add(trueNode);
                        newNode.add(falseNode);
                        ((DefaultTreeModel) targetTree.getModel()).reload(newNode);
                    }

                }
                else if ((selectedlist == this.grouplist) && (this.list.getSelectedIndex() >= 0))
                {
                    DefaultMutableTreeNode pnode =
                            (DefaultMutableTreeNode) this.listmodel.get(this.list.getSelectedIndex());
                    if (pnode != null)
                    {
                        pnode.add(newNode);
                        ((DefaultTreeModel) targetTree.getModel()).reload(attributeNode);
                        ((DefaultTreeModel) targetTree.getModel()).reload(pnode);
                    }
                }
            }
        }
        if (this.targetType.equals(TargetInfo.INHERITANCE))
        {
            myDialog2.dispose();
        }
        else
        {
            myDialog.dispose();
        }
        // if (childTargetName == null) {
        //
        // return;
        //
        // } else {
        //
        // DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
        // new TargetInfo(type, childTargetName));
        // selectedlistmodel.addElement(newNode);
        // selectedlist.setSelectedIndex(selectedlist.getLastVisibleIndex());
        //
        // if (selectedlist == this.list){
        // attributeNode.add(newNode);
        // ((DefaultTreeModel)targetTree.getModel()).reload(attributeNode);
        // }
        // else if (selectedlist == this.grouplist &&
        // this.list.getSelectedIndex() >= 0){
        // DefaultMutableTreeNode pnode = (DefaultMutableTreeNode)
        // this.listmodel.get(this.list.getSelectedIndex());
        // if (pnode != null){
        // pnode.add(newNode);
        // ((DefaultTreeModel)targetTree.getModel()).reload(attributeNode);
        // ((DefaultTreeModel)targetTree.getModel()).reload(pnode);
        // }
        // }
        // }

    }

    public void updateChildNode(String type, JList selectedlist, DefaultListModel selectedlistmodel)
    {

        if (selectedlist.getSelectedIndex() >= 0)
        {
            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) selectedlistmodel.get(selectedlist.getSelectedIndex());

            // String AttrType = extractTypeOfAttribute(currentNode.toString());
            // String AttrValue =
            // extractValueOfAttribute(currentNode.toString());

            String AttrType = ((TargetInfo) ((DefaultMutableTreeNode) selectedlist.getSelectedValue()).getUserObject())
                    .gettypeOfvalue();
            String AttrValue = ((TargetInfo) ((DefaultMutableTreeNode) selectedlist.getSelectedValue()).getUserObject())
                    .getvalueOfvalue();
            TargetDialog myDialog = null;

            if (type.equals(AttributeType))
            {
                if (currentNode.getChildCount() == 0)
                {
                    myDialog = new TargetDialog(frame, "Update", true, true, TargetInfo.AttrType, AttributeType,
                            AttrType, AttrValue);
                }
                else
                {
                    myDialog = new TargetDialog(frame, "Update", true, false, TargetInfo.AttrType, AttributeType,
                            AttrType, AttrValue);
                }
            }
            else if (type.equals(AttributeValueType))
            {
                myDialog = new TargetDialog(frame, "Update", true, false, TargetInfo.AttrValueType, AttributeType,
                        AttrType, AttrValue); // frame,
                // type
                // on-off,
                // type,
                // value
            }

            if (myDialog.getAnswer())
            {

                String TargetAttrType = myDialog.getMyType();
                String TargetAttrValue = myDialog.getMyValue();

                String TargetOldAttrType = myDialog.getMyOldType();
                String TargetOldAttrValue = myDialog.getMyOldValue();

                DefaultMutableTreeNode tempNode =
                        new DefaultMutableTreeNode(new TargetInfo(type, TargetAttrType + ";" + TargetAttrValue));

                // duplicate check
                if (type.equals(TargetInfo.SUBJECT_ATTRIBUTES) || type.equals(TargetInfo.RESOURCE_ATTRIBUTES)
                        || type.equals(TargetInfo.ACTION_ATTRIBUTES) || type.equals(TargetInfo.ENVIRONMENT_ATTRIBUTES))

                {

                    if (targetTree != null)
                    {

                        DefaultMutableTreeNode rootNode = TargetTreeUtil.getRootNode(targetTree);
                        List<String> typelist = new ArrayList<String>();
                        typelist.add(TargetInfo.SUBJECT_ATTRIBUTES);
                        typelist.add(TargetInfo.RESOURCE_ATTRIBUTES);
                        typelist.add(TargetInfo.ACTION_ATTRIBUTES);
                        typelist.add(TargetInfo.ENVIRONMENT_ATTRIBUTES);

                        for (int i = 0; i < typelist.size(); i++)
                        {
                            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode,
                                    typelist.get(i).toString(), TargetInfo.attrLevel);
                            for (int j = 0; j < choices.size(); j++)
                            {
                                String valueOfvalue =
                                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(j)).getUserObject())
                                                .getvalueOfvalue();
                                // 1. no duplicate with other names except it
                                // can be same with old name when only requiring
                                // change of types
                                if (TargetAttrValue.equalsIgnoreCase(valueOfvalue)
                                        && !TargetAttrValue.equalsIgnoreCase(TargetOldAttrValue))
                                {
                                    JOptionPane.showMessageDialog(null,
                                            "cannot update: duplicate name exist in the " + typelist.get(i).toString(),
                                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                                    return;
                                }
                            }
                        }
                    }

                }
                else
                {
                    for (int i = 0; i < selectedlistmodel.size(); i++)
                    {
                        String iterString = ((DefaultMutableTreeNode) selectedlistmodel.get(i)).toString();
                        // check duplicate when it is not itself.
                        if (selectedlist.getSelectedIndex() != i)
                        {
                            if (iterString.equalsIgnoreCase(tempNode.toString()))
                            {
                                JOptionPane.showMessageDialog(null, "cannot update: duplicate name", "Warning",
                                        JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        }
                    }
                }

                // String newTargetName = myDialog.getSelectedString();

                // update tree
                currentNode.setUserObject(new TargetInfo(type, TargetAttrType + ";" + TargetAttrValue));
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
                ((DefaultTreeModel) targetTree.getModel()).reload(parentNode);

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
            // if (newTargetName == null || newTargetName.equals("")) {return;};

        }
    }

    public void deleteChildNode(String type, JList selectedlist, DefaultListModel selectedlistmodel)
    {

        // String childTargetName = (String) selectedlist.getSelectedValue();

        if (selectedlist.getSelectedIndex() >= 0)
        {

            // remove from the list
            DefaultMutableTreeNode currentNode =
                    (DefaultMutableTreeNode) selectedlistmodel.get(selectedlist.getSelectedIndex());
            selectedlistmodel.removeElement(currentNode);

            // remove from the tree
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
            currentNode.removeAllChildren();
            parentNode.remove(currentNode);
            ((DefaultTreeModel) targetTree.getModel()).reload(parentNode);

            if (selectedlist == this.list)
            {
                if (this.listmodel.capacity() > 0)
                {
                    this.list.setSelectedIndex(0);
                }
                UpdateAttributeValueList();
            }

        }
    }

    public void removeNode()
    {

        TargetTreeUtil.removeNode(targetTree);

    }

    public void UpdateAttributeValueList()
    {
        grouplistmodel.clear();

        if (this.list.getSelectedIndex() >= 0)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) listmodel.get(list.getSelectedIndex());
            if (node != null)
            {
                List choices =
                        TargetTreeUtil.getTargetTreeChildrenNode2List(node, AttributeValueType, TargetInfo.nodeLevel);
                for (int i = 0; i < choices.size(); i++)
                {
                    grouplistmodel.addElement(choices.get(i));
                }
                if (grouplistmodel.capacity() > 0)
                {
                    grouplist.setSelectedIndex(0);
                }
            }
        }
        else
        {

        }

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
    class SubjectsListSelectionAdapter implements ListSelectionListener
    {

        TargetPropertiesPanel subjectsPanel = null;

        SubjectsListSelectionAdapter(TargetPropertiesPanel subjectsPanel)
        {

            this.subjectsPanel = subjectsPanel;

        }

        @Override
        public void valueChanged(ListSelectionEvent e)
        {

            System.out.println("List : " + "valueChanged");

            if (e.getSource() == list)
            {

                UpdateAttributeValueList();

            }
            if (e.getSource() == grouplist)
            {
            }
        }

    }

    class SubjectsPanelActionAdapter implements ActionListener
    {

        TargetPropertiesPanel subjectsPanel = null;

        SubjectsPanelActionAdapter(TargetPropertiesPanel subjectsPanel)
        {

            this.subjectsPanel = subjectsPanel;

        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getActionCommand().equals("Add Attribute Value"))
            {
                if (targetType.equals(TargetInfo.INHERITANCE))
                {
                    frame.logPanel.append("Note that this action will not update any rules in ABAC, ");
                    frame.logPanel.append("Multilevel, or Workflow.");
                }
                targetParentNode = attributeNode;
                addChildNode(AttributeType, list, listmodel);

            }
            else if (e.getActionCommand().equals("Update Attribute Value"))
            {

                if (!ContainsInPolicyModelsOrProperties(AttributeType, list, listmodel))
                {
                    return;
                }

                frame.logPanel.append("Note that this action updates attribute values information");
                frame.logPanel.append("without updating attribute values used in inheritance, policies, or properties");
                // JOptionPane
                // .showMessageDialog(
                // null,
                // "This feature updates only attribute information (without
                // updating attribute information used in policies). Updating
                // attribute information could produce unexpected behaviors.",
                // "Warning", JOptionPane.INFORMATION_MESSAGE);

                targetParentNode = attributeNode;
                updateChildNode(AttributeType, list, listmodel);

            }
            else if (e.getActionCommand().equals("Delete Attribute Value"))
            {

                frame.logPanel.append("Note that this action deletes only attribute values information");
                frame.logPanel.append("without deleting attribute values used in inheritance, policies, or properties");
                if (!ContainsInPolicyModelsOrProperties(AttributeType, list, listmodel))
                {
                    return;
                }

                // JOptionPane
                // .showMessageDialog(
                // null,
                // "This feature deletes only attribute information (without
                // updating attribute information used in policies). Deleting
                // attribute information could produce unexpected behaviors.",
                // "Warning", JOptionPane.INFORMATION_MESSAGE);

                targetParentNode = attributeNode;
                deleteChildNode(AttributeType, list, listmodel);

            }
            else if (e.getActionCommand().equals("Add Attribute"))
            {

                targetParentNode = groupNode;
                if (targetType.equals(TargetInfo.INHERITANCE))
                {
                    frame.logPanel.append("Note that this action will not update any rules in ABAC, ");
                    frame.logPanel.append("Multilevel, or Workflow.");
                }
                addChildNode(AttributeValueType, grouplist, grouplistmodel);

            }
            else if (e.getActionCommand().equals("Update Attribute"))
            {
                /* Bruce Batson June 2013 Edit */
                frame.logPanel.append("Note that this action updates only attribute values information");
                frame.logPanel.append("without updating inheritance");
                /* End of edit */
                if (!ContainsInPolicyModelsOrProperties(AttributeType, grouplist, grouplistmodel))
                {
                    return;
                }

                // JOptionPane
                // .showMessageDialog(
                // null,
                // "This feature updates only attribute information (without
                // updating attribute information used in policies). Updating
                // attribute information could produce unexpected behaviors.",
                // "Warning", JOptionPane.INFORMATION_MESSAGE);

                targetParentNode = groupNode;
                updateChildNode(AttributeValueType, grouplist, grouplistmodel);

            }
            else if (e.getActionCommand().equals("Delete Attribute"))
            {
                /* Bruce Batson June 2013 Edit */
                frame.logPanel.append("Note that this action deletes only attribute values information");
                frame.logPanel.append("without deleting attribute values used in inheritance, policies, or properties");

                if (targetType.equals(TargetInfo.INHERITANCE))
                {
                    if (!ContainsInPolicyModelsOrProperties(AttributeValueType, grouplist, grouplistmodel))
                    {
                        return;
                    }

                }
                /* End of edit */
                if (!ContainsInPolicyModelsOrProperties(AttributeType, grouplist, grouplistmodel))
                {

                    return;
                }

                // JOptionPane
                // .showMessageDialog(
                // null,
                // "This feature deletes only attribute information (without
                // updating attribute information used in policies). Deleting
                // attribute information could produce unexpected behaviors.",
                // "Warning", JOptionPane.INFORMATION_MESSAGE);

                targetParentNode = groupNode;
                deleteChildNode(AttributeValueType, grouplist, grouplistmodel);

            }
            //WB
            else if (e.getActionCommand().equals("Call Inheritance"))
            {
                subjectsPanel.frame.moveToBack();


            }
            else if (e.getActionCommand().equals("Call Subject"))
            {
                subjectsPanel.frame.moveToFront();


            }
            else
            {

                // System.err.println("GroupPanel warning: command not
                // implemented: "
                // +
                // e.getActionCommand());
            }

        }

        private boolean ContainsInPolicyModelsOrProperties(String attributeType, JList selectedlist,
                DefaultListModel selectedlistmodel)
        {

            boolean returnType = true;
            String reason = "";

            JTree modelTree = frame.modelPanel.getModelTree();
            JTree propertyTree = frame.propertyGenerationPanel.getGenericTree();

            DefaultMutableTreeNode modeltreeRootNode = ModelTreeUtil.getFirstMatchingNode(modelTree, ModelInfo.ROOT, 0);
            DefaultMutableTreeNode propertyreeRooNode = GenericTreeUtil.getRootNode(propertyTree);
            /* Bruce Batson June 2013 Edit */
            /*
             * This takes the info from the inherited value that is trying to be
             * deleted and makes a sting to check if the inherited value that is
             * trying to be deleted is used in a rule.
             */
            String matchingString = null;
            if (selectedlist.getSelectedIndex() >= 0)
            {
                if (attributeType.equals(TargetInfo.INHERITANCE_ATTRIBUTES)
                        || attributeType.equals(TargetInfo.INHERITANCE_ATTR_VALUSES))
                {
                    DefaultMutableTreeNode currentNode =
                            (DefaultMutableTreeNode) selectedlistmodel.get(selectedlist.getSelectedIndex());
                    String holdToV = ((TargetInfo) currentNode.getUserObject()).gettypeOfvalue();
                    String holdVoV = ((TargetInfo) currentNode.getUserObject()).getvalueOfvalue();
                    DefaultMutableTreeNode parentNode =
                            TargetTreeUtil.getFirstMatchingNode(targetTree, holdToV, TargetInfo.SUBJECT_ATTRIBUTES, 2);
                    for (int i = 0; i < parentNode.getChildCount(); i++)
                    {
                        DefaultMutableTreeNode holdNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);
                        String holdsub = ((TargetInfo) holdNode.getUserObject()).getvalueOfvalue();
                        if (holdsub.equals(holdVoV))
                        {
                            currentNode = holdNode;
                        }
                    }

                    matchingString = TargetInfo.SUBJECT_ATTRIBUTES + ";"
                            + ((TargetInfo) parentNode.getUserObject()).getvalueOfvalue() + ";"
                            + ((TargetInfo) currentNode.getUserObject()).getValue();
                }
                /* End of Edit */
                else
                {
                    DefaultMutableTreeNode currentNode =
                            (DefaultMutableTreeNode) selectedlistmodel.get(selectedlist.getSelectedIndex());

                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) currentNode.getParent();

                    if (selectedlist == list)
                    {
                        matchingString = attributeType + ";" + currentNode.toString();
                    }
                    else
                    {
                        matchingString = attributeType + ";" + parentNode.toString() + ";" + currentNode.toString();

                    }
                }

                if (matchingString.startsWith("Action Attributes;MLSDefaultAction;String"))
                {
                    JOptionPane.showMessageDialog(null,
                            "Cannot update/delete the selected attribute.\nThe selected attribute is default attribute to create Multilevel Security Policies.\n",
                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                    return false;
                }

                System.err.println("matchingString:" + matchingString);

                if (modeltreeRootNode != null)
                {

                    // ABAC and Workflow checking
                    String[] selectedType =
                            {
                                    ModelInfo.ABAC, ModelInfo.WORKFLOW, ModelInfo.MULTILEVEL
                    };

                    for (int i = 0; i < selectedType.length; i++)
                    {
                        List choices =
                                ModelTreeUtil.getTargetTreeChildrenNode2List(modeltreeRootNode, selectedType[i], 2);

                        for (int j = 0; j < choices.size(); j++)
                        {

                            DefaultMutableTreeNode selectedModelRootNode = (DefaultMutableTreeNode) choices.get(j);

                            ModelInfo target = (ModelInfo) selectedModelRootNode.getUserObject();
                            // if (node.getLevel() == NodeLevel &&
                            // target.getModelType().equals(TargetInfoType))
                            //
                            // System.err.println(selectedModelRootNode.toString());
                            // System.err.println(ModelInfo.ABAC);

                            if (target.getModelType().equals(ModelInfo.ABAC)
                                    || target.getModelType().equals(ModelInfo.WORKFLOW))
                            {

                                String[] rules = ModelTreeUtil.getTargetTreeChildrenValues2StrArr(selectedModelRootNode,
                                        selectedType[i] + "RULES", 4);
                                // Bruce Batson edit July 2013
                                if (attributeType.equals(TargetInfo.INHERITANCE_ATTRIBUTES))
                                {
                                    // Checks if the deleted inherit value is
                                    // used in a rule made by inheritance.
                                    List Modrules = ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode,
                                            selectedType[i] + "RULES", 4);
                                    for (int k = 1; k < rules.length; k++)
                                    {
                                        String rulenum =
                                                ((ModelInfo) ((DefaultMutableTreeNode) Modrules.get(k)).getUserObject())
                                                        .getRuleNum();
                                        String prerulenum = ((ModelInfo) ((DefaultMutableTreeNode) Modrules.get(k - 1))
                                                .getUserObject()).getRuleNum();
                                        // System.err.println("Compareing " +
                                        // rulenum + " to " + prerulenum);
                                        // System.err.println(rules[k]);
                                        if ((rules[k].indexOf(matchingString) != -1) && rulenum.equals(prerulenum))
                                        {
                                            returnType = false;
                                            reason += "\t-" + target.getModelType() + "#" + choices.get(j).toString()
                                                    + "'s " + (k + 1) + "th rule includes the selected attribute\n";
                                        }
                                    }
                                }
                                else if (attributeType.equals(TargetInfo.INHERITANCE_ATTR_VALUSES))
                                {
                                    // Checks if a rule was made because of the
                                    // deleted value was inherited.
                                    List Modrules = ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode,
                                            selectedType[i] + "RULES", 4);
                                    for (int k = 0; k < (rules.length - 1); k++)
                                    {
                                        String rulenum =
                                                ((ModelInfo) ((DefaultMutableTreeNode) Modrules.get(k)).getUserObject())
                                                        .getRuleNum();
                                        String prerulenum = ((ModelInfo) ((DefaultMutableTreeNode) Modrules.get(k + 1))
                                                .getUserObject()).getRuleNum();
                                        // System.err.println("Compareing " +
                                        // rulenum + " to " + prerulenum);
                                        // System.err.println(rules[k]);
                                        if ((rules[k].indexOf(matchingString) != -1) && rulenum.equals(prerulenum))
                                        {
                                            returnType = false;
                                            reason += "\t-" + target.getModelType() + "#" + choices.get(j).toString()
                                                    + "'s " + (k + 1) + "th rule includes the selected attribute\n";
                                        }
                                    }
                                }
                                else
                                {
                                    // End of eidt
                                    for (int k = 0; k < rules.length; k++)
                                    {
                                        System.err.println(rules[k]);

                                        if (rules[k].indexOf(matchingString) != -1)
                                        { // true
                                            returnType = false;
                                            reason += "\t-" + target.getModelType() + "#" + choices.get(j).toString()
                                                    + "'s " + (k + 1) + "th rule includes the selected attribute\n";
                                        }
                                    }
                                }
                            }
                            else if (target.getModelType().equals(ModelInfo.MULTILEVEL))
                            {

                                String[] SubLevelrules = ModelTreeUtil.getTargetTreeChildrenValues2StrArr(
                                        selectedModelRootNode, ModelInfo.MULTILEVELSUBJECTLEVELS, 4);
                                // Bruce Batson edit July 2013
                                // Checks to see if rule was edited by
                                // inheritance
                                DefaultMutableTreeNode rootNode =
                                        TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.INHERITANCE, 1);
                                List inherit = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode,
                                        TargetInfo.INHERITANCE_ATTRIBUTES, 2);
                                // End of edit

                                for (int k = 0; k < SubLevelrules.length; k++)
                                {
                                    // Bruce Batson edit July 2013
                                    for (int x = 0; x < inherit.size(); x++)
                                    {
                                        String ToV =
                                                ((TargetInfo) ((DefaultMutableTreeNode) inherit.get(x)).getUserObject())
                                                        .gettypeOfvalue();
                                        String VoV =
                                                ((TargetInfo) ((DefaultMutableTreeNode) inherit.get(x)).getUserObject())
                                                        .getvalueOfvalue();
                                        if (((DefaultMutableTreeNode) inherit.get(x)).getChildCount() == 1)
                                        {
                                            rootNode =
                                                    ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) inherit.get(x))
                                                            .getChildAt(0));
                                            String replace = ((TargetInfo) rootNode.getUserObject()).getvalueOfvalue();
                                            if (SubLevelrules[k].indexOf(ToV) != -1)
                                            {
                                                SubLevelrules[k] = SubLevelrules[k].replace(replace, VoV);
                                            }
                                        }
                                    }
                                    SubLevelrules[k] = SubLevelrules[k].replace('#', ';');
                                    // End of edit
                                    if (SubLevelrules[k].indexOf(matchingString) != -1)
                                    { // true
                                        returnType = false;
                                        reason +=
                                                "\t-" + target.getModelType() + "#" + choices.get(j).toString()
                                                        + "'s " + (k
                                                                + 1)
                                                + "th subject level attribute includes the selected attribute\n";
                                    }
                                }

                                String[] ResLevelrules = ModelTreeUtil.getTargetTreeChildrenValues2StrArr(
                                        selectedModelRootNode, ModelInfo.MULTILEVELRESOURCELEVELS, 4);

                                for (int k = 0; k < ResLevelrules.length; k++)
                                {
                                    // Bruce Batson edit July 2013
                                    SubLevelrules[k] = SubLevelrules[k].replace('#', ';');
                                    // End of edit
                                    if (ResLevelrules[k].indexOf(matchingString) != -1)
                                    { // true
                                        returnType = false;
                                        reason +=
                                                "\t-" + target.getModelType() + "#" + choices.get(j).toString()
                                                        + "'s " + (k
                                                                + 1)
                                                + "th resource level attribute includes the selected attribute\n";
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                 * if (propertyTree != null){ List choices = (List)
                 * GenericTreeUtil.getTargetTreeChildrenNode2List(
                 * propertyreeRooNode, GenericInfo.PROPERTY, 1);
                 *
                 * String splitedStrings[] = currentNode.toString().split(";");
                 * matchingString = splitedStrings[0];
                 *
                 * for (int j = 0; j < choices.size(); j++) {
                 *
                 *
                 *
                 * System.err .println(attributeType); Pattern p =
                 * Pattern.compile(".*[^A-Za-z0-9_]"+matchingString+
                 * "[^A-Za-z0-9_].*");
                 *
                 * System.err .println(
                 * "TargetPropertiesPanel.SubjectsPanelActionAdapter.IsDeletableWOimpact()+matchingString"
                 * +matchingString); System.err .println(
                 * "TargetPropertiesPanel.SubjectsPanelActionAdapter.IsDeletableWOimpact()"
                 * +choices.get(j).toString()); Matcher m =
                 * p.matcher(choices.get(j).toString()); if (m.matches()){
                 * returnType = false; reason += "\t-Specfied propety list" +
                 * "'s " + (j + 1) +
                 * "th property includes the selected attribute\n"; } } }
                 */

            }

            if (returnType == false)
            {
                JOptionPane.showMessageDialog(null,
                        "Before update/delete the attribute, please delete rules (that includes the selected attribute) below.\n"
                                + reason,
                        "Warning", JOptionPane.INFORMATION_MESSAGE);
            }

            return returnType;
        }

    }

    /*
     * private boolean ContainsInProperties (String attributeType, JList
     * selectedlist, DefaultListModel selectedlistmodel) {
     *
     * boolean returnType = true; String reason = "";
     *
     * JTree propertyTree = frame.propertyGenerationPanel.getGenericTree();
     * DefaultMutableTreeNode propertyreeRooNode = GenericTreeUtil
     * .getRootNode(propertyTree);
     *
     * if (selectedlist.getSelectedIndex() >= 0) { DefaultMutableTreeNode
     * currentNode = (DefaultMutableTreeNode) selectedlistmodel
     * .get(selectedlist.getSelectedIndex());
     *
     * DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)
     * currentNode.getParent();
     *
     * String matchingString = null;
     *
     * if (selectedlist == list){ matchingString = attributeType + ";" +
     * currentNode.toString(); }else{ matchingString = attributeType + ";" +
     * parentNode.toString()+";" + currentNode.toString();
     *
     * }
     *
     * if (matchingString.startsWith("Action Attributes;MLSDefaultAction;String"
     * )){ JOptionPane .showMessageDialog( null,
     * "Cannot update/delete the selected attribute.\nThe selected attribute is default attribute to create Multilevel Security Policies.\n"
     * , "Warning", JOptionPane.INFORMATION_MESSAGE); return false; }
     *
     * if (propertyTree != null){ List choices = (List)
     * GenericTreeUtil.getTargetTreeChildrenNode2List(propertyreeRooNode,
     * GenericInfo.PROPERTY, 1);
     *
     * String splitedStrings[] = currentNode.toString().split(";");
     * matchingString = splitedStrings[0];
     *
     * for (int j = 0; j < choices.size(); j++) { System.err
     * .println(attributeType); Pattern p =
     * Pattern.compile(".*[^A-Za-z0-9_]"+matchingString+"[^A-Za-z0-9_].*");
     *
     * System.err .println(
     * "TargetPropertiesPanel.SubjectsPanelActionAdapter.IsDeletableWOimpact()+matchingString"
     * +matchingString); System.err .println(
     * "TargetPropertiesPanel.SubjectsPanelActionAdapter.IsDeletableWOimpact()"+
     * choices.get(j).toString()); Matcher m =
     * p.matcher(choices.get(j).toString()); if (m.matches()){ returnType =
     * false; reason += "\t-Specfied propety list" + "'s " + (j + 1) +
     * "th property includes the selected attribute\n"; } } }
     *
     * }
     *
     * if (returnType == false){ JOptionPane .showMessageDialog( null,
     * "Before update/delete the attribute, please delete rules (that includes the selected attribute) below.\n"
     * + reason, "Warning", JOptionPane.INFORMATION_MESSAGE); }
     *
     * return returnType; }
     *
     * }
     */
    /*
     * private boolean IsDeletableWOimpact(String attributeType, JList
     * selectedlist, DefaultListModel selectedlistmodel) {
     *
     * boolean returnType = true; String reason = "";
     *
     * // 1. checking rules // 2. checking properties JTree modelTree =
     * frame.modelPanel.getModelTree(); JTree propertyTree =
     * frame.propertyGenerationPanel.getGenericTree();
     *
     * // this.propertyTree = // frame.propertyGenerationPanel.getGenericTree();
     *
     * DefaultMutableTreeNode modeltreeRootNode = ModelTreeUtil
     * .getFirstMatchingNode(modelTree, ModelInfo.ROOT, 0);
     * DefaultMutableTreeNode propertyreeRooNode = GenericTreeUtil
     * .getRootNode(propertyTree);
     *
     * // GenericTreeUtil.getFirstMatchingNode(propertyTree, //
     * GenericInfo.ROOT, 0); // propertyreeRooNode = combinetreeRooNode;
     *
     * // System.err.println("delete check");
     *
     *
     * // grouplist
     *
     * // if (list.getSelectedIndex() >= 0) { // DefaultMutableTreeNode
     * parentNode = (DefaultMutableTreeNode) listmodel //
     * .get(list.getSelectedIndex()); // }
     *
     * if (selectedlist.getSelectedIndex() >= 0) { DefaultMutableTreeNode
     * currentNode = (DefaultMutableTreeNode) selectedlistmodel
     * .get(selectedlist.getSelectedIndex());
     *
     * DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)
     * currentNode.getParent();
     *
     * String matchingString = null;
     *
     * if (selectedlist == list){ matchingString = attributeType + ";" +
     * currentNode.toString(); }else{ matchingString = attributeType + ";" +
     * parentNode.toString()+";" + currentNode.toString();
     *
     * }
     *
     * if (matchingString.startsWith("Action Attributes;MLSDefaultAction;String"
     * )){ JOptionPane .showMessageDialog( null,
     * "Cannot update/delete the selected attribute.\nThe selected attribute is default attribute to create Multilevel Security Policies.\n"
     * , "Warning", JOptionPane.INFORMATION_MESSAGE); return false; }
     *
     *
     *
     * // System.err.println("matchingString:" + matchingString);
     *
     * if (modeltreeRootNode != null) {
     *
     * // ABAC and Workflow checking String[] selectedType = { ModelInfo.ABAC,
     * ModelInfo.WORKFLOW, ModelInfo.MULTILEVEL };
     *
     * for (int i = 0; i < selectedType.length; i++) { List choices = (List)
     * ModelTreeUtil .getTargetTreeChildrenNode2List( modeltreeRootNode,
     * selectedType[i], 2);
     *
     *
     * for (int j = 0; j < choices.size(); j++) {
     *
     * DefaultMutableTreeNode selectedModelRootNode = (DefaultMutableTreeNode)
     * choices .get(j);
     *
     * ModelInfo target = (ModelInfo) selectedModelRootNode.getUserObject(); //
     * if (node.getLevel() == NodeLevel &&
     * target.getModelType().equals(TargetInfoType)) // //
     * System.err.println(selectedModelRootNode.toString()); //
     * System.err.println(ModelInfo.ABAC);
     *
     * if (target.getModelType().equals(ModelInfo.ABAC)||target.getModelType().
     * equals(ModelInfo.WORKFLOW)){
     *
     * String[] rules = ModelTreeUtil .getTargetTreeChildrenValues2StrArr(
     * selectedModelRootNode, selectedType[i] + "RULES", 4);
     *
     * for (int k = 0; k < rules.length; k++) { // System.err.println(rules[k]);
     *
     * if (rules[k].indexOf(matchingString) != -1) { // true returnType = false;
     * reason += "\t-"+target.getModelType()+"#"+choices.get(j).toString() +
     * "'s " + (k + 1) + "th rule includes the selected attribute\n"; } } } else
     * if(target.getModelType().equals(ModelInfo.MULTILEVEL)){
     *
     * String[] SubLevelrules = ModelTreeUtil
     * .getTargetTreeChildrenValues2StrArr( selectedModelRootNode,
     * ModelInfo.MULTILEVELSUBJECTLEVELS, 4);
     *
     * for (int k = 0; k < SubLevelrules.length; k++) { if
     * (SubLevelrules[k].indexOf(matchingString) != -1) { // true returnType =
     * false; reason +=
     * "\t-"+target.getModelType()+"#"+choices.get(j).toString() + "'s " + (k +
     * 1) + "th subject level attribute includes the selected attribute\n"; } }
     *
     * String[] ResLevelrules = ModelTreeUtil
     * .getTargetTreeChildrenValues2StrArr( selectedModelRootNode,
     * ModelInfo.MULTILEVELRESOURCELEVELS, 4);
     *
     * for (int k = 0; k < ResLevelrules.length; k++) { if
     * (ResLevelrules[k].indexOf(matchingString) != -1) { // true returnType =
     * false; reason +=
     * "\t-"+target.getModelType()+"#"+choices.get(j).toString() + "'s " + (k +
     * 1) + "th resource level attribute includes the selected attribute\n"; } }
     * } } } }
     *
     *
     * if (propertyTree != null){ List choices = (List)
     * GenericTreeUtil.getTargetTreeChildrenNode2List(propertyreeRooNode,
     * GenericInfo.PROPERTY, 1);
     *
     * String splitedStrings[] = currentNode.toString().split(";");
     * matchingString = splitedStrings[0];
     *
     * for (int j = 0; j < choices.size(); j++) {
     *
     * // if (choices.get(j).toString().indexOf(matchingString) != -1) { // true
     * // returnType = false; // reason += "\t-Specfied propety list" // + "'s "
     * // + (j + 1) // + "th property includes the selected attribute\n"; // }
     *
     *
     * System.err .println(attributeType); Pattern p =
     * Pattern.compile(".*[^A-Za-z0-9_]"+matchingString+"[^A-Za-z0-9_].*");
     *
     * System.err .println(
     * "TargetPropertiesPanel.SubjectsPanelActionAdapter.IsDeletableWOimpact()+matchingString"
     * +matchingString); System.err .println(
     * "TargetPropertiesPanel.SubjectsPanelActionAdapter.IsDeletableWOimpact()"+
     * choices.get(j).toString()); Matcher m =
     * p.matcher(choices.get(j).toString()); if (m.matches()){ returnType =
     * false; reason += "\t-Specfied propety list" + "'s " + (j + 1) +
     * "th property includes the selected attribute\n"; } } }
     *
     * }
     *
     * if (returnType == false){ JOptionPane .showMessageDialog( null,
     * "Before update/delete the attribute, please delete rules (that includes the selected attribute) below.\n"
     * + reason, "Warning", JOptionPane.INFORMATION_MESSAGE); }
     *
     * return returnType; }
     *
     * }
     */
}
