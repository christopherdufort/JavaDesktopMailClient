package com.chrisdufort.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailbean.MailBean;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

/**
 * @author Christopher Dufort
 * @version 0.2.9-SNAPSHOT , Phase 2 - last modified 10/09/15
 * @since 0.2.0-SNAPSHOT
 * 
 *        This is the Implementation class of Data Access Object for Mail Beans.
 *        This class is the sole class responsible for communicating with the
 *        database to perform CRUD operations. This class has a multitude of
 *        methods necissary for a stand alone desktop mail client to query and
 *        update the database of mail beans.
 */
public class MailDAOImpl implements MailDAO {
	// TODO Implement use of peroperties instead of hardcoded.
	// Database and user credentials used for Database acces.
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final String url = "jdbc:mysql://localhost:3306/maildb";
	private final String user = "christopher";
	private final String password = "password";

	/**
	 * Constructor, always call to super.
	 */
	public MailDAOImpl() {
		super();
	}

	/**
	 * Create method used to insert any form of mailbean into the database. This
	 * method makes use of three distinct inserts in order to spread the
	 * mailbean across multiple tables. This method makes used of a transaction
	 * inorder to have an all or nothing insert group of all three inserts at
	 * once.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#createEmail(com.chrisdufort.mailbean.MailBean)
	 * @param mailBean
	 *            The mailbean to be inserted into the database
	 */
	@Override
	public int createEmail(MailBean mailBean) throws SQLException {
		int emailId = -1;
		LocalDateTime truncDate;
		String createEmailInsert = "INSERT INTO email(from_field, subject,text,html,sent_date,receive_date, folder_id, mail_status) "
				+ "VALUES(?,?,?,?,?,?,?,?)";
		String createRecipientInsert = "INSERT INTO recipient(address, address_type, email_id) " + "VALUES (?,?,?)";
		String createAttachmentInsert = "INSERT INTO attachment(content_id, attach_name, attach_size, content, email_id) "
				+ "VALUES (?,?,?,?,?)";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement ps1 = connection.prepareStatement(createEmailInsert,
						PreparedStatement.RETURN_GENERATED_KEYS);
				PreparedStatement ps2 = connection.prepareStatement(createRecipientInsert);
				PreparedStatement ps3 = connection.prepareStatement(createAttachmentInsert);) {
			// Start the transaction
			connection.setAutoCommit(false);
			// Bind variables.
			ps1.setString(1, mailBean.getFromField());
			ps1.setString(2, mailBean.getSubjectField());
			ps1.setString(3, mailBean.getTextMessageField());
			ps1.setString(4, mailBean.getHtmlMessageField());
			// ps1.setTimestamp(5, mailBean.getDateSentAsTimestamp());
			// ps1.setTimestamp(6, mailBean.getDateReceivedAsTimestamp());
			// TODO decide on final functionality currently only dates not time
			// in DB
			truncDate = mailBean.getDateSent().truncatedTo(ChronoUnit.DAYS);
			ps1.setTimestamp(5, Timestamp.valueOf(truncDate));
			truncDate = mailBean.getDateReceived().truncatedTo(ChronoUnit.DAYS);
			ps1.setTimestamp(6, Timestamp.valueOf(truncDate));

			int folderId = retrieveFolderID(mailBean.getFolder());
			if (folderId == -1)
				folderId = 1; // Default to inbox
			ps1.setInt(7, folderId);

			ps1.setInt(8, mailBean.getMailStatus());

			ps1.executeUpdate();

			ResultSet results = ps1.getGeneratedKeys(); // Returns a result set
														// of primary keys
														// inserted.

			if (results != null && results.next())
				emailId = results.getInt(1);

			// Bind recipient values
			for (String address : mailBean.getToField()) {
				ps2.setString(1, address);
				ps2.setInt(2, 0);
				ps2.setInt(3, emailId);
				ps2.executeUpdate();
			}
			for (String address : mailBean.getCcField()) {
				ps2.setString(1, address);
				ps2.setInt(2, 1);
				ps2.setInt(3, emailId);
				ps2.executeUpdate();
			}
			for (String address : mailBean.getBccField()) {
				ps2.setString(1, address);
				ps2.setInt(2, 2);
				ps2.setInt(3, emailId);
				ps2.executeUpdate();
			}
			// bind attachment values.
			for (EmailAttachment embed : mailBean.getEmbedAttachments()) {
				ps3.setString(1, embed.getContentId());
				ps3.setString(2, embed.getName());
				ps3.setInt(3, embed.getSize());
				ps3.setBytes(4, embed.toByteArray());
				ps3.setInt(5, emailId);
				ps3.executeUpdate();
			}
			for (EmailAttachment attach : mailBean.getFileAttachments()) {
				ps3.setString(1, null);
				ps3.setString(2, attach.getName());
				ps3.setInt(3, attach.getSize());
				ps3.setBytes(4, attach.toByteArray());
				ps3.setInt(5, emailId);
				ps3.executeUpdate();
			}
			// Commit the transation
			connection.commit();
		}
		log.info("createEmail() inserted an email with the id of " + emailId);
		return emailId;
	}

	/**
	 * This private method returns folder id when given a string folder name.
	 * 
	 * @param folderName
	 *            the folder name to search for an return the id of associated
	 *            data.
	 * @return id of folder
	 * @throws SQLException
	 */
	private int retrieveFolderID(String folderName) throws SQLException {
		int idOfFolder = -1;
		String selectFolderIdQuery = "SELECT folder_id " + "FROM folder " + "WHERE folder_name = ? ";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(selectFolderIdQuery);) {
			prepStmt.setString(1, folderName);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				while (resultSet.next()) {
					idOfFolder = resultSet.getInt("folder_id");
				}
			}
		}
		return idOfFolder;
	}

	/**
	 * This method is responsible for creating additional folders in the tree
	 * when given folder names.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#createFolder(java.lang.String)
	 * @param folderName
	 *            string folder name to add to the tree
	 */
	@Override
	public int createFolder(String folderName) throws SQLException {
		int insertedRows = 0;
		String createFolderInsert = "INSERT INTO folder(folder_name) " + "VALUES(?)";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(createFolderInsert);) {
			prepStmt.setString(1, folderName);
			insertedRows = prepStmt.executeUpdate();
		}
		log.info("createFolder() inserted " + insertedRows + " folders");
		return insertedRows;
	}

	/**
	 * This method is used to retrieve every single email in the database (very
	 * slow!)
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findAll()
	 */
	@Override
	public ArrayList<MailBean> findAll() throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();

		String findAllQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
				+ "FROM email JOIN folder ON email.folder_id = folder.folder_id";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findAllQuery);) {
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findAll() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}
	
	/**
     * Retrieve all the records for the given table and returns the data as an
     * ObservableList of MailBean objects
     *
     * @return The ObservableLisy of MailBean objects
     * @throws java.sql.SQLException
     */
	@Override
	public ObservableList<MailBean> findTableAll() throws SQLException {
		ObservableList<MailBean> foundObservableEmails = FXCollections.observableArrayList();
		
		String findAllQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
				+ "FROM email JOIN folder ON email.folder_id = folder.folder_id";
		
		try (Connection connection = DriverManager.getConnection(url, user, password);
				// You must use PreparedStatements to guard against SQL Injection
				PreparedStatement prepStmt = connection.prepareStatement(findAllQuery);) {
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				for (MailBean mb : createMailBeans(resultSet))
				foundObservableEmails.add(mb);
			}
		}
		log.info("findTableAll() returned " + foundObservableEmails.size() + " emails");
		return foundObservableEmails;
	}

	/**
	 * This method will retrieve a list of strings representing every folder
	 * name in the database
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findAllFolderNames()
	 */
	@Override
	public ArrayList<String> findAllFolderNames() throws SQLException {
		ArrayList<String> folderNames = new ArrayList<>();
		// ORDER BY included because folder_name is UNIQUE and becomes
		// incorrectly ordered because of InnoDB
		String findFolderQuery = "SELECT folder_name " + "FROM folder " + "ORDER BY folder_id";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findFolderQuery);) // TODO
																							// does
																							// not
																							// need
																							// to
																							// be
																							// a
																							// prepared
																							// statement
		{
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				while (resultSet.next()) {
					folderNames.add(resultSet.getString("folder_name"));
				}
			}
		}
		return folderNames;

	}

	/**
	 * This private method will create mail beans to be returned when provided
	 * with a result set. It will call the associated private methods to create
	 * email attachments and return recipients also.
	 * 
	 * @param resultSet
	 *            results from a query used to build a mail bean.
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<MailBean> createMailBeans(ResultSet resultSet) throws SQLException {
		ArrayList<MailBean> createdEmails = new ArrayList<>();

		try {
			while (resultSet.next()) {
				MailBean mail = new MailBean();
				mail.setId(resultSet.getInt("email_id"));
				mail.setFromField(resultSet.getString("from_field"));
				mail.setSubjectField(resultSet.getString("subject"));
				if (resultSet.getString("text") != null)
					mail.setTextMessageField(resultSet.getString("text"));
				if (resultSet.getString("html") != null)
					mail.setHtmlMessageField(resultSet.getString("html"));
				if (resultSet.getTimestamp("sent_date") != null)
					mail.setDateSent(resultSet.getTimestamp("sent_date"));
				if (resultSet.getTimestamp("receive_date") != null)
					mail.setDateReceived(resultSet.getTimestamp("receive_date"));
				mail.setFolder(resultSet.getString("folder_name"));
				mail.setMailStatus(resultSet.getInt("mail_status"));
				findAssociatedRecipient(mail);
				findAssociatedAttachments(mail);
				createdEmails.add(mail);
			}
		} finally {
			resultSet = null;
		}
		log.info("createMailBeans() created " + createdEmails.size() + " complete email beans.");
		return createdEmails;
	}

	/**
	 * Method used to retrieve the addresses from recipient tables to add to
	 * contructed mail bean.
	 * 
	 * @param mail
	 *            mailbean to add addresses to.
	 * @throws SQLException
	 */
	private void findAssociatedRecipient(MailBean mail) throws SQLException {
		String recipientQuery = "SELECT address, address_type "
				+ "FROM email JOIN recipient ON email.email_id = recipient.email_id";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(recipientQuery);) {
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				while (resultSet.next()) {
					switch (resultSet.getInt("address_type")) {
					case 0:
						mail.getToField().add(resultSet.getString("address"));
						break;
					case 1:
						mail.getCcField().add(resultSet.getString("address"));
						break;
					case 2:
						mail.getBccField().add(resultSet.getString("address"));
						break;
					}
				}
			}
		}

	}

	/**
	 * this method is called to retrieve attachments and add them to the
	 * constructing mail bean.
	 * 
	 * @param mail
	 *            Bean to add attachments to.
	 * @throws SQLException
	 */
	private void findAssociatedAttachments(MailBean mail) throws SQLException {
		String attachmentQuery = "SELECT content_id, attach_name, attach_size, content "
				+ "FROM email JOIN attachment ON email.email_id = attachment.email_id";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(attachmentQuery);) {
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				while (resultSet.next()) {
					if (resultSet.getString("content_id") == null) {
						EmailAttachmentBuilder fileBuilder = EmailAttachment.attachment()
								.bytes(resultSet.getBytes("content"));
						fileBuilder.setName(resultSet.getString("attach_name"));
						fileBuilder.setInline(resultSet.getString("content_id"));
						EmailAttachment fileAttachment = fileBuilder.create();
						mail.getFileAttachments().add(fileAttachment);
					} else {
						EmailAttachmentBuilder embedBuilder = EmailAttachment.attachment()
								.bytes(resultSet.getBytes("content"));
						embedBuilder.setName(resultSet.getString("attach_name"));
						EmailAttachment embedAttachment = embedBuilder.create();
						mail.getEmbedAttachments().add(embedAttachment);
					}
				}
			}
		}
	}

	/**
	 * This method will return a list of mailbean which match the to field
	 * provided.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findByTo(java.lang.String)
	 * @param toField
	 *            string toFiled address used to query the database.
	 */
	@Override
	public ArrayList<MailBean> findByTo(String toField) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();

		String findByToQuery = "SELECT e.email_id, e.from_field, e.subject, e.text, e.html, e.sent_date, e.receive_date, f.folder_name, e.mail_status "
				+ "FROM email e JOIN folder f ON e.folder_id = f.folder_id "
				+ "JOIN recipient ON e.email_id = recipient.email_id " + "WHERE recipient.address_type = 0 "
				+ "AND recipient.address = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByToQuery);) {
			prepStmt.setString(1, toField);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByTo() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	/**
	 * This method is responsible for querying the database and will return a
	 * list of mailbeans based on the provided from field.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findByFrom(java.lang.String)
	 * @param fromField
	 *            the field used to query for results.
	 */
	@Override
	public ArrayList<MailBean> findByFrom(String fromField) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();

		String findByFromQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
				+ "FROM email JOIN folder ON email.folder_id = folder.folder_id " + "WHERE email.from_field = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByFromQuery);) {
			prepStmt.setString(1, fromField);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByFrom() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	/**
	 * This method will return a list of mailbeans which match the provided
	 * parameter.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findByCc(java.lang.String)
	 */
	@Override
	public ArrayList<MailBean> findByCc(String ccField) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();

		String findByCcQuery = "SELECT e.email_id, e.from_field, e.subject, e.text, e.html, e.sent_date, e.receive_date, f.folder_name, e.mail_status "
				+ "FROM email e JOIN folder f ON e.folder_id = f.folder_id "
				+ "JOIN recipient ON e.email_id = recipient.email_id " + "WHERE recipient.address_type = 1 "
				+ "AND recipient.address = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByCcQuery);) {
			prepStmt.setString(1, ccField);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByCc() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	/**
	 * This method will return a list of mailbeans which match the provided
	 * parameter.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findByBcc(java.lang.String)
	 */
	@Override
	public ArrayList<MailBean> findByBcc(String bccField) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();

		String findByBccQuery = "SELECT e.email_id, e.from_field, e.subject, e.text, e.html, e.sent_date, e.receive_date, f.folder_name, e.mail_status "
				+ "FROM email e JOIN folder f ON e.folder_id = f.folder_id "
				+ "JOIN recipient ON e.email_id = recipient.email_id " + "WHERE recipient.address_type = 2 "
				+ "AND recipient.address = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByBccQuery);) {
			prepStmt.setString(1, bccField);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByBcc() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	/**
	 * This method will return a list of mailbeans which match the provided
	 * parameter.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findBySubject(java.lang.String)
	 */
	@Override
	public ArrayList<MailBean> findBySubject(String subject) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();

		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
				+ "FROM email JOIN folder ON email.folder_id = folder.folder_id " + "WHERE email.subject = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);) {
			prepStmt.setString(1, subject);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findBySubject() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	/**
	 * This method will return a list of mailbeans which match the provided
	 * parameter.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findByDateSent(java.time.LocalDateTime)
	 */
	@Override
	public ArrayList<MailBean> findByDateSent(LocalDateTime sentDate) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();

		sentDate = sentDate.truncatedTo(ChronoUnit.DAYS);
		;
		Timestamp timeStamp = Timestamp.valueOf(sentDate);

		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
				+ "FROM email JOIN folder ON email.folder_id = folder.folder_id " + "WHERE email.sent_date = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);) {
			prepStmt.setTimestamp(1, timeStamp);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByDateSent() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	/**
	 * This method will return a list of mailbeans which match the provided
	 * parameter.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findByDateReceive(java.time.LocalDateTime)
	 */
	@Override
	public ArrayList<MailBean> findByDateReceive(LocalDateTime receivedDate) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();

		receivedDate = receivedDate.truncatedTo(ChronoUnit.DAYS);
		Timestamp timeStamp = Timestamp.valueOf(receivedDate);

		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
				+ "FROM email JOIN folder ON email.folder_id = folder.folder_id " + "WHERE email.receive_date = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);) {
			prepStmt.setTimestamp(1, timeStamp);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByDateReceive() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	/**
	 * This method will return a list of mailbeans which match the provided
	 * parameter.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findByFolder(java.lang.String)
	 */
	@Override
	public ArrayList<MailBean> findByFolder(String folderName) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();

		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
				+ "FROM email JOIN folder ON email.folder_id = folder.folder_id " + "WHERE folder.folder_name = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);) {
			prepStmt.setString(1, folderName);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByFolder() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	/**
	 * This method will return a constructed mailbean based on the requested
	 * email id.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findEmailByID(int)
	 */
	@Override
	public MailBean findEmailByID(int mailId) throws SQLException {
		MailBean foundEmail = new MailBean();

		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
				+ "FROM email JOIN folder ON email.folder_id = folder.folder_id " + "WHERE email.email_id = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);) {
			prepStmt.setInt(1, mailId);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				foundEmail = createMailBeans(resultSet).get(0);
			}
		}
		log.info("findByID() returned the email with an id of " + foundEmail.getId());
		return foundEmail;
	}

	/**
	 * this method will return a folder name when provided with a requested
	 * folder id.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findFolderNameById(int)
	 */
	@Override
	public String findFolderNameById(int folderId) throws SQLException {
		String folderName = "";

		String findByDateQuery = "SELECT folder_name " + "FROM folder " + "WHERE folder.folder_id = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);) {
			prepStmt.setInt(1, folderId);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				if (resultSet.next())
					folderName = resultSet.getString("folder_name");
			}
		}
		log.info("findByFolderNameById() returned the folderName " + folderName);
		return folderName;
	}

	/**
	 * This method will find which recipients exist as specified index. Purpose
	 * of this method is still unclear to me.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findRecipientByID(int)
	 */
	@Override
	public String findRecipientByID(int recipientId) throws SQLException {
		String receipientAddress = "";

		String findByDateQuery = "SELECT address " + "FROM recipient " + "WHERE recipient.recipient_id = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);) {
			prepStmt.setInt(1, recipientId);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				if (resultSet.next())
					receipientAddress = resultSet.getString("address");
			}
		}
		log.info("findRecipientByID() returned the receipient address " + receipientAddress);
		return receipientAddress;
	}

	/**
	 * This method will return a constructed email attachment based on provided
	 * id. Purpose of this method still unclear to me.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#findByAttachmentID(int)
	 */
	@Override
	public EmailAttachment findByAttachmentID(int attachmentId) throws SQLException {
		EmailAttachment foundAttachment;
		EmailAttachmentBuilder fBuilder = EmailAttachment.attachment();

		String findByAttachQuery = "SELECT content_id, attach_name, content " + "FROM attachment "
				+ "WHERE attachment.attach_id = ?";
		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findByAttachQuery);) {
			prepStmt.setInt(1, attachmentId);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				if (resultSet.next()) {
					fBuilder.setInline(resultSet.getString("content_id"));
					fBuilder.setName(resultSet.getString("attach_name"));
					fBuilder.bytes(resultSet.getBytes("content"));
					foundAttachment = fBuilder.create();
				} else
					foundAttachment = fBuilder.create();
			}
		}
		log.info("findByAttachmentID() returned the attachment with the name of" + foundAttachment.getName());
		return foundAttachment;
	}

	/**
	 * This method will update the folder of a specified mailbean.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#updateFolderInBean(int,
	 *      java.lang.String)
	 */
	@Override
	public int updateFolderInBean(int idOfMailBean, String folderName) throws SQLException {
		int numberOfFoldersChanged = -1;
		int folderId = retrieveFolderID(folderName);

		String folderInBeanUpdate = "UPDATE email SET email.folder_id = ? " + "WHERE email.email_id = ?";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(folderInBeanUpdate);) {
			prepStmt.setInt(1, folderId);
			prepStmt.setInt(2, idOfMailBean);

			numberOfFoldersChanged = prepStmt.executeUpdate();
		}
		log.info("updateFolderInBean() if it existed modified the folder in email with email_id of " + idOfMailBean
				+ " to have a folder id of " + folderId);
		return numberOfFoldersChanged;
	}

	/**
	 * This method will change the name of a specified folder.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#updateFolderName(int,
	 *      java.lang.String)
	 */
	@Override
	public int updateFolderName(int idOfFolder, String newFolderName) throws SQLException {
		int rowsUpdated = -1;
		if (idOfFolder > 1) // Prevents request of negative id or overwriting of
							// inbox[0] or sent[1] folder names.
		{
			String folderNameUpdate = "UPDATE folder SET folder_name = ? " + "WHERE folder.folder_id = ?";

			try (Connection connection = DriverManager.getConnection(url, user, password);
					PreparedStatement prepStmt = connection.prepareStatement(folderNameUpdate);) {
				prepStmt.setString(1, newFolderName);
				prepStmt.setInt(2, idOfFolder);

				rowsUpdated = prepStmt.executeUpdate();
			}
		}
		log.info("updateFolderName() updated " + rowsUpdated + " rows");

		return rowsUpdated;
	}

	/**
	 * This method will delete and email from the database.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#deleteMail(int)
	 */
	@Override
	public int deleteMail(int MailId) throws SQLException {
		int rowsDeleted = -1;
		String deleteMailQuery = "DELETE FROM email " + "WHERE email_id = ?";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(deleteMailQuery);) {
			prepStmt.setInt(1, MailId);
			rowsDeleted = prepStmt.executeUpdate();
		}
		log.info("deleteMail() deleted " + rowsDeleted + " # of records");
		return rowsDeleted;
	}

	/**
	 * This method will delete a folder from the database.
	 * 
	 * @see com.chrisdufort.persistence.MailDAO#deleteFolder(java.lang.String)
	 */
	@Override
	public int deleteFolder(String folderName) throws SQLException {
		int rowsDeleted = -1;
		String deleteFolderQuery = "DELETE FROM folder " + "WHERE folder_name = ?";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(deleteFolderQuery);) {
			prepStmt.setString(1, folderName);
			rowsDeleted = prepStmt.executeUpdate();
		}
		log.info("deleteFolder() deleted " + rowsDeleted + " # of records");
		return rowsDeleted;
	}



}
