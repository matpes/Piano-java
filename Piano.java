package klavir;

import java.awt.*;

import java.awt.event.*;
import java.util.LinkedList;

import javax.sound.midi.MidiUnavailableException;

import klavir.Main.NoteData;

@SuppressWarnings("serial")
public class Piano extends Canvas {
	private LinkedList<Key> notes, sharps;
	private MidiPlayer player;
	private Chord chordForChecking = new Chord(new Duration(1, 4));
	private long[] pressed, released;
	private VerticalLines vl;
	private boolean shifted = false;
	public static long lastTimeChecked = 0;

	
	public Piano() {
		super();
		pressed = new long[250];
		released = new long[250];
		try {
			player = new MidiPlayer();
		} catch (MidiUnavailableException e2) {

		}
		setBackground(Color.DARK_GRAY);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				boolean found = false;
				if (y < 2 * Key.visinaPlatna / 3) {
					for (Key k : sharps) {
						if (x > k.getX() && x < k.getX() + k.getSirinaDirke()) {
							k.setMouseClicked(true);
							k.setColor(Main.sartruz);
							k.drawKey(getGraphics());
							found = true;
							pressed[k.getSymbol()] = System.currentTimeMillis();
							player.play(k.getNote().numSym);
							break;
						}
					}
				}
				if (!found) {
					for (Key k : notes) {
						int xx = k.getX();
						if (x > xx && x < xx + k.getSirinaDirke()) {
							k.setMouseClicked(true);
							k.setColor(Main.sartruz);
							k.drawKey(getGraphics());
							recolorSharps(k);
							pressed[k.getSymbol()] = System.currentTimeMillis();
							player.play(k.getNote().numSym);

							break;
						}
					}
				}
				if (Player.recording) {
					if (lastTimeChecked == 0) {
						lastTimeChecked = System.currentTimeMillis();
					} else {
						if (System.currentTimeMillis() - lastTimeChecked > 800) {
							Player.myComposition.addMusicSymbol(new Pause(new Duration(1, 4)));
						} else {
							if (System.currentTimeMillis() - lastTimeChecked > 400) {
								Player.myComposition.addMusicSymbol(new Pause(new Duration(1, 8)));
							}
						}
						lastTimeChecked = System.currentTimeMillis();
					}
				}

			}

