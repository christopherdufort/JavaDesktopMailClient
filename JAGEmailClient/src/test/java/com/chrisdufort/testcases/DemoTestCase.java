package com.chrisdufort.testcases;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailaction.BasicSendAndReceive;
import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAOImpl;
import com.chrisdufort.test.MethodLogger;

import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

/**
 * @author Christopher Dufort
 * @version 0.2.7-SNAPSHOT , Phase 2 - last modified 10/08/15
 * @since 0.2.4-SNAPSHOT
 */
public class DemoTestCase {
	
	@Rule
	public MethodLogger methodLogger = new MethodLogger();
	
	// Real programmers use logging, not System.out.println
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	
	//FIXME include properties file instead of hard coding
	private final String url =  "jdbc:mysql://localhost:3306";
	private final String user = "root";
	private final String password = "";
	
	// Instantiation of DAOImpl class used to call instance methods.
	private MailDAOImpl myDBImplementation = new MailDAOImpl();
	
	@Ignore //PASS
	@Test
	public void testCreateEmail() throws SQLException{
		MailBean myBean = new MailBean();
		MailBean dbBean = new MailBean();
		
		int insertedEmailID = -1;

		myBean.setFromField("from@domain.com");
		myBean.getToField().add("to@domain.com");
		myBean.setSubjectField("this is a test subject");
		
		insertedEmailID = myDBImplementation.createEmail(myBean);	
		dbBean = myDBImplementation.findByID(insertedEmailID);
		
		if (insertedEmailID == -1)
			fail("testCreateEmail() - failed due to Not properly inserted");
		else
			assertEquals("testCreateEmail() - failed due to Unequal mailBeans ",myBean , dbBean);
		
	}
	@Ignore //PASS
	@Test
	public void testFullCreateEmail() throws SQLException{
		MailBean myBean = new MailBean();
		MailBean dbBean = new MailBean();
		
		int insertedEmailID = -1;
		
		myBean.setFromField("from@email.com");
		myBean.getToField().add("to@email.com");
		myBean.getCcField().add("cc@email.com");
		myBean.getBccField().add("bcc@email.com");
		myBean.setSubjectField("Full Insert Subject");
		myBean.setTextMessageField("This is text");
		myBean.setHtmlMessageField("<html>This is html</html>");
		
		EmailAttachmentBuilder fBuilder = EmailAttachment.attachment().file("headshot.jpg");
		EmailAttachment fileAttachment = fBuilder.create();
		myBean.getFileAttachments().add(fileAttachment);

		EmailAttachmentBuilder eBuilder = EmailAttachment.attachment().bytes(new File("code_dragon_error.jpg"));
		eBuilder.setInline("code_dragon_error.jpg");
		EmailAttachment embedAttachment = eBuilder.create();
		myBean.getEmbedAttachments().add(embedAttachment);
		
		myBean.setDateSent(LocalDateTime.now());
		myBean.setDateReceived(LocalDateTime.now());
		myBean.setFolder("sent");
		myBean.setMailStatus(2);
		
		insertedEmailID = myDBImplementation.createEmail(myBean);
		dbBean = myDBImplementation.findByID(insertedEmailID);
		 

		if (insertedEmailID == -1)
			fail("testFullCreateEmail() - failed due to Not properly inserted");
		else
			assertEquals("testFullCreateEmail() failed due to Unequal mailBeans ", myBean, dbBean);
						
	}
	@Ignore //PASS
	@Test
	public void testFindAll() throws SQLException{
		int numberOfFieldsInDB = 2;
		int numberOfBeansFound = -1;
		ArrayList<MailBean> allBeans = myDBImplementation.findAll();
		
		numberOfBeansFound = allBeans.size();
		
		if (numberOfBeansFound == -1)
			fail("testFindAll() - failed due to query fail");
		else
			assertEquals("testFindAll() expected to find " + numberOfFieldsInDB + " emails, but instead found " + numberOfBeansFound , numberOfFieldsInDB, numberOfBeansFound);
	}
	
	
	@Test
	public void testUpdateFolderInBean() throws SQLException{
		int emailId = 1, fieldsUpdated = -1;
		
		int expectedFolderId = 2; // =="sent"
		String folderName = "sent";
		
		fieldsUpdated = myDBImplementation.updateFolderInBean(emailId, folderName);
		MailBean modifiedBean = myDBImplementation.findByID(1);
		
		if (fieldsUpdated < 0)
			fail("testFullInsertOfMailBean() - failed due to query");
		else
			assertEquals("testUpdateFolderInBean() failed due to name not changing", folderName, modifiedBean.getFolder() );
			
	}
	@Test
	public void testUpdateFolderName() throws SQLException{
		int idOfFolder = 3; //draft folder
		String newFolderName = "superDraft";
		
		
		int fieldsUpdated = -1;
		
		fieldsUpdated = myDBImplementation.updateFolderName(idOfFolder, newFolderName);
		String actualFolderName = myDBImplementation.findAllFolderNames().get(2); //0 is inbox, 1 is sent, 2 is draft
		if (fieldsUpdated < 0)
			fail("testUpdateFolderName() - failed due to query");
		else
			assertEquals("testUpdateFolderName() failed due to name not changing", "superDraft" , actualFolderName);
	}
	
