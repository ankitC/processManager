package utils;

import java.io.Serializable;

public class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String command;
	public int pid;
	
	public Message(String command, int pid) {
		this.command = command;
		this.pid = pid;
	}
	
	
}
