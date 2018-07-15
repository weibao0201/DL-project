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

/**
 * This class implements (XACML) target information.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 *******************************************************************/
public class TargetInfo
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    /* Target types. */
    public static String  ROOT                     = "Root";
    public static String  SUBJECTS                 = "Subjects";                     // SUBJECTS
    public static String  SUBJECT_ATTRIBUTES       = "Subject Attributes";           // SUBJECT
                                                                                     // ATTRIBUTES
    public static String  SUBJECT_ATTR_VALUSES     = "Subject Attribute Values";     // SUBJECT
                                                                                     // ATTRIBUTES
    public static String  RESOURCES                = "Resources";
    public static String  RESOURCE_ATTRIBUTES      = "Resource Attributes";
    public static String  RESOURCE_ATTR_VALUSES    = "Resource Attribute Values";    // SUBJECT
                                                                                     // ATTRIBUTES
    public static String  ACTIONS                  = "Actions";
    public static String  ACTION_ATTRIBUTES        = "Action Attributes";
    public static String  ACTION_ATTR_VALUSES      = "Action Attribute Values";      // SUBJECT
                                                                                     // ATTRIBUTES

    public static String  ENVIRONMENTS             = "Environments";
    public static String  ENVIRONMENT_ATTRIBUTES   = "Environment Attributes";
    public static String  ENVIRONMENT_ATTR_VALUSES = "Environment Attribute Values"; // SUBJECT
                                                                                     // ATTRIBUTES

    /* Bruce Batson June 2013 Edit */
    public static String  INHERITANCE              = "Inheritance";
    public static String  INHERITANCE_ATTRIBUTES   = "Inheritance Attributes";
    public static String  INHERITANCE_ATTR_VALUSES = "Inheritance Attributes Values";
    /* End of edit */

    public static String  AttrType                 = "Attributes";
    public static String  AttrValueType            = "Attribute Values";

    // debug to see tree view
    public static boolean TreeViewable             = false;

    public static int     Level                    = 1;
    public static int     attrLevel                = 2;
    public static int     nodeLevel                = 3;

    // public static String ENVIRONMENTS = "ENVIRONMENTS";
    // public static String RULES = "RULES";

    /***************************************************************************
     * Variables
     **************************************************************************/

    private String        targetType               = null;

    /**
     * Indicates whether or not this target has (or is intended to have) child
     * targets. An entity that has child targets is referred to as a group
     * container.
     */
    private String        typeOfvalue              = null;
    private String        valueOfvalue             = null;
    private String        value                    = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public TargetInfo(String targetType, String value)
    {

        // new TargetInfo(targetType, "", value);

        this.targetType = targetType;
        this.value = value;

        if (goodTargetType(targetType))
        {

            if (this.value.startsWith("Boolean;"))
            {
                this.typeOfvalue = "Boolean";
                this.valueOfvalue = this.value.substring(8);
            }
            else if (this.value.startsWith("String;"))
            {
                this.typeOfvalue = "String";
                this.valueOfvalue = this.value.substring(7);
            }
            else if (this.value.startsWith("Integer;"))
            {
                this.typeOfvalue = "Integer";
                this.valueOfvalue = this.value.substring(8);
            }

            /* Bruce Batson June 2013 Edit */
            /*
             * This is for inheritance. It finds the semicolon and breaks the
             * string up depending on its location. The first part is the type
             * of value and the second half is the value of value.
             */
            else
            {
                int semicolon = this.value.indexOf(";");
                if (semicolon != -1)
                {
                    this.typeOfvalue = this.value.substring(0, semicolon);
                    this.valueOfvalue = this.value.substring(semicolon + 1);
                }
            }
            /* End of Edit */

            // TargetInfo(targetType, valueType, value);

        }
        else
        {

            System.err.println("WARNING: TargetInfo Unknown target type: " + targetType);

        }

    }

    // public TargetInfo(String targetType, String valueType, String value) {
    //
    // if (goodTargetType(targetType)) {
    //
    // this.targetType = targetType;
    // this.valueType = valueType;
    // this.value = value;
    //
    // } else {
    //
    // System.err.println("WARNING: TargetInfo Unknown target type: " +
    // targetType);
    //
    // }
    //
    // }

    /***************************************************************************
     * Methods
     **************************************************************************/

    private boolean goodTargetType(String targetType)
    {

        if ((targetType.equals(ROOT)) || (targetType.equals(SUBJECTS)) || (targetType.equals(SUBJECT_ATTRIBUTES))
                || (targetType.equals(SUBJECT_ATTR_VALUSES)) || (targetType.equals(RESOURCES))
                || (targetType.equals(RESOURCE_ATTRIBUTES)) || (targetType.equals(RESOURCE_ATTR_VALUSES))
                || (targetType.equals(ACTIONS)) || (targetType.equals(ACTION_ATTRIBUTES))
                || (targetType.equals(ACTION_ATTR_VALUSES)) || (targetType.equals(ENVIRONMENTS))
                || (targetType.equals(ENVIRONMENT_ATTRIBUTES)) || (targetType.equals(ENVIRONMENT_ATTR_VALUSES))
                || (targetType.equals(INHERITANCE)) ||
                /* Bruce Batson June 2013 Edit */
                (targetType.equals(INHERITANCE_ATTRIBUTES)) || (targetType.equals(INHERITANCE_ATTR_VALUSES))
        /* End of edit */
        )
        {

            return true;

        }
        else
        {

            return false;

        }

    }

    // public String AttrTypeValueToString() {
    //
    // //return targetType+"-"+value;
    // return value;
    // }
    //
    // public String AttrTypeToString() {
    //
    // //return targetType+"-"+value;
    // return value;
    // }
    //
    // public String AttrValueToString() {
    //
    // //return targetType+"-"+value;
    // return value;
    // }

    @Override
    public String toString()
    {

        if ((targetType.equals(SUBJECT_ATTR_VALUSES)) || (targetType.equals(RESOURCE_ATTR_VALUSES))
                || (targetType.equals(ACTION_ATTR_VALUSES)) || (targetType.equals(ENVIRONMENT_ATTR_VALUSES)))
        {
            return this.valueOfvalue;
        }

        else if ((targetType.equals(SUBJECT_ATTRIBUTES)) || (targetType.equals(RESOURCE_ATTRIBUTES))
                || (targetType.equals(ACTION_ATTRIBUTES)) || (targetType.equals(ENVIRONMENT_ATTRIBUTES)))
        {

            return this.valueOfvalue + ";" + this.typeOfvalue;
        }

        else if ((targetType.equals(INHERITANCE_ATTR_VALUSES)) || (targetType.equals(INHERITANCE_ATTRIBUTES)))
        {
            return this.typeOfvalue + ";" + this.valueOfvalue;
        }

        // return valueType + valueType;
        // return value+"-" + targetType;
        return value;
    }

    // public String getValueType() {
    //
    // return this.valueType;
    //
    // }

    public String getvalueOfvalue()
    {

        return this.valueOfvalue;

    }

    public String gettypeOfvalue()
    {

        return this.typeOfvalue;

    }

    public String getTargetType()
    {

        return this.targetType;

    }

    public String getValue()
    {

        return this.value;

    }

    public void setValue(String value)
    {

        this.value = value;

    }

}
