package com.chrisdufort.JAGEmailClient.controllers;

import java.io.IOException;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.properties.mailbean.MailConfigBean;
import com.chrisdufort.properties.manager.PropertiesManager;

/**
 * Controller for configuration GUI.
 * TODO This class is currently missing validation for its fields.
 *
 * @author Christopher Dufort
 * @version 0.4.4-SNAPSHOT - phase 4, last modified 12/12/2015
 * @since 0.3.1
 */
public class FXMLController {

    // Real programmers use logging, not System.out.println
    private final Logger log = LoggerFactory.getLogger(getClass().getName());
    
    private MailConfigBean mailConfigData;

    // The @FXML annotation on a class variable results in the matching
    // reference being injected into the variable
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailAddressTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField imapUrlTextField;
    @FXML
    private TextField smtpUrlTextField;
    @FXML
    private TextField imapPortTextField;
    @FXML
    private TextField smtpPortTextField;
    @FXML
    private TextField mysqlPortTextField;
    @FXML
    private TextField mysqlUrlTextField;
    @FXML
    private TextField mysqlDatabaseTextField;
    @FXML
    private TextField mysqlUsernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField mysqlPasswordTextField;
    
    private boolean submitClicked = false;


	/**
	 * Default constructor creates an instance of MailConfigBean that can be bound to
	 * the form
	 */
	public FXMLController() {
		super();
		mailConfigData = new MailConfigBean();
	}

    // resources were from the FXMLLoader
    @FXML
    private ResourceBundle resources;

	private Stage dialogStage;

    /**
     * This method is automatically called after the fxml file has been loaded.
	 * This code binds the properties of the data bean to the JavaFX controls.
	 * Changes to a control is immediately written to the bean and a change to
	 * the bean is immediately shown in the control.
     */
    @FXML
    private void initialize() {
        log.info("controlller initialize called");
        Bindings.bindBidirectional(usernameTextField.textProperty() , mailConfigData.usernameProperty());
        Bindings.bindBidirectional(emailAddressTextField.textProperty(), mailConfigData.emailAddressProperty());
        Bindings.bindBidirectional(nameTextField.textProperty(), mailConfigData.nameProperty());
        Bindings.bindBidirectional(passwordTextField.textProperty(), mailConfigData.passwordProperty());
        Bindings.bindBidirectional(imapUrlTextField.textProperty(), mailConfigData.imapUrlProperty());
        Bindings.bindBidirectional(smtpUrlTextField.textProperty(), mailConfigData.smtpUrlProperty());
        Bindings.bindBidirectional(imapPortTextField.textProperty(), mailConfigData.imapPortProperty(),new NumberStringConverter());
        Bindings.bindBidirectional(smtpPortTextField.textProperty(), mailConfigData.smtpPortProperty(),new NumberStringConverter());
        Bindings.bindBidirectional(mysqlPortTextField.textProperty(), mailConfigData.mysqlPortProperty(),new NumberStringConverter());
        Bindings.bindBidirectional(mysqlUrlTextField.textProperty(), mailConfigData.mysqlUrlProperty());
        Bindings.bindBidirectional(mysqlDatabaseTextField.textProperty(), mailConfigData.mysqlDatabaseProperty());
        Bindings.bindBidirectional(mysqlUsernameTextField.textProperty(), mailConfigData.mysqlUsernameProperty());
        Bindings.bindBidirectional(mysqlPasswordTextField.textProperty(), mailConfigData.mysqlPasswordProperty());
    }
    
    /**
     * The even handler registered in the FXML file for when the button is
     * pressed
     *
     * @param event
     * @throws IOException 
     */
    @FXML
    void submitPressed(ActionEvent event) throws IOException {
    	PropertiesManager propManager = new PropertiesManager();
    	propManager.writeTxtProperties("", "TextConfigProperties", mailConfigData);
    	propManager.writeXmlProperties("", "XMLConfigProperties", mailConfigData);
    	submitClicked = true;
    	exitPressed(event);
    	   	
    }
    
    /**
     * The even handler registered in the FXML file for when the exit button is
     * pressed - Exit event handler
     *
     * @param event
     */
    @FXML
    void exitPressed(ActionEvent event) {
    	//Close this dialog
        Node  source = (Node)event.getSource(); 
        Stage stage  = (Stage)source.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Sets the stage of this dialog.
     * 
     * Maintain a reference to the stage.
     * Add an icon to the top left corner.
     * 
     * @param dialogStage
     * 				The stage used to display this dialog.
     */
	public void setDialogStage(Stage dialogStage) {	
        this.dialogStage = dialogStage;
        
        // Set the dialog icon.
        this.dialogStage.getIcons().add(new Image("file:resources/images/edit.png"));
	}

	/**
	 * This method updates the value of the private boolean checking value submit boolean.
	 * @return
	 */
	public boolean isSubmitClicked() {
		return submitClicked;
	}

	public void setConfigBean(MailConfigBean mailConfig) {
		log.debug("set config bean called");
		mailConfigData = mailConfig;
		initialize();
		
	}
}
