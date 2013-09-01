package migratablePocess;

import java.io.FileWriter;
import java.io.IOException;

import IO.TransactionalInputStream;
import IO.TransactionalOutputStream;

public class Decode implements MigratableProcess {
	public TransactionalInputStream inFile;
	public TransactionalOutputStream outFile;
	public boolean suspending;
	
	public Decode(String[] arguments){
		inFile=new TransactionalInputStream(arguments[1]);
		outFile= new TransactionalOutputStream(arguments[2]);
	//	suspending=false;
	}

	public void run() {
		char c;
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		suspending=false;
	}

	public void suspend() {
		suspending=true;
		while(suspending);
	}


}
