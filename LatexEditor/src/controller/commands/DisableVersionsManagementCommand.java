package controller.commands;

import controller.LatexEditorController;

public class DisableVersionsManagementCommand implements Command{
	private LatexEditorController controller;
	
	public DisableVersionsManagementCommand (LatexEditorController controller){
		this.controller = controller;
	}

	public void execute() {
		controller.getVersionManager().disable();
	}
}
