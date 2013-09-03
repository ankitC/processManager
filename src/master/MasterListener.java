package master;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import utils.Message;

public class MasterListener implements Runnable {
	public Socket mSock;
	public int forWorker;

	public MasterListener(Socket mSock, int forWorker){
		this.mSock=mSock;
		this.forWorker=forWorker;
	}

	public void run() {
		/*Monitor the inputStream for communication with the Worker*/
	try {
		ObjectInputStream objInput=new ObjectInputStream(mSock.getInputStream());
		Message inMsg;
		while(true){
			Object incomingMsg=objInput.readObject();
			inMsg=(Message)incomingMsg;
			System.out.println("Message Received");
			if(inMsg.command.equalsIgnoreCase("done")){
				System.out.println("pid:"+inMsg.pid+" Done!");
				Master.pid.remove(inMsg.pid);
				Master.pidToCommand.remove(inMsg.pid);
				Master.pidToStatus.remove(inMsg.pid);
				Master.pidToWorker.remove(inMsg.pid);
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
		OutputStream outputStrm;
		ObjectOutputStream objectOutStrm;

		try {
			outputStrm     =  mSock.getOutputStream();
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
