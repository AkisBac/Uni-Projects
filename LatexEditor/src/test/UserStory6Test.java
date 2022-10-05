package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import model.LatexDocument;

import org.junit.Test;

import controller.LatexEditorController;

public class UserStory6Test {
	LatexEditorController controller = new LatexEditorController();
	ArrayList<LatexDocument> history = new ArrayList<LatexDocument>();

	@Test
	public void test() {
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.enact("EnableVersionsManagementCommand");
		history = controller.getVersionManager().getStrategy().getEntireHistory();
		controller.enact("DisableVersionsManagementCommand");
		controller.setEditContents("Hello World");
		controller.enact("EditCommand");
		ArrayList<LatexDocument> newHistory = controller.getVersionManager().getStrategy().getEntireHistory();
		assertEquals("The history should remain the same",history,newHistory);
		assertEquals("The VersionManager is disabled",controller.getVersionManager().isEnabled(),false);
	}

}
