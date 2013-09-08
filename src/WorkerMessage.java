import java.io.Serializable;

public class WorkerMessage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Status status;
    private int pid;

    public Status getStatus() {
        return status;
    }

    public int getPid() {
        return pid;
    }

    public WorkerMessage(Status status, int pid) {
        this.status = status;
        this.pid = pid;
    }
}
