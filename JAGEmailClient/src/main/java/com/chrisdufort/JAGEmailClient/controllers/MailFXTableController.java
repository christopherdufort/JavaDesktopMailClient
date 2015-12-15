package com.chrisdufort.JAGEmailClient.controllers;

import java.sql.SQLException;
import java.time.LocalDateTime;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAO;

/**
 * 
 * @author Christopher Dufort
 * @version 0.4.6-SNAPSHOT - phase 4, last modified 12/15/2015
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
	private TableColumn<MailBean, String> toColumn;
	@FXML
	private TableColumn<MailBean, String> fromColumn;

	@FXML
	private TableColumn<MailBean, String> subjectColumn;

	@FXML
	private TableColumn<MailBean, String> messageColumn;

	@FXML
	private TableColumn<MailBean, String> attachColumn;

	@FXML
	private TableColumn<MailBean, LocalDateTime> dateSentColumn;
	
	@FXML
	private TableColumn<MailBean, LocalDateTime> dateReceiveColumn;

	private MailFXHTMLController mailFXHTMLController;



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
		//what to do for ToField
		fromColumn.setCellValueFactory(cellData -> cellData.getValue().fromFieldProperty());
		subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectFieldProperty());
		messageColumn.setCellValueFactory(cellData -> cellData.getValue().htmlMessageFieldProperty());
		dateSentColumn.setCellValueFactory(cellData -> cellData.getValue().dateSentProperty());
		dateReceiveColumn.setCellValueFactory(cellData -> cellData.getValue().dateReceivedProperty());

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
	public void displayTheTable(ObservableList<MailBean> searchBeans) throws SQLException{
		mailDataTable.setItems(searchBeans);
	}
 

	/**
	 * Sets the width of the columns based on a percentage of the overall width
	 */
	private void adjustColumnWidths() {
		// Get the current width of the table
		double width = mailFXTable.getPrefWidth();

	}

	
	public void setHTMLController(MailFXHTMLController mailFXHTMLController) {
		this.mailFXHTMLController = mailFXHTMLController;
	}

	/**
	 * To be able to test the selection handler for the table, this method
	 * displays the MailBean object that corresponds to the selected row.
	 * 
	 * @param newValue
	 */
	private void showMailDetails(MailBean mailBean) {
		
		System.out.println(mailBean.getToField().size());
		mailFXHTMLController.displayMailAsHTML(mailBean);

	}

	public TableView<MailBean> getMailDataTable() {
		return mailDataTable;
	}
	
	/**
	 * When a drag is detected the control at the start of the drag is accessed
	 * to determine what will be dragged.
	 * 
	 * SceneBuilder writes the event as ActionEvent that you must change to the
	 * proper event type that in this case is DragEvent
	 * 
	 * @param event
	 */
	@FXML
	private void dragDetected(MouseEvent event) {
		/* drag was detected, start drag-and-drop gesture */
		log.debug("onDragDetected");

		/* allow any transfer mode */
		Dragboard db = mailDataTable.startDragAndDrop(TransferMode.ANY);

		/* put a string on dragboard */
		ClipboardContent content = new ClipboardContent();
		content.putString(String.valueOf(mailDataTable.getSelectionModel().getSelectedItem().getId()));
		db.setContent(content);

		event.consume();

	}

}
