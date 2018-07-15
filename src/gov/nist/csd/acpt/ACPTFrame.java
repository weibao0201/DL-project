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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import gov.nist.csd.acpt.generic.*;
import gov.nist.csd.acpt.model.ABACBoolPanel;
import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.model.ModelPanel;
import gov.nist.csd.acpt.model.ModelTreeUtil;
import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetPanel;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.DomParser;
import gov.nist.csd.acpt.util.Generalproperties;
import gov.nist.csd.acpt.util.GraphicsUtil;
import gov.nist.csd.acpt.util.ProjectPropertyDialog;
import gov.nist.csd.acpt.util.XMLCreator;

/**
 * This class implements the Access Control Policy Test (ACPT) application
 * frame.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 1.6
 */
public class ACPTFrame extends JFrame
{

    /***************************************************************************
     * Constants
     **************************************************************************/

    private static final long serialVersionUID           = 0;

    private static String     ACPT_TITLE                 = "ACPT";

    /***************************************************************************
     * Variables
     **************************************************************************/

    private String            projectName                = null;

    /* Panes and panels */
    public JLayeredPane       layeredPane                = null;
    public JLayeredPane       sodlayeredPane             = null;
    public JTabbedPane        tabbedPane                 = null;
    public TargetPanel        targetPanel                = null;
    public TargetPanel        subjectPanel               = null;
    public TargetPanel        resourcePanel              = null;
    public TargetPanel        actionanel                 = null;
    public TargetPanel        enviromnmentPanel          = null;

    /* Bruce Batson June 2013 Edit */
    public TargetPanel        inheritancePanel           = null;
    public disclaimer         disclaim                   = null;
    public ABACPolicyClaimer  abacPolicyClaimer          = null;
    public CaveatClaimer      caveatClaimer              = null;
    /* End of Edit */

    public ModelPanel         modelPanel                 = null;
    // public ModelPanel RBACPanel = null;
    public ABACBoolPanel      ABACBoolPanel              = null;
    public ModelPanel         ABACPanel                  = null;
    public ModelPanel         MultiLevelPanel            = null;
    public ModelPanel         WorkFlowPanel              = null;
    public GenericPanel       modelCombinePanel          = null;
    public GenericPanel       NuSMVPanel                 = null;
    public GenericPanel       CombinatorialTestPanel     = null;
    public GenericPanel       XACMLGenerationPanel       = null;
    public GenericPanel       propertyGenerationPanel    = null;
    public GenericPanel       sodPanel                   = null;

    public LogPanel           logPanel                   = null;

    /* Menu items */
    public JMenuItem          newMenuItem                = null;
    public JMenuItem          projectPropertyItem        = null;
    public JMenuItem          openProjectMenuItem        = null;
    public JMenuItem          openSampleMenuItem         = null;
    public JMenuItem          saveMenuItem               = null;
    public JMenuItem          saveAsMenuItem             = null;
    public JMenuItem          printMenuItem              = null;
    public JMenuItem          importMenu                 = null;
    public JMenuItem          importTargetsExcelMenuItem = null;
    public JMenuItem          importTargetsXACMLMenuItem = null;
    public JMenuItem          exportXACMLMenuItem        = null;
    public JMenuItem          exitMenuItem               = null;
    public JMenuItem          sampleACMenuItem           = null;
    public JMenuItem          cutMenuItem                = null;
    public JMenuItem          copyMenuItem               = null;
    public JMenuItem          pasteMenuItem              = null;
    public JMenuItem          addChildMenuItem           = null;
    public JMenuItem          deleteMenuItem             = null;
    public JMenuItem          deleteChildrenMenuItem     = null;
    public JMenuItem          preferencesMenuItem        = null;

    /* Adapters */

    public ACPTActionAdapter  acptActionAdapter          = null;
    public ACPTMouseAdapter   acptMouseAdapter           = null;
    public ACPTWindowAdapter  acptWindowAdapter          = null;

    //
    public File               selectedOpenProjFile       = null;

    public boolean            showABACBoolPanel          = true;

    /***************************************************************************
     * Constructors
     **************************************************************************/

    /**
     * Constructor.
     */
    public ACPTFrame()
    {
        /*
         * Here is were the Disclaimer Dialog appears to ask the user if they
         * wish to continue. If they chose no then the program will close.
         */
        disclaim = new disclaimer(this, "Disclaimer", true);
        if(disclaim.getAnswer())
        {
            caveatClaimer = new CaveatClaimer(this, "ACPT Caveat", true);
            if(caveatClaimer.getAnswer())
            {
                abacPolicyClaimer = new ABACPolicyClaimer(this, "ACPT Caveat", true);
                if (abacPolicyClaimer.getAnswer())
                {
                    showABACBoolPanel = false;
                }
                setFrame();
                setAdapters();
                setMenus();
                setPanels();
                setProperties();
                display();
            }
            else {
                System.exit(0);
            }
        }
        else
        {
            System.exit(0);
        }

    }

    /***************************************************************************
     * Initialization methods
     **************************************************************************/

    /**
     * Set the main ACPT frame.
     */
    private void setFrame()
    {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.addWindowListener(this.acptWindowAdapter);

        this.setTitle(ACPT_TITLE);
        // this.setIconImage(GraphicsUtil.getImage("/images/padlock.jpg",
        // getClass()));

    }

