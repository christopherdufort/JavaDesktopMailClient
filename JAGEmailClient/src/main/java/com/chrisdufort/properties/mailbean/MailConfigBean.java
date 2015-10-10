package com.chrisdufort.properties.mailbean;

/**
 * This Mail Configuration Bean class contains all the relevant information
 * needed for authentication and connection to an email server with an email
 * account. This class is sufficient for basic Gmail functionality. Additional
 * fields are required to work with other mail systems.
 * 
 * User information including Username/Email and passwords are NOT encrypted,
 * passwords are stored in simple text.
 *
 * @author Christopher Dufort
 * @version 0.2.9-SNAPSHOT , last modified 10/09/15
 * @since 0.0.1-SNAPSHOT , Phase 1
 */
public class MailConfigBean {

	private String host; // TODO remove this in favor of new properties?
	private String userEmailAddress;
	private String password;
	private String imapUrl;
	private String smtpUrl;
	private int imapPort;
	private int smtpPort;
	private int mysqlPort;
	private String mysqlUrl;
	private String mysqlUsername;
	private String mysqlPassword;

	/**
	 * Default no parameter constructor. Calls constructor of super
	 * class(Object). Instantiates values to empty strings.
	 */
	public MailConfigBean() {
		super();
		this.host = "";
		this.userEmailAddress = "";
		this.password = "";
		this.setImapUrl("");
		this.setSmtpUrl("");
		this.setImapPort(0);
		this.setSmtpPort(0);
		this.setMysqlPort(0);
		this.setMysqlUrl("");
		this.setMysqlUsername("");
		this.setMysqlPassword("");
	}

	/**
	 * @param host
	 *            String representing host server information (SMTP/IMAP/POP3)
	 *            designed for gmail.
	 * @param userName
	 *            String representing user name / email of connection
	 *            type(send/receive).
	 * @param password
	 *            String stored currently in plain text - authorization password
	 *            for userName.
	 */
	public MailConfigBean(final String host, final String userEmailAddress, final String password) {
		super();
		this.host = host;
		this.userEmailAddress = userEmailAddress;
		this.password = password;
		// TODO support other fields here or make another constructor
	}

	/**
	 * @return host - string host which was set
	 */
	public final String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set server type and domain
	 */
	public final void setHost(final String host) {
		this.host = host;
	}

	/**
	 * @return userName string email/username for authentication
	 */
	public final String getUserEmailAddress() {
		return userEmailAddress;
	}

	/**
	 * @param userName
	 *            the userName to set for authentication with host
	 */
	public final void setUserEmailAddress(final String userEmailAddress) {
		this.userEmailAddress = userEmailAddress;
	}

	/**
	 * @return the password string un-encrypted password for authentication
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set for authentication with host
	 */
	public final void setPassword(final String password) {
		this.password = password;
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

}