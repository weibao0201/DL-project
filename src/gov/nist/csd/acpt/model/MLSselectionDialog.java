package gov.nist.csd.acpt.model;

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
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

// Fri Oct 25 18:07:43 EST 2004
//
// Written by Sean R. Owens, sean at guild dot net, released to the
// public domain.  Share and enjoy.  Since some people argue that it is
// impossible to release software to the public domain, you are also free
// to use this code under any version of the GPL, LPGL, or BSD licenses,
// or contact me for use of another license.
// http://darksleep.com/player

// A very simple custom dialog that takes a string as a parameter,
// displays it in a JLabel, along with two Jbuttons, one labeled Yes,
// and one labeled No, and waits for the user to click one of them.

import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.GraphicsUtil;

public class MLSselectionDialog extends JDialog implements ActionListener
{
    private JPanel                 myPanel          = null;

    private String                 TargetDialogType = null;

    private JComboBox              AttrLstComboBox  = null;
    private JComboBox              check            = null;
    private JLabel                 attributeLabel   = null;
    private JComboBox              booleanValue     = null;

    private JTextField             valueField       =
            new JTextField();                               /*
                                                             * initialization to
                                                             * prevent a null
                                                             * pointer exception
                                                             */

    private JButton                yesButton        = null;
    private JButton                noButton         = null;
    private boolean                answer           = false;

    private DefaultMutableTreeNode myNode           = null;
    private int                    myLevel;
    /* My Edit */
    private JTree                  targetTree       = null;

    // I also add JTree to the constructor of MLSselectionDialog
    /* End of Edit */
    public boolean getAnswer()
    {
        return answer;
    }

    public DefaultMutableTreeNode getMyNode()
    {
        return this.myNode;
    }

    public int getMyLevel()
    {
        return this.myLevel;
    }

