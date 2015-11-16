package com.chrisdufort.properties.manager;

import static java.nio.file.Files.newInputStream;
import static java.nio.file.Files.newOutputStream;
import static java.nio.file.Paths.get;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import com.chrisdufort.properties.mailbean.MailConfigBean;

/**
 * @author Christopher Dufort
 * @version 0.3.0-SNAPSHOT , Phase 2 - last modified 10/14/15
 * @since 0.2.9-SNAPSHOT
 */
public class PropertiesManager {

	/**
	 * @param propFileName
	 */
	public PropertiesManager() {
		super();
	}

	/**
	 * Returns a MailConfigBean object with the contents of the properties file
	 * 
	 * @param path
	 *            Must exist, will not be created
	 * @param propFileName
	 *            Name of the properties file
	 * @return The bean loaded with the properties
	 * @throws IOException
	 */
	public MailConfigBean loadTextProperties(String path, String propFileName) throws IOException {

		Properties prop = new Properties();

		Path txtFile = get(path, propFileName + ".properties");
		MailConfigBean mailConfig = new MailConfigBean();

		// File must exist
		if (Files.exists(txtFile)) {
			try (InputStream propFileStream = newInputStream(txtFile);) {
				prop.load(propFileStream);
			}
			mailConfig.setUsername(prop.getProperty("username"));
			mailConfig.setEmailAddress(prop.getProperty("emailAddress"));
			mailConfig.setName(prop.getProperty("name"));
			mailConfig.setPassword(prop.getProperty("password"));
			mailConfig.setImapUrl(prop.getProperty("imapUrl"));
			mailConfig.setSmtpUrl(prop.getProperty("smtpUrl"));
			mailConfig.setSmtpPort(Integer.parseInt(prop.getProperty("smtpPort"))); //This cast ok?
			mailConfig.setImapPort(Integer.parseInt(prop.getProperty("imapPort")));   //This cast ok?
			mailConfig.setMysqlPort(Integer.parseInt(prop.getProperty("mysqlPort"))); //This cast ok?
			mailConfig.setMysqlUrl(prop.getProperty("mysqlUrl"));
			mailConfig.setMysqlDatabase(prop.getProperty("mysqlDatabase"));
			mailConfig.setMysqlUsername(prop.getProperty("mysqlUsername"));
			mailConfig.setMysqlPassword(prop.getProperty("mysqlPassword"));
		}
		return mailConfig;
	}

	/**
	 * Creates a plain text properties file based on the parameters
	 * 
	 * @param path
	 *            Must exist, will not be created
	 * @param propFileName
	 *            Name of the properties file
	 * @param mailConfig
	 *            The bean to store into the properties
	 * @throws IOException
	 */
	public void writeTxtProperties(String path, String propFileName, MailConfigBean mailConfig) throws IOException {

		Properties prop = new Properties();
	
		prop.setProperty("username", mailConfig.getUsername());
		prop.setProperty("emailAddress", mailConfig.getEmailAddress());
		prop.setProperty("name", mailConfig.getName());
		prop.setProperty("password", mailConfig.getPassword());
		prop.setProperty("imapUrl", mailConfig.getImapUrl());
		prop.setProperty("smtpUrl", mailConfig.getSmtpUrl());
		prop.setProperty("imapPort", String.valueOf(mailConfig.getImapPort())); 	//OK?
		prop.setProperty("smtpPort", String.valueOf(mailConfig.getSmtpPort())); 	//OK?
		prop.setProperty("mysqlPort", String.valueOf(mailConfig.getMysqlPort())); 	//OK?
		prop.setProperty("mysqlUrl", mailConfig.getMysqlUrl());
		prop.setProperty("mysqlDatabase", mailConfig.getMysqlDatabase());
		prop.setProperty("mysqlUsername", mailConfig.getMysqlUsername());
		prop.setProperty("mysqlPassword", mailConfig.getMysqlPassword());

		Path txtFile = get(path, propFileName + ".properties");
		
		// Creates the file or if file exists it is truncated to length of zero
		// before writing
		try (OutputStream propFileStream = newOutputStream(txtFile, StandardOpenOption.CREATE);) {
			prop.store(propFileStream, "SMTP Properties");
		}
	}

