package master;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;


public class MasterConnectionHandler implements Runnable {

    private Master master;
    private Map<Integer, Socket> workers;

    private int workerID = 0;

    public MasterConnectionHandler(Master master) {
        System.out.println("Making a new MasterConnectionHandler");
        this.master = master;
        workers = master.getWorkerToSocket();
    }

    public Master getMaster() {
        return master;
    }

    public void run() {
        ServerSocket servSocket;
        try {
            servSocket = new ServerSocket(master.getPort());
            System.out.println("Server listening on port: "+servSocket.getLocalPort());
            while (true){
                Socket socket=servSocket.accept();
                System.out.println("Connection established to "+ socket.getInetAddress()+":"+socket.getPort());

                workers.put(workerID, socket);

                MasterCommunicator m = new MasterCommunicator(master, workerID);
                master.getWorkerToCommunicator().put(workerID, m);
                Thread listenerThread=new Thread(m);
                System.out.println("Starting a new listener for the worker");
                listenerThread.start();
                workerID++;

            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
