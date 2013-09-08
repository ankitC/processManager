package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import migratableProcess.MigratableProcess;
import utils.Message;

public class Worker {

	public static Socket workerSocket;
	public static ObjectInputStream objIn;
	public static ObjectOutputStream objOut;

	public static HashMap<Integer, MigratableProcess> pidToMigratableProcess=new HashMap<Integer, MigratableProcess>();
	public static HashMap<Integer, Thread>pidToThread=new HashMap<Integer, Thread>();


	public static ArrayList<Integer> runningProcess=new ArrayList<Integer>();
	public static ArrayList<Thread> processThread=new ArrayList<Thread>();
	
	private static void workerInit(String hostname){
		int port =8081;
		try {
			workerSocket=  new Socket(InetAddress.getByName(hostname), port);
			objOut=new ObjectOutputStream(workerSocket.getOutputStream());
			objOut.flush();
			objIn=new ObjectInputStream(workerSocket.getInputStream());
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public static void runWorker(String hostname) throws UnknownHostException, IOException{
		
		workerInit(hostname);
		Message inMsg;
		while(true){

			try {
				System.out.println("Awaiting object");
				Object incomingMsg=objIn.readObject();
				inMsg=(Message)incomingMsg;
				System.out.println("Message Received: "+inMsg.command+ "  "+ inMsg.pid);

				if(inMsg.command.equalsIgnoreCase("start")){
					MigratableProcess mProc=utils.DeserializeProcess.deSerializeProcess(inMsg.pid);
					System.out.println("Deserializing process");
					pidToMigratableProcess.put(inMsg.pid, mProc);
					System.out.println("Starting Process"+inMsg.pid);
					runningProcess.add(inMsg.pid);
					Thread t=new Thread(mProc);
					t.start();
					processThread.add(t);
					System.out.println("Added process to the list");
				}

				if(inMsg.command.equalsIgnoreCase("suspend")){
					MigratableProcess mProc=pidToMigratableProcess.get(inMsg.pid);
					mProc.suspend();
					runningProcess.remove(inMsg.pid);
					System.out.println("Serializing process"+inMsg.pid);
					utils.SerializeProcess.serializeProcess(inMsg.pid, mProc);
					pidToMigratableProcess.remove(inMsg.pid);
				}


			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void sendMessageToMaster(Message msg){
		try {
			
			objOut.writeObject(msg);
			objOut.flush();
			//outputStrm.close();
			//	objectOutStrm.close();

		} catch (Exception e) {
			System.err.println("Command not sent");
		}

	}
}

