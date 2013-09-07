

import java.io.IOException;

import migratableProcess.MigratableProcess;

import IO.TransactionalFileInputStream;
import IO.TransactionalFileOutputStream;

public class Decode implements MigratableProcess {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TransactionalFileInputStream inFile;
	public TransactionalFileOutputStream outFile;
	public boolean suspending;
	
	public Decode(String[] arguments){
		inFile=new TransactionalFileInputStream(arguments[1]);
		outFile= new TransactionalFileOutputStream(arguments[2]);
	}

	public void run() {

		suspending=false;
		while(!suspending){
			try {
				int a=inFile.read();
				int b=inFile.read();

				if(a==-1||b==-1)
					break;
				char first=(char)a;
				char second=(char)b;
				
				StringBuffer letter= new StringBuffer();
				letter.append(first);
				letter.append(second);
				
				int asciiLetter=Integer.parseInt(letter.toString(), 16) ;
				outFile.write(asciiLetter);
				
				Thread.sleep(3000);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		suspending=false;
	}

	public void suspend() {
		suspending=true;
		while(suspending){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}
