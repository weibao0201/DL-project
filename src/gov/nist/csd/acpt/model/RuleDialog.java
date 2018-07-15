package gov.nist.csd.acpt.model;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * This class implements target tree utility functions.
 *
 * @author JeeHyun Hwang
 * @version $Revision$, $Date$
 * @since 6.0
 */

import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.Generalproperties;
import gov.nist.csd.acpt.util.GraphicsUtil;

public class RuleDialog extends JDialog implements ActionListener
{
    private JPanel         myPanel                  = null;
    private JButton        yesButton                = null;
    private JButton        noButton                 = null;
    private boolean        answer                   = false;
    CheckCombBoxLst        checkCompLst             = null;
    private String         DialogType               = null;
    private JComboBox      initialState             = null;
    private JComboBox      decision                 = null;

    private JTextArea      conditiontextArea        = null;

    private int            countSubject             = 0;
    private int            countResource            = 0;
    private int            countAction              = 0;
    private int            countEnviroment          = 0;
    /* Bruce Batson June 2013 Edit */
    private int            ruleNum;
    /* End of Edit */

    DefaultMutableTreeNode targetSubjectRootNode    = null;
    DefaultMutableTreeNode targetResourceRootNode   = null;
    DefaultMutableTreeNode targetActionRootNode     = null;
    DefaultMutableTreeNode targetEnviromentRootNode = null;

    public CheckCombBoxLst getcheckCompLst()
    {
        return checkCompLst;
    }

    public boolean getAnswer()
    {
        return answer;
    }

    /* Bruce Batson June 2013 Edit */
    public int getcountSubject()
    {
        return countSubject;
    }

    public int getRuleNum()
    {
        return ruleNum;
    }

    /* End of Edit */
    public String getCondition()
    {
        return conditiontextArea.getText();
    }

