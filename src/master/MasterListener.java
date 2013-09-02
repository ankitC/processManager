package master;

import java.io.InputStream;
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

	}

	private void sendMessageToSlave(Message msg) {
		OutputStream outputStrm;
		ObjectOutputStream objectOutStrm;
		InputStream inputStrm; 
		ObjectInputStream objectInStrm;
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
