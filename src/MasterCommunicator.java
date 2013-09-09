import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

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
        System.out.println("Making a new MasterCommunicator");
        socket = master.getWorkerToSocket().get(worker);
        System.out.println("Done making a new MasterCommunicator");
    }

	public void run() {
		/*Monitor the inputStream for communication with the Worker*/
        try {
            WorkerMessage inMsg;

            while(true){
                if (objInput == null) {
                    objInput = new ObjectInputStream(socket.getInputStream());
                }
                Object incomingMsg = objInput.readObject();
                inMsg=(WorkerMessage)incomingMsg;
                //System.out.println("WorkerMessage Received");

                if (inMsg.getStatus() != Status.DONE) {
                    master.getPidToStatus().put(inMsg.getPid(), inMsg.getStatus());
                } else {
                    master.getPidToStatus().remove(inMsg.getPid());
                    //master.getPidToProcess().remove(inMsg.getPid());
                    master.getPidToWorker().remove(inMsg.getPid());
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
			System.err.println("Command not sent");

		}

	}


}
