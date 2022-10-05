package model;

import java.util.HashMap;
import java.util.Map;

import model.templates.TemplateFactory;

public class LatexDocumentManager {
	private final Map<String, LatexDocument> prototypes = new HashMap<>();
	private TemplateFactory factory = new TemplateFactory();
	
	public LatexDocumentManager(String author, String date, String copyright, String versionID) {
		prototypes.put("article",factory.createTemplate("article", author, date, copyright, versionID));
		prototypes.put("book", factory.createTemplate("book", author, date, copyright, versionID));
		prototypes.put("letter", factory.createTemplate("letter", author, date, copyright, versionID));
		prototypes.put("report",factory.createTemplate("report", author, date, copyright, versionID));
		prototypes.put("",factory.createTemplate("", author, date, copyright, versionID));
	}
	
	public LatexDocument createDocument(String type){
		return prototypes.get(type).clone();
	}
	
}