    private void setAdapters()
    {

        this.acptActionAdapter = new ACPTActionAdapter(this);
        this.acptMouseAdapter = new ACPTMouseAdapter(this);
        this.acptWindowAdapter = new ACPTWindowAdapter(this);

    }

    /**
     * Create ACPT menus and menu items.
     */
    private void setMenus()
    {

        /******************** File menu ***************************/

        JMenu fileMenu = new JMenu("File");

        newMenuItem = new JMenuItem("New Project");
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        newMenuItem.addActionListener(acptActionAdapter);
        newMenuItem.setActionCommand("New Project");
        newMenuItem.setEnabled(true);

        openProjectMenuItem = new JMenuItem("Project");
        openProjectMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        openProjectMenuItem.addActionListener(acptActionAdapter);
        openProjectMenuItem.setActionCommand("Open Project");
        openProjectMenuItem.setEnabled(true);

        openSampleMenuItem = new JMenuItem("Sample");
        openSampleMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        openSampleMenuItem.addActionListener(acptActionAdapter);
        openSampleMenuItem.setActionCommand("Open Sample");
        openSampleMenuItem.setEnabled(true);
        openSampleMenuItem.setVisible(Generalproperties.SampleOn);

        JMenu openMenu = new JMenu("Open...");
        openMenu.add(openProjectMenuItem);
        openMenu.add(openSampleMenuItem);

        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        saveMenuItem.addActionListener(acptActionAdapter);
        saveMenuItem.setActionCommand("Save");
        saveMenuItem.setEnabled(false);

        saveAsMenuItem = new JMenuItem("Save As...");
        saveAsMenuItem.addActionListener(acptActionAdapter);
        saveAsMenuItem.setActionCommand("SaveAs");
        saveAsMenuItem.setEnabled(false);

        printMenuItem = new JMenuItem("Print");
        printMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        printMenuItem.addActionListener(acptActionAdapter);
        printMenuItem.setActionCommand("Print");
        printMenuItem.setEnabled(false);

        importTargetsExcelMenuItem = new JMenuItem("Targets Excel");
        importTargetsExcelMenuItem.addActionListener(acptActionAdapter);
        importTargetsExcelMenuItem.setActionCommand("Import Targets Excel");
        importTargetsExcelMenuItem.setEnabled(false);

        importTargetsXACMLMenuItem = new JMenuItem("Targets XACML");
        importTargetsXACMLMenuItem.addActionListener(acptActionAdapter);
        importTargetsXACMLMenuItem.setActionCommand("Import Targets XACML");
        importTargetsXACMLMenuItem.setEnabled(false);

        importMenu = new JMenu("Import...");
        importMenu.setEnabled(false);
        importMenu.add(importTargetsExcelMenuItem);
        importMenu.add(importTargetsXACMLMenuItem);

        exportXACMLMenuItem = new JMenuItem("Export XACML");
        exportXACMLMenuItem.addActionListener(acptActionAdapter);
        exportXACMLMenuItem.setActionCommand("Export XACML");
        exportXACMLMenuItem.setEnabled(false);

        exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
        exitMenuItem.addActionListener(acptActionAdapter);
        exitMenuItem.setActionCommand("Exit");
        exitMenuItem.setEnabled(true);

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenu);
        fileMenu.addSeparator();
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(printMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(importMenu);
        fileMenu.add(exportXACMLMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        /******************** Edit menu ***************************/

        JMenu editMenu = new JMenu("Edit");

        cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        cutMenuItem.addActionListener(acptActionAdapter);
        cutMenuItem.setActionCommand("Cut");
        cutMenuItem.setEnabled(false);

        copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        copyMenuItem.addActionListener(acptActionAdapter);
        copyMenuItem.setActionCommand("Copy");
        copyMenuItem.setEnabled(false);

        pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        pasteMenuItem.addActionListener(acptActionAdapter);
        pasteMenuItem.setActionCommand("Paste");
        pasteMenuItem.setEnabled(false);

        addChildMenuItem = new JMenuItem("Add");
        addChildMenuItem.addActionListener(acptActionAdapter);
        addChildMenuItem.setActionCommand("Add Child Node");
        addChildMenuItem.setEnabled(false);

        deleteMenuItem = new JMenuItem("Remove");
        deleteMenuItem.addActionListener(acptActionAdapter);
        deleteMenuItem.setActionCommand("Remove Node");
        deleteMenuItem.setEnabled(false);

        deleteChildrenMenuItem = new JMenuItem("Remove Contents");
        deleteChildrenMenuItem.addActionListener(acptActionAdapter);
        deleteChildrenMenuItem.setActionCommand("Remove Children Nodes");
        deleteChildrenMenuItem.setEnabled(false);

        preferencesMenuItem = new JMenuItem("Preferences");
        preferencesMenuItem.addActionListener(acptActionAdapter);
        preferencesMenuItem.setActionCommand("Preferences");
        preferencesMenuItem.setEnabled(true);

        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        editMenu.addSeparator();
        editMenu.add(addChildMenuItem);
        editMenu.add(deleteMenuItem);
        editMenu.add(deleteChildrenMenuItem);
        editMenu.addSeparator();
        editMenu.add(preferencesMenuItem);

        /******************** Menu bar ***************************/

        JMenu projectMenu = new JMenu("Project");

        projectPropertyItem = new JMenuItem("Properties");
        projectPropertyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        projectPropertyItem.addActionListener(acptActionAdapter);
        projectPropertyItem.setActionCommand("Properties");
        projectPropertyItem.setEnabled(true);

        projectMenu.add(projectPropertyItem);

        /******************** Menu bar ***************************/

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(projectMenu);

        this.setJMenuBar(menuBar);

    }

    /**
     * Create ACPT panels.
     */
    private void setPanels()
    {

        /******************** Tabbed pane ***************************/
        tabbedPane = new JTabbedPane();
        tabbedPane.setEnabled(false);
        tabbedPane.addMouseListener(acptMouseAdapter);

        targetPanel = new TargetPanel(this, TargetInfo.ROOT);
        // targetPanel.createNewProject();
        // tabbedPane.addTab("Target", null, targetPanel, "Set target data");
        if (showABACBoolPanel) {
            ABACBoolPanel = new ABACBoolPanel(this);// , null, null,
            tabbedPane.addTab("Pseudo Exhausted Testing", null, ABACBoolPanel, "Run ABAC Boolean Expressions");
        }else {
            //WB
            subjectPanel = new TargetPanel(this, TargetInfo.SUBJECTS);
            inheritancePanel = new TargetPanel(this, TargetInfo.INHERITANCE);

            layeredPane = new JLayeredPane();

            subjectPanel.setBounds(0, 0, 800, 375);
            inheritancePanel.setBounds(0, 0, 800, 375);
//            layeredPane.setBounds(0, 0, 600, 400);
//            layeredPane.addMouseMotionListener(acptMouseAdapter);
//            layeredPane.setLayout(new BorderLayout());
            layeredPane.add(subjectPanel, new Integer(100), 0);
//            subjectPanel.setOpaque(false);
//            layeredPane.setLayout(new BoxLayout(layeredPane, BoxLayout.Y_AXIS));
            layeredPane.add(inheritancePanel, new Integer(0), 0);

            tabbedPane.addTab("Subject", null, layeredPane, "Set subject data");

//            subjectPanel = new TargetPanel(this, TargetInfo.SUBJECTS);
//            tabbedPane.addTab("Subject", null, subjectPanel, "Set subject data");

            resourcePanel = new TargetPanel(this, TargetInfo.RESOURCES);
            tabbedPane.addTab("Resource", null, resourcePanel, "Set resource data");

            actionanel = new TargetPanel(this, TargetInfo.ACTIONS);
            tabbedPane.addTab("Action", null, actionanel, "Set action data");

            enviromnmentPanel = new TargetPanel(this, TargetInfo.ENVIRONMENTS);
            tabbedPane.addTab("Environment", null, enviromnmentPanel, "Set environments data");

        /* Bruce Batson June 2013 Edit */
//            inheritancePanel = new TargetPanel(this, TargetInfo.INHERITANCE);
//            tabbedPane.addTab("Inheritance", null, inheritancePanel, "Set Inheritance");
        /* End of edit */

            // modelPanel = new ModelPanel(this, targetPanel.getTargetTree());

            modelPanel = new ModelPanel(this);
            // modelPanel.createModelTree();
            // tabbedPane.addTab("Model", null, modelPanel, "Set AC model");

            // RBACPanel = new ModelPanel(this);
            // tabbedPane.addTab("RBAC", null, RBACPanel, "Set RBAC model");

            ABACPanel = new ModelPanel(this);
            tabbedPane.addTab("ABAC", null, ABACPanel, "Set ABAC model");


            // rootPaneCheckingEnabled);


            MultiLevelPanel = new ModelPanel(this);
            tabbedPane.addTab("MultiLevel", null, MultiLevelPanel, "Set MultiLevel model");

            WorkFlowPanel = new ModelPanel(this);
            tabbedPane.addTab("WorkFlow", null, WorkFlowPanel, "Set WorkFlow model");



            //WB
            sodPanel = new GenericPanel(this, GenericInfo.SOD);
            sodPanel.setBounds(0, 0, 800, 375);

//            tabbedPane.addTab("Separation of Duty Requirements", null, sodPanel, "Set Separation of Duty");

            propertyGenerationPanel = new GenericPanel(this, GenericInfo.PROPERTY);
            propertyGenerationPanel.setBounds(0, 0, 800, 375);

            sodlayeredPane = new JLayeredPane();
            sodlayeredPane.add(propertyGenerationPanel, new Integer(100), 0);
            sodlayeredPane.add(sodPanel, new Integer(0), 0);
            tabbedPane.addTab("Security Requirements", null, sodlayeredPane, "Set Security Requirement");



            modelCombinePanel = new GenericPanel(this, GenericInfo.MODELCOMBINE);
            tabbedPane.addTab("Combination", null, modelCombinePanel, "Set Model Combination");

            NuSMVPanel = new GenericPanel(this, GenericInfo.NUSMV);
            tabbedPane.addTab("Verification", null, NuSMVPanel, "Set NuSMV");

            CombinatorialTestPanel = new GenericPanel(this, GenericInfo.TEST);
            tabbedPane.addTab("Test", null, CombinatorialTestPanel, "Set Test");

            XACMLGenerationPanel = new GenericPanel(this, GenericInfo.XACML);
            tabbedPane.addTab("XACML", null, XACMLGenerationPanel, "Set XACML");
        }
        /*
         * modelCombinePanel = new ModelCombinePanel(this); tabbedPane.addTab(
         * "Model Combination", null, modelCombinePanel, "Set Model Composition"
         * );
         *
         * NuSMVPanel = new NuSMVPanel(this); // NuSMVPanel = new
         * NuSMVPanel(this,targetPanel.getTargetTree(),
         * modelPanel.getModelTree()); tabbedPane.addTab("NuSMV", null,
         * NuSMVPanel, "Set NuSMV");
         */
        /*
         * CombinatorialTestPanel = new CombinatorialTestPanel(this);
         * tabbedPane.addTab("Test Generation", null, CombinatorialTestPanel,
         * "Set Combinatorial Test");
         *
         * XACMLGenerationPanel = new XACMLGenerationPanel(this);
         * tabbedPane.addTab("XACML Generation", null, XACMLGenerationPanel,
         * "Set XACML");
         */
        /******************** Log panel ***************************/

        logPanel = new LogPanel();

        /******************** Split pane **************************/

        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, tabbedPane, logPanel);
        verticalSplitPane.setOneTouchExpandable(true);
        verticalSplitPane.setDividerLocation(400);

        /******************** Content pane ************************/

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(verticalSplitPane, BorderLayout.CENTER);

    }

