package com.chrisdufort.test.mailaction;

import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chrisdufort.mailaction.BasicSendAndReceive;
import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.properties.MailConfigBean;
import com.chrisdufort.test.MethodLogger;

import jodd.mail.EmailAttachment;
import jodd.mail.EmailAttachmentBuilder;

/**
 * Our Test class used to determine if an email can be properly created and
 * sent, properly retrieved and recreated, and if the messages and their
 * contents are equal.
 * 
 * THE MINIMUM REQUIRED TO SEND ANY JODD EMAIL IS A TO, FROM, AND SUBJECT FIELD
 * FILLED.
 * 
 * @author Christopher Dufort
 * @version 0.2.1-SNAPSHOT , Phase 2 - last modified 09/24/15
 * @since 0.0.1-SNAPSHOT , Phase 1
 */
public class MailActionTest {

	/**
	 * A Rule is implemented as a class with methods that are associated with
	 * the lifecycle of a unit test. These methods run when required. Avoids the
	 * need to cut and paste code into every test method.
	 * 
	 * Our MethodLogger class will be used to log method names and success or
	 * failures of each JUnit test.
	 */
	@Rule
	public MethodLogger methodLogger = new MethodLogger();

	// Real programmers use logging, not System.out.println
	private final Logger log = LoggerFactory.getLogger(getClass().getName());

