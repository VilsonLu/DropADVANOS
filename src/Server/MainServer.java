package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainServer {
	private static List<Socket> listOfSockets = new ArrayList<>();

	public static void main(String[] args) throws IOException {

		// will load all files from Server folder and assign each a monitor for
		// mutex.
		FileManager.initializeFileLocks();
		int portNumber = 4441;
		int processID = 1;
		ServerSocket serverSocket = new ServerSocket(portNumber);
		while (true) {
			// insert lock here
			Socket socket = serverSocket.accept();
			listOfSockets.add(socket);
			new ClientHandlerThread(socket, processID).start();
			processID++;
		}
	}
}
