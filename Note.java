package klavir;

import java.awt.*;

import javax.sound.midi.*;

public class Note extends MusicSymbol {

	private int octave;
	private int numSym;
	private char hight;
	private char chrSym;
	private boolean sharp;

	// protected void write(ostream & it)override;
	// protected void writeFormatt(ofstream & it)override;

	// public static void det_color(Note *n, uint8_t &R, uint8_t &G, uint8_t &B);

	public void setOctave(int o) {
		if (octave + o >= 6) {
			octave = 6;
		} else {
			if (octave + o <= 2) {
				octave = 2;
			} else {
				octave = o;
			}
		}
	}

	public Note(Duration dur, int octave, int numSym, char hight, char chrSym) {
		this(dur, octave, numSym, hight, chrSym, false);
	}

	public Note(Duration dur, int octave, int numSym, char hight, char chrSym, boolean sharp) {
		super(dur);
		this.octave = octave;
		this.numSym = numSym;
		this.hight = hight;
		this.sharp = sharp;
		this.chrSym = chrSym;
	}

	public void setSign(int c) {

		if (c == 0)
			return;
		if (c > 0) {
			if (hight == 'A' || hight == 'B') {
				hight = 'B';
				return;
			}
			if (hight + c <= 'G') {
				hight += c;
				return;
			} else {
				if (hight + c == 'H') {
					hight = 'A';
					return;
				} else {
					hight = 'B';
					return;
				}
			}
		} else {
			while (c < 0) {
				if (hight != 'C') {
					if (hight == 'A') {
						hight = 'G';
					} else {
						hight -= 1;
					}
				}
				c++;
			}
		}
	}

	public int getNumSym() {
		return numSym;
	}

	public int Oct() {
		return octave;
	}

	public char Hight() {
		return hight;
	}

	public boolean isSharp() {
		return sharp;
	}

	@Override
	public void setSharp() {
		sharp = !sharp;
	}

	@Override
	public boolean same(Note note) {
		if (note.chrSym == chrSym && note.du.jednako(du)) {
			return true;
		}
		return false;
	}

	@Override
	public void playMusicSymbol(MidiPlayer player, Piano piano) {

		try {
			piano.collorKey(numSym);
			player.play(numSym, (du.getDenominator() % 6) * 100);
			// System.out.println((du.getDenominator()*2)%12*100-100);
			piano.decollorKey(numSym);
		} catch (InterruptedException e) {
			piano.decollorKey(numSym);
		}

	}

	public String getText(boolean charLetter) {
		String text = null;
		if (charLetter) {
			text = "" + chrSym;
		} else {
			StringBuilder s = new StringBuilder();
			s.append(hight);
			if (sharp) {
				s.append('#');
			}
			s.append(octave);
			text = s.toString();
		}
		return text;
	}

	@Override
	public void drawMusicSymbol(Graphics g, int leftBorder, int height, boolean charLetter) {
		Color bg = null;

		if (du.getDenominator() == 4) {
			bg = Color.RED;
			g.setColor(bg);
			g.fillRect(leftBorder, height / 2 - 7, 50, 14);
		} else {
			bg = Color.GREEN;
			g.setColor(bg);
			g.fillRect(leftBorder, height / 2 - 7, 25, 14);
		}
		String text = getText(charLetter);
		g.setColor(Color.BLACK);
		g.setFont(new Font("", Font.BOLD, 14));
		g.drawString(text, leftBorder, height / 2 + 5);
	}

	@Override
	public boolean isNoteEight() {
		return du.getDenominator() == 8;
	}

	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		/*
		 * if(du.getDenominator()==8) { s.append('[').append(chrSym).append(']'); return
		 * s.toString(); }
		 */
		s.append(chrSym);
		return s.toString();
	}

	@Override
	public int insertMidiSymbol(int i, Track track) throws InvalidMidiDataException {
		int offs = 0;
		if (du.getDenominator() == 8) {
			offs = 1;
		} else {
			offs = 2;
		}

		MidiEvent event = new MidiEvent(new ShortMessage(144, 1, numSym, 100), i);
		track.add(event);
		i += offs;
		event = new MidiEvent(new ShortMessage(128, 1, numSym, 100), i);
		track.add(event);
		return offs;
	}

}
