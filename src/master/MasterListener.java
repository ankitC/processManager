package master;

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
