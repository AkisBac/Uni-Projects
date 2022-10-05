package model.templates;

import model.LatexDocument;


public class ReportTemplate extends LatexDocument {
	private static  String myContents = "\\documentclass[11pt,a4paper]{report}\n\n"
			+ "\\usepackage{graphicx}\n\n"
			+ "\\begin{document}\n\n"
			+ "\\title{Report Template: How to Structure a LaTeX Document}\n"
			+ "\\author{Author1 \\and Author2 \\and ...}\n"
			+ "\\date{\\today}\n\\maketitle\n\n"
			+ "\\begin{abstract}\nYour abstract goes here...\n...\n\\end{abstract}\n\n"
			+ "\\chapter{First Chapter}\n\n\\section{Section Title 1}\n\\section{Section Title 2}\n\\section{Section Title.....}\n\n\\chapter{....}\n\n\\chapter{Conclusion}"
			+ "\n\n\n\\chapter*{References}\n\n\\end{document}";

	public ReportTemplate(String author, String dateModified, String copyright,
			String versionID) {
		
		super(author, dateModified, copyright, versionID, myContents);
	}

	
	public ReportTemplate() {
		super();
	}


	public LatexDocument clone() {
		ReportTemplate copy = new ReportTemplate();
		copy.setContents(this.getContents());
		copy.setAuthor(this.getAuthor());
		copy.setDate(this.getDate());
		copy.setVersionID(this.getVersionID());
		copy.setCopyright(this.getCopyright());
		return copy;
	}

}
