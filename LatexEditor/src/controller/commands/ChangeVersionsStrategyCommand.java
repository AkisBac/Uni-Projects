package controller.commands;


import model.strategies.VersionsStrategy;
import model.strategies.VersionsStrategyFactory;
import controller.LatexEditorController;

public class ChangeVersionsStrategyCommand implements Command {
	
	private LatexEditorController controller;
	private VersionsStrategyFactory versionsFactory;
	
	public ChangeVersionsStrategyCommand(LatexEditorController controller){
		this.controller = controller;
		versionsFactory = new VersionsStrategyFactory();
	}

	public void execute() {
		
		VersionsStrategy previousStrategy = controller.getVersionManager().getStrategy();
		controller.getVersionManager().setStrategy(versionsFactory.createStrategy(controller.getUserSelectedVersionStrategy(),controller));
		controller.getVersionManager().getStrategy().setEntireHistory(previousStrategy.getEntireHistory());
		
	}

}
