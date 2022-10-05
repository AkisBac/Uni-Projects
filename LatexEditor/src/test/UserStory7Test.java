package test;

import static org.junit.Assert.*;

import org.junit.Test;

import controller.LatexEditorController;

public class UserStory7Test {
	LatexEditorController controller = new LatexEditorController();

	@Test
	public void test() {
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.enact("EnableVersionsManagementCommand");
		controller.setEditContents("Hello World");
		controller.enact("EditCommand");
		controller.setEditContents("Hello Planet");
		controller.enact("EditCommand");
		controller.enact("RollbackToPreviousVersionCommand");
		assertEquals("Contents should be the same",controller.getDoc().getContents(),"Hello World");
	}

}
