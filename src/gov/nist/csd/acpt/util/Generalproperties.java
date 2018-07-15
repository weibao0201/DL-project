package gov.nist.csd.acpt.util;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * This class implements Image and ImageIcon retrieval.
 *
 * @author steveq@nist.gov
 * @version $Revision: 1.1 $, $Date: 2008/07/16 17:03:01 $
 * @since 6.0
 */
public class Generalproperties
{

    public static String   PropertyConfigfile          = "res/config.properties";

    public static boolean  TreeViewable                = false;
    public static boolean  DummyEnabledForAttributesOn = false;                   // (false)
                                                                                  // dummy
                                                                                  // values
                                                                                  // are
                                                                                  // used
                                                                                  // as
                                                                                  // A
                                                                                  // =
                                                                                  // dummy
    public static boolean  SampleOn                    = false;                   // SampeOn
                                                                                  // (True)
                                                                                  // shows
                                                                                  // an
                                                                                  // sample
    public static String   Fireeye                     = "";
    public static String   XACML2Templates             = "";
    public static String   XACML3Templates             = "";
    public static String   NUSMV                       = "";
    public static String   NUSMVTemplates              = "";
    public static int      ProcessStateNumber          = 10;

    public static String[] ProcessStateNumberArray;

    public static int      MultiLevelStateNumber       = 10;
    public static String[] MultiLevelStateNumberArray;

    public void setProperties()
    {

        Properties properties = new Properties();
        try
        {
            properties.load(new FileInputStream(PropertyConfigfile));
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null,
                    "Unable to load or find custom config properties (default config file name is config.properties) ",
                    "Warning", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String temp = properties.getProperty("TreeViewable");
        if (temp != null)
        {
            TreeViewable = temp.equalsIgnoreCase("yes");
        }

        temp = properties.getProperty("DummyEnabledForAttributesOn");

        if (temp != null)
        {
            DummyEnabledForAttributesOn = temp.equalsIgnoreCase("yes");
        }

        Fireeye = properties.getProperty("Fireeye");
        Fireeye = Fireeye.replace("\"", "");

        if (!(new File(Fireeye)).exists())
        {
            JOptionPane.showMessageDialog(null, "fireeye file (in config properties) does not exists", "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
        }

        XACML2Templates = properties.getProperty("XACML2Templates");
        XACML2Templates = XACML2Templates.replace("\"", "");

        if (!(new File(XACML2Templates)).exists())
        {
            JOptionPane.showMessageDialog(null, "XACML 2.0 Template file (in config properties) does not exists",
                    "Warning", JOptionPane.INFORMATION_MESSAGE);
        }

        XACML3Templates = properties.getProperty("XACML3Templates");
        XACML3Templates = XACML3Templates.replace("\"", "");

        if (!(new File(XACML3Templates)).exists())
        {
            JOptionPane.showMessageDialog(null, "XACML 3.0 Template file (in config properties) does not exists",
                    "Warning", JOptionPane.INFORMATION_MESSAGE);
        }

        NUSMV = properties.getProperty("NUSMV");
        // System.out.println(NUSMV);
        NUSMV = NUSMV.replace("\"", "");
        if (!(new File(NUSMV)).exists())
        {
            JOptionPane.showMessageDialog(null, "Cannot find NuSMV execution file configured in config properties",
                    "Warning", JOptionPane.INFORMATION_MESSAGE);
        }

        NUSMVTemplates = properties.getProperty("NUSMVTemplates");
        NUSMVTemplates = NUSMVTemplates.replace("\"", "");
        if (!(new File(NUSMVTemplates)).exists())
        {
            JOptionPane.showMessageDialog(null, "Cannot find NUSMV Template file configured in config properties",
                    "Warning", JOptionPane.INFORMATION_MESSAGE);
        }

        ProcessStateNumber = Integer.parseInt(properties.getProperty("ProcessStateNumber"));

        ProcessStateNumberArray = new String[ProcessStateNumber];
        for (int i = 0; i < ProcessStateNumber; i++)
        {
            ProcessStateNumberArray[i] = (i + 1) + "";
        }

        MultiLevelStateNumber = Integer.parseInt(properties.getProperty("MultiLevelStateNumber"));

        MultiLevelStateNumberArray = new String[MultiLevelStateNumber];
        for (int i = 0; i < MultiLevelStateNumber; i++)
        {
            MultiLevelStateNumberArray[i] = (i + 1) + "";
        }
    }

}
