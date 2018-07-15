package gov.nist.csd.acpt.generic;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.GraphicsUtil;

public class PropertyDialogForAttributeSelection extends JDialog implements ActionListener
{
    private JPanel         myPanel                  = null;
    private JButton        yesButton                = null;
    private JButton        noButton                 = null;
    private JButton        getButton                = null;
    private JButton        eraseButton              = null;

    private boolean        answer                   = false;
    CheckCombBoxLst2       checkCompLst             = null;

    private String         DialogType               = null;
    // private JComboBox initialState = null;
    // private JComboBox decision = null;
    // private JComboBox propertytype = null;
    // private JTextArea propertytextArea = null;

    private int            countSubject             = 0;
    private int            countResource            = 0;
    private int            countAction              = 0;
    private int            countEnviroment          = 0;

    private String         myText                   = "";

    DefaultMutableTreeNode targetSubjectRootNode    = null;
    DefaultMutableTreeNode targetResourceRootNode   = null;
    DefaultMutableTreeNode targetActionRootNode     = null;
    DefaultMutableTreeNode targetEnviromentRootNode = null;

    public List<DefaultMutableTreeNode> getSelectedNodeLst()
    {
        return checkCompLst.getSelectedNodeLst();
    }

    // public CheckCombBoxLst2 getcheckCompLst() { return checkCompLst; }
    // public String getMyText() { return this.myText; }

    public boolean getAnswer()
    {
        return answer;
    }

