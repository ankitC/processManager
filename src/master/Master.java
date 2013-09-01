package master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Master {

	public static void runMaster() throws IOException{
		
		MasterThread masterThread=new MasterThread();
		Thread master=new Thread(masterThread);
		master.run();
		
		int port=8081;
		ServerSocket servSocket= new ServerSocket(port);
		System.out.println("Server listening on port:"+servSocket.getLocalPort());
		while (true){
			Socket socket=servSocket.accept();
			System.out.println("Connection established to"+ socket.getInetAddress()+":"+socket.getPort());
		}
		
	}

}
