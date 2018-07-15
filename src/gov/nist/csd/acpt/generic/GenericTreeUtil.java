package gov.nist.csd.acpt.generic;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements target tree utility functions.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 6.0
 */
public class GenericTreeUtil
{

    public static DefaultTreeModel createNewModelTreeModel(String projectType)
    {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new GenericInfo(GenericInfo.ROOT, projectType));

        /*
         * DefaultMutableTreeNode genericnode = null;
         *
         * if (projectType.equals(GenericInfo.MODELCOMBINE)){ genericnode = new
         * DefaultMutableTreeNode( new GenericInfo(GenericInfo.MODELCOMBINE,
         * GenericInfo.MODELCOMBINE)); } else if
         * (projectType.equals(GenericInfo.NUSMV)){ genericnode = new
         * DefaultMutableTreeNode( new GenericInfo(GenericInfo.NUSMV,
         * GenericInfo.NUSMV)); } else if
         * (projectType.equals(GenericInfo.TEST)){ genericnode = new
         * DefaultMutableTreeNode( new GenericInfo(GenericInfo.TEST,
         * GenericInfo.TEST)); } else if
         * (projectType.equals(GenericInfo.XACML)){ genericnode = new
         * DefaultMutableTreeNode( new GenericInfo(GenericInfo.XACML,
         * GenericInfo.XACML)); } else if
         * (projectType.equals(GenericInfo.PROPERTY)){ genericnode = new
         * DefaultMutableTreeNode( new GenericInfo(GenericInfo.PROPERTY,
         * GenericInfo.PROPERTY)); }
         *
         * root.add(genericnode);
         */

