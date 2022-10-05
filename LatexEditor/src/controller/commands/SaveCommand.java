package controller.commands;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import controller.LatexEditorController;

public class SaveCommand implements Command{
	private LatexEditorController controller;
	private FileWriter fileWriter;

	public SaveCommand(LatexEditorController controller){
		this.controller = controller;
	}
	
	
	
	public void execute() {
		try {
			fileWriter = new FileWriter(controller.getSaveFile());
			String template = controller.getUserSelectedTemplate();
			String extension;
			if (template.equals("")){
				extension = "EmptyDoc";
			}else{
				extension = template.substring(0, 1).toUpperCase() + template.substring(1) + "Template";
			}
			Path currPath = Paths.get("");
			String currentPath = currPath.toAbsolutePath().toString();
			File history = new File(currentPath + "\\history");

			File[] listOfFiles = history.listFiles(new FilenameFilter() {
		        public boolean accept(File directory, String fileName) {
		            return fileName.endsWith("." + extension) && fileName.startsWith("PreviousVersion");
		        }
		    });
			for (int i = 0; i < listOfFiles.length; i++){
				File currentFile = new File (listOfFiles[i].getAbsolutePath());
				currentFile.delete();
			}
			fileWriter.write(controller.getDoc().getContents());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
