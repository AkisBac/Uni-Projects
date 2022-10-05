package model;

import model.strategies.VersionsStrategy;
import model.strategies.VersionsStrategyFactory;

public class VersionsManager {
	
	private boolean isEnabled = false;
	private VersionsStrategyFactory versionsFactory;
	private VersionsStrategy currentStrategy;
	private LatexDocument currentDoc;
	
	public VersionsManager (){
		versionsFactory = new VersionsStrategyFactory();
	}
	
	public void enable(){
		isEnabled = true;
		currentStrategy = versionsFactory.createStrategy("volatile", null);
	}
	
	public void disable(){
		isEnabled = false;
		currentDoc = null;
		//currentStrategy = null;
	}
	
	public boolean isEnabled(){
		return isEnabled;
	}
	
	public void setStrategy(VersionsStrategy strategy){
		currentStrategy = strategy;
	}
	
	public VersionsStrategy getStrategy(){
		return currentStrategy;
	}
	
	public void setCurrentVersion(LatexDocument doc){
		if(isEnabled){
			currentDoc = doc;
			currentStrategy.putVersion(currentDoc);
		}
	}
	
	public LatexDocument getPreviousVersion(){
		if(isEnabled){
			return currentStrategy.getVersion();
		}else{
			return null;
		}
		
	}
}
