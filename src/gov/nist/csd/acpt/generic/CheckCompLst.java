package gov.nist.csd.acpt.generic;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;

public class CheckCompLst
{
    public List<JCheckBox>   checksLst     = null;
    public ArrayList<String> checksNodeLst = null;

    CheckCompLst()
    {

        checksLst = new ArrayList<JCheckBox>();
        checksNodeLst = new ArrayList<String>();
    }

    public boolean addCheck(JCheckBox check, String node)
    {
        checksLst.add(check);
        checksNodeLst.add(node);
        return true;
    }

    public boolean selectAll()
    {
        for (JCheckBox a : checksLst)
        {
            a.setSelected(true);
        }
        return true;
    }

    public boolean deselectAll()
    {
        for (JCheckBox a : checksLst)
        {
            a.setSelected(false);
        }
        return true;
    }

    public List<String> getSelectedNodeLst()
    {

        List<String> selectedNodeLst = new ArrayList<String>();
        for (int i = 0; i < checksLst.size(); i++)
        {

            if (checksLst.get(i).isSelected())
            {
                selectedNodeLst.add(checksNodeLst.get(i));
            }
        }
        return selectedNodeLst;
    }
}
