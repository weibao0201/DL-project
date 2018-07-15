package gov.nist.csd.acpt.model;

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
public class ModelTreeUtil
{

    public static DefaultTreeModel createNewModelTreeModel(String projectName)
    {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ROOT, "Model"));

        DefaultMutableTreeNode rbac = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.RBAC, ModelInfo.RBAC));

        DefaultMutableTreeNode multilevel =
                new DefaultMutableTreeNode(new ModelInfo(ModelInfo.MULTILEVEL, ModelInfo.MULTILEVEL));

        DefaultMutableTreeNode workflow =
                new DefaultMutableTreeNode(new ModelInfo(ModelInfo.WORKFLOW, ModelInfo.WORKFLOW));

        DefaultMutableTreeNode abac = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABAC, ModelInfo.ABAC));

        root.add(rbac);
        root.add(multilevel);
        root.add(workflow);
        root.add(abac);

        return new DefaultTreeModel(root);

    }

    public static DefaultTreeModel createSampleModelTreeModel()
    {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ROOT, "Model"));

        DefaultMutableTreeNode rbac = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.RBAC, ModelInfo.RBAC));

        DefaultMutableTreeNode multilevel =
                new DefaultMutableTreeNode(new ModelInfo(ModelInfo.MULTILEVEL, ModelInfo.MULTILEVEL));

        DefaultMutableTreeNode workflow =
                new DefaultMutableTreeNode(new ModelInfo(ModelInfo.WORKFLOW, ModelInfo.WORKFLOW));

        DefaultMutableTreeNode abac = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABAC, ModelInfo.ABAC));

        // root.add(rbac);
        root.add(multilevel);
        root.add(workflow);
        root.add(abac);

        /******************** RBAC Model *************************/

        DefaultMutableTreeNode rbacUniv = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABAC, "Univ"));

        abac.add(rbacUniv);

        DefaultMutableTreeNode rbacUnivRuleRoot = new DefaultMutableTreeNode(
                new ModelInfo(ModelInfo.ABACRule, "Rules: " + ModelInfo.FirstApplRuleCombinatoin));

        rbacUniv.add(rbacUnivRuleRoot);

        DefaultMutableTreeNode rbacUnivRule1 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;Roles;String;Faculty#Resource Attributes;ResourceClass;String;ExternalGrades#Action Attributes;ActionClass;String;Write->Permit"));

        DefaultMutableTreeNode newCondition0 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.XACMLRuleCondition,
                "<Condition FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:and\">"));
        rbacUnivRule1.add(newCondition0);

        DefaultMutableTreeNode rbacUnivRule2 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;Roles;String;Student#Resource Attributes;ResourceClass;String;ExternalGrades#Action Attributes;ActionClass;String;Write->Deny"));

        DefaultMutableTreeNode rbacUnivRule3 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;Roles;String;Student:Faculty:Univ#Resource Attributes;ResourceClass;String;ExternalGrades#Action Attributes;ActionClass;String;Write->Permit"));

        DefaultMutableTreeNode rbacUnivRule4 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;Roles:Univ:USA;String;Student:Faculty:Univ#Resource Attributes;ResourceClass;String;ExternalGrades#Action Attributes;ActionClass;String;Write->Permit"));

        DefaultMutableTreeNode rbacUnivRule5 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;Roles;String;Faculty#Subject Attributes;Roles:Univ:USA;String;Student:Faculty:Univ#Resource Attributes;ResourceClass;String;InternalGrades#Action Attributes;ActionClass;String;Write->Permit"));

        DefaultMutableTreeNode rbacUnivRule6 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;MLS_subj2;Integer;3#Subject Attributes;Roles:Univ:USA;String;Student:Faculty:Univ#Resource Attributes;MLS_res2;Integer;4#Action Attributes;ActionCode;String;access#Environment Attributes;code1;Integer;8#Environment Attributes;code2;Integer;1#Environment Attributes;clearance;String;outstanding->Permit"));

        DefaultMutableTreeNode rbacUnivRule7 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Resource Attributes;ResourceClass;String;ExternalGrades#Environment Attributes;code1;Integer;4#Environment Attributes;code2;Integer;3->Deny"));

        // rbacUnivRuleRoot.add(rbacUnivRule1);
        // rbacUnivRuleRoot.add(rbacUnivRule2);
        // rbacUnivRuleRoot.add(rbacUnivRule3);
        // rbacUnivRuleRoot.add(rbacUnivRule4);
        // rbacUnivRuleRoot.add(rbacUnivRule5);
        rbacUnivRuleRoot.add(rbacUnivRule6);
        rbacUnivRuleRoot.add(rbacUnivRule7);

        /******************** RBAC Model *************************/

        DefaultMutableTreeNode rbacUniv2 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABAC, "Univ2"));

        abac.add(rbacUniv2);

        DefaultMutableTreeNode rbacUnivRuleRoot2 = new DefaultMutableTreeNode(
                new ModelInfo(ModelInfo.ABACRule, "Rules: " + ModelInfo.FirstApplRuleCombinatoin));

        rbacUniv2.add(rbacUnivRuleRoot2);

        DefaultMutableTreeNode rbacUnivRule21 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;Roles;String;Faculty#Resource Attributes;ResourceClass;String;ExternalGrades#Action Attributes;ActionClass;String;Write->Deny"));

        DefaultMutableTreeNode newCondition20 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.XACMLRuleCondition,
                "<Condition FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:and\">"));
        rbacUnivRule1.add(newCondition0);

        DefaultMutableTreeNode rbacUnivRule22 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;Roles;String;Student#Resource Attributes;ResourceClass;String;ExternalGrades#Action Attributes;ActionClass;String;Write->Permit"));

        DefaultMutableTreeNode rbacUnivRule23 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;Roles;String;Student:Faculty:Univ#Resource Attributes;ResourceClass;String;ExternalGrades#Action Attributes;ActionClass;String;Write->Permit"));

        rbacUnivRuleRoot2.add(rbacUnivRule21);
        rbacUnivRuleRoot2.add(rbacUnivRule22);
        rbacUnivRuleRoot2.add(rbacUnivRule23);

        /******************** ABAC Model *************************/

        DefaultMutableTreeNode abacUniv = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABAC, "School"));

        abac.add(abacUniv);

        DefaultMutableTreeNode abacUnivRuleRoot = new DefaultMutableTreeNode(
                new ModelInfo(ModelInfo.ABACRule, "Rules: " + ModelInfo.FirstApplRuleCombinatoin));

        abacUniv.add(abacUnivRuleRoot);

        DefaultMutableTreeNode abacUnivRule1 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;Users;String;Jane#Resource Attributes;ResourceClass;String;InternalGrades#Resource Attributes;IsConflict;Boolean;True#Action Attributes;ActionClass;String;Write->Permit"));

        DefaultMutableTreeNode abacUnivRule2 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.ABACRule,
                "Subject Attributes;Users;String;Jim#Resource Attributes;ResourceClass;String;InternalGrades#Resource Attributes;IsConflict;Boolean;True#Action Attributes;ActionClass;String;Write->Deny"));

        abacUnivRuleRoot.add(abacUnivRule1);
        abacUnivRuleRoot.add(abacUnivRule2);

        /******************** WorkFlow Model *************************/

        DefaultMutableTreeNode workflowBank = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.WORKFLOW, "BANK"));

        workflow.add(workflowBank);

        DefaultMutableTreeNode workflowBankRuleRoot = new DefaultMutableTreeNode(
                new ModelInfo(ModelInfo.WORKFLOWRule, "Rules: " + ModelInfo.FirstApplRuleCombinatoin));

        workflowBank.add(workflowBankRuleRoot);

        DefaultMutableTreeNode workflowBankRule1 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.WORKFLOWRule,
                "Process_State 1#Subject Attributes;CompanyRoles;String;Manager#Resource Attributes;ResourceClass;String;Order#Action Attributes;ActionClass;String;Write->Permit and go to Next State"));
        DefaultMutableTreeNode workflowBankRule2 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.WORKFLOWRule,
                "Process_State 2#Subject Attributes;CompanyRoles;String;Secratery#Resource Attributes;ResourceClass;String;Order#Action Attributes;ActionClass;String;Write->Permit and go to Next State"));
        DefaultMutableTreeNode workflowBankRule3 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.WORKFLOWRule,
                "Process_State 3#Subject Attributes;CompanyRoles;String;Accountant#Resource Attributes;ResourceClass;String;Order#Action Attributes;ActionClass;String;Pay->Permit"));
        DefaultMutableTreeNode workflowBankRule4 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.WORKFLOWRule,
                "Process_State 1#Subject Attributes;CompanyRoles;String;Accountant#Resource Attributes;ResourceClass;String;Order#Action Attributes;ActionClass;String;Write->Deny"));

        DefaultMutableTreeNode workflowBankRule5 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.WORKFLOWRule,
                "Process_State 3#Subject Attributes;Users;String;Jane#Resource Attributes;IsConflict;Boolean;False#Environment Attributes;code1;Integer;8#Environment Attributes;code2;Integer;3->Deny"));

        DefaultMutableTreeNode workflowBankRule6 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.WORKFLOWRule,
                "Process_State 1#Subject Attributes;Roles;String;Student#Resource Attributes;MLS_res1;Integer;7#Action Attributes;ActionClass;String;Read#Environment Attributes;code2;Integer;3#Environment Attributes;clearance;String;outstanding->Permit"));

        DefaultMutableTreeNode newCondition1 = new DefaultMutableTreeNode(new ModelInfo(ModelInfo.XACMLRuleCondition,
                "<Condition FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:and\">"));
        workflowBankRule1.add(newCondition1);

        // workflowBankRuleRoot.add(workflowBankRule1);
        // workflowBankRuleRoot.add(workflowBankRule2);
        // workflowBankRuleRoot.add(workflowBankRule3);
        // workflowBankRuleRoot.add(workflowBankRule4);
        workflowBankRuleRoot.add(workflowBankRule5);
        workflowBankRuleRoot.add(workflowBankRule6);

        /******************** MultiLevel Model *************************/

        DefaultMutableTreeNode multilevelGrad =
                new DefaultMutableTreeNode(new ModelInfo(ModelInfo.MULTILEVEL, "Grad_School"));

        multilevel.add(multilevelGrad);

        DefaultMutableTreeNode multilevelSubjectLevelsRoot =
                new DefaultMutableTreeNode(new ModelInfo(ModelInfo.MULTILEVELSUBJECTLEVELS, "Subject Levels"));

        multilevelGrad.add(multilevelSubjectLevelsRoot);

        DefaultMutableTreeNode multilevelSubjectLevelsRule1 = new DefaultMutableTreeNode(
                new ModelInfo(ModelInfo.MULTILEVELSUBJECTLEVELS, "Subject Attributes;MLS_subj1;Integer#5"));
        DefaultMutableTreeNode multilevelSubjectLevelsRule2 = new DefaultMutableTreeNode(
                new ModelInfo(ModelInfo.MULTILEVELSUBJECTLEVELS, "Subject Attributes;MLS_subj2;Integer#3"));

        multilevelSubjectLevelsRoot.add(multilevelSubjectLevelsRule1);
        multilevelSubjectLevelsRoot.add(multilevelSubjectLevelsRule2);

        DefaultMutableTreeNode multilevelResourceLevelsRoot =
                new DefaultMutableTreeNode(new ModelInfo(ModelInfo.MULTILEVELRESOURCELEVELS, "Resource Levels"));

        multilevelGrad.add(multilevelResourceLevelsRoot);

        DefaultMutableTreeNode multilevelResourceLevelsRule1 = new DefaultMutableTreeNode(
                new ModelInfo(ModelInfo.MULTILEVELRESOURCELEVELS, "Resource Attributes;MLS_res1;Integer#7"));
                // DefaultMutableTreeNode multilevelResourceLevelsRule2 = new
                // DefaultMutableTreeNode(
                // new ModelInfo(ModelInfo.MULTILEVELRESOURCELEVELS, "Resource
                // Attributes;IsConflict;Boolean#2"));

        // DefaultMutableTreeNode multilevelResourceLevelsRule3 = new
        // DefaultMutableTreeNode(
        // new ModelInfo(ModelInfo.MULTILEVELRESOURCELEVELS, "Resource
        // Attributes;ResourceClass;String;Order#5"));

        multilevelResourceLevelsRoot.add(multilevelResourceLevelsRule1);
        // multilevelResourceLevelsRoot.add(multilevelResourceLevelsRule2);
        // multilevelResourceLevelsRoot.add(multilevelResourceLevelsRule3);

        DefaultMutableTreeNode multilevelGradRuleRoot = new DefaultMutableTreeNode(
                new ModelInfo(ModelInfo.MULTILEVELRule, "Rules: " + ModelInfo.FirstApplRuleCombinatoin));

        multilevelGrad.add(multilevelGradRuleRoot);

        DefaultMutableTreeNode multilevelGradRule1 =
                new DefaultMutableTreeNode(new ModelInfo(ModelInfo.MULTILEVELRule, "no read up"));
        DefaultMutableTreeNode multilevelGradRule2 =
                new DefaultMutableTreeNode(new ModelInfo(ModelInfo.MULTILEVELRule, "no write down"));

        multilevelGradRuleRoot.add(multilevelGradRule1);
        multilevelGradRuleRoot.add(multilevelGradRule2);

        return new DefaultTreeModel(root);

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
            ModelInfo target = (ModelInfo) node.getUserObject();
            if ((node.getLevel() == NodeLevel) && target.getModelType().equals(TargetInfoType))
            {
                return node;
            }
        }

        return null;

    }

    public static DefaultMutableTreeNode getFirstMatchingNodeOnTypeValue(JTree jtree, String TargetInfoType,
            String TargetValue, int NodeLevel)
    {

        DefaultTreeModel targetTreeModel = (DefaultTreeModel) jtree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) targetTreeModel.getRoot();
        DefaultMutableTreeNode node = null;

        Enumeration enumeration = root.breadthFirstEnumeration();

        // System.err.println("in"+NodeLevel+";"+TargetInfoType+";"+
        // TargetValue);

        while (enumeration.hasMoreElements())
        {

            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            ModelInfo target = (ModelInfo) node.getUserObject();

            if ((node.getLevel() == NodeLevel) && target.getModelType().equals(TargetInfoType)
                    && target.getValue().equals(TargetValue))
            {
                return node;
            }

            if (node.getLevel() == 2)
            {
                // System.err.println(node.getLevel()+";"+target.getModelType()+";"+
                // target.getValue());
            }
        }

        System.err.println("Warning: NodeLevel=" + NodeLevel + "&Type=" + TargetInfoType + "&Value=" + TargetValue
                + " --cannot find a matching node with name, type, and level");

        return null;

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

    public static ModelInfo getCurrentModel(JTree modelTree)
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
            return (ModelInfo) nodeInfo;

        }

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
            ModelInfo target = (ModelInfo) node.getUserObject();
            if ((node.getLevel() == NodeLevel) && target.getModelType().equals(TargetInfoType))
            {
                list.add(node);
            }
        }
        return list;
    }

    /*
     * public static List<DefaultMutableTreeNode> getTargetTreeChildrenNode2List
     * (DefaultMutableTreeNode AncestoNode, String TargetInfoType, int
     * NodeLevel) {
     *
     * List<DefaultMutableTreeNode> list = new
     * ArrayList<DefaultMutableTreeNode>();
     *
     * DefaultMutableTreeNode node = null;
     *
     * Enumeration enumeration = AncestoNode.breadthFirstEnumeration();
     *
     * while(enumeration.hasMoreElements()) { //get the node node =
     * (DefaultMutableTreeNode)enumeration.nextElement(); TargetInfo target =
     * (TargetInfo) node.getUserObject(); if (node.getLevel() == NodeLevel &&
     * target.getTargetType().equals(TargetInfoType)) list.add(node); } return
     * list; }
     */
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

            // It returns toString, I change it to other way
            // strArray[i] = list.get(i).toString();
            strArray[i] = ((ModelInfo) list.get(i)).getValue();

        }

        return strArray;
    }

    public static List<ModelInfo> getTargetTreeChildrenValues2List(DefaultMutableTreeNode AncestoNode,
            String TargetInfoType, int NodeLevel)
    {

        List<ModelInfo> list = new ArrayList<ModelInfo>();

        DefaultMutableTreeNode node = null;

        Enumeration enumeration = AncestoNode.breadthFirstEnumeration();

        while (enumeration.hasMoreElements())
        {
            // get the node
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            ModelInfo target = (ModelInfo) node.getUserObject();
            if ((node.getLevel() == NodeLevel) && target.getModelType().equals(TargetInfoType))
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

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new ModelInfo(targetType, value));

        currentNode.add(newNode);

        ((DefaultTreeModel) modelTree.getModel()).reload(currentNode);

    }

    // true: non-conflict
    // false: conflict

    public static boolean IsPolicyModelNameNonConflict(String modelType, String value, JTree modelTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

        DefaultMutableTreeNode NodeOfModelRoot = null;
        List<ModelInfo> ModelLst = null;

        // When add new policy name
        if (currentNode.getLevel() == ModelInfo.NodeLevelofmodelroot)
        {
            NodeOfModelRoot = currentNode;
            ModelLst = getTargetTreeChildrenValues2List(NodeOfModelRoot,
                    ((ModelInfo) NodeOfModelRoot.getUserObject()).getModelType(), ModelInfo.NodeLevelofmodel);
        }
        // when change policy name
        else if (currentNode.getLevel() == ModelInfo.NodeLevelofmodel)
        {
            NodeOfModelRoot = (DefaultMutableTreeNode) currentNode.getParent();
            ModelLst = getTargetTreeChildrenValues2List(NodeOfModelRoot,
                    ((ModelInfo) NodeOfModelRoot.getUserObject()).getModelType(), ModelInfo.NodeLevelofmodel);
            ModelLst.remove(currentNode.getUserObject()); // remove
                                                          // the
                                                          // same
                                                          // object
                                                          // (considering
                                                          // users
                                                          // don't
                                                          // need to
                                                          // check
                                                          // for a
                                                          // node
                                                          // with
                                                          // new
                                                          // name
        }
        else
        {
            // if two above cases are not applicable, we should consdier a new
            // case.
            GraphicsUtil.showWarningDialog("Warning: Check IsPolicyModelNameNonConflict function.",
                    "Name Conflict Warning", null);
            return false;
        }

        for (ModelInfo modelInfo : ModelLst)
        {

            if (modelInfo.getModelType().equals(modelType) && modelInfo.getValue().equals(value))
            {
                GraphicsUtil.showWarningDialog("Error: Name conflict between policies.", "Name Conflict Warning", null);
                return false;
            }
        }
        return true;

    }

    public static DefaultMutableTreeNode addNode2(String targetType, String value, JTree modelTree)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to add null node");
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new ModelInfo(targetType, value));

        currentNode.add(newNode);

        ((DefaultTreeModel) modelTree.getModel()).reload(currentNode);

        return newNode;
    }

    public static DefaultMutableTreeNode addGrandChildNode(String targetType, String value, JTree modelTree,
            boolean Inherit)
    {

        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) modelTree.getLastSelectedPathComponent();

        if (currentNode == null)
        {

            System.err.println("WARNING: TreeUtil: trying to add null node");
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new ModelInfo(targetType, value));

        // currentNode.add(newNode);

        boolean isAdded = false;
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
                    if (ruleTarget.getModelType().equals(targetType)
                            && ruleTarget.toString().equals(newNode.getUserObject().toString()))
                    {
                        // Warning
                        if (!Inherit)
                        {
                            GraphicsUtil.showWarningDialog("Same rule already exists.", "Duplication Warning", null);
                        }
                        return null;
                    }
                }
                // if no duplication, add a node
                isAdded = true;
                node.add(newNode);
                break;
            }
        }

        // if rule container node (parent node for new node) does not exist
        if (isAdded == false)
        {
            DefaultMutableTreeNode SubElementRoot = null;
            if (targetType.equals(ModelInfo.RBACRule) || targetType.equals(ModelInfo.ABACRule)
                    || targetType.equals(ModelInfo.MULTILEVELRule) || targetType.equals(ModelInfo.WORKFLOWRule))
            {
                SubElementRoot = new DefaultMutableTreeNode(new ModelInfo(targetType, "Rules"));
            }
            else if (targetType.equals(ModelInfo.MULTILEVELSUBJECTLEVELS))
            {
                SubElementRoot = new DefaultMutableTreeNode(new ModelInfo(targetType, "Subject Levels"));
            }
            else if (targetType.equals(ModelInfo.MULTILEVELRESOURCELEVELS))
            {
                SubElementRoot = new DefaultMutableTreeNode(new ModelInfo(targetType, "Resource Levels"));
            }
            currentNode.add(SubElementRoot);
            SubElementRoot.add(newNode);
            ((DefaultTreeModel) modelTree.getModel()).reload(currentNode);
            isAdded = true;
        }

        DefaultMutableTreeNode pNode = (DefaultMutableTreeNode) newNode.getParent();
        ((DefaultTreeModel) modelTree.getModel()).reload(pNode);

        return newNode;
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
