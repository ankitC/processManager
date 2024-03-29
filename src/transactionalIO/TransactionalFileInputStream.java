package transactionalIO;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class TransactionalFileInputStream extends InputStream implements Serializable{

	private static final long serialVersionUID = 1L;
	private String fileName;
	private int pos;

	public TransactionalFileInputStream(String fileName) {
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

	private FileInputStream openFile() throws IOException {
		FileInputStream fs = new FileInputStream(fileName);
		fs.skip(pos);
		return fs;
	}

	@Override
	public int read() throws IOException {

		FileInputStream fin=openFile();
		int returnVal=fin.read();
		fin.close();

		if(returnVal>-1)
			pos=pos+1;

		return returnVal;
	}

	@Override
	public String toString() {
		return "TransactionalInputStream [fileName=" + fileName + ", pos="+ pos + "]";
	}
}
