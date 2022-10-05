package model.templates;

import model.LatexDocument;

public class TemplateFactory {
	
	public LatexDocument createTemplate(String templateName,String author, String date, String copyright, String versionID){
		
		if(templateName.equals("book")){
			return new BookTemplate(author,date,copyright,versionID);
		}else if(templateName.equals("report")){
			return new ReportTemplate(author,date,copyright,versionID);
		}else if(templateName.equals("letter")){
			return new LetterTemplate(author,date,copyright,versionID);
		}else if(templateName.equals("article")){
			return new ArticleTemplate(author,date,copyright,versionID);
		}else{
			return new EmptyDoc(author,date,copyright,versionID);
		}
	}
}
