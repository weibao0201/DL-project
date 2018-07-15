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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.Generalproperties;
import gov.nist.csd.acpt.util.GraphicsUtil;

public class PropertyDialog extends JDialog implements ActionListener
{
    private JPanel         myPanel                  = null;
    private JButton        yesButton                = null;
    private JButton        noButton                 = null;
    private JButton        getButton                = null;
    private JButton        eraseButton              = null;

    private boolean        answer                   = false;
    CheckCombBoxLst3       checkCompLst             = null;
    private String         DialogType               = null;
    private JComboBox      initialState             = null;
    private JComboBox      decision                 = null;
    private JComboBox      propertytype             = null;
    private JTextArea      propertytextArea         = null;

    private int            countSubject             = 0;
    private int            countResource            = 0;
    private int            countAction              = 0;
    private int            countEnviroment          = 0;

    private String         myText                   = "";
    private List<String>   nodeLst                  = null;

    DefaultMutableTreeNode targetSubjectRootNode    = null;
    DefaultMutableTreeNode targetResourceRootNode   = null;
    DefaultMutableTreeNode targetActionRootNode     = null;
    DefaultMutableTreeNode targetEnviromentRootNode = null;

    public CheckCombBoxLst3 getcheckCompLst()
    {
        return checkCompLst;
    }

    public String getMyText()
    {
        return this.myText;
    }

    public String getMySelectedAttributes()
    {
        return this.nodeLst.toString();
    }

    public boolean getAnswer()
    {
        return answer;
    }

    public PropertyDialog(JFrame frame, boolean modal, String DialogType, JTree targetTree, String myMessage,
            String dialogPurpose, DefaultMutableTreeNode curNode, List<String> nodeLst)
    {
        super(frame, modal);

        this.DialogType = DialogType;
        this.nodeLst = nodeLst;
        checkCompLst = new CheckCombBoxLst3();
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

        JComboBox check = null;
        JLabel attributeLabel = null;

        ///

        panel = new JPanel(new GridLayout(0, 4));
        border = BorderFactory.createTitledBorder("Process States if you use workflow models");
        panel.setBorder(border);

        panel.setMaximumSize(new Dimension(500, GraphicsUtil.FIELD_HEIGHT_Large));

        attributeLabel = new JLabel("    * " + "Process States");
        attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        check = new JComboBox();
        check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        check.addItem("Any " + "State");
        for (int i = 0; i < Generalproperties.ProcessStateNumber; i++)
        {
            check.addItem(Generalproperties.ProcessStateNumberArray[i]);
        }
        // check.addItem("Any "+ "State");
        // check.addItem("1");

        // checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode)
        // choices.get(i));
        checkCompLst.addCheckWithNode(check,
                new DefaultMutableTreeNode(new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, "Integer;Process_State")));
        panel.add(attributeLabel);
        panel.add(check);
        myPanel.add(panel);

        // DefaultMutableTreeNode roles = new DefaultMutableTreeNode(
        // new TargetInfo(TargetInfo.SUBJECT_ATTRIBUTES, "String;Roles"));