        return new DefaultTreeModel(root);

    }

    public static DefaultTreeModel createSampleModelTreeModel(String projectType)
    {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new GenericInfo(GenericInfo.ROOT, projectType));

        if (projectType.equals(GenericInfo.MODELCOMBINE))
        {
            DefaultMutableTreeNode node1 =
                    new DefaultMutableTreeNode(new GenericInfo(GenericInfo.MODELCOMBINE, "ABAC#Univ"));
            DefaultMutableTreeNode node2 =
                    new DefaultMutableTreeNode(new GenericInfo(GenericInfo.MODELCOMBINE, "ABAC#School"));
            DefaultMutableTreeNode node3 =
                    new DefaultMutableTreeNode(new GenericInfo(GenericInfo.MODELCOMBINE, "MULTILEVEL#Grad_School"));
            DefaultMutableTreeNode node4 =
                    new DefaultMutableTreeNode(new GenericInfo(GenericInfo.MODELCOMBINE, "WORKFLOW#BANK"));

            root.add(node1);
            root.add(node2);
            root.add(node3);
            root.add(node4);
        }
        else if (projectType.equals(GenericInfo.PROPERTY))
        {
            DefaultMutableTreeNode node1 = new DefaultMutableTreeNode(new GenericInfo(GenericInfo.PROPERTY,
                    "SPEC (Roles = Faculty)&(ResourceClass = ExternalGrades)&(ActionClass = Write) ->  decision = Permit    [Roles, ResourceClass, ActionClass]"));
            DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(new GenericInfo(GenericInfo.PROPERTY,
                    "SPEC ( Roles = Student)&(ResourceClass = InternalGrades)&(ActionClass = Write) ->  decision = Permit    [Roles, ResourceClass, ActionClass]"));
            // DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(
            // new GenericInfo(GenericInfo.PROPERTY, "SPEC (role_subject =
            // Manager)&(resource = InternalGrade)&(action = write) -> decision
            // = Deny"));
            // DefaultMutableTreeNode node4 = new DefaultMutableTreeNode(
            // new GenericInfo(GenericInfo.PROPERTY, "LTLSPEC
            // (state1=3)&(role_subject = Accountant)&(resource = order)&(action
            // = pay) -> decision = Permit"));
            //
            //
            //
            //
            DefaultMutableTreeNode node5 = new DefaultMutableTreeNode(new GenericInfo(GenericInfo.PROPERTY,
                    "SPEC (Roles = Faculty)&(Roles:Univ:USA = Student:Faculty:Univ)&(ResourceClass = InternalGrades)&(ActionClass = Write)  -> decision =  Permit   [Roles, Roles:Univ:USA, ResourceClass, ActionClass]"));

            /*
             * DefaultMutableTreeNode node1 = new DefaultMutableTreeNode( new
             * GenericInfo(GenericInfo.PROPERTY,
             * "SPEC (role_subject = Faculty)&(resource = ExternalGrade)&(action = write) -> decision = Permit"
             * )); DefaultMutableTreeNode node2 = new DefaultMutableTreeNode(
             * new GenericInfo(GenericInfo.PROPERTY,
             * "SPEC (role_subject = Student)&(resource = InternalGrade)&(action = grade) -> decision = Deny"
             * )); DefaultMutableTreeNode node3 = new DefaultMutableTreeNode(
             * new GenericInfo(GenericInfo.PROPERTY,
             * "SPEC (role_subject = Manager)&(resource = InternalGrade)&(action = write) -> decision = Deny"
             * )); DefaultMutableTreeNode node4 = new DefaultMutableTreeNode(
             * new GenericInfo(GenericInfo.PROPERTY,
             * "LTLSPEC (state1=3)&(role_subject = Accountant)&(resource = order)&(action = pay) -> decision = Permit"
             * ));
             *
             */
            root.add(node1);
            root.add(node2);
            // root.add(node3);
            // root.add(node4);
            root.add(node5);
        }

        return new DefaultTreeModel(root);

    }

    public static DefaultMutableTreeNode getRootNode(JTree jtree)
    {

        DefaultTreeModel targetTreeModel = (DefaultTreeModel) jtree.getModel();

        return (DefaultMutableTreeNode) targetTreeModel.getRoot();
    }

    public static DefaultMutableTreeNode getCurrentNode(JTree modelTree)
    {

        return (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

    }

    // check only type matching (e.g., Combination)
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
            GenericInfo target = (GenericInfo) node.getUserObject();
            if ((node.getLevel() == NodeLevel) && target.getType().equals(TargetInfoType))
            {
                return node;
            }
        }

        return null;

    }

    public static DefaultMutableTreeNode getFirstMatchingNode(JTree jtree, String TargetValue, String TargetInfoType,
            int NodeLevel)
    {

        DefaultTreeModel targetTreeModel = (DefaultTreeModel) jtree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) targetTreeModel.getRoot();
        DefaultMutableTreeNode node = null;

        Enumeration enumeration = root.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            GenericInfo target = (GenericInfo) node.getUserObject();
            if ((node.getLevel() == NodeLevel) && target.getValue().equals(TargetValue)
                    && target.getType().equals(TargetInfoType))
            {
                return node;
            }
        }

        return null;

    }

    public static List<DefaultMutableTreeNode> getTargetTreeChildrenNode2List(DefaultMutableTreeNode AncestoNode,
            String TargetInfoType, int NodeLevel)
    {

        List<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();

        DefaultMutableTreeNode node = null;

        Enumeration enumeration = AncestoNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            GenericInfo target = (GenericInfo) node.getUserObject();
            if ((node.getLevel() == NodeLevel) && target.getType().equals(TargetInfoType))
            {
                list.add(node);
            }
        }
        return list;
    }

    public static GenericInfo getCurrentModel(JTree modelTree)
    {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

        if (node == null)
        {
            // Nothing is selected.
            return null;
        }
        else
        {
            Object nodeInfo = node.getUserObject();
            return (GenericInfo) nodeInfo;

        }

    }

    public static boolean addChildNode(String targetType, String value, JTree targetTree, DefaultMutableTreeNode pNode)
    {

        DefaultMutableTreeNode currentNode = pNode;

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to add null node");
        }

        Enumeration enumeration = pNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            DefaultMutableTreeNode dupChknode = (DefaultMutableTreeNode) enumeration.nextElement();
            GenericInfo target = (GenericInfo) dupChknode.getUserObject();
            if (target.getType().equals(targetType) && target.getValue().equals(value))
            {
                // duplication check
                GraphicsUtil.showWarningDialog("Same content already exists.", "Duplication Warning", null);
                return false;
            }
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new GenericInfo(targetType, value));

        currentNode.add(newNode);

        ((DefaultTreeModel) targetTree.getModel()).reload(currentNode);

        return true;
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
            GenericInfo target = (GenericInfo) currentNode.getUserObject();
            if (target.getType().equals(targetType) && target.getValue().equals(value))
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

            // System.err.println(node.getUserObject().toString());
            GenericInfo target = (GenericInfo) node.getUserObject();
            if ((node.getLevel() == NodeLevel) && target.getType().equals(TargetInfoType))
            {
                list.add(target);
            }
        }
        return list;
    }

    public static void addNode(String targetType, String value, JTree modelTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to add null node");
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new GenericInfo(targetType, value));

        currentNode.add(newNode);

        ((DefaultTreeModel) modelTree.getModel()).reload(currentNode);

    }

    public static boolean swapChildNode(int firstIndex, int secondIndex, JTree combineTree,
            DefaultMutableTreeNode targetParentNode)
    {

        if (firstIndex == secondIndex)
        {
            return false;
        }
        if (firstIndex > secondIndex)
        {
            int temp = firstIndex;
            firstIndex = secondIndex;
            secondIndex = temp;
        }

        DefaultMutableTreeNode firstNode = (DefaultMutableTreeNode) targetParentNode.getChildAt(firstIndex);
        DefaultMutableTreeNode secondNode = (DefaultMutableTreeNode) targetParentNode.getChildAt(secondIndex);
        targetParentNode.remove(firstIndex);
        targetParentNode.remove(secondIndex - 1);
        targetParentNode.insert(secondNode, firstIndex);
        targetParentNode.insert(firstNode, secondIndex);

        ((DefaultTreeModel) combineTree.getModel()).reload(targetParentNode);
        return true;
    }

    public static void addGrandChildNode(String targetType, String value, JTree modelTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to add null node");
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new GenericInfo(targetType, value));

        // currentNode.add(newNode);

        boolean isAdded = false;
        DefaultMutableTreeNode node = null;
        DefaultMutableTreeNode ruleNode = null;

        Enumeration enumeration = currentNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            GenericInfo target = (GenericInfo) node.getUserObject();
            if (target.getType().equals(targetType))
            {
                // duplication check
                Enumeration enumerationOfrules = node.breadthFirstEnumeration();
                while (enumeration.hasMoreElements())
                {
                    ruleNode = (DefaultMutableTreeNode) enumeration.nextElement();
                    GenericInfo ruleTarget = (GenericInfo) ruleNode.getUserObject();
                    if (ruleTarget.getType().equals(targetType) && ruleTarget.getValue().equals(value))
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
         * if (targetType.equals(GenericInfo.RBACRule)||
         * targetType.equals(GenericInfo.ABACRule)||
         * targetType.equals(GenericInfo.MULTILEVELRule)||
         * targetType.equals(GenericInfo.WORKFLOWRule)){ SubElementRoot = new
         * DefaultMutableTreeNode(new GenericInfo(targetType, "Rules")); } else
         * if (targetType.equals(GenericInfo.MULTILEVELSUBJECTLEVELS)){
         * SubElementRoot = new DefaultMutableTreeNode(new
         * GenericInfo(targetType, "Subject Levels")); } else if
         * (targetType.equals(GenericInfo.MULTILEVELRESOURCELEVELS)){
         * SubElementRoot = new DefaultMutableTreeNode(new
         * GenericInfo(targetType, "Resource Levels")); }
         * currentNode.add(SubElementRoot); SubElementRoot.add(newNode);
         * ((DefaultTreeModel)modelTree.getModel()).reload(currentNode); isAdded
         * = true; }
         */
        DefaultMutableTreeNode pNode = (DefaultMutableTreeNode) newNode.getParent();
        ((DefaultTreeModel) modelTree.getModel()).reload(pNode);

    }

    public static boolean removeGrandChildNode(String targetType, String value, JTree modelTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to remove null node");
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new GenericInfo(targetType, value));

        DefaultMutableTreeNode node = null;
        DefaultMutableTreeNode ruleNode = null;

        Enumeration enumeration = currentNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            GenericInfo target = (GenericInfo) node.getUserObject();
            if (target.getType().equals(targetType))
            {
                // duplication check
                Enumeration enumerationOfrules = node.breadthFirstEnumeration();
                while (enumeration.hasMoreElements())
                {
                    ruleNode = (DefaultMutableTreeNode) enumeration.nextElement();
                    GenericInfo ruleTarget = (GenericInfo) ruleNode.getUserObject();
                    if (ruleTarget.getType().equals(targetType) && ruleTarget.getValue().equals(value))
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

    public static void removeNode(JTree modelTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

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

        ((DefaultTreeModel) modelTree.getModel()).reload(parentNode);
    }

    public static void removeAllChildren(JTree modelTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to delete children from null node");
        }

        if (currentNode.getChildCount() > 0)
        {

            currentNode.removeAllChildren();

        }

        ((DefaultTreeModel) modelTree.getModel()).reload(currentNode);

    }

}