			@Override
			public void mouseReleased(MouseEvent e) {

				boolean found = false;
				for (Key k : sharps) {
					if (k.isMouseClicked()) {
						k.setColor(Color.BLACK);
						k.drawKey(getGraphics());
						k.setMouseClicked(false);
						player.release(k.getNote().numSym);
						NoteData nd = k.getNote();
						Note newNote = null;
						if (System.currentTimeMillis() - pressed[k.getSymbol()] < 200) {
							newNote = new Note(new Duration(1, 8), nd.octave, nd.numSym, nd.hight, k.getSymbol());
						} else {
							newNote = new Note(new Duration(1, 4), nd.octave, nd.numSym, nd.hight, k.getSymbol());
						}
						if (vl != null && vl.getFirst() != null && vl.getFirst().same(newNote)) {
							if (vl != null && vl.getSecond() instanceof Pause) {
								vl.shift();
							}
							vl.shift();
						}
						pressed[k.getSymbol()] = 0;
						if (Player.recording) {
							Player.myComposition.addMusicSymbol(newNote);
						}
						// INSERT INTO RECORDING
						found = true;
						break;
					}
				}

				if (!found) {
					for (Key k : notes) {
						if (k.isMouseClicked()) {
							k.setColor(Color.WHITE);
							k.drawKey(getGraphics());
							k.setMouseClicked(false);
							player.release(k.getNote().numSym);
							recolorSharps(k);
							NoteData nd = k.getNote();
							Note newNote = null;
							if (System.currentTimeMillis() - pressed[k.getSymbol()] < 200) {
								newNote = new Note(new Duration(1, 8), nd.octave, nd.numSym, nd.hight, k.getSymbol());
							} else {
								newNote = new Note(new Duration(1, 4), nd.octave, nd.numSym, nd.hight, k.getSymbol());
							}
							if (vl != null && vl.getFirst() != null && vl.getFirst().same(newNote)) {
								if (vl != null && vl.getSecond() instanceof Pause) {
									vl.shift();
								}
								vl.shift();
							}
							pressed[k.getSymbol()] = 0;
							// INSERT INTO RECORDING
							if (Player.recording) {
								Player.myComposition.addMusicSymbol(newNote);
							}
							break;
						}
					}
				}
				if (Player.recording) {
					lastTimeChecked = System.currentTimeMillis();
				}
			}
		});

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				boolean found = false;
				for (Key k : sharps) {
					if (c == k.getSymbol() && k.getColor() != Main.sartruz) {
						k.setColor(Main.sartruz);
						k.drawKey(getGraphics());
						found = true;
						player.play(k.getNote().numSym);
						pressed[k.getSymbol()] = System.currentTimeMillis();
						break;
					}
				}
				if (!found) {
					for (Key k : notes) {
						if (c == k.getSymbol() && k.getColor() != Main.sartruz) {
							k.setColor(Main.sartruz);
							k.drawKey(getGraphics());
							recolorSharps(k);
							player.play(k.getNote().numSym);
							pressed[k.getSymbol()] = System.currentTimeMillis();
							break;
						}
					}
				}
				if (Player.recording) {
					if (lastTimeChecked != 0) {
						if (System.currentTimeMillis() - lastTimeChecked > 800) {
							Player.myComposition.addMusicSymbol(new Pause(new Duration(1, 4)));
						} else {
							if (System.currentTimeMillis() - lastTimeChecked > 400) {
								Player.myComposition.addMusicSymbol(new Pause(new Duration(1, 8)));
							}
						}
					}
					lastTimeChecked = System.currentTimeMillis();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				boolean found = false;
				char c = e.getKeyChar();
				for (Key k : sharps) {
					if (pressed[k.getSymbol()] != 0) {
						pressed[k.getSymbol()] = 0;
						released[k.getSymbol()] = System.currentTimeMillis();
						NoteData nd = k.getNote();
						Note newNote = null;
						if (System.currentTimeMillis() - pressed[k.getSymbol()] < 200) {
							newNote = new Note(new Duration(1, 8), nd.octave, nd.numSym, nd.hight, k.getSymbol());
						} else {
							newNote = new Note(new Duration(1, 4), nd.octave, nd.numSym, nd.hight, k.getSymbol());
						}
						chordForChecking.addNote(newNote);
						player.release(k.getNote().numSym);
					}
					if (c == k.getSymbol()) {

						k.setColor(Color.BLACK);
						k.drawKey(getGraphics());
						// player.release(k.getNote().numSym);
						found = true;
						// break;
					}
				}

				// if (!found) {
				for (Key k : notes) {
					if (pressed[k.getSymbol()] != 0) {

						released[k.getSymbol()] = System.currentTimeMillis();
						NoteData nd = k.getNote();
						Note newNote = null;
						if (System.currentTimeMillis() - pressed[k.getSymbol()] < 200) {
							newNote = new Note(new Duration(1, 8), nd.octave, nd.numSym, nd.hight, k.getSymbol());
						} else {
							newNote = new Note(new Duration(1, 4), nd.octave, nd.numSym, nd.hight, k.getSymbol());
						}
						pressed[k.getSymbol()] = 0;
						chordForChecking.addNote(newNote);
						player.release(k.getNote().numSym);
					}
					if (c == k.getSymbol()) {
						k.setColor(Color.WHITE);
						k.drawKey(getGraphics());
						recolorSharps(k);

						released[k.getSymbol()] = 0;
						// player.release(k.getNote().numSym);
						// break;
					}
				}
				// }

				if (chordForChecking.getChord().size() == 1) {

					Note newNote = chordForChecking.getChord().get(0);
					if (Player.recording) {
						lastTimeChecked = System.currentTimeMillis();
						Player.myComposition.addMusicSymbol(newNote);
					}
					if (vl != null && vl.getFirst() != null && vl.getFirst().same(newNote)) {
						if (vl != null && vl.getSecond() instanceof Pause) {
							vl.shift();
						}
						vl.shift();
					}
				} else {
					if (Player.recording) {
						lastTimeChecked = System.currentTimeMillis();
						if (!chordForChecking.toString().equals("[]")) {
							Player.myComposition.addMusicSymbol(chordForChecking);
						}
					}
					if (vl != null && vl.getFirst() != null && vl.getFirst().same(chordForChecking) && !shifted) {
						if (vl != null && vl.getSecond() instanceof Pause) {
							vl.shift();
						}
						vl.shift();
						shifted = true;
					}
				}
				
				// INSERT INTO RECORDING
				chordForChecking = new Chord(new Duration(1, 4));
				shifted = false;
			}

		});

		setBounds(200, 200, 840, 200);
		notes = new LinkedList<Key>();
		sharps = new LinkedList<Key>();

		Main.map.forEach((c, dn) -> {
			if (dn.sharp) {
				sharps.add(new Key(Color.BLACK, c, dn));
			} else {
				notes.add(new Key(c, dn));
			}
		});
	}

	private void recolorSharps(Key k) {
		int xx = k.getX();
		for (Key kk : sharps) {
			if ((xx < kk.getX() && xx + k.getSirinaDirke() > kk.getX())
					|| (xx > kk.getX() && xx < kk.getX() + kk.getSirinaDirke())) {
				kk.drawKey(getGraphics());
			}
		}

	}

	public void collorKey(int numSym) {
		boolean found = false;
		for (Key k : sharps) {
			if (k.getNote().numSym == numSym) {
				k.setColor(Main.sartruz);
				k.drawKey(getGraphics());
				found = true;
				break;
			}
		}

		if (!found) {
			for (Key k : notes) {
				if (k.getNote().numSym == numSym) {
					k.setColor(Main.sartruz);
					k.drawKey(getGraphics());
					recolorSharps(k);
					break;
				}
			}
		}
	}

	public void decollorKey(int numSym) {
		boolean found = false;
		for (Key k : sharps) {
			if (k.getNote().numSym == numSym) {
				k.setColor(Color.BLACK);
				k.drawKey(getGraphics());
				found = true;
				break;
			}
		}

		if (!found) {
			for (Key k : notes) {
				if (k.getNote().numSym == numSym) {
					k.setColor(Color.WHITE);
					k.drawKey(getGraphics());
					recolorSharps(k);
					break;
				}
			}
		}
	}

	@Override
	public void paint(Graphics arg0) {
		Key.sirinaPlatna = getWidth();
		Key.visinaPlatna = getHeight();
		notes.forEach(n -> n.drawKey(arg0));
		sharps.forEach(s -> s.drawKey(arg0));
	}

	public VerticalLines getVl() {
		return vl;
	}

	public void setVl(VerticalLines vl) {
		this.vl = vl;
	}

}
