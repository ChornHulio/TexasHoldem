package rollout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import core.Card;

public class Reader {
	
	public double[][] readFile(String pathname) throws Exception {
		double[][] values = new double[Card.VALUE.values().length][Card.VALUE.values().length];
		for(int i = 0; i < values.length; i++) {
			for (int j = 0; j < values[i].length; j++) {
				values[i][j] = 0;
			}
		}
		
		File file = new File(pathname);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			BufferedReader in = new BufferedReader(fr);
			String line = null;
			for (int i = 0; i < Card.VALUE.values().length; i++) {
				if((line = in.readLine()) == null) {
					throw new Exception("File disrupted");
				}
				line = line.trim();
				String[] strings = line.split("\t");
				for (int j = Card.VALUE.values().length - strings.length; j < strings.length; j++) {
					values[i][j] = Double.parseDouble(strings[j]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return values;
	}

}
