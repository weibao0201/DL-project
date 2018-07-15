package gov.nist.csd.acpt.target;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements target tree utility functions.
 *
 * @author JeeHyun Hwang
 * @version $Revision$, $Date$
 * @since 6.0
 */
public class TargetTreeUtil
{

    public static DefaultTreeModel createNewTargetTreeModel(String projectName)
    {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ROOT, projectName));

        DefaultMutableTreeNode subjects =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECTS, TargetInfo.SUBJECTS));

        // DefaultMutableTreeNode subject_attributes = new
        // DefaultMutableTreeNode(
        // new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES,
        // TargetInfo.SUBJECT_ATTRIBUTES));

        DefaultMutableTreeNode resources =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCES, TargetInfo.RESOURCES));

        // DefaultMutableTreeNode resource_attributes = new
        // DefaultMutableTreeNode(
        // new TargetInfo(TargetInfo.RESOURCE_ATTRIBUTES,
        // TargetInfo.RESOURCE_ATTRIBUTES));

        DefaultMutableTreeNode actions =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTIONS, TargetInfo.ACTIONS));

        DefaultMutableTreeNode MLSDefaultAction =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTRIBUTES, "String;MLSDefaultAction"));

        DefaultMutableTreeNode MLSDefaultAction1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTR_VALUSES, "String;read"));

        DefaultMutableTreeNode MLSDefaultAction2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTR_VALUSES, "String;write"));

        DefaultMutableTreeNode enviromnments =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENTS, TargetInfo.ENVIRONMENTS));

        /* My Edit */
        DefaultMutableTreeNode inheritance =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.INHERITANCE, TargetInfo.INHERITANCE));
        /* End of edit */

        root.add(subjects);
        // root.add(subject_attributes);
        root.add(resources);
        // root.add(resource_attributes);
        root.add(actions);
        root.add(enviromnments);

        /* My Edit */
        root.add(inheritance);
        /* End of Edit */

        actions.add(MLSDefaultAction);
        MLSDefaultAction.add(MLSDefaultAction1);
        MLSDefaultAction.add(MLSDefaultAction2);

        return new DefaultTreeModel(root);

    }

    // public static DefaultTreeModel createSampleTargetTreeModel() {
    //
    // return null;
    //
    // }

    public static DefaultTreeModel createSampleModelTreeModel(String projectName)
    {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ROOT, projectName));

        DefaultMutableTreeNode subjects =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECTS, TargetInfo.SUBJECTS));

        DefaultMutableTreeNode roles =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, "String;Roles"));

        DefaultMutableTreeNode roleValue1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;Faculty"));

        DefaultMutableTreeNode roleValue2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;Student"));

        DefaultMutableTreeNode roleValue3 = new DefaultMutableTreeNode(
                new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;Student:Faculty:Univ"));

        DefaultMutableTreeNode roles2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, "String;Roles:Univ:USA"));

        DefaultMutableTreeNode roles2Value1 = new DefaultMutableTreeNode(
                new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;Student:Faculty:Univ"));

        DefaultMutableTreeNode roles2Value2 = new DefaultMutableTreeNode(
                new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;Security:Faculty:Univ"));

        DefaultMutableTreeNode Companyroles =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, "String;CompanyRoles"));

        DefaultMutableTreeNode CompanyroleValue1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;Manager"));

        DefaultMutableTreeNode CompanyroleValue2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;Secratery"));

        DefaultMutableTreeNode CompanyroleValue3 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;Accountant"));

        DefaultMutableTreeNode locations =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, "String;Locations"));

        DefaultMutableTreeNode locationValue1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;London"));

        DefaultMutableTreeNode locationValue2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;NY"));

        DefaultMutableTreeNode users =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, "String;Users"));

        DefaultMutableTreeNode userValue1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;Jim"));

        DefaultMutableTreeNode userValue2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "String;Jane"));

        DefaultMutableTreeNode subjMLS1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, "Integer;MLS_subj1"));

        DefaultMutableTreeNode subjMLS1_1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "Integer;5"));

        DefaultMutableTreeNode subjMLS2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, "Integer;MLS_subj2"));

        DefaultMutableTreeNode subjMLS2_1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "Integer;3"));

        DefaultMutableTreeNode subjMLS2_2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTR_VALUSES, "Integer;6"));

        subjects.add(subjMLS1);
        subjMLS1.add(subjMLS1_1);
        subjects.add(subjMLS2);
        subjMLS2.add(subjMLS2_1);
        // subjMLS2.add(subjMLS2_2);

        subjects.add(roles);
        roles.add(roleValue1);
        roles.add(roleValue2);
        roles.add(roleValue3);

        subjects.add(roles2);
        roles2.add(roles2Value1);
        roles2.add(roles2Value2);

        subjects.add(Companyroles);
        Companyroles.add(CompanyroleValue1);
        Companyroles.add(CompanyroleValue2);
        Companyroles.add(CompanyroleValue3);

        subjects.add(users);
        users.add(userValue1);
        users.add(userValue2);
        subjects.add(locations);
        locations.add(locationValue1);
        locations.add(locationValue2);

        // DefaultMutableTreeNode subject_attributes = new
        // DefaultMutableTreeNode(
        // new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES,
        // TargetInfo.SUBJECT_ATTRIBUTES));

        DefaultMutableTreeNode resources =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCES, TargetInfo.RESOURCES));

        DefaultMutableTreeNode resClass =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTRIBUTES, "String;ResourceClass"));

        DefaultMutableTreeNode resClass1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTR_VALUSES, "String;InternalGrades"));

        DefaultMutableTreeNode resClass2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTR_VALUSES, "String;ExternalGrades"));

        DefaultMutableTreeNode resClass3 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTR_VALUSES, "String;Order"));

        DefaultMutableTreeNode resCritera =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTRIBUTES, "Boolean;IsConflict"));

        DefaultMutableTreeNode resCritera1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTR_VALUSES, "Boolean;True"));

        DefaultMutableTreeNode resCritera2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTR_VALUSES, "Boolean;False"));

        DefaultMutableTreeNode resMLS1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTRIBUTES, "Integer;MLS_res1"));

        DefaultMutableTreeNode resMLS1_1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTR_VALUSES, "Integer;7"));

        DefaultMutableTreeNode resMLS2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTRIBUTES, "Integer;MLS_res2"));

        DefaultMutableTreeNode resMLS2_1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTR_VALUSES, "Integer;4"));

        DefaultMutableTreeNode resMLS2_2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.RESOURCE_ATTR_VALUSES, "Integer;8"));

        resources.add(resMLS1);
        resMLS1.add(resMLS1_1);
        resources.add(resMLS2);
        resMLS2.add(resMLS2_1);
        resMLS2.add(resMLS2_2);

        resources.add(resClass);
        resClass.add(resClass1);
        resClass.add(resClass2);
        resClass.add(resClass3);
        resources.add(resCritera);
        resCritera.add(resCritera1);
        resCritera.add(resCritera2);

        // DefaultMutableTreeNode dummy1 = new DefaultMutableTreeNode(
        // new TargetInfo(TargetInfo.RESOURCE_ATTRIBUTES,
        // "Boolean;DumResAttr"));
        //
        //
        //
        // DefaultMutableTreeNode dummy2 = new DefaultMutableTreeNode(
        // new TargetInfo(TargetInfo.RESOURCE_ATTR_VALUSES,
        // "Boolean;DumResAttr2"));
        //
        // resources.add(dummy1);
        // resources.add(dummy2);
        //
        // DefaultMutableTreeNode dummy3 = new DefaultMutableTreeNode(
        // new TargetInfo(TargetInfo.RESOURCE_ATTR_VALUSES, "Boolean;False"));
        //
        // dummy1.add(dummy3);

        // DefaultMutableTreeNode resource_attributes = new
        // DefaultMutableTreeNode(
        // new TargetInfo(TargetInfo.RESOURCE_ATTRIBUTES,
        // TargetInfo.RESOURCE_ATTRIBUTES));

        DefaultMutableTreeNode actions =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTIONS, TargetInfo.ACTIONS));

        DefaultMutableTreeNode MLSDefaultAction =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTRIBUTES, "String;MLSDefaultAction"));

        DefaultMutableTreeNode MLSDefaultAction1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTR_VALUSES, "String;read"));

        DefaultMutableTreeNode MLSDefaultAction2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTR_VALUSES, "String;write"));

        DefaultMutableTreeNode actClass =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTRIBUTES, "String;ActionClass"));

        DefaultMutableTreeNode actClass1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTR_VALUSES, "String;Write"));

        DefaultMutableTreeNode actClass2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTR_VALUSES, "String;Read"));

        DefaultMutableTreeNode actClass3 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTR_VALUSES, "String;Access"));

        DefaultMutableTreeNode actClass4 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTR_VALUSES, "String;Pay"));

        DefaultMutableTreeNode actCritera =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTRIBUTES, "String;ActionCode"));

        DefaultMutableTreeNode actCritera1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTR_VALUSES, "String;access"));

        DefaultMutableTreeNode actCritera2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ACTION_ATTR_VALUSES, "String;modify"));

        actions.add(MLSDefaultAction);
        MLSDefaultAction.add(MLSDefaultAction1);
        MLSDefaultAction.add(MLSDefaultAction2);
        actions.add(actClass);
        actClass.add(actClass1);
        actClass.add(actClass2);
        actClass.add(actClass3);
        actClass.add(actClass4);
        actions.add(actCritera);
        actCritera.add(actCritera1);
        actCritera.add(actCritera2);

        DefaultMutableTreeNode enviromnments =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENTS, TargetInfo.ENVIRONMENTS));

        DefaultMutableTreeNode env2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENT_ATTRIBUTES, "Integer;code1"));

        DefaultMutableTreeNode env2_1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENT_ATTR_VALUSES, "Integer;4"));

        DefaultMutableTreeNode env2_2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENT_ATTR_VALUSES, "Integer;8"));

        DefaultMutableTreeNode env3 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENT_ATTRIBUTES, "Integer;code2"));

        DefaultMutableTreeNode env3_1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENT_ATTR_VALUSES, "Integer;1"));

        DefaultMutableTreeNode env3_2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENT_ATTR_VALUSES, "Integer;3"));

        DefaultMutableTreeNode env4 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENT_ATTRIBUTES, "String;clearance"));

        DefaultMutableTreeNode env4_1 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENT_ATTR_VALUSES, "String;outstanding"));

        DefaultMutableTreeNode env4_2 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENT_ATTR_VALUSES, "String;good"));

        DefaultMutableTreeNode env4_3 =
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.ENVIRONMENT_ATTR_VALUSES, "String;okay"));

        env2.add(env2_1);
        env2.add(env2_2);
        env3.add(env3_1);
        env3.add(env3_2);
        env4.add(env4_1);
        env4.add(env4_2);
        env4.add(env4_3);
        enviromnments.add(env2);
        enviromnments.add(env3);
        enviromnments.add(env4);

        root.add(subjects);
        // root.add(subject_attributes);
        root.add(resources);
        // root.add(resource_attributes);
        root.add(actions);
        root.add(enviromnments);

        return new DefaultTreeModel(root);

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
            TargetInfo target = (TargetInfo) node.getUserObject();
            // System.err.println("Given:"+NodeLevel+TargetValue+":"+TargetInfoType);
            // System.err.println("In the
            // tree:"+node.getLevel()+target.getvalueOfvalue()+":"+target.getTargetType());
            if ((node.getLevel() == NodeLevel) && target.getvalueOfvalue().equals(TargetValue)
                    && target.getTargetType().equals(TargetInfoType))
            {

                // System.err.println("FOUND:"+target.getvalueOfvalue()+":"+target.getTargetType());
                return node;
            }
        }

        return null;

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

    public static List<TargetInfo> getTargetTreeChildrenValues2List(DefaultMutableTreeNode AncestoNode,
            String TargetInfoType, int NodeLevel)
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

    public static List<DefaultMutableTreeNode> getTargetTreeChildrenNode2List(DefaultMutableTreeNode AncestoNode,
            String TargetInfoType, int NodeLevel)
    {

        List<DefaultMutableTreeNode> list = new ArrayList<DefaultMutableTreeNode>();

        DefaultMutableTreeNode node = null;

        // if (AncestoNode == null){return null;}

        Enumeration enumeration = AncestoNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            TargetInfo target = (TargetInfo) node.getUserObject();
            if ((node.getLevel() == NodeLevel) && target.getTargetType().equals(TargetInfoType))
            {
                list.add(node);
            }
        }
        return list;
    }

    public static DefaultMutableTreeNode getCurrentNode(JTree targetTree)
    {

        return (DefaultMutableTreeNode) targetTree.getLastSelectedPathComponent();

    }

    public static TargetInfo getCurrentTarget(JTree targetTree)
    {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) targetTree.getLastSelectedPathComponent();

        if (node == null)
        {
            // Nothing is selected.
            return null;
        }
        else
        {
            Object nodeInfo = node.getUserObject();
            return (TargetInfo) nodeInfo;

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
