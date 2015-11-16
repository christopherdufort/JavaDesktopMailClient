package com.chrisdufort.test.properties.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.chrisdufort.properties.mailbean.MailConfigBean;
import com.chrisdufort.properties.manager.PropertiesManager;
import com.chrisdufort.test.MethodLogger;

/**
 * @author Christopher Dufort
 * @version 0.3.1 - last modified 10/20/2015
 * @since 0.3.1
 * 
 * FIXME NEED TO TEST this implementation
 *
 */
public class TestProperties {

    // A Rule is implemented as a class with methods that are associated
    // with the lifecycle of a unit test. These methods run when required.
    // Avoids the need to cut and paste code into every test method.
    @Rule
    public MethodLogger methodLogger = new MethodLogger();

	private PropertiesManager pm;
	private MailConfigBean mailConfig1; 

    /**
     * @throws java.lang.Exception
     */
	
	@Ignore
    @Before
    public void setUp() throws Exception {
        pm = new PropertiesManager();
    	mailConfig1 = new MailConfigBean();
    	mailConfig1.setSmtpUrl("smtp.gmail.com");
    	mailConfig1.setEmailAddress("jafg.send@gmail.com");
    	mailConfig1.setPassword("jafgsend514");
    }

    //TODO test WriteTextProperties
	@Ignore
    @Test
    public void testWriteText() throws FileNotFoundException, IOException {
    	
        pm.writeXmlProperties("", "TextProps", mailConfig1);

        MailConfigBean mailConfig2 = pm.loadTextProperties("", "TextProps");

        assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }
	@Ignore
    @Test
    public void testWriteXml() throws FileNotFoundException, IOException {
        pm.writeXmlProperties("", "TextProps", mailConfig1);

        MailConfigBean mailConfig2 = pm.loadTextProperties("", "TextProps");

        assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }
	@Ignore
    @Test
    public void testReadJarText() throws NullPointerException, IOException {

        MailConfigBean mailConfig2 = pm.loadJarTextProperties("jar.properties");

        assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }
}
