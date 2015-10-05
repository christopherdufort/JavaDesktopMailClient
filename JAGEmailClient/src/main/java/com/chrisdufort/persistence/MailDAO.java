package com.chrisdufort.persistence;
/**
 * @author Christopher Dufort
 * @version 0.2.1-SNAPSHOT , Phase 2 - last modified 09/30/15
 * @since 0.2.0-SNAPSHOT
 */
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Interface for CRUD methods used in populating/retrieving a database of MailBeans.
 * 
 * @author 1040570
 */
import com.chrisdufort.mailbean.MailBean;
/**
 * @author Christopher Dufort
 * @version 0.2.5-SNAPSHOT , Phase 2 - last modified 10/05/15
 * @since 0.2.1-SNAPSHOT
 */
public interface MailDAO {
	//Create (Insert)
	public int createEmail(MailBean mailBean) throws SQLException;
	public int createFolder(String folderName) throws SQLException;
	
	//Read (Select)
	public ArrayList<MailBean> findAll() throws SQLException;
	public ArrayList<String> findAllFolderNames() throws SQLException;
	public ArrayList<MailBean> findByTo(String toField) throws SQLException;
	public ArrayList<MailBean> findByFrom(String fromField) throws SQLException;
	public ArrayList<MailBean> findByCc(String ccField) throws SQLException;
	public ArrayList<MailBean> findByBcc(String bccField) throws SQLException;
	public ArrayList<MailBean> findBySubject(String subject) throws SQLException;
	public ArrayList<MailBean> findByDateSent(LocalDateTime sentDate) throws SQLException;
	public ArrayList<MailBean> findByDateReceive(LocalDateTime receivedDate) throws SQLException;
	public ArrayList<MailBean> findByFolder(String folderName) throws SQLException;	
	public MailBean	findByID(int mailId) throws SQLException;
	
	//Update
	 public int updateFolderInBean(int idOfMailBean, String folderName) throws SQLException;
	 public int updateFolderName(int idOfFolder, String newFolderName) throws SQLException;
	
	//Delete
	 public int deleteMail(int MailId) throws SQLException;  //TODO ALSO BY OTHER?
	 public int deleteFolder(String folderName) throws SQLException; //TODO ALSO by ID?
		
}
