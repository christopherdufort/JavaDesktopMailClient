package com.chrisdufort.JAGEmailClient.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAO;


/**
 * This is the controller for handling the editing of an FX Mail Bean.
 * 
 * @author Christopher Dufort
 * @version 0.3.95-SNAPSHOT - phase 3, last modified 11/15/2015
 * @since 0.3.95
 */
public class MailFXEditController {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    @FXML
    private BorderPane mailFXEditLayout;

    @FXML
    private Button sendButton;

    @FXML
    private Button attachFileButton;

    @FXML
    private Button embedFileButton;

    @FXML
    private Button deleteButton;

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

	private boolean sendClicked = false;

	private Stage dialogStage;

	private MailBean meanBean;

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
    void handleDelete(ActionEvent event) {
    	//Close this dialog
        Node  source = (Node)event.getSource(); 
        Stage stage  = (Stage)source.getScene().getWindow();
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
    
    /**
     * This method is an event handler for adding embeded to email.
     * @param event
     */
    @FXML
    void handleSend(ActionEvent event) {
    	log.debug("sending email and storing in database");
    }

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded. Not much to do here.
	 */
	@FXML
	private void initialize() {
		// Nothing to initialize with HTMLEditor.
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


}
