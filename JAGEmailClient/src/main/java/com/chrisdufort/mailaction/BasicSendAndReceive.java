package com.chrisdufort.mailaction;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.Flags;

import com.chrisdufort.mailbean.MailBean;
import com.chrisdufort.properties.MailConfigBean;

import jodd.mail.Email;
import jodd.mail.EmailAttachment;
import jodd.mail.EmailFilter;
import jodd.mail.EmailMessage;
import jodd.mail.ImapServer;
import jodd.mail.ImapSslServer;
import jodd.mail.MailAddress;
import jodd.mail.MailException;
import jodd.mail.ReceiveMailSession;
import jodd.mail.ReceivedEmail;
import jodd.mail.SendMailSession;
import jodd.mail.SmtpServer;
import jodd.mail.SmtpSslServer;

/**
 * This class is responsible for sending and receiving of email messages. This
 * class sends and receives basic emails and complex emails each with their own
 * associated fields. It makes use of the JODD EMAIL API and source code:
 * http://jodd.org/doc/email.html
 * http://jodd.org/api/jodd/mail/package-summary.html
 * 
 * This class contains three primary methods: 1. Sending simple emails
 * (withoutHTML/attachments/embed). 2. Sending Multipart emails (with
 * HTML/attachments/embed). 3. Receiving of all email types.
 * 
 * @author Christopher Dufort
 * @version 0.2.1-SNAPSHOT , Phase 2 - last modified 09/23/15
 * @since 0.0.1-SNAPSHOT , Phase 1
 */
public class BasicSendAndReceive {
	/**
	 * Simple email send method, handled emails with only Plain Text. Requires
	 * both a data mail bean and a configuration mail bean. Handles basic
	 * function incl: from, to, cc, bcc, subject and plain text.
	 * 
	 * @param mailBean
	 *            A data object containing all the fields and methods describing
	 *            an email message.
	 * @param sendConfigBean
	 *            A configuration object containing all the fields needed for a
	 *            connection to a sending email account.
	 * @return messageId A string representing a message id returned from the
	 *         session when a message is sent. A value of -1 indicates an error.
	 */
	public final String sendEmail(final MailBean mailBean, final MailConfigBean sendConfigBean) throws MailException {

		// Create am SMTP server object which retrieves fields from
		// send configuration bean.
		// SSL = TLS Secure e-mail sending.
		SmtpServer<?> smtpServer = SmtpSslServer.create(sendConfigBean.getHost())
				.authenticateWith(sendConfigBean.getUserEmailAddress(), sendConfigBean.getPassword());

		// Creating a JODD Email object, email is built using common API.
		Email email = Email.create();

		// From field (author of email).
		email.from(sendConfigBean.getUserEmailAddress());

		// Loop through all toField addresses and append them to email object.
		for (String toAddress : mailBean.getToField()) {
			email.to(toAddress);
		}

		// Only add cc if they exist (array not null or empty).
		if (!(mailBean.getCcField() == null) && !mailBean.getCcField().isEmpty()) {
			// Loop through all ccField addresses and add them to email object.
			for (String copyAddress : mailBean.getCcField()) {
				email.cc(copyAddress);
			}
		}

		// Only add bcc if they exist (array not null or empty).
		if (!(mailBean.getBccField() == null) && !mailBean.getBccField().isEmpty()) {
			// Loop through all bccField addresses and add them to email object.
			for (String blindAddress : mailBean.getBccField()) {
				email.bcc(blindAddress);
			}
		}

		// Emails should contain a string subject field description of the
		// purpose. (if not null)
		if (mailBean.getSubjectField() != null) {
			email.subject(mailBean.getSubjectField());
		}

		// Insertion of plain text field, only string made of pure text, no
		// HTML. All emails should contain a plain text field.
		if (mailBean.getTextMessageField() != null) {
			email.addText(mailBean.getTextMessageField());
		}

		// Current LocalDateTime is converted to Date format.
		LocalDateTime currentDateTime = LocalDateTime.now();
		Date sendDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

		// Attach the current date/time stamp to the email before sending.
		email.sentOn(sendDate);

		// An alternative to the above without time(old code)may be:
		// email.setCurrentSentDate();

		// Create a session: the the object responsible for communicating with
		// the server.
		SendMailSession session = smtpServer.createSession();

		// Similar to File I/O:
		// 1.Open session with server, 2.Send an email, 3.Close session
		// May throw EmailException - should be handled.
		// Finally will always close session resources.
		String messageId = "-1"; // Signifies an error has occurred if not
									// changed.
		try {
			session.open();
			messageId = session.sendMail(email);
		} catch (MailException me) {
			// What kinds of errors occur / how do i handle them?
			me.printStackTrace();
		} finally {
			session.close();
		}
		return messageId;

	}

