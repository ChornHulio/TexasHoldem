package rollout;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
	
	public Writer(String dirPath) {
		File dir = new File(dirPath);
		dir.mkdir();
	}

	public void writeOutputFile(String dir, String filename, String str) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(dir+filename, false); // clear file before writing
			fw.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fw != null) {
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}		
	}

}
