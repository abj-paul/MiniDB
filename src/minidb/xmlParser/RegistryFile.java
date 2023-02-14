package minidb.xmlParser;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import constants.constants;

public class RegistryFile extends XMLFiles {

    public RegistryFile(String path) {
        super(path);
    }
    void createFile() {
	this.insertBasicRegistryFileSyntax();
        this.updateFile();

        System.out.println("Intialized: " + xmlFile.getPath());
    }

    private void insertBasicRegistryFileSyntax() {
        Element rootElem = doc.createElement("root");
        Element emptyDb = this.addDbEntry("empty", "true");

        rootElem.appendChild(emptyDb);
        this.doc.appendChild(rootElem);

	this.createDefaultEmptyDatabaseFile();
    }

    private void createDefaultEmptyDatabaseFile() {
        new DatabaseFile(this.getDatabasePath("empty", true));
    }
    
    private Element addDbEntry(String name, String disabled) {
        Document doc = this.doc;

        Element databaseElem = doc.createElement("database");
        Element nameElem = doc.createElement("name");
        Element pathElem = doc.createElement("path");
        Element createTime = doc.createElement("createdTime");
        Element updateTime = doc.createElement("lastUpdateTime");

        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_DATE_TIME; // Superior ISO8601 Format
        LocalDateTime now = LocalDateTime.now();
        String timeNow = dtf.format(now);

        nameElem.appendChild(doc.createTextNode(name));
        pathElem.appendChild(doc.createTextNode(this.getDatabasePath(name, true)));
        createTime.appendChild(doc.createTextNode(timeNow));
        updateTime.appendChild(doc.createTextNode(timeNow));

        databaseElem.appendChild(nameElem);
        databaseElem.appendChild(pathElem);
        databaseElem.appendChild(createTime);
        databaseElem.appendChild(updateTime);
        databaseElem.setAttribute("disabled", disabled);

        return databaseElem;
    }

    public void createNewDatabase(String name) {
        try {
            if (!this.isDatabaseExists(name)) {
                Element dbEntry = addDbEntry(name, "false");
                this.doc.getDocumentElement().appendChild(dbEntry);
                this.updateFile();

                new DatabaseFile(this.getDatabasePath(name, true));
                System.out.println("Successfully created Database named: " + name);
            } else {
                System.out.println("Database already exists");
            }
	    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listAllDatabases() {
        NodeList list = this.doc.getElementsByTagName("name");

        for (int i = 0; i < list.getLength(); i++) {
            Node dbNode = list.item(i);
            String name = dbNode.getTextContent();
            System.out.println(i + ". " + name);
        }
    }
    
    public int checkDatabase(String name) {
        int x = -1;
        NodeList list = this.doc.getElementsByTagName("name");

        for (int i = 0; i < list.getLength(); i++) {
            Node dbNode = list.item(i);
            String dbName = dbNode.getTextContent();
            // System.out.println(dbName + " " + name);
            if (Objects.equals(dbName, name)) {
                x = i;
            }
        }

        return x;
    }

    private boolean isDatabaseExists(String name) {
        if (checkDatabase(name) != -1) {
            return true;
        } else
            return false;
    }

    public String getDatabasePath(String name, boolean create) {
        if (create) {
            return constants.DB_DIR_PATH + "\\" + name + ".xml";
        } else {
            if (isDatabaseExists(name)) {
                return constants.DB_DIR_PATH + "\\" + name + ".xml";
            } else {
                return null;
            }
        }
    }

    public void deleteDatabase(String name) {
        int dbid = checkDatabase(name);

        if (dbid != -1) {
            String dbPath = getDatabasePath(name, false);

            NodeList list = this.doc.getElementsByTagName("database");
            Element dbEntry = (Element) list.item(dbid);
            dbEntry.getParentNode().removeChild(dbEntry);
            this.updateFile();

            File f = new File(dbPath);
            f.delete();
            print("Database deleted");

        } else {
            print("Database does not exist");
        }
    }
}
