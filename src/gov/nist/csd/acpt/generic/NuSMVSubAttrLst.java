package gov.nist.csd.acpt.generic;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.util.Generalproperties;

public class NuSMVSubAttrLst
{
    private DefaultMutableTreeNode       pNode        = null;
    private List<DefaultMutableTreeNode> childNodeLst = null;

    private String                       newline      = "\r\n";

    public DefaultMutableTreeNode getpNode()
    {
        return this.pNode;
    }

    public List<DefaultMutableTreeNode> getchildNodeLst()
    {
        return this.childNodeLst;
    }

    public void addchildNode2Lst(DefaultMutableTreeNode cNode)
    {
        childNodeLst.add(cNode);
    }

    public int getchildcount()
    {
        return childNodeLst.size();
    }

    public String toVARstring()
    {

        String temp = "\t" + ((TargetInfo) pNode.getUserObject()).getvalueOfvalue() + "\t: { ";

        // temp += pNode.toString();

        // property setting to avoid an error when an attribute has less than
        // two elements during NuSMV verification

        temp += "dummy";

        if (Generalproperties.DummyEnabledForAttributesOn)
        {
            if (childNodeLst.size() == 0)
            {
                temp += "\t,\tdummyTwo";
                // }else if (childNodeLst.size() == 1){
                // temp += childNodeLst.get(0).toString() + "\t,\tdummyOne";
            }
            else
            {
                for (int i = 0; i < childNodeLst.size(); i++)
                {
                    String cc = ((TargetInfo) childNodeLst.get(i).getUserObject()).getvalueOfvalue();
                    // if (i==0){
                    // temp += "dummy\t,\t"+childNodeLst.get(i).toString();
                    // }else{
                    // temp += "\t,\t" + childNodeLst.get(i).toString();
                    // }
                    temp += "\t,\t" + childNodeLst.get(i).toString();
                }
            }
        }
        else
        {
            for (int i = 0; i < childNodeLst.size(); i++)
            {
                String cc = ((TargetInfo) childNodeLst.get(i).getUserObject()).getvalueOfvalue();
                temp += "\t,\t" + childNodeLst.get(i).toString();
                // if (i==0){
                // temp += "dummy\t,\t"+ childNodeLst.get(i).toString();
                // }else{
                // temp += "\t,\t" + childNodeLst.get(i).toString();
                // }
            }
        }

        temp += "\t};\t" + newline;
        return temp;
    }

    @Override
    public String toString()
    {

        return ((TargetInfo) pNode.getUserObject()).getvalueOfvalue();
    }

    public String toNextStatestring()
    {

        String state = ((TargetInfo) pNode.getUserObject()).getvalueOfvalue();
        return "\tnext (" + state + ") :=\t" + state + "\t;\t" + newline;

    }

    NuSMVSubAttrLst(DefaultMutableTreeNode pNode)
    {
        this.pNode = pNode;
        childNodeLst = new ArrayList<DefaultMutableTreeNode>();
    }

}
