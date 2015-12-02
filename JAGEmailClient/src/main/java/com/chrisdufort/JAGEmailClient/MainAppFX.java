package com.chrisdufort.JAGEmailClient;

import static java.nio.file.Paths.get;
import static javafx.application.Application.launch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.JAGEmailClient.controllers.FXMLController;
import com.chrisdufort.JAGEmailClient.controllers.MailFXEditController;
import com.chrisdufort.JAGEmailClient.controllers.RootLayoutController;
import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAOImpl;
import com.chrisdufort.properties.mailbean.MailConfigBean;
import com.chrisdufort.properties.manager.PropertiesManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This class is the new starting point of the application.
 * Using JavaFX with Java 8, main method entry point for program is found here.
 * This class will create the gui and initialize all of its sub parts.
 *
 * @author Christopher Dufort
 * @version 0.3.95-SNAPSHOT - phase 3, last modified 11/15/2015
 * @since 0.3.0-SNAPSHOT
 */
public class MainAppFX extends Application {

    // Real programmers use logging, not System.out.println
    private final Logger log = LoggerFactory.getLogger(getClass().getName());

    // The primary window or frame of this application
    private Stage primaryStage;
    private BorderPane rootLayout;
    private Locale currentLocale;
    private MailConfigBean mailConfigBean;
    private PropertiesManager propManager;
    
    /**
     * Constructor
     */
    public MainAppFX() {
        super();
        mailConfigBean = new MailConfigBean();
        propManager = new PropertiesManager();
    }

    /**
     * The application starts here
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        log.info("Program Begins");

        // The Stage comes from the framework so make a copy to use elsewhere
        this.primaryStage = primaryStage;
        
        // Set the application icon using getResourceAsStream.
     		this.primaryStage.getIcons().add(
     				new Image(MainAppFX.class
     						.getResourceAsStream("/images/email.png")));
     		
     	//Don't place properties inside of the jar file, they will not be editable, store in root of project.
     	mailConfigBean = propManager.loadTextProperties("", "TextConfigProperties");
     	

     	File configFile = new File("./TextConfigProperties.properties");
     	
     	//Optional to load XML properties instead.
     	
     	//FIXME how to submit the config and and continue flow of logic into the rootlayout?
     	if (configFile.exists())
     	{
     		// Create the root scene and put it on the stage.
     		log.debug("Config file exists - starting root application");
     		initRootLayout();
     	}
     	else
     	{
     		// Create the configuration Scene and put it on the Stage
     		log.debug("Config does not exist - launching configuration dialog");
     		configureStage();
            // Set the window title
            primaryStage.setTitle(ResourceBundle.getBundle("ConfigBundle").getString("TITLE"));
     	}
        // Raise the curtain on the Stage
        primaryStage.show();
    }

    public void initRootLayout() {
    	Locale locale = Locale.getDefault();
    	log.debug("Local = " + locale);
    	
		currentLocale = new Locale("en","CA");
		//currentLocale = new Locale("fr","CA");
		
		try {
			// Instantiate the FXMLLoader
			FXMLLoader loader = new FXMLLoader();
			
            // Localize the loader with its bundle
            // Uses the default locale and if a matching bundle is not found
            // will then use ConfigBundle.properties
			loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));

			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXRootLayout.fxml"));
		
			rootLayout = (BorderPane) loader.load();
			
			// Load the BorderPane into a Scene
	         Scene scene = new Scene(rootLayout);
	         
	         // Put the Scene on Stage
	         this.primaryStage.setScene(scene);
	     	
	         // Set the window title
	         primaryStage.setTitle(ResourceBundle.getBundle("ConfigBundle").getString("TITLE"));
	         // Raise the curtain on the Stage
	         
			 // Retrieve the controller if you must send it messages
			RootLayoutController rootController = loader.getController();
			rootController.setMainApp(this);
	         
	         primaryStage.show();
	         
			// Show the scene containing the root layout.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
     * Load the FXML and bundle, create a Scene and put the Scene on Stage.
     *
     * Using this approach allows you to use loader.getController() to get a
     * reference to the fxml's controller should you need to pass data to it.
     * Not used in this archetype.
     */
    private void configureStage() {
        try {
            // Instantiate the FXMLLoader
            FXMLLoader loader = new FXMLLoader();

            // Set the location of the fxml file in the FXMLLoader
            loader.setLocation(MainAppFX.class.getResource("/fxml/ConfigScene.fxml"));

            // Localize the loader with its bundle
            // Uses the default locale and if a matching bundle is not found
            // will then use ConfigBundle.properties
            loader.setResources(ResourceBundle.getBundle("ConfigBundle"));

            // Parent is the base class for all nodes that have children in the
            // scene graph such as AnchorPane or GridPane and most other containers
            Parent parent = (GridPane) loader.load();

            // Load the parent into a Scene
            Scene scene = new Scene(parent);

            // Put the Scene on Stage
            this.primaryStage.setScene(scene);
            
            log.debug("finished configuration");

        } catch (IOException ex) { // getting resources or files could fail
            log.error(null, ex);
            System.exit(1);
        }
    }
   
    /**
     * This method is responsible for displaying the configuration dialog
     * @param mailConfig
     * @return
     */
    public boolean showConfigEditDialog(MailConfigBean mailConfig) {
    	try{
    		// Load the fxml file and create a new stage for the popup dialog.
    		FXMLLoader loader = new FXMLLoader();
    		loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale)); //TODO this need a messagebundle file locals not working?
    		loader.setLocation(MainAppFX.class.getResource("/fxml/ConfigScene.fxml"));
    		GridPane page = (GridPane) loader.load();
    		
			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("JAG Client Edit Confguration");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			// Set the MailConfigBean into the controller.
			FXMLController controller = loader.getController();
			controller.setDialogStage(dialogStage);
					
			// Set the dialog icon.
			dialogStage.getIcons().add(new Image(MainAppFX.class.getResourceAsStream("/images/edit.png")));
			
			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
			
			return controller.isSubmitClicked();
			
    	}catch (IOException e) {
    		e.printStackTrace();
			return false;
		}
    }
    /**
     * Where it all begins
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }
    
    /**
     * Opens a dialog to edit details for a new email.
     * If the user clicks send, the fields provided are saved into the bean and returned.
     * 
     * @param newMail
     * @return
     */
	public boolean showMailEditDialog(MailBean newMail) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));
			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXEditLayout.fxml"));
			BorderPane page = (BorderPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Create Email");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the Bean into the controller.
			MailFXEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMailBean(newMail);

			// Set the dialog icon.
			dialogStage.getIcons().add(new Image(MainAppFX.class.getResourceAsStream("/images/edit.png")));

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isSendClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
