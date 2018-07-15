package gov.nist.csd.acpt.generic;

import gov.nist.csd.acpt.ACPTFrame;
import gov.nist.csd.acpt.target.TargetInfo;
import gov.nist.csd.acpt.target.TargetTreeUtil;
import gov.nist.csd.acpt.util.GraphicsUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class SoDPanel extends JPanel {

    private ACPTFrame                   frame                       = null;
    private JTree                       modelTree                   = null;
    private JTree                       sodTree                     = null;
    private GenericInfo                 targetInfo                  = null;
    private JTree                       targetTree                  = null;
    public DefaultMutableTreeNode       sodTreeRootNode             = null;

    private SoDPanelActionAdapter       soDPanelActionAdapter       = null;

    public JList                        subjectList                 = null;
    public DefaultListModel             subjectListModel            = null;
    public JList                        subjectSelectedList         = null;
    public DefaultListModel             subjectSelectedListModel    = null;

    public JList                        resourceList                = null;
    public DefaultListModel             resourceListModel           = null;
    public JList                        resourceSelectedList        = null;
    public DefaultListModel             resourceSelectedListModel   = null;

    public JList                        actionList                = null;
    public DefaultListModel             actionListModel           = null;
    public JList                        actionSelectedList        = null;
    public DefaultListModel             actionSelectedListModel   = null;

    public JList                        environmentList                = null;
    public DefaultListModel             environmentListModel           = null;
    public JList                        environmentSelectedList        = null;
    public DefaultListModel             environmentSelectedListModel   = null;

    private boolean                     editable                    = false;
    private TitledBorder                titledBorder                = null;

    private final static String         SUBJECT_SELECT_LEFT_BTN     = "sslb";
    private final static String         SUBJECT_SELECT_RIGHT_BTN    = "ssrb";
    private final static String         RESOURCE_SELECT_LEFT_BTN    = "rslb";
    private final static String         RESOURCE_SELECT_RIGHT_BTN   = "rsrb";
    private final static String         ACTION_SELECT_LEFT_BTN      = "aslb";
    private final static String         ACTION_SELECT_RIGHT_BTN     = "asrb";
    private final static String         ENVIRONMENT_SELECT_LEFT_BTN      = "eslb";
    private final static String         ENVIRONMENT_SELECT_RIGHT_BTN     = "esrb";
    private final static String         REFRESH_BTN                 = "refresh";
    private final static String         PROPERTY_BTN                = "property";

    public SoDPanel(ACPTFrame frame, JTree sodTree, GenericInfo targetInfo, boolean editable) {
        this.frame = frame;
        this.modelTree = frame.modelPanel.getModelTree();
        this.targetTree = frame.targetPanel.getTargetTree();
        this.sodTree = sodTree;
        this.targetInfo = targetInfo;

        setVariables();
        setAdapters();
        setPanels(targetInfo);
        setEditable(editable);
    }

    private void setVariables() {
        this.sodTreeRootNode = GenericTreeUtil.getFirstMatchingNode(this.sodTree, GenericInfo.ROOT, 0);
    }

    private void setAdapters() {
        this.soDPanelActionAdapter = new SoDPanelActionAdapter(this);
    }

    private void setPanels(GenericInfo targetInfo) {

        loadData();

        JPanel subjectPanel;
        JPanel resourcePanel;
        JPanel actionPanel;
        JPanel environmentPanel;

        JScrollPane subjectOrigin;
        JScrollPane subjectTarget;
        JPanel subjectSelect;
        JButton subjectSelectLeftBtn;
        JButton subjectSelectRightBtn;

        JScrollPane resourceOrigin;
        JScrollPane resourceTarget;
        JPanel resourceSelect;
        JButton resourceSelectLeftBtn;
        JButton resourceSelectRightBtn;

        JScrollPane actionOrigin;
        JScrollPane actionTarget;
        JPanel actionSelect;
        JButton actionSelectLeftBtn;
        JButton actionSelectRightBtn;

        JScrollPane environmentOrigin;
        JScrollPane environmentTarget;
        JPanel environmentSelect;
        JButton environmentSelectLeftBtn;
        JButton environmentSelectRightBtn;

        JPanel refreshPanel;

        //subject panel
        subjectPanel = new JPanel();
        subjectPanel.setLayout(new BorderLayout(0, 0));
        subjectPanel.setPreferredSize(new Dimension(150, 300)); //330, 300


        subjectPanel.setBorder(BorderFactory.createTitledBorder("subjects"));

        subjectOrigin = new JScrollPane(subjectList);
        subjectOrigin.setPreferredSize(new Dimension(70, 300)); //120, 300
        subjectPanel.add(subjectOrigin, BorderLayout.WEST);

        subjectTarget = new JScrollPane(subjectSelectedList);
//        subjectTarget.setPreferredSize(new Dimension(80, 300));
//        subjectPanel.add(subjectTarget, BorderLayout.EAST);

        subjectSelect = new JPanel();
        subjectSelect.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
        subjectPanel.add(subjectSelect, BorderLayout.CENTER);

        subjectSelectLeftBtn = new JButton("Confirm");
        subjectSelectLeftBtn.setHorizontalAlignment(0);
        subjectSelectLeftBtn.setHorizontalTextPosition(0);
        subjectSelectLeftBtn.addActionListener(soDPanelActionAdapter);
        subjectSelectLeftBtn.setActionCommand(SUBJECT_SELECT_LEFT_BTN);

        subjectSelectRightBtn = new JButton("-->");
//        subjectSelectRightBtn.setHorizontalAlignment(0);
//        subjectSelectRightBtn.setHorizontalTextPosition(0);
//        subjectSelectRightBtn.addActionListener(soDPanelActionAdapter);
//        subjectSelectRightBtn.setActionCommand(SUBJECT_SELECT_RIGHT_BTN);

//        subjectSelect.add(subjectSelectRightBtn);
        subjectSelect.add(subjectSelectLeftBtn);

        //resource panel
        resourcePanel = new JPanel();
        resourcePanel.setLayout(new BorderLayout(0, 0));
        resourcePanel.setPreferredSize(new Dimension(150, 300)); //330, 300

        resourcePanel.setBorder(BorderFactory.createTitledBorder("resources"));
        resourceOrigin = new JScrollPane(resourceList);
        resourceOrigin.setPreferredSize(new Dimension(70, 300));
        resourcePanel.add(resourceOrigin, BorderLayout.WEST);

        resourceTarget = new JScrollPane(resourceSelectedList);
//        resourceTarget.setPreferredSize(new Dimension(80, 300));
//        resourcePanel.add(resourceTarget, BorderLayout.EAST);

        resourceSelect = new JPanel();
        resourceSelect.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
        resourcePanel.add(resourceSelect, BorderLayout.CENTER);

        resourceSelectLeftBtn = new JButton("Confirm");
        resourceSelectLeftBtn.setHorizontalAlignment(0);
        resourceSelectLeftBtn.setHorizontalTextPosition(0);
        resourceSelectLeftBtn.addActionListener(soDPanelActionAdapter);
        resourceSelectLeftBtn.setActionCommand(RESOURCE_SELECT_LEFT_BTN);

        resourceSelectRightBtn = new JButton("-->");
//        resourceSelectRightBtn.setHorizontalAlignment(0);
//        resourceSelectRightBtn.setHorizontalTextPosition(0);
//        resourceSelectRightBtn.addActionListener(soDPanelActionAdapter);
//        resourceSelectRightBtn.setActionCommand(RESOURCE_SELECT_RIGHT_BTN);

//        resourceSelect.add(resourceSelectRightBtn);
        resourceSelect.add(resourceSelectLeftBtn);

        //action panel
        actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout(0, 0));
        actionPanel.setPreferredSize(new Dimension(150, 300)); //330, 300

        actionPanel.setBorder(BorderFactory.createTitledBorder("actions"));
        actionOrigin = new JScrollPane(actionList);
        actionOrigin.setPreferredSize(new Dimension(70, 300));
        actionPanel.add(actionOrigin, BorderLayout.WEST);

        actionTarget = new JScrollPane(actionSelectedList);
//        actionTarget.setPreferredSize(new Dimension(80, 300));
//        actionPanel.add(actionTarget, BorderLayout.EAST);

        actionSelect = new JPanel();
        actionSelect.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
        actionPanel.add(actionSelect, BorderLayout.CENTER);

        actionSelectLeftBtn = new JButton("Confirm");
        actionSelectLeftBtn.setHorizontalAlignment(0);
        actionSelectLeftBtn.setHorizontalTextPosition(0);
        actionSelectLeftBtn.addActionListener(soDPanelActionAdapter);
        actionSelectLeftBtn.setActionCommand(ACTION_SELECT_LEFT_BTN);

        actionSelectRightBtn = new JButton("-->");
//        actionSelectRightBtn.setHorizontalAlignment(0);
//        actionSelectRightBtn.setHorizontalTextPosition(0);
//        actionSelectRightBtn.addActionListener(soDPanelActionAdapter);
//        actionSelectRightBtn.setActionCommand(ACTION_SELECT_RIGHT_BTN);

//        actionSelect.add(actionSelectRightBtn);
        actionSelect.add(actionSelectLeftBtn);

        //environment panel
        environmentPanel = new JPanel();
        environmentPanel.setLayout(new BorderLayout(0, 0));
        environmentPanel.setPreferredSize(new Dimension(150, 300)); //330, 300

        environmentPanel.setBorder(BorderFactory.createTitledBorder("environment"));
        environmentOrigin = new JScrollPane(environmentList);
        environmentOrigin.setPreferredSize(new Dimension(70, 300));
        environmentPanel.add(environmentOrigin, BorderLayout.WEST);

        environmentTarget = new JScrollPane(environmentSelectedList);
//        environmentTarget.setPreferredSize(new Dimension(80, 300));
//        environmentPanel.add(environmentTarget, BorderLayout.EAST);

        environmentSelect = new JPanel();
        environmentSelect.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 3));
        environmentPanel.add(environmentSelect, BorderLayout.CENTER);

        environmentSelectLeftBtn = new JButton("Confirm");
        environmentSelectLeftBtn.setHorizontalAlignment(0);
        environmentSelectLeftBtn.setHorizontalTextPosition(0);
        environmentSelectLeftBtn.addActionListener(soDPanelActionAdapter);
        environmentSelectLeftBtn.setActionCommand(ENVIRONMENT_SELECT_LEFT_BTN);

        environmentSelectRightBtn = new JButton("-->");
