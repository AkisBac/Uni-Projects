package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import model.LatexDocument;

import org.junit.Test;

import controller.LatexEditorController;

public class UserStory5Test {
	LatexEditorController controller = new LatexEditorController();

	@Test
	public void testVolatileToStable() {
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.enact("EnableVersionsManagementCommand");
		controller.setEditContents("Hello World");
		controller.enact("EditCommand");
		controller.setUserSelectedVersionStrategy("stable");
		controller.enact("ChangeVersionsStrategyCommand");
		Path currPath = Paths.get("");
		String currentPath = currPath.toAbsolutePath().toString();
		File history = new File(currentPath + "\\history");
		String template = controller.getUserSelectedTemplate();
		String extension;
		if (template.equals("")){
			extension = "EmptyDoc";
		}else{
			extension = template.substring(0, 1).toUpperCase() + template.substring(1) + "Template";
		}
		File[] listOfFiles = history.listFiles(new FilenameFilter() {
	        public boolean accept(File directory, String fileName) {
	            return fileName.endsWith("." + extension);
	        }
	    });
		assertEquals("2 Files must be created from the change of version strategy",listOfFiles.length,2);
		for (int i = 0; i < listOfFiles.length; i++){
			File currentFile = new File (listOfFiles[i].getAbsolutePath());
			currentFile.delete();
		}
	}
	
	@Test
	public void testStableToVolatile() {
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.enact("EnableVersionsManagementCommand");
		controller.setEditContents("Hello World");
		controller.enact("EditCommand");
		ArrayList<LatexDocument> history = controller.getVersionManager().getStrategy().getEntireHistory();
		controller.setUserSelectedVersionStrategy("stable");
		controller.enact("ChangeVersionsStrategyCommand");
		controller.setUserSelectedVersionStrategy("volatile");
		controller.enact("ChangeVersionsStrategyCommand");
		ArrayList<LatexDocument> newHistory = controller.getVersionManager().getStrategy().getEntireHistory();
		for (int i = 0; i < history.size(); i++){
			assertEquals("The contents should be the same",newHistory.get(i).getContents(),history.get(i).getContents());
		}
		String template = controller.getUserSelectedTemplate();
		Path currPath = Paths.get("");
		String currentPath = currPath.toAbsolutePath().toString();
		File historyDirectory = new File(currentPath + "\\history");
		String extension;
		if (template.equals("")){
			extension = "EmptyDoc";
		}else{
			extension = template.substring(0, 1).toUpperCase() + template.substring(1) + "Template";
		}
		File[] listOfFiles = historyDirectory.listFiles(new FilenameFilter() {
	        public boolean accept(File directory, String fileName) {
	            return fileName.endsWith("." + extension);
	        }
	    });
		for (int i = 0; i < listOfFiles.length; i++){
			File currentFile = new File (listOfFiles[i].getAbsolutePath());
			currentFile.delete();
		}
		
	}

}
