import java.io.IOException;

public class Encode extends MigratableProcess{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public TransactionalFileInputStream inFile;
	public TransactionalFileOutputStream outFile;
	
	public Encode(String[] arguments){
        super(arguments);
        inFile=new TransactionalFileInputStream(arguments[1]);
		outFile= new TransactionalFileOutputStream(arguments[2]);
	}

	public void run() {

        running = true;
        suspended = false;
		while(!suspended){

			int a;
			try {
				a = inFile.read();

				if(a==-1) {
                    done = true;
                    break;
                }

				
				String conv = Integer.toHexString(a);
				byte[] b;
				b = conv.getBytes("US-ASCII");

				for(int i=0; i<b.length;i++)
					outFile.write((int)b[i]);
				
				Thread.sleep(3000);


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
		// TODO Auto-generated method stub
		suspended=true;
		while(suspended){
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
