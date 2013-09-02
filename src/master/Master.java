package master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Master {
	
	public static HashMap<Integer, Socket> workerToSocket=new HashMap<Integer,Socket>();
	public static HashMap<Integer,Integer> pidToWorker=new HashMap<Integer,Integer>();
	public static HashMap<Integer,String> pidToStatus=new HashMap<Integer,String>();
	public static HashMap<Integer,String> pidToCommand=new HashMap<Integer, String>();
	public static HashMap<Integer,MasterListener>workerToListner=new HashMap<Integer, MasterListener>();
	
	public static int serverPort;
	public static int workerNumber=0;
	

	
	public static void runMaster() throws IOException{
		
		MasterConsole masterThread=new MasterConsole();
		Thread master=new Thread(masterThread);
		master.start();
		
		int port=serverPort;
		ServerSocket servSocket= new ServerSocket(port);
		System.out.println("Server listening on port:"+servSocket.getLocalPort());
		while (true){
			Socket socket=servSocket.accept();
			System.out.println("Connection established to"+ socket.getInetAddress()+":"+socket.getPort());
			workerNumber++;
			workerToSocket.put(workerNumber, socket);
			MasterListener m=new MasterListener(socket, workerNumber);
			workerToListner.put(workerNumber, m);
			Thread listenerThread=new Thread(m);
			listenerThread.start();
		}
	}
}
