package Server;

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
import java.util.ArrayList;
import java.util.List;

public class ClientHandlerThread extends Thread {
	
	private Socket socket;
	private int processID;
	
	private BufferedReader in;
	private PrintWriter out;
	
	//Sending files
	private DataOutputStream dos;
	private DataInputStream dis;
	
	public ClientHandlerThread(Socket socket, int processID) throws IOException{
		this.socket = socket;
		this.processID = processID;
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		System.out.println("Server: " + processID);
	}
	
	public void run(){
		out.println("Hello client: " + processID);
		try {
			receiveFiles();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sync(){
		
	}
	
	public List<File> receiveFiles() throws IOException{
		List<File> listOfFiles = new ArrayList<>();
		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());
		
		int numFiles = dis.readInt();
		System.out.println("Number of Files to be received: " + numFiles);
		int n = 0;
		byte[] buffer = new byte[1024];
		System.out.println("Receiving files...");
		for(int i=0; i<numFiles; i++){
			String filename = dis.readUTF();
			System.out.println("Receiving: "+filename);
			File f = new File("./Server/"+filename);
			FileOutputStream fos = new FileOutputStream(f);
			long filesize = dis.readLong();
			while(filesize > 0 &&(n = dis.read(buffer,0,(int)Math.min(filesize, buffer.length))) != -1){
                fos.write(buffer,0,n);
                fos.flush();
                filesize -= n;
            }
			listOfFiles.add(f);
            fos.close();
		}
		
		return listOfFiles;
		
	}
	
	

}
