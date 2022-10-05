package model.strategies;

import java.util.ArrayList;

import model.LatexDocument;

public interface VersionsStrategy {
	public abstract void putVersion(LatexDocument doc);
	public abstract LatexDocument getVersion();
	public abstract void setEntireHistory(ArrayList <LatexDocument> history);
	public abstract ArrayList <LatexDocument> getEntireHistory();
	public abstract void removeVersion(int index);
}
