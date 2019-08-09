package klavir;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {

	public static Color sartruz = Color.PINK;//new Color(127, 255, 0);
	static class NoteData {
		public int octave;
		public int numSym;
		public char hight;
		public boolean sharp;
		public NoteData(int octave, int numSym, char hight, boolean sharp) {
			super();
			this.octave = octave;
			this.numSym = numSym;
			this.hight = hight;
			this.sharp = sharp;
		}

		@Override
		public String toString() {
			return "NoteData [octave=" + octave + ", numSym=" + numSym + ", hight=" + hight + ", sharp=" + sharp + "]";
		}

	}

	static Map<Character, NoteData> map = new HashMap<Character, NoteData>();

	private static File file;

	private static void readExcel(String fileName) {
		try {
			file = new File(fileName);
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(file));
			Stream<String> stream = br.lines();
			Pattern p = Pattern.compile("(.),(.*),([0-9]*).*");

			stream.forEach(l -> {
				Matcher m = p.matcher(l);
				if (m.matches()) {
					char slovo = m.group(1).charAt(0);
					NoteData nd;
					if (m.group(2).charAt(1) == '#') {
						nd = new NoteData(m.group(2).charAt(2) - '0', Integer.parseInt(m.group(3)),
								m.group(2).charAt(0), true);
					} else {
						nd = new NoteData(m.group(2).charAt(1) - '0', Integer.parseInt(m.group(3)),
								m.group(2).charAt(0), false);
					}
					map.put(slovo, nd);

				}

			});

		} catch (FileNotFoundException e) {
			System.out.println("Fajl nije pronadjen! ALOOOO!");
		} finally {

		}
	}

	public static void main(String[] args) {
		readExcel(args[0]);
		//Composition c = new Composition();
		//c.loadComposition("fur_elise.txt");
		Player p = new Player();
		p.setVisible(true);
		/*int i = 0;
		i++;
		i++;
		System.out.println(i);*/
	}

}
