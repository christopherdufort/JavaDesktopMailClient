package com.chrisdufort.JAGEmailClient.controllers;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import javafx.scene.web.WebView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAO;


/**
 * 
 * @author Christopher
 * @version 0.4.5-SNAPSHOT - phase 4, last modified 12/13/2015
 * @since 0.3.4
 */
public class MailFXHTMLController {

	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	private MailDAO mailDAO;

	@FXML
	private BorderPane mailFXHTMLLayout;

    @FXML
    private WebView mailFXWebView;
    
    @FXML
    private TextField toTextField;

    @FXML
    private TextField ccTextField;

    @FXML
    private TextField bccTextField;

    @FXML
    private TextField subjectTextField;
    
    @FXML
    private Button replyButton;

    @FXML
    private Button replyAllButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button forwardButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button saveAttachmentButton;

	private MailBean visibleMailBean;

	private RootLayoutController rootLayoutController;


	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded. Not much to do here.
	 */
	@FXML
	private void initialize() {
		// Nothing to initialize with HTML.
	}

	/**
	 * Saves the file to the person file that is currently open. If there is no
	 * open file, the "save as" dialog is shown.
	 */
	@FXML
	private void handleSave(ActionEvent event) {
    	if (visibleMailBean != null){
    		log.debug("saving email");
    	}
	}
    @FXML
    private void handleDelete(ActionEvent event) throws SQLException {

    	if (visibleMailBean!= null)
    	{
    		mailDAO.deleteMail(visibleMailBean.getId());
    	}
    }

    @FXML
    private void handleForward(ActionEvent event) {
    	if (visibleMailBean != null){
    		log.debug("creating forward email");
    	}
    }

    @FXML
    private void handleReply(ActionEvent event) {
    	if (visibleMailBean != null){ 		
	    	log.debug("creating reply email");
	    	MailBean replyBean = new MailBean();
	    	//get to the from field of the person who sent you the visible email
	    	// and create a new email with the current content, a re subject and to the person
	    	replyBean.getToField().add(visibleMailBean.getFromField());
	    	replyBean.setSubjectField("re: " + subjectTextField.getText());
	    	replyBean.setHtmlMessageField(mailFXWebView.getAccessibleText());
	    	rootLayoutController.createNewEmail(replyBean);
    	}

    }

    @FXML
    private void handleReplyAll(ActionEvent event) {
    	if (visibleMailBean != null){
    		log.debug("replying to all email");
    	}
    }

    @FXML
    private void handleSaveAttachment(ActionEvent event) {
    	if (visibleMailBean != null){
    		log.debug("saving attachment");
    	}
    }


	/**
	 * Sets a reference to the mailDAO object that retrieves data from the
	 * database. Convert the first three fields from the first three records
	 * into HTML.
	 * 
	 * @param mailDAO
	 * @throws SQLException
	 */
	public void setMailDAO(MailDAO mailDAO) {
		this.mailDAO = mailDAO;
	}
	
	public void displayMailAsHTML(MailBean mailBean){
		
		//Store in variable to be used by other methods
		this.visibleMailBean = mailBean;
		
		//TODO put the normal text in html if it does not exist in html?
		if (mailBean != null){
			StringBuilder toField = new StringBuilder(), ccField = new StringBuilder(), bccField = new StringBuilder();
			String subject, htmlText;

			for(String to : mailBean.getToField())
			{
				toField.append(to);
				toField.append("; ");
			}
			for(String cc : mailBean.getCcField())
			{
				ccField.append(cc);
				ccField.append("; ");
			}
			for(String bcc : mailBean.getBccField())
			{
				bccField.append(bcc);
				bccField.append("; ");
			}
			
			toTextField.setText(toField.toString());
		    ccTextField.setText(ccField.toString());
		    bccTextField.setText(bccField.toString());
		    
		    subject = mailBean.getSubjectField();
		    subjectTextField.setText(subject);
		    
		    htmlText = mailBean.getHtmlMessageField();
		    
		    FileWriter fWriter = null;
		    BufferedWriter bWriter = null;
		    
		    try{
		    	fWriter = new FileWriter("fileName.html");
		    	bWriter = new BufferedWriter(fWriter);
		    	bWriter.write(htmlText);
		    	bWriter.close(); //make sure you close the writer objec
		    } catch (Exception e) {
		    	  e.printStackTrace();
		    }

		    final String html = "fileName.html";
	        final java.net.URI uri = java.nio.file.Paths.get(html).toAbsolutePath().toUri();
	        mailFXWebView.getEngine().load(uri.toString());
	        
	       // log.debug(mailBean.getEmbedAttachments().get(0).toByteArray()
	        
	        byte[] data = mailBean.getEmbedAttachments().get(0).toByteArray();
	        //contendid is always null?
	        Path file = Paths.get(mailBean.getEmbedAttachments().get(0).getName());
	     
			try {
				Files.write(file, data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	        
	        // create WebView with specified local content
	        //mailFXWebView.getEngine().loadContent(htmlText);
		}

	}

	public void setParentController(RootLayoutController rootLayoutController) {
		this.rootLayoutController = rootLayoutController;
	}
}
