package com.chrisdufort.JAGEmailClient.controllers;

import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.properties.mailbean.MailConfigBean;

/**
 * Basic class for an FXML controller
 *
 * #KFCStandard and JavaFX8
 *
 * @author Christopher Dufort
 * @version 0.3.3-SNAPSHOT , last modified 10/21/2015
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


	/**
	 * Default constructor creates an instance of FishData that can be bound to
	 * the form
	 */
	public FXMLController() {
		super();
		mailConfigData = new MailConfigBean();
	}

    // resources were from the FXMLLoader
    @FXML
    private ResourceBundle resources;

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
     */
    @FXML
    void submitPressed(ActionEvent event) {

    }
    /**
     * The even handler registered in the FXML file for when the exit button is
     * pressed - Exit event handler
     *
     * @param event
     */
    @FXML
    void exitPressed(ActionEvent event) {
    	Platform.exit();
    }
}
