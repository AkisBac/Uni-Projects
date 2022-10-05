package test;

import static org.junit.Assert.*;

import org.junit.Test;

import controller.LatexEditorController;

public class UserStory2Test {
	LatexEditorController controller = new LatexEditorController();

	@Test
	public void EditContentsTest() {
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		String newContents = "Hello World";
		controller.setEditContents(newContents);
		controller.enact("EditCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),newContents);
	}

}
