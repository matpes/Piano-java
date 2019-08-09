package klavir;

import javax.sound.midi.MidiUnavailableException;

public class PianoThread extends Thread {

	private Composition composition;
	private Piano piano;
	private Player owner;
	private boolean working = false;
	private VerticalLines vl;
	public PianoThread(Composition composition, Piano piano, Player player, VerticalLines vl) {
		super();
		this.composition = composition;
		this.piano = piano;
		owner = player;
		this.vl = vl;
	}

	@Override
	public void run() {
		int i = 0;
		try {
			while (!interrupted()) {
				synchronized (this) {
					while (!working) {
						wait();
					}
				}
				try {
					MidiPlayer pla = new MidiPlayer();
					
					for(MusicSymbol sym :composition.getComposition()) {
						synchronized (this) {
							while (!working) {
								wait();
							}
						}
						sym.playMusicSymbol(pla, piano);
						vl.setI(i);
						vl.shift();
						i++;
						//slider.getPanel().repaint();
					}
					
					/*.forEach(l->{
						while(!working) {
						}
						l.playMusicSymbol(pla, piano);
					});*/
				} catch (MidiUnavailableException e) {
				}
				working = false;
				owner.resetButtons();
			}
		} catch (InterruptedException e) {
		}

	}

	public synchronized void continuePlaying() {
		working = true;
		notifyAll();
	}

	public void pausePlaying() {
		working = false;
	}

	public void stopPlaying() {
		working = false;
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		interrupt();
	}

	public Composition getComposition() {
		return composition;
	}

	public void setComposition(Composition composition) {
		this.composition = composition;
	}

}
