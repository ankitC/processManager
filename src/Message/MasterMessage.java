package Message;

import java.io.Serializable;

/**
 * <p>Responsible for giving a worker the command it should execute
 * for a particular process.</p>
 * <p>Notice that this message is sent FROM a {@link Master} TO a {@link Worker}.</p>
 */
public class MasterMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Command command;
	private int pid;

    public Command getCommand() {
        return command;
    }

    public int getPid() {
        return pid;
    }
	
	public MasterMessage(Command command, int pid) {
		this.command = command;
		this.pid = pid;
	}
}
