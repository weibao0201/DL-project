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

/**
 * This class implements (XACML) target information.
 *
 * @author JeeHyun Hwang
 * @version $Revision$, $Date$
 * @since 6.0
 *******************************************************************/
public class GenericInfo
{

    /***************************************************************************
     * Constants
     **************************************************************************/
    public static String   configfile                         = "config.properties";
    /*
     * public GenericPanel modelCombinePanel = null; public GenericPanel
     * NuSMVPanel = null; public GenericPanel CombinatorialTestPanel = null;
     * public GenericPanel XACMLGenerationPanel = null; public GenericPanel
     * propertyGenerationPanel = null;
     */
    /* Target types. */
    public static String   ROOT                               = "ROOT";
    public static String   MODELCOMBINE                       = "COMBINATION";
    public static String   NUSMV                              = "NUSMV";
    public static String   TEST                               = "TEST";
    public static String   XACML                              = "XACML";
    public static String   PROPERTY                           = "PROPERTY";
    public static String   SOD                                = "SOD";
//    public static String   SEPARATION_OF_DUTY                 = "SEPARATION_OF_DUTY";

    public static String   SOD_SUBJECTS_VALUES                = "Sod_Subjects_Values";
    public static String   SOD_RESOURCES_VALUES               = "Sod_Resources_Values";
    public static String   SOD_ACTIONS_VALUES                 = "Sod_Actions_Values";
    public static String   SOD_ENVIRONMENTS_VALUES            = "Sod_Environments_Values";

    public static String   FirstApplRuleCombinatoin           = "First-applicable";
    public static String   DenyOverrideRuleCombination        = "Deny-overrides";
    public static String   PermitOverrideRuleCombination      = "Permit-overrides";
    /**
     * The following five variables indicate new combination algorithms
     *
     * @author Ang Li(University of Arkansas), Qinghua Li(University of
     *         Arkansas) Vincent C. Hu(NIST) Jia Di (University of Arkansas)
     * @data 05/11/2015
     */
    public static String   WeakConsensusRuleCombination       = "Weak-consensus";
    public static String   StrongConsensusRuleCombination     = "Strong-consensus";
    public static String   WeakMajorityRuleCombination        = "Weak-majority";
    public static String   StrongMajorityRuleCombination      = "Strong-majority";
    public static String   SuperMajorityPermitRuleCombination = "Super-majority-permit";
    public static String[] PolicyCombinationAlgs              =
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

    /* Target types. */
    /*
     *
     * public static String RBAC = "RBAC"; public static String RBACRule =
     * "RBACRULES"; public static String MULTILEVEL = "MULTILEVEL"; public
     * static String MULTILEVELRule = "MULTILEVELRULES"; public static String
     * MULTILEVELSUBJECTLEVELS = "MULTILEVELSUBJECTLEVELS"; public static String
     * MULTILEVELRESOURCELEVELS = "MULTILEVELRESOURCELEVELS"; public static
     * String WORKFLOW = "WORKFLOW"; public static String WORKFLOWRule =
     * "WORKFLOWRULES"; public static String ABAC = "ABAC"; public static String
     * ABACRule = "ABACRULES";
     */
    public static int      NodeLevelofroot                    = 0;
    public static int      NodeLevelofmodelroot               = 1;
    public static int      NodeLevelofmodel                   = 2;
    public static int      NodeLevelofruleroot                = 3;
    public static int      NodeLevelofrule                    = 4;

    public static String   XACMLFileType                      = "XACMLFileType";
    public static String   TXTFileType                        = "TXTFileType";
    public static String   SMVFileType                        = "SMVFileType";

    public static String   XACMLVersion2                      = "XACML 2.0";
    public static String   XACMLVersion3                      = "XACML 3.0";

    /***************************************************************************
     * Variables
     **************************************************************************/

    private String         Type                               = null;

    /**
     * Indicates whether or not this target has (or is intended to have) child
     * targets. An entity that has child targets is referred to as a group
     * container.
     */
    private String         value                              = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public GenericInfo(String Nodetype, String value)
    {

        if (goodTargetType(Nodetype))
        {

            this.Type = Nodetype;
            this.value = value;

        }
        else
        {

            System.err.println("WARNING: ModelInfo Unknown target type: " + Nodetype);

        }

    }

    /***************************************************************************
     * Methods
     **************************************************************************/

    private boolean goodTargetType(String targetType)
    {

        if ((targetType.equals(ROOT)) || (targetType.equals(MODELCOMBINE)) || (targetType.equals(NUSMV))
                || (targetType.equals(TEST)) || targetType.equals(XACML) || targetType.equals(PROPERTY)
                || targetType.equals(SOD) || targetType.equals(SOD_SUBJECTS_VALUES) || targetType.equals(SOD_RESOURCES_VALUES)|| targetType.equals(SOD_ACTIONS_VALUES))
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

        return value;

    }

    public String getType()
    {

        return this.Type;

    }

    public String getValue()
    {

        return this.value;

    }

    public String getCombiningAlgorithm()
    {

        String combinationAlg = this.value;
        if (combinationAlg.equals("COMBINATION"))
        {
            combinationAlg = "Policies: First-applicable";
        }
        return combinationAlg.substring(10);

    }

    public void setValue(String value)
    {

        this.value = value;

    }

}
