package model.templates;

import model.LatexDocument;


public class BookTemplate extends LatexDocument {
	private static String myContents = "\\documentclass[11pt,a4paper]{book}\n\n"
			+ "\\usepackage{graphicx}\n\n"
			+ "\\begin{document}\n\n"
			+ "\\title{Book: How to Structure a LaTeX Document}\n"
			+ "\\author{Author1 \\and Author2 \\and ...}\n"
			+ "\\date{\\today}\n\\maketitle\n\n\\frontmatter\n\n"
			+ "\\chapter{Preface}\n\n\\mainmatter\n\n"
			+ "\\chapter{First Chapter}\n\n\\section{Section Title 1}\n\\section{Section Title 2}\n\\section{Section Title.....}\n\n\\chapter{....}\n\n\\chapter{Conclusion}"
			+ "\n\n\\chapter{Conclusion}\n\n\\chapter*{References}\n\n\\backmatter\n\n\\chapter{Last note}\n\n\\end{document}";
	
	
	public BookTemplate(String author, String dateModified, String copyright,
			String versionID) {

		super(author, dateModified, copyright, versionID, myContents);
	}


	public BookTemplate() {
		super();
	}


	@Override
	public LatexDocument clone() {
		BookTemplate copy = new BookTemplate();
		copy.setContents(this.getContents());
		copy.setAuthor(this.getAuthor());
		copy.setDate(this.getDate());
		copy.setVersionID(this.getVersionID());
		copy.setCopyright(this.getCopyright());
		return copy;
	}

}