        ///

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
                panel.setMaximumSize(new Dimension(500,
                        ((countSubject / 2) + (countSubject % 2)) * GraphicsUtil.FIELD_HEIGHT_Large));
            }
            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();

                if (nodeLst.contains(valueOfvalue))
                {

                    attributeLabel = new JLabel("    * " + valueOfvalue);
                    attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                    attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                    check = new JComboBox();
                    check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                    check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

                    List choices2 =
                            TargetTreeUtil.getTargetTreeChildrenNode2List((DefaultMutableTreeNode) choices.get(i),
                                    TargetInfo.SUBJECT_ATTR_VALUSES, TargetInfo.nodeLevel);
                    check.addItem("Any " + valueOfvalue);
                    for (int j = 0; j < choices2.size(); j++)
                    {
                        check.addItem(choices2.get(j));
                    }

                    check.setActionCommand("Check:" + (checkount++));
                    checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode) choices.get(i));
                    panel.add(attributeLabel);
                    panel.add(check);
                }
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
                panel.setMaximumSize(new Dimension(500,
                        ((countResource / 2) + (countResource % 2)) * GraphicsUtil.FIELD_HEIGHT_Large));
            }

            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();
                if (nodeLst.contains(valueOfvalue))
                {

                    attributeLabel = new JLabel("    * " + valueOfvalue);
                    attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                    attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                    check = new JComboBox();
                    check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                    check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

                    List choices2 =
                            TargetTreeUtil.getTargetTreeChildrenNode2List((DefaultMutableTreeNode) choices.get(i),
                                    TargetInfo.RESOURCE_ATTR_VALUSES, TargetInfo.nodeLevel);

                    check.addItem("Any " + valueOfvalue);
                    for (int j = 0; j < choices2.size(); j++)
                    {
                        check.addItem(choices2.get(j));
                    }

                    check.setActionCommand("Check:" + (checkount++));
                    checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode) choices.get(i));
                    panel.add(attributeLabel);
                    panel.add(check);
                }
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
                        new Dimension(500, ((countAction / 2) + (countAction % 2)) * GraphicsUtil.FIELD_HEIGHT_Large));
            }

            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();

                if (nodeLst.contains(valueOfvalue))
                {
                    attributeLabel = new JLabel("    * " + valueOfvalue);
                    attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                    attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                    check = new JComboBox();
                    check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                    check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

                    List choices2 =
                            TargetTreeUtil.getTargetTreeChildrenNode2List((DefaultMutableTreeNode) choices.get(i),
                                    TargetInfo.ACTION_ATTR_VALUSES, TargetInfo.nodeLevel);

                    check.addItem("Any " + valueOfvalue);
                    for (int j = 0; j < choices2.size(); j++)
                    {
                        check.addItem(choices2.get(j));
                    }

                    check.setActionCommand("Check:" + (checkount++));
                    checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode) choices.get(i));
                    panel.add(attributeLabel);
                    panel.add(check);
                }
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
                panel.setMaximumSize(new Dimension(500,
                        ((countEnviroment / 2) + (countEnviroment % 2)) * GraphicsUtil.FIELD_HEIGHT_Large));
            }

            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();

                if (nodeLst.contains(valueOfvalue))
                {
                    attributeLabel = new JLabel("    * " + valueOfvalue);
                    attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                    attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                    check = new JComboBox();
                    check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                    check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

                    List choices2 =
                            TargetTreeUtil.getTargetTreeChildrenNode2List((DefaultMutableTreeNode) choices.get(i),
                                    TargetInfo.ENVIRONMENT_ATTR_VALUSES, TargetInfo.nodeLevel);

                    check.addItem("Any " + valueOfvalue);
                    for (int j = 0; j < choices2.size(); j++)
                    {
                        check.addItem(choices2.get(j));
                    }

                    check.setActionCommand("Check:" + (checkount++));
                    checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode) choices.get(i));
                    panel.add(attributeLabel);
                    panel.add(check);
                }
            }
        }

        myPanel.add(panel);

        String[] decisionSet = null;
        String titleOfdecision = null;

        titleOfdecision = "Property Type and Decision Selection";
        decisionSet = new String[]
        {
                "Permit", "Deny"
        };

        panel = new JPanel(new GridLayout(0, 4));

        border = BorderFactory.createTitledBorder("Security requirement type and decision");
        panel.setBorder(border);

        panel.setMinimumSize(new Dimension(500, 0));
        // panel.setPreferredSize(new Dimension(500, 0));
        panel.setMaximumSize(new Dimension(500, GraphicsUtil.FIELD_HEIGHT_Large));

        attributeLabel = new JLabel("Decision");
        attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        decision = new JComboBox(decisionSet);

        decision.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        decision.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        panel.add(attributeLabel);
        panel.add(decision);

        attributeLabel = new JLabel("      Property");
        attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        propertytype = new JComboBox(new String[]
        {
                "SPEC", "LTLSPEC"
        });

        propertytype.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        propertytype.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        panel.add(attributeLabel);
        panel.add(propertytype);

        myPanel.add(panel);

        propertytextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(propertytextArea);
        propertytextArea.append(myMessage);
        propertytextArea.setLineWrap(true);
        propertytextArea.setWrapStyleWord(true);
        propertytextArea.setEditable(true);
        scrollPane.setPreferredSize(new Dimension(550, 100));
        scrollPane.setMaximumSize(new Dimension(550, 200));
        myPanel.add(scrollPane);

        panel = new JPanel(new GridLayout(0, 3));
        panel.setMinimumSize(new Dimension(500, 0));
        // panel.setPreferredSize(new Dimension(500, 0));
        panel.setMaximumSize(new Dimension(500, GraphicsUtil.FIELD_HEIGHT));

        getButton = new JButton("Sample Security Requirement");
        getButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        getButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        getButton.addActionListener(this);
        panel.add(getButton);

        // eraseButton = new JButton("Erase");
        // eraseButton.addActionListener(this);
        // eraseButton.setPreferredSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // eraseButton.setMaximumSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // panel.add(eraseButton);

        yesButton = new JButton("Confirm/" + dialogPurpose);
        yesButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        yesButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        yesButton.addActionListener(this);
        panel.add(yesButton);
        noButton = new JButton("Cancel");
        noButton.addActionListener(this);
        noButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        noButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        panel.add(noButton);
        // pack();

        myPanel.add(panel);

        ////////////////////////////////////////////////////////////

        JTextArea textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);

        String info =
                "Property Generation Tips:\nStep1) Select at least one attribute (that is not \"any\" attribute) of interest in drop-down boxes "
                        + "\nStep2) Click \"Sample Property\" button to generate sample proprety"
                        + "\nStep3) Modify a sample property using notations. Examples notations are & (and), | (or),  = (eqaul), != (not equal), < (less than), > (greater than), (, )."
                        + "\nStep4) Click \"Confirm/Add\" or  \"Confirm/Update\" button to add/edit the generated property";

        textArea.append(info);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        // textArea.setBackground(new Color(230, 230, 230));
        textArea.setBackground(null);
        scrollPane.setPreferredSize(new Dimension(550, 50));
        scrollPane.setMaximumSize(new Dimension(550, 300));
        myPanel.add(scrollPane);

        this.add(myPanel);
        int height = 500;
        if (checkCompLst.getchecksLst().size() >= 10)
        {
            height = 500 + ((checkCompLst.getchecksLst().size() - 9) * 25);
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

        if (getButton == e.getSource())
        {

            String propType = propertytype.getSelectedItem().toString();
            String Propertytext = null;

            if (checkCompLst.getPropertyString().equals(""))
            {
                // Warning
                GraphicsUtil.showWarningDialog(
                        "select at least one attribute (which is not \"any \" attribute) to generate sample property",
                        "Null Warning", null);
                return;
            }

            if (propType.equals("SPEC"))
            {
                Propertytext = "SPEC " + checkCompLst.getPropertyString() + "  -> decision =  "
                        + decision.getSelectedItem().toString();

            }
            else
            {
                Propertytext = "LTLSPEC  " + checkCompLst.getPropertyString() + "  -> decision =  "
                        + decision.getSelectedItem().toString();
            }

            propertytextArea.setText(Propertytext);

        }
        else if (eraseButton == e.getSource())
        {

            propertytextArea.setText("");
        }
        else if (yesButton == e.getSource())
        {

            this.myText = propertytextArea.getText();
            propertytextArea.setText(this.myText);

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
    class CheckCombBoxLst3
    {

        private List<JComboBox>              checksLst     = null;
        private List<DefaultMutableTreeNode> checksNodeLst = null;

        public List<JComboBox> getchecksLst()
        {
            return this.checksLst;
        }

        CheckCombBoxLst3()
        {

            checksLst = new ArrayList<JComboBox>();
            checksNodeLst = new ArrayList<DefaultMutableTreeNode>();
        }

        public boolean addCheckWithNode(JComboBox check, DefaultMutableTreeNode node)
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

                if (checksLst.get(i).getSelectedIndex() != 0)
                {
                    selectedNodeLst.add(checksNodeLst.get(i));
                }

            }
            return selectedNodeLst;
        }

        public int selectedCount()
        {
            int selectedCount = 0;

            for (int i = 0; i < checksLst.size(); i++)
            {
                if (checksLst.get(i).getSelectedIndex() != 0)
                {
                    selectedCount++;
                }
            }
            return selectedCount;
        }

        public String getPropertyString()
        {

            String rule = "";

            for (int i = 0; i < checksLst.size(); i++)
            {

                // if (i == 0){rule += "[";}

                if (checksLst.get(i).getSelectedIndex() != 0)
                {

                    String TargetType = ((TargetInfo) checksNodeLst.get(i).getUserObject()).getTargetType();
                    String valueOfvalue = ((TargetInfo) checksNodeLst.get(i).getUserObject()).getvalueOfvalue();
                    String typeOfvalue = ((TargetInfo) checksNodeLst.get(i).getUserObject()).gettypeOfvalue();
                    String temp = "(" + valueOfvalue + " = " + checksLst.get(i).getSelectedItem().toString() + ")";
                    if (rule.equals(""))
                    {
                        rule = temp;
                    }
                    else
                    {
                        rule += " & " + temp;
                    }
                }
            }

            return rule;
        }

    }
}
