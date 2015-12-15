package com.chrisdufort.persistence;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;


import com.chrisdufort.mailbean.MailBean;

import javafx.collections.ObservableList;
import jodd.mail.EmailAttachment;

/**
 * @author Christopher Dufort
 * @version 0.4.6-SNAPSHOT , Phase 4 - last modified 12/15/15
 * @since 0.2.1-SNAPSHOT
 * 
 *        This is the Mail Data Access Object Interface, its an interface
 *        responsible for describing the basic functions of any database access
 *        class working with mail beans.
 * 
 *        Any DAO implementing classes that should overload all of these
 *        methods.
 *        
 *        Read Methods changed to Observable Lists with Gui Implementation as of version 0.3.6
 */
public interface MailDAO {
	// Create (Insert)
	public int createEmail(MailBean mailBean) throws SQLException;

	public int createFolder(String folderName) throws SQLException;

	// Read (Select)
	public ObservableList<MailBean> findAll() throws SQLException;
	
	public ObservableList<MailBean> findTableAll() throws SQLException;

	public ObservableList<MailBean> findByTo(String toField) throws SQLException;

	public ObservableList<MailBean> findByFrom(String fromField) throws SQLException;

	public ObservableList<MailBean> findByCc(String ccField) throws SQLException;

	public ObservableList<MailBean> findByBcc(String bccField) throws SQLException;

	public ObservableList<MailBean> findBySubject(String subject) throws SQLException;

	public ObservableList<MailBean> findByDateSent(LocalDateTime sentDate) throws SQLException;

	public ObservableList<MailBean> findByDateReceive(LocalDateTime receivedDate) throws SQLException;

	public ObservableList<MailBean> findByFolder(String folderName) throws SQLException;

	public ObservableList<String> findAllFolderNames() throws SQLException;

	public MailBean findEmailByID(int mailId) throws SQLException;

	public String findFolderNameById(int folderId) throws SQLException;

	public String findRecipientByID(int recipientId) throws SQLException;

	public EmailAttachment findByAttachmentID(int attachmentId) throws SQLException;

	// Update
	public int updateFolderInBean(int idOfMailBean, String folderName) throws SQLException;

	public int updateFolderName(int idOfFolder, String newFolderName) throws SQLException;

	// Delete
	public int deleteMail(int MailId) throws SQLException; // TODO ALSO BY
															// OTHER?

	public int deleteFolder(String folderName) throws SQLException; // TODO ALSO
																	// by ID?

	//Formerly private
	public int retrieveFolderID(String folderName) throws SQLException;

}
