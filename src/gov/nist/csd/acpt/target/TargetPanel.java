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
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements the (XACML) target editor panel.
 *
 * @author JeeHyun Hwang
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class TargetPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long              serialVersionUID                = 0;

    /***************************************************************************
     * Variables
     **************************************************************************/

    private String                         targetPanelType                 = null;

    private JPanel                         navigatorPanel                  = null;
    private JTree                          targetTree                      = null;
    private DefaultTreeModel               targetTreeModel                 = null;
    private JScrollPane                    targetTreeScrollPane            = null;
    // private TargetAttributesPanel targetAttributesPanel = null;
    public TargetPropertiesPanel           targetPropertiesPanel           = null;

    /*
     * private SubjectsPanel subjectsPanel = null; private ResourcesPanel
     * resourcesPanel = null; private ActionsPanel actionsPanel = null;
     */

    private UserPanel                      userPanel                       = null;

    private JMenuItem                      addChildMenuItem                = null;
    private JMenuItem                      deleteMenuItem                  = null;
    private JMenuItem                      deleteChildrenMenuItem          = null;
    private JPopupMenu                     groupPopupMenu                  = null;

    private JSplitPane                     horizontalSplitPane             =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private File                           targetInputFile                 = null;
    private ACPTFrame                      frame                           = null;

    /* Adapters */
    public TargetPanelActionAdapter        targetPanelActionAdapter        = null;
    public TargetPanelMouseAdapter         targetPanelMouseAdapter         = null;
    public TargetPanelTreeSelectionAdapter targetPanelTreeSelectionAdapter = null;

    /* Flags */
    public boolean                         usersExist                      = false;
    public boolean                         userAttributesExist             = false;

    /***************************************************************************
     * Constructors
     **************************************************************************/
    /*
     * public TargetPanel(ACPTFrame frame) {
     *
     * this.frame = frame; setAdapters(); setPopupMenus(); setPanels();
     * setTree();
     *
     * }
     */
    public TargetPanel(ACPTFrame frame, String targetPanelType)
    {

        this.frame = frame;
        this.targetPanelType = targetPanelType;
        setAdapters();
        setPopupMenus();
        setPanels();
        setTree();

    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setAdapters()
    {

        this.targetPanelActionAdapter = new TargetPanelActionAdapter(this);
        this.targetPanelMouseAdapter = new TargetPanelMouseAdapter(this);
        this.targetPanelTreeSelectionAdapter = new TargetPanelTreeSelectionAdapter(this);

    }

    /**
     * Create ACPT menus and menu items.
     */
    private void setPopupMenus()
    {

        /******************** Group popup menu *************************/

        groupPopupMenu = new JPopupMenu();

        addChildMenuItem = new JMenuItem("Add");
        addChildMenuItem.addActionListener(targetPanelActionAdapter);
        addChildMenuItem.setActionCommand("Add Child Node");

        deleteMenuItem = new JMenuItem("Remove");
        deleteMenuItem.addActionListener(targetPanelActionAdapter);
        deleteMenuItem.setActionCommand("Remove Node");

        deleteChildrenMenuItem = new JMenuItem("Remove Contents");
        deleteChildrenMenuItem.addActionListener(targetPanelActionAdapter);
        deleteChildrenMenuItem.setActionCommand("Remove Children Nodes");

        groupPopupMenu.add(addChildMenuItem);
        groupPopupMenu.addSeparator();
        groupPopupMenu.add(deleteMenuItem);
        groupPopupMenu.addSeparator();
        groupPopupMenu.add(deleteChildrenMenuItem);

    }

    private void setPanels()
    {

        // this.targetTree.setModel(this.targetTreeModel);

        if (this.targetTree != null)
        {

            // refresh

            boolean editable = false;
            this.removeAll();

            if (targetPropertiesPanel != null)
            {
                System.gc();
            }

            // navigation panel (this panel is shown at the case of
            // TargetInfo.TreeViewable is true);

            this.navigatorPanel = new JPanel();
            this.navigatorPanel.setLayout(new BorderLayout());
            TitledBorder titledBorder = BorderFactory.createTitledBorder("Navigator");
            this.navigatorPanel.setBorder(titledBorder);

            targetTreeScrollPane = null;
            targetTreeScrollPane = new JScrollPane(this.targetTree);

            navigatorPanel.removeAll();
            navigatorPanel.add(targetTreeScrollPane, BorderLayout.CENTER);
            navigatorPanel.validate();
            navigatorPanel.setVisible(TargetInfo.TreeViewable);

            // targetPropertiesPanel panel
            targetPropertiesPanel = new TargetPropertiesPanel(frame, this.targetTree, this.targetPanelType, editable);
            // subjects
            // targetPropertiesPanel = new TargetPropertiesPanel(frame,
            // this.targetTree, TargetInfo.SUBJECTS, editable);

            // layout
            this.setLayout(new BorderLayout());
            this.add(navigatorPanel, BorderLayout.WEST);
            this.add(targetPropertiesPanel, BorderLayout.CENTER);

            // added for refresh 8/10
            this.validate();

            /* Enable or disable panes and panels */
            frame.tabbedPane.setEnabled(true);
        }
    }

    public void updatePanel(DefaultTreeModel treeModel)
    {

        this.targetTreeModel = treeModel;
        // call setTree before setPanels due to setPanel contents are dependent
        // on a tree
        setTree();
        setPanels();
    }

    public void setTree()
    {

        this.targetTree = new JTree(targetTreeModel);
        this.targetTree.setModel(this.targetTreeModel);
        this.targetTree.addMouseListener(targetPanelMouseAdapter);
        this.targetTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.targetTree.addTreeSelectionListener(targetPanelTreeSelectionAdapter);

        ToolTipManager.sharedInstance().registerComponent(targetTree);

        //// Tree Icon Images Setup
        // ImageIcon subjectIcon = GraphicsUtil.getImageIcon(
        // "/images/subject.gif", getClass());
        // ImageIcon subjectAttributeIcon = GraphicsUtil.getImageIcon(
        // "/images/subject_attribute.gif", getClass());
        // ImageIcon resourceIcon = GraphicsUtil.getImageIcon(
        // "/images/resource2.jpg", getClass());
        // ImageIcon resourceAttributeIcon = GraphicsUtil.getImageIcon(
        // "/images/resource_attribute.gif", getClass());
        //
        // if (subjectIcon != null && subjectAttributeIcon != null
        // && resourceIcon != null && resourceAttributeIcon != null) {
        // targetTree.setCellRenderer(new TargetTreeRenderer(subjectIcon,
        // subjectAttributeIcon, resourceIcon, resourceAttributeIcon));
        // }
        //
    }

    /***************************************************************************
     * Public methods
     **************************************************************************/

    public void setEnableComponents(boolean addChildMenuItemBoolean, boolean deleteMenuItemBoolean,
            boolean deleteChildrenMenuItemBoolean)
    {

        /******************** Menus *************************/

        this.addChildMenuItem.setEnabled(addChildMenuItemBoolean);
        frame.addChildMenuItem.setEnabled(addChildMenuItemBoolean);

        this.deleteMenuItem.setEnabled(deleteMenuItemBoolean);
        frame.deleteMenuItem.setEnabled(deleteMenuItemBoolean);

        this.deleteChildrenMenuItem.setEnabled(deleteChildrenMenuItemBoolean);
        frame.deleteChildrenMenuItem.setEnabled(deleteChildrenMenuItemBoolean);

        /******************** Panels *************************/

    }

    // public void setAttributeTree(DefaultTreeModel treeModel) {
    // /* Set project name in frame */
    // // this.frame.setProjectName(projectName);
    // /* Set target tree */
    // /*
    // * this.targetTreeModel = TargetTreeUtil
    // * .createNewTargetTreeModel(projectName);
    // */
    // this.targetTreeModel = treeModel;
    //
    // this.targetTree.setModel(this.targetTreeModel);
    //
    // // this.targetTree = targetTree;
    // targetTreeScrollPane = null;
    // targetTreeScrollPane = new JScrollPane(this.targetTree);
    //
    // navigatorPanel.removeAll();
    // navigatorPanel.add(targetTreeScrollPane, BorderLayout.CENTER);
    // navigatorPanel.validate();
    // navigatorPanel.setVisible(true);
    //
    // /* Enable or disable menu items */
    // frame.saveMenuItem.setEnabled(true);
    // frame.saveAsMenuItem.setEnabled(true);
    // frame.printMenuItem.setEnabled(true);
    // frame.importMenu.setEnabled(true);
    // frame.importTargetsExcelMenuItem.setEnabled(true);
    // frame.importTargetsXACMLMenuItem.setEnabled(true);
    // frame.exportXACMLMenuItem.setEnabled(true);
    //
    // /* */
    // /*
    // * boolean editable = false; targetAttributesPanel = null; System.gc();
    // * targetAttributesPanel = new TargetAttributesPanel(frame, targetTree,
    // * new TargetInfo(TargetInfo.SUBJECTS, TargetInfo.SUBJECTS), editable);
    // * //this.setLayout(new BorderLayout()); //this.add(navigatorPanel,
    // * BorderLayout.WEST); this.add(targetAttributesPanel,
    // * BorderLayout.CENTER);
    // */
    // /* Enable or disable panes and panels */
    // frame.tabbedPane.setEnabled(true);
    //
    // }

    public String createNewProject()
    {

        String projectName = GraphicsUtil.showTextFieldInputDialog("Project name:\n", "New Project",
                "Name cannot be empty string", "Name error", "MyProject", frame);

        if (projectName == null)
        {

            return null;

        }
        else
        {

            /* Set project name in frame */
            this.frame.setProjectName(projectName);

            /* Set target tree */
            this.targetTreeModel = TargetTreeUtil.createNewTargetTreeModel(projectName);
            this.targetTree.setModel(this.targetTreeModel);

            targetTreeScrollPane = null;
            targetTreeScrollPane = new JScrollPane(this.targetTree);

            /*
             * navigatorPanel.removeAll();
             * navigatorPanel.add(targetTreeScrollPane, BorderLayout.CENTER);
             * navigatorPanel.validate(); navigatorPanel.setVisible(true);
             */

            /* Enable or disable menu items */
            frame.saveMenuItem.setEnabled(true);
            frame.saveAsMenuItem.setEnabled(true);
            frame.printMenuItem.setEnabled(true);
            frame.importMenu.setEnabled(true);
            frame.importTargetsExcelMenuItem.setEnabled(true);
            frame.importTargetsXACMLMenuItem.setEnabled(true);
            frame.exportXACMLMenuItem.setEnabled(true);

            /* Enable or disable panes and panels */
            frame.tabbedPane.setEnabled(true);

            return projectName;
        }

    }

    public void createSavedProject(String projectName, DefaultMutableTreeNode root)
    {

        /* Set project name in frame */
        this.frame.setProjectName(projectName);

        /* Set target tree */
        this.targetTreeModel = new DefaultTreeModel(root);
        this.targetTree.setModel(this.targetTreeModel);

        targetTreeScrollPane = null;
        targetTreeScrollPane = new JScrollPane(this.targetTree);

        /*
         * navigatorPanel.removeAll(); navigatorPanel.add(targetTreeScrollPane,
         * BorderLayout.CENTER); navigatorPanel.validate();
         * navigatorPanel.setVisible(true);
         */

        /* Enable or disable menu items */
        frame.saveMenuItem.setEnabled(true);
        frame.saveAsMenuItem.setEnabled(true);
        frame.printMenuItem.setEnabled(true);
        frame.importMenu.setEnabled(true);
        frame.importTargetsExcelMenuItem.setEnabled(true);
        frame.importTargetsXACMLMenuItem.setEnabled(true);
        frame.exportXACMLMenuItem.setEnabled(true);

        /* Enable or disable panes and panels */
        frame.tabbedPane.setEnabled(true);
    }

    public void createSampleProject()
    {

        String projectName = "Sample Project";

        /* Set project name in frame */
        this.frame.setProjectName(projectName);

        /* Set target tree */
        this.targetTreeModel = TargetTreeUtil.createSampleModelTreeModel(projectName);
        this.targetTree.setModel(this.targetTreeModel);

        targetTreeScrollPane = null;
        targetTreeScrollPane = new JScrollPane(this.targetTree);

        /*
         * navigatorPanel.removeAll(); navigatorPanel.add(targetTreeScrollPane,
         * BorderLayout.CENTER); navigatorPanel.validate();
         * navigatorPanel.setVisible(true);
         */

        /* Enable or disable menu items */
        frame.saveMenuItem.setEnabled(true);
        frame.saveAsMenuItem.setEnabled(true);
        frame.printMenuItem.setEnabled(true);
        frame.importMenu.setEnabled(true);
        frame.importTargetsExcelMenuItem.setEnabled(true);
        frame.importTargetsXACMLMenuItem.setEnabled(true);
        frame.exportXACMLMenuItem.setEnabled(true);

        /* Enable or disable panes and panels */
        frame.tabbedPane.setEnabled(true);

        /*
         * public static String ROOT = "Root"; public static String SUBJECTS =
         * "Subjects"; // SUBJECTS public static String SUBJECT_ATTRIBUTES =
         * "Subject Attributes"; // SUBJECT ATTRIBUTES public static String
         * RESOURCES = "Resources"; public static String RESOURCE_ATTRIBUTES =
         * "Resource Attributes"; public static String ACTIONS = "Actions";
         */
        /*
         * clearSelection() expandRow(int row) getMaxSelectionRow()
         * setSelectionRow(int row)
         */
        /*
         * targetTree.setSelectionRow(1);
         *
         * System.err.println(targetTree.getSelectionPaths().toString());
         * TreePath[] a = targetTree.getSelectionPaths(); for (int i = 0; i <
         * a.length; i++) { System.err.println(a[i]);
         * System.err.println(a[i].getClass().getName()); } TreePath b =
         * targetTree.getSelectionPath();
         *
         * System.err.println(b); System.err.println(b.getClass());
         *
         *
         * //targetTree.setSelectionPath(new TreePath[] (new TreePath[] {
         * "Sample Project", "Subjects"})); targetTree.setSelectionPath(b);
         *
         * targetTree.setSelectionRow(1);
         */
        // Sample Project, Subjects
        // targetTree.setSelectionPath(new TreePath (new Object[] {new
        // DefaultMutableTreeNode(new TargetInfo(TargetInfo.ROOT,
        // "Sample Project")), new DefaultMutableTreeNode(new
        // TargetInfo(TargetInfo.SUBJECTS, TargetInfo.SUBJECTS))}));
        /*
         * targetTree.setSelectionRow(1); TreePath b =
         * targetTree.getSelectionPath(); System.err.println(b);
         * System.err.println(b.getClass());
         *
         * targetTree.getPathForRow(1); targetTree.setSelectionPath(new TreePath
         * (new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ROOT,
         * "Sample Project"))));
         */
        /*
         * // an sample from PrincipalPolitica.java
         * targetTree.setSelectionRow(0); TreePath b =
         * targetTree.getSelectionPath(); System.err.println(b);
         * System.err.println(b.getClass()); //
         * System.err.println("good"+b.equals(new TreePath (new Object // (
         * "Sample Project"))));
         *
         * TreePath Path2SUBJECTS = targetTree.getPathForRow(1); //
         * targetTree.setSelectionPath(new TreePath (new //
         * DefaultMutableTreeNode(new TargetInfo(TargetInfo.ROOT, //
         * "Sample Project")))); TreePath Path2SUBJECT_ATTRIBUTES =
         * targetTree.getPathForRow(2); TreePath Path2RESOURCES =
         * targetTree.getPathForRow(3); TreePath Path2RESOURCE_ATTRIBUTES =
         * targetTree.getPathForRow(4); TreePath Path2ACTIONS =
         * targetTree.getPathForRow(5);
         *
         * targetTree.setSelectionPath(Path2SUBJECTS);
         * TargetTreeUtil.addNode(TargetInfo.SUBJECTS, "Jane", targetTree);
         * TargetTreeUtil.addNode(TargetInfo.SUBJECTS, "Jim", targetTree);
         * targetTree.expandPath(Path2SUBJECTS);
         *
         * targetTree.setSelectionPath(Path2SUBJECT_ATTRIBUTES);
         * TargetTreeUtil.addNode(TargetInfo.SUBJECT_ATTRIBUTES, "Faculty",
         * targetTree); TargetTreeUtil.addNode(TargetInfo.SUBJECT_ATTRIBUTES,
         * "Student", targetTree);
         * TargetTreeUtil.addNode(TargetInfo.SUBJECT_ATTRIBUTES, "Manager",
         * targetTree); TargetTreeUtil.addNode(TargetInfo.SUBJECT_ATTRIBUTES,
         * "Secretary", targetTree);
         * TargetTreeUtil.addNode(TargetInfo.SUBJECT_ATTRIBUTES, "Accountant",
         * targetTree); targetTree.expandPath(Path2SUBJECT_ATTRIBUTES);
         *
         * targetTree.setSelectionPath(Path2RESOURCES);
         * TargetTreeUtil.addNode(TargetInfo.RESOURCES, "InternalGrade",
         * targetTree); TargetTreeUtil.addNode(TargetInfo.RESOURCES,
         * "ExternalGrade", targetTree);
         * TargetTreeUtil.addNode(TargetInfo.RESOURCES, "order", targetTree);
         * targetTree.expandPath(Path2RESOURCES);
         *
         * targetTree.setSelectionRow(5);
         * targetTree.setSelectionPath(Path2ACTIONS);
         * TargetTreeUtil.addNode(TargetInfo.ACTIONS, "write", targetTree);
         * TargetTreeUtil.addNode(TargetInfo.ACTIONS, "grade", targetTree);
         * TargetTreeUtil.addNode(TargetInfo.ACTIONS, "read", targetTree);
         * TargetTreeUtil.addNode(TargetInfo.ACTIONS, "pay", targetTree);
         * targetTree.expandPath(Path2RESOURCES);
         */

        // targetTree.setRootVisible(false);
    }

    public void addChildNode()
    {

        TargetInfo targetInfo = TargetTreeUtil.getCurrentTarget(targetTree);
        String targetValue = targetInfo.getValue();

        String childTargetName = GraphicsUtil.showTextFieldInputDialog(targetValue + " name:\n", "New " + targetValue,
                "Name cannot be empty string", "Name error", "", frame);

        if (childTargetName == null)
        {

            return;

        }
        else
        {

            TargetTreeUtil.addNode(targetInfo.getTargetType(), childTargetName, targetTree);

            usersExist = true;

        }

    }

    public void removeNode()
    {

        TargetTreeUtil.removeNode(targetTree);

    }

    public void removeAllChildren()
    {

        TargetTreeUtil.removeAllChildren(targetTree);

    }

    public JTree getTargetTree()
    {
        return targetTree;
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
    class TargetPanelActionAdapter implements ActionListener
    {

        TargetPanel targetPanel = null;

        TargetPanelActionAdapter(TargetPanel targetPanel)
        {

            this.targetPanel = targetPanel;

        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("Target Panel: " + e.getActionCommand());

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

                System.err.println("TargetPanel warning: command not implemented: " + e.getActionCommand());
            }

        }

    }

    /**
     * This inner class implements the target panel mouse adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class TargetPanelMouseAdapter extends MouseAdapter
    {

        TargetPanel targetPanel = null;

        TargetPanelMouseAdapter(TargetPanel targetPanel)
        {

            this.targetPanel = targetPanel;

        }

        /**
         * This method is primarily used for invoking a popup menu on the tree.
         */
        @Override
        public void mouseReleased(MouseEvent e)
        {

            if (e.getSource() == targetTree)
            {

                if (e.isPopupTrigger())
                {

                    DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode) targetPanel.targetTree.getLastSelectedPathComponent();

                    if (node == null)
                    {
                        // Nothing is selected.
                        return;
                    }

                    Object nodeInfo = node.getUserObject();
                    TargetInfo targetInfo = (TargetInfo) nodeInfo;
                    String targetType = targetInfo.getTargetType();
                    String targetValue = targetInfo.getValue();

                    if (targetType.equals(TargetInfo.ROOT))
                    {

                        setEnableComponents(false, false, false);

                    }
                    else
                    {

                        if (targetType.equals(TargetInfo.SUBJECTS))
                        {

                            if (node.getLevel() == 1)
                            { // USERS root

                                if (node.getChildCount() > 0)
                                {
                                    setEnableComponents(true, false, true);
                                }
                                else
                                {
                                    setEnableComponents(true, false, false);
                                }

                            }
                            else if (node.getLevel() == 2)
                            { // USERS
                              // instances

                                setEnableComponents(false, true, false);

                            }

                        }
                        else if (targetType.equals(TargetInfo.SUBJECT_ATTRIBUTES))
                        {

                            if (node.getLevel() == 1)
                            { // SUBJECT_ATTRIBUTES
                              // Root

                                if (node.getChildCount() > 0)
                                {
                                    setEnableComponents(true, false, true);
                                }
                                else
                                {
                                    setEnableComponents(true, false, false);
                                }

                            }
                            else if (node.getLevel() == 2)
                            { // SUBJECT_ATTRIBUTES
                              // INSTANCES

                                setEnableComponents(false, true, false);

                            }

                        }
                        else if (targetType.equals(TargetInfo.RESOURCES))
                        {

                            if (node.getChildCount() > 0)
                            {
                                setEnableComponents(true, false, true);
                            }
                            else
                            {
                                setEnableComponents(true, false, false);
                            }

                            if (node.getLevel() == 2)
                            {
                                setEnableComponents(false, true, false);
                            }

                        }
                        else if (targetType.equals(TargetInfo.RESOURCE_ATTRIBUTES))
                        {

                            if (node.getChildCount() > 0)
                            {
                                setEnableComponents(true, false, true);
                            }
                            else
                            {
                                setEnableComponents(true, false, false);
                            }

                            if (node.getLevel() == 2)
                            {
                                setEnableComponents(false, true, false);
                            }

                        }
                        else if (targetType.equals(TargetInfo.ENVIRONMENTS))
                        {

                            if (node.getChildCount() > 0)
                            {
                                setEnableComponents(true, false, true);
                            }
                            else
                            {
                                setEnableComponents(true, false, false);
                            }

                            if (node.getLevel() == 2)
                            {
                                setEnableComponents(false, true, false);
                            }

                        }
                        else if (targetType.equals(TargetInfo.ENVIRONMENT_ATTRIBUTES))
                        {

                            if (node.getChildCount() > 0)
                            {
                                setEnableComponents(true, false, true);
                            }
                            else
                            {
                                setEnableComponents(true, false, false);
                            }

                            if (node.getLevel() == 2)
                            {
                                setEnableComponents(false, true, false);
                            }

                        }
                        else if (targetType.equals(TargetInfo.ACTIONS))
                        {

                            if (node.getChildCount() > 0)
                            {
                                setEnableComponents(true, false, true);
                            }
                            else
                            {
                                setEnableComponents(true, false, false);
                            }

                            if (node.getLevel() == 2)
                            {
                                setEnableComponents(false, true, false);
                            }
                        }

                        groupPopupMenu.show(e.getComponent(), e.getX(), e.getY());

                    }

                    targetPanel.horizontalSplitPane.setRightComponent(targetPropertiesPanel);
                    targetPanel.horizontalSplitPane.setDividerLocation(400);

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
        public void mousePressed(MouseEvent e)
        {

            if (SwingUtilities.isRightMouseButton(e))
            {

                int row = targetTree.getClosestRowForLocation(e.getX(), e.getY());
                targetTree.setSelectionRow(row);

            }

        }

        @Override
        public void mouseClicked(MouseEvent e)
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

    }

    /**
     * This inner class implements the target panel mouse adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class TargetPanelTreeSelectionAdapter implements TreeSelectionListener
    {

        TargetPanel targetPanel = null;

        TargetPanelTreeSelectionAdapter(TargetPanel targetPanel)
        {

            this.targetPanel = targetPanel;

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
        public void valueChanged(TreeSelectionEvent e)
        {

            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) targetPanel.targetTree.getLastSelectedPathComponent();

            if (node == null)
            {
                // Nothing is selected.
                return;
            }

            Object nodeInfo = node.getUserObject();
            TargetInfo targetInfo = (TargetInfo) nodeInfo;
            String targetType = targetInfo.getTargetType();
            String targetValue = targetInfo.getValue();
            boolean editable = false;

            if (targetType.equals(TargetInfo.ROOT))
            {

                setEnableComponents(false, false, false);
                editable = true;
            }
            else
            {
                //
                // if (targetType.equals(TargetInfo.SUBJECTS)) {
                //
                // if (node.getLevel() == 1) { // USERS root
                //
                // targetPropertiesPanel = null;
                // System.gc();
                // targetPropertiesPanel = new TargetPropertiesPanel(
                // frame, targetTree, targetInfo, editable);

                targetPanel.horizontalSplitPane.setRightComponent(targetPropertiesPanel);
                targetPanel.horizontalSplitPane.setDividerLocation(400);
                ////
                // if (node.getChildCount() > 0) {
                // setEnableComponents(true, false, true);
                // } else {
                // setEnableComponents(true, false, false);
                // }
                //
                // } else if (node.getLevel() == 2) { // USERS
                // // instances
                //
                // userPanel = null;
                // System.gc();
                // userPanel = new UserPanel(frame, targetTree,
                // targetInfo, editable);
                //
                // targetPanel.horizontalSplitPane
                // .setRightComponent(userPanel);
                // targetPanel.horizontalSplitPane.setDividerLocation(400);
                //
                // setEnableComponents(false, true, false);
                //
                // }
                //
                // } else if (targetType.equals(TargetInfo.SUBJECT_ATTRIBUTES))
                //// {
                //
                // if (node.getLevel() == 1) { // SUBJECT_ATTRIBUTES
                // // Root
                //
                // targetPropertiesPanel = null;
                // System.gc();
                // targetPropertiesPanel = new TargetPropertiesPanel(
                // frame, targetTree, targetInfo, editable);
                //
                // targetPanel.horizontalSplitPane
                // .setRightComponent(targetPropertiesPanel);
                // targetPanel.horizontalSplitPane.setDividerLocation(400);
                //
                // if (node.getChildCount() > 0) {
                // setEnableComponents(true, false, true);
                // } else {
                // setEnableComponents(true, false, false);
                // }
                //
                // } else if (node.getLevel() == 2) { // SUBJECT_ATTRIBUTES
                // // INSTANCES
                //
                // targetPropertiesPanel = null;
                // System.gc();
                // targetPropertiesPanel = new TargetPropertiesPanel(
                // frame, targetTree, targetInfo, editable);
                //
                // targetPanel.horizontalSplitPane
                // .setRightComponent(targetPropertiesPanel);
                // targetPanel.horizontalSplitPane.setDividerLocation(400);
                //
                // setEnableComponents(true, true, false);
                //
                // }
                //
                // } else {
                //
                // editable = true;
                //
                // if (node.getChildCount() > 0) {
                //
                // setEnableComponents(true, true, true);
                //
                // } else {
                //
                // setEnableComponents(true, true, false);
                //
                // }
                //
                // targetPropertiesPanel = null;
                // System.gc();
                // targetPropertiesPanel = new TargetPropertiesPanel(frame,
                // targetTree, targetInfo, editable);
                //
                // targetPanel.horizontalSplitPane
                // .setRightComponent(targetPropertiesPanel);
                // targetPanel.horizontalSplitPane.setDividerLocation(400);
                //
                // }
            }

        }

    }

    class TargetTreeRenderer extends DefaultTreeCellRenderer
    {

        Icon subjectIcon;
        Icon resourceIcon;
        Icon environmentIcon;
        Icon subjectAttributeIcon;
        Icon resourceAttributeIcon;
        Icon environmentAttributeIcon;

        public TargetTreeRenderer(Icon subjectIcon, Icon subjectAttributeIcon, Icon resourceIcon,
                Icon resourceAttributeIcon)
        {

            this.subjectIcon = subjectIcon;
            this.resourceIcon = resourceIcon;
            this.environmentIcon = environmentIcon;
            this.subjectAttributeIcon = subjectAttributeIcon;
            this.resourceAttributeIcon = resourceAttributeIcon;
            this.environmentAttributeIcon = environmentAttributeIcon;
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                boolean leaf, int row, boolean hasFocus)
        {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (leaf && isSubject(value))
            {
                setIcon(subjectIcon);
                setToolTipText("Subject or user");

            }
            else if (leaf && isSubjectAttribute(value))
            {
                setIcon(subjectAttributeIcon);
                setToolTipText("Subject or user attribute");

            }
            else if (leaf && isResource(value))
            {
                setIcon(resourceIcon);
                setToolTipText("Resource");

            }
            else if (leaf && isResourceAttribute(value))
            {
                setIcon(resourceAttributeIcon);
                setToolTipText("Resource attribute");

            }
            else if (leaf && isEnvironmentAttribute(value))
            {
                setIcon(environmentAttributeIcon);
                setToolTipText("Environment attribute");

            }

            else
            {
                setToolTipText(null); // no tool tip
            }

            return this;
        }

        protected boolean isSubject(Object value)
        {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            TargetInfo targetInfo = (TargetInfo) (node.getUserObject());

            if ((targetInfo.getTargetType().equals(TargetInfo.SUBJECTS))
                    && (!targetInfo.getValue().equals(TargetInfo.SUBJECTS)))
            {

                return true;

            }
            else
            {
                return false;
            }

        }

        protected boolean isResource(Object value)
        {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            TargetInfo targetInfo = (TargetInfo) (node.getUserObject());

            if ((targetInfo.getTargetType().equals(TargetInfo.RESOURCES))
                    && (!targetInfo.getValue().equals(TargetInfo.RESOURCES)))
            {

                return true;

            }
            else
            {
                return false;
            }

        }

        protected boolean isEnvironment(Object value)
        {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            TargetInfo targetInfo = (TargetInfo) (node.getUserObject());

            if ((targetInfo.getTargetType().equals(TargetInfo.ENVIRONMENTS))
                    && (!targetInfo.getValue().equals(TargetInfo.ENVIRONMENTS)))
            {

                return true;

            }
            else
            {
                return false;
            }

        }

        protected boolean isSubjectAttribute(Object value)
        {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            TargetInfo targetInfo = (TargetInfo) (node.getUserObject());

            if ((targetInfo.getTargetType().equals(TargetInfo.SUBJECT_ATTRIBUTES))
                    && (!targetInfo.getValue().equals(TargetInfo.SUBJECT_ATTRIBUTES)))
            {

                return true;

            }
            else
            {
                return false;
            }

        }

        protected boolean isResourceAttribute(Object value)
        {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            TargetInfo targetInfo = (TargetInfo) (node.getUserObject());

            if ((targetInfo.getTargetType().equals(TargetInfo.RESOURCE_ATTRIBUTES))
                    && (!targetInfo.getValue().equals(TargetInfo.RESOURCE_ATTRIBUTES)))
            {

                return true;

            }
            else
            {
                return false;
            }

        }

        protected boolean isEnvironmentAttribute(Object value)
        {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            TargetInfo targetInfo = (TargetInfo) (node.getUserObject());

            if ((targetInfo.getTargetType().equals(TargetInfo.ENVIRONMENT_ATTRIBUTES))
                    && (!targetInfo.getValue().equals(TargetInfo.ENVIRONMENT_ATTRIBUTES)))
            {

                return true;

            }
            else
            {
                return false;
            }

        }
        // protected boolean isTutorialBook(Object value) {
        // DefaultMutableTreeNode node =
        // (DefaultMutableTreeNode)value;
        // BookInfo nodeInfo =
        // (BookInfo)(node.getUserObject());
        // String title = nodeInfo.bookName;
        // if (title.indexOf("Tutorial") >= 0) {
        // return true;
        // }
        //
        // return false;
        // }
    }

}
