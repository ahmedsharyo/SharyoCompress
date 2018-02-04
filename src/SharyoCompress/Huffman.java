package SharyoCompress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class Huffman {

	
	public static String compress(String s) {
		
		
		HashMap<String, String> dic = new HashMap<String, String>();
		HashMap<String, Double> freq = new HashMap<String, Double>();
		int n = s.length();
		for (int i = 0; i < n; i++) {
			String d = "";
			d += s.charAt(i);
			if (freq.containsKey(d)) {
				Double rep = freq.get(d) + 1.0;
				freq.replace(d, rep);
			} else {
				freq.put(d, 1.0);
			}
		}

		for (Map.Entry<String, Double> entry : freq.entrySet()) {
			String key = entry.getKey();
			Double value = entry.getValue();
			freq.replace(key, (freq.get(key) / n));
			dic.put(key, "");
		}

		if (freq.size() == 1) {
			for (Map.Entry<String, Double> entry : freq.entrySet()) {
				String key = entry.getKey();
				dic.replace(key, "0");
			}
		}

		while (freq.size() != 1) {
			String fir = null;
			double mi1 = 5.0;
			String second = null;
			double mi2 = 5.0;
			for (Map.Entry<String, Double> entry : freq.entrySet()) {
				String key = entry.getKey();
				Double value = entry.getValue();
				if (value < mi1) {
					mi1 = value;
					fir = key;
				}
			}
			for (Map.Entry<String, Double> entry : freq.entrySet()) {
				String key = entry.getKey();
				Double value = entry.getValue();
				if (value < mi2 && !key.equals(fir)) {
					mi2 = value;
					second = key;
				}
			}

			for (int i = 0; i < fir.length(); i++) {
				String sub = fir.substring(i, i + 1);
				String val = dic.get(sub);
				val = "1" + val;
				dic.replace(sub, val);
			}

			for (int i = 0; i < second.length(); i++) {
				String sub = second.substring(i, i + 1);
				String val = dic.get(sub);
				val = "0" + val;
				dic.replace(sub, val);
			}

			String nw = second + fir;
			Double nwval = mi1 + mi2;
			freq.remove(fir);
			freq.remove(second);
			freq.put(nw, nwval);
		}
		// write
		String path = "dic_char.txt";
		try {
			FileOutputStream fos = new FileOutputStream(path);
			ObjectOutputStream os = new ObjectOutputStream(fos);

			for (Map.Entry<String, String> entry : dic.entrySet()) {
				String key = entry.getKey();
				os.writeByte(key.charAt(0));

			}
			os.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		PrintWriter writer1 = null;
		try {
			writer1 = new PrintWriter("dic_bits.txt");
		} catch (Exception e) {
		}

		for (Map.Entry<String, String> entry : dic.entrySet()) {
			String value = entry.getValue();
			writer1.println(value);
		}
		writer1.close();
		String out = "";
		for (int i = 0; i < n; i++) {
			String d = "";
			d += s.charAt(i);
			out += dic.get(d);
		}
		return out;

	}

	

	public static String decompress(String s) {

		HashMap<String, String> dic = new HashMap<String, String>();
		Vector<String> values = new Vector<String>();
		Vector<String> keys = new Vector<String>();

		String path = "dic_char.txt";

		char input;

		FileInputStream fis;
		try {
			fis = new FileInputStream(path);
			ObjectInputStream is = new ObjectInputStream(fis);
			while (is.available() != 0) {
				input = (char) is.read();
				values.add(input + "");
			}
			is.close();

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scanner scan1 = null;
		try {
			scan1 = new Scanner(new File("dic_bits.txt"));
		} catch (Exception e) {
			System.out.println("error");
		}

		while (scan1.hasNextLine()) {
			String key = scan1.nextLine();
			keys.add(key);
		}
		for (int i = 0; i < values.size(); i++) {
			String key, value;
			key = keys.get(i);
			value = values.get(i);
			dic.put(key, value);
		}

		String out = "";
		String sofar = "";
		for (int i = 0; i < s.length(); i++) {
			sofar += s.charAt(i);
			if (dic.containsKey(sofar)) {
				out += dic.get(sofar);
				sofar = "";
			}
		}
		return out;
	}

}
