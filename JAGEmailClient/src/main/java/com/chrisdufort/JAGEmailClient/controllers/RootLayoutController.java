package com.chrisdufort.JAGEmailClient.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.chrisdufort.JAGEmailClient.MainAppFX;
import com.chrisdufort.persistence.MailDAO;
import com.chrisdufort.persistence.MailDAOImpl;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * This is the root layout. All of the other layouts are added in code here.
 * This allows us to use the standalone containers with minimal changes.
 * 
 * i18n added
 * 
 * @author Christopher Dufort
 * @version 0.3.4-SNAPSHOT - phase 3, last modified 10/25/2015
 * @since 0.3.4
 *
 */
public class RootLayoutController {

	@FXML
	private AnchorPane treeSplit;

	@FXML
	private AnchorPane tableSplit;

	@FXML
	private AnchorPane htmlSplit;

    @FXML 
    private ResourceBundle resources;

	private MailDAO mailDAO;
	private MailFXTreeController mailFXTreeController;
	private MailFXTableController mailFXTableController;
	private MailFXHTMLController mailFXHTMLController;

	public RootLayoutController() {
		super();
		mailDAO = new MailDAOImpl();
	}

	/**
	 * Here we call upon the methods that load the other containers and then
	 * send the appropriate action command to each container
	 */
	@FXML
	private void initialize() {

		initTreeLayout();
		initTableLayout();
		initHtmlLayout();

		// Tell the tree about the table
		setTableControllerToTree();

		try {
			mailFXTreeController.displayTree();
			mailFXTableController.displayTheTable();
			mailFXHTMLController.displayMailAsHTML();
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
	 * The TreeView Layout
	 */
	private void initTreeLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);
			
			loader.setLocation(MainAppFX.class
					.getResource("/fxml/FishFXTreeLayout.fxml"));
			AnchorPane treeView = (AnchorPane) loader.load();

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
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class
					.getResource("/fxml/FishFXTableLayout.fxml"));
			AnchorPane tableView = (AnchorPane) loader.load();

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
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(resources);

			loader.setLocation(MainAppFX.class
					.getResource("/fxml/FishFXHTMLLayout.fxml"));
			AnchorPane htmlView = (AnchorPane) loader.load();

			// Give the controller the data object.
			mailFXHTMLController = loader.getController();
			mailFXHTMLController.setMailDAO(mailDAO);

			htmlSplit.getChildren().add(htmlView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
