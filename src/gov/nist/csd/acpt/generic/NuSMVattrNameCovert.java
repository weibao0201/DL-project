package gov.nist.csd.acpt.generic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.util.StringLengthComparator;

public class NuSMVattrNameCovert
{

    public static String SpecialColonChar = "-_";

    public static String ConvertToFitNuSMFormatBack(String tmpString)
    {
        String str = tmpString;
        str = str.replaceAll(SpecialColonChar, ":");

        return str;
    }

    public static String ConvertToFitNuSMFormat(String tmpString,
            List<DefaultMutableTreeNode> subjecActionResourceAttrNodeLst)
    {

        List<String> NameCollection = new ArrayList<String>();
        String str = tmpString;

        for (DefaultMutableTreeNode pNode : subjecActionResourceAttrNodeLst)
        {
            String name = ((TargetInfo) pNode.getUserObject()).getvalueOfvalue();
            NameCollection.add(name);

            Enumeration enumeration = pNode.children();
            while (enumeration.hasMoreElements())
            {
                DefaultMutableTreeNode cNode = (DefaultMutableTreeNode) enumeration.nextElement();
                String cname = ((TargetInfo) cNode.getUserObject()).getvalueOfvalue();
                NameCollection.add(cname);
            }
        }

        // Sort to change a string with a longer length first (to preventing
        // modifying a:b:c with a:b, which is part)
        Collections.sort(NameCollection, new StringLengthComparator());

        for (String orignialname : NameCollection)
        {

            if (orignialname.indexOf(':') > -1)
            {
                String modifiedname = orignialname.replaceAll(":", SpecialColonChar);
                str = str.replaceAll(orignialname, modifiedname);
            }
        }

        // System.out.println(str);

        return str;
    }

}