	@Ignore //PASS
	@Test
	public void testFindAllFolderNames(){
		ArrayList<String> foundFolderNames = null;
		ArrayList<String> allFolderNames = new ArrayList<>();
		
		allFolderNames.add("inbox");
		allFolderNames.add("sent");
		allFolderNames.add("draft");
		
		try {
			foundFolderNames = myDBImplementation.findAllFolderNames();
		} catch (SQLException e) {
			log.error("SQLException occured in testFindAllFolderNames()" , e);
		}
		assertEquals("testFindAllFolderNames() failed due to unequal folder name lists", allFolderNames, foundFolderNames);
	}
	
	@Ignore //PASS
	@Test
	public void testDeleteFolder(){
		int expectedRowsDeleted = 1;
		int actualRowsDeleted = -1;
		
		try {
			actualRowsDeleted = myDBImplementation.deleteFolder("draft");
		} catch (SQLException e) {
			log.error("SQLException occured in testDeleteFolder()" , e);
		}
		assertEquals("testDeleteFolder() failed due to un equal deleted #", expectedRowsDeleted, actualRowsDeleted);
		
	}
	@Ignore //PASS
	@Test
	public void testDeleteMail() throws SQLException{	
		int expectedRowsDeleted = 1;
		int actualRowsDeleted = -1;
		
		actualRowsDeleted = myDBImplementation.deleteMail(2);
		
		
		if (actualRowsDeleted < 0)
			fail("testDeleteMail() - failed due to query");
		else
			assertEquals("testDeleteMail() failed due incorrect amount deleted", expectedRowsDeleted, actualRowsDeleted);
	}
	
	@Ignore //PASS
	@Test
	public void testCreateFolder() throws SQLException{
		String newFolderName = "test";
		int expectedRowsCreated = 1;
		int actualRowsCreated = -1;

		
		actualRowsCreated = myDBImplementation.createFolder(newFolderName);
		
		if (actualRowsCreated < 0)
			fail("testCreateFolder() - failed due to query");
		else
			assertEquals("testCreateFolder() failed due to incorrect amount created", expectedRowsCreated, actualRowsCreated);
	}
		

	
	//---------------------------------FOLLOWING ARE NOT TEST CASES-----------------------------------------------------------------
	@Before
	public void createDatabase() {
		final String  createDatabaseScript = loadAsString("createMailDB.sql");
		try(Connection connection = DriverManager.getConnection(url, user, password)) 
		{
			for (String statement : splitStatements(new StringReader(createDatabaseScript), ";")) 
			{
				connection.prepareStatement(statement).execute();
			}
		} catch(SQLException e){
			//throw new RuntimeException("Failed creating database", e); //FIXME this is useless
			log.error("Failed creating database " + e.getMessage()); 	
		}
	}
	
	private String loadAsString(final String path){
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
				Scanner scanner = new Scanner(inputStream);) 
		{
			return scanner.useDelimiter("\\A").next();
		}catch (IOException e) {
			throw new RuntimeException("Unable to close input stream.", e);
		}		
	}
	private List<String> splitStatements(Reader reader,String statementDelimiter) {
		final BufferedReader bufferedReader = new BufferedReader(reader);
		final StringBuilder sqlStatement = new StringBuilder();
		final List<String> statements = new LinkedList<String>();
		try {
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				if (line.isEmpty() || isComment(line)) {
					continue;
				}
				sqlStatement.append(line);
				if (line.endsWith(statementDelimiter)) {
					statements.add(sqlStatement.toString());
					sqlStatement.setLength(0);
				}
			}
			return statements;
		} catch (IOException e) {
			throw new RuntimeException("Failed parsing sql", e);
		}
	}

	private boolean isComment(final String line) {
		return line.startsWith("--") || line.startsWith("//")
				|| line.startsWith("/*") || line.startsWith("/**");
	}
}
