package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import migratableProcess.MigratableProcess;

public class DeserializeProcess {
	
	public static MigratableProcess deSerializeProcess(int pid){
		FileInputStream file;
		try {
			file = new FileInputStream(Config.serializeDirectory+Integer.toString(pid)+".ser");
	
		//	OutputStream outStrm=new OutputStream("/afs/ece/usr/achheda/ds/workspace/lab1/"+Integer.toString(taskID)+".ser");
		ObjectInputStream objectInStrm=new ObjectInputStream(file);
		Object o=objectInStrm.readObject();
		MigratableProcess m=(MigratableProcess)o;
		objectInStrm.close();
		file.close();
		return m;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
