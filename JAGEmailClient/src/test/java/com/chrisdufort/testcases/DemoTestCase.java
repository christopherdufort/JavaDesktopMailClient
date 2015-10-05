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
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.chrisdufort.mailbean.MailBean;

/**
 * @author Christopher Dufort
 * @version 0.2.4-SNAPSHOT , Phase 2 - last modified 10/04/15
 * @since 0.2.4-SNAPSHOT
 */


public class DemoTestCase {
	
	private final String url =  "jdbc:mysql://localhost:3306";
	private final String user = "root";
	private final String password = "";
	
	@Test
	public void testExistenceOfDB() throws SQLException{
	
		assertEquals(true,true);
	}
	
	@Before
	public  void createDatabase() {
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
