package gov.nist.csd.acpt.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import gov.nist.csd.acpt.generic.GenericInfo;
import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.target.TargetInfo;

public class DomParser
{

    // No generics
    List     myEmpls;
    Document dom;

    // public DomParser(){
    // //create a list to hold the employee objects
    // myEmpls = new ArrayList();
    // }

    // public DefaultMutableTreeNode runExample(String treeType) {
    //
    // //parse the xml file and get the dom object
    // //parseXmlFile();
    //
    // //get each employee element and create a Employee object
    // //parseDocument(treeType);
    //
    // //Iterate through the list and print the data
    // //printData();
    //
    // }
    //

    public void parseXmlFile(File file)
    {
        // get the factory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try
        {

            // Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();

            // parse using builder to get DOM representation of the XML file
            dom = db.parse(file.getAbsoluteFile().toString());
            System.out.println(file.getAbsoluteFile().toString() + " is loading");

        }
        catch (ParserConfigurationException pce)
        {
            pce.printStackTrace();
        }
        catch (SAXException se)
        {
            se.printStackTrace();
        }
        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    private void addChildElements2ChildNodes(String treeType, Element pe, DefaultMutableTreeNode pNode)
    {
        NodeList nl = pe.getChildNodes();

        if ((nl != null) && (nl.getLength() > 0))
        {
            for (int i = 0; i < nl.getLength(); i++)
            {
                Node e = nl.item(i);
                if (e instanceof Element)
                {
                    String type = ((Element) e).getAttribute("Type");
                    String value = ((Element) e).getAttribute("Value");
                    // System.err.println(type + ";"+ value);
                    DefaultMutableTreeNode node = null;
                    if (treeType.equals("Target"))
                    {
                        node = new DefaultMutableTreeNode(new TargetInfo(type, value));
                    }
                    else if (treeType.equals("Model"))
                    {
                        node = new DefaultMutableTreeNode(new ModelInfo(type, value));
                    }
                    else
                    {
                        node = new DefaultMutableTreeNode(new GenericInfo(type, value));
                    }
                    pNode.add(node);
                    addChildElements2ChildNodes(treeType, (Element) e, node);
                }
            }
        }
    }

    public DefaultMutableTreeNode parseDocument(String treeType)
    {
        // get the root elememt
        Element docEle = dom.getDocumentElement();
        DefaultMutableTreeNode rootOfTree = null;

        // get a nodelist of <employee> elements
        NodeList nl = docEle.getElementsByTagName(treeType);
        nl = ((Element) nl.item(0)).getChildNodes();

        if ((nl != null) && (nl.getLength() > 0))
        {
            for (int i = 0; i < nl.getLength(); i++)
            {
                Node e = nl.item(i);
                if (e instanceof Element)
                {

                    String type = ((Element) e).getAttribute("Type");
                    String value = ((Element) e).getAttribute("Value");
                    // System.err.println(type + ";"+ value);

                    if (treeType.equals("Target"))
                    {
                        rootOfTree = new DefaultMutableTreeNode(new TargetInfo(type, value));
                    }
                    else if (treeType.equals("Model"))
                    {
                        rootOfTree = new DefaultMutableTreeNode(new ModelInfo(type, value));
                    }
                    else
                    {
                        rootOfTree = new DefaultMutableTreeNode(new GenericInfo(type, value));
                    }

                    addChildElements2ChildNodes(treeType, (Element) e, rootOfTree);

                }
            }
        }

        return rootOfTree;
    }

}
