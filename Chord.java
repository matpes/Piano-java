package klavir;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Panel;
import java.util.Iterator;
import java.util.LinkedList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Chord extends MusicSymbol implements Iterable<Note> {
	private LinkedList<Note> chord;

	public Chord(Duration dur) {
		super(dur);
		chord = new LinkedList<Note>();
	}

	
	
	
	@Override
	public boolean same(Chord ms) {
		if(ms.chord.size()!=chord.size()) {
			return false;
		}
		boolean found = false;
		for(Note l:chord){
			found = false;
			for(Note note : ms.chord) {
				if(note.same(l)) {
					found = true;
					break;
				}
			}
			if(!found) {
				return false;
			}
		};
		return true;
	}




	@Override
	public void setOctave(int o) {
		chord.forEach(n -> {
			n.setOctave(o);
		});

		/*
		 * for (Note n : chord) { n.setOctave(o); }
		 */
	}

	@Override
	public void setSign(int c) {
		for (Note n : chord) {
			n.setSign(c);
		}
	}

	@Override
	public void setDuration(Duration d) {
		for (Note n : chord) {
			n.setDuration(d);
		}
	}

	@Override
	public void setSharp() {
		for (Note n : chord) {
			n.setSharp();
		}
	}

	boolean isNotEmpty() {
		return chord.size() != 0;
	}

	LinkedList<Note> getChord() {
		return chord;
	}

	/*
	 * auto begin() { return chord -> begin(); }
	 * 
	 * auto back() { return --end(); }
	 */

	/*
	 * list<Note*>::iterator end() { return chord->end(); }
	 */

	void addNote(Note n) {
		chord.add(n);
		// chord->push_back(n);
	}

	@Override
	public void playMusicSymbol(MidiPlayer player, Piano piano) {

		try {
			chord.forEach(l -> {
				piano.collorKey(l.getNumSym());
				player.play(l.getNumSym());
			});
			Thread.sleep(400);
		} catch (InterruptedException e) {

		} finally {
			chord.forEach(l -> {
				player.release(l.getNumSym());
				piano.decollorKey(l.getNumSym());
			});
		}
	}

	// OBRISIII
	public Panel addSlidingLabel(boolean charLetter) {
		Panel panel = new Panel(new GridLayout(0, 1));
		chord.forEach(l -> {
			// panel.add(l.getLabel(charLetter));
		});
		return panel;
	}

	@Override
	public void drawMusicSymbol(Graphics g, int leftBorder, int height, boolean charLetter) {

		int visinaNote = height / chord.size();
		if (visinaNote > 14) {
			visinaNote = 14;
		}
		int y = height / 2 - visinaNote * chord.size() / 2 - (chord.size() % 2 == 0 ? 0 : visinaNote / 2);
		
		for (Note n : chord) {
			g.setColor(Color.RED);
			g.fillRect(leftBorder, y, 50, visinaNote);
			g.setColor(Color.BLACK);
			g.setFont(new Font("", Font.BOLD, 14));
			g.drawString(n.getText(charLetter), leftBorder, y+9);
			y+=visinaNote;
		}

	}




	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append('[');
		for(MusicSymbol ms: chord) {
			s.append(ms.toString());
		}
		s.append(']');
		return s.toString();
	}




	@Override
	public Iterator<Note> iterator() {
		return chord.iterator();
	}




	@Override
	public int insertMidiSymbol(int i, Track track) throws InvalidMidiDataException {
		int offs = 0;
		if (du.getDenominator() == 8) {
			offs = 1;
		} else {
			offs = 2;
		}
		Iterator<Note> iter = chord.iterator();
		while(iter.hasNext()) {
		Note n = iter.next();
		MidiEvent event = new MidiEvent(new ShortMessage(144, 1, n.getNumSym(), 100), i);
		track.add(event);
		}
		
		i += offs;
		
		iter = chord.iterator();
		while(iter.hasNext()) {
		Note n = iter.next();
		MidiEvent event = new MidiEvent(new ShortMessage(128, 1, n.getNumSym(), 100), i);
		track.add(event);
		}
		return offs;
	}
	
	
	
	
}