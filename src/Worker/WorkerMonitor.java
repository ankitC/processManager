package Worker;

import Message.Status;
import Message.WorkerMessage;
import Migratable.MigratableProcess;

import java.util.Map;

/**
 * Responsible for checking if a {@link Migratable.MigratableProcess} has been completed on
 * the {@link Worker} that this monitor is attached to.
 */
public class WorkerMonitor implements Runnable {

    private Worker worker;

    public WorkerMonitor(Worker worker) {
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
