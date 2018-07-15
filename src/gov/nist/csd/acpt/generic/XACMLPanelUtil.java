package gov.nist.csd.acpt.generic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.model.ModelTreeUtil;
//import gov.nist.csd.acpt.target.SubjectsPanel;
import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.util.Generalproperties;
import gov.nist.csd.acpt.util.StringToXML;

/**
 * This class implements the (XACML) model editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class XACMLPanelUtil extends JPanel
{

    static String XACMLPolicyString           = "";
    static String policySetTempate_start      = "";
    static String policySetTempate_end        = "";
    static String policyTempate_start         = "";
    static String policyTempate_end           = "";
    static String ruleTempateFileName         = "";
    static String RBACruleTempate             = "";
    static String ABACruleTempate             = "";
    static String SubjectElementTemplate      = "";
    static String ResourceElementTemplate     = "";
    static String ActionElementTemplate       = "";
    static String EnviromentElementTemplate   = "";
    static String ApplyElementTemplate        = "";
    static String PolicyRuleTemplate          = "";
    static String DenyFallThroughRuleTemplate = "";

    static String XACMLVersion                = "";

    static String MultiLevelruleTempate       = "";
    static String WorkFlowruleTempate         = "";
    // static String xacmlTempleteFileName = "xacml.templates";

    private static String getSetPolicyStart(String PolicyID, String Combination)
    {

        String tmpString = policySetTempate_start;
        String predixRuleCom = "urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:";
        tmpString = tmpString.replaceAll("POLSETID", PolicyID);
        tmpString = tmpString.replaceAll("POLSETCOMBINATION", predixRuleCom + Combination.toLowerCase());
        return tmpString;
    }

    private static String getPolicyStart(String PolicyID, String Combination)
    {

        String tmpString = policyTempate_start;
        String predixRuleCom = "urn:oasis:names:tc:xacml:1.0:rule-combining-algorithm:";
        tmpString = tmpString.replaceAll("POLID", PolicyID);
        tmpString = tmpString.replaceAll("POLCOMBINATION", predixRuleCom + Combination.toLowerCase());
        return tmpString;
    }

    private static String getRBACRule(String RuleId, String subject, String resource, String action, String effect)
    {

        String tmpString = RBACruleTempate;
        tmpString = tmpString.replaceAll("RID", RuleId);
        tmpString = tmpString.replaceAll("REffect", effect);
        tmpString = tmpString.replaceAll("RSUBJECT", subject);
        tmpString = tmpString.replaceAll("RRESOURCE", resource);
        tmpString = tmpString.replaceAll("RACTION", action);
        return tmpString;
    }

    private static String getABACRule(String RuleId, String subject, String resource, String action, String effect)
    {

        String tmpString = ABACruleTempate;
        // tmpString = tmpString.replaceAll("RID", RuleId);
        // tmpString = tmpString.replaceAll("REffect", effect);
        // tmpString = tmpString.replaceAll("RSUBJECT", subject);
        // tmpString = tmpString.replaceAll("RRESOURCE", resource);
        // tmpString = tmpString.replaceAll("RACTION", action);

        return tmpString;
    }

    private static String getABACRule(String RuleId, String ABACRule, String cond)
    {

        String tmpRuleString = PolicyRuleTemplate;

        String[] content = ABACRule.split("->");
        String effect = content[1];
        String[] elements = content[0].split("#");
        String tmpSubjectString = "";
        String tmpResourceString = "";
        String tmpActionString = "";
        String tmpEnviromentString = "";
        String tmpApplyString = "";
        /* My Edit */
        // Changed the start of i from 0 to 1 so that it will ignore the rule
        // number
        for (int i = 1; i < elements.length; i++)
        {
            String tempString = getChangedElement(elements[i]);
            if (elements[i].startsWith(TargetInfo.SUBJECT_ATTRIBUTES))
            {
                tmpSubjectString += tempString;
            }
            else if (elements[i].startsWith(TargetInfo.RESOURCE_ATTRIBUTES))
            {
                tmpResourceString += tempString;
            }
            else if (elements[i].startsWith(TargetInfo.ACTION_ATTRIBUTES))
            {
                tmpActionString += tempString;
            }
            else if (elements[i].startsWith(TargetInfo.ENVIRONMENT_ATTRIBUTES))
            {
                tmpApplyString += tempString;
            }
        }

        if (XACMLPanelUtil.XACMLVersion.equals(GenericInfo.XACMLVersion2))
        {
            if (tmpSubjectString.equals(""))
            {
                tmpSubjectString = "";
            }
            else
            {
                tmpSubjectString = "          <Subjects>" + tmpSubjectString + "\n          </Subjects>";
            }
            if (tmpResourceString.equals(""))
            {
                tmpResourceString = "";
            }
            else
            {
                tmpResourceString = "          <Resources>" + tmpResourceString + "\n          </Resources>";
            }
            if (tmpActionString.equals(""))
            {
                tmpActionString = "";
            }
            else
            {
                tmpActionString = "          <Actions>" + tmpActionString + "\n          </Actions>";
            }
        }
        else if (XACMLPanelUtil.XACMLVersion.equals(GenericInfo.XACMLVersion3))
        {
            if (!tmpSubjectString.equals("") || !tmpResourceString.equals("") || !tmpActionString.equals(""))
            {

                tmpSubjectString = "          <AnyOf><AllOf>" + tmpSubjectString;
                tmpResourceString = tmpResourceString;
                tmpActionString = tmpActionString + "\n          </AllOf></AnyOf>";
            }
        }

        // Make XACML user environment conditoin + specified XACML condition

        // System.err.println("Cond Only::\n\n"+cond);
        tmpApplyString += "\n" + cond;

        // System.err.println("Together::\n\n"+tmpApplyString);

        if (tmpApplyString.trim().equals(""))
        {
            tmpEnviromentString = "";
        }
        else
        {
            tmpEnviromentString = EnviromentElementTemplate;
            tmpEnviromentString = tmpEnviromentString.replaceAll("RApplyElementSet", tmpApplyString);
        }

        tmpRuleString = tmpRuleString.replaceAll("RID", RuleId);
        tmpRuleString = tmpRuleString.replaceAll("REffect", effect);
        tmpRuleString = tmpRuleString.replaceAll("SubjectElementSet", tmpSubjectString);
        tmpRuleString = tmpRuleString.replaceAll("ResourceElementSet", tmpResourceString);
        tmpRuleString = tmpRuleString.replaceAll("ActionElementSet", tmpActionString);

        tmpRuleString = tmpRuleString.replaceAll("RCondition", tmpEnviromentString);
        // tmpRuleString = tmpRuleString.replaceAll("RCondition", cond);

        // System.err.println("Apply:"+tmpApplyString);
        // System.err.println("Condition:"+tmpEnviromentString);

        // System.err.println("Rule:"+tmpRuleString);

        return tmpRuleString;
    }

    private static String getWorkFlowRule(String RuleId, String ABACRule, String cond)
    {

        String tmpRuleString = PolicyRuleTemplate;
        String[] content = ABACRule.split("->");
        String effect = content[1];
        if (effect.equals("Permit and go to Next State"))
        {
            effect = "Permit";
        }
        String[] elements = content[0].split("#");
        String tmpSubjectString = "";
        String tmpResourceString = "";
        String tmpActionString = "";
        String tmpEnviromentString = "";
        String tmpApplyString = "";

        /* My Edit */
        // Changed the start of i from 0 to 1 so that it will ignore the rule
        // number
        for (int i = 1; i < elements.length; i++)
        {
            String tempString = getChangedElement(elements[i]);
            if (elements[i].startsWith("Process_State"))
            {
                tmpSubjectString += tempString;
            }
            else if (elements[i].startsWith(TargetInfo.SUBJECT_ATTRIBUTES))
            {
                tmpSubjectString += tempString;
            }
            else if (elements[i].startsWith(TargetInfo.RESOURCE_ATTRIBUTES))
            {
                tmpResourceString += tempString;
            }
            else if (elements[i].startsWith(TargetInfo.ACTION_ATTRIBUTES))
            {
                tmpActionString += tempString;
            }
            else if (elements[i].startsWith(TargetInfo.ENVIRONMENT_ATTRIBUTES))
            {
                tmpApplyString += tempString;
            }
        }

        if (XACMLPanelUtil.XACMLVersion.equals(GenericInfo.XACMLVersion2))
        {
            if (tmpSubjectString.equals(""))
            {
                tmpSubjectString = "";
            }
            else
            {
                tmpSubjectString = "          <Subjects>" + tmpSubjectString + "\n          </Subjects>";
            }
            if (tmpResourceString.equals(""))
            {
                tmpResourceString = "";
            }
            else
            {
                tmpResourceString = "          <Resources>" + tmpResourceString + "\n          </Resources>";
            }
            if (tmpActionString.equals(""))
            {
                tmpActionString = "";
            }
            else
            {
                tmpActionString = "          <Actions>" + tmpActionString + "\n          </Actions>";
            }
        }
        else if (XACMLPanelUtil.XACMLVersion.equals(GenericInfo.XACMLVersion3))
        {
            if (!tmpSubjectString.equals("") || !tmpResourceString.equals("") || !tmpActionString.equals(""))
            {

                tmpSubjectString = "          <AnyOf><AllOf>" + tmpSubjectString;
                tmpResourceString = tmpResourceString;
                tmpActionString = tmpActionString + "\n          </AllOf></AnyOf>";
            }
        }

        // Make XACML user environment conditoin + specified XACML condition
        tmpApplyString += "\n" + cond;

        if (tmpApplyString.trim().equals(""))
        {
            tmpEnviromentString = "";
        }
        else
        {
            tmpEnviromentString = EnviromentElementTemplate;
            tmpEnviromentString = tmpEnviromentString.replaceAll("RApplyElementSet", tmpApplyString);
        }

        tmpRuleString = tmpRuleString.replaceAll("RID", RuleId);
        tmpRuleString = tmpRuleString.replaceAll("REffect", effect);
        tmpRuleString = tmpRuleString.replaceAll("SubjectElementSet", tmpSubjectString);
        tmpRuleString = tmpRuleString.replaceAll("ResourceElementSet", tmpResourceString);
        tmpRuleString = tmpRuleString.replaceAll("ActionElementSet", tmpActionString);
        tmpRuleString = tmpRuleString.replaceAll("RCondition", tmpEnviromentString);

        return tmpRuleString;
    }

    private static String getChangedElement(String elementSet)
    {

        String tempString = "";
        if (elementSet.startsWith("Process_State"))
        {
            tempString = SubjectElementTemplate;
            tempString = tempString.replaceAll("RAttributeId", "Process_State");
            tempString = tempString.replaceAll("RTypeEqual", "string-equal");
            tempString = tempString.replaceAll("RTypeOfValue", "string");
            tempString = tempString.replaceAll("RAttributeValue", elementSet);
            return tempString;
        }

        if (elementSet.startsWith(TargetInfo.SUBJECT_ATTRIBUTES))
        {
            tempString = SubjectElementTemplate;
        }
        else if (elementSet.startsWith(TargetInfo.RESOURCE_ATTRIBUTES))
        {
            tempString = ResourceElementTemplate;
        }
        else if (elementSet.startsWith(TargetInfo.ACTION_ATTRIBUTES))
        {
            tempString = ActionElementTemplate;
        }
        else if (elementSet.startsWith(TargetInfo.ENVIRONMENT_ATTRIBUTES))
        {
            tempString = ApplyElementTemplate;
        }

        String elms[] = elementSet.split(";");
        String RAttributeId = elms[1];
        String RTypeEqual = elms[2].toLowerCase() + "-equal";
        String RTypeOfValue = elms[2].toLowerCase();
        String RAttributeValue = elms[3];

        tempString = tempString.replaceAll("RAttributeId", RAttributeId);
        tempString = tempString.replaceAll("RTypeEqual", RTypeEqual);
        tempString = tempString.replaceAll("RTypeOfValue", RTypeOfValue);
        tempString = tempString.replaceAll("RAttributeValue", RAttributeValue);

        return tempString;
    }

    private static String getWorkFlowRule(String RuleId, String state, String subject, String resource, String action,
            String effect)
    {

        String tmpString = WorkFlowruleTempate;
        tmpString = tmpString.replaceAll("RID", RuleId);
        tmpString = tmpString.replaceAll("RSTATE", state);
        tmpString = tmpString.replaceAll("REffect", effect);
        tmpString = tmpString.replaceAll("RSUBJECT", subject);
        tmpString = tmpString.replaceAll("RRESOURCE", resource);
        tmpString = tmpString.replaceAll("RACTION", action);
        return tmpString;
    }

    // private static String getMultiLevelRule(String RuleId, String action,
    // String property) {
    //
    // String tmpString = MultiLevelruleTempate;
    // tmpString = tmpString.replaceAll("RID", RuleId);
    // tmpString = tmpString.replaceAll("REffect", "Deny");
    // tmpString = tmpString.replaceAll("RACTION", action);
    // if (property.equals("up")){
    // tmpString = tmpString.replaceAll("RRelation", "integer-greater-than");
    // } else if (property.equals("down")){
    // tmpString = tmpString.replaceAll("RRelation", "integer-less-than");
    // }
    // return tmpString;
    // }

    private static String getMultiLevelDenyRule(String RuleId, String property, String subAttr, String resAttr)
    {

        String tmpString = MultiLevelruleTempate;
        tmpString = tmpString.replaceAll("RID", RuleId);
        tmpString = tmpString.replaceAll("REffect", "Deny");
        tmpString = tmpString.replaceAll("RSubAttributeId", subAttr);
        tmpString = tmpString.replaceAll("RResAttributeId", resAttr);

        if (property.equals("no read up"))
        {
            tmpString = tmpString.replaceAll("RACTION", "read");
            tmpString = tmpString.replaceAll("RRelation", "integer-greater-than");
        }
        else if (property.equals("no write down"))
        {
            tmpString = tmpString.replaceAll("RACTION", "write");
            tmpString = tmpString.replaceAll("RRelation", "integer-less-than");
        }
        return tmpString;
    }

    private static String getMultiLevelPermitRule(String RuleId, String property, String subAttr, String resAttr)
    {

        // System.err.println(MultiLevelruleTempate);
        String tmpString = MultiLevelruleTempate;
        tmpString = tmpString.replaceAll("RID", RuleId);
        tmpString = tmpString.replaceAll("REffect", "Permit");
        tmpString = tmpString.replaceAll("RSubAttributeId", subAttr);
        tmpString = tmpString.replaceAll("RResAttributeId", resAttr);

        if (property.equals("no read up"))
        {
            tmpString = tmpString.replaceAll("RACTION", "read");
            tmpString = tmpString.replaceAll("RRelation", "integer-less-than-or-equal");
        }
        else if (property.equals("no write down"))
        {
            tmpString = tmpString.replaceAll("RACTION", "write");
            tmpString = tmpString.replaceAll("RRelation", "integer-greater-than-or-equal");
        }
        return tmpString;
    }

    public static void XACMLGeneration(String option, ACPTFrame frame, JTree modelTree, JTree combineTree, List polSet,
            String XACMLVersion, File selected, List defaultDenyEnabledpolSet)
    {

        if (selected == null)
        {
            frame.logPanel.append("No file is selected for XACML generation ");
            return;
        }

        setTemplate(XACMLVersion);
        XACMLPolicyString = "";
        // PrintStream output = null;
        String outputFileName = selected.getAbsoluteFile().toString();
        // try {
        // output = new PrintStream(new FileOutputStream(outputFileName,
        // false));
        // } catch (FileNotFoundException e1) {
        // e1.printStackTrace();
        // }

        /*
         * DefaultMutableTreeNode combinetreeRooNode =
         * GenericTreeUtil.getRootNode (combineTree); String choices[] =
         * GenericTreeUtil.getTargetTreeChildrenValues2StrArr(
         * combinetreeRooNode, GenericInfo.MODELCOMBINE, 1);
         * getTargetTreeChildrenValues2List
         */

        // output.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        XACMLPolicyString += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

        if (option.equals("Merge") && (polSet.size() > 0))
        {
            // output.println("<!--## This XACML is the collection of policies
            // (e.g., POLICY 1) to be merged (by users).-->");
            XACMLPolicyString +=
                    "<!--## This XACML is the collection of policies (e.g., POLICY 1) to be merged (by users).-->";
        }

        DefaultMutableTreeNode combinetreeRootNode =
                GenericTreeUtil.getFirstMatchingNode(combineTree, GenericInfo.ROOT, 0);

        if (option.equals("Merge"))
        {
            // Merge
            String policyset_start = getSetPolicyStart("MergedPolicySet", "first-applicable");
            // output.println(policyset_start);
            XACMLPolicyString += policyset_start;

        }
        else
        {
            // Combine
            String policyset_start = getSetPolicyStart("CombinedPolicySet",
                    ((GenericInfo) combinetreeRootNode.getUserObject()).getCombiningAlgorithm());
            // output.println(policyset_start);
            XACMLPolicyString += policyset_start;
        }

        // output.println("\n<!--## POLICY START" + (1)+"-->");
        XACMLPolicyString += "\n<!--## POLICY START" + (1) + "-->";

        // output.println(policyTempate_start);

        int rulecount = 1;
        // xacml generation
        for (int i = 0; i < polSet.size(); i++)
        {

            String policyModelTypeName = polSet.get(i).toString();
            String model[] = policyModelTypeName.split("#");
            String modeltype = model[0];
            String modelvalue = model[1];

            DefaultMutableTreeNode selectedModelRootNode =
                    ModelTreeUtil.getFirstMatchingNodeOnTypeValue(modelTree, modeltype, modelvalue, 2);

            // String[] rulecombinations =
            // ModelTreeUtil.getTargetTreeChildrenValues2StrArr(selectedModelRootNode,
            // modeltype+"RULES", 3);
            String[] rules =
                    ModelTreeUtil.getTargetTreeChildrenValues2StrArr(selectedModelRootNode, modeltype + "RULES", 4);

            List<DefaultMutableTreeNode> ruleCombLst =
                    ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode, modeltype + "RULES", 3);
            List<DefaultMutableTreeNode> ruleLst =
                    ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode, modeltype + "RULES", 4);

            // System.err.println(choices[i]);

            if (modeltype.equals(ModelInfo.ABAC))
            {
                String rulecombinations = ((ModelInfo) ruleCombLst.get(0).getUserObject()).getRuleCombiningAlgorithm();
                String policy_start = getPolicyStart(modelvalue, rulecombinations);
                // output.println(policy_start);
                XACMLPolicyString += policy_start;

                // output.println("\n<!-- ABAC Model: "+ modelvalue+"-->") ;

                XACMLPolicyString += "\n<!-- ABAC Model: " + modelvalue + "-->";

                for (int j = 0; j < ruleLst.size(); j++)
                {
                    DefaultMutableTreeNode Cnode = ruleLst.get(j);
                    String ruleContent = ((ModelInfo) Cnode.getUserObject()).getValue();

                    // System.err.println("1:"+rules[j]);
                    // System.err.println("2:"+ruleContent);

                    // condition
                    String condContent = "";
                    if (Cnode.getChildCount() > 0)
                    { // if condition node exits
                        DefaultMutableTreeNode ConditionNode = (DefaultMutableTreeNode) Cnode.getFirstChild();
                        condContent = ((ModelInfo) ConditionNode.getUserObject()).getValue();
                    }

                    String xacmlrule = getABACRule("rule_" + (rulecount++), ruleContent, condContent);

                    // output.println(xacmlrule);
                    XACMLPolicyString += xacmlrule;

                }

                if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                {
                    // output.println(DenyFallThroughRuleTemplate);
                    XACMLPolicyString += DenyFallThroughRuleTemplate;
                }
            }
            else if (modeltype.equals(ModelInfo.WORKFLOW))
            {

                String rulecombinations = ((ModelInfo) ruleCombLst.get(0).getUserObject()).getRuleCombiningAlgorithm();
                String policy_start = getPolicyStart(modelvalue, rulecombinations);
                // output.println(policy_start);
                XACMLPolicyString += policy_start;
                // output.println("\n<!-- WORKFLOW Model: "+ modelvalue+"-->") ;
                XACMLPolicyString += "\n<!-- WORKFLOW Model: " + modelvalue + "-->";

                for (int j = 0; j < ruleLst.size(); j++)
                {

                    DefaultMutableTreeNode Cnode = ruleLst.get(j);
                    String ruleContent = ((ModelInfo) Cnode.getUserObject()).getValue();

                    // condition
                    String condContent = "";
                    if (Cnode.getChildCount() > 0)
                    { // if condition node exits
                        DefaultMutableTreeNode ConditionNode = (DefaultMutableTreeNode) Cnode.getFirstChild();
                        condContent = ((ModelInfo) ConditionNode.getUserObject()).getValue();
                    }

                    String xacmlrule = getWorkFlowRule("rule_" + (rulecount++), ruleContent, condContent);
                    // output.println(xacmlrule);
                    XACMLPolicyString += xacmlrule;

                }

                if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                {
                    // output.println(DenyFallThroughRuleTemplate);
                    XACMLPolicyString += DenyFallThroughRuleTemplate;
                }
            }
            else if (modeltype.equals(ModelInfo.MULTILEVEL))
            {
                String rulecombinations = ((ModelInfo) ruleCombLst.get(0).getUserObject()).getRuleCombiningAlgorithm();
                String policy_start = getPolicyStart(modelvalue, rulecombinations);
                // output.println(policy_start);
                XACMLPolicyString += policy_start;
                // output.println("\n<!-- MULTILEVEL Model: "+ modelvalue+"-->")
                // ;
                XACMLPolicyString += "\n<!-- MULTILEVEL Model: " + modelvalue + "-->";

                String subAttr = "";
                String resAttr = "";
                List choicesSubLst = ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode,
                        ModelInfo.MULTILEVELSUBJECTLEVELS, 4);
                List choicesResLst = ModelTreeUtil.getTargetTreeChildrenNode2List(selectedModelRootNode,
                        ModelInfo.MULTILEVELRESOURCELEVELS, 4);

                for (int k = 0; k < choicesSubLst.size(); k++)
                {
                    for (int j = 0; j < choicesResLst.size(); j++)
                    {

                        DefaultMutableTreeNode Subnode = (DefaultMutableTreeNode) choicesSubLst.get(k);
                        DefaultMutableTreeNode Resnode = (DefaultMutableTreeNode) choicesResLst.get(j);
                        subAttr = ((ModelInfo) Subnode.getUserObject()).getSecondTargetvalueOfnodes();
                        resAttr = ((ModelInfo) Resnode.getUserObject()).getSecondTargetvalueOfnodes();

                        for (int m = 0; m < ruleLst.size(); m++)
                        {

                            String ruleContent = ((ModelInfo) ruleLst.get(m).getUserObject()).getValue();
                            String tmp[] = ruleContent.split("#");

                            String xacmlrule =
                                    getMultiLevelDenyRule("rule_" + (rulecount++), ruleContent, subAttr, resAttr);
                            xacmlrule +=
                                    getMultiLevelPermitRule("rule_" + (rulecount++), ruleContent, subAttr, resAttr);

                            // output.println(xacmlrule);
                            XACMLPolicyString += xacmlrule;
                        }

                    }

                }
                /*
                 * String subAttr = ""; String resAttr = ""; List choicesLst =
                 * (List) ModelTreeUtil.getTargetTreeChildrenNode2List(
                 * selectedModelRootNode, ModelInfo.MULTILEVELSUBJECTLEVELS, 3);
                 * if (choicesLst.size() > 0){ DefaultMutableTreeNode
                 * MLSModelSubLevelRootnode = (DefaultMutableTreeNode)
                 * choicesLst.get(0);
                 *
                 * String temp =
                 * ((ModelInfo)MLSModelSubLevelRootnode.getUserObject()).
                 * getValue(); String[] tempStr = temp.split(";"); if
                 * (tempStr.length>1){ subAttr = tempStr[1]; } }
                 *
                 * choicesLst = (List)
                 * ModelTreeUtil.getTargetTreeChildrenNode2List(
                 * selectedModelRootNode, ModelInfo.MULTILEVELRESOURCELEVELS,
                 * 3); if (choicesLst.size() > 0){ DefaultMutableTreeNode
                 * MLSModelResLevelRootnode = (DefaultMutableTreeNode)
                 * choicesLst.get(0);
                 *
                 * String temp =
                 * ((ModelInfo)MLSModelResLevelRootnode.getUserObject()).
                 * getValue(); String[] tempStr = temp.split(";"); if
                 * (tempStr.length>1){ resAttr = tempStr[1]; } }
                 *
                 * for (int j = 0; j < rules.length; j++) { String tmp[] =
                 * rules[j].split("#");
                 *
                 * String xacmlrule =
                 * getMultiLevelDenyRule("rule_"+(rulecount++), rules[j],
                 * subAttr, resAttr ); xacmlrule +=
                 * getMultiLevelPermitRule("rule_"+(rulecount++), rules[j],
                 * subAttr, resAttr );
                 *
                 * output.println(xacmlrule); }
                 */
                // if (defaultDenyEnabled){
                // output.println(DenyFallThroughRuleTemplate);
                // }
                if ((defaultDenyEnabledpolSet != null) && defaultDenyEnabledpolSet.contains(policyModelTypeName))
                {
                    // output.println(DenyFallThroughRuleTemplate);
                    XACMLPolicyString += DenyFallThroughRuleTemplate;
                }
            }

            if (i < (polSet.size() - 1))
            {
                // if (option.equals("Merge") && i < polSet.size()-1){
                // output.println(policyTempate_end);
                // output.println("\n<!--## POLICY " + (i+1)+" END-->");
                // output.println("\n<!--## POLICY " + (i+2)+" START-->");

                XACMLPolicyString += policyTempate_end;
                XACMLPolicyString += "\n<!--## POLICY " + (i + 1) + " END-->";
                XACMLPolicyString += "\n<!--## POLICY " + (i + 2) + " START-->";

                // output.println(policySetTempate_start);
                // output.println("\n\n<!--## POLICY START" + (1)+"\n\n");

                // output.println(policyTempate_start);

            }

        }

        // output.println(policyTempate_end);
        //
        // output.println("\n<!--## POLICY " + (polSet.size())+" END-->");
        // output.println(policySetTempate_end);

        XACMLPolicyString += policyTempate_end;
        XACMLPolicyString += "\n<!--## POLICY " + (polSet.size()) + " END-->";
        XACMLPolicyString += policySetTempate_end;

        // if (option.equals("Merge")){
        //
        // output.println("\n<!--## POLICY " + (polSet.size())+" END-->");
        //// output.println(policySetTempate_start);
        //// output.println("\n\n<!--## POLICY START" + (1)+"\n\n");
        //
        // output.println(policySetTempate_end);
        // }
        // output.close();

        StringToXML.StringToXML(XACMLPolicyString, outputFileName);
        frame.logPanel.append("==========================");
        frame.logPanel.append("XACML file is created");
        frame.logPanel.append("XACML file : " + outputFileName.toString());

    }

    private static void setTemplate(String XACMLVersion)
    {
        // System.out.println("XACMLPanelUtil.setXACMLTemplate()"+TemplateFileName);
        // xacml.templates
        // xacml.templates

        XACMLPanelUtil.XACMLVersion = XACMLVersion;
        String xacmlTempleteFileName = Generalproperties.XACML2Templates;

        if (XACMLVersion.equals(GenericInfo.XACMLVersion2))
        {
            xacmlTempleteFileName = Generalproperties.XACML2Templates;
        }
        else if (XACMLVersion.equals(GenericInfo.XACMLVersion3))
        {
            xacmlTempleteFileName = Generalproperties.XACML3Templates;
        }

        System.out.println("XACMLPanelUtil.setXACMLTemplate()" + xacmlTempleteFileName);

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(xacmlTempleteFileName));

            // new BufferedReader(new InputStreamReader(
            // new FileInputStream(TemplateFileName)));

            String line = br.readLine();
            /*
             * policyTempate_start = ""; policyTempate_end = ""; RBACruleTempate
             * = ""; ABACruleTempate = ""; MultiLevelruleTempate = "";
             * WorkFlowruleTempate = "";
             */
            // initialize
            policySetTempate_start = "";
            policySetTempate_end = "";

            policyTempate_start = "";
            policyTempate_end = "";
            ruleTempateFileName = "";
            RBACruleTempate = "";
            ABACruleTempate = "";
            SubjectElementTemplate = "";
            ResourceElementTemplate = "";
            ActionElementTemplate = "";
            EnviromentElementTemplate = "";
            ApplyElementTemplate = "";

            PolicyRuleTemplate = "";

            DenyFallThroughRuleTemplate = "";

            MultiLevelruleTempate = "";
            WorkFlowruleTempate = "";

            int selection = -1;
            while (line != null)
            {

                // System.out.println(line);
                line = line;

                if (line.startsWith("[PolicySet Start]"))
                {
                    selection = 13;
                }
                else if (line.startsWith("[PolicySet End]"))
                {
                    selection = 14;
                }
                else if (line.startsWith("[Policy Start]"))
                {
                    selection = 0;
                }
                else if (line.startsWith("[Policy End]"))
                {
                    selection = 1;
                }
                else if (line.startsWith("[RBACRule]"))
                {
                    selection = 2;
                }
                else if (line.startsWith("[MultiLevelRule]"))
                {
                    selection = 3;
                }
                else if (line.startsWith("[ABACRule]"))
                {
                    selection = 4;
                }
                else if (line.startsWith("[WorkFlowRule]"))
                {
                    selection = 5;
                }
                else if (line.startsWith("[SubjectElement]"))
                {
                    selection = 6;
                }
                else if (line.startsWith("[ResourceElement]"))
                {
                    selection = 7;
                }
                else if (line.startsWith("[ActionElement]"))
                {
                    selection = 8;
                }
                else if (line.startsWith("[EnviromentElement]"))
                {
                    selection = 9;
                }
                else if (line.startsWith("[ApplyElement]"))
                {
                    selection = 10;
                }
                else if (line.startsWith("[PolicyRule]"))
                {
                    selection = 11;
                }
                else if (line.startsWith("[DenyFallThroughRule]"))
                {
                    selection = 12;
                }
                else
                {
                    if (selection == 13)
                    {
                        policySetTempate_start = policySetTempate_start + "\n" + line;
                    }
                    else if (selection == 14)
                    {
                        policySetTempate_end = policySetTempate_end + "\n" + line;
                    }
                    else if (selection == 0)
                    {
                        policyTempate_start = policyTempate_start + "\n" + line;
                    }
                    else if (selection == 1)
                    {
                        policyTempate_end = policyTempate_end + "\n" + line;
                    }
                    else if (selection == 2)
                    {
                        RBACruleTempate = RBACruleTempate + "\n" + line;
                    }
                    else if (selection == 3)
                    {
                        MultiLevelruleTempate = MultiLevelruleTempate + "\n" + line;
                    }
                    else if (selection == 4)
                    {
                        ABACruleTempate = ABACruleTempate + "\n" + line;
                    }
                    else if (selection == 5)
                    {
                        WorkFlowruleTempate = WorkFlowruleTempate + "\n" + line;
                    }
                    else if (selection == 6)
                    {
                        SubjectElementTemplate = SubjectElementTemplate + "\n" + line;
                    }
                    else if (selection == 7)
                    {
                        ResourceElementTemplate = ResourceElementTemplate + "\n" + line;
                    }
                    else if (selection == 8)
                    {
                        ActionElementTemplate = ActionElementTemplate + "\n" + line;
                    }
                    else if (selection == 9)
                    {
                        EnviromentElementTemplate = EnviromentElementTemplate + "\n" + line;
                    }
                    else if (selection == 10)
                    {
                        ApplyElementTemplate = ApplyElementTemplate + "\n" + line;
                    }
                    else if (selection == 11)
                    {
                        PolicyRuleTemplate = PolicyRuleTemplate + "\n" + line;
                    }
                    else if (selection == 12)
                    {
                        DenyFallThroughRuleTemplate = DenyFallThroughRuleTemplate + "\n" + line;
                    }
                }
                line = br.readLine();
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("error: I could not open " + "!");
        }
        catch (IOException e)
        {
            System.err.println("error: File operations on " + "failed!");
        }

        // System.err.println("=="+SubjectElementTemplate);
    }

}