	// The following fields are shared between all of the test cases.
	// Sending account
	private MailConfigBean sendConfigBean = new MailConfigBean("smtp.gmail.com", "jafg.send@gmail.com", "jafgsend514");
	// Receiving account
	private MailConfigBean receiveConfigBean = new MailConfigBean("imap.gmail.com", "jafg.receive@gmail.com",
			"jafgreceive514");
	// Copy(cc) account
	private MailConfigBean copyConfigBean = new MailConfigBean("imap.gmail.com", "jafg.copy@gmail.com", "jafgcopy514");
	// Blind(bcc) account
	private MailConfigBean blindConfigBean = new MailConfigBean("imap.gmail.com", "jafg.blind@gmail.com",
			"jafgblind514");
	// Instantiation of test class
	private BasicSendAndReceive basicSendAndReceive = new BasicSendAndReceive();

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendEmail(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test a basic plain text email message is created using the basic
	 * constructor, sent with simple send, received and compared. Only the
	 * fields in the basic constructor are tested to(1) , cc(0) bcc(0)
	 * ,subject(1) , message(1)
	 */
	@Ignore
	@Test
	public void testBasicSendEmail() {
		// Create mailBean
		ArrayList<String> toList = new ArrayList<>();
		toList.add(receiveConfigBean.getUserEmailAddress());
		
		String fromField = sendConfigBean.getUserEmailAddress();
		
		ArrayList<String> ccList = new ArrayList<>();
		
		ArrayList<String> bccList = new ArrayList<>();
		
		String subject = "A basic Test Message - 00";
		String message = "This is the plain text of the message - 00.";

		// Basic constructor (arraylist to, string from, arraylist cc, arraylist
		// bcc, string subjct, string text )
		MailBean mailBeanSend = new MailBean(toList, fromField, ccList, bccList, subject, message);

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The sent basic message did not match the received message", mailBeanSend, mailBeanReceive.get(0));
	}
	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendEmail(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test a basic plain text email message is created using the basic
	 * constructor, sent with simple send, received and compared.
	 * 
	 * This test uses a blank subject (empty string space)
	 * 
	 * Email will send but assert will fail based on null subject on receiving end vs empty spaced subject on send.
	 */
	@Ignore //FIXME current status of equals checks for nulls and trims fields causing this test to null pointer
	@Test
	public void testBasicSendNoSubjectEmail() {
		// Create mailBean
		ArrayList<String> toList = new ArrayList<>();
		toList.add(receiveConfigBean.getUserEmailAddress());
		
		String fromField = sendConfigBean.getUserEmailAddress();
		
		ArrayList<String> ccList = new ArrayList<>();
		
		ArrayList<String> bccList = new ArrayList<>();
		
		String subject = " ";
		String message = "This is the plain text of the message with no subject - 01.";

		// Basic constructor (arraylist to, string from, arraylist cc, arraylist
		// bcc, string subjct, string text )
		MailBean mailBeanSend = new MailBean(toList, fromField, ccList, bccList, subject, message);

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertNotEquals("These messages will not match and that is okay", mailBeanSend, mailBeanReceive.get(0));
	}

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendWithEmbeddedAndAttachment(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 *
	 * In this test a complex HTML/Attachment/Embed email message is created
	 * using the default constructor, sent, received and compared. All of the
	 * setters and getters are tested using this email.
	 */
	@Ignore
	@Test
	public void testHandBuiltSendEmail() {
		// Testing no parameter default constructor.
		// Testing all setter methods.
		MailBean mailBeanSend = new MailBean();

		// Create mailBean
		mailBeanSend.getToField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(copyConfigBean.getUserEmailAddress());

		String fromField = sendConfigBean.getUserEmailAddress();
		mailBeanSend.setFromField(fromField);

		mailBeanSend.getCcField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(blindConfigBean.getUserEmailAddress());

		String subject = "A Complex Test Message - 25";
		mailBeanSend.setSubjectField(subject);

		String plainText = "This is the plain text of the message (hidden) - 25.";
		mailBeanSend.setTextMessageField(plainText);

		String htmlText = "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>This is the HTML text of the message (visible) - 25.</h1><h2>Here is an image of a code dragon which is embedded in this email.</h2>"
				+ "<img src='cid:code_dragon_error.jpg'><h2>Rawr! My program has an Error!!</h2></body></html>";
		mailBeanSend.setHtmlMessageField(htmlText);

		String folder = "Testing";
		mailBeanSend.setFolder(folder);

		int mailStatus = 1;
		mailBeanSend.setMailStatus(mailStatus);

		LocalDateTime sentDate = LocalDateTime.MIN;
		mailBeanSend.setDateSent(sentDate);
		LocalDateTime receiveDate = LocalDateTime.MAX;
		mailBeanSend.setDateReceived(receiveDate);

		EmailAttachmentBuilder fBuilder = EmailAttachment.attachment().file("headshot.jpg");
		EmailAttachment fileAttachment = fBuilder.create();
		mailBeanSend.getFileAttachments().add(fileAttachment);

		EmailAttachmentBuilder eBuilder = EmailAttachment.attachment().bytes(new File("code_dragon_error.jpg"));
		eBuilder.setInline("code_dragon_error.jpg");
		EmailAttachment embedAttachment = eBuilder.create();
		mailBeanSend.getEmbedAttachments().add(embedAttachment);

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendWithEmbeddedAndAttachment(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The sent hand built message did not match the received message", mailBeanSend,
				mailBeanReceive.get(0));
	}

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendWithEmbeddedAndAttachment(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 *
	 * In this test a complex HTML/Attachment/Embed email message is created
	 * using the all paramater constructor, sent, received and compared. This
	 * test case uses each and every field availible and submits it through the
	 * constructor.
	 */
	@Ignore
	@Test
	public void testComplexSendEmail() {
		// Create mailBean
		ArrayList<String> toList = new ArrayList<>();
		toList.add(receiveConfigBean.getUserEmailAddress());

		String fromField = sendConfigBean.getUserEmailAddress();

		ArrayList<String> ccList = new ArrayList<>();
		ccList.add(copyConfigBean.getUserEmailAddress());

		ArrayList<String> bccList = new ArrayList<>();
		bccList.add(blindConfigBean.getUserEmailAddress());

		String subject = "A Complex Test Message - 19";
		String plainText = "This is the plain text of the message (hidden) - 19.";

		String htmlText = "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>This is the HTML text of the message (visible) - 19.</h1><h2>Here is an image of a code dragon which is embedded in this email.</h2>"
				+ "<img src='cid:code_dragon_error.jpg'><h2>Rawr! My program has an Error!!</h2></body></html>";
		String folder = "Testing";
		int mailStatus = 1;

		LocalDateTime sentDate = LocalDateTime.MIN;
		LocalDateTime receiveDate = LocalDateTime.MAX;

		EmailAttachmentBuilder fBuilder = EmailAttachment.attachment().file("headshot.jpg");
		EmailAttachment fileAttachment = fBuilder.create();
		ArrayList<EmailAttachment> allFileAttachments = new ArrayList<>(); // FIXME maybe issue?
		allFileAttachments.add(fileAttachment);

		EmailAttachmentBuilder eBuilder = EmailAttachment.attachment().bytes(new File("code_dragon_error.jpg"));
		eBuilder.setInline("code_dragon_error.jpg");
		EmailAttachment embedAttachment = eBuilder.create();
		ArrayList<EmailAttachment> allEmbedAttachments = new ArrayList<>(); //FIXME maybe issue?
		allEmbedAttachments.add(embedAttachment);

		// Full paramater constructor (arraylist toField, string fromField,
		// arraylist ccField, arraylist bccField, string subjectField, string
		// textMessage
		// string htmlMessage, string folder, int mailstatus, localdatetime
		// sent, localdatetime received, arraylist<emailattach> file,
		// arraylist<emailattach> embed )
		// This constructor makes most sense when pulling all fields from a
		// database.
		MailBean mailBeanSend = new MailBean(toList, fromField, ccList, bccList, subject, plainText, htmlText, folder,
				mailStatus, sentDate, receiveDate, allFileAttachments, allEmbedAttachments);

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendWithEmbeddedAndAttachment(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The sent complex email does not match the received", mailBeanSend, mailBeanReceive.get(0));
	}

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendEmail(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test a simple email message is created using the default
	 * constructor, sent, received and compared. Only the minimum fields
	 * required for sending a jodd email are tested: to,from,subject
	 * 
	 */
	@Ignore
	@Test
	public void testMinimumSendEmail() {
		// Testing no parameter default constructor.
		MailBean mailBeanSend = new MailBean();

		// Create mailBean
		mailBeanSend.getToField().add(receiveConfigBean.getUserEmailAddress());

		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());

		mailBeanSend.setSubjectField("A new simple test message - 30");

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a eight second pause to allow the Gmail server to receive what has
		// been sent extra time added for complex send
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The sent minimum email does not match the received", mailBeanSend, mailBeanReceive.get(0));
	}

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendEmail(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test a multiple field email message is created using the default
	 * constructor, sent, received and compared. Each of the following fields:
	 * to,cc,bcc,has multiple recipients also contains subject and text field
	 * 
	 * 
	 * Receive tested with receiveConfigBean
	 * 
	 * README THIS TEST OCCASIONALY FAILS ON GMAILS END WHEN TESTING WITH OTHERS
	 * INCORRECT COPY RETRIEVED FROM GMAILS SERVER.
	 * 
	 */
	@Ignore
	@Test
	public void testMultipleFields1SendEmail() {
		// Testing no parameter default constructor.
		MailBean mailBeanSend = new MailBean();

		// Create mailBean
		mailBeanSend.getToField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.getCcField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.getBccField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());

		mailBeanSend.setSubjectField("A new simple test message - 31");
		mailBeanSend.setTextMessageField("This is the plain text of the message (visible) - 31.");

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a ten second pause to allow the Gmail server to receive what has
		// been sent ++ extra time added for complex send to not overlap
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email from normal receive address.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The multiField 1 sent email does not match the received email", mailBeanSend,
				mailBeanReceive.get(0));
	}

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendEmail(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test a multiple field email message is created using the default
	 * constructor, sent, received and compared. Each of the following fields:
	 * to,cc,bcc,has multiple recipients also contains subject and text field
	 * 
	 * Receive tested with sendConfigBean (send to self)
	 * 
	 * README THIS TEST OCCASIONALY FAILS ON GMAILS END WHEN TESTING WITH OTHERS
	 * INCORRECT COPY RETRIEVED FROM GMAILS SERVER.
	 */
	@Ignore
	@Test
	public void testMultipleFields2SendEmail() {
		// Testing no parameter default constructor.
		MailBean mailBeanSend = new MailBean();

		// Create mailBean
		mailBeanSend.getToField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.getCcField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.getBccField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());

		mailBeanSend.setSubjectField("A new simple test message - 32");
		mailBeanSend.setTextMessageField("This is the plain text of the message (visible) - 32.");

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a eight second pause to allow the Gmail server to receive what has
		// been sent extra time added for complex send
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email (sent to self)
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(sendConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The multiField 2 sent email does not match the received email", mailBeanSend,
				mailBeanReceive.get(0));
	}

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendEmail(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test a multiple field email message is created using the default
	 * constructor, sent, received and compared. Each of the following fields:
	 * to,cc,bcc,has multiple recipients also contains subject and text field
	 *
	 * Receive tested with copyConfigBean (see if copy received message)
	 * 
	 * README THIS TEST OCCASIONALY FAILS ON GMAILS END WHEN TESTING WITH OTHERS
	 * INCORRECT COPY RETRIEVED FROM GMAILS SERVER.
	 */
	@Ignore
	@Test
	public void testMultipleFields3SendEmail() {
		// Testing no parameter default constructor.
		MailBean mailBeanSend = new MailBean();

		// Create mailBean
		mailBeanSend.getToField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.getCcField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.getBccField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());

		mailBeanSend.setSubjectField("A new simple test message - 33");
		mailBeanSend.setTextMessageField("This is the plain text of the message (visible) - 33.");

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a ten second pause to allow the Gmail server to receive what has
		// been sent ++ extra time added for complex send to not overlap
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(copyConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The multiField 3 sent email does not match the received email", mailBeanSend,
				mailBeanReceive.get(0));
	}

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendEmail(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test a multiple field email message is created using the default
	 * constructor, sent, received and compared. Each of the following fields:
	 * to,cc,bcc,has multiple recipients also contains subject and text field
	 * 
	 * Receive tested with copyConfigBean (see if blind copy received message)
	 * 
	 * README THIS TEST OCCASIONALY FAILS ON GMAILS END WHEN TESTING WITH OTHERS
	 * INCORRECT COPY RETRIEVED FROM GMAILS SERVER.
	 */
	@Ignore
	@Test
	public void testMultipleFields4SendEmail() {
		// Testing no parameter default constructor.
		MailBean mailBeanSend = new MailBean();

		// Create mailBean
		mailBeanSend.getToField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getToField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.getCcField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getCcField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.getBccField().add(receiveConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(sendConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(copyConfigBean.getUserEmailAddress());
		mailBeanSend.getBccField().add(blindConfigBean.getUserEmailAddress());

		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());

		mailBeanSend.setSubjectField("A new simple test message - 34");
		mailBeanSend.setTextMessageField("This is the plain text of the message (visible) - 34.");

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendEmail(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a eight second pause to allow the Gmail server to receive what has
		// been sent extra time added for complex send
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(blindConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The multiField 4 sent email does not match the received email", mailBeanSend,
				mailBeanReceive.get(0));
	}

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendWithEmbeddedAndAttachment(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test without text field email message is created using the
	 * default constructor, sent, received and compared. Html text included ->
	 * No attachments and nothing embeded.
	 * 
	 */
	@Ignore
	@Test
	public void testWithoutTextSendEmail() {
		// Testing no parameter default constructor.
		MailBean mailBeanSend = new MailBean();

		// Create mailBean
		mailBeanSend.getToField().add(receiveConfigBean.getUserEmailAddress());

		mailBeanSend.setFromField(sendConfigBean.getUserEmailAddress());

		mailBeanSend.setSubjectField("A new complex message without text - 40");

		String htmlText = "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>This is the HTML text of the message (visible) With no embed or attachments and no plain text! - 40.</h1></body></html>";

		mailBeanSend.setHtmlMessageField(htmlText);

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendWithEmbeddedAndAttachment(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a eight second pause to allow the Gmail server to receive what has
		// been sent extra time added for complex send
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		log.warn("mailbeanSend is " + mailBeanSend.toString() + " and " + mailBeanReceive.get(0).toString());
		assertEquals("The testWithoutTextSendEmail does not match the received email", mailBeanSend,
				mailBeanReceive.get(0));
	}

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendWithEmbeddedAndAttachment(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test case, the basic constructor will be used, but with extra
	 * will be sent with embedded and attachments(none), received, and compared
	 * To (x2) , from (x1), subject(x1), cc(x2) , bcc (1) , no message
	 * 
	 */
	@Ignore
	@Test
	public void testBasicEmptySendEmail() {
		// Create mailBean
		ArrayList<String> toList = new ArrayList<>();
		toList.add(receiveConfigBean.getUserEmailAddress());
		toList.add(sendConfigBean.getUserEmailAddress());

		String fromField = sendConfigBean.getUserEmailAddress();

		ArrayList<String> ccList = new ArrayList<>();
		ccList.add(copyConfigBean.getUserEmailAddress());
		ccList.add(blindConfigBean.getUserEmailAddress());

		ArrayList<String> bccList = new ArrayList<>();
		bccList.add(blindConfigBean.getUserEmailAddress());

		String subject = "A basic Test Message - 45";
		String message = "";

		// Basic constructor (arraylist to, string from, arraylist cc, arraylist
		// bcc, string subjct, string text )
		MailBean mailBeanSend = new MailBean(toList, fromField, ccList, bccList, subject, message);
		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendWithEmbeddedAndAttachment(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a eight second pause to allow the Gmail server to receive what has
		// been sent extra time added for complex send
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The testBasicEmptySendEmail does not match the received email", mailBeanSend,
				mailBeanReceive.get(0));
	}

	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendWithEmbeddedAndAttachment(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test case, the basic constructor will be used, but extra fields
	 * will be added then sent with sendWithEmbedAndAttach, received and
	 * compared. Includes send and receive date
	 * 
	 *  To (x1) , from (x1), subject(x1), cc(0) , bcc (0) , message(1),
	 * html(1) embed (1), senddate(1) , receivedate(1)
	 * 
	 * OCCATIONALLY RUNS INTO RECEIVE ISSUE WHEN RUN WITH OTHER TESTS
	 * 
	 */
	@Ignore
	@Test
	public void testBasicSendWithExtraFieldsEmail() {
		// Create mailBean
		ArrayList<String> toList = new ArrayList<>();
		toList.add(receiveConfigBean.getUserEmailAddress());

		String fromField = sendConfigBean.getUserEmailAddress();

		ArrayList<String> ccList = new ArrayList<>();

		ArrayList<String> bccList = new ArrayList<>();

		String subject = "A Basic message with extra fields - 46";
		String message = "A basic message with extra fields (invisible) - 46";

		// Basic constructor (arraylist to, string from, arraylist cc, arraylist
		// bcc, string subjct, string text )
		MailBean mailBeanSend = new MailBean(toList, fromField, ccList, bccList, subject, message);

		// Add additional fields
		String htmlText = "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\"><body><h1>This is the HTML text of the message "
				+ "(visible) - 46.</h1><h2>Here is an image of a code dragon which is embedded in this email.</h2><img src='cid:code_dragon_error.jpg'>"
				+ "<h2>Rawr! My program has an Error!!</h2></body></html>";
		mailBeanSend.setHtmlMessageField(htmlText);


		EmailAttachmentBuilder eBuilder = EmailAttachment.attachment().bytes(new File("code_dragon_error.jpg"));
		eBuilder.setInline("code_dragon_error.jpg");
		EmailAttachment embedAttachment = eBuilder.create();
		mailBeanSend.getEmbedAttachments().add(embedAttachment);

		LocalDateTime sentDate = LocalDateTime.MIN;
		mailBeanSend.setDateSent(sentDate);
		LocalDateTime receiveDate = LocalDateTime.MAX;
		mailBeanSend.setDateReceived(receiveDate);

		// Extra fields that may cause issues

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendWithEmbeddedAndAttachment(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a ten second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The testBasicSendWithExtraFieldsEmail does not match the received email", mailBeanSend,
				mailBeanReceive.get(0));
	}
	
	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendWithEmbeddedAndAttachment(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test case, the basic constructor will be used, but extra fields
	 * will be added then sent with sendWithEmbedAndAttach, received and
	 * compared. Included folder and mailstatus (no affect on sent email)
	 * 
	 * To (x1) , from (x1), subject(x1), cc(0) , bcc (0) , message(1),
	 * html(0) embed (2), Included folder and mailstatus (no affect on sent email)
	 * 
	 * 
	 */
	@Ignore
	@Test
	public void testMultipleEmbedWithoutHtmlEmail() {
		// Create mailBean
		ArrayList<String> toList = new ArrayList<>();
		toList.add(receiveConfigBean.getUserEmailAddress());

		String fromField = sendConfigBean.getUserEmailAddress();

		ArrayList<String> ccList = new ArrayList<>();

		ArrayList<String> bccList = new ArrayList<>();

		String subject = "A complex message with no html and 2 embed - 50";
		String message = "A complex message with no html and 2 embed (visible) - 50";

		// Basic constructor (arraylist to, string from, arraylist cc, arraylist
		// bcc, string subjct, string text )
		MailBean mailBeanSend = new MailBean(toList, fromField, ccList, bccList, subject, message);

		//Add two embeded with no html (will appear as attachments)
		EmailAttachmentBuilder eBuilder = EmailAttachment.attachment().bytes(new File("code_dragon_error.jpg"));
		eBuilder.setInline("code_dragon_error.jpg");
		EmailAttachment embedAttachment = eBuilder.create();
		mailBeanSend.getEmbedAttachments().add(embedAttachment);
		
		EmailAttachmentBuilder eBuilder2 = EmailAttachment.attachment().bytes(new File("headshot.jpg"));
		eBuilder2.setInline("headshot.jpg");
		EmailAttachment embedAttachment2 = eBuilder2.create();
		mailBeanSend.getEmbedAttachments().add(embedAttachment2);

		//No affect on sent email
		mailBeanSend.setFolder("Testing");
		mailBeanSend.setMailStatus(1);

		// Extra fields that may cause issues

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendWithEmbeddedAndAttachment(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The testMultipleEmbedWithoutHtmlEmail does not match the received email", mailBeanSend,
				mailBeanReceive.get(0));
	}
	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendWithEmbeddedAndAttachment(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 * In this test case, the basic constructor will be used, but extra fields
	 * will be added then sent with sendWithEmbedAndAttach, received and
	 * compared. empty text message, no html and 2 attacments
	 * 
	 * To (x1) , from (x1), subject(x1), cc(0) , bcc (0) , message(0),
	 * html(0) embed (0) attachment(2), 
	 * 
	 * 
	 */
	@Ignore
	@Test
	public void testEmptyMessWithMultAttachEmail() {
		// Create mailBean
		ArrayList<String> toList = new ArrayList<>();
		toList.add(receiveConfigBean.getUserEmailAddress());

		String fromField = sendConfigBean.getUserEmailAddress();

		ArrayList<String> ccList = new ArrayList<>();

		ArrayList<String> bccList = new ArrayList<>();

		String subject = "A complex message with no html and 2file attachment - 51";
		String message = "";

		// Basic constructor (arraylist to, string from, arraylist cc, arraylist
		// bcc, string subjct, string text )
		MailBean mailBeanSend = new MailBean(toList, fromField, ccList, bccList, subject, message);

		
		//Add two attachment with no html (will appear as attachments)
		EmailAttachmentBuilder fBuilder = EmailAttachment.attachment().file("headshot.jpg");
		EmailAttachment fileAttachment = fBuilder.create();
		mailBeanSend.getFileAttachments().add(fileAttachment);
		
		EmailAttachmentBuilder fBuilder2 = EmailAttachment.attachment().file("code_dragon_error.jpg");
		EmailAttachment fileAttachment2= fBuilder2.create();
		mailBeanSend.getFileAttachments().add(fileAttachment2);
		


		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendWithEmbeddedAndAttachment(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a five second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The testEmptyMessWithMultAttachEmail does not match the received email", mailBeanSend,
				mailBeanReceive.get(0));
	}
	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendWithEmbeddedAndAttachment(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 *
	 * In this test a complex HTML/Attachment/Embed email message is created
	 * using the all paramater constructor, sent, received and compared. This
	 * test case uses each and every field availible and submits it through the
	 * constructor but most fields are empty strings.
	 * 
	 * Time sent in reversed order (arrived before sent.)
	 */
	@Ignore
	@Test
	public void testEmptyComplexSendEmail() {
		
		// Create complex empty mailBean
		ArrayList<String> toList = new ArrayList<>();
		toList.add(receiveConfigBean.getUserEmailAddress());

		String fromField = sendConfigBean.getUserEmailAddress();

		ArrayList<String> ccList = new ArrayList<>();

		ArrayList<String> bccList = new ArrayList<>();

		String subject = "A Complex Test Message with empty fields - 60";
		String plainText = "";

		String htmlTEXT = "";
		String folder = "";
		int mailStatus = 0;
			
		LocalDateTime sentDate = LocalDateTime.MAX;    //reversed order
		LocalDateTime receiveDate = LocalDateTime.MIN; //reversed order

		ArrayList<EmailAttachment> allFileAttachments = new ArrayList<>(); 

		ArrayList<EmailAttachment> allEmbedAttachments = new ArrayList<>(); 


		// Full paramater constructor (arraylist toField, string fromField,
		// arraylist ccField, arraylist bccField, string subjectField, string
		// textMessage
		// string htmlMessage, string folder, int mailstatus, localdatetime
		// sent, localdatetime received, arraylist<emailattach> file,
		// arraylist<emailattach> embed )
		// This constructor makes most sense when pulling all fields from a
		// database.
		MailBean mailBeanSend = new MailBean(toList, fromField, ccList, bccList, subject, plainText, htmlTEXT, folder,
				mailStatus, sentDate, receiveDate, allFileAttachments, allEmbedAttachments);

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendWithEmbeddedAndAttachment(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a seven second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The sent complex empty email does not match the received", mailBeanSend, mailBeanReceive.get(0));
	}
	/**
	 * Test method for:
	 * {@link com.chrisdufort.mailaction.BasicSendAndReceive#sendWithEmbeddedAndAttachment(com.chrisdufort.mailbean.MailBean, com.chrisdufort.properties.MailConfigBean)}
	 * 
	 *
	 * In this test a complex HTML/Attachment/Embed email message is created
	 * using the all paramater constructor, sent, received and compared. This
	 * test case uses each and every field availible and submits it through the
	 * constructor with multiples in each field
	 * 
	 * HTML has 2 CID, EMBEDED FILES ARE ORDERED DIFFERENTLY FROM HTML IMG TAG
	 */
	@Ignore
	@Test
	public void testComplexSendWithMultipleFieldsEmail() {
		
		// Create complex empty mailBean
		ArrayList<String> toList = new ArrayList<>();
		toList.add(receiveConfigBean.getUserEmailAddress());
		toList.add(sendConfigBean.getUserEmailAddress());

		String fromField = sendConfigBean.getUserEmailAddress();

		ArrayList<String> ccList = new ArrayList<>();
		ccList.add(copyConfigBean.getUserEmailAddress());
		ccList.add(receiveConfigBean.getUserEmailAddress());

		ArrayList<String> bccList = new ArrayList<>();
		bccList.add(copyConfigBean.getUserEmailAddress());
		bccList.add(blindConfigBean.getUserEmailAddress());
		

		String subject = "A Complex Test Message with multiple of each field- 70";
		String plainText = "A Complex Test Message with multiple of each fields (invisible)- 70";

		//HTML ORDER -> DRAGON THEN HEADSHOT
		String htmlText = "<html><META http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				+ "<body><h1>This is the HTML text of the message (visible) - 70.</h1><h2>Here is an image of a code dragon, and my face which is embedded in this email.</h2>"
				+ "<img src='cid:code_dragon_error.jpg'><img src='cid:headshot.jpg'><h2>Rawr! My program has an Error!!</h2></body></html>";
		
		String folder = "TESTINGFINAL";
		int mailStatus = 99;

		LocalDateTime sentDate = LocalDateTime.now();
		LocalDateTime receiveDate = LocalDateTime.now();

		
	 
		//TWO OF EACH ATTACHMENT AND EMBED IN MIXED ORDER FROM HTML
		ArrayList<EmailAttachment> allEmbedAttachments = new ArrayList<>(); 
		ArrayList<EmailAttachment> allFileAttachments = new ArrayList<>();
		
		//EMBED ORDER -> HEADSHOT THEN DRAGON
		EmailAttachmentBuilder eBuilder = EmailAttachment.attachment().bytes(new File("headshot.jpg"));
		eBuilder.setInline("headshot.jpg");
		EmailAttachment embedAttachment = eBuilder.create();
		
		allEmbedAttachments.add(embedAttachment);
		
		EmailAttachmentBuilder eBuilder2 = EmailAttachment.attachment().bytes(new File("code_dragon_error.jpg"));
		eBuilder2.setInline("code_dragon_error.jpg");
		EmailAttachment embedAttachment2 = eBuilder2.create();
		
		allEmbedAttachments.add(embedAttachment2);
		
		//MIXED ORDER SEEMS TO RESULT IN HTML ORDER TAKING PRESIDENCE. 
		//-------------------------------
		
		EmailAttachmentBuilder fBuilder = EmailAttachment.attachment().file("headshot.jpg");
		EmailAttachment fileAttachment = fBuilder.create();
		
		allFileAttachments.add(fileAttachment);
		
		EmailAttachmentBuilder fBuilder2 = EmailAttachment.attachment().file("code_dragon_error.jpg");
		EmailAttachment fileAttachment2 = fBuilder2.create();
		
		allFileAttachments.add(fileAttachment2);
		
		// Full paramater constructor (arraylist toField, string fromField,
		// arraylist ccField, arraylist bccField, string subjectField, string
		// textMessage
		// string htmlMessage, string folder, int mailstatus, localdatetime
		// sent, localdatetime received, arraylist<emailattach> file,
		// arraylist<emailattach> embed )
		// This constructor makes most sense when pulling all fields from a
		// database.
		MailBean mailBeanSend = new MailBean(toList, fromField, ccList, bccList, subject, plainText, htmlText, folder,
				mailStatus, sentDate, receiveDate, allFileAttachments, allEmbedAttachments);

		String messageId = "-1"; // Signifying an error has occurred if not
									// otherwise changed.
		try {
			// Store result and send email.
			messageId = basicSendAndReceive.sendWithEmbeddedAndAttachment(mailBeanSend, sendConfigBean);
		} catch (Exception e1) {
			// TODO HOW DO I HANDLE THIS EXCEPTION?
			e1.printStackTrace();
		}
		log.info("MessageId is " + messageId + " value other than -1 = successful send.");

		// Add a seven second pause to allow the Gmail server to receive what has
		// been sent
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			log.error("Threaded sleep failed", e);
		}

		// Receive email.
		ArrayList<MailBean> mailBeanReceive = basicSendAndReceive.receiveEmail(receiveConfigBean);

		// assertEquals deprecated?, should use assertSame() instead?
		// Using the better test approach of a custom equals and hashCode method
		// TODO: REMOVE DEBUG FROM MAILBEAN - CURRENTLY ONLY THERE FOR TESTING
		// PURPOSES!
		assertEquals("The sent complex empty email does not match the received", mailBeanSend, mailBeanReceive.get(0));
	}
}
