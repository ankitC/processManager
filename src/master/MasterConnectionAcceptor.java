package master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import utils.Config;


public class MasterConnectionAcceptor implements Runnable {

	public static int serverPort=Config.serverPort;
	public static int workerNumber=0;

	public void run() {

		int port=serverPort;
		ServerSocket servSocket;
		try {
			servSocket = new ServerSocket(port);
			System.out.println("Server listening on port:"+servSocket.getLocalPort());
			while (true){
				Socket socket=servSocket.accept();
				System.out.println("Connection established to"+ socket.getInetAddress()+":"+socket.getPort());

				Master.workers.add(workerNumber);
				Master.workerToSocket.put(workerNumber, socket);

				MasterListener m=new MasterListener(socket, workerNumber);
				Master.workerToListner.put(workerNumber, m);
				Thread listenerThread=new Thread(m);
				System.out.println("Starting a new listener for the worker");
				listenerThread.start();
				workerNumber++;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
