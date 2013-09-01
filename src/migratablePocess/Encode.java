package migratablePocess;

import java.io.IOException;
import IO.TransactionalInputStream;
import IO.TransactionalOutputStream;

public class Encode implements MigratableProcess{

	public TransactionalInputStream inFile;
	public TransactionalOutputStream outFile;
	public boolean suspending;

	public void run() {
		// TODO Auto-generated method stub
		char c;
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
