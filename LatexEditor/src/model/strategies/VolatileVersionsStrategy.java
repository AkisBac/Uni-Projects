package model.strategies;

import java.util.ArrayList;
import model.LatexDocument;

public class VolatileVersionsStrategy implements VersionsStrategy {
	
	private int historyCounter = 0;
	private ArrayList<LatexDocument> history;

	public VolatileVersionsStrategy(){
		history = new ArrayList<LatexDocument>();
	}
	
	public void putVersion(LatexDocument doc) {
		LatexDocument currdoc = doc.clone();
		if(historyCounter == 30){
			historyCounter = 0;
			removeVersion(historyCounter);
		}
		history.add(currdoc);
		historyCounter ++;
	}

	
	public LatexDocument getVersion() {

		if (historyCounter - 1 > 0) {
			historyCounter -= 1;
			removeVersion(historyCounter);
		}
		LatexDocument docToReturn = history.get(historyCounter-1).clone();
		return  docToReturn;
	}

	
	public void setEntireHistory(ArrayList<LatexDocument> history) {
		this.history = history;
		historyCounter = history.size();
	}

	
	public ArrayList<LatexDocument> getEntireHistory() {
		return history;
	}

	
	public void removeVersion(int index) {
		history.remove(index);
	}

}
