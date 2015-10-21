package com.chrisdufort.test.properties.manager;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Before;
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
 */
public class TestProperties {

    // A Rule is implemented as a class with methods that are associated
    // with the lifecycle of a unit test. These methods run when required.
    // Avoids the need to cut and paste code into every test method.
    @Rule
    public MethodLogger methodLogger = new MethodLogger();

	private PropertiesManager pm;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        pm = new PropertiesManager();
    }

    @Test
    public void testWriteText() throws FileNotFoundException, IOException {
        MailConfigBean mailConfig1 = new MailConfigBean("smtp.gmail.com", "MooseForever", "cst.send@moose.com");
        pm.writeTextProperties("", "TextProps", mailConfig1);

        MailConfigBean mailConfig2 = pm.loadTextProperties("", "TextProps");

        assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }

    @Test
    public void testWriteXml() throws FileNotFoundException, IOException {
        MailConfigBean mailConfig1 = new MailConfigBean("smtp.gmail.com", "MooseForever", "cst.send@moose.com");
        pm.writeXmlProperties("", "TextProps", mailConfig1);

        MailConfigBean mailConfig2 = pm.loadTextProperties("", "TextProps");

        assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }
    
    @Test
    public void testReadJarText() throws NullPointerException, IOException {
        MailConfigBean mailConfig1 = new MailConfigBean("smtp.gmail.com", "MooseForever", "cst.send@moose.com");

        MailConfigBean mailConfig2 = pm.loadJarTextProperties("jar.properties");

        assertEquals("The two beans do not match", mailConfig1, mailConfig2);
    }
}
