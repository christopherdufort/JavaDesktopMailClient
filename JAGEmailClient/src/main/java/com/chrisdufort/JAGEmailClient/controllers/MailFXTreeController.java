package com.chrisdufort.JAGEmailClient.controllers;

import java.sql.SQLException;
import java.util.ResourceBundle;

import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAO;



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
 * @version 0.3.4-SNAPSHOT phase 3 , last modified 10/25/2015
 * @since 0.3.4
 */
public class MailFXTreeController {

	private MailDAO mailDAO;
	private MailFXTableController mailFXTableController;
	
	@FXML
	private AnchorPane mailFXTreeLayout;
	
	@FXML
	private TreeView<MailBean> mailFXTreeView;
	
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
		MailBean rootMail = new MailBean();
		
		// The tree will display common name so we set this for the root
		// Because we are using i18n the root name comes from the resource
		// bundle
		rootMail.setFolder(resources.getString("Folders"));

		mailFXTreeView.setRoot(new TreeItem<MailBean>(rootMail));
		
		// This cell factory is used to choose which field in the FihDta object is used for the node name
		mailFXTreeView.setCellFactory((e) -> new TreeCell<MailBean>(){
            @Override
            protected void updateItem(MailBean item, boolean empty) {
                super.updateItem(item, empty);
                if(item != null) {
                    setText(item.getFolder());
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
	 * @param fishFXTableController
	 */
	public void setTableController(MailFXTableController mailFXTableController) {
		this.mailFXTableController = mailFXTableController;
	}
	
	/**
	 * Build the tree from the database
	 * @throws SQLException
	 */
	public void displayTree() throws SQLException {
		// Retreive the list of mail Beans
		ObservableList<MailBean> mailBeans = mailDAO.findTableAll();
		
		// Build an item for each fish and add it to the root
        if (mailBeans != null) {
            for (MailBean mailBean : mailBeans) {
            	TreeItem<MailBean> item = new TreeItem<>(mailBean);
            	item.setGraphic(new ImageView(getClass().getResource("/images/foder.png").toExternalForm()));
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
						(observable, oldValue, newValue) -> showMailDetails(newValue));
	}

	/**
	 * To be able to test the selection handler for the tree, this method
	 * displays the MailBean object that corresponds to the selected node.
	 * 
	 * @param MailBean
	 * 
	 */
	private void showMailDetails(TreeItem<MailBean> MailBean) {
		//FIXME decide on system.out.println
		System.out.println(MailBean.getValue());
	}
}