    private void display()
    {

        this.setMinimumSize(new Dimension(800, 600));
        this.setPreferredSize(new Dimension(800, 600));
        GraphicsUtil.centerFrame(this);

        this.setVisible(true);
    }

    /**
     * Set properties
     */
    private void setProperties()
    {
        Generalproperties p = new Generalproperties();
        p.setProperties();
    }

    /***************************************************************************
     * Public methods
     **************************************************************************/

    public void moveToFront()
    {
        layeredPane.setLayer(subjectPanel, new Integer(100), 0);
        layeredPane.setLayer(inheritancePanel, new Integer(0), 0);
    }

    public void moveToBack()
    {
        layeredPane.setLayer(subjectPanel, new Integer(0), 0);
        layeredPane.setLayer(inheritancePanel, new Integer(100), 0);
    }

    public void sodmoveToFront()
    {
        sodlayeredPane.setLayer(propertyGenerationPanel, new Integer(100), 0);
        sodlayeredPane.setLayer(sodPanel, new Integer(0), 0);


    }

    public void sodmoveToBack()
    {
        sodlayeredPane.setLayer(propertyGenerationPanel, new Integer(0), 0);
        sodlayeredPane.setLayer(sodPanel, new Integer(100), 0);

    }

    public void log(String msg)
    {
        logPanel.append(msg);
    }

