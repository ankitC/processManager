package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import migratableProcess.MigratableProcess;
import utils.Message;

public class Master {

	public static HashMap<Integer, Socket> workerToSocket=new HashMap<Integer,Socket>();
	public static HashMap<Integer,Integer> pidToWorker=new HashMap<Integer,Integer>();
	public static HashMap<Integer,String> pidToStatus=new HashMap<Integer,String>();
	public static HashMap<Integer,String> pidToCommand=new HashMap<Integer, String>();
	public static HashMap<Integer,MasterListener>workerToListner=new HashMap<Integer, MasterListener>();

	/*List of current Process IDs and workers*/
	public static HashSet<Integer>runningPid=new HashSet<Integer>();
	public static HashSet<Integer> suspendedPid=new HashSet<Integer>();
	public static ArrayList<Integer> workers=new ArrayList<Integer>();

	public static int taskID=0;


	public static void runMaster() throws IOException{

		MasterConnectionAcceptor mca=new MasterConnectionAcceptor();
		/*Start off a thread that monitors incoming connections*/
		Thread connAccThread=new Thread(mca);
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
				if (arguments.length == 0)
					continue;
				System.out.println(arguments[0]);

				if (arguments[0].equalsIgnoreCase("exit")) {
					System.out.println("Exiting System.");
					System.exit(0);
				}

				/*Handles the command to list nodes or processes, start them and suspend them*/
				if(arguments[0].equalsIgnoreCase("cmd")){
					handleCommand(arguments);
					continue;
				}
				
				/*Starts off a Migratable Process*/
				MigratableProcess task;

				@SuppressWarnings("unchecked")
				Class<MigratableProcess> taskClass = (Class<MigratableProcess>) (Class
						.forName(arguments[0]));
				Constructor<MigratableProcess> taskConstructor = (Constructor<MigratableProcess>) (taskClass
						.getConstructor(String[].class));

				Object taskObject = new Object();
				taskObject = (Object[]) arguments;
				task = taskConstructor.newInstance(taskObject);

				Master.pidToCommand.put(taskID,input);

				/*Serialize the object*/
				utils.SerializeProcess.serializeProcess(taskID, task);
				Master.suspendedPid.add(taskID);
				Master.pidToStatus.put(taskID, "suspended");
				System.out.println("New task "+taskID+" added to the suspended list");
				taskID++;

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

	private static void handleCommand(String[] arguments){
		
		/*handle a list command*/
		if(arguments[1].equalsIgnoreCase("ls")){
			if(arguments[2].equalsIgnoreCase("nodes")){
				System.out.println("Nodes:");
				/*Print all available nodes*/
				System.out.println(Master.workers.toString());
				return;
			}
			if(arguments[2].equalsIgnoreCase("ps")){
				System.out.println("Current Processes");
				/*show processes*/
				System.out.println("Running processes:");
				System.out.println(Master.runningPid.toString());

				System.out.println();

				System.out.println("Suspended processes:");
				System.out.println(Master.suspendedPid.toString());
				return;
			}
		}
		
		/*Handle suspend command*/
		if(arguments[1].equalsIgnoreCase("suspend")){
			int pid=Integer.valueOf(arguments[2]);
			if(Master.runningPid.contains(pid)||Master.suspendedPid.contains(pid)){
				if(Master.pidToStatus.get(pid).equalsIgnoreCase("running")){

					Message m=new Message("suspend", pid);
					MasterListener listener=Master.workerToListner.get(Master.pidToWorker.get(pid));
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
			return;
		}
		
		/*handle a start command*/
		if(arguments[1].equalsIgnoreCase("start")){
			int pid=Integer.valueOf(arguments[2]);
			System.out.println("pid:"+pid);
			int worker=Integer.valueOf(arguments[3]);
			System.out.println("worker:"+worker);
			
			if(Master.runningPid.contains(pid)||Master.suspendedPid.contains(pid)){
				if(Master.pidToStatus.get(pid).equalsIgnoreCase("suspended")){
					if(Master.workers.contains(worker)){
						
						Message m=new Message("start", pid);
						MasterListener listener=Master.workerToListner.get(worker);
						System.out.println("Sending message to worker");
						System.out.println("Hello");
						System.out.println("listner Object=" + listener.toString());
						System.out.flush();
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
			return;
		}


	}



}


