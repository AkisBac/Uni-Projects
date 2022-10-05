package view;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.CardLayout;
import java.awt.Toolkit;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JRadioButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JScrollPane;

import controller.LatexEditorController;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JEditorPane;

import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;



public class LatexEditorGUI {
	
	private LatexEditorController controller = new LatexEditorController();
	private String template;
	private int elementsChanged = 0;
	private JFrame frame;
	
	
	public JFrame getFrame(){
		return frame;
	}
	
	private boolean determineFileExtensions(String path){
		String[] format=path.split("\\.");
		if (!format[1].equals("tex")){
			return false;
		}else{	         
	        return true;
		}
	}
	
	
	private void initMenu(JScrollPane pane,JEditorPane editorPane){
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		

		JMenuItem saveMenuItem = new JMenuItem("Save");
		JMenuItem rollBackMenuItem = new JMenuItem("Rollback To Previous Version");
		fileMenu.add(saveMenuItem);
		fileMenu.add(rollBackMenuItem);
		pane.setColumnHeaderView(menuBar);
		
		JMenu actionMenu = new JMenu("Actions");
		JMenu addLatexCommandMenu = new JMenu("Add a Latex command");
		JCheckBoxMenuItem versionManager = new JCheckBoxMenuItem("Enable VersionManager");
		
		
		
		JMenu storageStrategy = new JMenu("Storage Strategy");
		JCheckBoxMenuItem volatileStrategy = new JCheckBoxMenuItem("Volatile");
		volatileStrategy.setSelected(true);
		JCheckBoxMenuItem stableStrategy = new JCheckBoxMenuItem("Stable");
		
		volatileStrategy.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(stableStrategy.isSelected()){
					stableStrategy.setSelected(false);
					controller.setUserSelectedVersionStrategy("volatile");
					controller.enact("ChangeVersionsStrategyCommand");
				}else{
					volatileStrategy.setSelected(true);
				}
				
			}
		});
		
		saveMenuItem.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				File file;
				fc.showSaveDialog(frame);	
				
				if(fc.getSelectedFile().getName().contains("tex")){
						file = new File(fc.getSelectedFile().getPath());
				}else{
						file = new File(fc.getSelectedFile().getPath()+".tex");
				}
				fc.setFileFilter(new FileNameExtensionFilter("*.tex", "tex"));
				controller.setSaveFile(file);
				controller.setEditContents(editorPane.getText());
				controller.enact("EditCommand");
				controller.enact("SaveCommand");
			}
			
		});
		
		
		
		stableStrategy.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(volatileStrategy.isSelected()){
					volatileStrategy.setSelected(false);
					controller.setUserSelectedVersionStrategy("stable");
					controller.enact("ChangeVersionsStrategyCommand");
				}else{
					volatileStrategy.setSelected(true);
				}
				
			}
		});
		
		storageStrategy.add(volatileStrategy);
		storageStrategy.add(stableStrategy);
		
		DocumentListener docListener = new DocumentListener(){

			@Override
			public void insertUpdate(DocumentEvent e) {
				elementsChanged += e.getLength();
				if(elementsChanged > 5){
					elementsChanged = 0;
					controller.setEditContents(editorPane.getText());
					controller.enact("EditCommand");
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				elementsChanged += e.getLength();
				if(elementsChanged > 5){
					elementsChanged = 0;
					controller.setEditContents(editorPane.getText());
					controller.enact("EditCommand");
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				return;
			}
		};
		
		rollBackMenuItem.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(!controller.getVersionManager().isEnabled()){
					JOptionPane.showMessageDialog(null,"Version Management System is not enabled ","Error",JOptionPane.ERROR_MESSAGE);
				}else{
					editorPane.getDocument().removeDocumentListener(docListener);
					controller.enact("RollbackToPreviousVersionCommand");
					editorPane.setText(controller.getDoc().getContents());
					editorPane.getDocument().addDocumentListener(docListener);
				}
			}
			
		});
		
		versionManager.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(versionManager.isSelected()){
					editorPane.getDocument().addDocumentListener(docListener);
					controller.enact("EnableVersionsManagementCommand");
					actionMenu.add(storageStrategy);
				}else{
					editorPane.getDocument().removeDocumentListener(docListener);
					controller.enact("DisableVersionsManagementCommand");
					actionMenu.remove(storageStrategy);
				}
			}
		});
		
		actionMenu.add(versionManager);
		
		Iterator<String> latexCommandsKeys = controller.getDoc().getLatexCommands().keySet().iterator();
		
		while(latexCommandsKeys.hasNext()){
			String LatexCommand = latexCommandsKeys.next();
			JMenuItem menuItem = new JMenuItem(LatexCommand);
			menuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					editorPane.getDocument().removeDocumentListener(docListener);
					controller.setUserSelectedCommand(LatexCommand);
					controller.setInsertedCommandPosition(editorPane.getCaretPosition());
					int caretPosition = editorPane.getCaretPosition();
					controller.enact("AddLatexCommand");
					editorPane.setText(controller.getDoc().getContents());
					controller.setEditContents(editorPane.getText());
					controller.enact("EditCommand");
					editorPane.setCaretPosition(caretPosition + controller.getUserSelectedCommand().length()-1);
					editorPane.getDocument().addDocumentListener(docListener);
				}
			});
			
			addLatexCommandMenu.add(menuItem);
		}
		actionMenu.add(addLatexCommandMenu);
		
		menuBar.add(fileMenu);
		menuBar.add(actionMenu);
	}
	
	
	private void initListeners(JRadioButton firstTemplate,JRadioButton secondTemplate,JRadioButton thirdTemplate,JRadioButton fourthTemplate){
		addActionListenerToRadioButtons(firstTemplate,secondTemplate,thirdTemplate,fourthTemplate);
		addActionListenerToRadioButtons(secondTemplate,firstTemplate,thirdTemplate,fourthTemplate);
		addActionListenerToRadioButtons(thirdTemplate,firstTemplate,secondTemplate,fourthTemplate);
		addActionListenerToRadioButtons(fourthTemplate,firstTemplate,secondTemplate,thirdTemplate);
	}
	
	private void addActionListenerToRadioButtons(JRadioButton selectedTemplate,JRadioButton firstDisabledTemplate,JRadioButton secondDisabledTemplate,JRadioButton thirdDisabledTemplate){
		selectedTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				firstDisabledTemplate.setSelected(false);
				secondDisabledTemplate.setSelected(false);
				thirdDisabledTemplate.setSelected(false);
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LatexEditorGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setBounds(100, 100, 720, 540);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-frame.getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-frame.getHeight()/2);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		
		JPanel Home = new JPanel();
		frame.getContentPane().add(Home, "name_7877270626213");
		Home.setLayout(null);
		
		JButton createLatexDocButton = new JButton("Create Latex Doc");
		createLatexDocButton.setBounds(121, 267, 137, 23);
		Home.add(createLatexDocButton);
		
		JButton loadLatexDocButton = new JButton("Load Latex Doc");
		loadLatexDocButton.setBounds(461, 267, 137, 23);
		Home.add(loadLatexDocButton);
		Home.setVisible(true);
		
		JPanel CreateLatexBasedOnTemplate = new JPanel();
		frame.getContentPane().add(CreateLatexBasedOnTemplate, "name_7877291619380");
		
		
		
		createLatexDocButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Home.setVisible(false);
				CreateLatexBasedOnTemplate.setVisible(true);
			}
		});
		CreateLatexBasedOnTemplate.setLayout(null);
		
		JLabel lblChooseOneOf = new JLabel("Please choose one of the templates below for your Latex Document:");
		lblChooseOneOf.setBounds(110, 34, 544, 17);
		lblChooseOneOf.setFont(new Font("Arial", Font.PLAIN, 17));
		CreateLatexBasedOnTemplate.add(lblChooseOneOf);
		
		JRadioButton reportTemplateSelected = new JRadioButton(" Report Template");
		reportTemplateSelected.setBounds(110, 103, 146, 23);
		reportTemplateSelected.setFocusPainted(false);
		CreateLatexBasedOnTemplate.add(reportTemplateSelected);
		
		JRadioButton bookTemplateSelected = new JRadioButton(" Book Template");
		bookTemplateSelected.setBounds(110, 173, 165, 23);
		bookTemplateSelected.setFocusPainted(false);
		CreateLatexBasedOnTemplate.add(bookTemplateSelected);
		
		JRadioButton letterTemplateSelected = new JRadioButton(" Letter Template");
		letterTemplateSelected.setBounds(110, 313, 165, 23);
		letterTemplateSelected.setFocusPainted(false);
		CreateLatexBasedOnTemplate.add(letterTemplateSelected);
		
		JButton createLatexNextButton = new JButton("Next");
		createLatexNextButton.setBounds(451, 406, 89, 23);
		CreateLatexBasedOnTemplate.add(createLatexNextButton);
		
		JButton createLatexBackButton = new JButton("Back");
		createLatexBackButton.setBounds(167, 406, 89, 23);
		createLatexBackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Home.setVisible(true);
				CreateLatexBasedOnTemplate.setVisible(false);
			}
		});
		CreateLatexBasedOnTemplate.add(createLatexBackButton);
		
		
		JRadioButton articleTemplateSelected = new JRadioButton(" Article Template");
		articleTemplateSelected.setBounds(110, 243, 165, 23);
		articleTemplateSelected.setFocusPainted(false);
		CreateLatexBasedOnTemplate.add(articleTemplateSelected);
		
		initListeners(reportTemplateSelected,bookTemplateSelected,letterTemplateSelected,articleTemplateSelected);
		
		JPanel panel_1 = new JPanel();
		frame.getContentPane().add(panel_1, "name_36422975876141");
		panel_1.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(null);
		scrollPane_2.setBounds(0, 0, 934, 640);
		panel_1.add(scrollPane_2);
		
		JEditorPane editorPane_2 = new JEditorPane();
		scrollPane_2.setViewportView(editorPane_2);
		
		createLatexNextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(reportTemplateSelected.isSelected()){
					template = "report";
				}else if(bookTemplateSelected.isSelected()){
					template = "book";
				}else if(letterTemplateSelected.isSelected()){
					template = "letter";
				}else if(articleTemplateSelected.isSelected()){
					template = "article";
				}else{
					template ="";
				}
				controller.setUserSelectedTemplate(template);
				controller.enact("CreateCommand");
				CreateLatexBasedOnTemplate.setVisible(false);
				editorPane_2.setText(controller.getDoc().getContents());
				initMenu(scrollPane_2,editorPane_2);
				frame.setBounds(100, 100, 940, 670);
				frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-frame.getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-frame.getHeight()/2);
				panel_1.setVisible(true);
			}
		});
		
		loadLatexDocButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.addChoosableFileFilter(new FileNameExtensionFilter("*.tex", "tex"));
				fc.setAcceptAllFileFilterUsed(true);
				fc.showOpenDialog(frame);
				File file = fc.getSelectedFile();
				if (file!=null){
					if(!file.exists()){
						JOptionPane.showMessageDialog(null,"No file Selected ","Error",JOptionPane.ERROR_MESSAGE);
					}
					if(!determineFileExtensions(file.getPath())){
						JOptionPane.showMessageDialog(null,"The format of the file you are trying to import is not supported","Error",JOptionPane.ERROR_MESSAGE);
					}else{
						controller.setUserSelectedFile(file);
						controller.enact("LoadCommand");
						Home.setVisible(false);
						editorPane_2.setText(controller.getDoc().getContents());
						initMenu(scrollPane_2,editorPane_2);
						frame.setBounds(100, 100, 940, 670);
						frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width/2-frame.getWidth()/2, Toolkit.getDefaultToolkit().getScreenSize().height/2-frame.getHeight()/2);
						panel_1.setVisible(true);
					}
				}
			}
		});
		
		
	}
}
