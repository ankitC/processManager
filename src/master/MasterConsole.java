package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import migratableProcess.MigratableProcess;
import utils.Message;
import utils.SerializeProcess;


public class MasterConsole implements Runnable {

	public static int taskID=0;

	public void run() {
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

				if(arguments[0].equalsIgnoreCase("cmd")){
					handleCommand(arguments);
					continue;
				}

				MigratableProcess task;

				Class<MigratableProcess> taskClass = (Class<MigratableProcess>) (Class
						.forName(arguments[0]));
				Constructor<MigratableProcess> taskConstructor = (Constructor<MigratableProcess>) (taskClass
						.getConstructor(String[].class));

				Object taskObject = new Object();
				taskObject = (Object[]) arguments;
				task = taskConstructor.newInstance(taskObject);
							
				Master.pidToCommand.put(taskID,input);

				/*Serialize the object*/
				SerializeProcess.serializeProcess(taskID, task);

				int hostWorker=taskID%(Master.workers.size());
				Message msg=new Message("start",taskID);

				Master.pidToWorker.put(taskID, hostWorker);
				Master.pidToStatus.put(taskID, "running");

				MasterListener m=Master.workerToListner.get(hostWorker);
				m.sendMessageToWorker(msg);
				taskID++;

			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.out.println("Requested class does not exist: Try 'Encode <infile><outfile>' or 'Decode <infile><outfile> ");
				e.printStackTrace();
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
				System.out.println("Exception Occured.");
			}

		}

	}

	private void handleCommand(String[] arguments){

		if(arguments[1].equalsIgnoreCase("ls")){
			if(arguments[2].equalsIgnoreCase("nodes")){
				System.out.println("Nodes");
				/*Print all available nodes*/
				System.out.println(Master.workers.toString());
				return;
			}
			if(arguments[2].equalsIgnoreCase("ps")){
				System.out.println("Current Processes");
				/*show processes*/
				System.out.println();
				return;
			}
		}

		if(arguments[1].equalsIgnoreCase("suspend")){
			int pid=Integer.valueOf(arguments[2]);
			if(Master.pid.contains(pid)){
				if(Master.pidToStatus.get(pid).equalsIgnoreCase("running")){
					Message m=new Message("suspend", pid);
					MasterListener listener=Master.workerToListner.get(Master.pidToWorker.get(pid));
					listener.sendMessageToWorker(m);
					Master.pidToStatus.put(pid, "suspended");
				}else{
					System.out.println("Process Already suspended on node:"+Master.pidToWorker.get(pid));
				}
			}else{
				System.out.println("Invalid ProcessID");
			}
			return;
		}

		if(arguments[1].equalsIgnoreCase("start")){
			int pid=Integer.valueOf(arguments[2]);
			if(Master.pid.contains(pid)){
				if(Master.pidToStatus.get(pid).equalsIgnoreCase("suspended")){
					Message m=new Message("start", pid);
					MasterListener listener=Master.workerToListner.get(Master.pidToWorker.get(pid));
					listener.sendMessageToWorker(m);
					Master.pidToStatus.put(pid, "running");
				}else{
					System.out.println("Process Already running on node:"+Master.pidToWorker.get(pid));
				}
			}else{
				System.out.println("Invalid ProcessID");
			}
			return;
		}

		if(arguments[1].equalsIgnoreCase("move")){
			return;
		}

	}

}
