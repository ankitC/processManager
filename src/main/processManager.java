package main;

import java.io.IOException;
import java.net.UnknownHostException;

import master.*;
import slave.*;

public class processManager {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		
		if(args.length==2){
		
			if(args[0].equals("-c")){
				System.out.println("-c detected / hostname:"+args[1]);
				System.out.println("I am a slave");
				Slave.runSlave(args[1]);
			}
			else
				System.out.println("Invalid Argument; use -c for slave mode");
		}
		else if(args.length>2 || args.length==1)
			System.out.println("Error: usage processmanager / processmanager -c  <hostname>");
		else{
			System.out.println("I am a master");
			Master.runMaster();
		}

	}

}
