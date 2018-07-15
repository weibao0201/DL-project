package gov.nist.csd.acpt.util;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class implements Image and ImageIcon retrieval.
 *
 * @author steveq@nist.gov
 * @version $Revision: 1.1 $, $Date: 2008/07/16 17:03:01 $
 * @since 6.0
 */
public class GraphicsUtil
{

    /** Standard height of Swing components in pixels. */
    public static int FIELD_HEIGHT_Large = 50;
    public static int FIELD_HEIGHT       = 23;
    public static int FIELD_HEIGHT_Small = 20;
    // public static int FIELD_HEIGHT_Tight = 23;
    public static int DEFAULT_GAP        = 5;
    public static int GAP_ZERO           = 0;;

    public static void centerDialog(final JDialog dialog)
    {

        // Center frame
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension size = dialog.getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        final int y = screenSize.height - size.height;
        final int x = screenSize.width - size.width;
        dialog.setLocation(x, y);

    }

    public static void centerFrame(final Frame frame)
    {

        // Center frame
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension size = frame.getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        final int y = screenSize.height - size.height;
        final int x = screenSize.width - size.width;
        frame.setLocation(x, y);

    }

    /**
     * Get an ImageIcon from a JAR file.
     *
     * @param imagePath
     * @return
     */
    public static Image getImage(final String imagePath, final Class baseClass)
    {

        Image image = null;

        try
        {

            final InputStream in = baseClass.getResourceAsStream(imagePath);

            if (in == null)
            {

                System.err.println("Warning: Cannot load image: " + imagePath);

            }
            else
            {

                final BufferedInputStream is = new BufferedInputStream(in);
                image = ImageIO.read(is);

                // if (image != null)
                // System.out.println("Successfully loaded image: " +
                // imagePath);
            }

        }
        catch (final IOException ioe)
        {
            ioe.printStackTrace();
        }

        return image;

    }

    /**
     * Get an ImageIcon from a JAR file.
     *
     * @param imagePath
     * @return
     */
    public static ImageIcon getImageIcon(final String imagePath, final Class baseClass)
    {

        final ImageIcon imageIcon = null;
        Image image = null;

        try
        {

            final InputStream in = baseClass.getResourceAsStream(imagePath);

            if (in == null)
            {

                System.err.println("Warning: Cannot load image: " + imagePath);

            }
            else
            {

                final BufferedInputStream is = new BufferedInputStream(in);
                image = ImageIO.read(is);

                // if (image != null)
                // System.out.println("Successfully loaded image: " +
                // imagePath);
            }

        }
        catch (final IOException ioe)
        {
            ioe.printStackTrace();
        }

        // jhwang4 temporarily added the following line in case that an Image is
        // missing.
        if (image == null)
        {
            return null;
        }
        return new ImageIcon(image);
    }

    public static String showTextDialog(final String message, final String title, final JFrame frame)
    {

        return "";
    }

    /**
     * This method shows an text field input dialog and returns the value. This
     * method will loop through a warning dialog if the user does not enter
     * data.
     *
     * @param message
     *            The dialog message.
     * @param frameTitle
     *            The dialog frame title.
     * @param warningMessage
     *            The warning message for empty input values.
     * @param warningTitle
     *            The warning title for empty input values.
     * @param defaultValue
     *            An initial default value.
     * @param frame
     *            The application frame.
     * @return
     */
    public static String showTextFieldInputDialog(final String message, final String frameTitle,
            final String warningMessage, final String warningTitle, final String defaultValue, final JFrame frame)
    {

        String inputValue = null;
        boolean done = false;

        while (!done)
        {

            inputValue = (String) JOptionPane.showInputDialog(frame, message, frameTitle, JOptionPane.PLAIN_MESSAGE,
                    null, null, defaultValue);

            if (inputValue == null)
            {

                System.err.println("WARNING: TargetPanel childTargetName is null");
                done = true;

            }
            else if (inputValue.equals(""))
            {

                GraphicsUtil.showWarningDialog(warningMessage, warningTitle, frame);
            }
            else
            {

                done = true;
            }

        }

        return inputValue;

    }

    public static void showWarningDialog(final String message, final String title, final JFrame frame)
    {

        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.WARNING_MESSAGE);

    }

}
