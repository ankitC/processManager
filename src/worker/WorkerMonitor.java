package worker;

public class WorkerMonitor {

	public static void monitorWork(){
		while(true){

			for(int i=0; i<Worker.runningProcess.size();i++){
				int pid=Worker.runningProcess.get(i);
				Thread t=Worker.pidToThread.get(pid);
				try {
					t.join(100);
					if(!t.isAlive()){
						Worker.runningProcess.remove(pid);
						Worker.pidToMigratableProcess.remove(pid);
						Worker.pidToThread.remove(pid);
						Worker.processThread.remove(t);
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
