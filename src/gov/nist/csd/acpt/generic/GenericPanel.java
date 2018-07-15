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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements the (XACML) model editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class GenericPanel extends JPanel
{

    /**
     * This inner class implements the model panel mouse adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class GenericPanelActionAdapter implements ActionListener
    {

        GenericPanel genericPanel = null;

        GenericPanelActionAdapter(final GenericPanel genericPanel)
        {

            this.genericPanel = genericPanel;

        }

        @Override
        public void actionPerformed(final ActionEvent e)
        {

            if (e.getActionCommand().equals("Add Child Node"))
            {

                addChildNode();

            }
            else if (e.getActionCommand().equals("Remove Node"))
            {

                removeNode();

            }
            else if (e.getActionCommand().equals("Remove Children Nodes"))
            {

                removeAllChildren();

            }
            else
            {

                // System.err.println("GenericPanel warning: command not
                // implemented: "
                // + e.getActionCommand());
            }

        }

    }

    /**
     * This inner class implements the model panel mouse adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class GenericPanelMouseAdapter extends MouseAdapter
    {

        GenericPanel genericPanel = null;

        GenericPanelMouseAdapter(final GenericPanel genericPanel)
        {

            this.genericPanel = genericPanel;

        }

        @Override
        public void mouseClicked(final MouseEvent e)
        {

            if (e.getSource() == navigatorPanel)
            {

                if (e.isPopupTrigger())
                {
                    // popup.show(e.getComponent(),
                    // e.getX(), e.getY());
                }

            }
        }

        /**
         * This method is primarily used for right-click selection of nodes.
         *
         * @param e
         *            The mouse event.
         */
        @Override
        public void mousePressed(final MouseEvent e)
        {

            if (SwingUtilities.isRightMouseButton(e))
            {

                final int row = genericTree.getClosestRowForLocation(e.getX(), e.getY());
                genericTree.setSelectionRow(row);

            }

        }

        /**
         * This method is primarily used for invoking a popup menu on the tree.
         */
        @Override
        public void mouseReleased(final MouseEvent e)
        {

            if (e.getSource() == genericTree)
            {

                if (e.isPopupTrigger())
                {

                    final DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode) genericPanel.genericTree.getLastSelectedPathComponent();

                    if (node == null)
                    {
                        // Nothing is selected.
                        return;
                    }

                    final Object nodeInfo = node.getUserObject();
                    final GenericInfo genericInfo = (GenericInfo) nodeInfo;
                    final String genericType = genericInfo.getType();
                    final String genericValue = genericInfo.getValue();
                    System.out.println("GenericPanelTreeSelectionAdapter.valueChanged()-USERS root");

                    if (genericType.equals(GenericInfo.ROOT))
                    {

                        System.out.println("GenericPanelMouseAdapter.mouseReleased() root");
                        setEnableComponents(false, false, false);

                    }
                    else
                    {

                        // The following is changing...
                        System.out.println("GenericPanelMouseAdapter.mouseReleased() else");

                        /*
                         * if ((genericType.equals(GenericInfo.RBAC)) ||
                         * (genericType.equals(GenericInfo.MULTILEVEL)) ||
                         * (genericType.equals(GenericInfo.WORKFLOW)) ||
                         * (genericType.equals(GenericInfo.ABAC))) {
                         *
                         * if (node.getLevel() ==
                         * GenericInfo.NodeLevelofmodelroot) { if
                         * (node.getChildCount() > 0) {
                         *
                         * setEnableComponents(true, false, true);
                         *
                         * } else {
                         *
                         * setEnableComponents(true, false, false);
                         *
                         * } } else if (node.getLevel() ==
                         * GenericInfo.NodeLevelofmodel) { if
                         * (node.getChildCount() > 0) {
                         * setEnableComponents(false, true, true); } else {
                         * setEnableComponents(false, true, false); } }
                         *
                         *
                         * } else {
                         *
                         * if (node.getChildCount() > 0) {
                         *
                         * setEnableComponents(true, true, true);
                         *
                         * } else {
                         *
                         * setEnableComponents(true, true, false);
                         *
                         * } if (node.getLevel() ==
                         * GenericInfo.NodeLevelofruleroot) {
                         *
                         * if (node.getChildCount() > 0) {
                         * setEnableComponents(false, true, true); } else {
                         * setEnableComponents(false, true, false); } } else if
                         * (node.getLevel() == GenericInfo.NodeLevelofrule) {
                         * setEnableComponents(false, true, false); } }
                         *
                         */
                        groupPopupMenu.show(e.getComponent(), e.getX(), e.getY());

                    }

                    genericPanel.horizontalSplitPane.setRightComponent(genericPropertiesPanel);
                    genericPanel.horizontalSplitPane.setDividerLocation(400);

                }
            }
        }

    }

    /**
     * This inner class implements the model panel mouse adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class GenericPanelTreeSelectionAdapter implements TreeSelectionListener
    {

        GenericPanel genericPanel = null;

        GenericPanelTreeSelectionAdapter(final GenericPanel genericPanel)
        {

            this.genericPanel = genericPanel;

        }

        /**
         * This method traps events when a user changes the selected node on the
         * tree. This method will immediately display the information associated
         * with the newly selected node.
         *
         * @param e
         *            The tree selection event.
         */
        @Override
        public void valueChanged(final TreeSelectionEvent e)
        {

            final DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) genericPanel.genericTree.getLastSelectedPathComponent();

            if (node == null)
            {
                // Nothing is selected.
                return;
            }

            final Object nodeInfo = node.getUserObject();
            final GenericInfo genericInfo = (GenericInfo) nodeInfo;
            final String modelType = genericInfo.getType();
            final String modelValue = genericInfo.getValue();
            boolean editable = false;

            if (modelType.equals(GenericInfo.ROOT))
            {

                setEnableComponents(false, false, false);
                editable = false;

            }
            else
            {

                if (modelType.equals(GenericInfo.ROOT))
                {

                    if (node.getLevel() == GenericInfo.NodeLevelofmodelroot)
                    { // USERS root

                        /*
                         * System.out .println(
                         * "GenericPanelTreeSelectionAdapter.valueChanged()-USERS root"
                         * ); genericPropertiesPanel = null; System.gc();
                         * genericPropertiesPanel = new
                         * GenericPropertiesPanel(frame, genericTree,
                         * genericInfo, editable);
                         *
                         * genericPanel.horizontalSplitPane
                         * .setRightComponent(genericPropertiesPanel);
                         * genericPanel.horizontalSplitPane.setDividerLocation(
                         * 400);
                         *
                         */

                        if (node.getChildCount() > 0)
                        {
                            setEnableComponents(true, false, true);
                        }
                        else
                        {
                            setEnableComponents(true, false, false);
                        }

                    }
                    else if (node.getLevel() == GenericInfo.NodeLevelofmodel)
                    { // USERS
                      // instances
                      // System.out
                      // .println("GenericPanelTreeSelectionAdapter.valueChanged()-USERS
                      // instance");
                        /*
                         * rbacPanel = null; System.gc(); rbacPanel = new
                         * RBACPanel(frame, genericTree, genericInfo, editable);
                         *
                         * genericPanel.horizontalSplitPane
                         * .setRightComponent(rbacPanel);
                         * genericPanel.horizontalSplitPane.setDividerLocation(
                         * 400);
                         *
                         * setEnableComponents(false, true, false);
                         */
                    }
                }

                else
                {

                    editable = true;

                    if (node.getChildCount() > 0)
                    {

                        setEnableComponents(true, true, true);

                    }
                    else
                    {

                        setEnableComponents(true, true, false);

                    }

                }
            }

        }

    }

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long               serialVersionUID                 = 0;
    /***************************************************************************
     * Variables
     **************************************************************************/

    private String                          genericType                      = null;
    private JPanel                          navigatorPanel                   = null;

    private JTree                           genericTree                      = null;
    private JTree                           modelTree                        = null;
    private JTree                           targetTree                       = null;
    private DefaultTreeModel                genericTreeModel                 = null;
    private JScrollPane                     genericTreeScrollPane            = null;
    private final GenericPropertiesPanel    genericPropertiesPanel           = null;
    /*
     * private RBACPanel rbacPanel = null; private RBACRulePanel rbacRulePanel =
     * null; private ABACPanel abacPanel = null; private ABACRulePanel
     * abacRulePanel = null; private WORKFLOWPanel workflowPanel = null; private
     * WORKFLOWRulePanel workflowRulePanel = null; private MULTILEVELPanel
     * multilevelPanel = null; private MULTILEVELRulePanel multilevelRulePanel =
     * null; private MULTILEVELSubjectLevelsPanel multilevelSubjectLevelsPanel =
     * null; private MULTILEVELResourceLevelsPanel multilevelResourceLevelsPanel
     * = null;
     */
    private JMenuItem                       addChildMenuItem                 = null;

    private JMenuItem                       deleteMenuItem                   = null;
    private JMenuItem                       deleteChildrenMenuItem           = null;
    private JPopupMenu                      groupPopupMenu                   = null;

    private final JSplitPane                horizontalSplitPane              =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private final File                      modelInputFile                   = null;
    private ACPTFrame                       frame                            = null;

    /* Adapters */
    public GenericPanelActionAdapter        genericPanelActionAdapter        = null;

    public GenericPanelMouseAdapter         genericPanelMouseAdapter         = null;

    public GenericPanelTreeSelectionAdapter genericPanelTreeSelectionAdapter = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/
    /*
     * public GenericPanel(ACPTFrame frame) { this.frame = frame;
     * this.targetTree = frame.targetPanel.getTargetTree(); this.modelTree =
     * frame.modelPanel.getModelTree(); setAdapters(); setPopupMenus();
     * setPanels(); setTree(); }
     */

    public GenericPanel(final ACPTFrame frame, final String genericType)
    {
        this.frame = frame;
        this.genericType = genericType;
        targetTree = frame.targetPanel.getTargetTree();
        modelTree = frame.modelPanel.getModelTree();

        setAdapters();
        setPopupMenus();
        setPanels();
        setTree();
    }

    public void addChildNode()
    {

        final GenericInfo genericInfo = GenericTreeUtil.getCurrentModel(genericTree);
        final String modelValue = genericInfo.getValue();

        final String childModelName = GraphicsUtil.showTextFieldInputDialog(modelValue + " name:\n",
                "New " + modelValue, "Name cannot be empty string", "Name error", "", frame);

        if (childModelName == null)
        {

            return;

        }
        else
        {

            GenericTreeUtil.addNode(genericInfo.getType(), childModelName, genericTree);

        }

    }

    public void createModelTree()
    {

        /* Set model tree */
        genericTreeModel = GenericTreeUtil.createNewModelTreeModel(genericType);
        genericTree.setModel(genericTreeModel);
        /*
         * genericTreeScrollPane = null; genericTreeScrollPane = new
         * JScrollPane(this.genericTree);
         *
         * navigatorPanel.removeAll(); navigatorPanel.add(genericTreeScrollPane,
         * BorderLayout.CENTER); navigatorPanel.validate();
         * navigatorPanel.setVisible(true);
         */
        int row = 0;
        while (row < genericTree.getRowCount())
        {
            genericTree.expandRow(row);
            row++;
        }

    }

    public void createSampleProject(final String type)
    {

        genericTreeModel = GenericTreeUtil.createSampleModelTreeModel(genericType);

        genericTree.setModel(genericTreeModel);

        genericTreeScrollPane = null;
        genericTreeScrollPane = new JScrollPane(genericTree);
        /*
         * navigatorPanel.removeAll(); navigatorPanel.add(genericTreeScrollPane,
         * BorderLayout.CENTER); navigatorPanel.validate();
         * navigatorPanel.setVisible(true);
         */
        int row = 0;
        while (row < genericTree.getRowCount())
        {
            genericTree.expandRow(row);
            row++;
        }

    }

    public void createSavedProject(final String type, final DefaultMutableTreeNode root)
    {

        genericTreeModel = new DefaultTreeModel(root);

        genericTree.setModel(genericTreeModel);

        genericTreeScrollPane = null;
        genericTreeScrollPane = new JScrollPane(genericTree);
        /*
         * navigatorPanel.removeAll(); navigatorPanel.add(genericTreeScrollPane,
         * BorderLayout.CENTER); navigatorPanel.validate();
         * navigatorPanel.setVisible(true);
         */
        int row = 0;
        while (row < genericTree.getRowCount())
        {
            genericTree.expandRow(row);
            row++;
        }
    }

    public JTree getGenericTree()
    {
        return genericTree;
    }

    public void removeAllChildren()
    {

        GenericTreeUtil.removeAllChildren(genericTree);

    }

    public void removeNode()
    {

        GenericTreeUtil.removeNode(genericTree);

    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setAdapters()
    {

        genericPanelActionAdapter = new GenericPanelActionAdapter(this);
        genericPanelMouseAdapter = new GenericPanelMouseAdapter(this);
        genericPanelTreeSelectionAdapter = new GenericPanelTreeSelectionAdapter(this);

    }

    /***************************************************************************
     * Public methods
     **************************************************************************/

    public void setEnableComponents(final boolean addChildMenuItemBoolean, final boolean deleteMenuItemBoolean,
            final boolean deleteChildrenMenuItemBoolean)
    {

        /******************** Menus *************************/

        addChildMenuItem.setEnabled(addChildMenuItemBoolean);
        frame.addChildMenuItem.setEnabled(addChildMenuItemBoolean);

        deleteMenuItem.setEnabled(deleteMenuItemBoolean);
        frame.deleteMenuItem.setEnabled(deleteMenuItemBoolean);

        deleteChildrenMenuItem.setEnabled(deleteChildrenMenuItemBoolean);
        frame.deleteChildrenMenuItem.setEnabled(deleteChildrenMenuItemBoolean);

        /******************** Panels *************************/

    }

    private void setPanels()
    {

        /* Navigator panel */
        navigatorPanel = new JPanel();
        navigatorPanel.setLayout(new BorderLayout());
        final TitledBorder titledBorder = BorderFactory.createTitledBorder("Navigator");
        navigatorPanel.setBorder(titledBorder);

        /* Config panel */
        // JPanel propertiesPanel = new JPanel();

        final JPanel propertiesPanel = null;

        // by Type

        /* Content pane */
        /*
         * this.setLayout(new BorderLayout()); this.add(navigatorPanel,
         * BorderLayout.WEST);
         */

        // propertiesPanel.setLayout(new BorderLayout());
        // titledBorder = BorderFactory.createTitledBorder("Properties");
        // propertiesPanel.setBorder(titledBorder);

        /* Horizontal panel */
        /*
         * this.horizontalSplitPane = new
         * JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigatorPanel,
         * propertiesPanel);
         * this.horizontalSplitPane.setOneTouchExpandable(true);
         * this.horizontalSplitPane.setDividerLocation(400);
         */

    }

    /***************************************************************************
     * Inner classes
     **************************************************************************/

    /**
     * Create ACPT menus and menu items.
     */
    private void setPopupMenus()
    {

        /******************** Group popup menu *************************/

        groupPopupMenu = new JPopupMenu();

        addChildMenuItem = new JMenuItem("Add");
        addChildMenuItem.addActionListener(genericPanelActionAdapter);
        addChildMenuItem.setActionCommand("Add Child Node");

        deleteMenuItem = new JMenuItem("Remove");
        deleteMenuItem.addActionListener(genericPanelActionAdapter);
        deleteMenuItem.setActionCommand("Remove Node");

        deleteChildrenMenuItem = new JMenuItem("Remove Contents");
        deleteChildrenMenuItem.addActionListener(genericPanelActionAdapter);
        deleteChildrenMenuItem.setActionCommand("Remove Children Nodes");

        groupPopupMenu.add(addChildMenuItem);
        groupPopupMenu.addSeparator();
        groupPopupMenu.add(deleteMenuItem);
        groupPopupMenu.addSeparator();
        groupPopupMenu.add(deleteChildrenMenuItem);

    }

    /*
     * public void createSampleProject() {
     *
     *
     * this.genericTreeModel = ModelTreeUtil .createSampleModelTreeModel();
     *
     * this.genericTree.setModel(this.genericTreeModel);
     *
     * genericTreeScrollPane = null; genericTreeScrollPane = new
     * JScrollPane(this.modelTree);
     *
     * navigatorPanel.removeAll(); navigatorPanel.add(genericTreeScrollPane,
     * BorderLayout.CENTER); navigatorPanel.validate();
     * navigatorPanel.setVisible(true);
     *
     * int row = 0; while (row < modelTree.getRowCount()) {
     * modelTree.expandRow(row); row++; }
     *
     *
     * }
     */
    public void setSubPanels(final String SubPanelType)
    {

        final boolean editable = false;

        removeAll();

        if (SubPanelType.equals(GenericInfo.MODELCOMBINE))
        {
            final CombinationsPanel subPanel = new CombinationsPanel(frame, genericTree,
                    new GenericInfo(GenericInfo.MODELCOMBINE, GenericInfo.MODELCOMBINE), editable);

            this.add(subPanel, BorderLayout.CENTER);
        }
        else if (SubPanelType.equals(GenericInfo.TEST))
        {
            final TestPanel subPanel =
                    new TestPanel(frame, genericTree, new GenericInfo(GenericInfo.TEST, GenericInfo.TEST), editable);
            this.add(subPanel, BorderLayout.CENTER);
        }
        else if (SubPanelType.equals(GenericInfo.XACML))
        {
            final XACMLPanel subPanel =
                    new XACMLPanel(frame, genericTree, new GenericInfo(GenericInfo.XACML, GenericInfo.XACML), editable);
            this.add(subPanel, BorderLayout.CENTER);
        }
        else if (SubPanelType.equals(GenericInfo.NUSMV))
        {
            final NUSMVPanel subPanel =
                    new NUSMVPanel(frame, genericTree, new GenericInfo(GenericInfo.NUSMV, GenericInfo.NUSMV), editable);
            this.add(subPanel, BorderLayout.CENTER);
        }
        else if (SubPanelType.equals(GenericInfo.PROPERTY))
        {
            final PropertyPanel subPanel = new PropertyPanel(frame, genericTree,
                    new GenericInfo(GenericInfo.PROPERTY, GenericInfo.PROPERTY), editable);
            this.add(subPanel, BorderLayout.CENTER);
        }
        else if (SubPanelType.equals(GenericInfo.SOD))
        {
            final SoDPanel subPanel = new SoDPanel(frame, genericTree,
                    new GenericInfo(GenericInfo.SOD, GenericInfo.SOD), editable);
            this.add(subPanel, BorderLayout.CENTER);
        }

        /* Enable or disable panes and panels */
        frame.tabbedPane.setEnabled(true);

    }

    public void setTree()
    {

        genericTree = new JTree(genericTreeModel);
        genericTree.setModel(genericTreeModel);
        genericTree.addMouseListener(genericPanelMouseAdapter);
        genericTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        genericTree.addTreeSelectionListener(genericPanelTreeSelectionAdapter);

        createModelTree();

    }

}
