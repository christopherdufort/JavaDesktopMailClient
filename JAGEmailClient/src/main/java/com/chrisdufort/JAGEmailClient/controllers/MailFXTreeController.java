package com.chrisdufort.JAGEmailClient.controllers;

import java.sql.SQLException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Christopher Dufort
 * @version 0.3.6-SNAPSHOT phase 3 , last modified 10/29/2015
 * @since 0.3.4
 */
public class MailFXTreeController {
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
	
	private MailDAO mailDAO;
	private MailFXTableController mailFXTableController;
	
	@FXML
	private AnchorPane mailFXTreeLayout;
		
    @FXML
    private TreeView<String> mailFXTreeView;
	
	// Resource bundle is injected when controller is loaded
	@FXML
	private ResourceBundle resources;

	

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// We need a root node for the tree and it must be the same type as all nodes
		// The tree will display common name so we set this for the root
		// Because we are using i18n the root name comes from the resource
		// bundle
		String rootFolder = resources.getString("FOLDERS");

		mailFXTreeView.setRoot(new TreeItem<String>(rootFolder));
		
		// This cell factory is used to choose which field in the FihDta object is used for the node name
		mailFXTreeView.setCellFactory((e) -> new TreeCell<String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null) {
                    setText(item);
                    setGraphic(getTreeItem().getGraphic());
                } else {
                    setText("");
                    setGraphic(null);
                }
            }
        });
	}
	
	/**
	 * Sets a reference to the MailDAO object that retrieves data from the
	 * database. With this inform
	 * 
	 * @param MailDAO
	 * @throws SQLException
	 */
	public void setMailDAO(MailDAO mailDAO){
		this.mailDAO = mailDAO;
	}
	
	/**
	 * The RootLayoutController calls this method to provide a reference to the
	 * MailFXTableController from which it can request a reference to the
	 * TreeView. With theTreeView reference it can change the selection in the
	 * TableView.
	 * 
	 * @param mailFXTableController
	 */
	public void setTableController(MailFXTableController mailFXTableController) {
		this.mailFXTableController = mailFXTableController;
	}
	
	/**
	 * Build the tree from the database
	 * @throws SQLException
	 */
	public void displayTree() throws SQLException {
		
		log.debug("MailFXTreeControllers displayTree method is called");
		
		// Retreive the list of folder names
		ObservableList<String> folders = mailDAO.findAllFolderNames();
		
		// Build an item for each mail and add it to the root
        if (folders != null) {
            for (String folder : folders) {
            	TreeItem<String> item = new TreeItem<>(folder);
            	item.setGraphic(new ImageView(getClass().getResource("/images/folder.png").toExternalForm()));
            	mailFXTreeView.getRoot().getChildren().add(item);
            }
        }

        // Open the tree
        mailFXTreeView.getRoot().setExpanded(true);
        
		// Listen for selection changes and show the MailBean details when changed.
    	mailFXTreeView
				.getSelectionModel()
				.selectedItemProperty()
				.addListener(
						(observable, oldValue, newValue) -> showMailDetailsTree(newValue));
	}

	/**
	 * Using the reference to the mailFXTableController it can change the
	 * selected row in the TableView It also displays the FishData object that
	 * corresponds to the selected node.
	 * 
	 * @param folder
	 * 
	 */
	private void showMailDetailsTree(TreeItem<String> folder) {
		
		// Get the row number
		int rowSelected = mailFXTableController.getMailDataTable().getSelectionModel().getSelectedIndex();
		
		//select the row that contains the mailbean object from the tree
		mailFXTableController.getMailDataTable().getSelectionModel().select(rowSelected);
		

		try{
			mailFXTableController.displayTheTable(folder.getValue());
		}
		catch (SQLException e){
			e.printStackTrace();
		}
	}
}
