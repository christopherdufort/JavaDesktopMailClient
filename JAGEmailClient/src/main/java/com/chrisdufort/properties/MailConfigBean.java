package com.chrisdufort.properties;

/**
 * This Mail Configuration Bean class contains all the relevant information
 * needed for authentication and connection to an email server with an email
 * account. 
 * This class is sufficient for basic Gmail functionality.
 * Additional fields are required to work with other mail systems.
 * 
 * User information including Username/Email and passwords are NOT encrypted,
 * passwords are stored in simple text.
 *
 * @author Christopher Dufort
 * @version 0.2.1-SNAPSHOT , last modified 09/14/15
 * @since 0.0.1-SNAPSHOT , Phase 1
 */
public class MailConfigBean {

	private String host;
	private String userEmailAddress;
	private String password;

	/**
	 * Default no parameter constructor. Calls constructor of super
	 * class(Object). Instantiates values to empty strings.
	 */
	public MailConfigBean() {
		super();
		this.host = "";
		this.userEmailAddress = "";
		this.password = "";
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
}