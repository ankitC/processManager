package worker;

import java.util.Map;
import migratableProcess.MigratableProcess;
import common.Status;

public class WorkerMonitor implements Runnable {

    private WorkerPrime worker;

    public WorkerMonitor(WorkerPrime worker) {
        this.worker = worker;
    }

	public void run(){
		while(true){
            for (Map.Entry<Integer, MigratableProcess> e : worker.getPidToMigratableProcess().entrySet()) {
                Integer pid = e.getKey();
                MigratableProcess mProc = e.getValue();
                if (mProc.isDone()) {
                    worker.getPidToMigratableProcess().remove(pid);
                    worker.getPidToThread().remove(pid);
                    worker.sendMessageToMaster(new WorkerMessage(Status.DONE, pid));
                    System.out.println("Finished process "+pid);
                } 
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

}
