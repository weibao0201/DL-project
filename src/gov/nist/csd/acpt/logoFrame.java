package gov.nist.csd.acpt;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class logoFrame extends JFrame implements WindowListener
{

    String    logofile    = "res/logo.JPG";
    int       height      = 320;           // pixel
    int       wideth      = 610;           // pixel
    int       miliseconds = 10000;         // seconds
    ImageIcon icon;
    Image     image;

    public logoFrame()
    {
        super("A Frame");
        setSize(wideth, height);
        setLogoLocation();
        removerBorder();
        setLogoAsBackground();

        // in case the user closes the window
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        setVisible(true);
        setAlwaysOnTop(true); // set always true as window
        // enables Window Events on this Component
        this.addWindowListener(this);

        ACPTMouseAdapter mouseActionLisener = new ACPTMouseAdapter(this);
        this.addMouseListener(mouseActionLisener);
        // start a timer
        Thread t = new Timer();
        t.start();

    }

    private void setLogoAsBackground()
    {
        // background
        icon = new ImageIcon(logofile);

        JPanel panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                // Dispaly image at at full size
                g.drawImage(icon.getImage(), 0, 0, null);
                super.paintComponent(g);
            }
        };

        panel.setOpaque(false);
        getContentPane().add(panel);

    }

    private void removerBorder()
    {
        // remove border (title bar) ok.
        setUndecorated(true);

    }

    private void setLogoLocation()
    {
        // location
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension inicioSize = getSize();
        if (inicioSize.height > screenSize.height)
        {
            inicioSize.height = screenSize.height;
        }
        if (inicioSize.width > screenSize.width)
        {
            inicioSize.width = screenSize.width;
        }
        setLocation((screenSize.width - inicioSize.width) / 2, (screenSize.height - inicioSize.height - 100) / 2);
    }

    @Override
    public void windowOpened(WindowEvent e)
    {
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
    }

    // the event that we are interested in
    @Override
    public void windowClosed(WindowEvent e)
    {

        super.dispose();
        // sSystem.exit(0);
    }

    @Override
    public void windowIconified(WindowEvent e)
    {
    }

    @Override
    public void windowDeiconified(WindowEvent e)
    {
    }

    @Override
    public void windowActivated(WindowEvent e)
    {
    }

    @Override
    public void windowDeactivated(WindowEvent e)
    {
    }

    // a simple timer
    class Timer extends Thread
    {
        @Override
        public void run()
        {
            try
            {
                sleep(miliseconds);
            }
            catch (InterruptedException e)
            {
            }
            // close the frame
            logoFrame.this.processWindowEvent(new WindowEvent(logoFrame.this, WindowEvent.WINDOW_CLOSED));

            // added to finalize
            try
            {
                super.finalize();
            }
            catch (Throwable e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    // instantiate the Frame
    public static void main(String args[])
    {
        new logoFrame();
    }

    /**
     * This inner class implements the Access Control Policy Test (ACPT) mouse
     * adapter.
     *
     */

    class ACPTMouseAdapter extends MouseAdapter
    {

        ACPTFrame frame = null;

        public ACPTMouseAdapter(logoFrame logoFrame)
        {
            this.frame = frame;
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            System.out.println("clicked");
            logoFrame.this.processWindowEvent(new WindowEvent(logoFrame.this, WindowEvent.WINDOW_CLOSED));

        }
    }
}
