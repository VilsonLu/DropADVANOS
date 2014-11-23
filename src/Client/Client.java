package Client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private Socket socket;
	
	//Sending Message
	private BufferedReader in;
	private PrintWriter out;
	
	//Sending files
	private DataOutputStream dos;
	private DataInputStream dis;
	
	private String folder;

	public Client(String folderPath) throws UnknownHostException, IOException {
		String address = "localhost";
		int portNumber = 4441;
		socket = new Socket(address, portNumber);
		this.folder = folderPath;
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		sendFiles();
		
		String input = "";
		while((input=in.readLine())!= null){
			System.out.println(input);
		}
		
		
		
	}
	
	public void sendFiles() throws IOException{
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
		for(File f: listOfFiles){
			System.out.println("Sending "+f.getName());
			dos.writeUTF(f.getName());
			FileInputStream fis = new FileInputStream(f);
			dos.writeLong(f.length());
			while((n=fis.read(buffer))!= -1){
				System.out.println("N:" + n);
				dos.write(buffer,0,n);
				dos.flush();
			}
			fis.close();
			dos.flush();
		}
		dos.close();
	
	}

}
