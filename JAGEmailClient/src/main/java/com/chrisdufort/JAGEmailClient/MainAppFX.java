package com.chrisdufort.JAGEmailClient;

import static javafx.application.Application.launch;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.persistence.MailDAOImpl;
import com.chrisdufort.properties.mailbean.MailConfigBean;
import com.chrisdufort.properties.manager.PropertiesManager;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Basic class for starting a JavaFX application
 *
 * #KFCStandard and JavaFX8
 *
 * @author Christopher Dufort
 * @version 0.3.5-SNAPSHOT - phase 3, last modified 10/28/2015
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
     		
     	//TODO handle this check better or move it? 
     	mailConfigBean = propManager.loadTextProperties("src/main/resources/properties", "TextConfigProperties");
     	
     	//FIXME how to submit and continue flow of logic
     	if (mailConfigBean.getEmailAddress().equals(""))
     	{
     		// Create the configuration Scene and put it on the Stage
            configureStage();
     	}
     	// Create the root scene and put it on the stage.
         initRootLayout();
         
        // Set the window title
        primaryStage.setTitle(ResourceBundle.getBundle("ConfigBundle").getString("TITLE"));
        // Raise the curtain on the Stage
        primaryStage.show();
    }

    public void initRootLayout() {
    	Locale locale = Locale.getDefault();
    	log.debug("Local = " + locale);
    	
		currentLocale = new Locale("en","CA");
		//currentLocale = new Locale("fr","CA");
		
		//Locale currentLocale = Locale.CANADA;
		//Locale currentLocale = Locale.CANADA_FRENCH;
		
		try {
			// Instantiate the FXMLLoader
			FXMLLoader loader = new FXMLLoader();

			// Set the location of the fxml file in the FXMLLoader
			loader.setLocation(MainAppFX.class.getResource("/fxml/MailFXRootLayout.fxml"));
			
            // Localize the loader with its bundle
            // Uses the default locale and if a matching bundle is not found
            // will then use ConfigBundle.properties
			loader.setResources(ResourceBundle.getBundle("MessagesBundle", currentLocale));
			
			rootLayout = (BorderPane) loader.load();
			
			// Load the BorderPane into a Scene
	         Scene scene = new Scene(rootLayout);
	         
	         // Put the Scene on Stage
	         this.primaryStage.setScene(scene);
	     	
	         // Set the window title
	         primaryStage.setTitle(ResourceBundle.getBundle("ConfigBundle").getString("TITLE"));
	         // Raise the curtain on the Stage
	         primaryStage.show();
	         
			// Retrieve the controller if you must send it messages
			//RootLayoutController rootController = loader.getController();

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
     * Where it all begins
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        System.exit(0);
    }
}