	/**
	 * Multipart email send method, handled emails that include:
	 * HTML/Attachments/Embedded files. Requires both a data mail bean and a
	 * configuration mail bean. Handles basic function incl: from, to, cc, bcc,
	 * subject and plain text. Handles complex function incl: embedded
	 * attachments, HTML Messages, attached files.
	 * 
	 * File attachments are handled using EmailAttachment class instead of
	 * stand-alone attachment bean for simplicity.
	 * 
	 * @param mailBean
	 *            A data object containing all the fields and methods describing
	 *            an email message.
	 * @param sendConfigBean
	 *            A configuration object containing all the fields needed for a
	 *            connection to a sending email account.
	 * @return messageId A string representing a message id returned from the
	 *         session when a message is sent. A value of -1 indicates an error.
	 */
	public final String sendWithEmbeddedAndAttachment(final MailBean mailBean, final MailConfigBean sendConfigBean)
			throws MailException {

		// Create am SMTP server object which retrieves fields from
		// send configuration bean.
		// SSL = TLS Secure e-mail sending.
		SmtpServer<?> smtpServer = SmtpSslServer.create(sendConfigBean.getHost())
				.authenticateWith(sendConfigBean.getUserEmailAddress(), sendConfigBean.getPassword());

		// Creating a JODD Email object, email is built using common API.
		Email email = Email.create();

		// From field (author of email)
		email.from(sendConfigBean.getUserEmailAddress());

		/// Loop through all toField addresses and append them to email object.
		for (String toAddress : mailBean.getToField()) {
			email.to(toAddress);
		}

		// Only add cc if they exist (array not null or empty).
		if (!(mailBean.getCcField() == null) && !mailBean.getCcField().isEmpty()) {
			// Loop through all ccField addresses and add them to email object.
			for (String copyAddress : mailBean.getCcField()) {
				email.cc(copyAddress);
			}
		}

		// Only add bcc if they exist (array not null or empty).
		if (!(mailBean.getBccField() == null) && !mailBean.getBccField().isEmpty()) {
			// Loop through all bccField addresses and add them to email object.
			for (String blindAddress : mailBean.getBccField()) {
				email.bcc(blindAddress);
			}
		}

		// Emails should contain a string subject field description of the
		// purpose (if not null)
		if (mailBean.getSubjectField() != null) {
			email.subject(mailBean.getSubjectField());
		}

		// Plain text should be added before HTML when working with JODD Email

		// Insertion of plain text field, only string made of pure text, no
		// HTML. All emails should contain a plain text field. (only add if not
		// null)
		if (mailBean.getTextMessageField() != null) {
			email.addText(mailBean.getTextMessageField());
		}

		// Optional - modern email clients interpret messages as html adding
		// additional functionality. (only add if not null and not empty as to
		// prevent plain text from being hidden)
		if (mailBean.getHtmlMessageField() != null && !mailBean.getHtmlMessageField().isEmpty()) {
			email.addHtml(mailBean.getHtmlMessageField());
		}

		// Only add attachments if array exists( not null ) and there are files
		// (array not empty).
		if (!(mailBean.getFileAttachments() == null) && !(mailBean.getFileAttachments().isEmpty())) {
			// Loop through all fileAttachments and add them to email object.
			for (EmailAttachment fileAttachments : mailBean.getFileAttachments()) {
				email.attach(fileAttachments);
			}
		}

		// Only add embedded attachments if array exists( not null ) and there
		// are files (array not empty).
		if (!(mailBean.getEmbedAttachments() == null) && !(mailBean.getEmbedAttachments().isEmpty())) {
			// Loop through all fileAttachments and add them to email object.
			for (EmailAttachment embedAttachment : mailBean.getEmbedAttachments()) {
				email.embed(embedAttachment);
			}
		}

		// Current LocalDateTime is converted to Date format.
		LocalDateTime currentDateTime = LocalDateTime.now();
		Date sendDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

		// Attach the current date/time stamp to the email before sending.
		email.sentOn(sendDate);

		// An alternative to the above without time(old code)may be:
		// email.setCurrentSentDate();

		// A session is the object responsible for communicating with the server
		SendMailSession session = smtpServer.createSession();

		// Similar to File I/O:
		// 1.Open session with server, 2.Send an email, 3.Close session
		// May throw EmailException - should be handled.
		// Finally will always close session resources.
		String messageId = "-1"; // Signifies an error has occurred if not
									// changed.
		try {
			session.open();
			messageId = session.sendMail(email);
		} catch (MailException me) {
			// What kinds of errors occur / how do i handle them?
			me.printStackTrace();
		} finally {
			session.close();
		}
		return messageId;

	}

