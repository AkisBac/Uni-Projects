package model.strategies;

import java.util.ArrayList;

import model.LatexDocument;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import controller.LatexEditorController;

public class StableVersionsStrategy implements VersionsStrategy {
	
	private File previousVersionFile;
	private FileWriter fileWriter;
	private Scanner previousVersionReader;
	private ArrayList<LatexDocument> history;
	private int historyCounter = 0;
	private LatexDocument doc;
	private String currentPath;
	private LatexEditorController controller;
	
	public StableVersionsStrategy(LatexEditorController controller){
		Path currPath = Paths.get("");
		currentPath = currPath.toAbsolutePath().toString();
		File directory = new File(currentPath + "\\history");
		if(!directory.exists()){
			if(!directory.mkdir()){
				System.out.println("Error");
			}
		}
		history = new ArrayList<LatexDocument>();
		this.controller = controller;
		
	}

	public void putVersion(LatexDocument doc) {
		String filename;
		
		determineHistoryCounter();
		try {
			this.doc = doc.clone();
			historyCounter ++;
			if(controller.getSaveFile() != null){
				filename = controller.getSaveFile().getName();
				int pos = filename.lastIndexOf(".");
				if (pos > 0 && pos < (filename.length() - 1)) {
				    filename = filename.substring(0, pos);
				}
				filename = filename + historyCounter + "" + "." + doc.getClass().getSimpleName();
			}else if (controller.getUserSelectedFile() != null){
				filename = controller.getUserSelectedFile().getName();
				int pos = filename.lastIndexOf(".");
				if (pos > 0 && pos < (filename.length() - 1)) {
				    filename = filename.substring(0, pos);
				}
				filename = filename + historyCounter + "" + "." + doc.getClass().getSimpleName();
			}else{
				filename = "PreviousVersion" + historyCounter + "" + "." + doc.getClass().getSimpleName();
			}
			previousVersionFile = new File(currentPath+"\\history\\" +filename);
			fileWriter = new FileWriter(previousVersionFile);
			fileWriter.write(doc.getContents());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public LatexDocument getVersion() {
		File history = new File(currentPath + "\\history");
		File lastFile;
		String template = controller.getUserSelectedTemplate();
		String extension;
		if (template.equals("")){
			extension = "EmptyDoc";
		}else{
			extension = template.substring(0, 1).toUpperCase() + template.substring(1) + "Template";
		}
		
		
		File[] listOfFiles = history.listFiles(new FilenameFilter() {
	        public boolean accept(File directory, String fileName) {
	        	if(controller.getSaveFile() != null){
	        		 return fileName.endsWith("." + extension) && fileName.startsWith(controller.getSaveFile().getName().substring(0, controller.getSaveFile().getName().lastIndexOf(".")));
	        	}else if (controller.getUserSelectedFile() != null){
	        		return fileName.endsWith("." + extension) && fileName.startsWith(controller.getUserSelectedFile().getName().substring(0, controller.getUserSelectedFile().getName().lastIndexOf(".")));
				}else{
	        		 return fileName.endsWith("." + extension);
	        	}
	        }
	    });
		if (listOfFiles.length == 0){
			return controller.getDoc();
		}else if (listOfFiles.length == 1){
			lastFile = new File(listOfFiles[listOfFiles.length-1].getAbsolutePath());
		}else{
			lastFile = new File(listOfFiles[listOfFiles.length-2].getAbsolutePath());
			File fileToDelete =  new File(listOfFiles[listOfFiles.length-1].getAbsolutePath());
			fileToDelete.delete();
		}
		
		String contents = "";
		
		try {
			previousVersionReader = new Scanner(lastFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		while(previousVersionReader.hasNextLine()){
			contents += previousVersionReader.nextLine()+ "\n";
		}
		contents = contents.substring(0, contents.length()-1);
		previousVersionReader.close();
		doc.setContents(contents);
		if (lastFile.exists()){
			lastFile.delete();
		}
		return doc;
	}

	public void setEntireHistory(ArrayList<LatexDocument> history) {
		for (int i = 0; i < history.size(); i++){
			putVersion(history.get(i));
		}
	}

	
	public ArrayList<LatexDocument> getEntireHistory() {
		File history = new File(currentPath + "\\history");
		String template = controller.getUserSelectedTemplate();
		String extension;
		if (template.equals("")){
			extension = "EmptyDoc";
		}else{
			extension = template.substring(0, 1).toUpperCase() + template.substring(1) + "Template";
		}
		File[] listOfFiles = history.listFiles(new FilenameFilter() {
	        public boolean accept(File directory, String fileName) {
	        	if(controller.getSaveFile() != null){
	        		 return fileName.endsWith("." + extension) && fileName.startsWith(controller.getSaveFile().getName().substring(0, controller.getSaveFile().getName().lastIndexOf(".")));
	        	}else if (controller.getUserSelectedFile() != null){
	        		return fileName.endsWith("." + extension) && fileName.startsWith(controller.getUserSelectedFile().getName().substring(0, controller.getUserSelectedFile().getName().lastIndexOf(".")));
				}else{
	        		 return fileName.endsWith("." + extension);
	        	}
	        }
	    });
		
		for (int i = 0; i < listOfFiles.length; i++){
			String contents = "";
			File currentFile = new File (listOfFiles[i].getAbsolutePath());
			LatexDocument currentDoc = doc.clone();
			try {
				previousVersionReader = new Scanner(currentFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			while(previousVersionReader.hasNextLine()){
				contents += previousVersionReader.nextLine() + "\n";
			}
			if (contents.equals("")){
				currentDoc.setContents(contents);
				this.history.add(currentDoc);
				previousVersionReader.close();
				continue;
			}
			contents = contents.substring(0, contents.length()-1);
			previousVersionReader.close();
			currentDoc.setContents(contents);
			this.history.add(currentDoc);
		}
		return this.history;
		
	}

	public void removeVersion(int index) {
		if(previousVersionFile.exists()){
			previousVersionFile.delete();
			try {
				previousVersionFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void determineHistoryCounter(){
		File history = new File(currentPath + "\\history");
		String template = controller.getUserSelectedTemplate();
		String extension;
		if (template.equals("")){
			extension = "EmptyDoc";
		}else{
			extension = template.substring(0, 1).toUpperCase() + template.substring(1) + "Template";
		}
		File[] listOfFiles = history.listFiles(new FilenameFilter() {
	        public boolean accept(File directory, String fileName) {
	        	if(controller.getSaveFile() != null){
	        		 return fileName.endsWith("." + extension) && fileName.startsWith(controller.getSaveFile().getName().substring(0, controller.getSaveFile().getName().lastIndexOf(".")));
	        	}else if (controller.getUserSelectedFile() != null){
	        		return fileName.endsWith("." + extension) && fileName.startsWith(controller.getUserSelectedFile().getName().substring(0, controller.getUserSelectedFile().getName().lastIndexOf(".")));
				}else{
	        		 return fileName.endsWith("." + extension);
	        	}
	           
	        }
	    });
		if(listOfFiles.length == 0){
			historyCounter = 0;
			return;
		}
		File lastFile = new File(listOfFiles[listOfFiles.length-1].getAbsolutePath());
		if(controller.getSaveFile() != null){
			historyCounter = Integer.parseInt(lastFile.getName().split("\\.")[0].split(controller.getSaveFile().getName().substring(0, controller.getSaveFile().getName().lastIndexOf(".")))[1]);
	   	}else if (controller.getUserSelectedFile() != null){
	   		historyCounter = Integer.parseInt(lastFile.getName().split("\\.")[0].split(controller.getUserSelectedFile().getName().substring(0, controller.getUserSelectedFile().getName().lastIndexOf(".")))[1]);
		}
		else{
	   		historyCounter = Integer.parseInt(lastFile.getName().split("\\.")[0].split("PreviousVersion")[1]);
	   		
	   	}

	}

}
