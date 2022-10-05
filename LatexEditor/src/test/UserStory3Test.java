package test;

import static org.junit.Assert.*;

import org.junit.Test;

import controller.LatexEditorController;

public class UserStory3Test {
	LatexEditorController controller = new LatexEditorController();

	@Test
	public void addChapterLatexCommandTest() {
		String addChapter = "\\chapter{...}\n";
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.setUserSelectedCommand("Add chapter");
		controller.setInsertedCommandPosition(0);
		controller.enact("AddLatexCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),addChapter);
	}
	
	@Test
	public void addSectionLatexCommandTest() {
		String addSection = "\\section{}\n";
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.setUserSelectedCommand("Add section");
		controller.setInsertedCommandPosition(0);
		controller.enact("AddLatexCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),addSection);
	}
	
	@Test
	public void addSubsectionLatexCommandTest() {
		String addSubsection = "\\subsection{}\n";
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.setUserSelectedCommand("Add subsection");
		controller.setInsertedCommandPosition(0);
		controller.enact("AddLatexCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),addSubsection);
	}
	
	@Test
	public void addSubSubsectionLatexCommandTest() {
		String addSubSubsection = "\\subsubsection{}\n";
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.setUserSelectedCommand("Add subsubsection");
		controller.setInsertedCommandPosition(0);
		controller.enact("AddLatexCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),addSubSubsection);
	}
	
	@Test
	public void addEnumerationListItemizeLatexCommandTest() {
		String addEnumerationListItemize = "\\begin{itemize}\n\n\\item ...\n\n\\item ...\n\n\\end{itemize}\n";
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.setUserSelectedCommand("Add enumeration list(itemize)");
		controller.setInsertedCommandPosition(0);
		controller.enact("AddLatexCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),addEnumerationListItemize);
	}
	
	@Test
	public void addEnumerationListLatexCommandTest() {
		String addEnumerationList = "\\begin{enumerate}\n\n\\item ...\n\n\\item ...\n\n\\end{enumerate}\n";
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.setUserSelectedCommand("Add enumeration list");
		controller.setInsertedCommandPosition(0);
		controller.enact("AddLatexCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),addEnumerationList);
	}
	
	@Test
	public void addTableLatexCommandTest() {
		String addTable = "\\begin{table}\\caption{....}\\label{...}\n\n\\begin{tabular}{|c|c|c|}\n\n  \\hline\n\n... &...&...\\\\\n\n... &...&...\\\\\n\n... &...&...\\\\\n\n  \\hline\n\n\\end{tabular}\n\n\\end{table}\n";
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.setUserSelectedCommand("Add table");
		controller.setInsertedCommandPosition(0);
		controller.enact("AddLatexCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),addTable);
	}
	
	@Test
	public void addGraphicsLatexCommandTest() {
		String addGraphics = "\\begin{figure}\n\n\\includegraphics[width=...,height=...]{...}\n\n\\caption{....}\\label{...}\n\n\\end{figure}\n\n";
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		controller.setUserSelectedCommand("Add graphics");
		controller.setInsertedCommandPosition(0);
		controller.enact("AddLatexCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),addGraphics);
	}

}
