package gov.nist.csd.acpt.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class StringToXML
{

    public static void StringToXML(String xmlString, String outputName)
    {

        PrintStream output = null;
        try
        {
            output = new PrintStream(new FileOutputStream(outputName, false));
        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }

        output.println(xmlString);
        output.close();

    }

    /*
     * public static void StringToXML(String xmlString, String outputName) {
     *
     * DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
     *
     * DocumentBuilder builder; try { builder = factory.newDocumentBuilder();
     *
     * // Use String reader Document document = builder.parse( new InputSource(
     * new StringReader( xmlString ) ) );
     *
     * TransformerFactory tranFactory = TransformerFactory.newInstance();
     * Transformer aTransformer = tranFactory.newTransformer(); Source src = new
     * DOMSource( document ); Result dest = new StreamResult( new File(
     * outputName ) ); aTransformer.transform( src, dest ); } catch (Exception
     * e) { // TODO Auto-generated catch block e.printStackTrace(); }
     *
     *
     * }
     */

}