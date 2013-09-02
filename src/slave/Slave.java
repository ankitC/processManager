package slave;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Slave {
	
	public static void runSlave(String hostname) throws UnknownHostException, IOException{
		int port =8081;
		Socket slaveSocket=  new Socket(InetAddress.getByName(hostname), port);
		System.out.println("Connected to the master");
		
	
	}

}
