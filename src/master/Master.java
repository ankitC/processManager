package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import worker.WorkerMessage;

import migratableProcess.MigratableProcess;

import common.Command;
import common.Config;
import common.Marshaller;
import common.Status;

public class Master implements Runnable {

    //private HashMap<Integer, MigratableProcess> pidToProcess;

    private Map<Integer, Status> pidToStatus = new ConcurrentHashMap<Integer, Status>();
    private Map<Integer, Integer> pidToWorker = new ConcurrentHashMap<Integer,Integer>();
    private Map<Integer, Socket> workerToSocket = new ConcurrentHashMap<Integer,Socket>();
    private Map<Integer, MasterCommunicator> workerToCommunicator = new ConcurrentHashMap<Integer, MasterCommunicator>();
	private int pid =0;

    private int port = Config.serverPort;

	public void run() {

		MasterConnectionHandler mch = new MasterConnectionHandler(this);
		/*Start off a thread that monitors incoming connections*/
		Thread connAccThread=new Thread(mch);
		connAccThread.start();
		System.out.println("Now accepting connections");

		/*Starts off the console*/
		System.out.println("Initializing Console");

		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String input;
		String[] arguments;

		while (true) {
			System.out.print("Master-->");
			try {
				input = console.readLine();
				arguments = input.split(" ");
				
				//System.out.println(arguments[0]);

				if (arguments[0].equalsIgnoreCase("exit")) {
					System.out.println("Closing all workers.");
					
					MasterCommunicator communicator;
					MasterMessage exitMessage=new MasterMessage(Command.EXIT, 0);
					Set<Integer> workers= workerToCommunicator.keySet();
					Iterator<Integer> workerIterator=workers.iterator();
					while(workerIterator.hasNext()){
						communicator=workerToCommunicator.get(workerIterator.next());
						communicator.sendMessageToWorker(exitMessage);
					}
					System.out.println("Exiting System.");
					System.exit(0);
				}

				/*Handles the command to list nodes or processes, start them and suspend them*/
				if(arguments[0].equalsIgnoreCase("cmd")){
					handleCommand(arguments);
					continue;
				}
				
				if(arguments[0].equalsIgnoreCase("")){
					continue;
					
				}
				
				if(arguments[0].equalsIgnoreCase("help")){
					System.out.println("Commands:");
					System.out.println("cmd ls nodes");
					System.out.println("cmd ls ps");
					System.out.println("");
					System.out.println("Processes:");
					System.out.println("Encode <infile> <outfile>");
					System.out.println("Decode  <infile> <outfile>");
					continue;
				}

				/*Starts off a Migratable Process*/
				MigratableProcess task;

				Class<MigratableProcess> processClass =
                        (Class<MigratableProcess>) (Class.forName(arguments[0]));
				Constructor<MigratableProcess> processConstructor =
                        (Constructor<MigratableProcess>) (processClass.getConstructor(String[].class));

				//Object taskObject = new Object();
				//taskObject = (Object[]) arguments;
				task = processConstructor.newInstance((Object) arguments);
                task.setPid(pid);

				/*Serialize the object*/
				Marshaller.serialize(task);

                pidToStatus.put(pid, Status.SUSPENDED);

				pid++;

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("Requested class does not exist: Try 'Encode <infile> <outfile>' or 'Decode <infile> <outfile> ");
			//	e.printStackTrace();
			} catch (NoSuchMethodException e) {
				System.out.println("No Constructor written! Write it!!!");
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				System.out.println("Could not instantiate an object of the requested class.");
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}catch (Exception e){
				e.printStackTrace();
				System.out.println("Exception Occured.");
			}

		}
	}

