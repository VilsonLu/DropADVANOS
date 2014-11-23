package Client;

import java.io.IOException;
import java.net.UnknownHostException;

public class ClientDriver {
	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
		String folderPath = args[0];
		Client client = new Client(args[0]);
	}
}
