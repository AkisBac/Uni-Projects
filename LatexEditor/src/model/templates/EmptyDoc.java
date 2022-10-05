package model.templates;

import model.LatexDocument;

public class EmptyDoc extends LatexDocument {
	
	private static String myContents = "";

	public EmptyDoc(String author, String dateModified, String copyright,
			String versionID) {
		
		super(author, dateModified, copyright, versionID, myContents);
	}

	
	public EmptyDoc() {
		super();
	}


	@Override
	public LatexDocument clone() {
		EmptyDoc copy = new EmptyDoc();
		copy.setContents(this.getContents());
		copy.setAuthor(this.getAuthor());
		copy.setDate(this.getDate());
		copy.setVersionID(this.getVersionID());
		copy.setCopyright(this.getCopyright());
		return copy;
	}

}
