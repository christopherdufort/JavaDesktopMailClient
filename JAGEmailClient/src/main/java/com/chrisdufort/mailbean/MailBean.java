package com.chrisdufort.mailbean;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Cannot import references from src/test/ that code is not visible during compilation (production code) -> always remember Run-> Run As -> Maven build.

import jodd.mail.EmailAttachment;


/**
 * Custom Dawson Email MailBean, filled with many common mail fields including:
 * to, cc, bcc, from , subject, text message, html message, sent date, receive date, attachments
 * and some custom ones including folder and mailstatus.
 * This class adheres to the standard set by Java Beans including :
 * No parameter constructor, private fields, accessors and mutators (currently not serializable)
 * 
 * This class maintains three constructors offering creation in various forms:
 * No parameter constructor: create emails with all fields initialized to default values(set manually)
 * Basic email constructor: create plain text emails with some fields initialized to default others set.
 * Complex email constructor: Multipart emails with HTML and Attachments all fields necessary. 
 * 
 * When passing a reference to a setter it is best practice to declare it
 * final so that the setter cannot change the reference, there for all setters in this bean are final.
 * 
 * ArrayLists do not require a setter instead using .add for the ArrayList api is sufficient.
 * 
 * Using JavaFX (Property Beans) as of version 0.3.4 onward.
 * 
 * @author Christopher Dufort
 * @version 0.3.4-SNAPSHOT , Phase 3 - last modified 10/25/15
 * @since 0.0.1-SNAPSHOT , Phase 1
 */
public class MailBean {
	//Method logger not needed/used here.
	
	// TODO: REMOVE ALL LOGGING FROM BEAN BEFORE CODE IS READY FOR PRODUCTION USE!
	private final Logger log = LoggerFactory.getLogger(getClass().getName());
	
	// Unique primary key id obtained from DB location.
	private IntegerProperty id;
	
	// The address or addresses that this email is being sent to
	private ArrayList<String> toField;
	
	// The Carbon Copy address or addresses that this email is also being sent to
	private ArrayList<String> ccField;
	
	// The Blind Carbon Copy address or addresses that this email is secretly being sent to
	private ArrayList<String> bccField;

	// The sender of the email
	private StringProperty fromField;

	// The subject line of the email
	private StringProperty subjectField;

	// Plain text part of the email
	private StringProperty textMessageField;
	
	// Html text part of the email (optional)
	private StringProperty htmlMessageField;

	// Name of the folder
	private StringProperty folder;

	// Status 0 = New Email ready for Sending.
	// Status 1 = Mail has been Sent.
	// Status 2 = Mail has been Received.
	private IntegerProperty mailStatus;
	
	//Date and Time email was sent (overwritten at send time)
	private ObjectProperty<LocalDateTime> dateSent;
	
	//Date and Time email was receive (overwritten at receive time)
	private ObjectProperty<LocalDateTime> dateReceived;
	
	// File attachments added to the email (optional)
	private ArrayList<EmailAttachment> fileAttachments;
	
