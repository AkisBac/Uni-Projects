package controller.commands;

import controller.LatexEditorController;

public class EnableVersionsManagementCommand implements Command{
	

	private LatexEditorController controller;
	
	public EnableVersionsManagementCommand (LatexEditorController controller){
		this.controller = controller;
	}


	public void execute() {
		controller.getVersionManager().enable();
		controller.getVersionManager().setCurrentVersion(controller.getDoc());	
	}

}
