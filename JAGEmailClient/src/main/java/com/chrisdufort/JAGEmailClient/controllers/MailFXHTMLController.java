package com.chrisdufort.JAGEmailClient.controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAO;


/**
 * 
 * @author Christopher
 * @version 0.3.4-SNAPSHOT - phase 3 , last modified 10/25/2015
 * @since 0.3.4
 */
public class MailFXHTMLController {

	private final Logger log = LoggerFactory.getLogger(this.getClass()
			.getName());

	private MailDAO mailDAO;

	@FXML
	private BorderPane mailFXHTMLLayout;

	@FXML
	private HTMLEditor mailFXHTMLEditor;

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded. Not much to do here.
	 */
	@FXML
	private void initialize() {

	}

	/**
	 * Saves the file to the person file that is currently open. If there is no
	 * open file, the "save as" dialog is shown.
	 */
	@FXML
	private void handleSave() {
		System.out.println(mailFXHTMLEditor.getHtmlText());
	}

	/**
	 * Opens an about dialog.
	 */
	@FXML
	private void handleAbout() {
		// Modal dialog box
		// JavaFX dialog coming in 8u40
		Alert dialog = new Alert(AlertType.INFORMATION);
		dialog.setTitle("Java Application for Java Project");
		dialog.setHeaderText("About");
		dialog.setContentText("Created By Christopher Dufort");
		dialog.show();
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		System.exit(0);
	}

	/**
	 * Sets a reference to the FishDAO object that retrieves data from the
	 * database. Convert the first three fields from the first three records
	 * into HTML.
	 * 
	 * @param fishDAO
	 * @throws SQLException
	 */
	public void setMailDAO(MailDAO mailDAO) {
		this.mailDAO = mailDAO;
	}

	public void displayMailAsHTML() {
		ArrayList<MailBean> data = null;
		try {
			data = mailDAO.findAll();
		} catch (SQLException e) {
			log.error("Error retrieving records: ", e.getCause());
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<html><body contenteditable='true'>");
		for (int x = 0; x < 3; ++x) {
			sb.append(data.get(x).getHtmlMessageField()).append("</br>");
			//sb.append(data.get(x).getCommonName()).append("</br>");
			//sb.append(data.get(x).getLatin()).append("</br></br>");
		}
		sb.append("</body></html>");

		mailFXHTMLEditor.setHtmlText(sb.toString());

	}
	
	public void displayOtherHTML() {
		String other = "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>Here is my photograph embedded in this email.</h1><img src=\"" 
				+ getClass().getResource("/FreeFall.jpg") + "\"><h2>I'm flying!</h2></body></html>";
		
		mailFXHTMLEditor.setHtmlText(other);
	}

}
