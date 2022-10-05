package test;

import static org.junit.Assert.*;
import org.junit.Test;
import controller.LatexEditorController;


public class UserStory1Test {
	LatexEditorController controller = new LatexEditorController();

	@Test
	public void ReportTest() {
		String reportContents = "\\documentclass[11pt,a4paper]{report}\n\n"
				+ "\\usepackage{graphicx}\n\n"
				+ "\\begin{document}\n\n"
				+ "\\title{Report Template: How to Structure a LaTeX Document}\n"
				+ "\\author{Author1 \\and Author2 \\and ...}\n"
				+ "\\date{\\today}\n\\maketitle\n\n"
				+ "\\begin{abstract}\nYour abstract goes here...\n...\n\\end{abstract}\n\n"
				+ "\\chapter{First Chapter}\n\n\\section{Section Title 1}\n\\section{Section Title 2}\n\\section{Section Title.....}\n\n\\chapter{....}\n\n\\chapter{Conclusion}"
				+ "\n\n\n\\chapter*{References}\n\n\\end{document}";
		controller.setUserSelectedTemplate("report");
		controller.enact("CreateCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),reportContents);
		
		
	}
	
	@Test
	public void BookTest() {
		String bookContents = "\\documentclass[11pt,a4paper]{book}\n\n"
				+ "\\usepackage{graphicx}\n\n"
				+ "\\begin{document}\n\n"
				+ "\\title{Book: How to Structure a LaTeX Document}\n"
				+ "\\author{Author1 \\and Author2 \\and ...}\n"
				+ "\\date{\\today}\n\\maketitle\n\n\\frontmatter\n\n"
				+ "\\chapter{Preface}\n\n\\mainmatter\n\n"
				+ "\\chapter{First Chapter}\n\n\\section{Section Title 1}\n\\section{Section Title 2}\n\\section{Section Title.....}\n\n\\chapter{....}\n\n\\chapter{Conclusion}"
				+ "\n\n\\chapter{Conclusion}\n\n\\chapter*{References}\n\n\\backmatter\n\n\\chapter{Last note}\n\n\\end{document}";
		controller.setUserSelectedTemplate("book");
		controller.enact("CreateCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),bookContents);
		
		
	}
	
	@Test
	public void ArticleTest() {
		String articleContents = "\\documentclass[11pt,twocolumn,a4paper]{article}\n\n"
				+ "\\usepackage{graphicx}\n\n"
				+ "\\begin{document}\n\n"
				+ "\\title{Article: How to Structure a LaTeX Document}\n"
				+ "\\author{Author1 \\and Author2 \\and ...}\n"
				+ "\\date{\\today}\n\\maketitle\n\n"
				+ "\\section{Section Title 1}\n\n\\section{Section Title 2}\n\n\\section{Section Title.....}\n\n\\section{Conclusion}\n\n\\section*{References}"
				+ "\n\n\\end{document}";
		controller.setUserSelectedTemplate("article");
		controller.enact("CreateCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),articleContents);
		
		
	}
	
	@Test
	public void LetterTest() {
		String letterContents = "\\documentclass{letter}\n\\usepackage{hyperref}\n\\signature{Sender's Name}\n\\address{Sender's address...}\n"
				+ "\\begin{document}\n\n\\begin{letter}{Destination address....}\n\\opening{Dear Sir or Madam:}\n\n"
				+ "I am writing to you .......\n\n\n\\closing{Yours Faithfully,}\n\n\\ps\n\nP.S. text .....\n\n"
				+ "\\encl{Copyright permission form}\n\n\\end{letter}\n\\end{document}";
		controller.setUserSelectedTemplate("letter");
		controller.enact("CreateCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),letterContents);
		
		
	}
	
	@Test
	public void EmptyDocTest() {
		String EmptyContents = "";
		controller.setUserSelectedTemplate("");
		controller.enact("CreateCommand");
		assertEquals("The contents should be the same",controller.getDoc().getContents(),EmptyContents);
		
		
	}

}
