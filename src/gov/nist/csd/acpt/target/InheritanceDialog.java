package gov.nist.csd.acpt.target;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.util.GraphicsUtil;

public class InheritanceDialog extends JDialog implements ActionListener
{
    private JPanel    myPanel           = null;
    public JTree      targetTree        = null;

    private JComboBox check             = null;
    private JLabel    subject_att_value = null;
    private JButton   yesButton         = null;
    private JButton   noButton          = null;
    private boolean   answer            = false;

    private String    TargetDialogType  = null;
    private String    myType            = null;
    private String    myValue           = null;

    private String    myOldType         = null;
    private String    myOldValue        = null;

    public boolean getAnswer()
    {
        return answer;
    }

    public String getMyType()
    {
        return this.myType;
    }

    public String getMyValue()
    {
        return this.myValue;
    }

    public InheritanceDialog(JFrame frame, JTree targetTree, String command, boolean modal, String dialogType,
            String AttributeType, String myType, String myMessage)
    {
        super(frame, modal);

        this.TargetDialogType = dialogType;
        this.targetTree = targetTree;

        JPanel panel = null;
        Border border = null;

        myPanel = new JPanel();

        panel = new JPanel(new GridLayout(0, 2));
        // Checks to see if the user is adding the Beneficiary or the Inherited
        // value
        if (dialogType.equals(TargetInfo.AttrType))
        {
            border = BorderFactory.createTitledBorder("Beneficiary Add");
        }
        else
        {
            border = BorderFactory.createTitledBorder("Inherited Values Add");
        }

        panel.setBorder(border);

        subject_att_value = new JLabel("Subject Attribute Value List");
        subject_att_value.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        subject_att_value.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        DefaultMutableTreeNode rootNode =
                TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.SUBJECTS, TargetInfo.Level);
        List subject_a = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, TargetInfo.SUBJECT_ATTRIBUTES,
                TargetInfo.attrLevel);
        int count = 0;
        // Figures out how big the String Array choices needs to be
        for (int i = 0; i < subject_a.size(); i++)
        {
            String valueOfvalue =
                    ((TargetInfo) ((DefaultMutableTreeNode) subject_a.get(i)).getUserObject()).getvalueOfvalue();
            rootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, valueOfvalue, TargetInfo.SUBJECT_ATTRIBUTES,
                    TargetInfo.attrLevel);
            String[] subject_a_v = TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode,
                    TargetInfo.SUBJECT_ATTR_VALUSES, TargetInfo.nodeLevel);
            count += subject_a_v.length;
        }
        // Creates a String array that holds all the possible choices
        // that will be add to the Combo Box
        String[] choices = new String[count];
        count = 0;
        for (int i = 0; i < subject_a.size(); i++)
        {
            String valueOfvalue =
                    ((TargetInfo) ((DefaultMutableTreeNode) subject_a.get(i)).getUserObject()).getvalueOfvalue();
            rootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, valueOfvalue, TargetInfo.SUBJECT_ATTRIBUTES,
                    TargetInfo.attrLevel);
            String[] subject_a_v = TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode,
                    TargetInfo.SUBJECT_ATTR_VALUSES, TargetInfo.nodeLevel);
            for (int j = 0; j < subject_a_v.length; j++)
            {
                choices[j + count] = valueOfvalue + ";" + subject_a_v[j];
            }
            count += subject_a_v.length;
        }
        check = new JComboBox(choices);
        check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        check.setSelectedItem(myType);
        check.addActionListener(this);
        check.setEnabled(true);
        panel.add(subject_att_value);
        panel.add(check);
        myPanel.add(panel);

        panel = new JPanel(new GridLayout(0, 2));
        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));

        yesButton = new JButton(command);
        yesButton.addActionListener(this);
        panel.add(yesButton);
        noButton = new JButton("Cancel");
        noButton.addActionListener(this);
        panel.add(noButton);
        pack();

        myPanel.add(panel);
        this.add(myPanel);

        this.setMinimumSize(new Dimension(400, 250));
        this.setPreferredSize(new Dimension(400, 250));

        pack();
        setLocationRelativeTo(frame);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (yesButton == e.getSource())
        {
            // This takes the selected item and brakes it into two parts
            // based on the semicolon position
            String selectedItem = (String) check.getSelectedItem();
            int semicolon = selectedItem.indexOf(";");
            String selectedItem_type = selectedItem.substring(0, semicolon);
            selectedItem = selectedItem.substring(semicolon + 1);
            this.myValue = selectedItem;
            this.myType = selectedItem_type;
            answer = true;
            setVisible(false);
        }
        else if (noButton == e.getSource())
        {
            answer = false;
            setVisible(false);
        }

    }
}