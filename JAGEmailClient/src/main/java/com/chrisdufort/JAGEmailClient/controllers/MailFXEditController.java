package com.chrisdufort.JAGEmailClient.controllers;


import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailaction.BasicSendAndReceive;
import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAO;
import com.chrisdufort.persistence.MailDAOImpl;
import com.chrisdufort.properties.mailbean.MailConfigBean;


/**
 * This is the controller for handling the editing of an FX Mail Bean.
 * 
 * @author Christopher Dufort
 * @version 0.4.4-SNAPSHOT - phase 4, last modified 12/12/2015
 * @since 0.3.95
 */
public class MailFXEditController {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	private MailBean mailToSend;
	private BasicSendAndReceive sending;

    // The @FXML annotation on a class variable results in the matching
    // reference being injected into the variable
    @FXML
    private BorderPane mailFXEditLayout;

    @FXML
    private Button sendButton;

    @FXML
    private Button attachFileButton;

    @FXML
    private Button embedFileButton;
  
    @FXML
    private Button cancelButton;

    @FXML
    private TextField toTextField;

    @FXML
    private TextField ccTextField;

    @FXML
    private TextField bccTextField;

    @FXML
    private TextField subjectTextField;

    @FXML
    private HTMLEditor mailFXEditorView;
    
    @FXML
    private MenuBar fxEditMenuBar;

	private boolean sendClicked = false;

	private Stage dialogStage;

	private MailBean meanBean;

	private MailConfigBean configBean;

	private MailDAO myDAO = new MailDAOImpl();
	
	/**
	 * Default constructor creates an instance of MailBean that will be bound to the form.
	 */
	public MailFXEditController(){
		super();
		mailToSend = new MailBean();
	}
	
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded. Not much to do here.
	 */
	@FXML
	private void initialize() {
        //cant bind to
		//cant bind cc
		//cant bind bcc
        Bindings.bindBidirectional(subjectTextField.textProperty(), mailToSend.subjectFieldProperty());
        //cant bind editor

	}
	
    /**
     * This method is an event handler for adding embeded to email.
     * @param event
     * @throws SQLException 
     */
    @FXML
    void handleSend(ActionEvent event) throws SQLException {
    	log.debug("sending email and storing in database");
    	
    	buildEmailToSend(); //builds an email

    	sending = new BasicSendAndReceive();    	
    	sending.sendWithEmbeddedAndAttachment(mailToSend, configBean);
    	
    	myDAO.createEmail(mailToSend);
    	
    	//Store the email
    	
    	Stage stage = (Stage)sendButton.getScene().getWindow();
    	stage.close();
    	
    	
    }

	private void buildEmailToSend() {
    	ArrayList<String>toList = new ArrayList<>();
    	ArrayList<String>ccList = new ArrayList<>();
    	ArrayList<String>bccList = new ArrayList<>();
    	String htmlText;
    	String fromField = configBean.getEmailAddress();
    	
    	//TODO handle string builder with delimiters
    	toList.add(toTextField.getText());
    	ccList.add(ccTextField.getText());
    	bccList.add(bccTextField.getText());
    
    	htmlText = mailFXEditorView.getHtmlText();
    	
    	mailToSend.setFromField(fromField);
    	mailToSend.getToField().addAll(toList);
    	mailToSend.getCcField().addAll(ccList);
    	mailToSend.getBccField().addAll(bccList);
    	//subject binded automatically
    	mailToSend.setHtmlMessageField(htmlText);
		
	}

	/**
	 * This is the event handler for the about button being clicked.
	 * @param event
	 */
    @FXML
    void handleAbout(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Java Application for Java Project");
		alert.setHeaderText("About");
		alert.setContentText("Created By Christopher Dufort");

		alert.showAndWait();
    }
    
    
    /**
     * This is the event handler for adding a new attachment to an email.
     * 
     * @param event
     */
    @FXML
    void handleAttach(ActionEvent event) {
    	log.debug("Adding attachment to email");
    }
    
    /**
     * This is the event handler for deleting an email that is being editied.
     * This method currently just closes the dialog.
     * @param event
     */
    @FXML
    void handleClose(ActionEvent event) {
    	log.debug("Closing EditStage");     
    	Stage stage = (Stage)cancelButton.getScene().getWindow();
    	stage.close();

    }
    
    /**
     * This method is an event handler for adding an embeded attachment to an email.
     * 
     * @param event
     */
    @FXML
    void handleEmbed(ActionEvent event) {
    	log.debug("embedding attachment inside email");
    }
    
	public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
        
	}

	public void setMailBean(MailBean newMail) {
		this.meanBean = newMail;
		
	}

	public boolean isSendClicked() {
		return sendClicked ;
	}

	public void setConfigBean(MailConfigBean configBean) {
		this.configBean = configBean;
		
	}
	



}
