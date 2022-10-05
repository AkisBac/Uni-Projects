package model.templates;

import java.util.HashMap;
import java.util.Map;

import model.LatexDocument;


public class LetterTemplate  extends LatexDocument {
	private static String myContents = "\\documentclass{letter}\n\\usepackage{hyperref}\n\\signature{Sender's Name}\n\\address{Sender's address...}\n"
			+ "\\begin{document}\n\n\\begin{letter}{Destination address....}\n\\opening{Dear Sir or Madam:}\n\n"
			+ "I am writing to you .......\n\n\n\\closing{Yours Faithfully,}\n\n\\ps\n\nP.S. text .....\n\n"
			+ "\\encl{Copyright permission form}\n\n\\end{letter}\n\\end{document}";

	
	public LetterTemplate(String author, String dateModified, String copyright,
			String versionID) {
		
		super(author, dateModified, copyright, versionID, myContents);
		
	}
	
	public LetterTemplate() {
		super();
	}
	
	public Map<String, String> getLatexCommands(){
		return new HashMap<>();	
	}

	public LatexDocument clone() {
		LetterTemplate copy = new LetterTemplate();
		copy.setContents(this.getContents());
		copy.setAuthor(this.getAuthor());
		copy.setDate(this.getDate());
		copy.setVersionID(this.getVersionID());
		copy.setCopyright(this.getCopyright());
		return copy;
	}

}
