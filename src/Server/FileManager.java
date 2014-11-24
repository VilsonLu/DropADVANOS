package Server;

import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;

public class FileManager {

	public static final String folderLocation = "Server/";

	private HashMap<String, Monitor> fileMap = new HashMap<>();

	// will load all files from Server folder and assign each a monitor for
	// mutex.
	public static void initializeFileLocks() {
		// scan for existing files in folder Server, instantiate one monitor for
		// each
		// add them to filemap
	}

	public static boolean clientHasALaterCopy(String filename, Timestamp t) {
		File serverFile = new File(folderLocation + filename);
		if (!serverFile.exists() || t.getTime() > serverFile.lastModified())
			return true;
		else
			return false;
	}

	public static synchronized void acquireLockOfFile(String filename) {
		// if filename in keys, get monitor and lock
		// else create new monitor, put to fileMap
		// acquirelock
	}

	public static synchronized void releaseLockOfFile(String name) {
		// get monitor of file from filemap
		// release lock
	}

}
