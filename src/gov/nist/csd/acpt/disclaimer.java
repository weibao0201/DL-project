package gov.nist.csd.acpt;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

/* Program: disclaimer.java
 * Author:  Bruce Batson
 * Date:    June 6, 2013
 *
 * Program Description:
 *
 * This program post a dialog that show the user a disclaimer
 * and as if the user wish to continue using this program. If
 * they say yes then the ACPT.java will be called. If they say
 * no then it will terminate the program.
 */

import gov.nist.csd.acpt.util.GraphicsUtil;

public class disclaimer extends JDialog implements ActionListener
{
    private JButton   yesButton   = null;
    private JButton   noButton    = null;
    private JPanel    buttonPanel = null;
    private JTextArea disclaim    = null;
    private JCheckBox accept      = null;

    private boolean   answer      = false;

    public disclaimer(final JFrame frame, final String title, final boolean mod)
    {
        super(frame, title, mod);
        // @Mod by dylan.yaga@nist.gov
        // Changes the UI from default JAVA look to whichever OS it is run on
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
        setSize(450, 150);
        // @Mod by dylan.yaga@nist.gov
        // Disabled resizability
        setResizable(false);
        // @Mod by dylan.yaga@nist.gov
        // Center Dialog was being called prior to setSize and was not
        // functioning properly
        GraphicsUtil.centerDialog(this);

        disclaim = new JTextArea("Note that ACPT includes third party software not developed by NIST, "
                + "there is no guarantee that using ACPT is malware free.");
        // @Mod by dylan.yaga@nist.gov
        // Word wrap was not working correctly, was cutting words in half
        disclaim.setWrapStyleWord(true);
        disclaim.setLineWrap(true);
        disclaim.setEditable(false);
        accept = new JCheckBox("Check this box if you read the above statement and still wish to continue");
        buttonPanel = new JPanel();
        yesButton = new JButton("Continue");
        yesButton.setEnabled(false);
        noButton = new JButton("Cancel");
        // @Mod by dylan.yaga@nist.gov
        // Adding the action listener to enable/disable yesButton based on
        // checked value
        accept.addActionListener(this);
        yesButton.addActionListener(this);
        noButton.addActionListener(this);
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        add(disclaim, BorderLayout.NORTH);
        add(accept, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        // pack();
        setVisible(true);

    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == yesButton)
        {
            if (accept.isSelected())
            {
                answer = true;
                setVisible(false);
            }
        }

        else if (e.getSource() == noButton)
        {
            answer = false;
            setVisible(false);
        }
        // @Mod by dylan.yaga@nist.gov
        // UI Logic for the accept checkbox enabling/disabling the yesButton
        else if (e.getSource() == accept)
        {
            yesButton.setEnabled(accept.isSelected());
        }
    }

    public boolean getAnswer()
    {
        return answer;
    }
}