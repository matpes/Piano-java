package klavir;

import java.awt.*;

@SuppressWarnings("serial")
public class VerticalLines extends Canvas {

	private Composition composition;
	private int i = 0;
	private boolean charLetter;
	private MusicSymbol firstElem, secondElem;
	
	
	public VerticalLines() {
		super();
		setPreferredSize(new Dimension(600, 200));
		charLetter = true;
	}

	

	public void setI(int i) {
		this.i = i;
	}


	public MusicSymbol getFirst() {
			if(composition!=null) {
				return firstElem;
			}
		return null;
	}
	
	public MusicSymbol getSecond() {
		if(composition!=null) {
			return secondElem;
		}
	return null;
}
	

	public void setTextTipe(boolean b) {
		charLetter = b;
		repaint();
	}

	
	public void setComposition(Composition composition) {
		this.composition = composition;
		i=0;
		repaint();

	}

	public void setChars(boolean chars) {
		charLetter = chars;
	}

	public void shift() {
		++i;
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		int visina = getHeight();
		int sirina = getWidth();

		int j = 10;

		int k = 0;
		if (composition != null) {
			for (MusicSymbol ms : composition.getComposition()) {
				if (j >= getWidth()) {
					break;
				}
				if(k==i) {
					firstElem = ms;
				}
				if(k==i+1) {
					secondElem = ms;
				}
				if (k >= i) {
					ms.drawMusicSymbol(g, j, 2*visina / 3, charLetter);
					if (ms.getDuration().getDenominator() == 4) {
						j += 50;
					} else {
						j += 25;
					}
				}
				k++;
			}

			for (int i = 0; i < sirina / 50 + 1; i++) {
				g.fillRect(i * 50 + 10, 2*visina / 3, 2, visina / 3);
			}
		}
	}

}
