package Server;

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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ClientHandlerThread extends Thread {

	private Socket socket;
	private int processID;

	private BufferedReader in;
	private PrintWriter out;

	// Sending files
	private DataOutputStream dos;
	private DataInputStream dis;

	public ClientHandlerThread(Socket socket, int processID) throws IOException {
		this.socket = socket;
		this.processID = processID;
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("Server: " + processID);
	}

	public void run() {
		try {
			List<File> clientFiles = receiveFiles();

			// List<File> filesToSendBack = syncFiles(clientFiles);
			// sendFiles(filesToSendBack);
			sendFiles(clientFiles);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public List<File> syncFiles(List<File> clientFiles) {

	// }

	public List<File> receiveFiles() throws IOException {
		List<File> listOfFiles = new ArrayList<>();
		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());

		int numFiles = dis.readInt();
		System.out.println("Number of Files to be received: " + numFiles);
		int n = 0;
		byte[] buffer = new byte[1024];
		System.out.println("Receiving files...");
		for (int i = 0; i < numFiles; i++) {
			String title = dis.readUTF();
			String[] tokens = title.split("---");
			String filename = tokens[0];
			String dateModified = tokens[1];
			Timestamp t = new Timestamp(Long.valueOf(dateModified));
			System.out.println("Receiving: " + filename + " " + t);
			File f = new File("./Server/" + filename);
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

	public void sendFiles(List<File> listOfFiles) throws IOException {

		// String folderName = "Server/";
		// System.out.println("Checking files...");
		// System.out.println("Directory: " + folderName);
		// File folder = new File(folderName);
		// File[] listOfFiles = folder.listFiles();

		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());
		dos.writeInt(listOfFiles.size());
		dos.flush();

		byte[] buffer = new byte[1024];
		int n = 0;
		for (File f : listOfFiles) {
			System.out.println("Sending " + f.getName());
			dos.writeUTF(f.getName());
			FileInputStream fis = new FileInputStream(f);
			dos.writeLong(f.length());
			while ((n = fis.read(buffer)) != -1) {
				// System.out.println("N:" + n);
				dos.write(buffer, 0, n);
				dos.flush();
			}
			fis.close();
			dos.flush();
		}
		// dos.close();

	}
}
