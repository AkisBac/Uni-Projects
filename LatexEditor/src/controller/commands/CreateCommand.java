package controller.commands;

import controller.LatexEditorController;
import model.LatexDocumentManager;

public class CreateCommand implements Command {
	private LatexDocumentManager docManager;
	private LatexEditorController controller;
	
	
	public CreateCommand(LatexEditorController controller){
		docManager = new LatexDocumentManager("Sabbas","24/3/2019","Copyright","1.0");
		this.controller = controller;
	}

	@Override
	public void execute() {
		controller.setDoc(docManager.createDocument(controller.getUserSelectedTemplate()));
	}

}
