package worker;

import java.util.Map;


import migratableProcess.MigratableProcess;

import common.Status;

public class WorkerMonitor implements Runnable {

    private Worker worker;

    public WorkerMonitor(Worker worker) {
        this.worker = worker;
    }

	public void run(){
		while(true){

			/*for(Map.Entry<Integer, Thread> e : worker.getPidToThread().entrySet()){
                Integer pid = e.getKey();
                Thread t = e.getValue();
				try {
					t.join(100);
					if(!t.isAlive()){
                        worker.getPidToMigratableProcess().remove(pid);
                        worker.getPidToThread().remove(pid);

                        worker.sendMessageToMaster(new WorkerMessage(Status.DONE, pid));
						System.out.println("Finished process "+pid);
					} else if (worker.getRunningPids().contains(pid)) {
                        worker.sendMessageToMaster(new WorkerMessage(Status.RUNNING, pid));
                    } else {
                        worker.sendMessageToMaster(new WorkerMessage(Status.SUSPENDED, pid));
                    }
				} catch (InterruptedException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}
			}*/
            for (Map.Entry<Integer, MigratableProcess> e : worker.getPidToMigratableProcess().entrySet()) {
                Integer pid = e.getKey();
                MigratableProcess mProc = e.getValue();
                if (mProc.isDone()) {
                    worker.getPidToMigratableProcess().remove(pid);
                    worker.getPidToThread().remove(pid);
                    worker.sendMessageToMaster(new WorkerMessage(Status.DONE, pid));
                    System.out.println("Finished process "+pid);
                } /*else if (mProc.isSuspended()) {
                    worker.sendMessageToMaster(new WorkerMessage(Status.SUSPENDED, pid));
                } else if (mProc.isRunning()) {
                    worker.sendMessageToMaster(new WorkerMessage(Status.RUNNING, pid));
                }*/
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
	}

}