	/**
	 * Returns a MailConfigBean object with the contents of the properties file
	 * that is in an XML format
	 * 
	 * @param path
	 *            Must exist, will not be created. Empty string means root of
	 *            program folder
	 * @param propFileName
	 *            Name of the properties file
	 * @return The bean loaded with the properties
	 * @throws IOException
	 */
	public MailConfigBean loadXmlProperties(String path, String propFileName) throws IOException {

		Properties prop = new Properties();

		// The path of the XML file
		Path xmlFile = get(path, propFileName + ".xml");

		MailConfigBean mailConfig = new MailConfigBean();

		// File must exist
		if (Files.exists(xmlFile)) {
			try (InputStream propFileStream = newInputStream(xmlFile);) {
				prop.loadFromXML(propFileStream);
			}
			
			mailConfig.setUsername(prop.getProperty("username"));
			mailConfig.setEmailAddress(prop.getProperty("emailAddress"));
			mailConfig.setName(prop.getProperty("name"));
			mailConfig.setPassword(prop.getProperty("password"));
			mailConfig.setImapUrl(prop.getProperty("imapUrl"));
			mailConfig.setSmtpUrl(prop.getProperty("smtpUrl"));
			mailConfig.setImapPort(Integer.valueOf(prop.getProperty("imapPort")));
			mailConfig.setSmtpPort(Integer.valueOf(prop.getProperty("smtpPort")));
			mailConfig.setMysqlPort(Integer.valueOf(prop.getProperty("mysqlPort")));
			mailConfig.setMysqlUrl(prop.getProperty("mysqlUrl"));
			mailConfig.setMysqlDatabase(prop.getProperty("mysqlDatabase"));
			mailConfig.setMysqlUsername(prop.getProperty("mysqlUsername"));
			mailConfig.setMysqlPassword(prop.getProperty("mysqlPassword"));
		}
		return mailConfig;
	}

	/**
	 * Creates an XML properties file based on the parameters
	 * 
	 * @param path
	 *            Must exist, will not be created
	 * @param propFileName
	 *            Name of the properties file. Empty string means root of
	 *            program folder
	 * @param mailConfig
	 *            The bean to store into the properties
	 * @throws IOException
	 */
	public void writeXmlProperties(String path, String propFileName, MailConfigBean mailConfig) throws IOException {

		Properties prop = new Properties();
		
		prop.setProperty("username", mailConfig.getUsername());
		prop.setProperty("emailAddress", mailConfig.getEmailAddress());
		prop.setProperty("name", mailConfig.getName());
		prop.setProperty("password", mailConfig.getPassword());
		prop.setProperty("imapUrl", mailConfig.getImapUrl());
		prop.setProperty("smtpUrl", mailConfig.getSmtpUrl());
		prop.setProperty("imapPort", String.valueOf(mailConfig.getImapPort())); 	//OK?
		prop.setProperty("smtpPort", String.valueOf(mailConfig.getSmtpPort())); 	//OK?
		prop.setProperty("mysqlPort", String.valueOf(mailConfig.getMysqlPort())); 	//OK?
		prop.setProperty("mysqlUrl", mailConfig.getMysqlUrl());
		prop.setProperty("mysqlDatabase", mailConfig.getMysqlDatabase());
		prop.setProperty("mysqlUsername", mailConfig.getMysqlUsername());
		prop.setProperty("mysqlPassword", mailConfig.getMysqlPassword());

		Path xmlFile = get(path, propFileName + ".xml");
		
		// Creates the file or if file exists it is truncated to length of zero
		// before writing
		try (OutputStream propFileStream = newOutputStream(xmlFile, StandardOpenOption.CREATE);) {
			prop.storeToXML(propFileStream, "XML SMTP Properties");
		}
	}

	/**
	 * Retrieve the properties file. This syntax rather than normal File I/O is
	 * employed because the properties file is inside the jar. The technique
	 * shown here will work in both Java SE and Java EE environments. A similar
	 * technique is used for loading images.
	 * 
	 * In a Maven project all configuration files, images and other files go
	 * into src/main/resources. The files and folders placed there are moved to
	 * the root of the project when it is built.
	 * 
	 * @param propertiesFileName
	 *            : Name of the properties file
	 * @throws IOException
	 *             : Error while reading the file
	 * @throws NullPointerException
	 *             : File not found
	 */
	public MailConfigBean loadJarTextProperties(String propertiesFileName) throws IOException, NullPointerException {
		
		Properties prop = new Properties();
		MailConfigBean mailConfig = new MailConfigBean();
		// There is no exists method for files in a jar so we try to get the
		// resource and if its not there it returns a null
		if (this.getClass().getResource("/" + propertiesFileName) != null) 
		{
			try (InputStream stream = getClass().getResourceAsStream("/" + propertiesFileName);) {
				prop.load(stream);
			}
			mailConfig.setUsername(prop.getProperty("username"));
			mailConfig.setEmailAddress(prop.getProperty("emailAddress"));
			mailConfig.setName(prop.getProperty("name"));
			mailConfig.setPassword(prop.getProperty("password"));
			mailConfig.setImapUrl(prop.getProperty("imapUrl"));
			mailConfig.setSmtpUrl(prop.getProperty("smtpUrl"));
			mailConfig.setImapPort(Integer.valueOf(prop.getProperty("imapPort")));
			mailConfig.setSmtpPort(Integer.valueOf(prop.getProperty("smtpPort")));
			mailConfig.setMysqlPort(Integer.valueOf(prop.getProperty("mysqlPort")));
			mailConfig.setMysqlUrl(prop.getProperty("mysqlUrl"));
			mailConfig.setMysqlDatabase(prop.getProperty("mysqlDatabase"));
			mailConfig.setMysqlUsername(prop.getProperty("mysqlUsername"));
			mailConfig.setMysqlPassword(prop.getProperty("mysqlPassword"));
		}
		return mailConfig;
	}
}
