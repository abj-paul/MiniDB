package minidb.xmlParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Factory{} 

abstract class XMLFactory{
    public abstract XMLFiles createFile(String path);
}

class RegistryFileFactory extends XMLFactory {
    @Override
    public XMLFiles createFile(String path) {
	RegistryFile registryFile = new RegistryFile(path);
	return registryFile;
    }
}

	
class DatabaseFileFactory extends XMLFactory {
    @Override
    public XMLFiles createFile(String path) {
	DatabaseFile databaseFile = new DatabaseFile(path);
	return databaseFile;
    }
}
