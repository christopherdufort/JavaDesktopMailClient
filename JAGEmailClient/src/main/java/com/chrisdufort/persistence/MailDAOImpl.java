package com.chrisdufort.persistence;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailbean.MailBean;

import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;
import jodd.mail.att.FileAttachment;


/**
 * @author Christopher Dufort
 * @version 0.2.7-SNAPSHOT , Phase 2 - last modified 10/08/15
 * @since 0.2.0-SNAPSHOT
 */
public class MailDAOImpl implements MailDAO{
	private final Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private final String url = "jdbc:mysql://localhost:3306/maildb";
	private final String user = "root";
	private final String password= ""; //FIXME create cutom user and give permissions
	
	public MailDAOImpl(){
		super();
	}
			
	@Override
	public int createEmail(MailBean mailBean) throws SQLException {
		int emailId = -1;
		String createEmailInsert = "INSERT INTO email(from_field, subject,text,html,sent_date,receive_date, folder_id, mail_status) "
								+ "VALUES(?,?,?,?,?,?,?,?)";
		String createRecipientInsert = "INSERT INTO recipient(address, address_type, email_id) "
									+ "VALUES (?,?,?)";
		String createAttachmentInsert = "INSERT INTO attachment(content_id, attach_name, attach_size, content, email_id) "
										+ "VALUES (?,?,?,?,?)";
		try (Connection connection = DriverManager.getConnection(url,user,password);
				PreparedStatement ps1 = connection.prepareStatement(createEmailInsert,PreparedStatement.RETURN_GENERATED_KEYS);
				PreparedStatement ps2 = connection.prepareStatement(createRecipientInsert);
				PreparedStatement ps3 = connection.prepareStatement(createAttachmentInsert);
				) 
		{
			connection.setAutoCommit(false);
			ps1.setString(1, mailBean.getFromField());
			ps1.setString(2, mailBean.getSubjectField());
			ps1.setString(3, mailBean.getTextMessageField());
			ps1.setString(4, mailBean.getHtmlMessageField());
			ps1.setTimestamp(5, mailBean.getDateSentAsTimestamp());
			ps1.setTimestamp(6, mailBean.getDateReceivedAsTimestamp());
			
			int folderId = retrieveFolderID(mailBean.getFolder());
			if (folderId == -1)
				folderId = 1; //Default to inbox
			ps1.setInt(7, folderId);
			
			ps1.setInt(8, mailBean.getMailStatus());
			
			ps1.executeUpdate();
			
			ResultSet results = ps1.getGeneratedKeys(); //Returns a result set of primary keys inserted.
			
			if (results != null && results.next())
				emailId = results.getInt(1);
			
			for(String address : mailBean.getToField())
			{
				ps2.setString(1, address);
				ps2.setInt(2, 0);
				ps2.setInt(3, emailId);
				ps2.executeUpdate();
			}
			for(String address : mailBean.getCcField())
			{
				ps2.setString(1, address);
				ps2.setInt(2, 1);
				ps2.setInt(3, emailId);
				ps2.executeUpdate();
			}
			for(String address : mailBean.getBccField())
			{
				ps2.setString(1, address);
				ps2.setInt(2, 2);
				ps2.setInt(3, emailId);
				ps2.executeUpdate();
			}
			
			for(EmailAttachment embed  : mailBean.getEmbedAttachments())
			{
				ps3.setString(1, embed.getContentId());
				ps3.setString(2, embed.getName());
				ps3.setInt(3, embed.getSize());
				ps3.setBytes(4, embed.toByteArray());
				ps3.setInt(5, emailId);
				ps3.executeUpdate();
			}
			for(EmailAttachment attach  : mailBean.getFileAttachments())
			{
				ps3.setString(1, null);
				ps3.setString(2, attach.getName());
				ps3.setInt(3, attach.getSize());
				ps3.setBytes(4, attach.toByteArray());
				ps3.setInt(5, emailId);
				ps3.executeUpdate();
			}
					
			//if (mailBean.getFileAttachments().size() > 0|| mailBean.getEmbedAttachments().size() > 0) FIXME delete this line	
			connection.commit();
		}
		log.info("createEmail() inserted an email with the id of " + emailId);
		return emailId;
	}

