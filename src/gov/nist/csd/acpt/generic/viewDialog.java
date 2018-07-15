package gov.nist.csd.acpt.generic;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import gov.nist.csd.acpt.util.GraphicsUtil;

public class viewDialog extends JDialog implements ActionListener
{
    // private JPanel myPanel = null;
    private JButton closeButton = null;
    // private JButton noButton = null;
    // private JButton getButton = null;
    // private JButton eraseButton = null;

    // private boolean answer = false;
    //
    // private String DialogType = null;
    // private JComboBox initialState = null;
    // private JComboBox decision = null;
    // private JComboBox propertytype = null;
    // private JTextArea propertytextArea = null;

    //
    // public String getMyText() { return this.myText; }
    //
    // public boolean getAnswer() { return answer; }

    public viewDialog(JFrame frame, boolean modal, File filename, String opendfiletype, boolean LineWrapEnabled)
    {
        super(frame, modal);

        JLabel addSubjectLabel;
        // try {
        addSubjectLabel = new JLabel("File: results\\" + filename.getName().toString(), SwingConstants.LEFT);
        // } catch (IOException e1) {
        // // TODO Auto-generated catch block
        // e1.printStackTrace();
        // JOptionPane.showMessageDialog(null,
        // "cannot view: File name may be incorrect", "Warning",
        // JOptionPane.INFORMATION_MESSAGE);
        // return;
        // }
        addSubjectLabel.setPreferredSize(new Dimension(200, GraphicsUtil.FIELD_HEIGHT));
        addSubjectLabel.setMaximumSize(new Dimension(500, GraphicsUtil.FIELD_HEIGHT));

        this.closeButton = new JButton("Close");
        this.closeButton.setPreferredSize(new Dimension(200, GraphicsUtil.FIELD_HEIGHT));
        this.closeButton.setMaximumSize(new Dimension(200, GraphicsUtil.FIELD_HEIGHT));
        this.closeButton.addActionListener(this);

        String contents = "";
        String errormsg = "";

        if (opendfiletype.equals(GenericInfo.XACMLFileType))
        {

        }
        else if (opendfiletype.equals(GenericInfo.SMVFileType))
        {
            contents += "\n";
            contents +=
                    "*** Note that, for NuSMV execution, ACPT converts specicial character, \":\" with \"-_\" to avoid errors.\n";
            contents += "\n";
        }
        else if (opendfiletype.equals(GenericInfo.TXTFileType))
        {
            contents += "\n";
            contents +=
                    "*** Note that, for NuSMV execution, ACPT converts specicial character, \":\" with \"-_\" to avoid errors.\n";
            contents += "\n";
        }

        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            String line = br.readLine();
            boolean writeCommentOn = true;
            while (line != null)
            {

                if (opendfiletype.equals(GenericInfo.SMVFileType))
                {
                    line = NuSMVattrNameCovert.ConvertToFitNuSMFormatBack(line);
                    if (line.startsWith("-- specification"))
                    {
                        writeCommentOn = true;

                    }
                    else if (line.startsWith("-- invariant"))
                    {
                        writeCommentOn = false;
                        if (line.endsWith("false"))
                        {
                        }
                        else if (line.endsWith("true"))
                        {
                            line.replaceAll("-- invariant", "");
                            line.replaceAll("is true", "");
                            errormsg += "\n" + line;
                        }
                    }

                    if (writeCommentOn == true)
                    {
                        contents += "\n" + line;
                    }
                }
                else
                {
                    contents += line + "\n";
                }

                line = br.readLine();
            }
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Alex: I could not open " + filename + "!");
        }
        catch (IOException e)
        {
            System.err.println("Alex: File operations on " + filename + "failed!");
        }

        JTextArea textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

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

        // Warning

        textArea.append(contents);
        // textArea.setLineWrap(true);
        textArea.setLineWrap(LineWrapEnabled);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        // textArea.setBackground(new Color(230, 230, 230));
        // textArea.setBackground(null);
        scrollPane.setPreferredSize(new Dimension(550, 400));
        scrollPane.setMaximumSize(new Dimension(550, 400));

        JPanel panel = new JPanel();
        panel.setMinimumSize(new Dimension(500, 0));

        panel.add(addSubjectLabel);
        panel.add(scrollPane);
        panel.add(this.closeButton);
        this.add(panel);
        // this.add(scrollPane);
        //
        // this.add(this.closeButton);

        // this.add(myPanel);
        int height = 500;
        // if (checkCompLst.getchecksLst().size() >= 10){
        // height = 500 + (checkCompLst.getchecksLst().size()-9) * 25;
        // }
        this.setMinimumSize(new Dimension(600, height));
        this.setPreferredSize(new Dimension(600, height));
        //

        if (!errormsg.equals(""))
        {
            GraphicsUtil.showWarningDialog(
                    "The following conditions in the property can not be reachable:" + errormsg
                            + "\nNote that there are various reasons to cause this error. For example, typos or undefined values in the property can cause such an error.",
                    "Condition Reachablity", null);
            this.dispose();
        }
        else
        {
            pack();
            setLocationRelativeTo(frame);
            setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        if (closeButton == e.getSource())
        {
            this.dispose();

        }
    }

}
