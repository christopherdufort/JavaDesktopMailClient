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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.persistence.MailDAOImpl;
import com.chrisdufort.test.MethodLogger;

import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

/**
 * @author Christopher Dufort
 * @version 0.2.9-SNAPSHOT , Phase 2 - last modified 10/09/15
 * @since 0.2.4-SNAPSHOT
 */
public class DemoTestCase {

	@Rule
	public MethodLogger methodLogger = new MethodLogger();

	// Real programmers use logging, not System.out.println
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	// private final Properties progProperties = propertiesFile();

	// FIXME include properties file instead of hard coding
	private final String url = "jdbc:mysql://localhost:3306";
	private final String user = "root";
	private final String password = "";

	// Instantiation of DAOImpl class used to call instance methods.
	private MailDAOImpl myDBImplementation = new MailDAOImpl();
	@Ignore
	@Test
	public void testCreateEmail() throws SQLException {
		MailBean myBean = new MailBean();
		MailBean dbBean = new MailBean();

		int insertedEmailID = -1;

		myBean.setFromField("from@domain.com");
		myBean.getToField().add("to@domain.com");
		myBean.setSubjectField("this is a test subject");

		insertedEmailID = myDBImplementation.createEmail(myBean);
		dbBean = myDBImplementation.findEmailByID(insertedEmailID);

		if (insertedEmailID == -1)
			fail("testCreateEmail() - failed due to Not properly inserted");
		else
			assertEquals("testCreateEmail() - failed due to Unequal mailBeans ", myBean, dbBean);

	}
	@Ignore
	@Test
	public void testFullCreateEmail() throws SQLException {
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
		dbBean = myDBImplementation.findEmailByID(insertedEmailID);

		if (insertedEmailID == -1)
			fail("testFullCreateEmail() - failed due to Not properly inserted");
		else
			assertEquals("testFullCreateEmail() failed due to Unequal mailBeans ", myBean, dbBean);

	}
	@Ignore
	@Test
	public void testFindByTo() throws SQLException {
		ArrayList<MailBean> foundBeans = new ArrayList<>();
		String toField = "to@email.com";
		String fromField = "from@gmail.com";
		String subjectField = "Testing find by To subject";

		MailBean bean1 = new MailBean();
		bean1.setFromField(fromField);
		bean1.getToField().add(toField);
		bean1.setSubjectField(subjectField);

		MailBean bean2 = new MailBean();
		bean2.getToField().add(toField);
		bean2.setFromField(fromField);
		bean2.setSubjectField(subjectField);

		MailBean bean3 = new MailBean();
		bean3.getToField().add(toField);
		bean3.setFromField(fromField);
		bean3.setSubjectField(subjectField);

		myDBImplementation.createEmail(bean1);
		myDBImplementation.createEmail(bean2);
		myDBImplementation.createEmail(bean3);

		foundBeans = myDBImplementation.findByTo(toField);

		assertEquals("testFindByTo() failed due to incorrect found # ", 3, foundBeans.size());
	}
	@Ignore
	@Test
	public void testFindByCc() throws SQLException {
		ArrayList<MailBean> foundBeans = new ArrayList<>();
		String toField = "to.cc@email.com";
		String fromField = "from.cc@gmail.com";
		String subjectField = "Testing find by Cc subject";
		String ccField = "cc.cc@email.com";

		MailBean bean1 = new MailBean();
		bean1.setFromField(fromField);
		bean1.getToField().add(toField);
		bean1.getCcField().add(ccField);
		bean1.setSubjectField(subjectField);

		MailBean bean2 = new MailBean();
		bean2.getToField().add(toField);
		bean2.setFromField(fromField);
		bean1.getCcField().add(ccField);
		bean2.setSubjectField(subjectField);

		myDBImplementation.createEmail(bean1);
		myDBImplementation.createEmail(bean2);

		foundBeans = myDBImplementation.findByCc(ccField);

		assertEquals("testFindByCc() failed due to incorrect found # ", 2, foundBeans.size());
	}
	@Ignore
	@Test
	public void testFindByBcc() throws SQLException {
		ArrayList<MailBean> foundBeans = new ArrayList<>();
		String toField = "to.bcc@email.com";
		String fromField = "from.bcc@gmail.com";
		String subjectField = "Testing find by Bcc subject";
		String bccField = "bcc@email.com";

		MailBean bean1 = new MailBean();
		bean1.setFromField(fromField);
		bean1.getToField().add(toField);
		bean1.setSubjectField(subjectField);
		bean1.getBccField().add(bccField);

		myDBImplementation.createEmail(bean1);

		foundBeans = myDBImplementation.findByBcc(bccField);

		assertEquals("testFindByBcc() failed due to incorrect found # ", 1, foundBeans.size());
	}
	@Ignore
	@Test
	public void testFindByFrom() throws SQLException {

		ArrayList<MailBean> foundBeans = new ArrayList<>();
		String toField = "to.from@email.com";
		String fromField = "from.from@gmail.com";
		String subjectField = "Testing find by from subject";
		int expectedFinds = 1;

		EmailAttachmentBuilder fBuilder = EmailAttachment.attachment().file("headshot.jpg");
		EmailAttachment fileAttachment = fBuilder.create();

		MailBean fromBean = new MailBean();
		fromBean.getFileAttachments().add(fileAttachment);
		fromBean.setFromField(fromField);
		fromBean.getToField().add(toField);
		fromBean.setSubjectField(subjectField);

		int idOfCreatedBean = myDBImplementation.createEmail(fromBean);
		fromBean.setId(idOfCreatedBean);
		MailBean dbBean = myDBImplementation.findEmailByID(idOfCreatedBean);

		foundBeans = myDBImplementation.findByFrom(fromField);

		if (expectedFinds == foundBeans.size())
			assertEquals("testFindByFrom() failed due to difference of retrieval ", fromBean, dbBean);
		else
			fail("testFindByFrom() - failed due to incorrect # found");
	}
	@Ignore
	@Test
	public void testFindBySubject() throws SQLException {

		ArrayList<MailBean> foundBeans = new ArrayList<>();
		String toField = "to.email@gmail.com";
		String fromField = "from.email@gmail.com";
		String subjectField = "Testing find by SUBJECT subject";
		int expectedFinds = 1;

		EmailAttachmentBuilder fBuilder = EmailAttachment.attachment().file("headshot.jpg");
		EmailAttachment fileAttachment = fBuilder.create();

		MailBean fromBean = new MailBean();
		fromBean.getFileAttachments().add(fileAttachment);
		fromBean.setFromField(fromField);
		fromBean.getToField().add(toField);
		fromBean.setSubjectField(subjectField);

		int idOfCreatedBean = myDBImplementation.createEmail(fromBean);
		fromBean.setId(idOfCreatedBean);
		MailBean dbBean = myDBImplementation.findEmailByID(idOfCreatedBean);

		foundBeans = myDBImplementation.findBySubject(subjectField);

		if (expectedFinds == foundBeans.size())
			assertEquals("testFindBySubject() failed due to difference of retrieval ", fromBean, dbBean);
		else
			fail("testFindBySubject() - failed due to incorrect # found");
	}
	@Ignore
	@Test
	public void testFindByFolder() throws SQLException {
		String folderName = "draft";
		int expectedFinds = 2;
		ArrayList<MailBean> foundBeans = new ArrayList<>();

		foundBeans = myDBImplementation.findByFolder(folderName);
		assertEquals("testFindByFolder() failed due to difference of retrieval ", expectedFinds, foundBeans.size());
	}
	@Ignore
	@Test
	public void testFindAll() throws SQLException {
		int numberOfFieldsInDB = 2;
		int numberOfBeansFound = -1;
		ArrayList<MailBean> allBeans = myDBImplementation.findAll();

		numberOfBeansFound = allBeans.size();

		if (numberOfBeansFound == -1)
			fail("testFindAll() - failed due to query fail");
		else
			assertEquals("testFindAll() expected to find " + numberOfFieldsInDB + " emails, but instead found "
					+ numberOfBeansFound, numberOfFieldsInDB, numberOfBeansFound);
	}
	@Ignore
	@Test
	public void testFindByDateSent() throws SQLException {
		ArrayList<MailBean> foundBeans = new ArrayList<>();
		int expectedFinds = 2;
		MailBean bean1 = new MailBean();
		MailBean bean2 = new MailBean();
		MailBean bean3 = new MailBean();

		bean1.setFromField("date.sent@jodd.com");
		bean2.setFromField("date.sent@jodd.com");
		bean3.setFromField("date.sent@jodd.com");
		bean1.getToField().add("date.sent@jodd.com");
		bean2.getToField().add("date.sent@jodd.com");
		bean3.getToField().add("date.sent@jodd.com");
		bean1.setSubjectField("Testing sentDate subject");
		bean2.setSubjectField("Testing sentDate subject");
		bean3.setSubjectField("Testing sentDate subject");

		bean1.setDateSent(LocalDateTime.now());
		bean2.setDateSent(Timestamp.valueOf(LocalDateTime.now()));
		bean3.setDateSent(LocalDateTime.now().minusDays(1));

		myDBImplementation.createEmail(bean1);
		myDBImplementation.createEmail(bean2);
		myDBImplementation.createEmail(bean3);

		foundBeans = myDBImplementation.findByDateSent(LocalDateTime.now());

		assertEquals("testFindByDateSent() failed due to different # of retrieval ", expectedFinds, foundBeans.size());
	}
	@Ignore
	@Test
	public void testFindByDateReceived() throws SQLException {
		ArrayList<MailBean> foundBeans = new ArrayList<>();
		int expectedFinds = 1;
		MailBean bean1 = new MailBean();
		MailBean bean3 = new MailBean();

		bean1.setFromField("date.receive@jodd.com");
		bean3.setFromField("date.receive@jodd.com");
		bean1.getToField().add("date.receive@jodd.com");
		bean3.getToField().add("date.receive@jodd.com");
		bean1.setSubjectField("Testing ReceiveDate subject");
		bean3.setSubjectField("Testing ReceiveDate subject");

		bean1.setDateReceived(LocalDateTime.now());
		bean3.setDateReceived(LocalDateTime.now().minusDays(1));

		myDBImplementation.createEmail(bean1);
		myDBImplementation.createEmail(bean3);

		foundBeans = myDBImplementation.findByDateReceive(LocalDateTime.now());

		assertEquals("testFindByDateReceived() failed due to different # of retrieval ", expectedFinds,
				foundBeans.size());
	}
	@Ignore
	@Test //FIXME This test has shown some errors with null pointers *needs more indepth testing*
	public void testfindEmailByID() throws SQLException {
		int expectedId = 3, insertedEmailID = -1;

		ArrayList<String> to = new ArrayList<>();
		to.add("third@to.com");
		ArrayList<String> cc = new ArrayList<>();
		cc.add("third@cc.com");
		ArrayList<String> bcc = new ArrayList<>();
		bcc.add("third@bcc.com");

		MailBean myBean = new MailBean(to, "third@from.com", cc, bcc, "3rd subject", "3rd text");
		
		myBean.setDateSent(LocalDateTime.now());
		myBean.setDateReceived(LocalDateTime.now());
		insertedEmailID = myDBImplementation.createEmail(myBean);
		MailBean dbBean = myDBImplementation.findEmailByID(insertedEmailID);

		if (insertedEmailID == -1)
			fail("testfindEmailByID() - failed due to query failure");
		else if (expectedId == insertedEmailID)
			assertEquals("testfindEmailByID() - failed due improper retrieval by id ", myBean, dbBean);
	}
	@Ignore
	@Test
	public void testUpdateFolderInBean() throws SQLException {
		int emailId = 1, fieldsUpdated = -1;

		String folderName = "sent";

		fieldsUpdated = myDBImplementation.updateFolderInBean(emailId, folderName);
		MailBean modifiedBean = myDBImplementation.findEmailByID(1);

		if (fieldsUpdated < 0)
			fail("testFullInsertOfMailBean() - failed due to query");
		else
			assertEquals("testUpdateFolderInBean() failed due to name not changing", folderName,
					modifiedBean.getFolder());

	}
	@Ignore
	@Test
	public void testUpdateFolderName() throws SQLException {
		int idOfFolder = 3; // draft folder
		String newFolderName = "superDraft";

		int fieldsUpdated = -1;

		fieldsUpdated = myDBImplementation.updateFolderName(idOfFolder, newFolderName);
		String actualFolderName = myDBImplementation.findAllFolderNames().get(2); // 0
																					// is
																					// inbox,
																					// 1
																					// is
																					// sent,
																					// 2
																					// is
																					// draft
		if (fieldsUpdated < 0)
			fail("testUpdateFolderName() - failed due to query");
		else
			assertEquals("testUpdateFolderName() failed due to name not changing", "superDraft", actualFolderName);
	}
	@Ignore
	@Test
	public void testFindAllFolderNames() {
		ArrayList<String> foundFolderNames = null;
		ArrayList<String> allFolderNames = new ArrayList<>();

		allFolderNames.add("inbox");
		allFolderNames.add("sent");
		allFolderNames.add("draft");

		try {
			foundFolderNames = myDBImplementation.findAllFolderNames();
		} catch (SQLException e) {
			log.error("SQLException occured in testFindAllFolderNames()", e);
		}
		assertEquals("testFindAllFolderNames() failed due to unequal folder name lists", allFolderNames,
				foundFolderNames);
	}
	@Ignore
	@Test
	public void testDeleteFolder() {
		int expectedRowsDeleted = 1;
		int actualRowsDeleted = -1;

		try {
			actualRowsDeleted = myDBImplementation.deleteFolder("draft");
		} catch (SQLException e) {
			log.error("SQLException occured in testDeleteFolder()", e);
		}
		assertEquals("testDeleteFolder() failed due to un equal deleted #", expectedRowsDeleted, actualRowsDeleted);

	}
	@Ignore
	@Test
	public void testDeleteMail() throws SQLException {
		int expectedRowsDeleted = 1;
		int actualRowsDeleted = -1;
		int emailIdToDelete = 2;

		actualRowsDeleted = myDBImplementation.deleteMail(emailIdToDelete);

		if (actualRowsDeleted < 0)
			fail("testDeleteMail() - failed due to query");
		else if (!findIfAttachmentDeleted(emailIdToDelete)) {
			fail("testDeleteMail() - failed due to related attachment records not deleted");
		} else if (!findIfRecipientDeleted(emailIdToDelete)) {
			fail("testDeleteMail() - failed due to related recipient records not deleted");
		} else
			assertEquals("testDeleteMail() failed due incorrect amount deleted", expectedRowsDeleted,
					actualRowsDeleted);
	}
	@Ignore
	@Test
	public void testCreateFolder() throws SQLException {
		String newFolderName = "test";
		int expectedRowsCreated = 1;
		int actualRowsCreated = -1;

		actualRowsCreated = myDBImplementation.createFolder(newFolderName);

		if (actualRowsCreated < 0)
			fail("testCreateFolder() - failed due to query");
		else
			assertEquals("testCreateFolder() failed due to incorrect amount created", expectedRowsCreated,
					actualRowsCreated);
	}

