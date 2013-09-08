package Migratable;

/**
 * Prints numbers from 1 to 13 to STOUT, one number per second.
 */
public class PrintNums extends MigratableProcess{

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
                e.printStackTrace();
            }
        }
        suspended = false;
    }

    public void suspend() {

        suspended=true;
        while(suspended){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
