package test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import controller.LatexEditorController;

public class UserStory8and9Test {
	LatexEditorController controller = new LatexEditorController();

	@Test
	public void test() {
		controller.setUserSelectedTemplate("report");
		controller.enact("CreateCommand");
		Path currPath = Paths.get("");
		String contents = controller.getDoc().getContents();
		String currentPath = currPath.toAbsolutePath().toString();
		File fileToSave = new File(currentPath + "\\exampleFile.tex");
		controller.setSaveFile(fileToSave);
		controller.enact("SaveCommand");
		controller.setUserSelectedFile(fileToSave);
		controller.enact("LoadCommand");
		assertEquals("The contents of the loaded file should be the same with the contents of the file that was previously saved",contents,controller.getDoc().getContents());
	}

}
