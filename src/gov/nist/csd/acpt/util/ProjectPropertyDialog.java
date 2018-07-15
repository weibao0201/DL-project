package gov.nist.csd.acpt.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

public class ProjectPropertyDialog extends JDialog implements ActionListener
{
    private JPanel     myPanel           = null;
    private JButton    yesButton         = null;
    private JButton    noButton          = null;
    private boolean    answer            = false;
    private JTextField MLSstateField     = null;
    private JTextField ProcessstateField = null;

    public boolean getAnswer()
    {
        return answer;
    }

    public ProjectPropertyDialog(JFrame frame, String title, boolean modal)
    {
        super(frame, title, modal);

        myPanel = new JPanel();
        myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));

        /******************** Property panel *************************/

        JLabel stateNumOfMLS = new JLabel("Number of MultiLevel States: ", SwingConstants.LEFT);
        stateNumOfMLS.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        stateNumOfMLS.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        JLabel stateNumOfProcess = new JLabel("Number of Process States: ", SwingConstants.LEFT);
        stateNumOfProcess.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        stateNumOfProcess.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        this.MLSstateField = new JTextField(Generalproperties.MultiLevelStateNumber + "");
        this.MLSstateField.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        this.MLSstateField.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        this.ProcessstateField = new JTextField(Generalproperties.ProcessStateNumber + "");
        this.ProcessstateField.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        this.ProcessstateField.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        JPanel addRuleEastPanel = new JPanel(new GridLayout(0, 2));
        addRuleEastPanel.setLayout(new BoxLayout(addRuleEastPanel, BoxLayout.Y_AXIS));
        addRuleEastPanel.add(stateNumOfMLS);
        addRuleEastPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleEastPanel.add(stateNumOfProcess);

        JPanel addRuleCenterPanel = new JPanel();
        addRuleCenterPanel.setLayout(new BoxLayout(addRuleCenterPanel, BoxLayout.Y_AXIS));
        addRuleCenterPanel.add(MLSstateField);
        addRuleCenterPanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.DEFAULT_GAP)));
        addRuleCenterPanel.add(ProcessstateField);

        JPanel addRuleCWestPanel = new JPanel();
        addRuleCWestPanel.setLayout(new BoxLayout(addRuleCWestPanel, BoxLayout.Y_AXIS));
        addRuleCWestPanel.add(Box.createRigidArea(new Dimension(15, 40)));

        JPanel addRulePanel = new JPanel();
        addRulePanel.setLayout(new BoxLayout(addRulePanel, BoxLayout.Y_AXIS));

        TitledBorder titledBorder3 = BorderFactory.createTitledBorder("State Properties");
        addRulePanel.setBorder(titledBorder3);
        addRulePanel.setLayout(new BorderLayout());
        addRulePanel.add(addRuleEastPanel, BorderLayout.WEST);
        addRulePanel.add(addRuleCenterPanel, BorderLayout.CENTER);
        addRulePanel.add(addRuleCWestPanel, BorderLayout.EAST);

        myPanel.add(addRulePanel);

        /******************** button panel *************************/

        this.yesButton = new JButton("Update");
        this.yesButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.yesButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.yesButton.addActionListener(this);
        this.yesButton.setActionCommand("Yes");

        this.noButton = new JButton("Cancel");
        this.noButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.noButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.noButton.addActionListener(this);
        this.noButton.setActionCommand("Cancel");

        JPanel groupButtonPanel = new JPanel();
        groupButtonPanel.setLayout(new BoxLayout(groupButtonPanel, BoxLayout.X_AXIS));
        groupButtonPanel.add(yesButton);
        groupButtonPanel.add(noButton);

        myPanel.add(groupButtonPanel);

        this.add(myPanel);
        int height = 150;
        this.setMinimumSize(new Dimension(300, height));
        this.setPreferredSize(new Dimension(300, height));

        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (yesButton == e.getSource())
        {

            try
            {
                Generalproperties.MultiLevelStateNumber = Integer.parseInt(this.MLSstateField.getText().trim());
                Generalproperties.ProcessStateNumber = Integer.parseInt(this.ProcessstateField.getText().trim());

            }
            catch (NumberFormatException e2)
            {
                JOptionPane.showMessageDialog(null, "Error: please input integer values for text fields", "Warning",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Read/update/store properties file.
            Properties properties = new Properties();
            try
            {
                properties.load(new FileInputStream(Generalproperties.PropertyConfigfile));
                properties.setProperty("MultiLevelStateNumber", Generalproperties.MultiLevelStateNumber + "");
                properties.setProperty("ProcessStateNumber", Generalproperties.ProcessStateNumber + "");
                properties.store(new FileOutputStream(Generalproperties.PropertyConfigfile), null);

            }
            catch (IOException e2)
            {

                JOptionPane.showMessageDialog(null, "Error: IOExeption for property load/store", "Warning",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, "Project properties are updated.");

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
