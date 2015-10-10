package com.chrisdufort.test;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines JUnit rules that every test method in every test class
 * that instantiates this class will follow during its lifecycle.
 *
 * The rules in this class log the name of every test that is run and log every
 * test that fails.
 *
 * @author Christopher Dufort
 * @version 0.2.1-SNAPSHOT , last modified 09/15/15
 * @since 0.0.1-SNAPSHOT , Phase 1
 */
public class MethodLogger extends TestWatcher {

	// See method prepareLogger for the reason the logger is not
	// instantiated here.
	private Logger log;

	/**
	 * Constructor
	 */
	public MethodLogger() {
		super();
	}

	/**
	 * Whenever a test starts, this method logs the name of the test method as
	 * provided by the description.
	 *
	 * @param description
	 *            describes a test which is to be run or has been run
	 */
	@Override
	protected void starting(Description description) {
		prepareLogger(description);
		log.info("Starting test [{}]", description.getMethodName());
	}

	/**
	 * Whenever a test finishes, this method logs the name of the test method as
	 * provided by the description.
	 *
	 * @param description
	 *            describes a test which is to be run or has been run
	 */
	@Override
	protected void finished(Description description) {
		prepareLogger(description);
		log.info("Finished test [{}]", description.getMethodName());
		log.info("***********************************************************************************************");
	}

	/**
	 * Whenever a test fails, this method logs the name of the test method as
	 * provided by the description and the exception thrown.
	 *
	 * @param e
	 *            A throwable object normally an exception thrown when an error
	 *            occurs.
	 * @param description
	 *            describes a test which is to be run or has been run
	 */
	@Override
	protected void failed(Throwable e, Description description) {
		log.error("Failed test [{}]", description.getMethodName());
		log.error("Failure reason : " + e);
		// log.error("SEE STACKTRACE: ", e); //uncomment for full stack trace
		// logged.
	}

	/**
	 * Whenever a test succeeds, this method logs the name of the test method as
	 * provided by the description.
	 * 
	 * @param description
	 *            describes a test which is to be run or has been run
	 */
	@Override
	protected void succeeded(Description description) {
		prepareLogger(description);
		log.info("Succeeded test [{}]", description.getMethodName());

	}

	/**
	 * If logger is not initialized, initialize it with the name of the test
	 * class as provided by the description.
	 *
	 * @param description
	 *            describes a test which is to be run or has been run
	 *
	 */
	private void prepareLogger(Description description) {
		if (log == null) {
			log = LoggerFactory.getLogger(description.getClassName());
		}
	}
}
