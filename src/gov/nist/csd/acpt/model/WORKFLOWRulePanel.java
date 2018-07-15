/* This software was developed by employees of the National Institute of
 * Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 15 United States Code Section 105, works of NIST
 * employees are not subject to copyright protection in the United States
 * and are considered to be in the public domain.  As a result, a formal
 * license is not needed to use the software.
 *
 * This software is provided by NIST as a service and is expressly
 * provided "AS IS".  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
 * OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
 * AND DATA ACCURACY.  NIST does not warrant or make any representations
 * regarding the use of the software or the results thereof including, but
 * not limited to, the correctness, accuracy, reliability or usefulness of
 * the software.
 *
 * Permission to use this software is contingent upon your acceptance
 * of the terms of this agreement.
 */
package gov.nist.csd.acpt.model;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.Generalproperties;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements the (XACML) target editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class WORKFLOWRulePanel extends JPanel
{

    /**
     * This inner class implements the target panel mouse adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class WorkFlowRulePanelActionAdapter implements ActionListener
    {

        WORKFLOWRulePanel userPanel = null;

        WorkFlowRulePanelActionAdapter(final WORKFLOWRulePanel userPanel)
        {

            this.userPanel = userPanel;

        }

        @Override
        public void actionPerformed(final ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getActionCommand().equals("Update"))
            {

                // String value = valueField.getText();
                // updateTargetValue(value);

            }
            else if (e.getActionCommand().equals("Update Rule"))
            {

                System.out.println("Got update rule button");
                final String value = getRuleValue();
                if (value != null)
                {
                    updateTargetValue(value);
                }

            }

            else
            {

                // System.err.println("GroupPanel warning: command not
                // implemented: " +
                // e.getActionCommand());
            }

        }

    }

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long             serialVersionUID               = 0;
    /***************************************************************************
     * Variables
     **************************************************************************/

    // public JButton updateValueButton = null;
    public JButton                        addAttributeButton             = null;
    // public JTextField valueField = null;
    public JComboBox                      stateComboBox                  = null;
    public JComboBox                      subjectComboBox                = null;
    public JComboBox                      resourceComboBox               = null;
    public JComboBox                      actionComboBox                 = null;
    public JComboBox                      enviromnmentComboBox           = null;

    public JComboBox                      decisionComboBox               = null;
    // public JList list = null;
    // public DefaultListModel listmodel = null;
    private ACPTFrame                     frame                          = null;
    private JTree                         modelTree                      = null;
    private JTree                         targetTree                     = null;
    private ModelInfo                     modelInfo                      = null;
    private boolean                       editable                       = false;

    private TitledBorder                  titledBorder                   = null;

    /* Adapters */
    public WorkFlowRulePanelActionAdapter workflowRulePanelActionAdapter = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public WORKFLOWRulePanel(final ACPTFrame frame, final JTree modelTree, final ModelInfo modelInfo,
            final boolean editable)
    {

        this.frame = frame;
        this.modelTree = modelTree;
        targetTree = frame.targetPanel.getTargetTree();
        this.modelInfo = modelInfo;
        setAdapters();
        setPanels(modelInfo);
        setEditable(editable);
    }

    public String getRuleValue()
    {

        if ((stateComboBox.getSelectedItem() == null) || (subjectComboBox.getSelectedItem() == null)
                || (resourceComboBox.getSelectedItem() == null) || (actionComboBox.getSelectedItem() == null)
                || (enviromnmentComboBox.getSelectedItem() == null) || (decisionComboBox.getSelectedItem() == null))
        {

            // Warning
            GraphicsUtil.showWarningDialog("cannot add a new rule due to null.", "Null Warning", null);
            return null;
        }

        final String state = (String) stateComboBox.getSelectedItem();
        final String subj = (String) subjectComboBox.getSelectedItem();
        final String res = (String) resourceComboBox.getSelectedItem();
        final String act = (String) actionComboBox.getSelectedItem();
        final String env = (String) enviromnmentComboBox.getSelectedItem();
        final String deci = (String) decisionComboBox.getSelectedItem();

        final String value = state + "#" + subj + "#" + res + "#" + act + "#" + env + "#" + deci;

        return value;
    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setAdapters()
    {

        workflowRulePanelActionAdapter = new WorkFlowRulePanelActionAdapter(this);

    }

    /***************************************************************************
     * Public methods
     **************************************************************************/

    public void setEditable(final boolean editable)
    {

        this.editable = editable;
        // this.valueField.setEnabled(editable);
        // this.updateValueButton.setEnabled(editable);

    }

    private void setPanels(final ModelInfo modelInfo)
    {

        /******************** Name panel *************************/
        /*
         * JLabel valueLabel = new JLabel("Name: ", JLabel.LEFT);
         * valueLabel.setPreferredSize( new Dimension(75,
         * GraphicsUtil.FIELD_HEIGHT)); valueLabel.setMaximumSize( new
         * Dimension(75, GraphicsUtil.FIELD_HEIGHT));
         *
         * this.valueField = new JTextField(modelInfo.getValue());
         * this.valueField.setPreferredSize( new Dimension(150,
         * GraphicsUtil.FIELD_HEIGHT)); this.valueField.setMaximumSize( new
         * Dimension(150, GraphicsUtil.FIELD_HEIGHT));
         *
         * this.updateValueButton = new JButton("Update");
         * this.updateValueButton.setPreferredSize( new Dimension(80,
         * GraphicsUtil.FIELD_HEIGHT)); this.updateValueButton.setMaximumSize(
         * new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
         *
         * this.updateValueButton.addActionListener(userPanelActionAdapter);
         * this.updateValueButton.setActionCommand("Update");
         *
         * JPanel namePanel = new JPanel(); titledBorder =
         * BorderFactory.createTitledBorder("Name");
         * namePanel.setBorder(titledBorder);
         *
         * namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
         *
         * namePanel.add(Box.createRigidArea(new
         * Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
         *
         * namePanel.add(valueLabel); namePanel.add(Box.createRigidArea(new
         * Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
         *
         * namePanel.add(valueField); namePanel.add(Box.createRigidArea(new
         * Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
         *
         * namePanel.add(updateValueButton);
         * namePanel.add(Box.createRigidArea(new
         * Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
         */
        /********************
         * Object (e.g., button, combobox) Creation
         *************************/

        final JLabel addRuleAdditionLabel = new JLabel("New Rule Addition: ", SwingConstants.LEFT);
        addRuleAdditionLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        addRuleAdditionLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        // String[] testStrings = { "Attr 1", "Attr 2", "Attr 3", "Attr 4" };

        final DefaultMutableTreeNode rootNode = TargetTreeUtil.getRootNode(targetTree);
        final String[] testStrings =
                TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode, TargetInfo.SUBJECT_ATTRIBUTES, 2);

        final JLabel addStateLabel = new JLabel("State: ", SwingConstants.LEFT);
        addStateLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        addStateLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        final JLabel addSubjectLabel = new JLabel("Subject: ", SwingConstants.LEFT);
        addSubjectLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        addSubjectLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        final JLabel addResourceLabel = new JLabel("Resource: ", SwingConstants.LEFT);
        addResourceLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        addResourceLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        final JLabel addActionLabel = new JLabel("Action: ", SwingConstants.LEFT);
        addActionLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        addActionLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        final JLabel addEnviromentLabel = new JLabel("Enviroment: ", SwingConstants.LEFT);
        addActionLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        addActionLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        final JLabel addDecisionLabel = new JLabel("Decision: ", SwingConstants.LEFT);
        addDecisionLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        addDecisionLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        stateComboBox = new JComboBox(Generalproperties.ProcessStateNumberArray);
        // stateComboBox.setSelectedIndex(0);
        stateComboBox.setActionCommand("StateComboBox");
        stateComboBox.addActionListener(workflowRulePanelActionAdapter);
        stateComboBox.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        stateComboBox.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        subjectComboBox = new JComboBox(
                TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode, TargetInfo.SUBJECT_ATTRIBUTES, 2));
        // subjectComboBox.setSelectedIndex(0);
        subjectComboBox.setActionCommand("SubjectComboBox");
        subjectComboBox.addActionListener(workflowRulePanelActionAdapter);
        subjectComboBox.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        subjectComboBox.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        resourceComboBox =
                new JComboBox(TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode, TargetInfo.RESOURCES, 2));
        // resourceComboBox.setSelectedIndex(0);
        resourceComboBox.setActionCommand("ResourceComboBox");
        resourceComboBox.addActionListener(workflowRulePanelActionAdapter);
        resourceComboBox.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        resourceComboBox.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        actionComboBox =
                new JComboBox(TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode, TargetInfo.ACTIONS, 2));
        // actionComboBox.setSelectedIndex(0);
        actionComboBox.setActionCommand("ActionComboBox");
        actionComboBox.addActionListener(workflowRulePanelActionAdapter);
        actionComboBox.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        actionComboBox.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        enviromnmentComboBox =
                new JComboBox(TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode, TargetInfo.ENVIRONMENTS, 2));
        // enviromnmentComboBox.setSelectedIndex(0);
        enviromnmentComboBox.setActionCommand("ActionComboBox");
        enviromnmentComboBox.addActionListener(workflowRulePanelActionAdapter);
        enviromnmentComboBox.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        enviromnmentComboBox.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        decisionComboBox = new JComboBox(new String[]
        {
                "Permit and go to Next State", "Permit", "Deny"
        });
        // decisionComboBox.setSelectedIndex(0);
        decisionComboBox.setActionCommand("ActionComboBox");
        decisionComboBox.addActionListener(workflowRulePanelActionAdapter);
        decisionComboBox.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        decisionComboBox.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        addAttributeButton = new JButton("Update");
        addAttributeButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        addAttributeButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        addAttributeButton.addActionListener(workflowRulePanelActionAdapter);
        addAttributeButton.setActionCommand("Update Rule");

        /******************** Combobox value Setup *************************/

        final String values[] = modelInfo.getValue().split("#");
        for (int i = 0; i < values.length; i++)
        {
            if (i == 0)
            {
                stateComboBox.setSelectedItem(values[i]);
            }
            else if (i == 1)
            {
                subjectComboBox.setSelectedItem(values[i]);
            }
            else if (i == 2)
            {
                resourceComboBox.setSelectedItem(values[i]);
            }
            else if (i == 3)
            {
                actionComboBox.setSelectedItem(values[i]);
            }
            else if (i == 3)
            {
                enviromnmentComboBox.setSelectedItem(values[i]);
            }
            else if (i == 4)
            {
                decisionComboBox.setSelectedItem(values[i]);
            }
        }

        /******************** Add Rule Addition panel *************************/

        final JPanel addRulePanel = new JPanel();
        addRulePanel.setLayout(new BoxLayout(addRulePanel, BoxLayout.Y_AXIS));

        final JPanel addRuleEastPanel = new JPanel();
        addRuleEastPanel.setLayout(new BoxLayout(addRuleEastPanel, BoxLayout.Y_AXIS));
        addRuleEastPanel.add(addStateLabel);
        addRuleEastPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleEastPanel.add(addSubjectLabel);
        addRuleEastPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleEastPanel.add(addResourceLabel);
        addRuleEastPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleEastPanel.add(addActionLabel);
        addRuleEastPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleEastPanel.add(addEnviromentLabel);
        addRuleEastPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleEastPanel.add(addDecisionLabel);
        addRuleEastPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));

        final JPanel addRuleCenterPanel = new JPanel();
        addRuleCenterPanel.setLayout(new BoxLayout(addRuleCenterPanel, BoxLayout.Y_AXIS));
        addRuleCenterPanel.add(stateComboBox);
        addRuleCenterPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleCenterPanel.add(subjectComboBox);
        addRuleCenterPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleCenterPanel.add(resourceComboBox);
        addRuleCenterPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleCenterPanel.add(actionComboBox);
        addRuleCenterPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleCenterPanel.add(enviromnmentComboBox);
        addRuleCenterPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleCenterPanel.add(decisionComboBox);

        final JPanel addRuleCWestPanel = new JPanel();

        addRuleCWestPanel.setLayout(new BoxLayout(addRuleCWestPanel, BoxLayout.Y_AXIS));
        addRuleCWestPanel.add(Box.createRigidArea(new Dimension(15, 40)));
        addRuleCWestPanel.add(addAttributeButton);

        final TitledBorder titledBorder3 = BorderFactory.createTitledBorder("Rule Update");
        addRulePanel.setBorder(titledBorder3);
        addRulePanel.setLayout(new BorderLayout());
        addRulePanel.add(addRuleEastPanel, BorderLayout.WEST);
        addRulePanel.add(addRuleCenterPanel, BorderLayout.CENTER);
        addRulePanel.add(addRuleCWestPanel, BorderLayout.EAST);

        // this.setLayout(new BorderLayout());
        // this.add(addRulePanel, BorderLayout.CENTER);

        /******************** Rule Area panel *************************/

        final JPanel panel = new JPanel();
        final JTextArea conditiontextArea = new JTextArea();
        final JScrollPane scrollPane = new JScrollPane(conditiontextArea);
        final String rule = modelInfo.toString();
        String rulebody = rule.substring(0, rule.indexOf('-'));
        rulebody = ("#" + rulebody).replaceAll("#", "\n -");
        final String ruleeffect = rule.substring(rule.indexOf('>') + 1);
        final String format = "* RULE Body:\n" + rulebody + "\n\n* Rule Effect:\n" + ruleeffect;
        conditiontextArea.append(format);
        conditiontextArea.setLineWrap(true);
        conditiontextArea.setWrapStyleWord(true);
        conditiontextArea.setEditable(true);
        scrollPane.setPreferredSize(new Dimension(300, 250));
        scrollPane.setMaximumSize(new Dimension(300, 400));
        panel.add(scrollPane);

        /******************** Title Border *************************/

        /* Titled border for this panel. */
        titledBorder = BorderFactory.createTitledBorder("Rule");
        setBorder(titledBorder);
        this.add(panel);

    }

    /***************************************************************************
     * Inner classes
     **************************************************************************/

    public void updateTargetValue(final String value)
    {

        modelInfo.setValue(value);
        if (modelInfo.getModelType().equals(TargetInfo.ROOT))
        {
            frame.setProjectName(value);
        }
        titledBorder.setTitle(value + " properties");
        this.repaint();
        modelTree.repaint();
        return;
    }

}
