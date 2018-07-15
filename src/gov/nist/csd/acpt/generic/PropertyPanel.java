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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements the (XACML) target editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class PropertyPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long         serialVersionUID           = 0;

    /***************************************************************************
     * Variables
     **************************************************************************/

    // public String selectedModelType = null;

    public String                     selectedCombineTreeType    = GenericInfo.PROPERTY;
    public DefaultMutableTreeNode     modeltreeRootNode          = null;
    public DefaultMutableTreeNode     combinetreeRooNode         = null;
    public DefaultMutableTreeNode     propertyreeRooNode         = null;

    public DefaultMutableTreeNode     targetParentNode           = null;

    public int                        attrLevel                  = 1;
    public int                        nodeLevel                  = 2;

    public JButton                    removeButton               = null;
    public JButton                    addButton                  = null;
    public JButton                    updateButton               = null;
    public JButton                    sodButton                  = null;

    public JTextField                 valueField                 = null;
    private ACPTFrame                 frame                      = null;
    private JTree                     combineTree                = null;
    private JTree                     targetTree                 = null;
    private TitledBorder              titledBorder               = null;

    public JList                      list                       = null;
    public DefaultListModel           listmodel                  = null;

    public JList                      grouplist                  = null;
    public DefaultListModel           grouplistmodel             = null;

    /* Adapters */
    public PropertyPanelActionAdapter propertyPanelActionAdapter = null;

    private GenericInfo               targetInfo                 = null;
    private boolean                   editable                   = false;
    private GenericPanel                            separationOfDutyPanel;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public PropertyPanel(ACPTFrame frame, JTree combineTree, GenericInfo targetInfo, boolean editable)
    {

        this.frame = frame;
        // this.modelTree = frame.modelPanel.getModelTree();
        this.separationOfDutyPanel = new GenericPanel(frame, GenericInfo.SOD);
        this.combineTree = combineTree;
        this.targetInfo = targetInfo;
        this.targetTree = frame.targetPanel.getTargetTree();
        setVariables();
        setAdapters();
        setPanels(targetInfo);
        setEditable(editable);

    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setVariables()
    {

        combinetreeRooNode = GenericTreeUtil.getFirstMatchingNode(this.combineTree, GenericInfo.ROOT, 0);
        propertyreeRooNode = combinetreeRooNode;

    }

    private void setAdapters()
    {

        this.propertyPanelActionAdapter = new PropertyPanelActionAdapter(this);

    }

    private void setPanels(GenericInfo targetInfo)
    {

        grouplistmodel = new DefaultListModel();
        grouplist = new JList(grouplistmodel);

        if (combineTree != null)
        {
            List choices =
                    GenericTreeUtil.getTargetTreeChildrenNode2List(combinetreeRooNode, selectedCombineTreeType, 1);

            for (int i = 0; i < choices.size(); i++)
            {
                grouplistmodel.addElement(choices.get(i));
            }
        }

        JScrollPane grouplistScroller = new JScrollPane(grouplist);
        // grouplistScroller.setAlignmentX(LEFT_ALIGNMENT);
        grouplistScroller.setPreferredSize(new Dimension(550, 250));
        grouplistScroller.setMaximumSize(new Dimension(550, 250));

        this.removeButton = new JButton("Remove");
        this.removeButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.removeButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.removeButton.addActionListener(propertyPanelActionAdapter);
        this.removeButton.setActionCommand("Remove");

        this.addButton = new JButton("Add");
        this.addButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.addButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.addButton.addActionListener(propertyPanelActionAdapter);
        this.addButton.setActionCommand("Add");

        this.updateButton = new JButton("Update");
        this.updateButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.updateButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.updateButton.addActionListener(propertyPanelActionAdapter);
        this.updateButton.setActionCommand("Update");

        this.sodButton = new JButton("Separation of Duty");
        this.sodButton.setPreferredSize(new Dimension(180, GraphicsUtil.FIELD_HEIGHT));
        this.sodButton.setMaximumSize(new Dimension(200, GraphicsUtil.FIELD_HEIGHT));
        this.sodButton.addActionListener(propertyPanelActionAdapter);
        this.sodButton.setActionCommand("SoD");

        /******************** group panel *************************/

        JPanel groupButtonPanel = new JPanel();
        groupButtonPanel.setLayout(new BoxLayout(groupButtonPanel, BoxLayout.X_AXIS));
        groupButtonPanel.add(addButton);
        groupButtonPanel.add(updateButton);
        groupButtonPanel.add(removeButton);
        groupButtonPanel.add(sodButton);

        JPanel groupPanel = new JPanel();
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder("List of Specified Security Requirements");
        groupPanel.setBorder(titledBorder2);
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        groupPanel.add(grouplistScroller);
        groupPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        groupPanel.add(groupButtonPanel);

        /********************
         * attribute properties panel
         *************************/

        titledBorder = BorderFactory.createTitledBorder(targetInfo.getValue() + "");
        this.setBorder(titledBorder);
        this.setLayout(new BorderLayout());
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createRigidArea(new Dimension(100, GraphicsUtil.FIELD_HEIGHT)));
        this.add(groupPanel);
        this.add(Box.createRigidArea(new Dimension(100, GraphicsUtil.FIELD_HEIGHT)));

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

    public void addChildNode(String type, JList selectedlist, DefaultListModel selectedlistmodel)
    {

        PropertyDialogForAttributeSelection myAttributeSelectionDialog = new PropertyDialogForAttributeSelection(frame,
                true, selectedCombineTreeType, targetTree, "", "Add", null);

        List<String> NodeLst = new ArrayList<String>();

        if (myAttributeSelectionDialog.getAnswer())
        {

            List<DefaultMutableTreeNode> nLst = myAttributeSelectionDialog.getSelectedNodeLst();

            for (int i = 0; i < nLst.size(); i++)
            {
                String valueOfvalue = ((TargetInfo) nLst.get(i).getUserObject()).getvalueOfvalue();
                NodeLst.add(valueOfvalue);
            }

            // System.err.println(myAttributeSelectionDialog.getSelectedNodeLst().toString());
            // String[] NodeLst = null;

            // myAttributeSelectionDialog.dispose();
        }
        if (myAttributeSelectionDialog.getAnswer())
        {
            PropertyDialog myDialog =
                    new PropertyDialog(frame, true, selectedCombineTreeType, targetTree, "", "Add", null, NodeLst);

            // PropertyDialog myDialog = new PropertyDialog(frame, true,
            // selectedCombineTreeType , targetTree , "", "Add", null);

            // String ruleValue = myDialog.getcheckCompLst().toString();

            if (myDialog.getAnswer())
            {

                String TargetAttrValue = myDialog.getMyText() + "   " + myDialog.getMySelectedAttributes();
                // System.err.println(TargetAttrValue);
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new GenericInfo(type, TargetAttrValue));

                // duplicate check
                for (int i = 0; i < selectedlistmodel.size(); i++)
                {
                    String iterString = ((DefaultMutableTreeNode) selectedlistmodel.get(i)).toString();
                    if (iterString.equals(newNode.toString()))
                    {
                        JOptionPane.showMessageDialog(null, "cannot add: duplicate name", "Warning",
                                JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }

                selectedlistmodel.addElement(newNode);
                selectedlist.setSelectedIndex(selectedlist.getLastVisibleIndex());
                propertyreeRooNode.add(newNode);
                ((DefaultTreeModel) combineTree.getModel()).reload(propertyreeRooNode);

            }
            myDialog.dispose();
        }

    }

    public void updateChildNode(String type, JList selectedlist, DefaultListModel selectedlistmodel)
    {

        if (selectedlist.getSelectedValue() == null)
        {
            JOptionPane.showMessageDialog(null, "Select property first before updating", "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<String> NodeLst = new ArrayList<String>();
        String selectedPropertyAndSelectedAttributes = selectedlist.getSelectedValue().toString();
        String selectedProperty = null;
        int indexS = selectedPropertyAndSelectedAttributes.indexOf('[');
        int indexE = selectedPropertyAndSelectedAttributes.indexOf(']');

        if (indexS > 0)
        {
            selectedProperty = selectedPropertyAndSelectedAttributes.substring(0, indexS).trim();
            String NodeLstStr = selectedPropertyAndSelectedAttributes.substring(indexS + 1, indexE);
            String[] NodeLstSplits = NodeLstStr.split(",");
            for (int i = 0; i < NodeLstSplits.length; i++)
            {
                NodeLst.add(NodeLstSplits[i].trim());
            }

            // System.err.println(selectedProperty);
            // System.err.println(NodeLstStr);
        }
        else
        {
            selectedProperty = selectedPropertyAndSelectedAttributes.trim();
        }

        int selectedIndex = selectedlist.getSelectedIndex();

        PropertyDialog myDialog = new PropertyDialog(frame, true, selectedCombineTreeType, targetTree, selectedProperty,
                "Update", null, NodeLst);

        if (myDialog.getAnswer())
        {

            String TargetAttrValue = myDialog.getMyText() + "   " + myDialog.getMySelectedAttributes();

            // duplicate check
            for (int i = 0; i < selectedlistmodel.size(); i++)
            {
                String iterString = ((DefaultMutableTreeNode) selectedlistmodel.get(i)).toString();
                if ((selectedIndex != i) && iterString.equals(TargetAttrValue))
                {
                    JOptionPane.showMessageDialog(null, "cannot add: duplicate properties", "Warning",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            }

            DefaultMutableTreeNode Node = ((DefaultMutableTreeNode) selectedlistmodel.get(selectedIndex));
            Node.setUserObject(new GenericInfo(type, TargetAttrValue));
            selectedlist.setSelectedIndex(selectedIndex);

            // refresh or repaint
            this.repaint();
            ((DefaultTreeModel) combineTree.getModel()).reload(propertyreeRooNode);
        }

        myDialog.dispose();

    }

    public void deleteChildNode(String type, JList selectedlist, DefaultListModel selectedlistmodel)
    {

        String childTargetName = (String) selectedlist.getSelectedValue();

        if (childTargetName == null)
        {

            return;

        }
        else
        {

            GenericTreeUtil.removeChildNode(type, childTargetName, combineTree, targetParentNode);
            selectedlistmodel.removeElement(childTargetName);
            // list.remove(list.getSelectedIndex());
        }
    }

    /*
     * public void removeNode() {
     *
     * TargetTreeUtil.removeNode(combineTree);
     *
     * }
     */
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
    class PropertyPanelActionAdapter implements ActionListener
    {

        PropertyPanel propertyPanel = null;

        PropertyPanelActionAdapter(PropertyPanel propertyPanel)
        {

            this.propertyPanel = propertyPanel;

        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getActionCommand().equals("Add"))
            {

                addChildNode(selectedCombineTreeType, grouplist, grouplistmodel);

            }
            else if (e.getActionCommand().equals("Update"))
            {
                updateChildNode(selectedCombineTreeType, grouplist, grouplistmodel);
            }
            else if (e.getActionCommand().equals("Remove"))
            {

                System.out.println("Got Remove Button");
                if (grouplist.getSelectedIndex() == -1)
                {
                    // Warning
                    GraphicsUtil.showWarningDialog("No rule is selected.", "Deletion Warning", null);
                    return;
                }

                if (grouplist.getSelectedIndex() >= 0)
                {

                    // remove from the grouplist
                    DefaultMutableTreeNode currentNode =
                            (DefaultMutableTreeNode) grouplistmodel.get(grouplist.getSelectedIndex());
                    grouplistmodel.removeElement(currentNode);
                    if (grouplistmodel.capacity() > 0)
                    {
                        grouplist.setSelectedIndex(0);
                    }

                    // remove from the tree
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());
                    currentNode.removeAllChildren();
                    parentNode.remove(currentNode);
                    ((DefaultTreeModel) combineTree.getModel()).reload(parentNode);
                }

            }
            else if(e.getActionCommand().equals("SoD")){
//              propertyPanel.frame.sodmoveToBack();
                SeparationOfDutyFrame separationOfDutyFrame = new SeparationOfDutyFrame(frame, separationOfDutyPanel);
                separationOfDutyFrame.setVisible(true);


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
