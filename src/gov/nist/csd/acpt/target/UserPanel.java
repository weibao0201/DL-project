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
package gov.nist.csd.acpt.target;

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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.util.GraphicsUtil;

/**
 * This class implements the (XACML) target editor panel.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */

public class UserPanel extends JPanel
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long     serialVersionUID       = 0;

    /***************************************************************************
     * Variables
     **************************************************************************/

    public JButton                updateValueButton      = null;
    public JTextField             valueField             = null;
    private ACPTFrame             frame                  = null;
    private JTree                 targetTree             = null;
    private TargetInfo            targetInfo             = null;
    private boolean               editable               = false;
    private TitledBorder          titledBorder           = null;

    /* Adapters */
    public UserPanelActionAdapter userPanelActionAdapter = null;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    public UserPanel(ACPTFrame frame, JTree targetTree, TargetInfo targetInfo, boolean editable)
    {

        this.frame = frame;
        this.targetTree = targetTree;
        this.targetInfo = targetInfo;
        setAdapters();
        setPanels(targetInfo);
        setEditable(editable);
    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    private void setAdapters()
    {

        this.userPanelActionAdapter = new UserPanelActionAdapter(this);

    }

    private void setPanels(TargetInfo targetInfo)
    {

        /******************** Name panel *************************/

        JLabel valueLabel = new JLabel("Name: ", SwingConstants.LEFT);
        valueLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        valueLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        this.valueField = new JTextField(targetInfo.getValue());
        this.valueField.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        this.valueField.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        this.updateValueButton = new JButton("Update");
        this.updateValueButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        this.updateValueButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        this.updateValueButton.addActionListener(userPanelActionAdapter);
        this.updateValueButton.setActionCommand("Update");

        JPanel namePanel = new JPanel();
        titledBorder = BorderFactory.createTitledBorder("Name");
        namePanel.setBorder(titledBorder);

        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));

        namePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        namePanel.add(valueLabel);
        namePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        namePanel.add(valueField);
        namePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        namePanel.add(updateValueButton);
        namePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        /******************** Add attributes panel *************************/

        JLabel addAttributeLabel = new JLabel("Attributes: ", SwingConstants.LEFT);
        addAttributeLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        addAttributeLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        // String[] testStrings = { "Attr 1", "Attr 2", "Attr 3", "Attr 4" };
        /*
         * DefaultMutableTreeNode subjectPNode =
         * TargetTreeUtil.getFirstMatchChildNodeFromRoot(targetTree,
         * TargetInfo.SUBJECT_ATTRIBUTES, TargetInfo.SUBJECT_ATTRIBUTES);
         * String[] testStrings =
         * TargetTreeUtil.getChildrenValue2StrArray(subjectPNode);
         */
        // DefaultTreeModel targetTreeModel = (DefaultTreeModel)
        // jtree.getModel();
        // return getFirstMatchChildNode((DefaultMutableTreeNode)
        // targetTreeModel.getRoot(), targetType, value);

        DefaultMutableTreeNode rootNode = TargetTreeUtil.getRootNode(targetTree);
        String[] testStrings =
                TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode, TargetInfo.SUBJECT_ATTRIBUTES, 2);

        JComboBox attributeComboBox = new JComboBox(testStrings);
        // attributeComboBox.setSelectedIndex(0);
        attributeComboBox.setActionCommand("ComboBox");
        attributeComboBox.addActionListener(userPanelActionAdapter);
        attributeComboBox.setPreferredSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        attributeComboBox.setMaximumSize(new Dimension(150, GraphicsUtil.FIELD_HEIGHT));

        JButton addAttributeButton = new JButton("Add");
        addAttributeButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        addAttributeButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        addAttributeButton.addActionListener(userPanelActionAdapter);
        addAttributeButton.setActionCommand("Add");

        JPanel addAttributePanel = new JPanel();

        addAttributePanel.setLayout(new BoxLayout(addAttributePanel, BoxLayout.X_AXIS));

        addAttributePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        addAttributePanel.add(addAttributeLabel);
        addAttributePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        addAttributePanel.add(attributeComboBox);
        addAttributePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        addAttributePanel.add(addAttributeButton);
        addAttributePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        /******************** List attributes panel *************************/

        JLabel listAttributesLabel = new JLabel("Current: ", SwingConstants.LEFT);
        listAttributesLabel.setPreferredSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        listAttributesLabel.setMaximumSize(new Dimension(75, GraphicsUtil.FIELD_HEIGHT));

        // String[] choices = { "A", "long", "array", "of", "strings" };
        String[] choices = {};

        JList list = new JList(choices);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setAlignmentX(LEFT_ALIGNMENT);

        listScroller.setPreferredSize(new Dimension(150, 100));
        listScroller.setMaximumSize(new Dimension(150, 100));

        JButton removeAttributeButton = new JButton("Remove");
        removeAttributeButton.setPreferredSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        removeAttributeButton.setMaximumSize(new Dimension(80, GraphicsUtil.FIELD_HEIGHT));

        removeAttributeButton.addActionListener(userPanelActionAdapter);
        removeAttributeButton.setActionCommand("Remove");

        JPanel removeAttributePanel = new JPanel();

        removeAttributePanel.setLayout(new BoxLayout(removeAttributePanel, BoxLayout.X_AXIS));

        removeAttributePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        removeAttributePanel.add(listAttributesLabel);
        removeAttributePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        removeAttributePanel.add(listScroller);
        removeAttributePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        removeAttributePanel.add(removeAttributeButton);
        removeAttributePanel.add(Box.createRigidArea(new Dimension(15, GraphicsUtil.FIELD_HEIGHT)));

        //
        //
        //
        //
        // JLabel attributeLabel = new JLabel(" Attribute: ", JLabel.LEFT);
        // attributeLabel.setPreferredSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        // attributeLabel.setMaximumSize(
        // new Dimension(75, GraphicsUtil.FIELD_HEIGHT));
        //
        // String[] testStrings = { "Attr 1", "Attr 2", "Attr 3", "Attr 4" };
        //
        // JComboBox attributeComboBox = new JComboBox(testStrings);
        // attributeComboBox.setSelectedIndex(0);
        // attributeComboBox.setActionCommand("ComboBox");
        // attributeComboBox.addActionListener(userPanelActionAdapter);
        // attributeComboBox.setPreferredSize(
        // new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        // attributeComboBox.setMaximumSize(
        // new Dimension(150, GraphicsUtil.FIELD_HEIGHT));
        //
        // this.updateValueButton = new JButton("Add");
        // this.updateValueButton.setPreferredSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        // this.updateValueButton.setMaximumSize(
        // new Dimension(80, GraphicsUtil.FIELD_HEIGHT));
        //
        // this.updateValueButton.addActionListener(userPanelActionAdapter);
        // this.updateValueButton.setActionCommand("Update");
        //
        // JPanel attributeSelectPanel = new JPanel();
        // attributeSelectPanel.setBorder(titledBorder);
        //
        // attributeSelectPanel.setLayout(new BoxLayout(attributeSelectPanel,
        // BoxLayout.X_AXIS));
        //
        // attributeSelectPanel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
        //
        // attributeSelectPanel.add(valueLabel);
        // attributeSelectPanel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
        //
        // attributeSelectPanel.add(valueField);
        // attributeSelectPanel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
        //
        // attributeSelectPanel.add(updateValueButton);
        // attributeSelectPanel.add(Box.createRigidArea(new
        // Dimension(15,GraphicsUtil.FIELD_HEIGHT)));
        //
        // String[] choices = {"A", "long", "array", "of", "strings"};
        //
        // JList list = new JList(choices);
        // JScrollPane listScroller = new JScrollPane(list);
        // listScroller.setPreferredSize(new Dimension(100, 80));
        // listScroller.setAlignmentX(LEFT_ALIGNMENT);
        //
        // JButton addButton = new JButton("Add");
        //
        JPanel attributePanel = new JPanel();
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder("Attributes");
        attributePanel.setBorder(titledBorder2);
        attributePanel.setLayout(new BorderLayout());
        attributePanel.add(addAttributePanel, BorderLayout.NORTH);
        attributePanel.add(removeAttributePanel, BorderLayout.CENTER);

        // attributePanel.setLayout(new BorderLayout());
        // attributePanel.add(attributeSelectPanel, BorderLayout.NORTH);
        // attributePanel.add(listScroller, BorderLayout.CENTER);
        // attributePanel.add(addButton, BorderLayout.EAST);
        //
        this.setLayout(new BorderLayout());
        this.add(namePanel, BorderLayout.NORTH);
        this.add(attributePanel, BorderLayout.CENTER);

        /* Titled border for this panel. */
        titledBorder = BorderFactory.createTitledBorder(targetInfo.getValue() + " properties");
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

        this.targetInfo.setValue(value);
        if (targetInfo.getTargetType().equals(TargetInfo.ROOT))
        {
            this.frame.setProjectName(value);
        }
        titledBorder.setTitle(value + " properties");
        this.repaint();
        this.targetTree.repaint();
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
    class UserPanelActionAdapter implements ActionListener
    {

        UserPanel userPanel = null;

        UserPanelActionAdapter(UserPanel userPanel)
        {

            this.userPanel = userPanel;

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
            else if (e.getActionCommand().equals("ComboBox"))
            {

                // System.out.println("Got combo box");

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
