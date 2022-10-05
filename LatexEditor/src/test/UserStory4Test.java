package test;

import static org.junit.Assert.*;
import model.strategies.VersionsStrategyFactory;
import org.junit.Test;
import controller.LatexEditorController;

public class UserStory4Test {
	LatexEditorController controller = new LatexEditorController();
	VersionsStrategyFactory factory = new VersionsStrategyFactory();

	@Test
	public void stableStrategyTest() {
		controller.setUserSelectedTemplate("report");
		controller.enact("CreateCommand");
		String contentsBeforeEdit = controller.getDoc().getContents();
		controller.getVersionManager().setStrategy(factory.createStrategy("stable",controller));
		controller.enact("EnableVersionsManagementCommand");
		controller.enact("CreateCommand");
		String newContents = "Hello world";
		controller.setEditContents(newContents);
		controller.enact("EditCommand");
		assertEquals("The contents should be the same",controller.getVersionManager().getPreviousVersion().getContents(),contentsBeforeEdit);
	}
	
	@Test
	public void volatileStrategyTest() {
		controller.setUserSelectedTemplate("report");
		controller.enact("CreateCommand");
		String contentsBeforeEdit = controller.getDoc().getContents();
		controller.enact("EnableVersionsManagementCommand");
		controller.enact("CreateCommand");
		String newContents = "Hello World";
		controller.setEditContents(newContents);
		controller.enact("EditCommand");
		assertEquals("The contents should be the same",controller.getVersionManager().getPreviousVersion().getContents(),contentsBeforeEdit);
	}

}