	public void handleCommand(String[] arguments){

		/*handle a list command*/
		if(arguments[1].equalsIgnoreCase("ls")){
			if(arguments[2].equalsIgnoreCase("nodes")){
				System.out.println("Nodes:");
				/*Print all available nodes*/
				System.out.println(workerToSocket.keySet().toString());
				return;
			}
			if(arguments[2].equalsIgnoreCase("ps")){
				System.out.println("Current Processes");
				/*show processes*/
				System.out.println("Running processes:");

                for (Map.Entry<Integer, Status> e : pidToStatus.entrySet()) {
                    if (e.getValue().equals(Status.RUNNING)) {
                        System.out.println(e.getKey());
                    }
                }

				System.out.println();

				System.out.println("Suspended processes:");

                for (Map.Entry<Integer, Status> e : pidToStatus.entrySet()) {
                    if (e.getValue().equals(Status.SUSPENDED)) {
                        System.out.println(e.getKey());
                    }
                }
			}
		}

		/*Handle suspend command*/
		if(arguments[1].equalsIgnoreCase("suspend")){
			int pid=Integer.valueOf(arguments[2]);
			/*if(Master.runningPid.contains(pid)||Master.suspendedPid.contains(pid)){
				if(Master.pidToStatus.get(pid).equalsIgnoreCase("running")){

					MasterMessage m=new MasterMessage("suspend", pid);
					MasterCommunicator listener=Master.workerToListner.get(Master.pidToWorker.get(pid));
					listener.sendMessageToWorker(m);


					Master.suspendedPid.add(pid);
					Master.runningPid.remove(pid);
					Master.pidToStatus.put(pid, "suspended");
					Master.pidToWorker.remove(pid);

				}else{
					System.out.println("Process Already suspended");
				}
			}else{
				System.out.println("Invalid ProcessID");
			}
			return;*/
            if (pidToStatus.get(pid) != null) {
                if (pidToStatus.get(pid).equals(Status.RUNNING)) {
                    MasterMessage m =new MasterMessage(Command.SUSPEND, pid);
                    MasterCommunicator listener= workerToCommunicator.get(pidToWorker.get(pid));
                    listener.sendMessageToWorker(m);

                    pidToStatus.put(pid, Status.SUSPENDED);
                    pidToWorker.remove(pid);
                } else {
                    System.out.println("That process is not currently running");
                }
            } else {
                System.out.println("Invalid process ID");
            }
		}

		/*handle a start command*/
		if(arguments[1].equalsIgnoreCase("start")){
			int pid=Integer.valueOf(arguments[2]);
			System.out.println("pid:"+pid);
			int worker=Integer.valueOf(arguments[3]);
			System.out.println("worker:"+worker);

            if (pidToStatus.get(pid) != null) {
                if (pidToStatus.get(pid).equals(Status.SUSPENDED)) {
                    if(workerToCommunicator.get(worker) != null){

                        MasterMessage m=new MasterMessage(Command.START, pid);
                        MasterCommunicator listener = workerToCommunicator.get(worker);
                        listener.sendMessageToWorker(m);

                        pidToStatus.put(pid, Status.RUNNING);
                        pidToWorker.put(pid, worker);
                    }else{
                        System.out.println("Specified worker does not exist");
                    }
                } else {
                    System.out.println("That process is already running");
                }
            } else {
                System.out.println("Invalid process ID");
            }

			/*if(Master.runningPid.contains(pid)||Master.suspendedPid.contains(pid)){
				if(Master.pidToStatus.get(pid).equalsIgnoreCase("suspended")){
					if(Master.workers.contains(worker)){

						MasterMessage m=new MasterMessage("start", pid);
						MasterCommunicator listener=Master.workerToListner.get(worker);
						listener.sendMessageToWorker(m);

						Master.suspendedPid.remove(pid);
						Master.runningPid.add(pid);
						Master.pidToStatus.put(pid, "running");
						Master.pidToWorker.put(pid, worker);
					}else{
						System.out.println("Specified worker does not exist");
					}
				}else{
					System.out.println("Process Already running on node:"+Master.pidToWorker.get(pid));
				}
			}else{
				System.out.println("Invalid ProcessID");
			}
			return;*/
		}


	}

    /*public HashMap<Integer, MigratableProcess> getPidToProcess() {
        return pidToProcess;
    }*/

    public Map<Integer, Status> getPidToStatus() {
        return pidToStatus;
    }

    public Map<Integer, Integer> getPidToWorker() {
        return pidToWorker;
    }

    public Map<Integer, Socket> getWorkerToSocket() {
        return workerToSocket;
    }

    public Map<Integer, MasterCommunicator> getWorkerToCommunicator() {
        return workerToCommunicator;
    }

    public int getPort() {
        return port;
    }
}


