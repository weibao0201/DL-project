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

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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

public class CombinationsPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long             serialVersionUID               = 0;

    /***************************************************************************
     * Variables
     **************************************************************************/

    // public String selectedModelType = null;
    public String                         selectedCombineTreeType        = GenericInfo.MODELCOMBINE;
    public DefaultMutableTreeNode         modeltreeRootNode              = null;
    public DefaultMutableTreeNode         combinetreeRooNode             = null;
    public DefaultMutableTreeNode         targetParentNode               = null;

    public int                            attrLevel                      = 1;
    public int                            nodeLevel                      = 2;

    public JButton                        updateValueButton              = null;

    public JButton                        addAttributeButton             = null;
    public JButton                        updateAttributeButton          = null;
    public JButton                        deleteAttributeButton          = null;
    public JButton                        addGroupButton                 = null;
    public JButton                        updateGroupButton              = null;
    public JButton                        deleteGroupButton              = null;

    public JComboBox                      ComboBox                       = null;
    // public JButton updateAlgButton = null;

    public JButton                        selectButton                   = null;
    public JButton                        deselectButton                 = null;
    public JButton                        upButton                       = null;
    public JButton                        downButton                     = null;

    public JTextField                     valueField                     = null;
    private ACPTFrame                     frame                          = null;
    private JTree                         modelTree                      = null;
    private JTree                         combineTree                    = null;
    private GenericInfo                   targetInfo                     = null;
    private boolean                       editable                       = false;
    private TitledBorder                  titledBorder                   = null;

    public JList                          list                           = null;
    public DefaultListModel               listmodel                      = null;

    public JList                          grouplist                      = null;
    public DefaultListModel               grouplistmodel                 = null;

    /* Adapters */
    public CombinationsPanelActionAdapter combinationsPanelActionAdapter = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public CombinationsPanel(ACPTFrame frame, JTree combineTree, GenericInfo targetInfo, boolean editable)
    {

        this.frame = frame;
        this.modelTree = frame.modelPanel.getModelTree();
        this.combineTree = combineTree;
        this.targetInfo = targetInfo;
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

        modeltreeRootNode = ModelTreeUtil.getFirstMatchingNode(this.modelTree, ModelInfo.ROOT, 0);
        combinetreeRooNode = GenericTreeUtil.getFirstMatchingNode(this.combineTree, GenericInfo.ROOT, 0);

    }

    private void setAdapters()
    {

        this.combinationsPanelActionAdapter = new CombinationsPanelActionAdapter(this);

    }

    private void setPanels(GenericInfo targetInfo)
    {

        listmodel = new DefaultListModel();
        list = new JList(listmodel);

        if (modeltreeRootNode != null)
        {

            String[] selectedType =
                    {
                            ModelInfo.RBAC, ModelInfo.ABAC, ModelInfo.MULTILEVEL, ModelInfo.WORKFLOW
            };

            for (int i = 0; i < selectedType.length; i++)
            {
                String[] choices =
                        ModelTreeUtil.getTargetTreeChildrenValues2StrArr(modeltreeRootNode, selectedType[i], nodeLevel);
                for (int j = 0; j < choices.length; j++)
                {
                    listmodel.addElement(selectedType[i] + "#" + choices[j]);
                }
            }

            if (listmodel.capacity() > 0)
            {
                list.setSelectedIndex(0);
            }
        }

        JScrollPane listScroller = new JScrollPane(list);
        // listScroller.setAlignmentX(LEFT_ALIGNMENT);
        listScroller.setPreferredSize(new Dimension(150, 250));
        listScroller.setMaximumSize(new Dimension(150, 250));

        grouplistmodel = new DefaultListModel();
        grouplist = new JList(grouplistmodel);

        if (combineTree != null)
        {
            String[] choices =
                    GenericTreeUtil.getTargetTreeChildrenValues2StrArr(combinetreeRooNode, selectedCombineTreeType, 1);
            // String[] choices = {"a", "b", "c1"};
            for (int i = 0; i < choices.length; i++)
            {
                grouplistmodel.addElement(choices[i]);
            }
            if (grouplistmodel.capacity() > 0)
            {
                grouplist.setSelectedIndex(0);
            }
        }

        JScrollPane grouplistScroller = new JScrollPane(grouplist);
        // grouplistScroller.setAlignmentX(LEFT_ALIGNMENT);
        grouplistScroller.setPreferredSize(new Dimension(150, 200));
        grouplistScroller.setMaximumSize(new Dimension(150, 200));

        this.selectButton = new JButton("Select-->");
        this.selectButton.setPreferredSize(new Dimension(100, GraphicsUtil.FIELD_HEIGHT));
        this.selectButton.setMaximumSize(new Dimension(100, GraphicsUtil.FIELD_HEIGHT));
        this.selectButton.addActionListener(combinationsPanelActionAdapter);
        this.selectButton.setActionCommand("Select");

        this.deselectButton = new JButton("Remove");
        this.deselectButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.deselectButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.deselectButton.addActionListener(combinationsPanelActionAdapter);
        this.deselectButton.setActionCommand("Remove");

        this.upButton = new JButton("Up");
        this.upButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.upButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.upButton.addActionListener(combinationsPanelActionAdapter);
        this.upButton.setActionCommand("Up");

        this.downButton = new JButton("Down");
        this.downButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.downButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.downButton.addActionListener(combinationsPanelActionAdapter);
        this.downButton.setActionCommand("Down");

        /******************** attributePanel panel *************************/

        JPanel attributePanel = new JPanel();
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder("Policies");
        attributePanel.setBorder(titledBorder1);
        attributePanel.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));
        attributePanel.add(listScroller);
        attributePanel.add(Box.createRigidArea(new Dimension(250, GraphicsUtil.FIELD_HEIGHT)));

        /********************
         * Policy Combination Panel
         *************************/

        JLabel algLabel = new JLabel("Algorithm: ", SwingConstants.LEFT);
        algLabel.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        algLabel.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        this.ComboBox = new JComboBox(GenericInfo.PolicyCombinationAlgs);
        this.ComboBox.setActionCommand("CombinationComboBox");
        this.ComboBox.addActionListener(combinationsPanelActionAdapter);
        this.ComboBox.setMinimumSize(new Dimension(120, GraphicsUtil.FIELD_HEIGHT));
        this.ComboBox.setPreferredSize(new Dimension(120, GraphicsUtil.FIELD_HEIGHT));
        this.ComboBox.setMaximumSize(new Dimension(120, GraphicsUtil.FIELD_HEIGHT));

        // this.updateAlgButton = new JButton("Update");
        // this.updateAlgButton.setPreferredSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // this.updateAlgButton.setMaximumSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        //
        // this.updateAlgButton.addActionListener(combinationsPanelActionAdapter);
        // this.updateAlgButton.setActionCommand("Update Combination");

        JPanel PolComPanel = new JPanel();
        titledBorder = BorderFactory.createTitledBorder("Policy Combination Algorithm");
        PolComPanel.setBorder(titledBorder);

        // PolComPanel.setLayout(new BoxLayout(PolComPanel, BoxLayout.X_AXIS));
        //
        // PolComPanel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
        //
        // PolComPanel.add(algLabel);
        // PolComPanel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));

        PolComPanel.add(ComboBox);
        // PolComPanel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));

        // PolComPanel.add(updateAlgButton);
        // PolComPanel.add(Box.createRigidArea(new
        // Dimension(80+15,GraphicsUtil.FIELD_HEIGHT)));

        // find policy combination
        String CombinationType = ((GenericInfo) combinetreeRooNode.getUserObject()).getCombiningAlgorithm();
        ComboBox.setSelectedItem(CombinationType);

        /******************** group panel *************************/

        JPanel groupButtonPanel = new JPanel();
        groupButtonPanel.setLayout(new BoxLayout(groupButtonPanel, BoxLayout.X_AXIS));
        groupButtonPanel.add(deselectButton);
        groupButtonPanel.add(upButton);
        groupButtonPanel.add(downButton);

        JPanel groupPanel = new JPanel();
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder("Selected Policies");
        groupPanel.setBorder(titledBorder2);
        groupPanel.setLayout(new BoxLayout(groupPanel, BoxLayout.Y_AXIS));
        // groupPanel.add(PolComPanel);
        groupPanel.add(grouplistScroller);
        groupPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        groupPanel.add(groupButtonPanel);

        /******************** group panel *************************/

        JPanel CombinationTargetPanel = new JPanel();
        CombinationTargetPanel.setLayout(new BoxLayout(CombinationTargetPanel, BoxLayout.Y_AXIS));
        CombinationTargetPanel.add(PolComPanel);
        CombinationTargetPanel.add(groupPanel);

        /********************
         * attribute properties panel
         *************************/

        titledBorder = BorderFactory.createTitledBorder(targetInfo.getValue() + "");
        this.setBorder(titledBorder);
        this.setLayout(new BorderLayout());
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(Box.createRigidArea(new Dimension(50, GraphicsUtil.FIELD_HEIGHT)));
        this.add(attributePanel);
        this.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));
        this.add(selectButton);
        this.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));
        this.add(CombinationTargetPanel);
        this.add(Box.createRigidArea(new Dimension(50, GraphicsUtil.FIELD_HEIGHT)));

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

        String childTargetName = (String) list.getSelectedValue();

        if (childTargetName == null)
        {

            return;

        }
        else
        {

            boolean isAdded = GenericTreeUtil.addChildNode(type, childTargetName, combineTree, targetParentNode);
            if (isAdded)
            {
                selectedlistmodel.addElement(childTargetName);
                selectedlist.setSelectedIndex(selectedlist.getLastVisibleIndex());
            }
        }

    }

    public void upChildNode(String type, JList selectedlist, DefaultListModel selectedlistmodel)
    {

        String childTargetName = (String) selectedlist.getSelectedValue();
        int childTargetIndex = selectedlist.getSelectedIndex();

        if (childTargetName == null)
        {

            return;

        }
        else
        {

            if (childTargetIndex > 0)
            {
                boolean isSwapped = GenericTreeUtil.swapChildNode(childTargetIndex, childTargetIndex - 1, combineTree,
                        targetParentNode);
                if (isSwapped)
                {
                    String temp = (String) selectedlistmodel.remove(childTargetIndex);
                    selectedlistmodel.add(childTargetIndex - 1, temp);
                    selectedlist.setSelectedIndex(childTargetIndex - 1);
                }
            }
        }

    }

    public void downChildNode(String type, JList selectedlist, DefaultListModel selectedlistmodel)
    {

        String childTargetName = (String) selectedlist.getSelectedValue();
        int childTargetIndex = selectedlist.getSelectedIndex();

        if (childTargetName == null)
        {

            return;

        }
        else
        {

            if (childTargetIndex < (selectedlistmodel.size() - 1))
            {
                boolean isSwapped = GenericTreeUtil.swapChildNode(childTargetIndex, childTargetIndex + 1, combineTree,
                        targetParentNode);
                if (isSwapped)
                {
                    String temp = (String) selectedlistmodel.remove(childTargetIndex);
                    selectedlistmodel.add(childTargetIndex + 1, temp);
                    selectedlist.setSelectedIndex(childTargetIndex + 1);
                }
            }
        }
    }

    /*
     * public void updateChildNode(String type, JList list, DefaultListModel
     * selectedlistmodel) {
     *
     * String oldTargetName = (String) list.getSelectedValue();
     *
     * if (oldTargetName == null) {
     *
     * return;
     *
     * } else {
     *
     * String newTargetName = GraphicsUtil.showTextFieldInputDialog( " name:\n",
     * "New ", "Name cannot be empty string", "Name error", oldTargetName,
     * frame);
     *
     * if (newTargetName == null) {return;};
     *
     * TargetTreeUtil.updateChildNode(type, oldTargetName, newTargetName,
     * combineTree, targetParentNode);
     *
     * int selectedindex = list.getSelectedIndex();
     *
     * selectedlistmodel.removeElement(oldTargetName);
     * selectedlistmodel.add(selectedindex, newTargetName);
     *
     * }
     *
     * }
     */

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
    class CombinationsPanelActionAdapter implements ActionListener
    {

        CombinationsPanel subjectsPanel = null;

        CombinationsPanelActionAdapter(CombinationsPanel subjectsPanel)
        {

            this.subjectsPanel = subjectsPanel;

        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getActionCommand().equals("Select"))
            {

                targetParentNode = combinetreeRooNode;
                addChildNode(selectedCombineTreeType, grouplist, grouplistmodel);

            }
            else if (e.getActionCommand().equals("Up"))
            {

                targetParentNode = combinetreeRooNode;
                upChildNode(selectedCombineTreeType, grouplist, grouplistmodel);

            }
            else if (e.getActionCommand().equals("Remove"))
            {

                targetParentNode = combinetreeRooNode;
                deleteChildNode(selectedCombineTreeType, grouplist, grouplistmodel);

            }
            else if (e.getActionCommand().equals("Down"))
            {

                targetParentNode = combinetreeRooNode;
                downChildNode(selectedCombineTreeType, grouplist, grouplistmodel);

            }
            else if (e.getActionCommand().equals("CombinationComboBox"))
            {

                combinetreeRooNode.setUserObject(
                        new GenericInfo(GenericInfo.ROOT, "Policies: " + ComboBox.getSelectedItem().toString()));
                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (combinetreeRooNode.getParent());
                ((DefaultTreeModel) combineTree.getModel()).reload(parentNode);

            }
            // else if (e.getActionCommand().equals("Update Combination")) {
            //
            // combinetreeRooNode.setUserObject(new
            // GenericInfo(GenericInfo.ROOT, "Policies:
            // "+ComboBox.getSelectedItem().toString()));
            // DefaultMutableTreeNode parentNode =
            // (DefaultMutableTreeNode)(combinetreeRooNode.getParent());
            // ((DefaultTreeModel)combineTree.getModel()).reload(parentNode);
            //
            // }
            else
            {

                // System.err.println("GroupPanel warning: command not
                // implemented: " +
                // e.getActionCommand());
            }
            frame.refreshSubPanelsWhenCombinationChanges();

        }

    }

    public JTree getGenericTree()
    {
        // TODO Auto-generated method stub
        return combineTree;
    }

}
