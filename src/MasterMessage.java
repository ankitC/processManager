import java.io.Serializable;

public class MasterMessage implements Serializable {
	/**
	 * 
	 */
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
