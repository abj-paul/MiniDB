package minidb.xmlParser;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import constants.constants;

public abstract class XMLFiles {
    protected File xmlFile;
    protected Document doc;

    public File getFile(){
	return this.xmlFile;
    }

    public void setFile(File newFile){
	this.xmlFile = newFile;
    }

    public Document getDocument(){
	return this.doc;
    }

    public void setDocument(Document doc){
	this.doc = doc;
    }

    public XMLFiles(String path) {
        try {
	    this.createDirectoryIfNotExists();
	    this.createAndLoadFile(path);
        } catch (ParserConfigurationException | SAXException | IOException err) {
            System.out.println(err);
            err.printStackTrace();
        }
    }

    private void createAndLoadFile(String path) throws ParserConfigurationException, SAXException, IOException{
	this.xmlFile = new File(path);
	boolean NoFileFound = xmlFile.createNewFile();
	loadFile(NoFileFound);
    }

    private void createDirectoryIfNotExists(){
	new File(constants.DB_DIR_PATH).mkdir(); // create `db` directory if it doesn't exist
    }

    private void loadFile(boolean NoFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        if (NoFile) {
            this.doc = docBuilder.newDocument();
            createFile(); // abstract method to create the file
        } else {
            this.doc = docBuilder.parse(xmlFile);
        }
    }

    abstract void createFile();

    private Transformer createTransformer() throws TransformerException{
	Transformer transformer = TransformerSingleton.getTransformer();
	transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	transformer.setOutputProperty(OutputKeys.INDENT, "no");
	transformer.setOutputProperty(OutputKeys.METHOD, "xml");

	return transformer;
    }

    private void transformDOMtoStream(Transformer transformer) throws TransformerException{
	DOMSource source = new DOMSource(this.doc);
	StreamResult result = new StreamResult(this.xmlFile);
	transformer.transform(source, result);
    }

    protected void updateFile() {
        try {
	    Transformer transformer = this.createTransformer();
	    this.transformDOMtoStream(transformer);
            print("Updated;");

        } catch (TransformerException err) {
            err.printStackTrace();
        }
    }

    protected void print(String x) {
        System.out.println(x);
    }
}
