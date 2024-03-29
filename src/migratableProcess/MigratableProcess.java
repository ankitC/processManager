package migratableProcess;
import java.io.Serializable;

public abstract class MigratableProcess implements Runnable, Serializable {

	private static final long serialVersionUID = -4043477201906251170L;
	protected int pid;
    protected boolean running;
    protected boolean suspended;
    protected boolean done;

    public MigratableProcess(String[] args) {
        running = false;
        suspended = false;
        done = false;
    }
    
    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public abstract void run();

    public abstract void suspend();

    public abstract String toString();

}
