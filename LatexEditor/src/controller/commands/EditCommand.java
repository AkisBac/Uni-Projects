package controller.commands;

import controller.LatexEditorController;

public class EditCommand implements Command{
	private LatexEditorController controller;
	
	public EditCommand (LatexEditorController controller){
		this.controller = controller;
	}

	@Override
	public void execute() {
		if(controller.getVersionManager().isEnabled()){
			controller.getDoc().setContents(controller.getEditContents());
			controller.getVersionManager().setCurrentVersion(controller.getDoc());
		}else{
			controller.getDoc().setContents(controller.getEditContents());
		}
		
	}

}
