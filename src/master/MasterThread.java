package master;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import migratablePocess.MigratableProcess;

public class MasterThread implements Runnable {

	public void run() {
		BufferedReader console = new BufferedReader(new InputStreamReader(
				System.in));
		String input;
		String[] arguments;

		while (true) {
			System.out.print("-->");
			try {
				input = console.readLine();
				arguments = input.split(" ");
				if (arguments.length == 0)
					continue;

				if (arguments[0].equalsIgnoreCase("exit")) {
					System.out.println("Exiting System.");
					System.exit(0);
				}

				MigratableProcess task;

				Class<MigratableProcess> taskClass = (Class<MigratableProcess>) (Class
						.forName(arguments[0]));
				Constructor<MigratableProcess> taskConstructor = (Constructor<MigratableProcess>) (taskClass
						.getConstructor(String[].class));

				Object taskObject = new Object();
				taskObject = (Object[]) arguments;
				task = taskConstructor.newInstance(taskObject);
				task.run();
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
			}

		}

	}

}
