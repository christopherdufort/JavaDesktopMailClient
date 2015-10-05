package com.chrisdufort.testcases;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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

/**
 * @author Christopher Dufort
 * @version 0.2.5-SNAPSHOT , Phase 2 - last modified 10/05/15
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
	
	
	@Test
	public void testInsertOfMailBean() throws SQLException{
		MailBean myBean = new MailBean();
		int actualCreatedEmails = -1;
		int expectedCreatedEmails = 1;
		myBean.setFromField("from@domain.com");
		myBean.getToField().add("to@gmmail.com");
		myBean.setSubjectField("this is a test subject");
		
		actualCreatedEmails = myDBImplementation.createEmail(myBean);	
		assertSame("testInsertOfMailBean() failued due to unqual deleted #", expectedCreatedEmails, actualCreatedEmails);
		
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
		assertEquals("testFindAllFolderNames() failed due to un equal deleted #", expectedRowsDeleted, actualRowsDeleted);
		
	}
	@Ignore
	@Test
	public void testExistenceOfDB() throws SQLException{
	
		assertEquals(true,true);
	}
	@Ignore
	@Test
	public void testExistedOfSeed() throws SQLException{
		assertEquals(true,true);
	}
	
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
			//throw new RuntimeException("Failed creating database", e);
			System.out.println("!!!!!!!!!!!!!!!!!!!!! " + e.getMessage()); //FIXME remove this println...
			
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
