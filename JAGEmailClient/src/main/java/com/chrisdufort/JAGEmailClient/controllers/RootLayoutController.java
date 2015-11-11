package com.chrisdufort.JAGEmailClient.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.JAGEmailClient.MainAppFX;
import com.chrisdufort.persistence.MailDAO;
import com.chrisdufort.persistence.MailDAOImpl;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * This is the root layout. All of the other layouts are added in code here.
 * This allows us to use the standalone containers with minimal changes.
 * 
 * i18n added
 * 
 * @author Christopher Dufort
 * @version 0.3.9-SNAPSHOT - phase 3, last modified 11/11/2015
 * @since 0.3.4
 *
 */
public class RootLayoutController {
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	@FXML
	private AnchorPane treeSplit;

	@FXML
	private AnchorPane tableSplit;

	@FXML
	private AnchorPane htmlSplit;

    @FXML 
    private ResourceBundle resources;
    
    @FXML
    private MenuItem englishItem;

    @FXML
    private MenuItem frenchItem;


	private MailDAO mailDAO;
	private MailFXTreeController mailFXTreeController;
	private MailFXTableController mailFXTableController;
	private MailFXHTMLController mailFXHTMLController;

	private Locale currentLocale;
	private FXMLLoader loader;

	public RootLayoutController() {
		super();
		log.debug("RootLayoutController is constructed");
		mailDAO = new MailDAOImpl();
	}
	
	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void handleAbout() {	
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Java Application for Java Project");
		alert.setHeaderText("About");
		alert.setContentText("Created By Christopher Dufort");

		alert.showAndWait();
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		Platform.exit();
	}

	/**
	 * Here we call upon the methods that load the other containers and then
	 * send the appropriate action command to each container
	 */
	@FXML
	private void initialize() {
		
		log.debug("RootLayoutController's initialize method is called");
		
		initTreeLayout();
		initTableLayout();
		initHtmlLayout();

		// Tell the tree about the table
		setTableControllerToTree();
		
		// Tell the table about the html
		setHTMLControllerToTable();

		try {
			mailFXTreeController.displayTree();
			mailFXTableController.displayTheTable("inbox"); //TODO remove hard coded default choice?
			//mailFXHTMLController.displayMailAsHTML();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Send the reference to the MailFXTableController to the MailFXTreeController
	 */
	private void setTableControllerToTree() {
		mailFXTreeController.setTableController(mailFXTableController);
	}
	
	/**
	 * Send the reference to the MailFXHTMLController to the MailFXTableController
	 */
	private void setHTMLControllerToTable() {
		mailFXTableController.setHTMLController(mailFXHTMLController);
	}

	/**
	 * The TreeView Layout
	 */
	private void initTreeLayout() {
		try {
			loader = new FXMLLoader();
			loader.setResources(resources);
			
			loader.setLocation(MainAppFX.class
					.getResource("/fxml/MailFXTreeLayout.fxml"));
			AnchorPane treeView = (AnchorPane)loader.load();

			// Give the controller the data object.
			mailFXTreeController = loader.getController();
			mailFXTreeController.setMailDAO(mailDAO);

			treeSplit.getChildren().add(treeView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The TableView Layout
	 */
	private void initTableLayout() {
		try {
			loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXTableLayout.fxml"));
			AnchorPane tableView = (AnchorPane)loader.load();

			// Give the controller the data object.
			mailFXTableController = loader.getController();
			mailFXTableController.setMailDAO(mailDAO);

			tableSplit.getChildren().add(tableView);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The HTMLEditor Layout
	 */
	private void initHtmlLayout() {
		try {
			loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class
					.getResource("/fxml/MailFXHTMLLayout.fxml"));
			BorderPane htmlView = (BorderPane)loader.load();

			// Give the controller the data object.
			mailFXHTMLController = loader.getController();
			mailFXHTMLController.setMailDAO(mailDAO);

			htmlSplit.getChildren().add(htmlView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

    @FXML
    void englishClicked(ActionEvent event) {
    	
		currentLocale = new Locale("en","CA");
		loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));
		
    }

    @FXML
    void frenchClicked(ActionEvent event) {
    	
    	currentLocale = new Locale("fr","CA");
		loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));
    }
}
