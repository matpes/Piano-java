package klavir;

import java.awt.Graphics;
import java.awt.Panel;
import javax.sound.midi.*;
public abstract class MusicSymbol {
	
	protected Duration du;
	/*protected void write(ostream& it)=0;
	protected void writeFormatt(ofstream& it) = 0;
	*/
	
	public MusicSymbol(Duration du) {
		super();
		this.du = du;
	}
	public abstract void setOctave(int o);
	public abstract void setSign (int c);
	public abstract void playMusicSymbol(MidiPlayer player, Piano piano);
	public abstract void drawMusicSymbol(Graphics g, int leftBorder, int height, boolean charLetter);
	public void setSharp() { }
	
	public boolean isNoteEight() {
		return false;
	}
	
	public abstract int insertMidiSymbol(int i, Track track) throws InvalidMidiDataException;
	
	public boolean same(Note ms) {
		return false;
	}
	
	public boolean same(Pause ms) {
		return false;
	}
	
	public boolean same(Chord ms) {
		return false;
	}
	
	public Duration getDuration(){
			return du;
	}

	public void setDuration(Duration d) {
			this.du = d;
	}

}
