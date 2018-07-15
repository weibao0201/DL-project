package gov.nist.csd.acpt;

import gov.nist.csd.acpt.util.GraphicsUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ABACPolicyClaimer extends JDialog implements ActionListener
{
    private JButton   yesButton   = null;
    private JButton   noButton    = null;
    private JPanel    buttonPanel = null;
    private JTextArea disclaim    = null;

    private boolean   answer      = false;

    public ABACPolicyClaimer(final JFrame frame, final String title, final boolean mod)
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
        setTitle("ACPT Caveat");
        setLayout(new BorderLayout());
        setSize(450,150);
        // @Mod by dylan.yaga@nist.gov
        // Disabled resizability
        setResizable(true);
        // @Mod by dylan.yaga@nist.gov
        // Center Dialog was being called prior to setSize and was not
        // functioning properly
        GraphicsUtil.centerDialog(this);

        disclaim = new JTextArea("ACPT provides the Pseudo Exhaustive Testing function for policy rules, Please choose one of the following:");
        // @Mod by dylan.yaga@nist.gov
        // Word wrap was not working correctly, was cutting words in half
        disclaim.setWrapStyleWord(true);
        disclaim.setAutoscrolls(true);
        disclaim.setLineWrap(true);
        disclaim.setEditable(false);
        buttonPanel = new JPanel();
        yesButton = new JButton("ACPT main");
        noButton = new JButton("Pseudo Exhaustive Testing");
        // @Mod by dylan.yaga@nist.gov
        // Adding the action listener to enable/disable yesButton based on
        // checked value
        yesButton.addActionListener(this);
        noButton.addActionListener(this);
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        add(disclaim, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        // pack();
        setVisible(true);

    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == yesButton)
        {
            answer = true;
            setVisible(false);
        }

        else if (e.getSource() == noButton)
        {
            answer = false;
            setVisible(false);
        }
    }

    public boolean getAnswer()
    {
        return answer;
    }
}