	@Ignore // THIS TEST CURRENTLY FAILS ADDITIONAL TESTING AND INPUT INTO HOW
			// JODD EMAILATTACHMENTS WORK NEEDED
	@Test
	public void testFindByAttachmentID() throws SQLException {

		MailBean myBean = new MailBean();
		EmailAttachment returnedAttachment;

		myBean.setFromField("attachment.from@email.com");
		myBean.getToField().add("attachment.to@email.com");
		myBean.setSubjectField("attachment retrieval subject");

		EmailAttachmentBuilder fBuilder = EmailAttachment.attachment().file("headshot.jpg");
		fBuilder.setInline("headshot.jpg");
		fBuilder.setName("headshot.jpg");
		EmailAttachment fileAttachment = fBuilder.create();
		myBean.getFileAttachments().add(fileAttachment);

		myDBImplementation.createEmail(myBean);
		returnedAttachment = myDBImplementation.findByAttachmentID(1);
		// Custom equal
		boolean equalAttachments = false;
		log.debug("class" + (fileAttachment.getClass() + " " + returnedAttachment.getClass()));
		log.debug("size" + (fileAttachment.getSize() == returnedAttachment.getSize()));
		log.debug("contedID" + (fileAttachment.getContentId() + " " + returnedAttachment.getContentId()));
		log.debug("bytes" + (fileAttachment.toByteArray() == returnedAttachment.toByteArray()));
		if (fileAttachment.getClass() == returnedAttachment.getClass())
			if (fileAttachment.getSize() == returnedAttachment.getSize())
				if (fileAttachment.getContentId() == returnedAttachment.getContentId())
					if (fileAttachment.toByteArray() == returnedAttachment.toByteArray())
						equalAttachments = true;
		assertEquals("testFindByAttachmentID() failed due un equals attachments", true, equalAttachments);
	}
	@Ignore
	@Test
	public void testFindRecipientByID() throws SQLException {

		MailBean myBean = new MailBean();
		String expectedRecipient = "recipient.id.to@email.com";
		String actualRecipient = "";

		int recipientId = 1;

		myBean.setFromField("recipient.id.from@email.com");
		myBean.getToField().add(expectedRecipient);
		myBean.setSubjectField("recipient id retrieval subject");

		myDBImplementation.createEmail(myBean);
		actualRecipient = myDBImplementation.findRecipientByID(recipientId);

		assertEquals("testFindRecipientByID() failed due un equals recipients", expectedRecipient, actualRecipient);
	}
	@Ignore
	@Test
	public void testFindFolderNameById() throws SQLException {

		String expectedFolderName = "inbox";
		String actualFolderName = "";

		int folderId = 1;

		actualFolderName = myDBImplementation.findFolderNameById(folderId);

		assertEquals("testFindByFolderNameById() failed due un equals attachments", expectedFolderName,
				actualFolderName);
	}

