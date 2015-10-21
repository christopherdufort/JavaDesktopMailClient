package com.chrisdufort.JAGEmailClient.controllers;

import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.properties.mailbean.MailConfigBean;

/**
 * Basic class for an FXML controller
 *
 * #KFCStandard and JavaFX8
 *
 * @author Christopher Dufort
 * @version 0.3.1-SNAPSHOT , last modified 10/20/2015
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
        
        //FIXME BIND!!!!!!!
        Bindings.bindBidirectional(usernameTextField.textProperty() , mailConfigData.username());
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
        Bindings.bindBidirectional(null, null);
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
}
