package Message;

import Migratable.MigratableProcess;

import java.io.Serializable;

/**
 * <p>Responsible for informing a master the {@link Status} of a particular {@link MigratableProcess}.
 * <p>Notice that this message is sent FROM a {@link Worker} TO a {@link Master}.</p>
 */
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