//        environmentSelectRightBtn.setHorizontalAlignment(0);
//        environmentSelectRightBtn.setHorizontalTextPosition(0);
//        environmentSelectRightBtn.addActionListener(soDPanelActionAdapter);
//        environmentSelectRightBtn.setActionCommand(ENVIRONMENT_SELECT_RIGHT_BTN);

//        environmentSelect.add(environmentSelectRightBtn);
        environmentSelect.add(environmentSelectLeftBtn);

//        //combine action panel and environment panel
        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BorderLayout(0, 0));
        combinedPanel.setPreferredSize(new Dimension(330, 300)); //330, 300

        combinedPanel.add(actionPanel, BorderLayout.WEST);
        combinedPanel.add(environmentPanel, BorderLayout.EAST);


        JButton refreshBtn = new JButton("refresh");
        refreshBtn.setHorizontalAlignment(0);
        refreshBtn.setHorizontalTextPosition(0);
        refreshBtn.addActionListener(soDPanelActionAdapter);
        refreshBtn.setActionCommand(REFRESH_BTN);

        JButton propertyBtn = new JButton("Security Requirements");
        propertyBtn.setHorizontalAlignment(0);
        propertyBtn.setHorizontalTextPosition(0);
        propertyBtn.addActionListener(soDPanelActionAdapter);
        propertyBtn.setActionCommand(PROPERTY_BTN);

        refreshPanel = new JPanel();
        refreshPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        refreshPanel.add(refreshBtn);
