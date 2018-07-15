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

/**
 * This class implements (XACML) target information.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 *******************************************************************/
public class ModelInfo
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    /* Target types. */
    public static String   ROOT                               = "ROOT";
    public static String   RBAC                               = "RBAC";
    public static String   RBACRule                           = "RBACRULES";
    public static String   MULTILEVEL                         = "MULTILEVEL";
    public static String   MULTILEVELRule                     = "MULTILEVELRULES";
    public static String   MULTILEVELSUBJECTLEVELS            = "MULTILEVELSUBJECTLEVELS";
    public static String   MULTILEVELRESOURCELEVELS           = "MULTILEVELRESOURCELEVELS";
    public static String   WORKFLOW                           = "WORKFLOW";
    public static String   WORKFLOWRule                       = "WORKFLOWRULES";
    public static String   ABAC                               = "ABAC";
    public static String   ABACRule                           = "ABACRULES";
    public static String   XACMLRuleCondition                 = "Condition";

    public static String   FirstApplRuleCombinatoin           = "First-applicable";
    public static String   DenyOverrideRuleCombination        = "Deny-overrides";
    public static String   PermitOverrideRuleCombination      = "Permit-overrides";
    public static String   WeakConsensusRuleCombination       = "Weak-consensus";
    public static String   StrongConsensusRuleCombination     = "Strong-consensus";
    public static String   WeakMajorityRuleCombination        = "Weak-majority";
    public static String   StrongMajorityRuleCombination      = "Strong-majority";
    public static String   SuperMajorityPermitRuleCombination = "Super-majority-permit";
    public static String[] RuleCombinationAlgs                =
                                                                      {
                                                                              FirstApplRuleCombinatoin,
                                                                              DenyOverrideRuleCombination,
                                                                              PermitOverrideRuleCombination,
                                                                              WeakConsensusRuleCombination,
                                                                              StrongConsensusRuleCombination,
                                                                              WeakMajorityRuleCombination,
                                                                              StrongMajorityRuleCombination,
                                                                              SuperMajorityPermitRuleCombination
    };
    //

    public static int      NodeLevelofroot                    = 0;
    public static int      NodeLevelofmodelroot               = 1;
    public static int      NodeLevelofmodel                   = 2;
    public static int      NodeLevelofruleroot                = 3;
    public static int      NodeLevelofrule                    = 4;

    // public static int NumberOfWorkFlowStates = 10;

    /***************************************************************************
     * Variables
     **************************************************************************/

    private String         modelType                          = null;

    /**
     * Indicates whether or not this target has (or is intended to have) child
     * targets. An entity that has child targets is referred to as a group
     * container.
     */
    private String         value                              = null;
    private String         SecondTargetvalueOfnodes           = null;
    private String         valueofvalue                       = null;
    private String         Abbreviatedvalueofvalue            = null;
    private String         AbbreviatedRule                    = null;
    private int            level                              = 0;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public ModelInfo(String targetType, String value)
    {

        if (goodTargetType(targetType))
        {

            this.modelType = targetType;
            this.value = value;

            if (this.modelType.equals(ModelInfo.MULTILEVELSUBJECTLEVELS)
                    || this.modelType.equals(ModelInfo.MULTILEVELRESOURCELEVELS))
            {

                if (value.indexOf(';') > 0)
                {
                    // System.err.println(value+value.indexOf(';'));
                    String[] abc = value.split("#");
                    valueofvalue = abc[0]; // represent a value without # level
                    level = Integer.parseInt(abc[1]); // represent a level in
                                                      // integer type
                    String[] eachvalues = valueofvalue.split(";");
                    SecondTargetvalueOfnodes = eachvalues[1];
                    Abbreviatedvalueofvalue = value.substring(value.indexOf(';') + 1); // show
                                                                                       // its
                                                                                       // toString
                                                                                       // output
                }
                // String[] abc = value.split("#");
                // if (abc.length >= 2) {
                // valueofvalue = abc[0];
                // level = Integer.parseInt(abc[1]);
                //
                // String[] ccc = valueofvalue.split(";");
                // if (ccc.length >= 1) {
                // Abbreviatedvalueofvalue = ccc[ccc.length - 1] + "#"
                // + level;
                // }
                // }
            }
            else if (this.modelType.equals(ModelInfo.ABACRule) || this.modelType.equals(ModelInfo.WORKFLOWRule))
            {

                // System.err.println(value);
                // System.err.println(modelType);
                if (!this.value.startsWith(("Rules: ")))
                {

                    String[] content = value.split("->");
                    String effect = content[1];
                    String[] elements = content[0].split("#");

                    String temprule = null;
                    /*
                     * Bruce Batson June 2013 Edit Change the start value of i
                     * from 0 to 1 so it will skip over the rule number.
                     */
                    for (int i = 1; i < elements.length; i++)
                    {
                        String tempString = getChangedElement(elements[i]);
                        if (temprule == null)
                        {
                            temprule = tempString;
                        }
                        else
                        {
                            temprule += "#" + tempString;
                        }
                    }

                    temprule += "->" + effect;
                    this.AbbreviatedRule = temprule;

                    // this.AbbreviatedRule = value;
                }
            }

        }
        else
        {

            System.err.println("WARNING: ModelInfo Unknown target type: " + targetType);

        }

    }

    private static String getChangedElement(String elementSet)
    {

        String tempString = "";

        String elms[] = elementSet.split(";");

        if (elementSet.startsWith("Process_State"))
        {

            return elms[0];
            // tempString = SubjectElementTemplate;
            // tempString = tempString.replaceAll("RAttributeId", "State");
            // tempString = tempString.replaceAll("RTypeEqual", "string-equal");
            // tempString = tempString.replaceAll("RTypeOfValue", "string");
            // tempString = tempString.replaceAll("RAttributeValue",
            // elementSet);
            // String stateNum = elms[0].replaceAll("State", "").trim();
            //
            // return "\r\n\t\tstate"+WorkFlowcount +" = "+ stateNum ;
        }

        String RAttributeId = elms[1];
        // String RTypeEqual = elms[2].toLowerCase()+ "-equal";
        // String RTypeOfValue = elms[2].toLowerCase();
        String RAttributeValue = elms[3];

        // tempString = tempString.replaceAll("RAttributeId", RAttributeId);
        // tempString = tempString.replaceAll("RTypeEqual", RTypeEqual);
        // tempString = tempString.replaceAll("RTypeOfValue", RTypeOfValue);
        // tempString = tempString.replaceAll("RAttributeValue",
        // RAttributeValue);

        return RAttributeId + ": " + RAttributeValue;
    }

    /***************************************************************************
     * Methods
     **************************************************************************/

    private boolean goodTargetType(String targetType)
    {

        if ((targetType.equals(ROOT)) || (targetType.equals(RBAC)) || (targetType.equals(RBACRule))
                || (targetType.equals(MULTILEVEL)) || targetType.equals(MULTILEVELSUBJECTLEVELS)
                || targetType.equals(MULTILEVELRESOURCELEVELS) || targetType.equals(MULTILEVELRule)
                || (targetType.equals(WORKFLOW)) || (targetType.equals(XACMLRuleCondition))
                || targetType.equals(WORKFLOWRule) || (targetType.equals(ABAC)) || (targetType.equals(ABACRule)))
        {

            return true;

        }
        else
        {

            return false;

        }

    }

    @Override
    public String toString()
    {

        if (Abbreviatedvalueofvalue != null)
        {
            return this.Abbreviatedvalueofvalue;
        }

        if (AbbreviatedRule != null)
        {
            return this.AbbreviatedRule;
        }

        if (this.modelType.equals(ModelInfo.MULTILEVELRule) && this.value.startsWith("Rules: "))
        {
            return "Rules";
        }

        return value;

    }

    // public String toCompleteRuleString() {
    //
    // return value;
    //
    // }

    public String getModelType()
    {

        return this.modelType;

    }

    public String getSecondTargetvalueOfnodes()
    {

        return this.SecondTargetvalueOfnodes;

    }

    public String getRuleCombiningAlgorithm()
    {

        return this.value.substring(7);

    }

    public String getValue()
    {

        return this.value;

    }

    public String getvalueofvalue()
    {

        return this.valueofvalue;

    }

    public int getLevel()
    {

        return this.level;

    }

    public void setValue(String value)
    {

        this.value = value;

    }

    /* Bruce Batson June 2013 Edit */
    public String getRuleNum()
    {
        String[] num = this.value.split("#");
        return num[0];
    }
    /* End of Edit */

}
