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
package gov.nist.csd.acpt.generic;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements the (XACML) target editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class GenericPropertiesPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long                serialVersionUID                  = 0;

    /***************************************************************************
     * Variables
     **************************************************************************/

    public JButton                           updateValueButton                 = null;
    public JTextField                        valueField                        = null;
    private ACPTFrame                        frame                             = null;
    private JTree                            genericTree                       = null;
    private GenericInfo                      genericInfo                       = null;
    private boolean                          editable                          = false;
    private TitledBorder                     titledBorder                      = null;

    /* Adapters */
    public ModelPropertiesPanelActionAdapter modelPropertiesPanelActionAdapter = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public GenericPropertiesPanel(ACPTFrame frame, JTree genericTree, GenericInfo genericInfo, boolean editable)
    {

        this.frame = frame;
        this.genericTree = genericTree;
        this.genericInfo = genericInfo;
        setAdapters();
        setPanels(genericInfo);
        setEditable(editable);

    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/
    private void setAdapters()
    {

        this.modelPropertiesPanelActionAdapter = new ModelPropertiesPanelActionAdapter(this);

    }

    private void setPanels(GenericInfo genericInfo)
    {

        JLabel valueLabel = new JLabel(" Name: ", SwingConstants.LEFT);
        valueLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        valueLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        this.valueField = new JTextField(genericInfo.getValue());
        this.valueField.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        this.valueField.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        this.updateValueButton = new JButton("Update");
        this.updateValueButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.updateValueButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        this.updateValueButton.addActionListener(modelPropertiesPanelActionAdapter);
        this.updateValueButton.setActionCommand("Update");

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        this.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        this.add(valueLabel);
        this.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        this.add(valueField);
        this.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        this.add(updateValueButton);
        this.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        titledBorder = BorderFactory.createTitledBorder(genericInfo.getValue() + " properties");
        this.setBorder(titledBorder);

    }

    /***************************************************************************
     * Public methods
     **************************************************************************/

    public void setEditable(boolean editable)
    {

        this.editable = editable;
        this.valueField.setEnabled(editable);
        this.updateValueButton.setEnabled(editable);

    }

    public void updateTargetValue(String value)
    {

        this.genericInfo.setValue(value);
        if (genericInfo.getType().equals(GenericInfo.ROOT))
        {
            this.frame.setProjectName(value);
        }
        titledBorder.setTitle(value + " properties");
        this.repaint();
        this.genericTree.repaint();
        System.out.println("Property button size: " + updateValueButton.getSize().toString());

    }

    /***************************************************************************
     * Inner classes
     **************************************************************************/

    /**
     * This inner class implements the target panel mouse adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class ModelPropertiesPanelActionAdapter implements ActionListener
    {

        GenericPropertiesPanel genericPropertiesPanel = null;

        ModelPropertiesPanelActionAdapter(GenericPropertiesPanel modelPropertiesPanel)
        {

            this.genericPropertiesPanel = modelPropertiesPanel;

        }

        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("Group Panel: " + e.getActionCommand());

            if (e.getActionCommand().equals("Update"))
            {

                String value = valueField.getText();
                updateTargetValue(value);

            }
            else
            {

                // System.err.println("GroupPanel warning: command not
                // implemented: " +
                // e.getActionCommand());
            }

        }

    }

}
