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
 * @version 0.2.9-SNAPSHOT , Phase 2 - last modified 10/09/15
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
			mailConfig.setHost(prop.getProperty("host"));
			mailConfig.setPassword(prop.getProperty("password"));
			mailConfig.setUserEmailAddress(prop.getProperty("userEmailAddress"));
			mailConfig.setImapUrl(prop.getProperty("imapUrl"));
			mailConfig.setSmtpUrl(prop.getProperty("smtpUrl"));
			// TODO add ports
			mailConfig.setMysqlUrl(prop.getProperty("mysqlUrl"));
			mailConfig.setMysqlUsername(prop.getProperty("mysqlUsername"));
			mailConfig.setMysqlPassword(prop.getProperty("mysqlPassword"));
		}
		return mailConfig;
	}

	/**
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
		Path txtFile = get(path, propFileName + ".properties");

		prop.setProperty("host", mailConfig.getHost());
		prop.setProperty("password", mailConfig.getPassword());
		prop.setProperty("userEmailAddress", mailConfig.getUserEmailAddress());

		prop.setProperty("imapUrl", mailConfig.getImapUrl());
		prop.setProperty("smtpUrl", mailConfig.getSmtpUrl());
		// TODO add ports
		prop.setProperty("mysqlUrl", mailConfig.getMysqlUrl());
		prop.setProperty("mysqlUsername", mailConfig.getMysqlUsername());
		prop.setProperty("mysqlPassword", mailConfig.getMysqlPassword());

		try (OutputStream propFileStream = newOutputStream(txtFile, StandardOpenOption.CREATE);) {
			prop.store(propFileStream, "SMTP Properties");
		}
	}

	/**
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
			mailConfig.setHost(prop.getProperty("host"));
			mailConfig.setPassword(prop.getProperty("password"));
			mailConfig.setUserEmailAddress(prop.getProperty("userEmailAddress"));
			mailConfig.setImapUrl(prop.getProperty("imapUrl"));
			mailConfig.setSmtpUrl(prop.getProperty("smtpUrl"));
			// TODO add ports
			mailConfig.setMysqlUrl(prop.getProperty("mysqlUrl"));
			mailConfig.setMysqlUsername(prop.getProperty("mysqlUsername"));
			mailConfig.setMysqlPassword(prop.getProperty("mysqlPassword"));
		}
		return mailConfig;
	}

	/**
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
		Path xmlFile = get(path, propFileName + ".xml");

		prop.setProperty("host", mailConfig.getHost());
		prop.setProperty("password", mailConfig.getPassword());
		prop.setProperty("userEmailAddress", mailConfig.getUserEmailAddress());

		prop.setProperty("imapUrl", mailConfig.getImapUrl());
		prop.setProperty("smtpUrl", mailConfig.getSmtpUrl());
		// TODO add ports
		prop.setProperty("mysqlUrl", mailConfig.getMysqlUrl());
		prop.setProperty("mysqlUsername", mailConfig.getMysqlUsername());
		prop.setProperty("mysqlPassword", mailConfig.getMysqlPassword());

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

		// Throws NullPointerException if file is not found
		// ClassLoader loader = Thread.currentThread().getContextClassLoader();

		try (InputStream stream = getClass().getResourceAsStream("/" + propertiesFileName);) {
			prop.load(stream);
		}
		mailConfig.setHost(prop.getProperty("host"));
		mailConfig.setPassword(prop.getProperty("password"));
		mailConfig.setUserEmailAddress(prop.getProperty("userEmailAddress"));
		mailConfig.setImapUrl(prop.getProperty("imapUrl"));
		mailConfig.setSmtpUrl(prop.getProperty("smtpUrl"));
		// TODO add ports
		mailConfig.setMysqlUrl(prop.getProperty("mysqlUrl"));
		mailConfig.setMysqlUsername(prop.getProperty("mysqlUsername"));
		mailConfig.setMysqlPassword(prop.getProperty("mysqlPassword"));

		return mailConfig;
	}
}