    public void clearLog()
    {
        logPanel.clear();
    }

    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
        this.setTitle(ACPT_TITLE + " - " + projectName);
        // this.title = projectName;
        // this.targetPanel.
    }

    public String getProjectName()
    {
        return this.projectName;
    }

    public boolean projectIsSet()
    {
        if (this.projectName == null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // refresh subpanels according to changes of polcies & rules
    public void refreshSubPanels()
    {
        modelCombinePanel.setSubPanels(GenericInfo.MODELCOMBINE);
        XACMLGenerationPanel.setSubPanels(GenericInfo.XACML);
        NuSMVPanel.setSubPanels(GenericInfo.NUSMV);
        CombinatorialTestPanel.setSubPanels(GenericInfo.TEST);
        propertyGenerationPanel.setSubPanels(GenericInfo.PROPERTY);
//        SoDPanel.setSubPanels(GenericInfo.SOD);
    }

    public void refreshSoDPanel() {
        sodPanel.setSubPanels(GenericInfo.SOD);
        DefaultMutableTreeNode sodTreeRootNode = GenericTreeUtil.
                getFirstMatchingNode(sodPanel.getGenericTree(), GenericInfo.ROOT, 0);
        sodTreeRootNode.removeAllChildren();
    }

    public void refreshSubPanelsWhenCombinationChanges()
    {
        XACMLGenerationPanel.setSubPanels(GenericInfo.XACML);
        NuSMVPanel.setSubPanels(GenericInfo.NUSMV);
        CombinatorialTestPanel.setSubPanels(GenericInfo.TEST);
        propertyGenerationPanel.setSubPanels(GenericInfo.PROPERTY);
//        SoDPanel.setSubPanels(GenericInfo.SOD);
    }
    /***************************************************************************
     * Inner classes
     **************************************************************************/

    /**
     * This inner class implements the Access Control Policy Test (ACPT) action
     * listener.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class ACPTActionAdapter implements ActionListener
    {

        ACPTFrame frame = null;

        ACPTActionAdapter(ACPTFrame frame)
        {

            this.frame = frame;

        }

        /**
         * Handle action events.
         *
         * @param e
         *            The action event.
         */
        @Override
        public void actionPerformed(ActionEvent e)
        {

            System.out.println("ACPTFrame: " + e.getActionCommand());
            if (e.getActionCommand().equals("Properties"))
            {
                ProjectPropertyDialog myDialog = new ProjectPropertyDialog(frame, "Project Properties", true);

                if (myDialog.getAnswer())
                {
                }

                myDialog.dispose();

            }
            else if (e.getActionCommand().equals("New Project"))
            {

                String projectName = targetPanel.createNewProject();

                // if projectName is null by clicking "cancel" or "close" button
                // in the dialogue
                if (projectName == null)
                {
                    return;
                }

                modelPanel.createModelTree();
                // Target tree is used in subject/resource/action Panels
                DefaultMutableTreeNode targetTreeRoot = TargetTreeUtil.getRootNode(targetPanel.getTargetTree());

                subjectPanel.updatePanel(new DefaultTreeModel(targetTreeRoot));
                resourcePanel.updatePanel(new DefaultTreeModel(targetTreeRoot));
                actionanel.updatePanel(new DefaultTreeModel(targetTreeRoot));
                enviromnmentPanel.updatePanel(new DefaultTreeModel(targetTreeRoot));

                /* Bruce Batson June 2013 Edit */
                inheritancePanel.updatePanel(new DefaultTreeModel(targetTreeRoot));
                /* End of Edit */

                // resourcePanel.setResourcesPanel(new
                // DefaultTreeModel(targetTreeRoot));
                // actionanel.setActionsPanel(new
                // DefaultTreeModel(targetTreeRoot));

                // Model tree is used in different sub model Panels
                DefaultMutableTreeNode modelTreenode = null;

                // modelTreenode = ModelTreeUtil.getFirstMatchingNode
                // (modelPanel.getModelTree(), ModelInfo.RBAC, 1);
                // RBACPanel.setAttributeTree((new
                // DefaultTreeModel(modelTreenode)));

                modelTreenode = ModelTreeUtil.getFirstMatchingNode(modelPanel.getModelTree(), ModelInfo.ABAC, 1);
                ABACPanel.setAttributeTree((new DefaultTreeModel(modelTreenode)));

                modelTreenode = ModelTreeUtil.getFirstMatchingNode(modelPanel.getModelTree(), ModelInfo.MULTILEVEL, 1);
                MultiLevelPanel.setAttributeTree((new DefaultTreeModel(modelTreenode)));

                modelTreenode = ModelTreeUtil.getFirstMatchingNode(modelPanel.getModelTree(), ModelInfo.WORKFLOW, 1);
                WorkFlowPanel.setAttributeTree((new DefaultTreeModel(modelTreenode)));

                sodPanel.createModelTree();
                sodPanel.setSubPanels(GenericInfo.SOD);

                propertyGenerationPanel.createModelTree();
                propertyGenerationPanel.setSubPanels(GenericInfo.PROPERTY);

                modelCombinePanel.createModelTree();
                modelCombinePanel.setSubPanels(GenericInfo.MODELCOMBINE);

                NUSMVPanel.resultLst = new ArrayList();
                TestPanel.resultLst = new ArrayList();
                XACMLPanel.resultLst = new ArrayList();

                NuSMVPanel.createModelTree();
                NuSMVPanel.setSubPanels(GenericInfo.NUSMV);

                CombinatorialTestPanel.createModelTree();
                CombinatorialTestPanel.setSubPanels(GenericInfo.TEST);

                XACMLGenerationPanel.createModelTree();
                XACMLGenerationPanel.setSubPanels(GenericInfo.XACML);

                saveMenuItem.setEnabled(false);
                //WB
//                tabbedPane.setSelectedComponent(subjectPanel);
                tabbedPane.setSelectedComponent(layeredPane);

                // tabbedPane.setEnabledAt(4, false);
                // tabbedPane.setEnabledAt(6, false);
                // tabbedPane.setEnabledAt(8, false);

                // tabbedPane.remove(MultiLevelPanel);
                // tabbedPane.remove(NuSMVPanel);
                // tabbedPane.setEnabledAt(5, false);

                // MultiLevelPanel.setEnabled(true);
                // propertyGenerationPanel.setEnabled(true);
                // NuSMVPanel.setEnabled(true);

                // tabbedPane.remove(MultiLevelPanel);
                // propertyGenerationPanel.setVisible(false);
                // tabbedPane.remove(NuSMVPanel);

            }
            else if (e.getActionCommand().equals("Open Sample"))
            {

                targetPanel.createSampleProject();
                modelPanel.createSampleProject();

                DefaultMutableTreeNode targetTreeRoot = TargetTreeUtil.getRootNode(targetPanel.getTargetTree());

                String[] choices =
                        TargetTreeUtil.getTargetTreeChildrenValues2StrArr(targetTreeRoot, TargetInfo.SUBJECTS, 2);
                // for (int i = 0; i < choices.length; i++) {
                // System.err.println(choices[i]);
                // }

                subjectPanel.updatePanel((new DefaultTreeModel(targetTreeRoot)));
                resourcePanel.updatePanel((new DefaultTreeModel(targetTreeRoot)));
                actionanel.updatePanel((new DefaultTreeModel(targetTreeRoot)));
                enviromnmentPanel.updatePanel((new DefaultTreeModel(targetTreeRoot)));

                // resourcePanel.setResourcesPanel(new
                // DefaultTreeModel(targetTreeRoot));
                // actionanel.setActionsPanel(new
                // DefaultTreeModel(targetTreeRoot));

                DefaultMutableTreeNode modelTreenode = null;
                // modelTreenode = ModelTreeUtil.getFirstMatchingNode
                // (modelPanel.getModelTree(), ModelInfo.RBAC, 1);
                // RBACPanel.setAttributeTree((new
                // DefaultTreeModel(modelTreenode)));

                modelTreenode = ModelTreeUtil.getFirstMatchingNode(modelPanel.getModelTree(), ModelInfo.ABAC, 1);
                ABACPanel.setAttributeTree((new DefaultTreeModel(modelTreenode)));
                // tabbedPane.setSelectedComponent(ABACPanel);

                modelTreenode = ModelTreeUtil.getFirstMatchingNode(modelPanel.getModelTree(), ModelInfo.MULTILEVEL, 1);
                MultiLevelPanel.setAttributeTree((new DefaultTreeModel(modelTreenode)));

                modelTreenode = ModelTreeUtil.getFirstMatchingNode(modelPanel.getModelTree(), ModelInfo.WORKFLOW, 1);
                WorkFlowPanel.setAttributeTree((new DefaultTreeModel(modelTreenode)));
                // tabbedPane.setSelectedComponent(WorkFlowPanel);

                sodPanel.createSampleProject(GenericInfo.SOD);
                sodPanel.setSubPanels(GenericInfo.SOD);

                propertyGenerationPanel.createSampleProject(GenericInfo.PROPERTY);
                propertyGenerationPanel.setSubPanels(GenericInfo.PROPERTY);
                // tabbedPane.setSelectedComponent(propertyGenerationPanel);

                modelCombinePanel.createSampleProject(GenericInfo.MODELCOMBINE);
                modelCombinePanel.setSubPanels(GenericInfo.MODELCOMBINE);

                NUSMVPanel.resultLst = new ArrayList();
                TestPanel.resultLst = new ArrayList();
                XACMLPanel.resultLst = new ArrayList();

                NuSMVPanel.createSampleProject(GenericInfo.NUSMV);
                NuSMVPanel.setSubPanels(GenericInfo.NUSMV);

                CombinatorialTestPanel.createSampleProject(GenericInfo.TEST);
                CombinatorialTestPanel.setSubPanels(GenericInfo.TEST);

                XACMLGenerationPanel.createSampleProject(GenericInfo.XACML);
                XACMLGenerationPanel.setSubPanels(GenericInfo.XACML);

                tabbedPane.setSelectedComponent(XACMLGenerationPanel);

                saveMenuItem.setEnabled(false);

                // tabbedPane.remove(MultiLevelPanel);
                // tabbedPane.remove(NuSMVPanel);
                // tabbedPane.setEnabledAt(5, false);

            }
            else if (e.getActionCommand().equals("Open Project"))
            {

                File selected = TestPanelUtil.getDlgOpenFileName("xml", "ACPT Files");

                if (selected == null)
                {
                    frame.logPanel.append("No file is selected");
                    return;
                }
                // frame.setTitle("abc");d
                // frame.setProjectName(selected.getName());
                // DNI temp
                // File selected = new File("DNI_demo.xml");

                // create an instance
                DomParser dpe = new DomParser();

                // call run example
                dpe.parseXmlFile(selected);
                DefaultMutableTreeNode rootNodeOfTargetTree = dpe.parseDocument("Target");
                DefaultMutableTreeNode rootNodeOfModelTree = dpe.parseDocument("Model");
                DefaultMutableTreeNode rootNodeOfCombineTree = dpe.parseDocument("Combinations");
                DefaultMutableTreeNode rootNodeOfPropertyree = dpe.parseDocument("Property");

                DefaultMutableTreeNode rootNodeOfSoDTree = null;

                try {
                    rootNodeOfSoDTree = dpe.parseDocument("SecurityConstraints");
                } catch (Exception e1) {
                    // xml doesn`t have 'SecurityConstraints' element
                    // do nothing
                }

                // Note that old version does not have Environment Element.
                // Therefore, to avoid errors, if Environment Element does not
                // exist, the following code creates and adds the element

                List elist = TargetTreeUtil.getTargetTreeChildrenValues2List(rootNodeOfTargetTree,
                        TargetInfo.ENVIRONMENTS, TargetInfo.Level);

                if (elist.size() == 0)
                {
                    DefaultMutableTreeNode enviromnments = new DefaultMutableTreeNode(
                            new TargetInfo(TargetInfo.ENVIRONMENTS, TargetInfo.ENVIRONMENTS));
                    rootNodeOfTargetTree.add(enviromnments);
                }
                /* Bruce Batson June 2013 Edit */
                // Note that old version does not have Inheritance Element.
                // Therefore, to avoid errors, if Inheritance Element does not
                // exist, the following code creates and adds the element
                List ilist = TargetTreeUtil.getTargetTreeChildrenValues2List(rootNodeOfTargetTree,
                        TargetInfo.INHERITANCE, TargetInfo.Level);
                if (ilist.size() == 0)
                {
                    DefaultMutableTreeNode inheritance =
                            new DefaultMutableTreeNode(new TargetInfo(TargetInfo.INHERITANCE, TargetInfo.INHERITANCE));
                    rootNodeOfTargetTree.add(inheritance);
                }
                /* End of Edit */

                /*
                 * ---- add node manually--- DefaultMutableTreeNode root
                 *
                 * getTargetTreeChildrenValues2List DefaultMutableTreeNode
                 * enviromnments = new DefaultMutableTreeNode( new
                 * TargetInfo(TargetInfo.ENVIRONMENTS,
                 * TargetInfo.ENVIRONMENTS)); root.add(enviromnments);
                 * attributeNode =
                 * TargetTreeUtil.getFirstMatchingNode(targetTree,
                 * this.targetType, TargetInfo.Level);
                 *
                 * List<TargetInfo> getTargetTreeChildrenValues2List
                 * (DefaultMutableTreeNode AncestoNode, String TargetInfoType,
                 * int NodeLevel)
                 */

                // targetPanel.createSavedProject(((TargetInfo)rootNodeOfTargetTree.getUserObject()).getValue(),
                // rootNodeOfTargetTree);

                targetPanel.createSavedProject(selected.getName(), rootNodeOfTargetTree);

                modelPanel.createSavedProject(rootNodeOfModelTree);

                DefaultMutableTreeNode targetTreeRoot = TargetTreeUtil.getRootNode(targetPanel.getTargetTree());
                subjectPanel.updatePanel((new DefaultTreeModel(targetTreeRoot)));
                resourcePanel.updatePanel((new DefaultTreeModel(targetTreeRoot)));
                actionanel.updatePanel((new DefaultTreeModel(targetTreeRoot)));
                enviromnmentPanel.updatePanel((new DefaultTreeModel(targetTreeRoot)));

                /* Bruce Batson June 2013 Edit */
                inheritancePanel.updatePanel(new DefaultTreeModel(targetTreeRoot));
                /* End of Edit */

                DefaultMutableTreeNode modelTreenode = null;

                modelTreenode = ModelTreeUtil.getFirstMatchingNode(modelPanel.getModelTree(), ModelInfo.ABAC, 1);
                ABACPanel.setAttributeTree((new DefaultTreeModel(modelTreenode)));

                modelTreenode = ModelTreeUtil.getFirstMatchingNode(modelPanel.getModelTree(), ModelInfo.MULTILEVEL, 1);
                MultiLevelPanel.setAttributeTree((new DefaultTreeModel(modelTreenode)));

                modelTreenode = ModelTreeUtil.getFirstMatchingNode(modelPanel.getModelTree(), ModelInfo.WORKFLOW, 1);
                WorkFlowPanel.setAttributeTree((new DefaultTreeModel(modelTreenode)));

                propertyGenerationPanel.createSavedProject(GenericInfo.PROPERTY, rootNodeOfPropertyree);
                propertyGenerationPanel.setSubPanels(GenericInfo.PROPERTY);

                modelCombinePanel.createSavedProject(GenericInfo.MODELCOMBINE, rootNodeOfCombineTree);
                modelCombinePanel.setSubPanels(GenericInfo.MODELCOMBINE);

                if (rootNodeOfSoDTree == null) {
                    sodPanel.createModelTree();
                }
                else {
                    sodPanel.createSavedProject(GenericInfo.SOD, rootNodeOfSoDTree);
                }
                sodPanel.setSubPanels(GenericInfo.SOD);

                NuSMVPanel.createSampleProject(GenericInfo.NUSMV);
                NuSMVPanel.setSubPanels(GenericInfo.NUSMV);

                CombinatorialTestPanel.createSampleProject(GenericInfo.TEST);
                CombinatorialTestPanel.setSubPanels(GenericInfo.TEST);

                XACMLGenerationPanel.createSampleProject(GenericInfo.XACML);
                XACMLGenerationPanel.setSubPanels(GenericInfo.XACML);

                // tabbedPane.setSelectedComponent(subjectPanel);
                // tabbedPane.setSelectedComponent(NuSMVPanel);

                // enable save button
                saveMenuItem.setEnabled(true);
                selectedOpenProjFile = selected;

                // tabbedPane.remove(MultiLevelPanel);
                // tabbedPane.remove(NuSMVPanel);
                // tabbedPane.setEnabledAt(5, false);

            }
            else if (e.getActionCommand().equals("Save"))
            {
                File selected = selectedOpenProjFile;

                // create an instance
                XMLCreator xce = new XMLCreator();

                // run the example
                xce.createRootEle();
                xce.runExample(targetPanel.getTargetTree(), "Target");
                xce.runExample(modelPanel.getModelTree(), "Model");

                xce.runExample(sodPanel.getGenericTree(), "SecurityConstraints");

                xce.runExample(modelCombinePanel.getGenericTree(), "Combinations");
                xce.runExample(propertyGenerationPanel.getGenericTree(), "Property");
                xce.printToFile(selected);
            }
            else if (e.getActionCommand().equals("SaveAs"))
            {

                // --
                // if (this.projectName )
                File selected = TestPanelUtil.getDlgSelectedFileName(projectName, "xml", "ACPT Files");

                if (selected == null)
                {
                    frame.logPanel.append("No file is selected");
                    return;
                }

                frame.setProjectName(selected.getName());

                // create an instance
                XMLCreator xce = new XMLCreator();

                // run the example
                xce.createRootEle();
                xce.runExample(targetPanel.getTargetTree(), "Target");
                xce.runExample(modelPanel.getModelTree(), "Model");

                xce.runExample(sodPanel.getGenericTree(), "SecurityConstraints");

                xce.runExample(modelCombinePanel.getGenericTree(), "Combinations");
                xce.runExample(propertyGenerationPanel.getGenericTree(), "Property");
                xce.printToFile(selected);

                // enable save button
                saveMenuItem.setEnabled(true);
                selectedOpenProjFile = selected;

            }
            else if (e.getActionCommand().equals("Exit"))
            {

                System.exit(0);

            }
            else if (e.getActionCommand().equals("Add Child"))
            {

                if (tabbedPane.getSelectedComponent() instanceof gov.nist.csd.acpt.target.TargetPanel)
                {

                    frame.targetPanel.addChildNode();

                }
                else if (tabbedPane.getSelectedComponent() instanceof gov.nist.csd.acpt.model.ModelPanel)
                {

                    frame.modelPanel.addChildNode();

                }

            }
            else if (e.getActionCommand().equals("Remove"))
            {

                if (tabbedPane.getSelectedComponent() instanceof gov.nist.csd.acpt.target.TargetPanel)
                {

                    frame.targetPanel.removeNode();

                }
                else if (tabbedPane.getSelectedComponent() instanceof gov.nist.csd.acpt.model.ModelPanel)
                {

                    frame.modelPanel.removeNode();

                }

            }
            else if (e.getActionCommand().equals("Remove Children"))
            {

                if (tabbedPane.getSelectedComponent() instanceof gov.nist.csd.acpt.target.TargetPanel)
                {

                    frame.targetPanel.removeAllChildren();

                }
                else if (tabbedPane.getSelectedComponent() instanceof gov.nist.csd.acpt.model.ModelPanel)
                {

                    frame.modelPanel.removeAllChildren();

                }

            }
            else
            {

                log("Warning: command not implemented: " + e.getActionCommand());
            }
        }

    }

    /**
     * This inner class implements the Access Control Policy Test (ACPT) mouse
     * adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class ACPTMouseAdapter extends MouseAdapter
    {

        ACPTFrame frame = null;

        ACPTMouseAdapter(ACPTFrame frame)
        {

            this.frame = frame;

        }

        @Override
        public void mouseClicked(MouseEvent e)
        {

            if (e.getSource() == tabbedPane)
            {

                if (tabbedPane.getSelectedComponent() == CombinatorialTestPanel)
                {

                    // System.err.println("TEST Panel is selected");
                    CombinatorialTestPanel.setSubPanels(GenericInfo.TEST);

                }
                else if (tabbedPane.getSelectedComponent() == modelCombinePanel)
                {

                    // System.err.println("TEST Panel is selected");
                    modelCombinePanel.setSubPanels(GenericInfo.MODELCOMBINE);

                }
                else if (tabbedPane.getSelectedComponent() == XACMLGenerationPanel)
                {

                    // System.err.println("TEST Panel is selected");
                    XACMLGenerationPanel.setSubPanels(GenericInfo.XACML);

                }
                else if (tabbedPane.getSelectedComponent() == NuSMVPanel)
                {

                    // System.err.println("TEST Panel is selected");
                    NuSMVPanel.setSubPanels(GenericInfo.NUSMV);

                }
                else if (tabbedPane.getSelectedComponent() == NuSMVPanel)
                {

                    // System.err.println("TEST Panel is selected");
                    NuSMVPanel.setSubPanels(GenericInfo.NUSMV);

                }
                else if (tabbedPane.getSelectedComponent() == propertyGenerationPanel)
                {

                    // System.err.println("TEST Panel is selected");
                    propertyGenerationPanel.setSubPanels(GenericInfo.PROPERTY);

                }
//                else if (tabbedPane.getSelectedComponent() == sodPanel)
//                {
//
//                    // System.err.println("TEST Panel is selected");
//                    sodPanel.setSubPanels(GenericInfo.SOD);
//
//                }

                else if (tabbedPane.getSelectedComponent() instanceof gov.nist.csd.acpt.model.ModelPanel)
                {

                    // GraphicsUtil
                    // .showWarningDialog(
                    // "Models require that you create all necessary target data
                    // first.",
                    // "Model Creation Warning", frame);
                    //
                }

                /* Disable menu items when selecting a tab */
                cutMenuItem.setEnabled(false);
                copyMenuItem.setEnabled(false);
                pasteMenuItem.setEnabled(false);
                addChildMenuItem.setEnabled(false);
                deleteMenuItem.setEnabled(false);
                deleteChildrenMenuItem.setEnabled(false);
            }
        }

    }

    /**
     * This inner class implements the Access Control Policy Test (ACPT) window
     * adapter.
     *
     * @author steveq@nist.gov
     * @version $Revision$, $Date$
     * @since 1.6
     */
    class ACPTWindowAdapter extends WindowAdapter
    {

        ACPTFrame frame = null;

        ACPTWindowAdapter(ACPTFrame frame)
        {

            this.frame = frame;

        }

        @Override
        public void windowClosing(WindowEvent e)
        {
            // if (policyTreeModel.getChildCount(policyTreeModel.getRoot()) > 0)
            // {
            // /*
            // * int resp = JOptionPane.showConfirmDialog(this,
            // * "Not Save. Do you wish to save the changes?", "Saving...",
            // * JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE); if
            // * (resp == JOptionPane.YES_OPTION) { actionPerformed(new
            // * ActionEvent(mnuItGuardar, 0, "mnuItGuardar")); }
            // */
            // }
            System.exit(0);
        }

    }

}
