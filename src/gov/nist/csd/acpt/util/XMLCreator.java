package gov.nist.csd.acpt.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

//For jdk1.5 with built in xerces parser
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import gov.nist.csd.acpt.generic.GenericInfo;
import gov.nist.csd.acpt.model.ModelInfo;
import gov.nist.csd.acpt.target.TargetInfo;

//For JDK 1.3 or JDK 1.4  with xerces 2.7.1
//import org.apache.xml.serialize.XMLSerializer;
//import org.apache.xml.serialize.OutputFormat;

public class XMLCreator
{

    // No generics
    List            myData;
    Document        dom;
    private Element rootEle = null;

    public XMLCreator()
    {
        // create a list to hold the data
        myData = new ArrayList();

        // initialize the list
        // loadData();

        // Get a DOM object
        createDocument();
    }

    public void createRootEle()
    {

        // create the root element <Books>
        rootEle = dom.createElement("ACPT");
        dom.appendChild(rootEle);

    }

    public void runExample(JTree jtree, String TreeType)
    {
        System.out.println("Started .. ");

        DefaultTreeModel targetTreeModel = (DefaultTreeModel) jtree.getModel();
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) targetTreeModel.getRoot();

        Element childEle = dom.createElement(TreeType);
        rootEle.appendChild(childEle);

        createDOMTree(childEle, rootNode);
        // printToFile();
        // System.out.println("Generated file successfully.");
    }

    /**
     * Add a list of books to the list In a production system you might populate
     * the list from a DB
     */
    // private void loadData(){
    // myData.add(new Book("Head First Java", "Kathy Sierra .. etc","Java
    // 1.5"));
    // myData.add(new Book("Head First Design Patterns", "Kathy Sierra ..
    // etc","Java Architect"));
    // }

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
            dom = db.newDocument();

        }
        catch (ParserConfigurationException pce)
        {
            // dump it
            System.out.println("Error while trying to instantiate DocumentBuilder " + pce);
            System.exit(1);
        }

    }

    /**
     * The real workhorse which creates the XML structure
     */
    private void createDOMTree(Element pEle, DefaultMutableTreeNode Node)
    {

        // Element bookEle = createBookElement(b);

        String elementType = "";
        String elementValue = "";

        if (Node.getUserObject() instanceof TargetInfo)
        {
            TargetInfo nodeInfo = (TargetInfo) Node.getUserObject();

            elementType = nodeInfo.getTargetType();
            // if (nodeInfo.getValueType() != null){
            // elementValue = nodeInfo.getValueType()+";"+nodeInfo.getValue();
            // }else{
            // elementValue = nodeInfo.getValue();
            // }
            elementValue = nodeInfo.getValue();
        }
        else if (Node.getUserObject() instanceof ModelInfo)
        {
            ModelInfo nodeInfo = (ModelInfo) Node.getUserObject();
            elementType = nodeInfo.getModelType();
            elementValue = nodeInfo.getValue();
        }
        else if (Node.getUserObject() instanceof GenericInfo)
        {
            GenericInfo nodeInfo = (GenericInfo) Node.getUserObject();
            elementType = nodeInfo.getType();
            elementValue = nodeInfo.getValue();
        }

        // System.err.println(elementType);
        // System.err.println(elementValue);
        //
        // Element childEle = dom.createElement("Node");
        Element childEle = dom.createElement(elementType.replaceAll(" ", "_"));

        childEle.setAttribute("Type", elementType);
        childEle.setAttribute("Value", elementValue);
        pEle.appendChild(childEle);

        Enumeration enumeration = Node.children();

        while (enumeration.hasMoreElements())
        {
            // get the node
            DefaultMutableTreeNode ChosenChildNode = (DefaultMutableTreeNode) enumeration.nextElement();
            createDOMTree(childEle, ChosenChildNode);
            // TargetInfo target = (TargetInfo) node.getUserObject();
        }

        /*
         * //No enhanced for Iterator it = myData.iterator();
         * while(it.hasNext()) { Book b = (Book)it.next(); //For each Book
         * object create <Book> element and attach it to root Element bookEle =
         * createBookElement(b); rootEle.appendChild(bookEle); }
         */

    }

    /**
     * Helper method which creates a XML element <Book>
     *
     * @param b
     *            The book for which we need to create an xml representation
     * @return XML element snippet representing a book
     */
    // private Element createBookElement(Book b){
    //
    // Element bookEle = dom.createElement("Book");
    // bookEle.setAttribute("Subject", b.getSubject());
    //
    // //create author element and author text node and attach it to bookElement
    // Element authEle = dom.createElement("Author");
    // Text authText = dom.createTextNode(b.getAuthor());
    // authEle.appendChild(authText);
    // bookEle.appendChild(authEle);
    //
    // //create title element and title text node and attach it to bookElement
    // Element titleEle = dom.createElement("Title");
    // Text titleText = dom.createTextNode(b.getTitle());
    // titleEle.appendChild(titleText);
    // bookEle.appendChild(titleEle);
    //
    // return bookEle;
    //
    // }

    /**
     * This method uses Xerces specific classes prints the XML document to file.
     */
    public void printToFile(File savedFile)
    {

        try
        {
            // print
            OutputFormat format = new OutputFormat(dom);
            format.setIndenting(true);

            // System.err.println("^^"+format.toString());
            // to generate output to console use this serializer
            // XMLSerializer serializer = new XMLSerializer(System.out, format);

            // to generate a file output use fileoutputstream instead of
            // system.out
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(savedFile), format);

            // System.err.println("^^"+serializer.toString());
            serializer.serialize(dom);

        }
        catch (IOException ie)
        {
            ie.printStackTrace();
        }
        System.out.println("Generated file successfully.");
    }

    public static void main(String args[])
    {

        // //create an instance
        // XMLCreator xce = new XMLCreator();
        //
        // //run the example
        // xce.runExample();
    }
}
