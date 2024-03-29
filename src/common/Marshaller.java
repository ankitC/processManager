package common;

import java.io.*;

import migratableProcess.MigratableProcess;


public class Marshaller {

    public static void serialize(MigratableProcess task){
    	task.toString();
        FileOutputStream file;
        try {
        	System.out.println("Serializing Process "+task.getPid());
            new File(Config.serializeDirectory+Integer.toString(task.getPid())+".ser").delete();
            file = new FileOutputStream(Config.serializeDirectory+Integer.toString(task.getPid())+".ser");
            ObjectOutputStream objectOutStrm = new ObjectOutputStream(file);
            objectOutStrm.writeObject(task);
            objectOutStrm.flush();
            objectOutStrm.close();
            System.out.println("Done");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static MigratableProcess deserialize(int pid){
        FileInputStream file;
        try {
        	System.out.println("DeSerializing Process "+ pid);
            file = new FileInputStream(Config.serializeDirectory+Integer.toString(pid)+".ser");
            ObjectInputStream objectInStrm=new ObjectInputStream(file);
            Object o=objectInStrm.readObject();
            MigratableProcess m=(MigratableProcess)o;
            objectInStrm.close();
            new File(Config.serializeDirectory+Integer.toString(pid)+".ser").delete();
            m.toString();
            System.out.println("Done");
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
