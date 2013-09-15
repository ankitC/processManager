import migratableProcess.MigratableProcess;

public class PrintNums extends MigratableProcess{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private int a = 0;

    public PrintNums(String[] args) {
        super(args);
    }

    public void run() {

        running = true;
        suspended = false;
        while(!suspended){

            if (a < 13) {
                System.out.println(++a);
            } else {
                done = true;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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

	@Override
	public String toString() {
		System.out.println("Migratable Process-PrintNumbers");
		return null;
	}

}
