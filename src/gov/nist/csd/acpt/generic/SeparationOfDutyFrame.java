package gov.nist.csd.acpt.generic;

import gov.nist.csd.acpt.generic.BasePopupFrame;

import javax.swing.*;

public class SeparationOfDutyFrame extends BasePopupFrame {
    public SeparationOfDutyFrame(JFrame baseFrame, GenericPanel contentPanel) {
        super(baseFrame, contentPanel, "Separation of Duty");
        setSize(780, 460);
        contentPanel.setSubPanels(GenericInfo.SOD);
    }
}
