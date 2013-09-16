import migratableProcess.MigratableProcess;
import transactionalIO.TransactionalFileInputStream;
import transactionalIO.TransactionalFileOutputStream;

import java.io.IOException;

public class Decode extends MigratableProcess {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TransactionalFileInputStream inFile;
	public TransactionalFileOutputStream outFile;
	
	public Decode(String[] arguments){
        super(arguments);
        inFile=new TransactionalFileInputStream(arguments[1]);
		outFile= new TransactionalFileOutputStream(arguments[2]);
	}

	public void run() {

        running = true;
		suspended = false;
		while(!suspended){
			try {
				int a=inFile.read();
				int b=inFile.read();

				if(a==-1||b==-1) {
                    done = true;
                    break;
                }

				char first=(char)a;
				char second=(char)b;
				
				StringBuffer letter= new StringBuffer();
				letter.append(first);
				letter.append(second);
				
				int asciiLetter=Integer.parseInt(letter.toString(), 16) ;
				outFile.write(asciiLetter);
				
				Thread.sleep(300);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		suspended = false;
	}

	public void suspend() {

		suspended = true;
		while(suspended){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String toString() {
		System.out.println("Migratable Process: Decode   "+inFile.getFileName()+"     "+outFile.getFileName());
		return null;
	}


}
