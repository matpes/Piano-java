package klavir;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.sound.midi.*;


public class MIDIFormater extends Formater {

	public MIDIFormater(Composition composition) {
		super(composition);
	}
	
	public void createMIDIFile(String title) throws MidiUnavailableException, InvalidMidiDataException, IOException {
		
		Sequencer sequencer = MidiSystem.getSequencer();
		sequencer.open();
		Sequence sequence = new Sequence(Sequence.PPQ, 4);
		Track track = sequence.createTrack();
		Iterator<MusicSymbol> iterator = composition.iterator();
		int i = 0;
		while(iterator.hasNext()) {
			MusicSymbol ms = iterator.next();
			i+=ms.insertMidiSymbol(i, track);
		}
		File file = new File(title);
		MidiSystem.write(sequence, 1, file);
		sequencer.close();
	}

}
