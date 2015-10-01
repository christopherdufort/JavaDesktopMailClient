package com.chrisdufort.persistence;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.chrisdufort.mailbean.MailBean;


/**
 * @author Christopher Dufort
 * @version 0.2.1-SNAPSHOT , Phase 2 - last modified 09/30/15
 * @since 0.2.0-SNAPSHOT
 */
public class MailDAOImpl implements MailDAO{

	@Override
	public int create(MailBean mailBean) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int create(String folderName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<MailBean> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MailBean> findByTo(String toField) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MailBean> findByFrom(String fromField) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MailBean> findByCc(String ccField) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MailBean> findByBcc(String bccField) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MailBean> findBySubject(String subject) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MailBean> findByDateSent(LocalDateTime sentDate) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MailBean> findByDateReceive(LocalDateTime receivedDate) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MailBean> findByFolder(String folderName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MailBean findByID(int mailId) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateFolderInBean(int idOfMailBean, String folderName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateFolderName(int idOfFolder, String newFolderName) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteMail(int MailId) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteFolder(int folderId) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}


}
