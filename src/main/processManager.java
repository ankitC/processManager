package main;

import java.io.IOException;
import java.net.UnknownHostException;

import master.*;
import worker.*;

public class processManager {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * 
	 * start off the main program as " <executableName>.jar"  for Master
	 * start off the main program as "<executableName>.jar -s <URL to master>" for worker
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		
		if(args.length==2){
		
			if(args[0].equals("-s")){
				System.out.println("-s detected / hostname:"+args[1]);
				System.out.println("I am a worker");
				/*start the worker node*/
				Worker.runWorker(args[1]);
			}
			else
				System.out.println("Invalid Argument; use -s for slave mode");
		}
		else if(args.length>2 || args.length==1)
			System.out.println("Error: usage processmanager / processmanager -s  <hostname>");
		else{
			System.out.println("I am a master");
			/*Start off the master*/
			Master.runMaster();
		}

	}

}