	/**
	 * Simple/Multipart email receive method that can receive both Plain Text or
	 * HTML/Attachmet/Embeded emails. This method will create MailBean objects
	 * through extracting fields from received JODD emails. Requires a
	 * configuration mail bean with receiving account info. Current
	 * Functionality only retrieves a single email at subscript 0 (last sent).
	 * 
	 * @param receiveConfigBean
	 *            A configuration object containing all the fields needed for a
	 *            connection to a receiving email account.
	 * @return mailBeans An ArrayList of type MailBean object, 1 or more
	 *         retrieved messages.
	 */
	public final ArrayList<MailBean> receiveEmail(final MailConfigBean receiveConfigBean) {

		// Initializing mailBeans to null, if not changed no emails were found.
		ArrayList<MailBean> mailBeans = null;

		// Create am IMAP server object which retrieves fields from
		// receive configuration bean.
		// SSL = TLS Secure e-mail sending.
		ImapServer imapServer = new ImapSslServer(receiveConfigBean.getHost(), receiveConfigBean.getUserEmailAddress(),
				receiveConfigBean.getPassword());

		// Create a session: the the object responsible for communicating with
		// the server.
		ReceiveMailSession session = imapServer.createSession();

		// Open connection with the server this may throw an exception.
		session.open();

		// Once the email has been received it will be marked as seen(read) on
		// the server.
		// Flag = SEEN , false -> Receive emails only which have not yet been
		// seen (not been read yet).
		ReceivedEmail[] emails = session.receiveEmailAndMarkSeen(EmailFilter.filter().flag(Flags.Flag.SEEN, false));

		// If there is any email then loop through them adding their contents to
		// a new MailBean that is then added to the array list.
		if (emails != null) {

			// Instantiate the array list of messages.
			mailBeans = new ArrayList<MailBean>();

			// Loop through each of the received Emails creating a MailBean for
			// each one.
			for (ReceivedEmail email : emails) {

				// Create a new MailBean using default constructor (all fields
				// set to default values)
				MailBean mailBean = new MailBean();

				// Retrieve from field from JODD email object.
				mailBean.setFromField(email.getFrom().getEmail());

				// Retrieve subject field from JODD email object.
				mailBean.setSubjectField(email.getSubject());

				// Loop through the toField Array retrieving each address from
				// JODD email.
				for (MailAddress toAddress : email.getTo()) {
					mailBean.getToField().add(toAddress.getEmail());
				}

				// Only add cc if they exist (not null and length > 0);
				if (email.getCc() != null && email.getCc().length > 0) {
					// Loop through the ccField Array retrieving each address
					// from JODD email
					for (MailAddress copyAddress : email.getCc()) {
						mailBean.getCcField().add(copyAddress.getEmail());
					}
				}
				// Messages may be multi-part so they are stored in an array
				// WE CANNOT ASSUME THAT THE EMAIL ARRAY WILL BE AS FOLLOWS.
				// 0 = Plain Text
				// 1 = HTML Text
				// THERE FOR WE MUST CHECK MIMETYPE.

				// Retrieve all messages.
				List<EmailMessage> messages = email.getAllMessages();

				// Check each message in the list if it is PLAIN TEXT or HTML
				// TEXT.
				// SetText or SetHtml to mailBean as needed.
				for (EmailMessage messageField : messages) {
					if (messageField.getMimeType().equals("TEXT/PLAIN")) {
						mailBean.setTextMessageField(messageField.getContent());
					} else if (messageField.getMimeType().equals("TEXT/HTML")) {
						mailBean.setHtmlMessageField(messageField.getContent());
					}
				}

				// Only retrieve attachments if at least one exists (arraylist
				// exists and is not empty).
				if (!(email.getAttachments() == null) && !email.getAttachments().isEmpty()) {
					// For each attachment(embedded or otherwise in the email)
					for (EmailAttachment emailAttachment : email.getAttachments()) {
						// If the attachment is embedded.
						if (emailAttachment.isInline()) {
							// Add to embeddedAttachment ArrayList of MailBean.
							mailBean.getEmbedAttachments().add(emailAttachment);
						} else {
							// Otherwise add to fileAttachment ArrayList of
							// MailBean.
							mailBean.getFileAttachments().add(emailAttachment);
						}
					}
				}
				// Support for legacy date object:Conversion of received Date to
				// a LocalDateTime.
				LocalDateTime receivedDateTime = LocalDateTime.ofInstant(email.getReceiveDate().toInstant(),
						ZoneId.systemDefault());

				// For display to user, and database store received date in Mail
				// Bean.
				mailBean.setDateReceived(receivedDateTime);

				// Add the newly created and populated mailBean to the array
				// list.
				mailBeans.add(mailBean);
			}
		}
		// Close session.
		session.close();

		// Return arrayList of created MailBeans.
		return mailBeans;
	}
}
