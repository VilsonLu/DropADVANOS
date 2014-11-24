package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client {
	private Socket socket;

	// Sending Message
	private BufferedReader in;
	private PrintWriter out;

	// Sending files
	private DataOutputStream dos;
	private DataInputStream dis;

	private String folder;

	String folderPath;

	public Client(String folderPath) throws UnknownHostException, IOException {
		this.folderPath = folderPath;
		String address = "localhost";
		int portNumber = 4441;
		socket = new Socket(address, portNumber);
		this.folder = folderPath;
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		sendFiles();

		receiveFiles();

	}

	private List<File> receiveFiles() throws IOException {
		List<File> listOfFiles = new ArrayList<>();
		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());

		int numFiles = dis.readInt();
		System.out.println("Number of Files to be received: " + numFiles);
		int n = 0;
		byte[] buffer = new byte[1024];
		System.out.println("Receiving files...");
		for (int i = 0; i < numFiles; i++) {
			String filename = dis.readUTF();
			System.out.println("Receiving: " + filename);
			File f = new File(folderPath + "/" + filename);
			FileOutputStream fos = new FileOutputStream(f);
			long filesize = dis.readLong();
			while (filesize > 0
					&& (n = dis.read(buffer, 0,
							(int) Math.min(filesize, buffer.length))) != -1) {
				fos.write(buffer, 0, n);
				fos.flush();
				filesize -= n;
			}
			listOfFiles.add(f);
			fos.close();
		}

		return listOfFiles;

	}

	public void sendFiles() throws IOException {
		System.out.println("Checking files...");
		System.out.println("Directory: " + this.folder);
		File folder = new File(this.folder);
		File[] listOfFiles = folder.listFiles();

		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());
		dos.writeInt(listOfFiles.length);
		dos.flush();

		byte[] buffer = new byte[1024];
		int n = 0;
		for (File f : listOfFiles) {
			System.out.println("Sending " + f.getName());
			dos.writeUTF(f.getName() + "---" + f.lastModified());
			FileInputStream fis = new FileInputStream(f);
			dos.writeLong(f.length());
			while ((n = fis.read(buffer)) != -1) {
				System.out.println("N:" + n);
				dos.write(buffer, 0, n);
				dos.flush();
			}
			fis.close();
			dos.flush();
		}
		// dos.close();

	}

}
