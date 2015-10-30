package com.chrisdufort.JAGEmailClient.controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAO;

/**
 * 
 * @author Christopher Dufort
 * @version 0.3.6-SNAPSHOT -phase 3 , last modified 10/29/2015
 * @since 0.3.4
 */
public class MailFXTableController {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

	private MailDAO mailDAO;

	@FXML
	private AnchorPane mailFXTable;

	@FXML
	private TableView<MailBean> mailDataTable;

	@FXML
	private TableColumn<MailBean, String> targetColumn;

	@FXML
	private TableColumn<MailBean, String> subjectColumn;

	@FXML
	private TableColumn<MailBean, String> messageColumn;

	@FXML
	private TableColumn<MailBean, String> attachColumn;

	@FXML
	private TableColumn<MailBean, LocalDateTime> dateColumn;


	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public MailFXTableController() {
		super();
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// Connects the property in the mailData object to the column in the table
		//FIXME make this change based on viewed folder;
		targetColumn.setCellValueFactory(cellData -> cellData.getValue().fromFieldProperty());
		subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectFieldProperty());
		messageColumn.setCellValueFactory(cellData -> cellData.getValue().textMessageFieldProperty());
		//FIXME do i even bother?
		//attachColumn.setCellValueFactory(cellData -> cellData.getValue());
		//FIXME make this change based on viewed folder;
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateReceivedProperty());


		adjustColumnWidths();

		// Listen for selection changes and show the mailData details when changed.
		mailDataTable.getSelectionModel().selectedItemProperty().addListener(
						(observable, oldValue, newValue) -> showMailDetails(newValue));
	}

	/**
	 * Sets a reference to the mailDAO object that retrieves data from the
	 * database
	 * 
	 * @param mailDAO
	 * @throws SQLException
	 */
	public void setMailDAO(MailDAO mailDAO) throws SQLException {
		this.mailDAO = mailDAO;
	}
	
	public void displayTheTable(String folderName) throws SQLException {
		// Add observable list data to the table
		mailDataTable.setItems(this.mailDAO.findByFolder(folderName));
	}
 

	/**
	 * Sets the width of the columns based on a percentage of the overall width
	 */
	private void adjustColumnWidths() {
		// Get the current width of the table
		double width = mailFXTable.getPrefWidth();
		// Set width of each column
		targetColumn.setPrefWidth(width * .05);
		subjectColumn.setPrefWidth(width * .15);
		messageColumn.setPrefWidth(width * .15);
		attachColumn.setPrefWidth(width * .05);
		dateColumn.setPrefWidth(width * .05);
	}


	/**
	 * To be able to test the selection handler for the table, this method
	 * displays the MailBean object that corresponds to the selected row.
	 * 
	 * @param newValue
	 */
	private void showMailDetails(MailBean newValue) {
		System.out.println(newValue);
	}

	public TableView<MailBean> getMailDataTable() {
		return mailDataTable;
	}

}
