package controller.commands;

import controller.LatexEditorController;

public class CommandsFactory {
	private LatexEditorController controller;
	
	public CommandsFactory(LatexEditorController controller){
		this.controller = controller;
	}
	
	public Command createCommands(String command){
		
		if(command.equals("CreateCommand")){
			return new CreateCommand(controller);
		}else if(command.equals("EditCommand")){
			return new EditCommand(controller);
		}else if (command.equals("AddLatexCommand")){
			return new AddLatexCommand(controller);
		}else if (command.equals("EnableVersionsManagementCommand")){
			return new EnableVersionsManagementCommand(controller);
		}else if (command.equals("ChangeVersionsStrategyCommand")){
			return new ChangeVersionsStrategyCommand(controller);
		}else if (command.equals("DisableVersionsManagementCommand")){
			return new DisableVersionsManagementCommand(controller);
		}else if (command.equals("RollbackToPreviousVersionCommand")){
			return new RollbackToPreviousVersionCommand(controller);
		}else if (command.equals("SaveCommand")){
			return new SaveCommand(controller);
		}else if (command.equals("LoadCommand")){
			return new LoadCommand(controller);
		}
		return null;
	}
}
