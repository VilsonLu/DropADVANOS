package Server;

import java.io.File;
import java.sql.Timestamp;

public class FileWithTime {

	File file;
	Timestamp time;

	public FileWithTime(File file, Timestamp time) {
		super();
		this.file = file;
		this.time = time;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

}
