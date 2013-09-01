package IO;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class TransactionalFileOutputStream extends OutputStream implements
		Serializable {
	public String fileName;
	public int pos;

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

	private FileOutputStream openFile() throws IOException {
		FileOutputStream fs = new FileOutputStream(fileName, true);
		return fs;
	}

	@Override
	public void write(int b) throws IOException {
		// TODO Auto-generated method stub
		FileOutputStream fout = openFile();
		byte[] writeByte = new byte[1];
		writeByte[0] = (byte) b;
		fout.write(writeByte);
		pos++;
	}

}