	// ---------------------------------FOLLOWING ARE NOT TEST
	// CASES-----------------------------------------------------------------
	@Before
	public void createDatabase() {
		final String createDatabaseScript = loadAsString("createMailDB.sql");
		try (Connection connection = DriverManager.getConnection(url, user, password)) {
			for (String statement : splitStatements(new StringReader(createDatabaseScript), ";")) {
				connection.prepareStatement(statement).execute();
			}
		} catch (SQLException e) {
			// throw new RuntimeException("Failed creating database", e);
			// //FIXME this hides valuable info
			log.error("Failed creating database " + e.getMessage());
		}
	}

	private String loadAsString(final String path) {
		try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
				Scanner scanner = new Scanner(inputStream);) {
			return scanner.useDelimiter("\\A").next();
		} catch (IOException e) {
			throw new RuntimeException("Unable to close input stream.", e);
		}
	}

	private List<String> splitStatements(Reader reader, String statementDelimiter) {
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
		return line.startsWith("--") || line.startsWith("//") || line.startsWith("/*") || line.startsWith("/**");
	}

	private boolean findIfAttachmentDeleted(int emailId) throws SQLException {
		String url = "jdbc:mysql://localhost:3306/maildb";

		String checkingDeletedQuery = "SELECT attach_id " + "FROM attachment " + "WHERE attachment.email_id = ?";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(checkingDeletedQuery);) {
			prepStmt.setInt(1, emailId);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				if (resultSet.next()) {
					log.error("an attachment was not properly deleted on cascade with an id of "
							+ resultSet.getInt("attach_id"));
					return false;
				} else
					return true;
			}
		}
	}

	private boolean findIfRecipientDeleted(int emailId) throws SQLException {
		String url = "jdbc:mysql://localhost:3306/maildb";

		String checkingDeletedQuery = "SELECT recipient_id " + "FROM recipient " + "WHERE recipient.email_id = ?";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement prepStmt = connection.prepareStatement(checkingDeletedQuery);) {
			prepStmt.setInt(1, emailId);
			try (ResultSet resultSet = prepStmt.executeQuery();) {
				if (resultSet.next()) {
					log.error("an recipient was not properly deleted on cascade with an id of "
							+ resultSet.getInt("recipient_id"));
					return false;
				} else
					return true;
			}
		}
	}
}
