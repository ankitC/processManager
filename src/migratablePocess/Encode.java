package migratablePocess;

import java.io.IOException;

import IO.TransactionalFileInputStream;
import IO.TransactionalFileOutputStream;

public class Encode implements MigratableProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TransactionalFileInputStream inFile;
	public TransactionalFileOutputStream outFile;
	public boolean suspending;
	
	public Encode(String[] arguments){
		inFile=new TransactionalFileInputStream(arguments[1]);
		outFile= new TransactionalFileOutputStream(arguments[2]);
	}

	public void run() {

		suspending=false;
		while(!suspending){

			int a;
			try {
				a = inFile.read();

				if(a==-1)
					break;
				
				String conv=new String(Integer.toHexString(a));
				byte[] b;
				b = conv.getBytes("US-ASCII");

				for(int i=0; i<b.length;i++)
					outFile.write((int)b[i]);


			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		suspending=false;
	}

	public void suspend() {
		// TODO Auto-generated method stub
		suspending=true;
		while(suspending);
	}

}
