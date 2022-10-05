package controller.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import controller.LatexEditorController;

public class LoadCommand implements Command{
	private LatexEditorController controller;
	File fileToLoad;
	private Scanner fileReader;
	private String contents = "";

	public LoadCommand(LatexEditorController controller){
		this.controller = controller;
		
	}
	
	private String determineTemplate(){
		contents += fileReader.nextLine() + "\n";
		String template = contents.split("\\{")[1];
		if(template == null){
			return "";
		}
		return template.substring(0, template.length()-2);
	}
	
	public void execute() {
		fileToLoad = controller.getUserSelectedFile();
		try {
			fileReader = new Scanner(fileToLoad);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		controller.setUserSelectedTemplate(determineTemplate());
		controller.enact("CreateCommand");
		while(fileReader.hasNextLine()){
			contents += fileReader.nextLine()+ "\n";
		}
		contents = contents.substring(0, contents.length()-1);
		fileReader.close();
		controller.getDoc().setContents(contents);
	}

}
