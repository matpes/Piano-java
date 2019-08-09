package klavir;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;

import javax.sound.midi.Track;

public class Pause extends MusicSymbol {

	public Pause(Duration dur) {
		super(dur);
		// TODO Auto-generated constructor stub
	}
	@Override
	public  void setOctave(int o) { }
	@Override
	public  void setSign(int c) { }
	@Override
	public void playMusicSymbol(MidiPlayer player, Piano piano) {
		try {
			Thread.sleep(du.getDenominator()%6*100);
		} catch (InterruptedException e) {
			
		}
	}
	
	
	
	@Override
	public boolean same(Pause ms) {
		return du.jednako(ms.du);
	}
	@Override
	public void drawMusicSymbol(Graphics g, int leftBorder, int height, boolean charLetter) {
		Color bg = null;
		if(du.getDenominator()==4) {
			bg = new Color(130, 0, 0);
			g.setColor(bg);
			g.fillRect(leftBorder, height/2-7, 50, 14);
			g.setColor(Color.BLACK);
		}else {
			bg = new Color(13, 97, 0);
			g.setColor(bg);
			g.fillRect(leftBorder, height/2-7, 25, 14);
			g.setColor(Color.BLACK);
		}
		
	}
	@Override
	public String toString() {
		if (du.getDenominator()==4) {
			return "|";
		}else {
			return " ";
		}
	}
	@Override
	public int insertMidiSymbol(int i, Track track) {
		if(du.getDenominator()==8) {
			return 1;
		}
		return 2;
	}
	
	
}
