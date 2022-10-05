package model.templates;

import java.util.Map;

import model.LatexDocument;


public class ArticleTemplate  extends LatexDocument {
	private static String myContents = "\\documentclass[11pt,twocolumn,a4paper]{article}\n\n"
			+ "\\usepackage{graphicx}\n\n"
			+ "\\begin{document}\n\n"
			+ "\\title{Article: How to Structure a LaTeX Document}\n"
			+ "\\author{Author1 \\and Author2 \\and ...}\n"
			+ "\\date{\\today}\n\\maketitle\n\n"
			+ "\\section{Section Title 1}\n\n\\section{Section Title 2}\n\n\\section{Section Title.....}\n\n\\section{Conclusion}\n\n\\section*{References}"
			+ "\n\n\\end{document}";


	public ArticleTemplate(String author, String dateModified, String copyright,
			String versionID){
		
		super(author, dateModified, copyright, versionID, myContents);
		
		
	}

	public ArticleTemplate() {
		super();
	}
	
	public Map<String, String> getLatexCommands(){
		LatexCommands.remove("Add chapter");
		return LatexCommands;	
	}
	
	

	public LatexDocument clone() {
		ArticleTemplate copy = new ArticleTemplate();
		copy.setContents(this.getContents());
		copy.setAuthor(this.getAuthor());
		copy.setDate(this.getDate());
		copy.setVersionID(this.getVersionID());
		copy.setCopyright(this.getCopyright());
		return copy;
	}

}
