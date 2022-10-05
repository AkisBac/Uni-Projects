package model.strategies;

import controller.LatexEditorController;

public class VersionsStrategyFactory {
	
	public VersionsStrategy createStrategy(String version,  LatexEditorController controller){
		if(version.equals("stable")){
			return new StableVersionsStrategy(controller);
		}
		return new VolatileVersionsStrategy();
	}
}
