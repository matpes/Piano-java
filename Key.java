package klavir;

import java.awt.*;

import klavir.Main.NoteData;

public class Key {
	private boolean mouseClicked;
	private int x, y;
	private Color color;
	private int sirinaDirke, visinaDirke;
	public static int sirinaPlatna, visinaPlatna;
	private char symbol;
	private NoteData note;

	
	
	public NoteData getNote() {
		return note;
	}

	public void setNote(NoteData note) {
		this.note = note;
	}

	public boolean isMouseClicked() {
		return mouseClicked;
	}

	public void setMouseClicked(boolean mouseClicked) {
		this.mouseClicked = mouseClicked;
	}

	public char getSymbol() {
		return symbol;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	
	
	public int getSirinaDirke() {
		return sirinaDirke;
	}

	public Key(Color color, char symbol, NoteData note) {
		super();
		this.color = color;
		this.symbol = symbol;
		this.note = note;
	}

	public Key(char symbol, NoteData note) {
		this(Color.WHITE, symbol, note);
	}

	public void drawKey(Graphics g) {
		
		sirinaDirke = sirinaPlatna / 35+ (sirinaPlatna % 35>15?1:0);
		visinaDirke = visinaPlatna;
		y = 0;
		x = ((note.octave - 2) * 7 + (note.hight + 3) % 7) * sirinaDirke;
		if (note.sharp) {
			x += 2*sirinaDirke/3;
			sirinaDirke=2*sirinaDirke/3;
			visinaDirke *=2;
			visinaDirke /=3;
		}
		g.setColor(color);
		g.fillRect(x, y, sirinaDirke, visinaDirke);
		Color oldc = color;
		if(color==Color.WHITE){
			color = Color.BLACK;
		}else {
			color=Color.WHITE;
		}
		g.setColor(color);
		g.drawRect(x-1, y, sirinaDirke, visinaDirke);
		g.drawString(""+symbol, x+sirinaDirke/3, 4*visinaDirke/5);
		color = oldc;
		//(symbol, 0, 1, x, 4*visinaDirke/5);
		
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	

}
