package main;

public class processManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if(args.length==2){
			System.out.println("I am a slave");
			if(args[0].equals("-c")){
				System.out.println("-c detected");
			}
			else
				System.out.println("Invalid Argument; use -c for slave mode");
		}
		else if(args.length>2 || args.length==1)
			System.out.println("Error: usage processmanager / processmanager -c  <hostname>");
		else
			System.out.println("I am a master");

	}

}
