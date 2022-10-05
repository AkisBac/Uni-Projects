package controller.commands;

import controller.LatexEditorController;

public class AddLatexCommand implements Command {
	
	private LatexEditorController controller;

	
	public AddLatexCommand(LatexEditorController controller){
		this.controller = controller;
	}
	
	@Override
	public void execute() {
		controller.getDoc().insertContent(controller.getInsertedCommandPosition(), controller.getUserSelectedCommand());
	}

}