    // public RuleDialog(JFrame frame, boolean modal, String DialogType, JTree
    // targetTree, String myMessage, String dialogPurpose) {
    //
    // }
    public RuleDialog(JFrame frame, boolean modal, String DialogType, JTree targetTree, String myMessage,
            String dialogPurpose, DefaultMutableTreeNode curNode, int ruleNum)
    {
        super(frame, "Rule Dialog", modal);

        /* Bruce Batson June 2013 Edit */
        this.ruleNum = ruleNum;
        /* End of Edit */
        this.DialogType = DialogType;
        checkCompLst = new CheckCombBoxLst();
        targetSubjectRootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.SUBJECTS, TargetInfo.Level);
        targetResourceRootNode =
                TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.RESOURCES, TargetInfo.Level);
        targetActionRootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.ACTIONS, TargetInfo.Level);
        targetEnviromentRootNode =
                TargetTreeUtil.getFirstMatchingNode(targetTree, TargetInfo.ENVIRONMENTS, TargetInfo.Level);

        // JPanel myBigPanel = new JPanel(new GridLayout(0, 1));
        // myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        myPanel = new JPanel();
        // myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
        // getContentPane().add(myPanel);

        // TitledBorder titledBorder = BorderFactory.createTitledBorder("
        // commands");
        // myPanel.setBorder(titledBorder);
        // this.setPreferredSize(new Dimension(600, 300));
        // myPanel.setLayout(new BorderLayout());
        // myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        int checkount = 0;
        JPanel panel = null;
        Border border = null;

        if (this.DialogType.equals(ModelInfo.WORKFLOWRule))
        {
            panel = new JPanel(new GridLayout(0, 2));
            border = BorderFactory.createTitledBorder("Rule's Initial State");
            panel.setBorder(border);
            // JComboBox initialState = null;
            JLabel attributeLabel = null;

            attributeLabel = new JLabel("Initial State");
            attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
            attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
            initialState = new JComboBox();
            initialState.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
            initialState.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

            for (int j = 0; j < Generalproperties.ProcessStateNumber; j++)
            {
                initialState.addItem("Process_State " + (j + 1));
            }

            panel.add(attributeLabel);
            panel.add(initialState);
            myPanel.add(panel);
        }

        JComboBox check = null;
        JLabel attributeLabel = null;

        panel = new JPanel(new GridLayout(0, 2));
        border = BorderFactory.createTitledBorder(TargetInfo.SUBJECT_ATTRIBUTES);
        panel.setBorder(border);

        // panel.add(Box.createRigidArea(new
        // Dimension(150,GraphicsUtil.GAP_ZERO)));
        // panel.add(Box.createRigidArea(new
        // Dimension(150,GraphicsUtil.GAP_ZERO)));

        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(targetSubjectRootNode,
                    TargetInfo.SUBJECT_ATTRIBUTES, TargetInfo.attrLevel);
            countSubject = choices.size();
            if (choices.size() == 0)
            {
                JLabel infoLabel = new JLabel("No " + TargetInfo.SUBJECT_ATTRIBUTES + " attributes are defined");
                panel.add(infoLabel);
                panel.add(Box.createRigidArea(new Dimension(150, GraphicsUtil.GAP_ZERO)));
            }
            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();

                attributeLabel = new JLabel(valueOfvalue);
                attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                check = new JComboBox();
                check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

                List choices2 = TargetTreeUtil.getTargetTreeChildrenNode2List((DefaultMutableTreeNode) choices.get(i),
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

        myPanel.add(panel);

        panel = new JPanel(new GridLayout(0, 2));
        panel.setMinimumSize(new Dimension(400, 400));
        // panel.setPreferredSize(new Dimension(400, 0));
        border = BorderFactory.createTitledBorder(TargetInfo.RESOURCE_ATTRIBUTES);
        panel.setBorder(border);

        // panel.add(Box.createRigidArea(new
        // Dimension(150,GraphicsUtil.GAP_ZERO)));
        // panel.add(Box.createRigidArea(new
        // Dimension(150,GraphicsUtil.GAP_ZERO)));

        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(targetResourceRootNode,
                    TargetInfo.RESOURCE_ATTRIBUTES, TargetInfo.attrLevel);
            countResource = choices.size();
            if (choices.size() == 0)
            {
                JLabel infoLabel = new JLabel("No " + TargetInfo.RESOURCE_ATTRIBUTES + " attributes are defined");
                panel.add(infoLabel);
                panel.add(Box.createRigidArea(new Dimension(150, GraphicsUtil.GAP_ZERO)));
            }

            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();
                attributeLabel = new JLabel(valueOfvalue);
                attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                check = new JComboBox();
                check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

                List choices2 = TargetTreeUtil.getTargetTreeChildrenNode2List((DefaultMutableTreeNode) choices.get(i),
                        TargetInfo.RESOURCE_ATTR_VALUSES, TargetInfo.nodeLevel);

                check.addItem("Any " + valueOfvalue);
                for (int j = 0; j < choices2.size(); j++)
                {
                    check.addItem(choices2.get(j));
                }

                // if (curNode != null){
                // updateSelected (curNode, valueOfvalue, check);
                // }
                check.setActionCommand("Check:" + (checkount++));
                checkCompLst.addCheckWithNode(check, (DefaultMutableTreeNode) choices.get(i));
                panel.add(attributeLabel);
                panel.add(check);
            }
        }

        myPanel.add(panel);

        panel = new JPanel(new GridLayout(0, 2));
        border = BorderFactory.createTitledBorder(TargetInfo.ACTION_ATTRIBUTES);
        panel.setBorder(border);

        // panel.add(Box.createRigidArea(new
        // Dimension(150,GraphicsUtil.GAP_ZERO)));
        // panel.add(Box.createRigidArea(new
        // Dimension(150,GraphicsUtil.GAP_ZERO)));

        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(targetActionRootNode,
                    TargetInfo.ACTION_ATTRIBUTES, TargetInfo.attrLevel);

            if (choices.size() == 0)
            {
                JLabel infoLabel = new JLabel("No " + TargetInfo.ACTION_ATTRIBUTES + " attributes are defined");
                panel.add(infoLabel);
                panel.add(Box.createRigidArea(new Dimension(150, GraphicsUtil.GAP_ZERO)));
            }

            countAction = choices.size();
            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();
                attributeLabel = new JLabel(valueOfvalue);
                attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                check = new JComboBox();
                check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

                List choices2 = TargetTreeUtil.getTargetTreeChildrenNode2List((DefaultMutableTreeNode) choices.get(i),
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

        myPanel.add(panel);

        panel = new JPanel(new GridLayout(0, 2));
        border = BorderFactory.createTitledBorder(TargetInfo.ENVIRONMENT_ATTRIBUTES);
        panel.setBorder(border);

        // panel.add(Box.createRigidArea(new
        // Dimension(150,GraphicsUtil.GAP_ZERO)));
        // panel.add(Box.createRigidArea(new
        // Dimension(150,GraphicsUtil.GAP_ZERO)));

        if (targetTree != null)
        {
            List choices = TargetTreeUtil.getTargetTreeChildrenNode2List(targetEnviromentRootNode,
                    TargetInfo.ENVIRONMENT_ATTRIBUTES, TargetInfo.attrLevel);

            if (choices.size() == 0)
            {
                JLabel infoLabel = new JLabel("No " + TargetInfo.ENVIRONMENT_ATTRIBUTES + " attributes are defined");
                panel.add(infoLabel);
                panel.add(Box.createRigidArea(new Dimension(150, GraphicsUtil.GAP_ZERO)));
            }

            countEnviroment = choices.size();
            for (int i = 0; i < choices.size(); i++)
            {
                String valueOfvalue =
                        ((TargetInfo) ((DefaultMutableTreeNode) choices.get(i)).getUserObject()).getvalueOfvalue();
                attributeLabel = new JLabel(valueOfvalue);
                attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
                check = new JComboBox();
                check.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
                check.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

                List choices2 = TargetTreeUtil.getTargetTreeChildrenNode2List((DefaultMutableTreeNode) choices.get(i),
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

        myPanel.add(panel);
        String[] decisionSet = null;
        String titleOfdecision = null;
        if (this.DialogType.equals(ModelInfo.WORKFLOWRule))
        {
            titleOfdecision = "Next State or Decision";
            // decisionSet = new String[] {"Permit and go to Next State",
            // "Permit"};
            decisionSet = new String[]
            {
                    "Permit"
            };

        }
        else
        {
            titleOfdecision = "Decision";
            decisionSet = new String[]
            {
                    "Permit", "Deny"
            };
        }

        panel = new JPanel(new GridLayout(0, 2));
        border = BorderFactory.createTitledBorder(titleOfdecision);
        panel.setBorder(border);
        attributeLabel = new JLabel(titleOfdecision);
        attributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        attributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        decision = new JComboBox(decisionSet);

        decision.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        decision.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        panel.add(attributeLabel);
        panel.add(decision);

        // decisionComboBox = new JComboBox(new String[]{"Permit", "Deny"});

        myPanel.add(panel);

        /******************** Condition Area panel *************************/

        panel = new JPanel();
        border = BorderFactory.createTitledBorder(
                "XACML Rule Condition (e.g.<Apply...>...</Apply>)used for only XACML Policy generation)");
        panel.setBorder(border);
        conditiontextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(conditiontextArea);
        // conditiontextArea.append("<Condition
        // FunctionId=\"urn:oasis:names:tc:xacml:1.0:function:and\">");
        conditiontextArea.setLineWrap(true);
        conditiontextArea.setWrapStyleWord(true);
        conditiontextArea.setEditable(true);
        scrollPane.setPreferredSize(new Dimension(300, 50));
        scrollPane.setMaximumSize(new Dimension(300, 100));
        panel.add(scrollPane);
        myPanel.add(panel);

        /******************** Button panel *************************/

        // myPanel.add(new JLabel(myMessage));
        panel = new JPanel(new GridLayout(0, 2));
        yesButton = new JButton(dialogPurpose);
        yesButton.addActionListener(this);
        panel.add(yesButton);
        noButton = new JButton("Cancel");
        noButton.addActionListener(this);
        panel.add(noButton);
        pack();

        myPanel.add(panel);
        // myPanel.setPreferredSize(new Dimension(100, 100));
        // myPanel.setMinimumSize(new Dimension(350, GraphicsUtil.FIELD_HEIGHT *
        // checkount + GraphicsUtil.FIELD_HEIGHT));
        // myPanel.setPreferredSize(new Dimension(350, GraphicsUtil.FIELD_HEIGHT
        // * checkount + GraphicsUtil.FIELD_HEIGHT));
        //
        // myPanel.setMinimumSize(new Dimension(350, GraphicsUtil.FIELD_HEIGHT *
        // checkount + GraphicsUtil.FIELD_HEIGHT));
        // myPanel.setPreferredSize(new Dimension(350, GraphicsUtil.FIELD_HEIGHT
        // * checkount + GraphicsUtil.FIELD_HEIGHT));

        // CardLayout cardLayout = new CardLayout();

        // this.setLayout(cardLayout);
        // this.add(myPanel, constraints)
        this.add(myPanel);
        // this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        int height = 600;
        if (checkCompLst.getchecksLst().size() >= 10)
        {
            height = 600 + ((checkCompLst.getchecksLst().size() - 9) * 25);
        }
        this.setMinimumSize(new Dimension(400, height));
        this.setPreferredSize(new Dimension(400, height));

        if (curNode != null)
        {
            checkCompLst.selection(curNode);
            if (curNode.getChildCount() > 0)
            { // if node has conditon node
                DefaultMutableTreeNode ConditionNode = (DefaultMutableTreeNode) curNode.getFirstChild();
                if (ConditionNode != null)
                {
                    String cond = ((ModelInfo) ConditionNode.getUserObject()).getValue();
                    conditiontextArea.append(cond);
                }
            }

            // updateSelected (curNode, valueOfvalue, check);
        }

        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    // private void updateSelected(DefaultMutableTreeNode curNode,
    // String valueOfvalue, JComboBox check) {
    //
    // String RuleInfo = ((ModelInfo) curNode.getUserObject()).getValue();
    //
    // for (int i = 0; i < check.getItemCount(); i++) {
    // TargetInfo selectedTargetInfo = (TargetInfo) ((DefaultMutableTreeNode)
    // check.get(i)).getUserObject());
    //
    // }
    // .getvalueOfvalue();
    //
    //
    // }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (yesButton == e.getSource())
        {
            // System.err.println("User chose yes.:::"+checkCompLst.toString());

            String[] numOfselElms = checkCompLst.toString().split("#");

            // System.err.println(this.DialogType + numOfselElms.length );
            /*
             * if ((this.DialogType.equals(ModelInfo.WORKFLOWRule) &&
             * numOfselElms.length <= 1)||
             * (this.DialogType.equals(ModelInfo.ABACRule) &&
             * numOfselElms.length <= 0) ){
             *
             * JOptionPane.showMessageDialog(null,
             * "Pleas select at least one attribute of which value is not any value"
             * , "Warning", JOptionPane.INFORMATION_MESSAGE); return; }
             */
            if (checkCompLst.selectedCount() <= 0)
            {
                // frame.logPanel.append("No file is selected for Test
                // generation ");
                JOptionPane.showMessageDialog(null,
                        "Pleas select at least one attribute of which value is not any value", "Warning",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
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

    /***************************************************************************
     * Inner classes
     **************************************************************************/
    class CheckCombBoxLst
    {

        private List<JComboBox>              checksLst     = null;
        private List<DefaultMutableTreeNode> checksNodeLst = null;

        public List<JComboBox> getchecksLst()
        {
            return this.checksLst;
        }

        /* Bruce Batson June 2013 Edit */
        public String getSubjectAttValue(int i)
        {
            return checksLst.get(i).getSelectedItem().toString();
        }

        /* End of Edit */
        public void selection(DefaultMutableTreeNode curNode)
        {

            String curNodeRule = ((ModelInfo) curNode.getUserObject()).getValue();
            JComboBox curComboBox = null;

            for (int i = 0; i < checksLst.size(); i++)
            {

                curComboBox = checksLst.get(i);
                String TargetType = ((TargetInfo) checksNodeLst.get(i).getUserObject()).getTargetType();
                String valueOfvalue = ((TargetInfo) checksNodeLst.get(i).getUserObject()).getvalueOfvalue();
                String typeOfvalue = ((TargetInfo) checksNodeLst.get(i).getUserObject()).gettypeOfvalue();

                for (int j = 0; j < curComboBox.getItemCount(); j++)
                {

                    String temp = TargetType + ";" + valueOfvalue + ";" + typeOfvalue + ";"
                            + curComboBox.getItemAt(j).toString();

                    if (curNodeRule.indexOf(temp) >= 0)
                    {
                        curComboBox.setSelectedIndex(j);
                    }

                }

            }

            if (DialogType.equals(ModelInfo.WORKFLOWRule))
            {
                curComboBox = initialState;
                for (int j = 0; j < curComboBox.getItemCount(); j++)
                {
                    String temp = curComboBox.getItemAt(j).toString() + "#";

                    if (curNodeRule.indexOf(temp) >= 0)
                    {
                        curComboBox.setSelectedIndex(j);
                    }
                }
                // rule = initialState.getSelectedItem().toString() + "#" +
                // rule;
            }

            curComboBox = decision;
            for (int j = 0; j < curComboBox.getItemCount(); j++)
            {
                String temp = "->" + curComboBox.getItemAt(j).toString();

                if (curNodeRule.indexOf(temp) >= 0)
                {
                    curComboBox.setSelectedIndex(j);
                }
            }

            //
            // rule += "->" + decision.getSelectedItem().toString();
            //
            //

        }

        CheckCombBoxLst()
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

        /*
         * public boolean selectAll() { for (JComboBox a: checksLst) {
         * a.setSelected(true); } return true; }
         *
         * public boolean deselectAll() { for (JComboBox a: checksLst) {
         * a.setSelected(false); } return true; }
         */
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

        @Override
        public String toString()
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
                    String temp = TargetType + ";" + valueOfvalue + ";" + typeOfvalue + ";"
                            + checksLst.get(i).getSelectedItem().toString();
                    if (rule.equals(""))
                    {
                        rule = temp;
                    }
                    else
                    {
                        rule += "#" + temp;
                    }
                }

                /*
                 * if ((countSubject-1) == i){rule += "][";} if ((countSubject +
                 * countResource -1) == i){rule += "][";} if ((countSubject +
                 * countResource + countAction -1) == i){rule += "]";}
                 */

            }

            if (DialogType.equals(ModelInfo.WORKFLOWRule))
            {
                rule = initialState.getSelectedItem().toString() + "#" + rule;
            }

            /* Bruce Batson June 2013 Edit */
            rule = "Rule " + ruleNum + "#" + rule;
            /* End of Eit */

            rule += "->" + decision.getSelectedItem().toString();

            return rule;
        }

        public String InheritanceRule(String newTargetType, String newvalueOfvalue, String newtypeOfvalue,
                String newsubject, String replace, String replace2)
        {
            String rule = "";
            boolean duplicate = false;
            for (int x = 0; x < checksLst.size(); x++)
            {
                if (checksLst.get(x).getSelectedItem().toString().equals(newsubject)
                        && ((TargetInfo) checksNodeLst.get(x).getUserObject()).getvalueOfvalue().equals(replace2))
                {
                    duplicate = true;
                }
            }
            if (!duplicate)
            {
                for (int i = 0; i < checksLst.size(); i++)
                {

                    // if (i == 0){rule += "[";}

                    if (checksLst.get(i).getSelectedIndex() != 0)
                    {

                        String TargetType = ((TargetInfo) checksNodeLst.get(i).getUserObject()).getTargetType();
                        String valueOfvalue = ((TargetInfo) checksNodeLst.get(i).getUserObject()).getvalueOfvalue();
                        String typeOfvalue = ((TargetInfo) checksNodeLst.get(i).getUserObject()).gettypeOfvalue();
                        String temp = TargetType + ";" + valueOfvalue + ";" + typeOfvalue + ";"
                                + checksLst.get(i).getSelectedItem().toString();
                        if (checksLst.get(i).getSelectedItem().toString().equals(replace)
                                && valueOfvalue.equals(replace2))
                        {
                            temp = newTargetType + ";" + newvalueOfvalue + ";" + newtypeOfvalue + ";" + newsubject;
                        }
                        if (rule.equals(""))
                        {
                            rule = temp;
                        }
                        else
                        {
                            rule += "#" + temp;
                        }
                    }

                    /*
                     * if ((countSubject-1) == i){rule += "][";} if
                     * ((countSubject + countResource -1) == i){rule += "][";}
                     * if ((countSubject + countResource + countAction -1) ==
                     * i){rule += "]";}
                     */

                }

                if (DialogType.equals(ModelInfo.WORKFLOWRule))
                {
                    rule = initialState.getSelectedItem().toString() + "#" + rule;
                }
                rule = "Rule " + ruleNum + "#" + rule;
                rule += "->" + decision.getSelectedItem().toString();
            }
            return rule;
        }

    }
}
