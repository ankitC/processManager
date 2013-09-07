package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import migratableProcess.MigratableProcess;
import utils.Message;

public class Worker {

	public static Socket workerSocket;
	public static ObjectInputStream objInput;
	public static ObjectOutputStream objOut;

	public static HashMap<Integer, MigratableProcess> pidToMigratableProcess=new HashMap<Integer, MigratableProcess>();
	public static HashMap<Integer, Thread>pidToThread=new HashMap<Integer, Thread>();
	
	
	public static ArrayList<Integer> runningProcess=new ArrayList<Integer>();
	public static ArrayList<Thread> processThread=new ArrayList<Thread>();


	public static void runWorker(String hostname) throws UnknownHostException, IOException{
		int port =8081;
		workerSocket=  new Socket(InetAddress.getByName(hostname), port);
	//	System.out.println("Connected to the master");

		objInput=new ObjectInputStream(workerSocket.getInputStream());
		objOut=new ObjectOutputStream(workerSocket.getOutputStream());
		
		
		
		Message inMsg;
		
		while(true){
			try {
				Object incomingMsg=objInput.readObject();
				inMsg=(Message)incomingMsg;
				System.out.println("Message Received: "+inMsg.command+ "  "+ inMsg.pid);
				
				if(inMsg.command.equalsIgnoreCase("start")){
					MigratableProcess mProc=utils.DeserializeProcess.deSerializeProcess(inMsg.pid);
					System.out.println("Deserializing process");
					pidToMigratableProcess.put(inMsg.pid, mProc);
					System.out.println("Starting Process"+inMsg.pid);
					runningProcess.add(inMsg.pid);
					//WorkerThread w=new WorkerThread(mProc, inMsg.pid);
					Thread t=new Thread(mProc);
					t.start();
					processThread.add(t);
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
		OutputStream outputStrm;
		ObjectOutputStream objectOutStrm;

		try {
			outputStrm     =  workerSocket.getOutputStream();
			objectOutStrm = new ObjectOutputStream(outputStrm);
			objectOutStrm.writeObject(msg);
			objectOutStrm.flush();
			outputStrm.close();
			objectOutStrm.close();
		} catch (Exception e) {
			System.err.println("Command not sent");
		}

	}
}

