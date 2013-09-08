package Master;

import Message.MasterMessage;
import Message.Status;
import Message.WorkerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * <p>Responsible for communicating with all of the workers of the
 * {@link Master} which it is assigned to.</p>
 *
 * <p>Can both send a {@link Message.Command} to a worker or listen for
 * a {@link Message.Status} from a worker.</p>
 */
public class MasterCommunicator implements Runnable {
	private Master master;
	private int worker;
    private Socket socket;

    private ObjectInputStream objInput;
    private ObjectOutputStream objOut;

	public MasterCommunicator(Master master, int worker) throws IOException {

        this.master = master;
        this.worker = worker;

        init();
	}

    private void init() throws IOException {
        System.out.println("Making a new Master.MasterCommunicator");
        socket = master.getWorkerToSocket().get(worker);
        System.out.println("Done making a new Master.MasterCommunicator");
    }

	public void run() {
		/*Monitor the inputStream for communication with the Worker.Worker*/
        try {
            WorkerMessage inMsg;

            while(true){
                if (objInput == null) {
                    objInput = new ObjectInputStream(socket.getInputStream());
                }
                Object incomingMsg = objInput.readObject();
                inMsg=(WorkerMessage)incomingMsg;

                if (inMsg.getStatus() != Status.DONE) {
                    master.getPidToStatus().put(inMsg.getPid(), inMsg.getStatus());
                } else {
                    master.getPidToStatus().remove(inMsg.getPid());
                    master.getPidToWorker().remove(inMsg.getPid());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
	}

	
	public void sendMessageToWorker(MasterMessage msg) {
		OutputStream outputStrm;
		ObjectOutputStream objectOutStrm;

		try {
            if (objOut == null) {
                objOut = new ObjectOutputStream(socket.getOutputStream());
            }
			objectOutStrm = objOut;
			objectOutStrm.writeObject(msg);
			objectOutStrm.flush();
            //objectOutStrm.reset();
			//outputStrm.close();
			//objectOutStrm.close();
		} catch (Exception e) {
            e.printStackTrace();
			System.err.println("Message.Command not sent");

		}

	}


}