//        refreshPanel.add(propertyBtn);

        this.setLayout(new BorderLayout());
        //this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createTitledBorder("SoD"));
        this.add(Box.createRigidArea(new Dimension(300, GraphicsUtil.FIELD_HEIGHT)));//100
        this.add(subjectPanel, BorderLayout.WEST);
        this.add(resourcePanel, BorderLayout.CENTER);
        this.add(combinedPanel, BorderLayout.EAST);
        this.add(refreshPanel, BorderLayout.PAGE_END);
        //this.add(Box.createRigidArea(new Dimension(300, GraphicsUtil.FIELD_HEIGHT)));
    }

    private void loadData() {
        // subject list
        subjectListModel = new DefaultListModel();
        subjectList = new JList(subjectListModel);

        String[] subjects = getSubjects();
        for (String subject : subjects) {
            subjectListModel.addElement(subject);
        }

        // resource list
        resourceListModel = new DefaultListModel();
        resourceList = new JList(resourceListModel);

        String[] resources = getResources();
        for (String resource : resources) {
            resourceListModel.addElement(resource);
        }

        // action list
        actionListModel = new DefaultListModel();
        actionList = new JList(actionListModel);

        String[] actions = getActions();
        for (String action : actions) {
            actionListModel.addElement(action);
        }

        // environment list
        environmentListModel = new DefaultListModel();
        environmentList = new JList(environmentListModel);

        String[] environments = getEnvironments();
        for (String environment : environments) {
            actionListModel.addElement(environment);
        }

        // selected subject list
        subjectSelectedListModel = new DefaultListModel();
        subjectSelectedList = new JList(subjectSelectedListModel);

        List subjectsSelected =
            GenericTreeUtil.getTargetTreeChildrenNode2List(sodTreeRootNode, GenericInfo.SOD_SUBJECTS_VALUES, 1);
        for (Object choice : subjectsSelected) {
            subjectSelectedListModel.addElement(choice);
        }

        // selected resource list
        resourceSelectedListModel = new DefaultListModel();
        resourceSelectedList = new JList(resourceSelectedListModel);

        List resourceSelected =
                GenericTreeUtil.getTargetTreeChildrenNode2List(sodTreeRootNode, GenericInfo.SOD_RESOURCES_VALUES, 1);
        for (Object choice : resourceSelected) {
            resourceSelectedListModel.addElement(choice);
        }

        // selected action list
        actionSelectedListModel = new DefaultListModel();
        actionSelectedList = new JList(actionSelectedListModel);

        List actionSelected =
                GenericTreeUtil.getTargetTreeChildrenNode2List(sodTreeRootNode, GenericInfo.SOD_ACTIONS_VALUES, 1);
        for (Object choice : actionSelected) {
            actionSelectedListModel.addElement(choice);
        }

        // selected environment list
        environmentSelectedListModel = new DefaultListModel();
        environmentSelectedList = new JList(environmentSelectedListModel);

        List environmentSelected =
                GenericTreeUtil.getTargetTreeChildrenNode2List(sodTreeRootNode, GenericInfo.SOD_ENVIRONMENTS_VALUES, 1);
        for (Object choice : environmentSelected) {
            environmentSelectedListModel.addElement(choice);
        }
    }

    private void refresh() {
        String[] subjects = getSubjects();
        for (String subject : subjects) {
            subjectListModel.addElement(subject);
        }

        String[] resources = getResources();
        for (String resource : resources) {
            resourceListModel.addElement(resource);
        }

        String[] actions = getActions();
        for (String action : actions) {
            actionListModel.addElement(action);
        }

        String[] environments = getEnvironments();
        for (String environment : environments) {
            environmentListModel.addElement(environment);
        }
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        // this.valueField.setEnabled(editable);
        // this.updateValueButton.setEnabled(editable);
    }

    public String[] getSubjects() {
        return getChoices(TargetInfo.SUBJECTS, TargetInfo.SUBJECT_ATTRIBUTES, TargetInfo.SUBJECT_ATTR_VALUSES);
    }

    public String[] getResources() {
        return getChoices(TargetInfo.RESOURCES, TargetInfo.RESOURCE_ATTRIBUTES, TargetInfo.RESOURCE_ATTR_VALUSES);
    }

    public String[] getActions() {
        return getChoices(TargetInfo.ACTIONS, TargetInfo.ACTION_ATTRIBUTES, TargetInfo.ACTION_ATTR_VALUSES);
    }

    public String[] getEnvironments() {
        return getChoices(TargetInfo.ENVIRONMENTS, TargetInfo.ENVIRONMENT_ATTRIBUTES, TargetInfo.ENVIRONMENT_ATTR_VALUSES);
    }

    public String[] getChoices(String modelName, String modelAttr, String modalAttrVal) {
        DefaultMutableTreeNode rootNode =
                TargetTreeUtil.getFirstMatchingNode(targetTree, modelName, TargetInfo.Level);
        java.util.List subject_a = TargetTreeUtil.getTargetTreeChildrenNode2List(rootNode, modelAttr,
                TargetInfo.attrLevel);
        int count = 0;
        // Figures out how big the String Array choices needs to be
        for (int i = 0; i < subject_a.size(); i++)
        {
            String valueOfvalue =
                    ((TargetInfo) ((DefaultMutableTreeNode) subject_a.get(i)).getUserObject()).getvalueOfvalue();
            rootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, valueOfvalue, modelAttr,
                    TargetInfo.attrLevel);
            String[] subject_a_v = TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode,
                    modalAttrVal, TargetInfo.nodeLevel);
            count += subject_a_v.length;
        }
        // Creates a String array that holds all the possible choices
        // that will be add to the Combo Box
        String[] choices = new String[count];
        count = 0;
        for (int i = 0; i < subject_a.size(); i++)
        {
            String valueOfvalue =
                    ((TargetInfo) ((DefaultMutableTreeNode) subject_a.get(i)).getUserObject()).getvalueOfvalue();
            rootNode = TargetTreeUtil.getFirstMatchingNode(targetTree, valueOfvalue, modelAttr,
                    TargetInfo.attrLevel);
            String[] subject_a_v = TargetTreeUtil.getTargetTreeChildrenValues2StrArr(rootNode,
                    modalAttrVal, TargetInfo.nodeLevel);
            for (int j = 0; j < subject_a_v.length; j++)
            {
                choices[j + count] = valueOfvalue + ";" + subject_a_v[j];
            }
            count += subject_a_v.length;
        }
        return choices;
    }

    public class SoDPanelActionAdapter implements ActionListener {

        private SoDPanel soDPanel;

        public SoDPanelActionAdapter(SoDPanel soDPanel) {
            this.soDPanel = soDPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case SUBJECT_SELECT_LEFT_BTN:
                    removeNode(GenericInfo.SOD_SUBJECTS_VALUES, subjectSelectedList ,subjectSelectedListModel);
                    break;
                case SUBJECT_SELECT_RIGHT_BTN:
                    addNode(GenericInfo.SOD_SUBJECTS_VALUES, subjectList, subjectSelectedListModel);
                    break;
                case RESOURCE_SELECT_LEFT_BTN:
                    removeNode(GenericInfo.SOD_RESOURCES_VALUES, resourceSelectedList ,resourceSelectedListModel);
                    break;
                case RESOURCE_SELECT_RIGHT_BTN:
                    addNode(GenericInfo.SOD_RESOURCES_VALUES, resourceList, resourceSelectedListModel);
                    break;
                case ACTION_SELECT_LEFT_BTN:
                    removeNode(GenericInfo.SOD_ACTIONS_VALUES, actionSelectedList ,actionSelectedListModel);
                    break;
                case ACTION_SELECT_RIGHT_BTN:
                    addNode(GenericInfo.SOD_ACTIONS_VALUES, actionList, actionSelectedListModel);
                    break;
                case ENVIRONMENT_SELECT_LEFT_BTN:
                    removeNode(GenericInfo.SOD_ENVIRONMENTS_VALUES, environmentSelectedList ,environmentSelectedListModel);
                    break;
                case ENVIRONMENT_SELECT_RIGHT_BTN:
                    addNode(GenericInfo.SOD_ENVIRONMENTS_VALUES, environmentList, environmentSelectedListModel);
                    break;
                case REFRESH_BTN:
                    frame.refreshSoDPanel();
                    break;
                case PROPERTY_BTN:
                    soDPanel.frame.sodmoveToFront();
                    break;
                default:
                    break;
            }
        }
    }


    private void removeNode(String type, JList selectedList, DefaultListModel selectedListModel) {
        List selectedValuesList = selectedList.getSelectedValuesList();

        if (selectedList == null) {
            return;
        }

        for (Object selectedValue : selectedValuesList) {
            selectedListModel.removeElement(selectedValue);
            removeNode(type, selectedValue);
        }
    }


    private void addNode(String type, JList selectedList, DefaultListModel selectedListModel) {
        List list = selectedList.getSelectedValuesList();

        if (selectedList == null) {
            return;
        }

        for (Object selectedValue : list) {
            if (addNode(type, selectedValue)) {
                selectedListModel.addElement(selectedValue);
            }
        }
    }

    private boolean addNode(String type, Object value) {
        return GenericTreeUtil.addChildNode(type, value.toString(), sodTree, sodTreeRootNode);
    }

    private void removeNode(String type, Object value) {
        GenericTreeUtil.removeChildNode(type, value.toString(), sodTree, sodTreeRootNode);
    }

}
