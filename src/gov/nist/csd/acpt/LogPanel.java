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
package gov.nist.csd.acpt;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/**
 * This class implements the (XACML) target editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 *******************************************************************/

public class LogPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long serialVersionUID = 0;

    /***************************************************************************
     * Variables
     **************************************************************************/

    private JTextArea         logTextArea      = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    protected LogPanel()
    {

        this.logTextArea = new JTextArea();
        this.logTextArea.setEditable(false);
        setPanels();

    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setPanels()
    {

        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Log");

        this.setLayout(new BorderLayout());
        this.setBorder(titledBorder);
        this.add(logScrollPane, BorderLayout.CENTER);

    }

    public void append(String message)
    {

        logTextArea.append(message + "\n");

    }

    public void clear()
    {
        logTextArea.setText("");
    }

}
