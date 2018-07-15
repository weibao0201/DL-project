package gov.nist.csd.acpt.model;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.util.GraphicsUtil;

public class MLSRuleUpdateDialog extends JDialog implements ActionListener
{
    private JPanel                 myPanel          = null;

    private String                 TargetDialogType = null;

    private JComboBox              check            = null;
    private JLabel                 attributeLabel   = null;
    private JComboBox              booleanValue     = null;
    private JTextField             valueField       = null;

    private JButton                yesButton        = null;
    private JButton                noButton         = null;
    private boolean                answer           = false;

    private DefaultMutableTreeNode myNode           = null;
    private String                 myProperty;

    public boolean getAnswer()
    {
        return answer;
    }

    public DefaultMutableTreeNode getMyNode()
    {
        return this.myNode;
    }

    public String getMyProperty()
    {
        return this.myProperty;
    }

    public MLSRuleUpdateDialog(JFrame frame, boolean modal, String dialogType, DefaultMutableTreeNode node)
    {
        super(frame, modal);

        this.TargetDialogType = dialogType;

        JPanel panel = null;
        Border border = null;

        myPanel = new JPanel();

        panel = new JPanel(new GridLayout(0, 2));
        border = BorderFactory.createTitledBorder("Rule Property Update");
        panel.setBorder(border);

        // attributeLabel= new JLabel("Selected Attribute Name");
        // attributeLabel.setPreferredSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        // attributeLabel.setMaximumSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        //
        // valueField = new JTextField(node.toString());
        // valueField.setPreferredSize(
        // new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        // valueField.setMaximumSize(
        // new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        // valueField.setEnabled(false);
        //
        // panel.add(attributeLabel);
        // panel.add(valueField);
        // panel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.DEFAULT_GAP)));
        // panel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.DEFAULT_GAP)));

        attributeLabel = new JLabel("Property Type");
        attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        check = new JComboBox(new String[]
        {
                "no read up", "no write down"
        });
        // for (int i = 1; i < 11; i++) {
        // check.addItem(i);
        // }

        String select = ((ModelInfo) node.getUserObject()).getValue().toString();
        check.setSelectedItem(select);

        check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        check.addActionListener(this);

        panel.add(attributeLabel);
        panel.add(check);

        myPanel.add(panel);

        panel = new JPanel(new GridLayout(0, 2));
        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));

        yesButton = new JButton("Update");
        yesButton.addActionListener(this);
        panel.add(yesButton);
        noButton = new JButton("Cancel");
        noButton.addActionListener(this);
        panel.add(noButton);

        // myPanel.add(new JLabel("Up property: if subjects with higher levels
        // than resources (denoted as R1) can do any action (e.g., read) R1."));
        // myPanel.add(new JLabel("Down property: "));

        pack();

        myPanel.add(panel);

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        String info =
                "* \"no read up\" property: a subject at a given security level may not read on an object at a higher security level.";
        info += "\n* \"no write down\" property: a subject at a given security level must not write to any object at a lower security level.";

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        scrollPane.setPreferredSize(new Dimension(380, 170));
        scrollPane.setMaximumSize(new Dimension(380, 170));
        myPanel.add(scrollPane);

        this.add(myPanel);
        // this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setMinimumSize(new Dimension(400, 350));
        this.setPreferredSize(new Dimension(400, 350));

        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

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

            this.myProperty = (String) check.getSelectedItem();
            answer = true;
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
