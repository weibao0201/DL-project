package gov.nist.csd.acpt.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class implements target tree utility functions.
 *
 * @author steveq@nist.gov
 * @version $Revision$, $Date$
 * @since 6.0
 */
public class Util
{

    /**
     * Using JAXP in implementation independent manner create a document object
     * using which we create a xml tree in memory
     */
    private void createDocument()
    {

        // get an instance of factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            // get an instance of builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // create an instance of DOM
            // dom = db.newDocument();

        }
        catch (ParserConfigurationException pce)
        {
            // dump it
            System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
            System.exit(1);
        }

    }

}
