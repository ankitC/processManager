import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Worker {
    private Socket workerSocket;
    private ObjectInputStream objInput;
    private ObjectOutputStream objOut;

    private Map<Integer, MigratableProcess> pidToMigratableProcess = new ConcurrentHashMap<Integer, MigratableProcess>();
    private Map<Integer, Thread> pidToThread = new ConcurrentHashMap<Integer, Thread>();

    public HashSet<Integer> getRunningPids() {
        return runningPids;
    }

    private HashSet<Integer> runningPids = new HashSet<Integer>();

    public void run(String hostname) throws IOException {

        WorkerMonitor monitor = new WorkerMonitor(this);
        new Thread(monitor).start();

        int port = Config.serverPort;
        workerSocket =  new Socket(InetAddress.getByName(hostname), port);
        System.out.println("Connected to the master");

        MasterMessage inMsg;

        while(true){
            try {
                if (objInput == null) {
                    objInput = new ObjectInputStream(workerSocket.getInputStream());
                }
                //objOut = new ObjectOutputStream(workerSocket.getOutputStream());
                //objOut=new ObjectOutputStream(workerSocket.getOutputStream());
                Object incomingMsg=objInput.readObject();
                //objInput.close();
                inMsg=(MasterMessage)incomingMsg;
                System.out.println("MasterMessage Received: "+inMsg.getCommand()+ "  "+ inMsg.getPid());

                int pid = inMsg.getPid();
                MigratableProcess mProc;

                switch (inMsg.getCommand()) {
                    case START:
                        mProc = Marshaller.deserialize(pid);
                        System.out.println("Deserializing process");
                        pidToMigratableProcess.put(pid, mProc);
                        runningPids.add(pid);
                        System.out.println("Starting Process " + pid);
                        Thread t=new Thread(mProc);
                        pidToThread.put(pid, t);
                        t.start();
                        break;
                    case SUSPEND:
                        mProc = pidToMigratableProcess.get(pid);
                        mProc.suspend();
                        runningPids.remove(pid);
                        System.out.println("Serializing process " + pid);
                        Marshaller.serialize(mProc);
                        //pidToMigratableProcess.remove(pid);
                        //pidToThread.remove(pid);
                        break;
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void sendMessageToMaster(WorkerMessage msg){
        OutputStream outputStrm;
        ObjectOutputStream objectOutStrm;

        try {
            if (objOut == null) {
                objOut = new ObjectOutputStream(workerSocket.getOutputStream());
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

    public Map<Integer, MigratableProcess> getPidToMigratableProcess() {
        return pidToMigratableProcess;
    }

    public Map<Integer, Thread> getPidToThread() {
        return pidToThread;
    }
}
