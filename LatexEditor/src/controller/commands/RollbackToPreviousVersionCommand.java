package controller.commands;

import controller.LatexEditorController;

public class RollbackToPreviousVersionCommand implements Command{
	private LatexEditorController controller;

	public RollbackToPreviousVersionCommand(LatexEditorController controller){
		this.controller = controller;
	}
	
	
	public void execute() {
		controller.setDoc(controller.getVersionManager().getPreviousVersion());
		
	}

}
