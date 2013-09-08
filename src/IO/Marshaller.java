package IO;

import Migratable.MigratableProcess;
import Config.Config;

import java.io.*;

/**
 * Responsible for serializing and deserializing a {@link MigratableProcess}.
 */
public class Marshaller {

    public static void serialize(MigratableProcess task){
        FileOutputStream file;
        try {
            new File(Config.serializeDirectory+Integer.toString(task.getPid())+".ser").delete();
            file = new FileOutputStream(Config.serializeDirectory+Integer.toString(task.getPid())+".ser");
            ObjectOutputStream objectOutStrm = new ObjectOutputStream(file);
            objectOutStrm.writeObject(task);
            objectOutStrm.flush();
            objectOutStrm.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static MigratableProcess deserialize(int pid){
        FileInputStream file;
        try {
            file = new FileInputStream(Config.serializeDirectory+Integer.toString(pid)+".ser");
            ObjectInputStream objectInStrm=new ObjectInputStream(file);
            Object o=objectInStrm.readObject();
            MigratableProcess m=(MigratableProcess)o;
            objectInStrm.close();
            new File(Config.serializeDirectory+Integer.toString(pid)+".ser").delete();
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
