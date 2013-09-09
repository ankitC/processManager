package worker;

import java.util.Iterator;

public class WorkerMonitor {

	public static void monitorWork(){
		while(true){
			Iterator<Integer> runningProcs=Worker.runningProcess.iterator();
			while(runningProcs.hasNext()){
				int pid=runningProcs.next();
				Thread t=Worker.pidToThread.get(pid);
				try {
					t.join(100);
					if(!t.isAlive()){
						Worker.runningProcess.remove(pid);
						Worker.pidToMigratableProcess.remove(pid);
						Worker.pidToThread.remove(pid);
						//Worker.processThread.remove(t);
						System.out.println("Finished process "+pid);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
