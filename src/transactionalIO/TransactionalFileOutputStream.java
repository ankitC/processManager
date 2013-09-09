package transactionalIO;
import java.io.*;

public class TransactionalFileOutputStream extends OutputStream implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private int pos;

	public TransactionalFileOutputStream(String fileName) {
		super();
		this.fileName = fileName;
		this.pos=0;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	@Override
	public String toString() {
		return "TransactionalOutputStream [fileName=" + fileName + ", pos="
				+ pos + "]";
	}

	private RandomAccessFile openFile() throws IOException {
		RandomAccessFile fs = new RandomAccessFile(fileName, "rws");
        fs.seek(pos);
		return fs;
	}

	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub
		RandomAccessFile fout = openFile();
		//byte[] writeByte = new byte[1];
		//writeByte[0] = (byte) b;
		//fout.write(writeByte);
        fout.write(b);
        fout.close();
		pos++;
	}

}
