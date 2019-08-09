package klavir;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class TXTFormater extends Formater {

	public TXTFormater(Composition composition) {
		super(composition);
		
		
	}
	
	public void createTXTFile(String title) throws IOException {
		BufferedWriter bufWri = new BufferedWriter(new FileWriter(title));
		Iterator<MusicSymbol> iterator = composition.iterator();
		boolean osmina = false;
		while(iterator.hasNext()) {
			MusicSymbol ms = iterator.next();
			if(ms.isNoteEight()) {
				if(!osmina) {
					bufWri.append('[');
					bufWri.append(ms.toString());
					osmina = true;
				}else {
					bufWri.append(' ');
					bufWri.append(ms.toString());
				}
			}else {
				if(osmina) {
					osmina = false;
					bufWri.append(']');
				}
				bufWri.append(ms.toString());
			}
		}
		bufWri.close();		
	}
	
	
	
	
	
}
