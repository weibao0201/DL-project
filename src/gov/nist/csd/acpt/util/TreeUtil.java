package gov.nist.csd.acpt.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.target.TargetInfo;

/**
 * This class implements target tree utility functions.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 6.0
 */
public class TreeUtil
{

    public static DefaultTreeModel createNewTargetTreeModel(String projectName)
    {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ROOT, projectName));

        DefaultMutableTreeNode subjects =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECTS, TargetInfo.SUBJECTS));

        DefaultMutableTreeNode subject_attributes = new DefaultMutableTreeNode(
                new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, TargetInfo.SUBJECT_ATTRIBUTES));

        DefaultMutableTreeNode resources =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCES, TargetInfo.RESOURCES));

        DefaultMutableTreeNode resource_attributes = new DefaultMutableTreeNode(
                new TargetInfo(TargetInfo.RESOURCE_ATTRIBUTES, TargetInfo.RESOURCE_ATTRIBUTES));

        DefaultMutableTreeNode actions =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTIONS, TargetInfo.ACTIONS));

        DefaultMutableTreeNode enviromnments =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENTS, TargetInfo.ENVIRONMENTS));

        DefaultMutableTreeNode enviromnment_attributes = new DefaultMutableTreeNode(
                new TargetInfo(TargetInfo.ENVIRONMENT_ATTRIBUTES, TargetInfo.ENVIRONMENT_ATTRIBUTES));

        root.add(subjects);
        root.add(subject_attributes);
        root.add(resources);
        root.add(resource_attributes);
        root.add(actions);
        root.add(enviromnments);
        root.add(enviromnment_attributes);

        return new DefaultTreeModel(root);

    }

    public static DefaultTreeModel createSampleTargetTreeModel()
    {

        return null;

    }

    /*
     * public static DefaultMutableTreeNode getFirstMatchChildNodeFromRoot(JTree
     * jtree, String targetType, String value) {
     *
     * DefaultTreeModel targetTreeModel = (DefaultTreeModel) jtree.getModel();
     * return getFirstMatchChildNode((DefaultMutableTreeNode)
     * targetTreeModel.getRoot(), targetType, value); }
     *
     *
     * public static DefaultMutableTreeNode
     * getFirstMatchChildNode(DefaultMutableTreeNode Pnode, String targetType,
     * String value) {
     *
     * DefaultMutableTreeNode node = null;
     *
     * Enumeration enumeration = Pnode.children();
     *
     * while(enumeration.hasMoreElements()) { //get the node node =
     * (DefaultMutableTreeNode)enumeration.nextElement();
     *
     * TargetInfo target = (TargetInfo) node.getUserObject();
     *
     * if (target.getTargetType().equals(targetType) &&
     * target.getValue().equals(value)){ return node; } }
     *
     * //tree node with string node found return null return null;
     *
     * }
     *
     * public static List getChildrenValue2List (DefaultMutableTreeNode Pnode) {
     *
     * List list = new ArrayList();
     *
     * DefaultMutableTreeNode node = null;
     *
     * Enumeration enumeration = Pnode.children();
     *
     * while(enumeration.hasMoreElements()) { //get the node node =
     * (DefaultMutableTreeNode)enumeration.nextElement(); TargetInfo target =
     * (TargetInfo) node.getUserObject(); list.add(target); } return list; }
     *
     * public static String[] getChildrenValue2StrArray (DefaultMutableTreeNode
     * Pnode) {
     *
     * List list = getChildrenValue2List (Pnode);
     *
     *
     * if (list.isEmpty()) return (new String[]{});
     *
     *
     * String[] strArray = new String[list.size()];
     *
     * for (int i = 0; i < strArray.length; i++) { strArray[i] =
     * list.get(i).toString(); }
     *
     *
     * return strArray; }
     */

    public static DefaultMutableTreeNode getRootNode(JTree jtree)
    {

        DefaultTreeModel targetTreeModel = (DefaultTreeModel) jtree.getModel();

        return (DefaultMutableTreeNode) targetTreeModel.getRoot();
    }

    public static DefaultMutableTreeNode getFirstMatchingNode(JTree jtree, String TargetInfoType, int NodeLevel)
    {

        DefaultTreeModel targetTreeModel = (DefaultTreeModel) jtree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) targetTreeModel.getRoot();
        DefaultMutableTreeNode node = null;

        Enumeration enumeration = root.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            TargetInfo target = (TargetInfo) node.getUserObject();
            if ((node.getLevel() == NodeLevel) && target.getTargetType().equals(TargetInfoType))
            {
                return node;
            }
        }

        return null;

    }

    public static String[] getTargetTreeChildrenValues2StrArr(DefaultMutableTreeNode AncestoNode, String TargetInfoType,
            int NodeLevel)
    {

        List list = getTargetTreeChildrenValues2List(AncestoNode, TargetInfoType, NodeLevel);

        if (list.isEmpty())
        {
            return (new String[] {});
        }

        String[] strArray = new String[list.size()];

        for (int i = 0; i < strArray.length; i++)
        {
            strArray[i] = list.get(i).toString();
        }

        return strArray;
    }

    public static List getTargetTreeChildrenValues2List(DefaultMutableTreeNode AncestoNode, String TargetInfoType,
            int NodeLevel)
    {

        List list = new ArrayList();

        DefaultMutableTreeNode node = null;

        Enumeration enumeration = AncestoNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            TargetInfo target = (TargetInfo) node.getUserObject();
            if ((node.getLevel() == NodeLevel) && target.getTargetType().equals(TargetInfoType))
            {
                list.add(target);
            }
        }
        return list;
    }

    public static DefaultMutableTreeNode getCurrentNode(JTree jtree)
    {

        return (DefaultMutableTreeNode) jtree.getLastSelectedPathComponent();

    }

    public static Object getCurrentTarget(JTree jtree)
    {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jtree.getLastSelectedPathComponent();

        if (node == null)
        {
            // Nothing is selected.
            return null;
        }
        else
        {
            Object nodeInfo = node.getUserObject();
            return nodeInfo;

        }

    }

    public static void addGrandChildNode(String targetType, String value, JTree targetTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) targetTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {
            currentNode = getRootNode(targetTree);
        }

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to add null node");
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new TargetInfo(targetType, value));

        // currentNode.add(newNode);

        boolean isAdded = false;
        DefaultMutableTreeNode node = null;
        DefaultMutableTreeNode ruleNode = null;

        Enumeration enumeration = currentNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            TargetInfo target = (TargetInfo) node.getUserObject();
            if (target.getTargetType().equals(targetType))
            {
                // duplication check
                Enumeration enumerationOfrules = node.breadthFirstEnumeration();
                while (enumeration.hasMoreElements())
                {
                    ruleNode = (DefaultMutableTreeNode) enumeration.nextElement();
                    TargetInfo ruleTarget = (TargetInfo) ruleNode.getUserObject();
                    if (ruleTarget.getTargetType().equals(targetType) && ruleTarget.getValue().equals(value))
                    {
                        // Warning
                        GraphicsUtil.showWarningDialog("Same rule already exists.", "Duplication Warning", null);
                        return;
                    }
                }
                // if no duplication, add a node
                isAdded = true;
                node.add(newNode);
                break;
            }
        }
        /*
         * // if rule container node (parent node for new node) does not exist
         * if (isAdded == false){ DefaultMutableTreeNode SubElementRoot = null;
         * if (targetType.equals(ModelInfo.RBACRule)||
         * targetType.equals(ModelInfo.ABACRule)||
         * targetType.equals(ModelInfo.MULTILEVELRule)||
         * targetType.equals(ModelInfo.WORKFLOWRule)){ SubElementRoot = new
         * DefaultMutableTreeNode(new ModelInfo(targetType, "Rules")); } else if
         * (targetType.equals(ModelInfo.MULTILEVELSUBJECTLEVELS)){
         * SubElementRoot = new DefaultMutableTreeNode(new ModelInfo(targetType,
         * "Subject Levels")); } else if
         * (targetType.equals(ModelInfo.MULTILEVELRESOURCELEVELS)){
         * SubElementRoot = new DefaultMutableTreeNode(new TarInfo(targetType,
         * "Resource Levels")); } currentNode.add(SubElementRoot);
         * SubElementRoot.add(newNode);
         * ((DefaultTreeModel)modelTree.getModel()).reload(currentNode); isAdded
         * = true; }
         */
        DefaultMutableTreeNode pNode = (DefaultMutableTreeNode) newNode.getParent();
        ((DefaultTreeModel) targetTree.getModel()).reload(pNode);

    }

    public static boolean removeGrandChildNode(String targetType, String value, JTree modelTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to remove null node");
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new ModelInfo(targetType, value));

        DefaultMutableTreeNode node = null;
        DefaultMutableTreeNode ruleNode = null;

        Enumeration enumeration = currentNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            ModelInfo target = (ModelInfo) node.getUserObject();
            if (target.getModelType().equals(targetType))
            {
                // duplication check
                Enumeration enumerationOfrules = node.breadthFirstEnumeration();
                while (enumeration.hasMoreElements())
                {
                    ruleNode = (DefaultMutableTreeNode) enumeration.nextElement();
                    ModelInfo ruleTarget = (ModelInfo) ruleNode.getUserObject();
                    if (ruleTarget.getModelType().equals(targetType) && ruleTarget.getValue().equals(value))
                    {
                        // Warning
                        // GraphicsUtil
                        // .showWarningDialog(
                        // "Same rule already exists.",
                        // "Duplication Warning", null);
                        node.remove(ruleNode);
                        ((DefaultTreeModel) modelTree.getModel()).reload(node);
                        return true;
                    }
                }
            }
        }

        // Warning
        GraphicsUtil.showWarningDialog("Such rule does not exist.", "Deletion Warning", null);
        return false;

    }

    public static void addChildNode(String targetType, String value, JTree targetTree, DefaultMutableTreeNode pNode)
    {

        DefaultMutableTreeNode currentNode = pNode;

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to add null node");
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new TargetInfo(targetType, value));

        currentNode.add(newNode);

        ((DefaultTreeModel) targetTree.getModel()).reload(currentNode);

    }

    public static void removeChildNode(String targetType, String value, JTree targetTree, DefaultMutableTreeNode pNode)
    {

        DefaultMutableTreeNode currentNode = null;
        DefaultMutableTreeNode ruleNode = null;

        Enumeration enumeration = pNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            currentNode = (DefaultMutableTreeNode) enumeration.nextElement();
            TargetInfo target = (TargetInfo) currentNode.getUserObject();
            if (target.getTargetType().equals(targetType) && target.getValue().equals(value))
            {

                if (currentNode == null)
                {

                    System.err.println("WARNING: TreeUtil: trying to delete null node");
                }

                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());

                if (parentNode == null)
                {

                    System.err.println("WARNING: TreeUtil: trying to delete from null parent node");
                }

                parentNode.remove(currentNode);

                ((DefaultTreeModel) targetTree.getModel()).reload(parentNode);

            }
        }
    }

    public static void updateChildNode(String targetType, String value, String newvalue, JTree targetTree,
            DefaultMutableTreeNode pNode)
    {

        DefaultMutableTreeNode currentNode = null;

        Enumeration enumeration = pNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            currentNode = (DefaultMutableTreeNode) enumeration.nextElement();
            TargetInfo target = (TargetInfo) currentNode.getUserObject();
            if (target.getTargetType().equals(targetType) && target.getValue().equals(value))
            {

                if (currentNode == null)
                {

                    System.err.println("WARNING: TreeUtil: trying to delete null node");
                }

                target.setValue(newvalue);

                DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());

                if (parentNode == null)
                {

                    System.err.println("WARNING: TreeUtil: trying to delete from null parent node");
                }

                ((DefaultTreeModel) targetTree.getModel()).reload(parentNode);

            }
        }
    }

    public static void addNode(String targetType, String value, JTree targetTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) targetTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to add null node");
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new TargetInfo(targetType, value));

        currentNode.add(newNode);

        ((DefaultTreeModel) targetTree.getModel()).reload(currentNode);

    }

    public static void removeNode(JTree targetTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) targetTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to delete null node");
        }

        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (currentNode.getParent());

        if (parentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to delete from null parent node");
        }

        parentNode.remove(currentNode);

        ((DefaultTreeModel) targetTree.getModel()).reload(parentNode);
    }

    public static void removeAllChildren(JTree targetTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) targetTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to delete children from null node");
        }

        if (currentNode.getChildCount() > 0)
        {

            currentNode.removeAllChildren();

        }

        ((DefaultTreeModel) targetTree.getModel()).reload(currentNode);

    }

}
