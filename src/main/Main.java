package main;
import java.io.IOException;

import worker.WorkerPrime;

import master.Master;

public class Main {

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        if(args.length==2){

            if(args[0].equals("-s")){
                System.out.println("-s detected / hostname:"+args[1]);
                System.out.println("I am a worker");
				/*start the worker node*/
                WorkerPrime worker = new WorkerPrime();
                worker.run(args[1]);
            }
            else
                System.out.println("Invalid Argument; use -s for slave mode");
        }
        else if(args.length>2 || args.length==1)
            System.out.println("Error: usage processmanager / processmanager -s  <hostname>");
        else{
            System.out.println("I am a master");
			/*Start off the master*/
            new Master().run();
        }

    }
}