	// File attachments embedded in the email (optional)
	private ArrayList<EmailAttachment> embedAttachments;

	
	/**
	 * Default constructor for a new mail message waiting to be sent.
	 * Initializes all fields to default values.
	 * This message is considered multipart.
	 * All fields are initialized to default values in order to prevent null pointers
	 */
	public MailBean() {
		super();
		this.toField = new ArrayList<String>(); 
		this.ccField = new ArrayList<String>(); 
		this.bccField = new ArrayList<String>(); 
		this.fromField = new SimpleStringProperty("");
		this.subjectField = new SimpleStringProperty("");
		this.textMessageField = new SimpleStringProperty("");
		this.htmlMessageField = new SimpleStringProperty(""); //Will override text message field making this a multipart message.
		this.folder = new SimpleStringProperty("draft"); //Hold in draft(to be sent folder)
		this.mailStatus = new SimpleIntegerProperty(0); //Manually set to ready to be sent status.
		this.dateSent = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now()); //set to current time, will be overwritten when email is sent.
		this.dateReceived = new SimpleObjectProperty<LocalDateTime>(LocalDateTime.now()); //set to current time, will be overwritten when email is received.
		this.fileAttachments = new ArrayList<EmailAttachment>(); 
		this.embedAttachments = new ArrayList<EmailAttachment>(); 
	}

	/**
	 * Constructor for creating messages from either a form or a database record.
	 * This is the constructor for a basic email (Plain text and no files/html)
	 * This constructor exists to allows simple messages to be sent.
	 * 
	 * @param toField
	 * @param fromField
	 * @param ccField
	 * @param bccField
	 * @param subjectField
	 * @param textMessageField
	 * 
	 */
	public MailBean(final ArrayList<String> toField,final String fromField, final ArrayList<String> ccField, final ArrayList<String> bccField, final String subjectField,
			final String textMessageField) {
		super();
		this.toField = new ArrayList<String>(toField);
		this.fromField = new SimpleStringProperty(fromField);
		this.ccField = new ArrayList<String>(ccField);
		this.bccField = new ArrayList<String>(bccField);	
		this.subjectField = new SimpleStringProperty(subjectField);
		this.textMessageField = new SimpleStringProperty(textMessageField);
		this.folder = new SimpleStringProperty("draft"); //Hold in draft(to be sent folder)
		this.mailStatus = new SimpleIntegerProperty(0); //Manually set to ready to be sent status.
	}
	/**
	 * Constructor for creating a complete message from either a form or a database record
	 * This is the constructor for the more modern email (HTML and file attachments).
	 * This constructor requires ALL fields associated with a mailBean.
	 * 
	 * @param toField
	 * @param fromField
	 * @param ccField
	 * @param bccField
	 * @param subjectField
	 * @param textMessageField
	 * @param htmlMessageField
	 * @param folder
	 * @param mailStatus
	 * @param dateSent
	 * @param dateReceived
	 * @param fileAttachment
	 * @param embedAttachment
	 * 
	 */
	public MailBean(final ArrayList<String> toField, final String fromField, final ArrayList<String> ccField, final ArrayList<String> bccField, final String subjectField,
			final String textMessageField, final String htmlMessageField, final String folder, final int mailStatus, final LocalDateTime dateSent, final LocalDateTime dateReceived,
			final ArrayList<EmailAttachment> fileAttachments, final ArrayList<EmailAttachment> embedAttachments) {
		super();
		this.toField = toField;
		this.fromField = new SimpleStringProperty(fromField);
		this.ccField = ccField;
		this.bccField = bccField;
		this.subjectField = new SimpleStringProperty(subjectField);
		this.textMessageField = new SimpleStringProperty(textMessageField);
		this.htmlMessageField = new SimpleStringProperty(htmlMessageField);
		this.folder = new SimpleStringProperty(folder);
		this.mailStatus = new SimpleIntegerProperty(mailStatus);
		this.dateSent = new SimpleObjectProperty<LocalDateTime>(dateSent);
		this.dateReceived = new SimpleObjectProperty<LocalDateTime>(dateReceived);
		this.fileAttachments = new ArrayList<EmailAttachment>(fileAttachments);
		this.embedAttachments = new ArrayList<EmailAttachment>(embedAttachments);
	}
	/** 
	 * @return the fromField
	 * 
	 */
	public final String getFromField() {
		return fromField.get();
	}
	
	/**
	 * 
	 * @return
	 */
	public StringProperty fromFieldProperty() {
		return fromField;
	}
	/**
	 * @param fromField
	 *            the fromField to set
	 */
	public final void setFromField(final String fromField) {
		this.fromField.set(fromField);
	}
	/**
	 * @return the subjectField
	 */
	public final String getSubjectField() {
		return subjectField.get();
	}
	
	public StringProperty subjectFieldProperty(){
		return subjectField;
	}
	/**
	 * 
	 * @param subjectField
	 *            the subjectField to set
	 */
	public final void setSubjectField(final String subjectField) {
		this.subjectField.set(subjectField);
	}
	/**
	 * @return the textMessageField Plain Text for the message.
	 */
	public final String getTextMessageField() {
		return textMessageField.get();
	}
	
	/**
	 * 
	 * @return
	 */
	public StringProperty textMessageFieldProperty(){
		return textMessageField;
	}
	/**
	 * @param textMessageField
	 *            the textMessageField(plain text)to set
	 */
	public final void setTextMessageField(final String textMessageField) {
		this.textMessageField.set(textMessageField);
	}
	/**
	 * @return the htmlMessageField HTML text for the message
	 */
	public String getHtmlMessageField() {
		return htmlMessageField.get();
	}
	
	public StringProperty htmlMessageFieldProperty(){
		return htmlMessageField;
	}
	/**
	 * @param htmlMessageField 
	 * 			the htmlMessageField(html message) to set
	 */
	public void setHtmlMessageField(final String htmlMessageField) {
		this.htmlMessageField.set(htmlMessageField);
	}
	/**
	 * @return the folder used for user sorting and organizing
	 */
	public final String getFolder() {
		return folder.get();
	}
	
	public StringProperty folderProperty(){
		return folder;
	}
	/**
	 * @param folder
	 *            the folder to set used for user sorting and organizing
	 */
	public final void setFolder(final String folder) {
		this.folder.set(folder);
	}
	/**
	 * @return the mailStatus
	 */
	public final int getMailStatus() {
		return mailStatus.get();
	}
	
	public IntegerProperty mailStatusProperty(){
		return mailStatus;
	}
	/**
	 * @param mailStatus
	 *            the mailStatus to set
	 */
	public final void setMailStatus(final int mailStatus) {
		this.mailStatus.set(mailStatus);
	}
	/**
	 * There is no set when working with collections. When you get the ArrayList
	 * you can add elements to it. A set method implies changing the current
	 * ArrayList for another ArrayList and this is something we rarely do with
	 * collections.
	 * 
	 * @return the toField
	 */
	public final ArrayList<String> getToField() {
		return toField;
	}	
	/**
	 * There is no set when working with collections. When you get the ArrayList
	 * you can add elements to it. A set method implies changing the current
	 * ArrayList for another ArrayList and this is something we rarely do with
	 * collections.
	 * 
	 * @return the getCCField
	 */
	public final ArrayList<String> getCcField() {
		return ccField;
	}
	/**
	 * There is no set when working with collections. When you get the ArrayList
	 * you can add elements to it. A set method implies changing the current
	 * ArrayList for another ArrayList and this is something we rarely do with
	 * collections.
	 * @return the bccField
	 */
	public final ArrayList<String> getBccField() {
		return bccField;
	}
	/**
	 * @return the dateSent
	 */
	public LocalDateTime getDateSent() {
		return dateSent.get();
	}
	
	public ObjectProperty<LocalDateTime> dateSentProperty() {
		return dateSent;
	}
	/**
	 * Alternative getDateSent method that returns a Timestamp(for ease of use with DB)
	 * Added in version 0.2.3
	 * 
	 * @return the dateSent
	 * 				the send date of the email as a Timestamp
	 */
	public Timestamp getDateSentAsTimestamp() {
		if (dateSent == null) //Avoid valueOf throwing nullPointerException
			return null;
		else
			return Timestamp.valueOf(dateSent.get());
	}

	/**
	 * @param dateSent 
	 * 				the send date of the email as a LocalDateTime
	 */
	public void setDateSent(final LocalDateTime dateSent) {
		this.dateSent.set(dateSent);
	}
	/**
	 * Overloaded setDateSent that takes a TimeStamp (for use with DB methods)
	 * Added in version 0.2.3
	 * 
	 * @param dateSent 
	 * 				the send date of the email
	 */
	public void setDateSent(final Timestamp dateSent){
		this.dateSent.set(dateSent.toLocalDateTime());
	}
	/**
	 * @return the dateReceived
	 */
	public LocalDateTime getDateReceived() {
		return dateReceived.get();
	}
	/**
	 * Alternative getDateReceived method that returns a Timestamp(for ease of use with DB)
	 * Added in version 0.2.3
	 * 
	 * @return the dateReceived
	 */
	public Timestamp getDateReceivedAsTimestamp() {
		if (dateReceived == null)
			return null;
		else
			return Timestamp.valueOf(dateReceived.get());
	}
	
	public ObjectProperty<LocalDateTime> dateReceivedProperty(){
		return dateReceived;
	}
	/**
	 * @param dateReceived 
	 * 				the date the email was received, that will be set into bean.
	 */
	public void setDateReceived(final LocalDateTime dateReceived) {
		this.dateReceived.set(dateReceived);
	}
	/**
	 * Overload of dateReceived that takes a TimeStamp (for use with DB methods)
	 * Added in version 0.2.3
	 * 
	 * @param dateReceived 
	 * 				the date the email was received, that will be set into bean.
	 */
	public void setDateReceived(final Timestamp dateReceived) {
		this.dateReceived.set(dateReceived.toLocalDateTime());
	}
	/**
	 * There is no set when working with collections. When you get the ArrayList
	 * you can add elements to it. A set method implies changing the current
	 * ArrayList for another ArrayList and this is something we rarely do with
	 * collections.
	 * @return the fileAttachments
	 */
	public ArrayList<EmailAttachment> getFileAttachments() {
		return fileAttachments;
	}

	/**
	 * There is no set when working with collections. When you get the ArrayList
	 * you can add elements to it. A set method implies changing the current
	 * ArrayList for another ArrayList and this is something we rarely do with
	 * collections.
	 * @return the embedAttachments
	 */
	public ArrayList<EmailAttachment> getEmbedAttachments() {
		return embedAttachments;
	}

	/**
	 * Overridden hasCode method used in comparison of object when sorting for performance.
	 * Other wise use equals to compare messages for equality.
	 * 
	 * All fields checked in hashcode should also be the fields checked in equals.
	 * 
	 * MAJOR REVAMP IN PHASE 2 (reorder and log)
	 * @version 0.1.0-SNAPSHOT , Phase 2 - last modified 09/23/15
	 * 
	 * @return result the "uniqueish" integer result of the hasCode calculation.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((toField == null) ? 0 : toField.hashCode());
		result = prime * result + ((fromField == null) ? 0 : fromField.hashCode());
		result = prime * result + ((ccField == null) ? 0 : ccField.hashCode());
		result = prime * result + ((subjectField == null) ? 0 : subjectField.hashCode());
		result = prime * result + ((textMessageField == null) ? 0 : textMessageField.hashCode());
		result = prime * result + ((htmlMessageField == null) ? 0 : htmlMessageField.hashCode());
		result = prime * result + ((embedAttachments == null) ? 0 : embedAttachments.hashCode());
		result = prime * result + ((fileAttachments == null) ? 0 : fileAttachments.hashCode());
		
		return result;
	}

	/**
	 * The overridden equals method used in comparison of two objects, used for full equality.
	 * All fields used in this method are required to be checked to ensure the same message.
	 * The following fields have been omitted due to differences once sent:
	 * bcc 			-- not visible in received messages - stored in header by gmail.
	 * dateSent		-- only visible on senders side.
	 * dateReceived -- only visible on receivers side.
	 * mailStatus 	-- status changed based on sent or received.
	 * folder		-- emails in different folders may be identical.
	 * 
	 * Triming is added to fields in the case that JODD or GMail add spaces to the message/text.
	 * 
	 * All fields checked in hashcode should also be the fields checked in equals.
	 * 
	 * MAJOR REVAMP IN PHASE 2 (reorder and log)
	 * @version 0.1.0-SNAPSHOT , Phase 2 - last modified 09/23/15
	 * 
	 * TODO: ALL LOG MESSAGES SHOULD BE REMOVED BEFORE CODE IS READY FOR PRODUCTION!
	 * Log the results of each comparison for testing the values of each.
	 * Comparison of last sent email with last received email.
	 * 
	 * @param obj 
	 * 			The object used for complete equality comparison, will be casted into MailBean
	 * @return boolean value representing true or false equality.
	 */
	@Override
	public boolean equals(Object obj) {
		log.debug("EQUALS METHOD COMPARISON IN PROGRESS");
		log.trace("----------------------------------------------------------------------------------------------");
		if (this == obj) 
		{
			log.debug("Inequality caused by: this == obj");
			return true;
		}
		if (obj == null) 
		{
			log.debug("Inequality caused by: obj == null");
			return false;
		}
		if (!(obj instanceof MailBean))
		{
			log.debug("Inequality caused by: !obj instanceof MailBean");
			return false;
		}
		MailBean other = (MailBean) obj;	//CAST INTO MAILBEAN
		if (toField == null) {
			if (other.toField != null) 
				if(toField.size() != other.getToField().size())
			{
				log.debug("Inequality caused by: toField == null OR other.toField != null OR toField.size() != other.getToField().size()");	
				log.trace("       To #: " + this.getToField().size() + " : " + other.getToField().size());
				return false;
			}
		} 
		else
		{
			for (int i = 0; i < toField.size() ; i++)
			{
				if(!toField.get(i).trim().equals(other.toField.get(i).trim()))
				{
					log.debug("Inequality caused by: !toField.get(i).trim().equals(other.toField.get(i).trim()");	
					log.trace("         To: " + this.getToField().get(i) + " : " + other.getToField().get(i));
					return false;
				}
			}
		}
		if (fromField == null) {
			if (other.fromField != null) 
			{
				log.debug("Inequality caused by: fromField == null OR other.fromField != null");
				log.trace("From length: " + this.getFromField().length() + " : " + other.getFromField().length());
				return false;
			}
		} 
		else if (!fromField.get().trim().equals(other.fromField.get().trim())) 
		{
			log.debug("Inequality caused by: !fromField.trim().equals(other.fromField.trim(");	
			log.trace("       From: " + this.getFromField() + " : " + other.getFromField());
			return false;
		}
		if (ccField == null) {
			if (other.ccField != null) 
				if (ccField.size() != other.getCcField().size())
			{
				log.debug("Inequality caused by: ccField == null OR other.ccField != null OR ccField.size() != other.getCcField().size()");
				log.trace("       CC #: " + this.getCcField().size() + " : " + other.getCcField().size());
				return false;
			}
		} 
		else
		{
			for (int i = 0; i<ccField.size() ; i++)
			{
				if(!ccField.get(i).trim().equals(other.ccField.get(i).trim()))
				{
					log.debug("Inequality caused by: !ccField.get(i).trim().equals(other.ccField.get(i).trim()");
					log.trace("         CC: " + this.getCcField().get(i) + " : " + other.getCcField().get(i));
					return false;
				}
					
			}
		}
		if (subjectField == null) {
			if (other.subjectField != null) 
			{
				log.debug("Inequality caused by: subjectField == null OR other.subjectField != null");
				log.trace("Subj length: " + this.getSubjectField() + " : " + other.getSubjectField());
				return false;
			}
		} else if (!subjectField.get().trim().equals(other.subjectField.get().trim())) 
		{
			log.debug("Inequality caused by: !subjectField.trim().equals(other.subjectField.trim()");
			log.trace("    Subject: " + this.getSubjectField() + " : " + other.getSubjectField());
			return false;
		}

		if (textMessageField == null) {
			if (other.textMessageField != null) 
			{
				log.debug("Inequality caused by: textMessageField == null OR other.textMessageField != null");
				log.trace("Text Length: " + this.getTextMessageField().trim().length() + " : " + other.getTextMessageField().trim().length());
				return false;
			}
		} 
		else
		if (!textMessageField.get().trim().equals(other.textMessageField.get().trim())) 
		{
			log.debug("Inequality caused by: !textMessageField.trim().equals(other.textMessageField.trim()");			
			log.trace(" Plain Text: " + this.getTextMessageField().trim() + " : " + other.getTextMessageField().trim());
			return false;
		}
		//FIXME Will allow HTML message fields to be null so plain text only message can be sent.
		/*
		if (htmlMessageField == null) {
			if (other.htmlMessageField != null) 
			{
				log.debug("Inequality caused by: htmlMessageField == null OR other.htmlMessageField != null");	
				log.trace("Html Length: " + this.getHtmlMessageField().trim().length() + " : " + other.getHtmlMessageField().length());
				return false;
			}
		} 
		else 
		*/
		if (htmlMessageField != null & other.htmlMessageField!=null) //SEE ABOVE
		if (!htmlMessageField.get().trim().equals(other.htmlMessageField.get().trim())) 
		{
			log.debug("Inequality caused by: !htmlMessageField.trim().equals(other.htmlMessageField.trim()");	
			log.trace("  HTML Text: " + this.getHtmlMessageField().trim() + " : " + other.getHtmlMessageField().trim());
			return false;
		}
		if (embedAttachments == null) {
			if (other.embedAttachments != null) 
				if(embedAttachments.size() != other.getEmbedAttachments().size())
			{
				log.debug("Inequality caused by: embedAttachments == null OR other.embedAttachments != null OR embedAttachments.size() != other.getEmbedAttachments().size(");
				log.trace("    Embed #: " + this.getEmbedAttachments().size() + " : " + other.getFileAttachments().size());
				return false;
			}
		}
		if (embedAttachments != null)
		{
			for(int i = 0 ; i < embedAttachments.size(); i++ )
			{
				if (!Arrays.equals(embedAttachments.get(i).toByteArray(), other.embedAttachments.get(i).toByteArray()))
				{
					log.debug("Inequality caused by: embedAttachments.get(i).toByteArray(), other.embedAttachments.get(i).toByteArray()");
					log.trace(" Embed name: " + this.getEmbedAttachments().get(i).getName() + " : " + other.getEmbedAttachments().get(i).getName());
					return false;
				}
			}			
		}		
		if (fileAttachments == null) {
			if (other.fileAttachments != null)
				if (fileAttachments.size() != other.getFileAttachments().size())
			{
				log.debug("Inequality caused by: fileAttachments == null OR other.fileAttachments != null OR fileAttachments.size() != other.getFileAttachments().size()");				
				log.trace("   Attach #: " + this.getFileAttachments().size() + " : " + other.getFileAttachments().size());
				return false;
			}
		}
		else 
		{
			for(int i = 0 ; i < fileAttachments.size(); i++ )
			{
				if (!Arrays.equals(fileAttachments.get(i).toByteArray(), other.fileAttachments.get(i).toByteArray()))
				{
					log.debug("Inequality caused by: fileAttachments.get(i).toByteArray(), other.fileAttachments.get(i).toByteArray()");	
					log.trace("Attach name: " + this.getFileAttachments().get(i).getName() + " : " + other.getFileAttachments().get(i).getName());
					return false;				
				}

			}	
		}
		log.trace("----------------------------------------------------------------------------------------------");
		log.debug("EQUALS METHOD COMPLETED WITH NO INCONSISTENCIES");
		// All Fields Match
		return true;
	}

	public int getId() {
		return id.get();
	}
	
	public IntegerProperty idProperty(){
		return id;
	}

	public void setId(final int id) {
		this.id.set(id);
	}
}
