package klavir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import klavir.Main.NoteData;

public class Composition implements Iterable<MusicSymbol> {
	private LinkedList<MusicSymbol> composition;

	public Composition() {
		super();
		composition = new LinkedList<MusicSymbol>();
	}

	public void addMusicSymbol(MusicSymbol ms) {
		composition.add(ms);
	}

	public void riseOctave(int i) {
		composition.forEach(ms -> {
			ms.setOctave(i);
		});
	}

	public void riseHeight(int i) {
		composition.forEach(ms -> {
			ms.setSign(i);
		});
	}
	
	public void loadComposition(String fileName) {
		try {
			File file = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(file));
			Stream<String> stream = br.lines();
			Pattern p = Pattern.compile("[^\\[]*\\[([^\\]]*)\\](.*)");
			//String line = "";
			//smatch res;
			//regex rx("(.)(#?)(.)");
			stream.forEach(l->{
				String line = l;
				int i = 0;
				while(i<line.length()) {
					char c = line.charAt(i);
					if(c=='[') {
						Matcher m = p.matcher(line);
						if(m.matches()) {
							String str = m.group(1);
							line = m.group(2);
							i = 0;
							if(str.length()==1 || str.charAt(1)==' ') {//TO SU OSMINE
								for(int j=0; j<str.length(); j++) {
									if(str.charAt(j)!=' ') {
										char cc=str.charAt(j);
										Main.NoteData nd= Main.map.get(cc);
										this.addMusicSymbol(new Note(new Duration(1, 8), nd.octave, nd.numSym, nd.hight, cc, nd.sharp));
									}
								}
							}else {
								Chord chord = new Chord(new Duration(1, 4));
								for(int j=0; j<str.length(); j++) {
									char cc=str.charAt(j);
									Main.NoteData nd= Main.map.get(cc);
									chord.addNote(new Note(new Duration(1, 4), nd.octave, nd.numSym, nd.hight, cc, nd.sharp));	
								}
								this.addMusicSymbol(chord);
							}
						}
					}else {
						i++;
						if(c==' ') {
							addMusicSymbol(new Pause(new Duration(1, 8)));
						}else {
							if (c == '|') {
								addMusicSymbol(new Pause(new Duration(1, 4)));
							}
							else {
								Main.NoteData nd= Main.map.get(c);
								this.addMusicSymbol(new Note(new Duration(1, 4), nd.octave, nd.numSym, nd.hight, c, nd.sharp));

							}
						}
						
					}
					
				}
				
				
				
			});

		} catch (FileNotFoundException e) {
			System.out.println("Fajl nije pronadjen! ALOOOO!");
		} finally {
		}
	}
	
	
	
	public LinkedList<MusicSymbol> getComposition() {
		return composition;
	}

	public void playComposition(MidiPlayer player, Piano piano, Boolean working) {
		composition.forEach(l->{
			if(!working) {
				return;
			}
			l.playMusicSymbol(player, piano);
		});
	}

	@Override
	public Iterator<MusicSymbol> iterator() {
		return composition.iterator();
	}
	
	

}