    public MLSselectionDialog(JFrame frame, boolean modal, String dialogType, List<DefaultMutableTreeNode> AttrLst,
            JTree targetTree)
    {
        super(frame, modal);

        this.TargetDialogType = dialogType;
        /* My Eidt */
        this.targetTree = targetTree;
        /* End of Edit */
        JPanel panel = null;
        Border border = null;

        myPanel = new JPanel();

        panel = new JPanel(new GridLayout(0, 2));
        border = BorderFactory.createTitledBorder("Attribute Selection");
        panel.setBorder(border);

        attributeLabel = new JLabel("Selected Attribute Name");
        attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        AttrLstComboBox = new JComboBox();
        AttrLstComboBox.setActionCommand("AttributeComboBox");
        AttrLstComboBox.addActionListener(this);
        AttrLstComboBox.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        AttrLstComboBox.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        for (int i = 0; i < AttrLst.size(); i++)
        {
            AttrLstComboBox.addItem(AttrLst.get(i));
        }

        panel.add(attributeLabel);
        // panel.add(valueField);
        panel.add(AttrLstComboBox);

        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));

        attributeLabel = new JLabel("Level");
        attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        valueField = new JTextField(GetFirstNodeValue((DefaultMutableTreeNode) AttrLstComboBox.getSelectedItem()));
        valueField.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        valueField.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        valueField.setEnabled(false);

        panel.add(attributeLabel);
        panel.add(valueField);

        myPanel.add(panel);

        panel = new JPanel(new GridLayout(0, 2));
        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));

        yesButton = new JButton("Select");
        yesButton.addActionListener(this);
        panel.add(yesButton);
        noButton = new JButton("Cancel");
        noButton.addActionListener(this);
        panel.add(noButton);
        pack();

        myPanel.add(panel);
        this.add(myPanel);
        // this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setMinimumSize(new Dimension(400, 250));
        this.setPreferredSize(new Dimension(400, 250));

        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    private String GetFirstNodeValue(DefaultMutableTreeNode node)
    {

        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getFirstChild();
        TargetInfo Childtarget = (TargetInfo) childNode.getUserObject();

        return Childtarget.getvalueOfvalue();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (e.getSource() == AttrLstComboBox)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) AttrLstComboBox.getSelectedItem();
            if (node != null)
            {
                GetFirstNodeValue(node);
                valueField.setText(GetFirstNodeValue(node));
            }
        }

        if (check == e.getSource())
        {
            /*
             * String selectedItem = (String) check.getSelectedItem(); if
             * (selectedItem.equals("Boolean")){ booleanValue.setEnabled(true);
             * valueField.setEnabled(false); }else if
             * (selectedItem.equals("Integer")){ booleanValue.setEnabled(false);
             * valueField.setEnabled(true); }else if
             * (selectedItem.equals("String")){ booleanValue.setEnabled(false);
             * valueField.setEnabled(true); }
             */
        }

        else if (yesButton == e.getSource())
        {
            this.myNode = (DefaultMutableTreeNode) AttrLstComboBox.getSelectedItem();
            /* My Eidt */
            DefaultMutableTreeNode rootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.ROOT, 0);
            List inheritance =
                    TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, TargetInfo.INHERITANCE_ATTRIBUTES, 2);
            // This is used to see if an inherited level was find
            boolean found_inher = false;
            for (int i = 0; i < inheritance.size(); i++)
            {
                // Checks to see if there is some inheritance for the added
                // subject
                if (((TargetInfo) ((DefaultMutableTreeNode) inheritance.get(i)).getUserObject()).gettypeOfvalue()
                        .equals(((TargetInfo) myNode.getUserObject()).getvalueOfvalue()))
                {
                    // Checks to see if there is more than one inherited values
                    // If so set myLevel to the value in the valueField
                    // else get the inherited level
                    if (((DefaultMutableTreeNode) inheritance.get(i)).getChildCount() > 1)
                    {
                        found_inher = false;
                    }
                    else
                    {
                        // Check if the inherited attribute has one child and is
                        // the value of type
                        // of the child is an integer. If so set myLevel to that
                        // integer.
                        // If not set myLevel to the value in the valueField
                        DefaultMutableTreeNode hold_node =
                                ((DefaultMutableTreeNode) ((DefaultMutableTreeNode) inheritance.get(i))
                                        .getFirstChild());
                        // System.err.println("Did this: "+((TargetInfo)
                        // hold_node.getUserObject()).gettypeOfvalue());
                        List subject = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode,
                                TargetInfo.SUBJECT_ATTRIBUTES, 2);
                        for (int j = 0; j < subject.size(); j++)
                        {
                            // System.err.println("Tried this: "+((TargetInfo)
                            // ((DefaultMutableTreeNode)
                            // subject.get(j)).getUserObject()).getvalueOfvalue());
                            if (((TargetInfo) hold_node.getUserObject()).gettypeOfvalue()
                                    .equals(((TargetInfo) ((DefaultMutableTreeNode) subject.get(j)).getUserObject())
                                            .getvalueOfvalue())
                                    && (((DefaultMutableTreeNode) subject.get(j)).getChildCount() == 1))
                            {
                                hold_node = (DefaultMutableTreeNode) ((DefaultMutableTreeNode) subject.get(j))
                                        .getFirstChild();
                                found_inher = true;
                                try
                                {
                                    this.myLevel = Integer
                                            .parseInt(((TargetInfo) hold_node.getUserObject()).getvalueOfvalue());
                                }
                                catch (Exception e2)
                                {
                                    // System.err.println("Counldn't do it");
                                    found_inher = false;
                                }
                            }
                        }
                    }
                }
            }
            if (!found_inher)
            {
                this.myLevel = Integer.parseInt(valueField.getText());
            }
            answer = true;
            /* End of Edit */
            setVisible(false);
        }
        else if (noButton == e.getSource())
        {
            // System.err.println("User chose no.");
            answer = false;
            setVisible(false);
        }
    }

}
