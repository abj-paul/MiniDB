import java.util.Scanner;
import minidb.xmlParser.DatabaseFile;
import minidb.xmlParser.RegistryFile;
import constants.*;

/*
To do
- Comment the code
- Table Layout for the data.
- usage of threads
*/

/**
 * Javadoc comments. All are static methods because we are not creating any
 * object of this class.
 * 
 * @author Chanakya
 */
public class cli {

    /**
     * The Registry File is a XML file which contains the information about the all
     * the databases created. It acts as a pointer to the Database File. So, We
     * instantly load the registry file.
     */
    public static RegistryFile registry;

    /**
     * This attribute is for storing the DatabaseFile instance. Which is assigned
     * when the user calls the command "use". if the user does not call the command
     * "use" then the We show a error message.
     */
    public static DatabaseFile CurrentDb;

    public static void main(String[] args) {
        print(constants.HEADING);

        registry = new RegistryFile(constants.DATA_XML_PATH);
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print(constants.CMD_PREFIX);

            String currentCmd = input.nextLine();

            // break if user wants to exit
            if (currentCmd.equals("exit;")) {
                break;
            }
            long startTime = System.nanoTime();
            cliInputs(currentCmd);
            long endTime = System.nanoTime();

            long exeTime = (endTime - startTime) / 1000000;
            print("\nExecution Time: " + exeTime + "ms");
        }

        input.close();
    }

    private static void cliInputs(String input) {
        String[] cmdArgs = input.split(" ");

	String[] commands = {"new","use", "list", "help", "info", "schema", "add", "read", "drop", "update", "delete"};
	Command[] commandObjects = {new New(), new Use(), new List(), new Help(), new Info(), new Schema(), new Add(), new Read(), new Drop(), new Update(), new Delete()};  


	boolean defaultNeeded = true;
	for(int i=0; i<commands.length; i++){
	    if (cmdArgs[0].equals(commands[i])) {
		commandObjects[i].execute(cmdArgs);
		defaultNeeded = false;
		break;
	    }
	}

	if(defaultNeeded) new Default().execute(cmdArgs);
    }

    public static void print(String x) {
        System.out.println(x);
    }
}

// Commands that are available
// ✅ read
// ✅ read id=2
// read id=8..99

// ✅ add 04,cow,25

// ✅ delete id=2
// delete id=5..7

// ✅ schema id, name, age
// schema update id, name,age
// ✅ schema show

// The hardest one:
// update name='cow' where id=2

// Future:
// - import/export databases cmds

interface Command {
    void execute(String cmdArgs[]);
    default void print(String x) {
	System.out.println(x);
    }
}

class New implements Command {
    public void execute(String cmdArgs[]) {
	cli.registry.createNewDatabase(cmdArgs[1]);
    }
}

class Use implements Command {
    public void execute(String cmdArgs[]) {
	String path = cli.registry.getDatabasePath(cmdArgs[1], false);

                if (path != null) {
                    cli.CurrentDb = new DatabaseFile(path);
                    cli.CurrentDb.EditMode();
                    print("Successfully loaded Database named: " + cmdArgs[1]);
                } else {
                    print("Database not found");
                }
    }
}

class List implements Command {
    public void execute(String cmdArgs[]) {
	cli.registry.listAllDatabases();
    }
}

class Help implements Command {
    public void execute(String cmdArgs[]) {
	print(constants.HELP_COMMANDS);
    }
}

class Info implements Command {
    public void execute(String cmdArgs[]) {
	
    }
}

class Schema implements Command {
    public void execute(String cmdArgs[]) {
	if (cli.CurrentDb != null) {
	    String xy = cmdArgs[1];
	    
	    if (xy.equals("show")) {
		print(cli.CurrentDb.getSchema());
	    } else {
		String[] schemaVals = xy.split(",");
		if (schemaVals.length > 1) {
		    cli.CurrentDb.createSchema(xy);
		} else {
		    print("There should be atleast 2 columns of data");
		}
	    }
	    
	} else {
	    print(errors.NO_DATABASE_SELECTED);
	}
    }
}

class Add implements Command {
    public void execute(String cmdArgs[]) {
	if (cli.CurrentDb != null) {
	    cli.CurrentDb.addData(cmdArgs[1]);
	} else {
	    print(errors.NO_DATABASE_SELECTED);
	}
    }
}

class Read implements Command {
    public void execute(String cmdArgs[]) {
	if (cli.CurrentDb != null) {
	    if (cmdArgs.length == 1) {
		cli.CurrentDb.readData();
	    } else {
		cli.CurrentDb.readData(cmdArgs[1]);
	    }
	} else {
	    print(errors.NO_DATABASE_SELECTED);
	}
    }
}

class Drop implements Command {
    public void execute(String cmdArgs[]) {
	cli.registry.deleteDatabase(cmdArgs[1]);
    }
}


class Update implements Command {
    public void execute(String cmdArgs[]) {
	// TODO
	if (cli.CurrentDb != null) {
	}
    }
}

class Delete implements Command {
    public void execute(String cmdArgs[]) {
	if (cli.CurrentDb != null) {
	    cli.CurrentDb.deleteData(cmdArgs[1]);
	} else {
	    print(errors.NO_DATABASE_SELECTED);
	}
    }
}

class Default implements Command {
    public void execute(String cmdArgs[]) {
	print("UNKNOWN COMMAND: " + cmdArgs[0] + "\nType `help;` for commands list");
    }
}
