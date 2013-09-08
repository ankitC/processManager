package master;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utils.Message;

public class MasterListener implements Runnable {
	public Socket mSock=new Socket();
	public int forWorker;
	public  ObjectInputStream objIn;
	public ObjectOutputStream objOut;
	
	public MasterListener(Socket mSock, int forWorker){
		System.out.println("Making a new Master listener");
		this.mSock=mSock;
		this.forWorker=forWorker;
	}

	public void run() {
		/*Monitor the inputStream for communication with the Worker*/
		try {
			System.out.println("Started a new listener");
			objOut=new ObjectOutputStream(this.mSock.getOutputStream());
			objOut.flush();
			objIn=new ObjectInputStream(this.mSock.getInputStream());
			
			//System.out.println("Streams initialized");
			//System.out.flush();
			//System.out.println(objIn.toString()+"\t\t"+objOut.toString());
			Message inMsg;
			while(true){
				Object incomingMsg=objIn.readObject();
				inMsg=(Message)incomingMsg;
				System.out.println("Message Received");
				if(inMsg.command.equalsIgnoreCase("done")){

					if(Master.runningPid.contains(inMsg.pid)){
						System.out.println("Process "+inMsg.pid+" completed!");
						Master.runningPid.remove(inMsg.pid);
						Master.pidToWorker.remove(inMsg.pid);
					}

					if(Master.suspendedPid.contains(inMsg.pid)){
						System.out.println("Process "+inMsg.pid+" suspended!");
						Master.pidToWorker.remove(inMsg.pid);
					}

				}


			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessageToWorker(Message msg) {
		try {
			//objectOutStrm = new ObjectOutputStream(mSock.getOutputStream());
			
			System.out.println("Writing to objOut");
			objOut.writeObject(msg);
			objOut.flush();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Command not sent");

		}

	}


}
