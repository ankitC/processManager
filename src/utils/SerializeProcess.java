package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import migratableProcess.MigratableProcess;

public class SerializeProcess {
	
	public static void serializeProcess(int pid, MigratableProcess task){
		FileOutputStream file;
		try {
			file = new FileOutputStream(Config.serializeDirectory+Integer.toString(pid)+".ser");
	
		//	OutputStream outStrm=new OutputStream("/afs/ece/usr/achheda/ds/workspace/lab1/"+Integer.toString(taskID)+".ser");
		ObjectOutputStream objectOutStrm=new ObjectOutputStream(file);
		objectOutStrm.writeObject(task);
		objectOutStrm.flush();
		objectOutStrm.close();
		file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
