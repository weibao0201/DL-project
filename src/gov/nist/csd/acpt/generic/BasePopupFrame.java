package gov.nist.csd.acpt.generic;

import org.jfree.ui.tabbedui.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.jfree.ui.tabbedui.VerticalLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public abstract class BasePopupFrame extends JFrame implements WindowListener {
    private JFrame baseFrame;

    public BasePopupFrame(JFrame baseFrame, JPanel contentPanel) {
        this(baseFrame, contentPanel, "");
    }

    public BasePopupFrame(JFrame baseFrame, JPanel contentPanel, String title) {
        super();

        this.baseFrame = baseFrame;

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new VerticalLayout());
        wrapperPanel.add(contentPanel);

        JButton closeButton = new JButton();
        closeButton.setText("Close");
        closeButton.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        wrapperPanel.add(closeButton);

        setSize(720, 460);
        setMinimumSize(new Dimension(720, 460));
        setTitle(title);
        setContentPane(wrapperPanel);
        addWindowListener(this);
        baseFrame.setEnabled(false);
    }

    @Override
    public void windowClosing(WindowEvent e) {
        baseFrame.setEnabled(true);
    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }
}
