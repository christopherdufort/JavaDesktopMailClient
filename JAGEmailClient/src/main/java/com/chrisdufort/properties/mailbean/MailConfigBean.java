package com.chrisdufort.properties.mailbean;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * This Mail Configuration Bean class contains all the relevant information
 * needed for authentication and connection to an email server with an email
 * account. This class is sufficient for basic Gmail functionality. Additional
 * fields are required to work with other mail systems.
 * 
 * User information including Username/Email and passwords are NOT encrypted,
 * passwords are stored in simple text.
 * 
 * Modified class to be in the form of JavaFX Beans (property)
 *
 * @author Christopher Dufort
 * @version 0.3.0-SNAPSHOT , Phase 3 last modified 10/14/15
 * @since 0.0.1-SNAPSHOT , Phase 1
 */
public class MailConfigBean {

	private StringProperty username;
	private StringProperty emailAddress;
	private StringProperty name;
	private StringProperty password;
	private StringProperty imapUrl;
	private StringProperty smtpUrl;
	private IntegerProperty imapPort;
	private IntegerProperty smtpPort;
	private IntegerProperty mysqlPort;
	private StringProperty mysqlUrl;
	private StringProperty mysqlDatabase;
	private StringProperty mysqlUsername;
	private StringProperty mysqlPassword;

	/**
	 * Default no parameter constructor. Calls constructor of super
	 * class(Object). Instantiates values to empty strings.
	 */
	public MailConfigBean() {
		super();
		this.setUsername("");
		this.setEmailAddress("");
		this.setName("");
		this.setPassword("");
		this.setImapUrl("");
		this.setSmtpUrl("");
		this.setImapPort(0);
		this.setSmtpPort(0);
		this.setMysqlPort(0);
		this.setMysqlUrl("");
		this.setMysqlUsername("");
		this.setMysqlPassword("");
	}

	public MailConfigBean(final String username, final String emailAddress, final String name, final String password,
			final String imapUrl, final String smtpUrl, final int imapPort, final int smtpPort, final String mysqlUrl,
			final String mysqlDatabase, final String mysqlUsername, final String mysqlPassword)
	{
		super();
	}


	/**
	 * @return the imapUrl
	 */
	public String getImapUrl() {
		return imapUrl;
	}

	/**
	 * @param imapUrl
	 *            the imapUrl to set
	 */
	public void setImapUrl(String imapUrl) {
		this.imapUrl = imapUrl;
	}

	/**
	 * @return the smtpUrl
	 */
	public String getSmtpUrl() {
		return smtpUrl;
	}

	/**
	 * @param smtpUrl
	 *            the smtpUrl to set
	 */
	public void setSmtpUrl(String smtpUrl) {
		this.smtpUrl = smtpUrl;
	}

	/**
	 * @return the imapPort
	 */
	public int getImapPort() {
		return imapPort;
	}

	/**
	 * @param imapPort
	 *            the imapPort to set
	 */
	public void setImapPort(int imapPort) {
		this.imapPort = imapPort;
	}

	/**
	 * @return the smtpPort
	 */
	public int getSmtpPort() {
		return smtpPort;
	}

	/**
	 * @param smtpPort
	 *            the smtpPort to set
	 */
	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	/**
	 * @return the mysqlPort
	 */
	public int getMysqlPort() {
		return mysqlPort;
	}

	/**
	 * @param mysqlPort
	 *            the mysqlPort to set
	 */
	public void setMysqlPort(int mysqlPort) {
		this.mysqlPort = mysqlPort;
	}

	/**
	 * @return the mysqlUrl
	 */
	public String getMysqlUrl() {
		return mysqlUrl;
	}

	/**
	 * @param mysqlUrl
	 *            the mysqlUrl to set
	 */
	public void setMysqlUrl(String mysqlUrl) {
		this.mysqlUrl = mysqlUrl;
	}

	/**
	 * @return the mysqlUsername
	 */
	public String getMysqlUsername() {
		return mysqlUsername;
	}

	/**
	 * @param mysqlUsername
	 *            the mysqlUsername to set
	 */
	public void setMysqlUsername(String mysqlUsername) {
		this.mysqlUsername = mysqlUsername;
	}

	/**
	 * @return the mysqlPassword
	 */
	public String getMysqlPassword() {
		return mysqlPassword;
	}

	/**
	 * @param mysqlPassword
	 *            the mysqlPassword to set
	 */
	public void setMysqlPassword(String mysqlPassword) {
		this.mysqlPassword = mysqlPassword;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * 
	 * @param userName
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the mysqlDatabase
	 */
	public String getMysqlDatabase() {
		return mysqlDatabase;
	}

	/**
	 * @param mysqlDatabase the mysqlDatabase to set
	 */
	public void setMysqlDatabase(String mysqlDatabase) {
		this.mysqlDatabase = mysqlDatabase;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}