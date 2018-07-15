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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.util.ContextMenuMouseListener;
import gov.nist.itl.csd.ABAC;

public class ABACBoolPanel extends JPanel
{
    class ABACPanelActionAdapter implements ActionListener
    {
        ABACBoolPanel userPanel = null;

        ABACPanelActionAdapter(final ABACBoolPanel userPanel)
        {

            this.userPanel = userPanel;

        }

        @Override
        public void actionPerformed(final ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getSource() == rdioTrueFalse)
            {
                ABAC.true_value = String.valueOf(true);// "True";
                ABAC.false_value = String.valueOf(false);// "False";
                UpdateResults();

            }
            else if (e.getSource() == rdioOneZero)
            {
                ABAC.true_value = "1";
                ABAC.false_value = "0";
                UpdateResults();
            }
            else if (e.getSource() == btnTest)
            {
                final String expression = txtInput.getText();
                if (expression.isEmpty())
                {
                    JOptionPane.showMessageDialog(mainFrame, "Input Policy Cannot Be Empty.", "Input Policy Error",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try
                {
                    // cast to lower, so 'MM' and 'mm' are treated the same -
                    // maybe
                    // a case sensitive checkbox option?
                    txtInput.setText(txtInput.getText().toLowerCase());
                    tester = new ABAC(txtInput.getText());
                    UpdateResults();
                    UpdateLog();

                }
                catch (final NullPointerException ex)
                {
                    JOptionPane.showMessageDialog(mainFrame, "Malformed Input Policy.", "Input Policy Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
            else if (e.getSource() == btnSaveAbacBoolResultsToFile)
            {
                if (tester == null)
                {
                    JOptionPane.showMessageDialog(mainFrame, "Unfortunately, there was no test output to save.",
                            "No Output To Save", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                final JFileChooser output = new JFileChooser();
                if (output.showSaveDialog(mainFrame) == JFileChooser.APPROVE_OPTION)
                {
                    try
                    {

                        final PrintWriter out = new PrintWriter(output.getSelectedFile());
                        out.write(tester.GTestFileOutput(delimiter.getText()));
                        out.write(tester.DTestFileOutput(delimiter.getText()));
                        out.flush();
                        out.close();
                        JOptionPane.showMessageDialog(mainFrame,
                                String.format("Output saved to %s", output.getSelectedFile().getAbsolutePath()),
                                "File Saved", JOptionPane.INFORMATION_MESSAGE);
                    }
                    catch (final FileNotFoundException e1)
                    {

                        // e1.printStackTrace();
                        JOptionPane.showMessageDialog(mainFrame,
                                String.format("File was not saved. Error: %d", e1.getMessage()), "Error Saving File",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        }

    }

    /***************************************************************************
     * Variables
     **************************************************************************/
    private final String          logBufferLine                =
            "========================================================================================";

    // Frames
    private ACPTFrame             frame                        = null;
    private final JPanel          mainFrame                    = null;
    // Adapter
    public ABACPanelActionAdapter userPanelActionAdapter       = null;
    // Text Fields for Input/Output
    private final JTextArea       txtResults                     = new JTextArea();
    private final JTextField      txtInput                     = new JTextField();
    private final JTextField      delimiter                    = new JTextField();
    private JRadioButton          rdioOneZero                  = null;
    private JRadioButton          rdioTrueFalse                = null;
    private JButton               btnTest                      = null;
    private JButton               btnSaveAbacBoolResultsToFile = null;
    // Attribute Based Access Control Boolean Expression Tester
    private ABAC                  tester                       = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public ABACBoolPanel(final ACPTFrame frame)
    {
        this.frame = frame;
        setAdapters();
        setPanels();
    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setAdapters()
    {
        userPanelActionAdapter = new ABACPanelActionAdapter(this);
    }

    /***************************************************************************
     * Public methods
     **************************************************************************/

    private void setPanels()
    {
        final ContextMenuMouseListener clkListener = new ContextMenuMouseListener();

        final JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createTitledBorder("Policy Input"));
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

        txtInput.addMouseListener(clkListener);
        txtInput.setText("emp & age > 18; & (fa | emt | med)");
        txtInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtInput.getPreferredSize().height));
        inputPanel.add(txtInput);
        btnTest = new JButton("Test");
        btnTest.addActionListener(userPanelActionAdapter);
        inputPanel.add(btnTest);

        final JPanel optionsPanel = new JPanel();
        optionsPanel.setBorder(BorderFactory.createTitledBorder("Options"));
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));

        rdioTrueFalse = new JRadioButton("True/False");
        rdioOneZero = new JRadioButton("1/0");
        rdioOneZero.setSelected(true);
        rdioTrueFalse.addActionListener(userPanelActionAdapter);
        rdioOneZero.addActionListener(userPanelActionAdapter);
        optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, rdioTrueFalse.getPreferredSize().height));

        final ButtonGroup groupValues = new ButtonGroup();
        groupValues.add(rdioTrueFalse);
        groupValues.add(rdioOneZero);
        optionsPanel.add(new JLabel("Output Style: "));
        optionsPanel.add(rdioOneZero);
        optionsPanel.add(rdioTrueFalse);
        // optionsPanel.add(new JLabel("|"));

        final JPanel resultPanel = new JPanel();
        resultPanel.setBorder(BorderFactory.createTitledBorder("Test Results"));
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        txtResults.addMouseListener(clkListener);
        txtResults.setEditable(false);
        final JScrollPane spGTest = new JScrollPane(txtResults);
        resultPanel.add(spGTest);


        final JPanel fileOutputPanel = new JPanel();
        fileOutputPanel.setBorder(BorderFactory.createTitledBorder("Test Results Output"));
        fileOutputPanel.setLayout(new BoxLayout(fileOutputPanel, BoxLayout.X_AXIS));
        fileOutputPanel.add(new JLabel("Delimiter Value"));
        delimiter.setText(",");
        delimiter.setMaximumSize(new Dimension(20, delimiter.getPreferredSize().height));
        fileOutputPanel.add(delimiter);
        btnSaveAbacBoolResultsToFile = new JButton("Save Output to File");
        btnSaveAbacBoolResultsToFile.addActionListener(userPanelActionAdapter);
        fileOutputPanel.add(btnSaveAbacBoolResultsToFile);
        fileOutputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, fileOutputPanel.getPreferredSize().height));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(inputPanel);
        add(optionsPanel);
        add(resultPanel);
        
        add(fileOutputPanel);
        frame.pack();

    }

    private void UpdateLog()
    {
        if (tester != null)
        {
            frame.log(logBufferLine);
            frame.log(String.format("ABAC Boolean Log %s", new Date()));
            frame.log(tester.Summary());
            frame.log(tester.ConsoleOutput);
            frame.log(tester.toString());
            frame.log(logBufferLine);
        }
    }

    private void UpdateResults()
    {
        if (tester != null)
        {

            tester.GTest();
            tester.DTest();
            txtResults.setText(String.format("%s\n\n\n%s", tester.GTestOutput(true), tester.DTestOutput(true)));
            
        }
    }

}
