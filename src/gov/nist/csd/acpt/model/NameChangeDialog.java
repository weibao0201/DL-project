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

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.generic.GenericInfo;
import gov.nist.csd.acpt.generic.GenericTreeUtil;
import gov.nist.csd.acpt.util.GraphicsUtil;

public class NameChangeDialog extends JDialog implements ActionListener
{
    private JPanel                 myPanel          = null;

    private String                 TargetDialogType = null;

    private JComboBox              check            = null;
    private JLabel                 attributeLabel   = null;
    private JComboBox              booleanValue     = null;
    private JTextField             OldvalueField    = null;
    private JTextField             valueField       = null;

    private JButton                yesButton        = null;
    private JButton                noButton         = null;
    private boolean                answer           = false;
    // private String SelectedValueSet = null;

    private ACPTFrame              frame            = null;
    private JTree                  modelTree        = null;

    private DefaultMutableTreeNode myNode           = null;
    private String                 myOldType        = null;
    private String                 myOldValue       = null;
    private String                 myNewValue       = null;

    // private String myType = null;
    // private String myValue = null;

    public boolean getAnswer()
    {
        return answer;
    }

    public String getNewValue()
    {
        return myNewValue;
    }

    // public String getSelectedString() { return SelectedValueSet; }

    // public String getMyOldType() { return this.myOldType; }
    // public String getMyOldValue() { return this.myOldValue; }

    // public String getMyType() { return this.myType; }
    // public String getMyValue() { return this.myValue; }

    public NameChangeDialog(ACPTFrame frame, boolean modal, JTree modelTree, DefaultMutableTreeNode curNode)
    {
        super(frame, modal);

        String modelType = ((ModelInfo) curNode.getUserObject()).getModelType();
        String value = ((ModelInfo) curNode.getUserObject()).getValue();

        this.frame = frame;
        this.modelTree = modelTree;
        this.myNode = curNode;
        this.myOldType = modelType;
        this.myOldValue = value;

        JPanel panel = null;
        Border border = null;

        myPanel = new JPanel();

        // ------------------------- Type Panel ------------------------------

        // ------------------------- Attribute Panel
        // ------------------------------

        // enable change value type based on the selected type

        panel = new JPanel(new GridLayout(0, 2));
        border = BorderFactory.createTitledBorder(myOldType + " Policy Name Change");
        panel.setBorder(border);

        attributeLabel = new JLabel("Policy Model Name");
        attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        OldvalueField = new JTextField(myOldValue);
        OldvalueField.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        OldvalueField.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        OldvalueField.setEnabled(false);

        panel.add(attributeLabel);
        panel.add(OldvalueField);

        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));

        attributeLabel = new JLabel("New Policy Model Name");
        attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        valueField = new JTextField();
        valueField.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        valueField.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        valueField.setEnabled(true);

        panel.add(attributeLabel);
        panel.add(valueField);
        myPanel.add(panel);

        /*
         *
         * //------------------------- Value Panel
         * ------------------------------ else if
         * (dialogType.equals(TargetInfo.AttrValueType)){
         *
         * panel = new JPanel(new GridLayout(0, 2)); border =
         * BorderFactory.createTitledBorder("Attribute Value Add/Update");
         * panel.setBorder(border);
         *
         *
         * // enable change value type based on the selected type attributeLabel
         * = new JLabel("Attribute Value Type");
         * attributeLabel.setPreferredSize( new Dimension(75,
         * GraphicsUtil.FIELD_HEIGHT)); attributeLabel.setMaximumSize( new
         * Dimension(75, GraphicsUtil.FIELD_HEIGHT));
         *
         * check = new JComboBox(new String[]{"Boolean", "Integer", "String"});
         * check.setPreferredSize(new Dimension(150,
         * GraphicsUtil.FIELD_HEIGHT)); check.setMaximumSize(new Dimension(150,
         * GraphicsUtil.FIELD_HEIGHT)); check.setSelectedItem(myType);
         * check.setEnabled(editable);
         *
         * //check.addActionListener(this);
         *
         * panel.add(attributeLabel); panel.add(check);
         * panel.add(Box.createRigidArea(new
         * Dimension(15,GraphicsUtil.DEFAULT_GAP)));
         * panel.add(Box.createRigidArea(new
         * Dimension(15,GraphicsUtil.DEFAULT_GAP))); // myPanel.add(panel);
         *
         *
         * // JPanel AttrValuepanel = new JPanel(new GridLayout(0, 2)); //
         * border = BorderFactory.createTitledBorder("Vaule"); //
         * panel.setBorder(border);
         *
         * attributeLabel = new JLabel(myType + " Value");
         * attributeLabel.setPreferredSize( new Dimension(75,
         * GraphicsUtil.FIELD_HEIGHT)); attributeLabel.setMaximumSize( new
         * Dimension(75, GraphicsUtil.FIELD_HEIGHT));
         *
         * panel.add(attributeLabel);
         *
         * if (myType.equals("Boolean")){ booleanValue = new JComboBox(new
         * String[]{"True", "False"}); booleanValue.setPreferredSize(new
         * Dimension(150, GraphicsUtil.FIELD_HEIGHT));
         * booleanValue.setMaximumSize(new Dimension(150,
         * GraphicsUtil.FIELD_HEIGHT)); booleanValue.setEnabled(true);
         * booleanValue.setSelectedItem(myMessage); panel.add(booleanValue);
         *
         * }else{ valueField = new JTextField(myMessage);
         * valueField.setPreferredSize( new Dimension(150,
         * GraphicsUtil.FIELD_HEIGHT)); valueField.setMaximumSize( new
         * Dimension(150, GraphicsUtil.FIELD_HEIGHT));
         * valueField.setEnabled(true); panel.add(valueField); }
         *
         *
         *
         * // myPanel.add(panel);
         *
         * }
         */
        // ------------------------- Value Panel ------------------------------

        // myPanel.add(new JLabel(myMessage));
        panel = new JPanel(new GridLayout(0, 2));
        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        panel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));

        yesButton = new JButton("Change");
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

        if (yesButton == e.getSource())
        {

            String newName = valueField.getText().trim();

            // empty, null, or conflicting name checking
            if ((newName == null) || newName.equals(""))
            {
                GraphicsUtil.showWarningDialog("Warning: Null or empty policy name.", "Incorrect Name Warning", null);
                return;
            }
            if (!ModelTreeUtil.IsPolicyModelNameNonConflict(myOldType, newName, modelTree))
            {
                return;
            }

            // policy names (within a current node) is updated by changing its
            // ModelInfo

            ModelInfo NewModelInfo = new ModelInfo(myOldType, newName);
            myNode.setUserObject(NewModelInfo);

            // update combination tab
            JTree combineTree = frame.modelCombinePanel.getGenericTree();
            DefaultMutableTreeNode matchingNode = GenericTreeUtil.getFirstMatchingNode(combineTree,
                    myOldType + "#" + myOldValue, "COMBINATION", GenericInfo.NodeLevelofmodelroot);
            if (matchingNode != null)
            {
                GenericInfo NewGenericInfo = new GenericInfo("COMBINATION", myOldType + "#" + newName);
                matchingNode.setUserObject(NewGenericInfo);
            }

            myNewValue = newName;
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
