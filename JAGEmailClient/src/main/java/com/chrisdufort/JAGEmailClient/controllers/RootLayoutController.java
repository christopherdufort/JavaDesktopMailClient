package com.chrisdufort.JAGEmailClient.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.JAGEmailClient.MainAppFX;
import com.chrisdufort.mailaction.BasicSendAndReceive;
import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAO;
import com.chrisdufort.persistence.MailDAOImpl;
import com.chrisdufort.properties.mailbean.MailConfigBean;
import com.chrisdufort.properties.manager.PropertiesManager;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * This is the root layout. All of the other layouts are added in code here.
 * This allows us to use the standalone containers with minimal changes.
 * 
 * Internationalization in the form of multiple languages added.
 * 
 * @author Christopher Dufort
 * @version 0.4.6-SNAPSHOT - phase 4, last modified 12/15/2015
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
    
    @FXML
    private Button addFolderButton;

    @FXML
    private Button deleteFolderButton;
    
    @FXML
    private Button refreshButton;
    
    @FXML
    private TextField searchTextField;
    
    @FXML
    private ComboBox<String> searchComboBox;
    
    @FXML
    private Button searchButton;

	private MailDAO mailDAO;
	private MailConfigBean configBean;
	private PropertiesManager loadConfig;
	private MailFXTreeController mailFXTreeController;
	private MailFXTableController mailFXTableController;
	private MailFXHTMLController mailFXHTMLController;

	private Locale currentLocale;
	private FXMLLoader loader;

	private MainAppFX mainApp;
	

	private ObservableList<MailBean> searchBeans;
	
	private BasicSendAndReceive sendAndReceive = new BasicSendAndReceive();

	private Timer timer;
	
	//TODO Throws exception may be file not found display in gui
	public RootLayoutController() throws IOException, SQLException {
		super();
		log.debug("RootLayoutController is constructed");
		mailDAO = new MailDAOImpl();	
		loadConfig= new PropertiesManager();
		configBean = loadConfig.loadTextProperties("./", "TextConfigProperties");
		refreshEmails();
		
		//set an timer task to refresh email every 5 minutes
        LocalDateTime ldt = LocalDateTime.now().plusMinutes(1);
        Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
        Date alarmDateTime = Date.from(instant);

        System.out.println("Time at which next alarm will ring " + alarmDateTime);
        timer = new Timer();
        timer.scheduleAtFixedRate(new RefreshTask(), alarmDateTime, 300000);
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
	
	@FXML
	private void handleHelp(ActionEvent event) {
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Java Application for Java Project");
		alert.setHeaderText("Help");
		alert.setContentText("JavaHelpAPi is deprecated feature under construction");

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
	 * Opens the configuration dialog to edit file.
	 * @param event
	 */
	@FXML
	private void handleConfig(ActionEvent event) {
		log.debug("clicked on config menu item");
		
		MailConfigBean configBean = new MailConfigBean();
		
		try {
			configBean = loadConfig.loadTextProperties("./", "TextConfigProperties");
		} catch (IOException e) {
			log.debug("No config file found instead using default empty bean");
		}

		boolean submitClicked = mainApp.showConfigEditDialog(configBean);
		if (submitClicked){
			log.debug("Config has been changed");
		}
		else
			log.debug("no changes to config made");
	}
	/**
	 * Opens the how to user dialog.
	 * @param event
	 */
    @FXML
    private void handHowToUse(ActionEvent event) {

    }
    
    /**
     * Opens a HTML editor to create a new email.
     * 
     * @param event
     */
    @FXML
    private void handleNewEmail(ActionEvent event) {
    	createNewEmail(new MailBean(), true);
    }
    
    /**
     * Opens a HTML editor in a dialog to create a new email.
     * @param mailBean
     */
    public void createNewEmail(MailBean mailBean, boolean brandNew){
    	boolean sendClicked = false;
    	sendClicked = mainApp.showMailEditDialog(mailBean,configBean,mailDAO, brandNew);

    	if (sendClicked) {
    		log.debug("A new email was created and sent");
    	}
    }
    
    @FXML
    private void handleAddFolder(ActionEvent event) {
    	log.debug("add folder clicked"); 	
    	TextInputDialog dialog = new TextInputDialog("folder name");
    	dialog.setTitle("Add folder Dialog");
    	dialog.setHeaderText("Adding a new folder.");
    	dialog.setContentText("Name of new folder:");
    	
    	Optional<String> folderName = dialog.showAndWait();
    	if (folderName.isPresent()){
    		try {
    			mailDAO.createFolder(folderName.get());
			} catch (SQLException e) {
				//This exception should never occur.
				log.error("No filename given");
			}
    	}
    	//Refresh the tree.
    	try {
			mailFXTreeController.displayTree();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    @FXML
    private void handleRenameFolder(ActionEvent event) {
	
    	log.debug("renamefolder clicked"); 	
    	
    	Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Rename a folder");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField from = new TextField();
        from.setPromptText("From");
        TextField to = new TextField();
        to.setPromptText("To");

        gridPane.add(from, 0, 0);
        gridPane.add(new Label("To:"), 1, 0);
        gridPane.add(to, 2, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> from.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(from.getText(), to.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            log.debug("Changing folder name From=" + pair.getKey() + ", To=" + pair.getValue());
        	String folderName = pair.getKey();
        	String newFolderName = pair.getValue();
        	if (!folderName.equals("")){
        		try {
        			int idOfFolderToChangeName = mailDAO.retrieveFolderID(folderName);
        			mailDAO.updateFolderName(idOfFolderToChangeName, newFolderName);
    			} catch (SQLException e) {
    				//This exception should never occur.
    				log.error("No filename given");
    			}
        	}
        });
    	
    	
    	//Refresh the tree.
    	try {
			mailFXTreeController.displayTree();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML
    private void handleDeleteFolder(ActionEvent event) {
    	log.debug("delete folder clicked"); 	
    	TextInputDialog dialog = new TextInputDialog("folder name");
    	dialog.setTitle("Delete folder Dialog");
    	dialog.setHeaderText("Please be sure you want to delete this folder!");
    	dialog.setContentText("Enter the name of the folder you wish to delete:");
    	
    	Optional<String> folderName = dialog.showAndWait();
    	if (folderName.isPresent()){
    		try {
    			mailDAO.deleteFolder(folderName.get());
			} catch (SQLException e) {
				//This exception should never occur.
				log.error("No filename given");
			}
    	}
    	try {
			mailFXTreeController.displayTree();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @FXML
    private void handleSearch(ActionEvent event) throws SQLException {
    	String searchText = searchTextField.getText();
    	String category = searchComboBox.getValue();
    	
    	log.debug("user is searching for " + searchText + " in " + category);

    	switch(category){
    		case "To":
    			searchBeans = mailDAO.findByTo(searchText);
    			break;
    		case "From":
    			searchBeans = mailDAO.findByFrom(searchText);
    			break;
    		case "CC":
    			searchBeans = mailDAO.findByCc(searchText);
    			break;
    		case "BCC":
    			searchBeans = mailDAO.findByBcc(searchText);
    			break;
    		case "Subject":
    			searchBeans = mailDAO.findBySubject(searchText);
    			break;
    		default:
    			
    			//should not happen  			
    	}
    	
    	mailFXTableController.displayTheTable(searchBeans);
    }
    

    @FXML
   private  void handleRefresh(ActionEvent event) throws SQLException {
    	refreshEmails();
    }
    
    public void refreshEmails()throws SQLException {
	    ArrayList<MailBean> beansToSync = sendAndReceive.receiveEmail(configBean);
	    
	    if (beansToSync != null){
	    	for(MailBean mailbean : beansToSync){
	    		mailbean.setFolder("inbox");
	    		mailDAO.createEmail(mailbean);
	    	}  	
	    }
    }
    
    /**
     * Is called by the main application to give a reference back to itself.
     * 
     * @param mainApp
     */
    public void setMainApp(MainAppFX mainApp) {
        this.mainApp = mainApp;

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
		
		//Populate search combobox
		List<String> list = new ArrayList<String>();
        list.add("To");
        list.add("From");
        list.add("CC");
        list.add("BCC");
        list.add("Subject");
        ObservableList obList = FXCollections.observableList(list);
        searchComboBox.setItems(obList);
        searchComboBox.setValue("To");
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
			mailFXHTMLController.setParentController(this);

			htmlSplit.getChildren().add(htmlView);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

    @FXML
    private void englishClicked(ActionEvent event) {
    	log.debug("english clicked, need to refresh gui");
		currentLocale = new Locale("en","CA");
		loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));
		
		mainApp.changeLanguage("english");
		
    }

    @FXML
    private void frenchClicked(ActionEvent event) {
    	log.debug("french clicked, need to refresh gui");
    	currentLocale = new Locale("fr","CA");
		loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));
		
		mainApp.changeLanguage("french");
    }
    
    @FXML
    private void handleSaveAttachment(ActionEvent event) {
    	log.debug("save attachment clicked - open a file explorer");
    }

    @FXML
    private void handleSaveEmail(ActionEvent event) {
    	log.debug("save email clicked - open a file explorer");
    }
    
    private class RefreshTask extends TimerTask {
    	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());

    	@Override
        public void run() {
    		log.debug("Refreshing emails on timer every 5 minutes");
    		try {
				refreshEmails();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        }
    }
}
