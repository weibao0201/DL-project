package gov.nist.csd.acpt.target;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import gov.nist.csd.acpt.util.GraphicsUtil;

public class TargetDialog extends JDialog implements ActionListener
{
    private JPanel     myPanel          = null;

    private String     TargetDialogType = null;

    private JComboBox  check            = null;
    private JLabel     attributeLabel   = null;
    private JComboBox  booleanValue     = null;
    private JTextField valueField       = null;

    private JButton    yesButton        = null;
    private JButton    noButton         = null;
    private boolean    answer           = false;
    // private String SelectedValueSet = null;
    private String     myOldType        = null;
    private String     myOldValue       = null;

    private String     myType           = null;
    private String     myValue          = null;

    public boolean getAnswer()
    {
        return answer;
    }
    // public String getSelectedString() { return SelectedValueSet; }

    public String getMyOldType()
    {
        return this.myOldType;
    }

    public String getMyOldValue()
    {
        return this.myOldValue;
    }

    public String getMyType()
    {
        return this.myType;
    }

    public String getMyValue()
    {
        return this.myValue;
    }

    public TargetDialog(JFrame frame, String command, boolean modal, boolean editable, String dialogType,
            String AttributeType, String myType, String myMessage)
    {
        super(frame, modal);

        this.TargetDialogType = dialogType;
        this.myOldType = myType;
        this.myOldValue = myMessage;
        // System.err.println(myOldType+";"+myOldValue);
        // System.err.println(myType+"dd" + dialogType +"::"+AttributeType);

        JPanel panel = null;
        Border border = null;

        myPanel = new JPanel();

        // ------------------------- Type Panel ------------------------------

        // ------------------------- Attribute Panel
        // ------------------------------

        if (dialogType.equals(TargetInfo.AttrType))
        {

            // enable change value type based on the selected type

            panel = new JPanel(new GridLayout(0, 2));
            border = BorderFactory.createTitledBorder("Attribute Add/Update");
            panel.setBorder(border);

            attributeLabel = new JLabel("Attribute Type");
            attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
            attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

            check = new JComboBox(new String[]
            {
                    "Boolean", "Integer", "String"
            });
            if (AttributeType.equals(TargetInfo.ACTION_ATTRIBUTES))
            {
                check = new JComboBox(new String[]
                {
                        "String"
                });
            }
            check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
            check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
            check.setSelectedItem(myType);
            check.addActionListener(this);
            check.setEnabled(editable);

            panel.add(attributeLabel);
            panel.add(check);
            panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
            panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
            // JPanel Attrpanel = new JPanel(new GridLayout(0, 2));
            // border = BorderFactory.createTitledBorder("Vaule");
            // panel.setBorder(border);

            attributeLabel = new JLabel("Attribute Name");
            attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
            attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

            valueField = new JTextField(myMessage);
            valueField.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
            valueField.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
            valueField.setEnabled(true);

            panel.add(attributeLabel);
            panel.add(valueField);
            myPanel.add(panel);
            /*
             * Attrpanel.add(attributeLabel); Attrpanel.add(valueField);
             * myPanel.add(Attrpanel);
             */
        }
        // ------------------------- Value Panel ------------------------------
        else if (dialogType.equals(TargetInfo.AttrValueType))
        {

            panel = new JPanel(new GridLayout(0, 2));
            border = BorderFactory.createTitledBorder("Attribute Value Add/Update");
            panel.setBorder(border);

            // enable change value type based on the selected type
            attributeLabel = new JLabel("Attribute Value Type");
            attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
            attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

            check = new JComboBox(new String[]
            {
                    "Boolean", "Integer", "String"
            });
            check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
            check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
            check.setSelectedItem(myType);
            check.setEnabled(editable);

            // check.addActionListener(this);

            panel.add(attributeLabel);
            panel.add(check);
            panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
            panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
            // myPanel.add(panel);

            // JPanel AttrValuepanel = new JPanel(new GridLayout(0, 2));
            // border = BorderFactory.createTitledBorder("Vaule");
            // panel.setBorder(border);

            attributeLabel = new JLabel(myType + " Value");
            attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
            attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

            panel.add(attributeLabel);

            if (myType.equals("Boolean"))
            {
                booleanValue = new JComboBox(new String[]
                {
                        "True", "False"
                });
                booleanValue.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                booleanValue.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                booleanValue.setEnabled(true);
                booleanValue.setSelectedItem(myMessage);
                panel.add(booleanValue);

            }
            else
            {
                valueField = new JTextField(myMessage);
                valueField.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                valueField.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                valueField.setEnabled(true);
                panel.add(valueField);
            }

            // JLabel attributeLabel3 = new JLabel("Text or Integer Value");
            // attributeLabel3.setPreferredSize(
            // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
            // attributeLabel3.setMaximumSize(
            // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

            // AttrValuepanel.add(attributeLabel);
            // AttrValuepanel.add(booleanValue);
            // AttrValuepanel.add(attributeLabel3);
            // AttrValuepanel.add(valueField);
            //
            myPanel.add(panel);

        }
        // ------------------------- Value Panel ------------------------------

        // myPanel.add(new JLabel(myMessage));
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
        // this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setMinimumSize(new Dimension(400, 250));
        this.setPreferredSize(new Dimension(400, 250));

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

            String selectedItem = (String) check.getSelectedItem();

            if (TargetDialogType.equals(TargetInfo.AttrType))
            {

                String AttrName = valueField.getText();
                AttrName = AttrName.trim();
                AttrName = AttrName.replaceAll(" ", "_");
                // SelectedValueSet = selectedItem+";"+AttrName;
                this.myType = selectedItem;
                this.myValue = AttrName;

                /*
                 * if (AttrName.trim().length()<=0){
                 * JOptionPane.showMessageDialog(null,
                 * "attribute name is not valid (length of name is less than 0)"
                 * , "Warning", JOptionPane.INFORMATION_MESSAGE);
                 * valueField.setText(""); return; }
                 */
                // addition for verification
                // add new attribute

                // Pattern p = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");
                Pattern p = Pattern.compile("[A-Za-z][A-Za-z0-9_:]*");
                Matcher m = p.matcher(AttrName);
                if (!m.matches())
                {
                    // JOptionPane.showMessageDialog(null,
                    // "Error: Invalid attribute name is used.\nCheck and follow
                    // naming rules below. \n 1. Attribute names can start with
                    // only letters.\n 2. Attribute names can only contain
                    // letters, numbers, and two special characters (: and _
                    // ).", "Warning",
                    // JOptionPane.INFORMATION_MESSAGE);
                    JOptionPane.showMessageDialog(null,
                            "Error: Invalid attribute name is used.\nCheck and follow naming rules below. \n 1. Attribute names can start with only letters.\n 2. Attribute names can only contain letters, numbers, and the underscore ( _ )",
                            "Warning", JOptionPane.INFORMATION_MESSAGE);
                    // valueField.setText("");
                    return;
                }

                // System.err.println(SelectedValueSet + valueField.getText());
            }
            else if (TargetDialogType.equals(TargetInfo.AttrValueType))
            {

                if (selectedItem.equals("Boolean"))
                {
                    // SelectedValueSet = selectedItem + ";" + (String)
                    // booleanValue.getSelectedItem();

                    this.myType = selectedItem;
                    this.myValue = (String) booleanValue.getSelectedItem();

                }
                else if (selectedItem.equals("Integer"))
                {
                    String AttrValue = valueField.getText();
                    AttrValue = AttrValue.trim();
                    AttrValue = AttrValue.replaceAll(" ", "_");
                    // SelectedValueSet = selectedItem + ";" + AttrValue;

                    this.myType = selectedItem;
                    this.myValue = AttrValue;

                    try
                    {
                        Integer.parseInt(AttrValue);
                    }
                    catch (Exception e2)
                    {
                        JOptionPane.showMessageDialog(null, "Pleas write an integer correctly", "Warning",
                                JOptionPane.INFORMATION_MESSAGE);
                        valueField.setText("");
                        return;
                    }
                    if (AttrValue.trim().length() <= 0)
                    {
                        JOptionPane.showMessageDialog(null,
                                "attribute name is not valid (length of name is less than 0)", "Warning",
                                JOptionPane.INFORMATION_MESSAGE);
                        valueField.setText("");
                        return;
                    }

                }
                else if (selectedItem.equals("String"))
                {
                    // SelectedValueSet = selectedItem + ";" + (String)
                    // valueField.getText();
                    String AttrValue = valueField.getText();
                    AttrValue = AttrValue.trim();
                    AttrValue = AttrValue.replaceAll(" ", "_");
                    this.myType = selectedItem;
                    this.myValue = AttrValue;

                    // if (AttrValue.trim().length()<=0){
                    // JOptionPane.showMessageDialog(null,
                    // "3attribute name is not valid (length of name is less
                    // than 0)", "Warning",
                    // JOptionPane.INFORMATION_MESSAGE);
                    // valueField.setText("");
                    // return;
                    // }

                    Pattern p = Pattern.compile("[A-Za-z][A-Za-z0-9_:]*");
                    // Pattern p = Pattern.compile("[A-Za-z][A-Za-z0-9_]*");
                    Matcher m = p.matcher(AttrValue);
                    if (!m.matches())
                    {
                        // JOptionPane.showMessageDialog(null,
                        // "Error: Invalid attribute name is used.\nCheck and
                        // follow naming rules below. \n 1. Attribute names can
                        // start with only letters.\n 2. Attribute names can
                        // only contain letters, numbers, and two special
                        // characters (: and _ ).", "Warning",
                        // JOptionPane.INFORMATION_MESSAGE);
                        JOptionPane.showMessageDialog(null,
                                "Error: Invalid attribute name is used.\nCheck and follow naming rules below. \n 1. Attribute names can start with only letters.\n 2. Attribute names can only contain letters, numbers, and the underscore ( _ )",
                                "Warning", JOptionPane.INFORMATION_MESSAGE);
                        // valueField.setText("");
                        return;
                    }
                }
            }
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
