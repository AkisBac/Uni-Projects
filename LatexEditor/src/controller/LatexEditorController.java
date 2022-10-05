package controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.LatexDocument;
import model.VersionsManager;
import controller.commands.Command;
import controller.commands.CommandsFactory;


public class LatexEditorController {
	
	private CommandsFactory factory;
	private final Map<String, Command> commands = new HashMap<>();
	private String userSelectedTemplate;
	private String userSelectedCommand;
	private LatexDocument doc;
	private String changedContents;
	private int insertPosition;
	private VersionsManager versionManager;
	private String userSelectedVersionStrategy;
	private File fileToLoad;
	private File fileToSave;
	
	
	public LatexEditorController() {
		
		factory = new CommandsFactory(this);
		commands.put("CreateCommand", factory.createCommands("CreateCommand"));
		commands.put("EditCommand", factory.createCommands("EditCommand"));
		commands.put("AddLatexCommand", factory.createCommands("AddLatexCommand"));
		commands.put("EnableVersionsManagementCommand", factory.createCommands("EnableVersionsManagementCommand"));
		commands.put("ChangeVersionsStrategyCommand", factory.createCommands("ChangeVersionsStrategyCommand"));
		commands.put("DisableVersionsManagementCommand", factory.createCommands("DisableVersionsManagementCommand"));
		commands.put("RollbackToPreviousVersionCommand", factory.createCommands("RollbackToPreviousVersionCommand"));
		commands.put("SaveCommand", factory.createCommands("SaveCommand"));
		commands.put("LoadCommand", factory.createCommands("LoadCommand"));
		versionManager = new VersionsManager();
	}
	
	public void enact(String key){
		commands.get(key).execute();
	}
	
	public void setUserSelectedTemplate(String template){
		userSelectedTemplate = template;
	}
	
	public String getUserSelectedTemplate(){
		return userSelectedTemplate;
	}
	
	public void setDoc(LatexDocument doc){
		this.doc = doc;
	}
	
	public LatexDocument getDoc(){
		return doc;
	}
	
	public VersionsManager getVersionManager(){
		return versionManager;
	}
	
	public void setUserSelectedCommand(String latexCommand){
		userSelectedCommand = latexCommand;
	}
	
	public String getUserSelectedCommand(){
		return doc.getLatexCommands().get(userSelectedCommand);
	}
	
	public int getInsertedCommandPosition(){
		return insertPosition;
	}
	
	public void setInsertedCommandPosition(int pos){
		insertPosition = pos;
	}
	
	public void setEditContents(String changedContents){
		this.changedContents = changedContents;
	}
	
	public String getEditContents(){
		return changedContents;
	}
	
	public void setSaveFile(File file){
		fileToSave = file;
	}
	
	public File getSaveFile(){
		return fileToSave;
	}
	
	public void setUserSelectedFile(File file){
		fileToLoad = file;
	}
	
	public File getUserSelectedFile(){
		return fileToLoad;
	}
	
	public String getUserSelectedVersionStrategy(){
		return userSelectedVersionStrategy;
	}
	
	public void setUserSelectedVersionStrategy(String strategy){
		userSelectedVersionStrategy = strategy;
	}

}
