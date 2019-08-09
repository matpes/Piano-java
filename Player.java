package klavir;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

@SuppressWarnings("serial")
public class Player extends Frame {
	private Piano piano;
	private TextField compositionName, savingName;
	private Button buttonLoad, buttonPlay, buttonPause, buttonStop, buttonRecord;
	private Composition loadedComposition;
	public static Composition myComposition;
	private PianoThread pThread;
	private VerticalLines vl;
	static public boolean recording = false;
	static public boolean saved = false;
	private Menu meni = new Menu("Options");
	private MenuBar meniBar = new MenuBar();
	private MenuItem save = new MenuItem("Save", new MenuShortcut('S'));
	private CheckboxMenuItem showNotes = new CheckboxMenuItem("Show notes", false);
	private Checkbox txt, midi;
	private Dialog dialog = new Dialog(this, "Save recorded composition", true);

	public Player() {
		super("Piano");
		meniBar.add(meni);
		meni.add(save);
		meni.addSeparator();
		meni.add(showNotes);
		setMenuBar(meniBar);
		Color bgCol = Color.PINK;
		Color fgCol = Color.DARK_GRAY;
		setBounds(200, 200, 1100, 500);
		Panel topPanel = new Panel(new GridLayout(1, 1));
		vl = new VerticalLines();
		vl.setPreferredSize(new Dimension(600, 80));

		topPanel.add(vl);
		topPanel.setBackground(bgCol);
		add(topPanel, "North");
		piano = new Piano();
		piano.setVl(vl);
		add(piano, "Center");
		Label title1 = new Label("TITLE OF THE", Label.CENTER);
		// title.setBackground(bgCol);
		title1.setForeground(bgCol);
		title1.setFont(new Font(null, Font.BOLD, 18));

		Label title2 = new Label("COMPOSITION", Label.CENTER);
		title2.setForeground(bgCol);
		title2.setFont(new Font(null, Font.BOLD, 26));

		addButtons(bgCol, fgCol);
		addButtonsAction();
		Panel panelSaOpcijama = new Panel(new FlowLayout(FlowLayout.CENTER, 20, 15));
		panelSaOpcijama.setPreferredSize(new Dimension(200, 400));
		panelSaOpcijama.setBackground(Color.DARK_GRAY);

		panelSaOpcijama.add(title1);
		panelSaOpcijama.add(title2);
		panelSaOpcijama.add(compositionName);
		panelSaOpcijama.add(buttonLoad);
		panelSaOpcijama.add(buttonPlay);
		panelSaOpcijama.add(buttonPause);
		panelSaOpcijama.add(buttonStop);
		panelSaOpcijama.add(buttonRecord);

		add(panelSaOpcijama, "East");
		// setVisible(true);
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dialog.dispose();
				dialog.removeAll();
				savedComp = false;
			}

		});
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if (buttonStop.isEnabled()) {
					pThread.stopPlaying();
				}
				if (pThread != null) {
					pThread.stopPlaying();
				}
				if(!savedGlobaly) {
					createSaveDialog();
				}
				dispose();
			}

		});
	}

	private void addButtonsAction() {
		buttonPause.setEnabled(false);
		buttonStop.setEnabled(false);
		buttonPlay.setEnabled(false);

		buttonLoad.addActionListener(l -> {
			if (!buttonPlay.isEnabled()) {
				buttonPlay.setEnabled(true);
			}

			if (pThread != null) {
				pThread.stopPlaying();
			}
			loadedComposition = new Composition();
			loadedComposition.loadComposition(compositionName.getText());
			vl.setComposition(loadedComposition);

		});

		buttonPlay.addActionListener(l -> {
			if (!(buttonStop.isEnabled() && !buttonPause.isEnabled())) {
				playComposition(loadedComposition);
			}

			if (!buttonPause.isEnabled()) {
				buttonPause.setEnabled(true);
			}

			if (!buttonStop.isEnabled()) {
				buttonStop.setEnabled(true);
			}
			buttonPlay.setEnabled(false);

			pThread.continuePlaying();
			// PUSTIM KOMPOZICIJU
			// Kada se komp zavrsi, onda odblokira dugme play

		});

		buttonRecord.addActionListener(l -> {
			if (!buttonPause.isEnabled()) {
				buttonPause.setEnabled(true);
			}

			if (!buttonStop.isEnabled()) {
				buttonStop.setEnabled(true);
			}

			// OBRADA
			savedGlobaly = false;
			if (myComposition != null) {
				// ASK TO SAVE
			}
			recording = true;
			myComposition = new Composition();
			buttonRecord.setEnabled(false);

		});

		buttonPause.addActionListener(l -> {

			// OBRADA
			if (!buttonPlay.isEnabled()) {
				pThread.pausePlaying();
			}

			if (!buttonRecord.isEnabled()) {
				recording = false;
			}

			if (!buttonPlay.isEnabled()) {
				buttonPlay.setEnabled(true);
			}

			if (!buttonRecord.isEnabled()) {
				buttonRecord.setEnabled(true);
			}
			buttonPause.setEnabled(false);

		});

		buttonStop.addActionListener(l -> {

			if (!buttonPlay.isEnabled()) {
				if (pThread != null) {
					pThread.stopPlaying();
				}
			}

			buttonPause.setEnabled(false);
			recording = false;
			// OBRBADA
			if (!buttonPlay.isEnabled()) {
				buttonPlay.setEnabled(true);
			}

			if (!buttonRecord.isEnabled()) {
				buttonRecord.setEnabled(true);
			}
			buttonStop.setEnabled(false);

		});

		save.addActionListener(l -> {
			createSaveDialog();
		});

		showNotes.addItemListener(l -> {
			if (vl != null) {
				vl.setTextTipe(!showNotes.getState());
			}

		});

	}

	private void addButtons(Color bgCol, Color fgCol) {
		compositionName = new TextField("jingle_bells.txt");
		compositionName.setPreferredSize(new Dimension(250, 75));
		compositionName.setFont(new Font(null, Font.BOLD, 16));
		compositionName.setForeground(fgCol);
		compositionName.setBackground(bgCol);

		buttonLoad = new Button("LD");
		buttonLoad.setBackground(bgCol);
		buttonLoad.setForeground(fgCol);
		buttonLoad.setFont(new Font(null, Font.BOLD, 20));
		buttonLoad.setPreferredSize(new Dimension(50, 50));

		buttonPlay = new Button("I>");
		buttonPlay.setBackground(bgCol);
		buttonPlay.setForeground(fgCol);
		buttonPlay.setFont(new Font(null, Font.BOLD, 20));
		buttonPlay.setPreferredSize(new Dimension(50, 50));

		buttonPause = new Button("II");
		buttonPause.setBackground(bgCol);
		buttonPause.setForeground(fgCol);
		buttonPause.setFont(new Font("Arial", Font.BOLD, 25));
		buttonPause.setPreferredSize(new Dimension(50, 50));
		char c = 183;
		/*
		 * for(char i = 0; i<500; i++) { int j = i; System.out.println(i + " " + j); }
		 */
		buttonStop = new Button("" + c);
		buttonStop.setBackground(bgCol);
		buttonStop.setForeground(fgCol);
		buttonStop.setFont(new Font(null, Font.BOLD, 120));
		buttonStop.setPreferredSize(new Dimension(50, 50));

		c = 174;
		buttonRecord = new Button("" + c);
		buttonRecord.setBackground(bgCol);
		buttonRecord.setForeground(fgCol);
		buttonRecord.setFont(new Font(null, Font.BOLD, 25));
		buttonRecord.setPreferredSize(new Dimension(50, 50));

	}

	public boolean savedComp = false, savedGlobaly = false;
	private Label statusLabel = new Label("Enter the name of the composition,", Label.CENTER);
	private Label statusLabel2 = new Label("chose format type and then save!", Label.CENTER);
	private void createSaveDialog() {

		dialog.setBounds(250, 250, 450, 200);
		dialog.setBackground(Color.GRAY);
		dialog.setFont(new Font(null, Font.BOLD, 14));
		savedComp = false;
		Button save = new Button("Save");
		save.setBackground(Color.PINK);
		
		save.addActionListener(l -> {
			if (!savedComp) {
				if (savingName.getText().length() != 0) {
					String title = savingName.getText();
					if (buttonRecord.isEnabled()) {
						if (myComposition != null) {
							if (txt.getState()) {
								TXTFormater txtF = new TXTFormater(myComposition);
								try {
									txtF.createTXTFile(title + ".txt");
								} catch (IOException e) {
								}
							}
							if (midi.getState()) {
								MIDIFormater midiF = new MIDIFormater(myComposition);
								try {
									midiF.createMIDIFile(title + ".midi");
								} catch (IOException | MidiUnavailableException | InvalidMidiDataException e) {
								}
							}
						}

						statusLabel.setText("Your composition is succsefuly saved!");
						statusLabel2.setText("");
						savedComp = true;
						savedGlobaly = true;
					} else {
						statusLabel.setText("You must stop recording first!");
						statusLabel2.setText("");
					}
				} else {
					statusLabel.setText("Enter the name first!");
					statusLabel2.setText("");
				}
			} else {
				statusLabel.setText("You already saved composition!");
				statusLabel2.setText("");
			}
		});
		txt = new Checkbox(".txt");
		midi = new Checkbox(".midi");

		txt.setForeground(Color.PINK);
		midi.setForeground(Color.PINK);
		savingName = new TextField();
		savingName.setPreferredSize(new Dimension(100, 25));
		savingName.setBackground(Color.PINK);
		Panel p = new Panel(new FlowLayout());
		statusLabel.setPreferredSize(new Dimension(300, 25));
		statusLabel2.setPreferredSize(new Dimension(300, 25));
		p.add(statusLabel);
		p.add(statusLabel2);
		p.add(new Label("Name of the composition"));
		p.add(savingName);
		Panel pp = new Panel(new GridLayout(2, 1));
		pp.add(txt);
		pp.add(midi);
		p.add(pp);
		p.add(save);
		dialog.add(p);
		dialog.setVisible(true);

	}

	private void playComposition(Composition composition) {

		if (pThread != null) {
			pThread.interrupt();
		}
		pThread = new PianoThread(composition, piano, this, vl);
		pThread.start();
		vl.setComposition(composition);
		vl.repaint();
	}

	public void resetButtons() {
		buttonPlay.setEnabled(true);
		buttonPause.setEnabled(false);
		buttonStop.setEnabled(false);
		vl.setComposition(loadedComposition);
	}

}