	private int retrieveFolderID(String folderName) throws SQLException{
		int idOfFolder = -1;
		String selectFolderIdQuery = "SELECT folder_id "
									+ "FROM folder "
									+ "WHERE folder_name = ? ";
		try(Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(selectFolderIdQuery);)
		{	
			prepStmt.setString(1, folderName);
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				while(resultSet.next())
				{
					idOfFolder = resultSet.getInt("folder_id");
				}
			}
		}	
		return idOfFolder;
	}
	@Override
	public int createFolder(String folderName) throws SQLException {
		int insertedRows = 0;
		String createFolderInsert = "INSERT INTO folder(folder_name) "
							+ "VALUES(?)";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(createFolderInsert);)
		{
			prepStmt.setString(1, folderName);
			insertedRows = prepStmt.executeUpdate();
		}
		log.info("createFolder() inserted " + insertedRows + " folders");
		return insertedRows;
	}
	

	@Override
	public ArrayList<MailBean> findAll() throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();
		
		String findAllQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
						   + "FROM email JOIN folder ON email.folder_id = folder.folder_id";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(findAllQuery);)
		{
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findAll() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}
	
	@Override
	public ArrayList<String> findAllFolderNames() throws SQLException{
		ArrayList<String> folderNames = new ArrayList<>();
		//ORDER BY included because folder_name is UNIQUE and becomes incorrectly ordered because of InnoDB
		String findFolderQuery = "SELECT folder_name "
								+ "FROM folder "
								+ "ORDER BY folder_id";
		try(Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(findFolderQuery);) //TODO does not need to be a prepared statement
		{	
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				while(resultSet.next())
				{	
					folderNames.add(resultSet.getString("folder_name"));
				}
			}
		}			
		return folderNames;
		
	}
	
	private ArrayList<MailBean> createMailBeans(ResultSet resultSet) throws SQLException {
		ArrayList<MailBean> createdEmails = new ArrayList<>();
		
		try{
			while(resultSet.next()){
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
		}finally{
			//FIXME should i null the result set or something here?
		}
		log.info("createMailBeans() created " + createdEmails.size() + " complete email beans.");
		return createdEmails;
	}
	
	private void findAssociatedRecipient(MailBean mail) throws SQLException{
		String recipientQuery = "SELECT address, address_type "
								+ "FROM email JOIN recipient ON email.email_id = recipient.email_id";
		
		try(Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(recipientQuery);)
		{
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				while(resultSet.next())
				{
					switch(resultSet.getInt("address_type"))
					{
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
	
	private void findAssociatedAttachments(MailBean mail) throws SQLException{
		String attachmentQuery = "SELECT content_id, attach_name, attach_size, content "
				+ "FROM email JOIN attachment ON email.email_id = attachment.email_id";

		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(attachmentQuery);)
		{
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				while(resultSet.next())
				{
					if (resultSet.getString("content_id") == null)
					{
						EmailAttachmentBuilder fileBuilder = EmailAttachment.attachment().bytes(resultSet.getBytes("content"));
						fileBuilder.setName(resultSet.getString("attach_name"));
						fileBuilder.setInline(resultSet.getString("content_id"));
						EmailAttachment fileAttachment = fileBuilder.create();
						mail.getFileAttachments().add(fileAttachment);
					}
					else
					{
						EmailAttachmentBuilder embedBuilder = EmailAttachment.attachment().bytes(resultSet.getBytes("content"));
						embedBuilder.setName(resultSet.getString("attach_name"));
						EmailAttachment embedAttachment = embedBuilder.create();
						mail.getEmbedAttachments().add(embedAttachment);
					}
				}
			}
		}
	}

	@Override
	public ArrayList<MailBean> findByTo(String toField) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();
		
		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
						   + "FROM email JOIN folder ON email.folder_id = folder.folder_id "
						   + "JOIN recipient ON email.email_id = recipient.email_id "
						   + "WHERE recipient.adress_type = 0 "
						   + "AND recipient.address = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);)
		{
			prepStmt.setString(1, toField);
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByFrom() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	@Override
	public ArrayList<MailBean> findByFrom(String fromField) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();
		
		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
						   + "FROM email JOIN folder ON email.folder_id = folder.folder_id "
						   + "WHERE email.from_field = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);)
		{
			prepStmt.setString(1, fromField);
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByFrom() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	@Override
	public ArrayList<MailBean> findByCc(String ccField) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();
		
		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
						   + "FROM email JOIN folder ON email.folder_id = folder.folder_id "
						   + "JOIN recipient ON email.email_id = recipient.email_id "
						   + "WHERE recipient.adress_type = 1 "
						   + "AND recipient.address = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);)
		{
			prepStmt.setString(1, ccField);
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByCc() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	@Override
	public ArrayList<MailBean> findByBcc(String bccField) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();
		
		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
						   + "FROM email JOIN folder ON email.folder_id = folder.folder_id "
						   + "JOIN recipient ON email.email_id = recipient.email_id "
						   + "WHERE recipient.adress_type = 1 "
						   + "AND recipient.address = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);)
		{
			prepStmt.setString(1, bccField);
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByBcc() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	@Override
	public ArrayList<MailBean> findBySubject(String subject) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();
		
		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
						   + "FROM email JOIN folder ON email.folder_id = folder.folder_id "
						   + "WHERE email.subject = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);)
		{
			prepStmt.setString(1, subject);
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findBySubject() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	@Override
	public ArrayList<MailBean> findByDateSent(LocalDateTime sentDate) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();
		Timestamp timeStamp = Timestamp.valueOf(sentDate);
		
		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
						   + "FROM email JOIN folder ON email.folder_id = folder.folder_id "
						   + "WHERE email.sent_date = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);)
		{
			prepStmt.setTimestamp(1, timeStamp);
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByDateSent() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	@Override
	public ArrayList<MailBean> findByDateReceive(LocalDateTime receivedDate) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();
		Timestamp timeStamp = Timestamp.valueOf(receivedDate);
		
		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
						   + "FROM email JOIN folder ON email.folder_id = folder.folder_id "
						   + "WHERE email.receive_date = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);)
		{
			prepStmt.setTimestamp(1, timeStamp);
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByDateReceive() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	@Override
	public ArrayList<MailBean> findByFolder(String folderName) throws SQLException {
		ArrayList<MailBean> foundEmails = new ArrayList<>();
		
		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
						   + "FROM email JOIN folder ON email.folder_id = folder.folder_id "
						   + "WHERE folder.folder_name = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);)
		{
			prepStmt.setString(1, folderName);
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				foundEmails = createMailBeans(resultSet);
			}
		}
		log.info("findByFolder() returned " + foundEmails.size() + " emails");
		return foundEmails;
	}

	@Override
	public MailBean findByID(int mailId) throws SQLException {
		MailBean foundEmail = new MailBean();
		
		String findByDateQuery = "SELECT email_id, from_field, subject, text, html, sent_date, receive_date, folder_name, mail_status "
						   + "FROM email JOIN folder ON email.folder_id = folder.folder_id "
						   + "WHERE email.email_id = ?";
		try(Connection connection = DriverManager.getConnection(url, user, password);
			PreparedStatement prepStmt = connection.prepareStatement(findByDateQuery);)
		{
			prepStmt.setInt(1, mailId);
			try(ResultSet resultSet = prepStmt.executeQuery();)
			{
				foundEmail = createMailBeans(resultSet).get(0);
			}
		}
		log.info("findByID() returned the email with an id of " + foundEmail.getId());
		return foundEmail;
	}

	@Override
	public int updateFolderInBean(int idOfMailBean, String folderName) throws SQLException {
		int numberOfFoldersChanged = -1;
		int folderId = retrieveFolderID(folderName);
		
		String folderInBeanUpdate = "UPDATE email SET email.folder_id = ? "
				+ "WHERE email.email_id = ?";
		
		try (Connection connection = DriverManager.getConnection(url,user,password);
				PreparedStatement prepStmt = connection.prepareStatement(folderInBeanUpdate);)
		{
			prepStmt.setInt(1, folderId);
			prepStmt.setInt(2, idOfMailBean);
			
			numberOfFoldersChanged = prepStmt.executeUpdate();
		}
		log.info("updateFolderInBean() if it existed modified the folder in email with email_id of " + idOfMailBean + " to have a folder id of " + folderId);
		return numberOfFoldersChanged;
	}

	@Override
	public int updateFolderName(int idOfFolder, String newFolderName) throws SQLException {
		int rowsUpdated = -1;
		if ( idOfFolder > 1) //Prevents request of negative id or overwriting of inbox[0] or sent[1] folder names.
		{
			String folderNameUpdate = "UPDATE folder SET folder_name = ? "
					+ "WHERE folder.folder_id = ?";
			
			try (Connection connection = DriverManager.getConnection(url,user,password);
					PreparedStatement prepStmt = connection.prepareStatement(folderNameUpdate);)
			{
				prepStmt.setString(1, newFolderName);
				prepStmt.setInt(2, idOfFolder);
				
				rowsUpdated = prepStmt.executeUpdate();
			}
		}
		log.info("updateFolderName() updated " + rowsUpdated + " rows");
		
		return rowsUpdated;
	}

	@Override
	public int deleteMail(int MailId) throws SQLException {
		int rowsDeleted = -1;
		String deleteMailQuery = "DELETE FROM email "
							+ "WHERE email_id = ?";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(deleteMailQuery);) 
		{
			prepStmt.setInt(1, MailId);
			rowsDeleted = prepStmt.executeUpdate();
		}
		log.info("deleteMail() deleted " + rowsDeleted + " # of records");
		return rowsDeleted;
	}

	@Override
	public int deleteFolder(String folderName) throws SQLException {
		int rowsDeleted = -1;
		String deleteFolderQuery = "DELETE FROM folder "
									+ "WHERE folder_name = ?";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(deleteFolderQuery);) 
		{
			prepStmt.setString(1, folderName);
			rowsDeleted = prepStmt.executeUpdate();
		}
		log.info("deleteFolder() deleted " + rowsDeleted + " # of records");
		return rowsDeleted;
	}


}
