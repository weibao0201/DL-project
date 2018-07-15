package gov.nist.csd.acpt;

import gov.nist.csd.acpt.util.GraphicsUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by charles on 3/13/2017.
 */
public class CaveatClaimer extends JDialog implements ActionListener {
    private JButton okButton = null;
    private JPanel buttonPanel = null;
    private JTextArea caveat = null;
    private boolean   answer      = false;

    public CaveatClaimer(final JFrame frame, final String title, final boolean mod){
        super(frame,title,mod);

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
        setSize(500, 360);
        setResizable(true);
        GraphicsUtil.centerDialog(this);

        caveat = new JTextArea( "Please note the following caveats are applied when using the ACPT:\n" +
                " \n" +
                "1. An attribute name (subject, action, resource) should be started with only letters. This limitation is due to the limitation of model checking engine used for ACPT.\n" +
                " \n" +
                "2. To specify properties (in the Property tab), every attributes in a specified properties need to be in the range of predefined attributes values, for example, if a subject attribute “employee” has values: “full_time”, “part_time”, and “intern”. And a specified property contains: \"SPEC (employee = temporary)...\". The verification result will generate error report complaining that the value “temporary” is \"undefined\"."
        );
        caveat.setWrapStyleWord(true);
        caveat.setAutoscrolls(true);
        caveat.setLineWrap(true);
        caveat.setEditable(false);
        buttonPanel = new JPanel();
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        buttonPanel.add(okButton);
        add(caveat, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==okButton){
            answer = true;
            setVisible(false);
        }else {
            answer = false;
            setVisible(false);
        }
    }

     public boolean getAnswer(){
         return answer;
     }
}
