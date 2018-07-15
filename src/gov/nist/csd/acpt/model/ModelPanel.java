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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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

public class ModelPanel extends JPanel
{

    /**
     * This inner class implements the model panel mouse adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class ModelPanelActionAdapter implements ActionListener
    {

        ModelPanel modelPanel = null;

        ModelPanelActionAdapter(final ModelPanel modelPanel)
        {

            this.modelPanel = modelPanel;

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

                // System.err.println("ModelPanel warning: command not
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
    class ModelPanelMouseAdapter extends MouseAdapter
    {

        ModelPanel modelPanel = null;

        ModelPanelMouseAdapter(final ModelPanel modelPanel)
        {

            this.modelPanel = modelPanel;

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

                final int row = modelTree.getClosestRowForLocation(e.getX(), e.getY());
                modelTree.setSelectionRow(row);

            }

        }

        /**
         * This method is primarily used for invoking a popup menu on the tree.
         */
        @Override
        public void mouseReleased(final MouseEvent e)
        {

            if (e.getSource() == modelTree)
            {

                if (e.isPopupTrigger())
                {

                    final DefaultMutableTreeNode node =
                            (DefaultMutableTreeNode) modelPanel.modelTree.getLastSelectedPathComponent();

                    if (node == null)
                    {
                        // Nothing is selected.
                        return;
                    }

                    final Object nodeInfo = node.getUserObject();
                    final ModelInfo modelInfo = (ModelInfo) nodeInfo;
                    final String modelType = modelInfo.getModelType();
                    final String modelValue = modelInfo.getValue();

                    if (modelType.equals(ModelInfo.ROOT))
                    {

                        setEnableComponents(false, false, false);

                    }
                    else
                    {

                        // The following is changing...

                        if ((modelType.equals(ModelInfo.RBAC)) || (modelType.equals(ModelInfo.MULTILEVEL))
                                || (modelType.equals(ModelInfo.WORKFLOW)) || (modelType.equals(ModelInfo.ABAC)))
                        {

                            if (node.getLevel() == ModelInfo.NodeLevelofmodelroot)
                            {
                                if (node.getChildCount() > 0)
                                {

                                    setEnableComponents(true, false, true);

                                }
                                else
                                {

                                    setEnableComponents(true, false, false);

                                }
                            }
                            else if (node.getLevel() == ModelInfo.NodeLevelofmodel)
                            {
                                if (node.getChildCount() > 0)
                                {
                                    setEnableComponents(false, true, true);
                                }
                                else
                                {
                                    setEnableComponents(false, true, false);
                                }
                            }

                        }
                        else
                        {

                            if (node.getChildCount() > 0)
                            {

                                setEnableComponents(true, true, true);

                            }
                            else
                            {

                                setEnableComponents(true, true, false);

                            }
                            if (node.getLevel() == ModelInfo.NodeLevelofruleroot)
                            {

                                if (node.getChildCount() > 0)
                                {
                                    setEnableComponents(false, true, true);
                                }
                                else
                                {
                                    setEnableComponents(false, true, false);
                                }
                            }
                            else if (node.getLevel() == ModelInfo.NodeLevelofrule)
                            {
                                setEnableComponents(false, true, false);
                            }
                        }

                        groupPopupMenu.show(e.getComponent(), e.getX(), e.getY());

                    }

                    modelPanel.horizontalSplitPane.setRightComponent(modelPropertiesPanel);
                    modelPanel.horizontalSplitPane.setDividerLocation(400);

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
    class ModelPanelTreeSelectionAdapter implements TreeSelectionListener
    {

        ModelPanel modelPanel = null;

        ModelPanelTreeSelectionAdapter(final ModelPanel modelPanel)
        {

            this.modelPanel = modelPanel;

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
                    (DefaultMutableTreeNode) modelPanel.modelTree.getLastSelectedPathComponent();

            if (node == null)
            {
                // Nothing is selected.
                return;
            }

            final Object nodeInfo = node.getUserObject();
            final ModelInfo modelInfo = (ModelInfo) nodeInfo;
            final String modelType = modelInfo.getModelType();
            final String modelValue = modelInfo.getValue();
            boolean editable = false;

            if (modelType.equals(ModelInfo.ROOT))
            {

                setEnableComponents(false, false, false);
                editable = false;

            }
            else
            {

                // if (modelType.equals(ModelInfo.RBAC)) {
                //
                // if (node.getLevel() == ModelInfo.NodeLevelofmodelroot ) { //
                // USERS root
                //
                //
                // System.out
                // .println("ModelPanelTreeSelectionAdapter.valueChanged()-USERS
                // root");
                // modelPropertiesPanel = null;
                // System.gc();
                // modelPropertiesPanel = new ModelPropertiesPanel(frame,
                // modelTree, modelInfo, editable);
                //
                // modelPanel.horizontalSplitPane
                // .setRightComponent(modelPropertiesPanel);
                // modelPanel.horizontalSplitPane.setDividerLocation(400);
                //
                //
                //
                // if (node.getChildCount() > 0) {
                // setEnableComponents(true, false, true);
                // } else {
                // setEnableComponents(true, false, false);
                // }
                //
                // } else if (node.getLevel() == ModelInfo.NodeLevelofmodel) {
                // // USERS
                // // instances
                // //System.out
                // //.println("ModelPanelTreeSelectionAdapter.valueChanged()-USERS
                // instance");
                //
                // rbacPanel = null;
                // System.gc();
                // rbacPanel = new RBACPanel(frame, modelTree, modelInfo,
                // editable);
                //
                // modelPanel.horizontalSplitPane
                // .setRightComponent(rbacPanel);
                // modelPanel.horizontalSplitPane.setDividerLocation(400);
                //
                // setEnableComponents(false, true, false);
                //
                // }
                // }
                //
                // if (modelType.equals(ModelInfo.RBACRule)) {
                //
                // if (node.getLevel() == ModelInfo.NodeLevelofruleroot) { //
                // USERS root
                // System.out
                // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.RBACRule
                // with 3");
                //
                // modelPropertiesPanel = null;
                // System.gc();
                // modelPropertiesPanel = new ModelPropertiesPanel(frame,
                // modelTree, modelInfo, editable);
                //
                // modelPanel.horizontalSplitPane
                // .setRightComponent(modelPropertiesPanel);
                // modelPanel.horizontalSplitPane.setDividerLocation(400);
                //
                // setEnableComponents(false, false, false);
                // editable = false;
                //
                // } else if (node.getLevel() == ModelInfo.NodeLevelofrule) { //
                // USERS root
                // System.out
                // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.RBACRule
                // with 4");
                //
                // rbacRulePanel = null;
                // System.gc();
                // rbacRulePanel = new RBACRulePanel(frame,
                // modelTree, modelInfo, editable);
                //
                // modelPanel.horizontalSplitPane
                // .setRightComponent(rbacRulePanel);
                // modelPanel.horizontalSplitPane.setDividerLocation(400);
                //
                // setEnableComponents(false, true, false);
                // editable = false;
                // }
                // }

                if (modelType.equals(ModelInfo.ABAC))
                {

                    if (node.getLevel() == ModelInfo.NodeLevelofmodelroot)
                    { // USERS root

                        // System.out
                        // .println("ModelPanelTreeSelectionAdapter.valueChanged()-
                        // ABAC USERS root");
                        modelPropertiesPanel = null;
                        System.gc();
                        modelPropertiesPanel = new ModelPropertiesPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(modelPropertiesPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        if (node.getChildCount() > 0)
                        {
                            setEnableComponents(true, false, true);
                        }
                        else
                        {
                            setEnableComponents(true, false, false);
                        }

                    }
                    else if (node.getLevel() == ModelInfo.NodeLevelofmodel)
                    { // USERS
                      // instances
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged()-ABAC
                      // USERS instance with level 2");

                        abacPanel = null;
                        System.gc();
                        abacPanel = new ABACPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(abacPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, true, false);

                    }
                }

                if (modelType.equals(ModelInfo.ABACRule))
                {

                    if (node.getLevel() == ModelInfo.NodeLevelofruleroot)
                    { // USERS root
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.ABACRule
                      // with 3");

                        modelPropertiesPanel = null;
                        System.gc();
                        modelPropertiesPanel = new ModelPropertiesPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(modelPropertiesPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, false, false);
                        editable = false;

                    }
                    else if (node.getLevel() == ModelInfo.NodeLevelofrule)
                    { // USERS root
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.ABACRule
                      // with 4");

                        abacRulePanel = null;
                        System.gc();
                        abacRulePanel = new ABACRulePanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(abacRulePanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, true, false);
                        editable = false;
                    }
                }

                if (modelType.equals(ModelInfo.WORKFLOW))
                {

                    if (node.getLevel() == ModelInfo.NodeLevelofmodelroot)
                    { // USERS root

                        // System.out
                        // .println("ModelPanelTreeSelectionAdapter.valueChanged()-
                        // WORKFLOW USERS root");
                        modelPropertiesPanel = null;
                        System.gc();
                        modelPropertiesPanel = new ModelPropertiesPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(modelPropertiesPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        if (node.getChildCount() > 0)
                        {
                            setEnableComponents(true, false, true);
                        }
                        else
                        {
                            setEnableComponents(true, false, false);
                        }

                    }
                    else if (node.getLevel() == ModelInfo.NodeLevelofmodel)
                    { // USERS
                      // instances
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged()-WORKFLOW
                      // USERS instance with level 2");

                        workflowPanel = null;
                        System.gc();
                        workflowPanel = new WORKFLOWPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(workflowPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, true, false);

                    }
                }

                if (modelType.equals(ModelInfo.WORKFLOWRule))
                {

                    if (node.getLevel() == ModelInfo.NodeLevelofruleroot)
                    { // USERS root
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.WORKFLOWRule
                      // with 3");

                        modelPropertiesPanel = null;
                        System.gc();
                        modelPropertiesPanel = new ModelPropertiesPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(modelPropertiesPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, false, false);
                        editable = false;

                    }
                    else if (node.getLevel() == ModelInfo.NodeLevelofrule)
                    { // USERS root
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.WORKFLOWRule
                      // with 4");

                        workflowRulePanel = null;
                        System.gc();
                        workflowRulePanel = new WORKFLOWRulePanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(workflowRulePanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, true, false);
                        editable = false;
                    }
                }

                if (modelType.equals(ModelInfo.MULTILEVEL))
                {

                    if (node.getLevel() == ModelInfo.NodeLevelofmodelroot)
                    { // USERS root

                        // System.out
                        // .println("ModelPanelTreeSelectionAdapter.valueChanged()-
                        // MULTILEVEL USERS root");
                        modelPropertiesPanel = null;
                        System.gc();
                        modelPropertiesPanel = new ModelPropertiesPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(modelPropertiesPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        if (node.getChildCount() > 0)
                        {
                            setEnableComponents(true, false, true);
                        }
                        else
                        {
                            setEnableComponents(true, false, false);
                        }

                    }
                    else if (node.getLevel() == ModelInfo.NodeLevelofmodel)
                    { // USERS
                      // instances
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged()-MULTILEVEL
                      // USERS instance with level 2");

                        multilevelPanel = null;
                        System.gc();
                        multilevelPanel = new MULTILEVELPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(multilevelPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, true, false);

                    }
                }

                if (modelType.equals(ModelInfo.MULTILEVELRule))
                {

                    if (node.getLevel() == ModelInfo.NodeLevelofruleroot)
                    { // USERS root
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.MULTILEVELRule
                      // with 3");

                        modelPropertiesPanel = null;
                        System.gc();
                        modelPropertiesPanel = new ModelPropertiesPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(modelPropertiesPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, false, false);
                        editable = false;

                    }
                    else if (node.getLevel() == ModelInfo.NodeLevelofrule)
                    { // USERS root
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.MULTILEVELRule
                      // with 4");

                        multilevelRulePanel = null;
                        System.gc();
                        multilevelRulePanel = new MULTILEVELRulePanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(multilevelRulePanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, true, false);
                        editable = false;
                    }
                }
                if (modelType.equals(ModelInfo.MULTILEVELSUBJECTLEVELS))
                {

                    if (node.getLevel() == ModelInfo.NodeLevelofruleroot)
                    { // USERS root
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.MULTILEVELSUBJECTLEVELS
                      // with 3");

                        modelPropertiesPanel = null;
                        System.gc();
                        modelPropertiesPanel = new ModelPropertiesPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(modelPropertiesPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, false, false);
                        editable = false;

                    }
                    else if (node.getLevel() == ModelInfo.NodeLevelofrule)
                    { // USERS root
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.MULTILEVELSUBJECTLEVELS
                      // with 4");

                        multilevelSubjectLevelsPanel = null;
                        System.gc();
                        multilevelSubjectLevelsPanel =
                                new MULTILEVELSubjectLevelsPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(multilevelSubjectLevelsPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, true, false);
                        editable = false;
                    }
                }
                if (modelType.equals(ModelInfo.MULTILEVELRESOURCELEVELS))
                {

                    if (node.getLevel() == ModelInfo.NodeLevelofruleroot)
                    { // USERS root
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.MULTILEVELRESOURCELEVELS
                      // with 3");

                        modelPropertiesPanel = null;
                        System.gc();
                        modelPropertiesPanel = new ModelPropertiesPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(modelPropertiesPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, false, false);
                        editable = false;

                    }
                    else if (node.getLevel() == ModelInfo.NodeLevelofrule)
                    { // USERS root
                      // System.out
                      // .println("ModelPanelTreeSelectionAdapter.valueChanged():ModelInfo.MULTILEVELRESOURCELEVELS
                      // with 4");

                        multilevelResourceLevelsPanel = null;
                        System.gc();
                        multilevelResourceLevelsPanel =
                                new MULTILEVELResourceLevelsPanel(frame, modelTree, modelInfo, editable);

                        modelPanel.horizontalSplitPane.setRightComponent(multilevelResourceLevelsPanel);
                        modelPanel.horizontalSplitPane.setDividerLocation(400);

                        setEnableComponents(false, true, false);
                        editable = false;
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

    private static final long             serialVersionUID               = 0;

    /***************************************************************************
     * Variables
     **************************************************************************/

    private JPanel                        navigatorPanel                 = null;
    private JTree                         modelTree                      = null;
    private JTree                         targetTree                     = null;
    private DefaultTreeModel              modelTreeModel                 = null;
    private JScrollPane                   modelTreeScrollPane            = null;
    private ModelPropertiesPanel          modelPropertiesPanel           = null;
    // private RBACPanel rbacPanel = null;
    // private RBACRulePanel rbacRulePanel = null;
    private ABACPanel                     abacPanel                      = null;
    private ABACRulePanel                 abacRulePanel                  = null;
    private WORKFLOWPanel                 workflowPanel                  = null;
    private WORKFLOWRulePanel             workflowRulePanel              = null;
    private MULTILEVELPanel               multilevelPanel                = null;

    private MULTILEVELRulePanel           multilevelRulePanel            = null;
    private MULTILEVELSubjectLevelsPanel  multilevelSubjectLevelsPanel   = null;
    private MULTILEVELResourceLevelsPanel multilevelResourceLevelsPanel  = null;
    private JMenuItem                     addChildMenuItem               = null;

    private JMenuItem                     deleteMenuItem                 = null;
    private JMenuItem                     deleteChildrenMenuItem         = null;
    private JPopupMenu                    groupPopupMenu                 = null;

    private JSplitPane                    horizontalSplitPane            = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private final File                    modelInputFile                 = null;
    private ACPTFrame                     frame                          = null;

    /* Adapters */
    public ModelPanelActionAdapter        modelPanelActionAdapter        = null;

    public ModelPanelMouseAdapter         modelPanelMouseAdapter         = null;

    public ModelPanelTreeSelectionAdapter modelPanelTreeSelectionAdapter = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public ModelPanel(final ACPTFrame frame)
    {
        this.frame = frame;
        targetTree = frame.targetPanel.getTargetTree();
        setAdapters();
        setPopupMenus();
        setPanels();
        setTree();
    }

    public void addChildNode()
    {

        final ModelInfo modelInfo = ModelTreeUtil.getCurrentModel(modelTree);
        final String modelValue = modelInfo.getValue();

        String childModelName = GraphicsUtil.showTextFieldInputDialog(modelValue + " name:\n", "New " + modelValue,
                "Name cannot be empty string", "Name error", "", frame);

        if (childModelName == null)
        {
            GraphicsUtil.showWarningDialog("Warning: null policy name.", "Incorrect Name Warning", null);
            return;
        }

        childModelName = childModelName.trim();
        childModelName = childModelName.replaceAll(" ", "_");

        final Pattern p = Pattern.compile("[A-Za-z][A-Za-z0-9_:]*");
        final Matcher m = p.matcher(childModelName);

        if (childModelName.trim().equals(""))
        {

            GraphicsUtil.showWarningDialog("Warning: Null or empty policy name.", "Incorrect Name Warning", null);
            return;

        }
        else if (!m.matches())
        {

            JOptionPane.showMessageDialog(null,
                    "Error: Invalid attribute name is used.\nCheck and follow naming rules below. \n 1. Policy names can start with only letters.\n 2. Policy names can only contain letters, numbers, and the underscore ( _ )",
                    "Warning", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        else
        {

            childModelName = childModelName.trim();

            // ModelTreeUtil.getCurrentNode(modelTree)
            // Conflict Check

            if (!ModelTreeUtil.IsPolicyModelNameNonConflict(modelValue, childModelName, modelTree))
            {
                return;
            }

            final DefaultMutableTreeNode childNode =
                    ModelTreeUtil.addNode2(modelInfo.getModelType(), childModelName, modelTree);

            if (modelValue.equals("ABAC"))
            {
                final DefaultMutableTreeNode multilevelGradRuleRoot = new DefaultMutableTreeNode(
                        new ModelInfo(ModelInfo.ABACRule, "Rules: " + ModelInfo.FirstApplRuleCombinatoin));
                childNode.add(multilevelGradRuleRoot);
                ((DefaultTreeModel) modelTree.getModel()).reload(childNode);
            }
            else if (modelValue.equals("MULTILEVEL"))
            {
                final DefaultMutableTreeNode multilevelResourceLevelsRoot = new DefaultMutableTreeNode(
                        new ModelInfo(ModelInfo.MULTILEVELRESOURCELEVELS, "Resource Levels"));

                final DefaultMutableTreeNode multilevelSubjectLevelsRoot =
                        new DefaultMutableTreeNode(new ModelInfo(ModelInfo.MULTILEVELSUBJECTLEVELS, "Subject Levels"));

                final DefaultMutableTreeNode multilevelGradRuleRoot = new DefaultMutableTreeNode(
                        new ModelInfo(ModelInfo.MULTILEVELRule, "Rules: " + ModelInfo.FirstApplRuleCombinatoin));
                childNode.add(multilevelSubjectLevelsRoot);
                childNode.add(multilevelResourceLevelsRoot);
                childNode.add(multilevelGradRuleRoot);
                ((DefaultTreeModel) modelTree.getModel()).reload(childNode);
            }
            else if (modelValue.equals("WORKFLOW"))
            {
                final DefaultMutableTreeNode multilevelGradRuleRoot = new DefaultMutableTreeNode(
                        new ModelInfo(ModelInfo.WORKFLOWRule, "Rules: " + ModelInfo.FirstApplRuleCombinatoin));
                childNode.add(multilevelGradRuleRoot);
                ((DefaultTreeModel) modelTree.getModel()).reload(childNode);
            }

        }

    }

    public void createModelTree()
    {

        /* Set model tree */
        modelTreeModel = ModelTreeUtil.createNewModelTreeModel("Model");
        modelTree.setModel(modelTreeModel);

        modelTreeScrollPane = null;
        modelTreeScrollPane = new JScrollPane(modelTree);

        navigatorPanel.removeAll();
        navigatorPanel.add(modelTreeScrollPane, BorderLayout.CENTER);
        navigatorPanel.validate();
        navigatorPanel.setVisible(true);

    }

    public void createSampleProject()
    {

        modelTreeModel = ModelTreeUtil.createSampleModelTreeModel();

        modelTree.setModel(modelTreeModel);

        modelTreeScrollPane = null;
        modelTreeScrollPane = new JScrollPane(modelTree);

        navigatorPanel.removeAll();
        navigatorPanel.add(modelTreeScrollPane, BorderLayout.CENTER);
        navigatorPanel.validate();
        navigatorPanel.setVisible(true);

        int row = 0;
        while (row < modelTree.getRowCount())
        {
            modelTree.expandRow(row);
            row++;
        }

    }

    public void createSavedProject(final DefaultMutableTreeNode root)
    {
        modelTreeModel = ModelTreeUtil.createSampleModelTreeModel();
        modelTreeModel = new DefaultTreeModel(root);
        modelTree.setModel(modelTreeModel);

        modelTreeScrollPane = null;
        modelTreeScrollPane = new JScrollPane(modelTree);

        navigatorPanel.removeAll();
        navigatorPanel.add(modelTreeScrollPane, BorderLayout.CENTER);
        navigatorPanel.validate();
        navigatorPanel.setVisible(true);

        int row = 0;
        while (row < modelTree.getRowCount())
        {
            modelTree.expandRow(row);
            row++;
        }
    }

    public JTree getModelTree()
    {
        return modelTree;
    }

    public void removeAllChildren()
    {

        ModelTreeUtil.removeAllChildren(modelTree);

    }

    public void removeNode()
    {

        ModelTreeUtil.removeNode(modelTree);

    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setAdapters()
    {

        modelPanelActionAdapter = new ModelPanelActionAdapter(this);
        modelPanelMouseAdapter = new ModelPanelMouseAdapter(this);
        modelPanelTreeSelectionAdapter = new ModelPanelTreeSelectionAdapter(this);

    }

    public void setAttributeTree(final DefaultTreeModel treeModel)
    {
        // setPanels();

        /* Set project name in frame */
        // this.frame.setProjectName(projectName);

        /* Set target tree */
        /*
         * this.targetTreeModel = TargetTreeUtil
         * .createNewTargetTreeModel(projectName);
         */
        modelTreeModel = treeModel;

        modelTree.setModel(modelTreeModel);

        // this.targetTree = targetTree;
        modelTreeScrollPane = null;
        modelTreeScrollPane = new JScrollPane(modelTree);

        navigatorPanel.removeAll();
        navigatorPanel.add(modelTreeScrollPane, BorderLayout.CENTER);
        navigatorPanel.validate();
        navigatorPanel.setVisible(true);

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

        /* Config default panel on the right side */
        final JPanel propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new BorderLayout());
        final TitledBorder titledBorder = BorderFactory.createTitledBorder("Properties");
        propertiesPanel.setBorder(titledBorder);
        horizontalSplitPane.setRightComponent(propertiesPanel);
        horizontalSplitPane.setDividerLocation(400);

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

    /***************************************************************************
     * Inner classes
     **************************************************************************/

    private void setPanels()
    {

        /* Navigator panel */
        navigatorPanel = new JPanel();
        navigatorPanel.setLayout(new BorderLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Navigator");
        navigatorPanel.setBorder(titledBorder);

        /* Config panel */
        final JPanel propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new BorderLayout());
        titledBorder = BorderFactory.createTitledBorder("Properties");
        propertiesPanel.setBorder(titledBorder);

        /* Horizontal panel */
        horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigatorPanel, propertiesPanel);
        horizontalSplitPane.setOneTouchExpandable(true);
        horizontalSplitPane.setDividerLocation(400);

        /* Content pane */
        setLayout(new BorderLayout());
        this.add(horizontalSplitPane, BorderLayout.CENTER);

    }

    /**
     * Create ACPT menus and menu items.
     */
    private void setPopupMenus()
    {

        /******************** Group popup menu *************************/

        groupPopupMenu = new JPopupMenu();

        addChildMenuItem = new JMenuItem("Add");
        addChildMenuItem.addActionListener(modelPanelActionAdapter);
        addChildMenuItem.setActionCommand("Add Child Node");

        deleteMenuItem = new JMenuItem("Remove");
        deleteMenuItem.addActionListener(modelPanelActionAdapter);
        deleteMenuItem.setActionCommand("Remove Node");

        deleteChildrenMenuItem = new JMenuItem("Remove Contents");
        deleteChildrenMenuItem.addActionListener(modelPanelActionAdapter);
        deleteChildrenMenuItem.setActionCommand("Remove Children Nodes");

        groupPopupMenu.add(addChildMenuItem);
        groupPopupMenu.addSeparator();
        groupPopupMenu.add(deleteMenuItem);
        groupPopupMenu.addSeparator();
        groupPopupMenu.add(deleteChildrenMenuItem);

    }

    public void setTree()
    {

        modelTree = new JTree(modelTreeModel);
        modelTree.setModel(modelTreeModel);
        modelTree.addMouseListener(modelPanelMouseAdapter);
        modelTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        modelTree.addTreeSelectionListener(modelPanelTreeSelectionAdapter);

        createModelTree();

    }

}
