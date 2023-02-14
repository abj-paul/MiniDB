package minidb.xmlParser;

import javax.xml.transform.*;

public class TransformerSingleton {
    private static Transformer transformer = null;

    private TransformerSingleton(){} 

    private static void createTransformer() throws TransformerException{
	transformer = TransformerFactory.newInstance().newTransformer();
	transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	transformer.setOutputProperty(OutputKeys.INDENT, "no");
	transformer.setOutputProperty(OutputKeys.METHOD, "xml");
    }

    public static Transformer getTransformer() throws TransformerException{
	if(transformer==null)
	    createTransformer();
	return transformer;
    }
}
