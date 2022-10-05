package model;

import java.util.HashMap;
import java.util.Map;


public abstract class LatexDocument {
	private String author;
	private String date;
	private String copyright;
	private String versionID;
	private String contents;
	protected Map<String, String> LatexCommands = new HashMap<>();
	
	public LatexDocument(){
		initializeCommands();
	}
	
	public LatexDocument(String author, String date, String copyright, String versionID,String contents){
		this.author = author;
		this.date = date;
		this.copyright = copyright;
		this.versionID = versionID;
		this.contents = contents;
		initializeCommands();
	}
	
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public void setVersionID(String versionID){
		this.versionID = versionID;
	}
	
	public void setCopyright(String copyright){
		this.copyright = copyright;
	}
	
	public void setContents(String contents){
		this.contents = contents;
	}
	
	public void insertContent(int pos, String command){
		
		 StringBuilder stringContents = new StringBuilder(contents);
		 contents = stringContents.insert(pos, command).toString();
	}
	
	public String getAuthor(){
		return this.author;
	}
	
	public String getVersionID(){
		return this.versionID;
	}
	
	public String getCopyright(){
		return this.copyright;
	}
	
	public String getDate(){
		return this.date;
	}
	
	public String getContents(){
		return this.contents;
	}
	
	public Map<String, String> getLatexCommands(){
		return LatexCommands;	
	}
	
	private void initializeCommands(){
		LatexCommands.put("Add chapter", "\\chapter{...}\n");
		LatexCommands.put("Add section", "\\section{}\n");
		LatexCommands.put("Add subsection", "\\subsection{}\n");
		LatexCommands.put("Add subsubsection", "\\subsubsection{}\n");
		LatexCommands.put("Add enumeration list(itemize)", "\\begin{itemize}\n\n\\item ...\n\n\\item ...\n\n\\end{itemize}\n");
		LatexCommands.put("Add enumeration list", "\\begin{enumerate}\n\n\\item ...\n\n\\item ...\n\n\\end{enumerate}\n");
		LatexCommands.put("Add table", "\\begin{table}\\caption{....}\\label{...}\n\n\\begin{tabular}{|c|c|c|}\n\n  \\hline\n\n... &...&...\\\\\n\n... &...&...\\\\\n\n... &...&...\\\\\n\n  \\hline\n\n\\end{tabular}\n\n\\end{table}\n");
		LatexCommands.put("Add graphics", "\\begin{figure}\n\n\\includegraphics[width=...,height=...]{...}\n\n\\caption{....}\\label{...}\n\n\\end{figure}\n\n");
		
	}
	
	public abstract LatexDocument clone();

}