    public PropertyDialogForAttributeSelection(JFrame frame, boolean modal, String DialogType, JTree targetTree,
            String myMessage, String dialogPurpose, DefaultMutableTreeNode curNode)
    {
        super(frame, modal);

        this.DialogType = DialogType;
        checkCompLst = new CheckCombBoxLst2();
        targetSubjectRootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.SUBJECTS, TargetInfo.Level);
        targetResourceRootNode =
                TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.RESOURCES, TargetInfo.Level);
        targetActionRootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.ACTIONS, TargetInfo.Level);
        targetEnviromentRootNode =
                TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.ENVIRONMENTS, TargetInfo.Level);

        myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        int checkount = 0;
        JPanel panel = null;
        Border border = null;

        JCheckBox check = null;
        JLabel attributeLabel = null;

        ///

        // panel = new JPanel(new GridLayout(0, 4));
        // border = BorderFactory.createTitledBorder("Process States if you use
        // workflow models");
        // panel.setBorder(border);
        //
        // panel.setMaximumSize(new Dimension(500,
        // GraphicsUtil.FIELD_HEIGHT_Large));
        //
        // attributeLabel = new JLabel(" * "+ "Process States");
        // attributeLabel.setPreferredSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        // attributeLabel.setMaximumSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        // check = new JComboBox();
        // check.setPreferredSize(new Dimension(150,
        // GraphicsUtil.FIELD_HEIGHT));
        // check.setMaximumSize(new Dimension(150,
        // GraphicsUtil.FIELD_HEIGHT));
        //
        //
        // check.addItem("Any "+ "State");
        // for (int i = 0; i < Generalproperties.ProcessStateNumber; i++) {
        // check.addItem(Generalproperties.ProcessStateNumberArray[i]);
        // }
        // check.addItem("Any "+ "State");
        // check.addItem("1");

        // checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode)
        // choices.get(i));
        // checkCompLst.addCheckWithNode(check, new DefaultMutableTreeNode(
        // new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES,
        // "Integer;Process_State")));
        // panel.add(attributeLabel);
        // panel.add(check);
        // myPanel.add(panel);

        // DefaultMutableTreeNode roles = new DefaultMutableTreeNode(
        // new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, "String;Roles"));

        ///
        panel = new JPanel(new GridLayout(0, 1));
        JLabel info1 = new JLabel("* Select at least one attribute that are used for property checking.");
        JLabel info2 = new JLabel("* ACPT sets unselected attributes' values as default dummy values.");
        // JLabel info = new JLabel("* Select at least one attribute that are
        // used for property checking.");
        panel.add(info1);
        panel.add(info2);
        panel.setMaximumSize(new Dimension(500, GraphicsUtil.FIELD_HEIGHT_Large));
        myPanel.add(panel);

        // JTextArea textArea = new JTextArea();
        // JScrollPane scrollPane = new JScrollPane(textArea);
        //
        // String info = "Select at least one attribute that are used for
        // property checking. Note that ACPT uses only selected attributes for
        // verifying each property against a policy. ACPT sets unselected
        // attributes' values as default dummy values.";
        //
        // textArea.append(info);
        // textArea.setLineWrap(true);
        // textArea.setWrapStyleWord(true);
        // textArea.setEditable(false);
        // //textArea.setBackground(new Color(230, 230, 230));
        // textArea.setBackground(null);
        // textArea.setBorder(null);
        // scrollPane.setPreferredSize(new Dimension(550, 50));
        // scrollPane.setMaximumSize(new Dimension(550, 150));
        // myPanel.add(scrollPane);
        //

        panel = new JPanel(new GridLayout(0, 4));
        border = BorderFactory.createTitledBorder(TargetInfo.SUBJECT_ATTRIBUTES);
        panel.setBorder(border);

        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(targetSubjectRootNode,
                    TargetInfo.SUBJECT_ATTRIBUTES, TargetInfo.attrLevel);
            countSubject = choices.size();
            if (choices.size() == 0)
            {
                JLabel infoLabel = new JLabel("No attributes are available");
                panel.add(infoLabel);
                panel.add(Box.createRigidArea(new Dimension(150, GraphicsUtil.GAP_ZERO)));
                panel.setMaximumSize(new Dimension(500, GraphicsUtil.FIELD_HEIGHT_Large));
            }
            else
            {
                panel.setMaximumSize(
                        new Dimension(500, (((countSubject - 1) / 4) + 1) * GraphicsUtil.FIELD_HEIGHT_Large));
                // panel.setMaximumSize(new Dimension(500, (countSubject/4+
                // countSubject%4)*GraphicsUtil.FIELD_HEIGHT_Large));
            }
            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();
                check = new JCheckBox(valueOfvalue);
                checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode) choices.get(i));
                panel.add(check);
            }
        }

        myPanel.add(panel);

        panel = new JPanel(new GridLayout(0, 4));
        border = BorderFactory.createTitledBorder(TargetInfo.RESOURCE_ATTRIBUTES);
        panel.setBorder(border);
        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(targetResourceRootNode,
                    TargetInfo.RESOURCE_ATTRIBUTES, TargetInfo.attrLevel);
            countResource = choices.size();
            if (choices.size() == 0)
            {
                JLabel infoLabel = new JLabel("No attributes are available");
                panel.add(infoLabel);
                panel.add(Box.createRigidArea(new Dimension(150, GraphicsUtil.GAP_ZERO)));
                panel.setMaximumSize(new Dimension(500, GraphicsUtil.FIELD_HEIGHT_Large));
            }
            else
            {
                panel.setMaximumSize(
                        new Dimension(500, (((countResource - 1) / 4) + 1) * GraphicsUtil.FIELD_HEIGHT_Large));
                // panel.setMaximumSize(new Dimension(500, (countResource/4+
                // countResource%4)*GraphicsUtil.FIELD_HEIGHT_Large));
            }

            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();
                check = new JCheckBox(valueOfvalue);
                checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode) choices.get(i));
                panel.add(check);
            }
        }

        myPanel.add(panel);

        panel = new JPanel(new GridLayout(0, 4));
        border = BorderFactory.createTitledBorder(TargetInfo.ACTION_ATTRIBUTES);
        panel.setBorder(border);
        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(targetActionRootNode,
                    TargetInfo.ACTION_ATTRIBUTES, TargetInfo.attrLevel);

            countAction = choices.size();

            if (choices.size() == 0)
            {
                JLabel infoLabel = new JLabel("No attributes are available");
                panel.add(infoLabel);
                panel.add(Box.createRigidArea(new Dimension(150, GraphicsUtil.GAP_ZERO)));
                panel.setMaximumSize(new Dimension(500, GraphicsUtil.FIELD_HEIGHT_Large));
            }
            else
            {
                panel.setMaximumSize(
                        new Dimension(500, (((countAction - 1) / 4) + 1) * GraphicsUtil.FIELD_HEIGHT_Large));
                // panel.setMaximumSize(new Dimension(500, (countAction/4+
                // countAction%4)*GraphicsUtil.FIELD_HEIGHT_Large));
            }

            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();
                check = new JCheckBox(valueOfvalue);
                checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode) choices.get(i));
                panel.add(check);
            }
        }

        myPanel.add(panel);

        panel = new JPanel(new GridLayout(0, 4));
        border = BorderFactory.createTitledBorder(TargetInfo.ENVIRONMENT_ATTRIBUTES);
        panel.setBorder(border);
        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(targetEnviromentRootNode,
                    TargetInfo.ENVIRONMENT_ATTRIBUTES, TargetInfo.attrLevel);

            countEnviroment = choices.size();

            if (choices.size() == 0)
            {
                JLabel infoLabel = new JLabel("No attributes are available");
                panel.add(infoLabel);
                panel.add(Box.createRigidArea(new Dimension(150, GraphicsUtil.GAP_ZERO)));
                panel.setMaximumSize(new Dimension(500, GraphicsUtil.FIELD_HEIGHT_Large));
            }
            else
            {
                panel.setMaximumSize(
                        new Dimension(500, (((countEnviroment - 1) / 4) + 1) * GraphicsUtil.FIELD_HEIGHT_Large));
                // panel.setMaximumSize(new Dimension(500, (countEnviroment/4+
                // countEnviroment%4)*GraphicsUtil.FIELD_HEIGHT_Large));
            }

            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();
                check = new JCheckBox(valueOfvalue);
                checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode) choices.get(i));
                panel.add(check);
            }
        }

        myPanel.add(panel);

        // String[] decisionSet = null;
        // String titleOfdecision = null;
        //
        // titleOfdecision = "Property Type and Decision Selection";
        // decisionSet = new String[] {"Permit", "Deny"};
        //
        //
        // panel = new JPanel(new GridLayout(0, 4));
        //
        // border = BorderFactory.createTitledBorder("Property type and
        // decision");
        // panel.setBorder(border);
        //
        // panel.setMinimumSize(new Dimension(500, 0));
        //// panel.setPreferredSize(new Dimension(500, 0));
        // panel.setMaximumSize(new Dimension(500,
        // GraphicsUtil.FIELD_HEIGHT_Large));
        //
        // attributeLabel = new JLabel("Decision");
        // attributeLabel.setPreferredSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        // attributeLabel.setMaximumSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        //
        // decision = new JComboBox(decisionSet);
        //
        // decision.setPreferredSize(new Dimension(150,
        // GraphicsUtil.FIELD_HEIGHT));
        // decision.setMaximumSize(new Dimension(150,
        // GraphicsUtil.FIELD_HEIGHT));
        //
        // panel.add(attributeLabel);
        // panel.add(decision);
        //
        // attributeLabel = new JLabel(" Property");
        // attributeLabel.setPreferredSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        // attributeLabel.setMaximumSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        //
        // propertytype = new JComboBox(new String[] {"SPEC", "LTLSPEC"});
        //
        // propertytype.setPreferredSize(new Dimension(150,
        // GraphicsUtil.FIELD_HEIGHT));
        // propertytype.setMaximumSize(new Dimension(150,
        // GraphicsUtil.FIELD_HEIGHT));
        //
        // panel.add(attributeLabel);
        // panel.add(propertytype);
        //
        // myPanel.add(panel);
        //
        // propertytextArea = new JTextArea();
        // JScrollPane scrollPane = new JScrollPane(propertytextArea);
        // propertytextArea.append(myMessage);
        // propertytextArea.setLineWrap(true);
        // propertytextArea.setWrapStyleWord(true);
        // propertytextArea.setEditable(true);
        // scrollPane.setPreferredSize(new Dimension(550, 100));
        // scrollPane.setMaximumSize(new Dimension(550, 200));
        // myPanel.add(scrollPane);
        //

        panel = new JPanel(new GridLayout(0, 2));
        panel.setMinimumSize(new Dimension(300, 0));
        // panel.setPreferredSize(new Dimension(500, 0));
        panel.setMaximumSize(new Dimension(300, GraphicsUtil.FIELD_HEIGHT));
        //
        // getButton = new JButton("Sample Property");
        // getButton.setPreferredSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // getButton.setMaximumSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // getButton.addActionListener(this);
        // panel.add(getButton);

        // eraseButton = new JButton("Erase");
        // eraseButton.addActionListener(this);
        // eraseButton.setPreferredSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // eraseButton.setMaximumSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // panel.add(eraseButton);

        JPanel subpanel = new JPanel();
        subpanel.setLayout(new BoxLayout(subpanel, BoxLayout.X_AXIS));

        yesButton = new JButton("Select");
        yesButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        yesButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        yesButton.addActionListener(this);
        // panel.add(yesButton);
        subpanel.add(yesButton);
        noButton = new JButton("Cancel");
        noButton.addActionListener(this);
        noButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        noButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // panel.add(noButton);
        subpanel.add(noButton);
        // pack();

        // myPanel.add(panel);
        myPanel.add(subpanel);
        myPanel.setMaximumSize(new Dimension(500, GraphicsUtil.FIELD_HEIGHT_Large));

        ////////////////////////////////////////////////////////////

        // JTextArea textArea = new JTextArea();
        // scrollPane = new JScrollPane(textArea);
        //
        // String info = "Property Generation Tips:\nStep1) Select at least one
        // attribute (that is not \"any\" attribute) of interest in drop-down
        // boxes " +
        // "\nStep2) Click \"Sample Property\" button to generate sample
        // proprety" +
        // "\nStep3) Modify a sample property using notations. Examples
        // notations are & (and), | (or), = (eqaul), != (not equal), < (less
        // than), > (greater than), (, )." +
        // "\nStep4) Click \"Confirm/Add\" or \"Confirm/Update\" button to
        // add/edit the generated property";
        //
        // textArea.append(info);
        // textArea.setLineWrap(true);
        // textArea.setWrapStyleWord(true);
        // textArea.setEditable(false);
        // //textArea.setBackground(new Color(230, 230, 230));
        // textArea.setBackground(null);
        // scrollPane.setPreferredSize(new Dimension(550, 50));
        // scrollPane.setMaximumSize(new Dimension(550, 300));
        // myPanel.add(scrollPane);
        //

        this.add(myPanel);
        int height = 350;
        if (checkCompLst.getchecksLst().size() >= 10)
        {
            height = 350 + ((checkCompLst.getchecksLst().size() - 9) * 10);
        }
        this.setMinimumSize(new Dimension(600, height));
        this.setPreferredSize(new Dimension(600, height));

        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        // System.out.println(e.toString());
        if (yesButton == e.getSource())
        {

            // this.myText = propertytextArea.getText();
            // propertytextArea.setText(this.myText);

            // selectedAttributesNodeLst =

            answer = true;
            setVisible(false);
        }
        else if (noButton == e.getSource())
        {
            answer = false;
            setVisible(false);
        }
    }

    /***************************************************************************
     * Inner classes
     **************************************************************************/
    class CheckCombBoxLst2
    {

        private List<JCheckBox>              checksLst     = null;
        private List<DefaultMutableTreeNode> checksNodeLst = null;

        public List<JCheckBox> getchecksLst()
        {
            return this.checksLst;
        }

        CheckCombBoxLst2()
        {

            checksLst = new ArrayList<JCheckBox>();
            checksNodeLst = new ArrayList<DefaultMutableTreeNode>();
        }

        public boolean addCheckWithNode(JCheckBox check, DefaultMutableTreeNode node)
        {
            checksLst.add(check);
            checksNodeLst.add(node);
            return true;
        }

        public List<DefaultMutableTreeNode> getSelectedNodeLst()
        {

            List<DefaultMutableTreeNode> selectedNodeLst = new ArrayList<DefaultMutableTreeNode>();
            for (int i = 0; i < checksLst.size(); i++)
            {
                if (checksLst.get(i).isSelected())
                {
                    selectedNodeLst.add(checksNodeLst.get(i));
                }
            }
            return selectedNodeLst;
        }

        /*
         * public List<DefaultMutableTreeNode> getSelectedNodeLst() {
         *
         * List<DefaultMutableTreeNode> selectedNodeLst = new
         * ArrayList<DefaultMutableTreeNode>(); for (int i = 0; i <
         * checksLst.size(); i++) {
         *
         *
         * if (((JComboBox) checksLst.get(i)).getSelectedIndex() != 0){
         * selectedNodeLst.add(checksNodeLst.get(i)); }
         *
         * } return selectedNodeLst; }
         *
         * public int selectedCount() { int selectedCount = 0;
         *
         * for (int i = 0; i < checksLst.size(); i++) { if (((JComboBox)
         * checksLst.get(i)).getSelectedIndex() != 0){ selectedCount++; } }
         * return selectedCount; } public String getPropertyString() {
         *
         * String rule = "";
         *
         * for (int i = 0; i < checksLst.size(); i++) {
         *
         *
         * //if (i == 0){rule += "[";}
         *
         * if (((JComboBox) checksLst.get(i)).getSelectedIndex() != 0){
         *
         * String TargetType = ((TargetInfo)((DefaultMutableTreeNode)
         * checksNodeLst.get(i)).getUserObject()).getTargetType(); String
         * valueOfvalue = ((TargetInfo)((DefaultMutableTreeNode)
         * checksNodeLst.get(i)).getUserObject()).getvalueOfvalue(); String
         * typeOfvalue = ((TargetInfo)((DefaultMutableTreeNode)
         * checksNodeLst.get(i)).getUserObject()).gettypeOfvalue(); String temp
         * = "(" + valueOfvalue + " = " + ((JComboBox)
         * checksLst.get(i)).getSelectedItem().toString() + ")"; if
         * (rule.equals("")){rule = temp;} else {rule += " & " + temp;} } }
         *
         *
         * return rule; }
         */
    }
}
