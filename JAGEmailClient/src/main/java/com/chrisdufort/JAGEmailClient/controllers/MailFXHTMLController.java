package com.chrisdufort.JAGEmailClient.controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAO;


/**
 * 
 * @author Christopher
 * @version 0.3.8-SNAPSHOT - phase 3 , last modified 11/10/2015
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
	private void handleSave() {
		//FIXME SAVE NOT SAVING
		

		System.out.println("FIXME!");
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

	public void displayMailAsHTML(MailBean mailBean) {
		
		
		//TODOput the normal text in html if it does not exist in html?
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
	
		//mailFXHTMLEditor.setHtmlText(htmlText);

		mailFXWebView.getEngine().loadContent(htmlText);

	}
	
	public void displayOtherHTML() {
		/*String other = "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src=\"" 
				+ getClass().getResource("/FreeFall.jpg") + "\"><h2>I'm flying!</h2></body></html>";
		*/
		//String other = "impliment me";
		//mailFXHTMLEditor.setHtmlText(other);
	}

}
