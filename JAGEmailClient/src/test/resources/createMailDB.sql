DROP DATABASE IF EXISTS MAILDB;

CREATE DATABASE MAILDB;

USE MAILDB;

GRANT ALL PRIVILEGES ON MAILDB.* TO christopher@"%" IDENTIFIED BY "password";
GRANT ALL PRIVILEGES ON MAILDB.* TO christopher@"localhost" IDENTIFIED BY "password";

DROP TABLE IF EXISTS RECIPIENT;
DROP TABLE IF EXISTS ATTACHMENT;
DROP TABLE IF EXISTS MAILMESSAGE;
DROP TABLE IF EXISTS FOLDER;

CREATE TABLE folder(
	folder_id INT(2) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	folder_name VARCHAR(30) NOT NULL UNIQUE
)ENGINE=InnoDB;

CREATE TABLE email(
	email_id INT(6) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	from_field VARCHAR(50) NOT NULL ,
	subject VARCHAR(70) NOT NULL DEFAULT '',
	text MEDIUMTEXT NOT NULL,
	html MEDIUMTEXT,
	sent_date TIMESTAMP DEFAULT NULL,
	receive_date TIMESTAMP DEFAULT NULL,
	folder_id INT(2) NOT NULL,
	mail_status INT(1) NOT NULL, 
	CONSTRAINT email_fk1 FOREIGN KEY (folder_id) REFERENCES folder(folder_id) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE attachment(
	attach_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	content_id VARCHAR(30),
	attach_name VARCHAR(30) NOT NULL,
	attach_size INT NOT NULL,
	content MEDIUMBLOB NOT NULL,
	email_id INT(6) NOT NULL,
	CONSTRAINT attachment_fk1 FOREIGN KEY (email_id) REFERENCES email(email_id) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE recipient(
	recipient_id INT(6) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	address VARCHAR(50) NOT NULL,
	address_type INT(1) NOT NULL, 
	email_id INT(6) NOT NULL,
	CONSTRAINT recipient_fk1 FOREIGN KEY (email_id) REFERENCES email(email_id) ON DELETE CASCADE
)ENGINE=InnoDB;

--Following are mock data used to seed the database outside of test scripts
INSERT INTO folder(folder_name) 
VALUES("inbox"),
("sent"),
("draft");

INSERT INTO email (from_field, subject, text, folder_id, mail_status)
VALUES ("first.from@email","First mock subject", "First mock email text content", 3,0);

INSERT INTO email (from_field, subject, text, html, folder_id, mail_status)
VALUES ("second.from@email","second mock subject", "second mock email text content", "<html> here is some html </html>", 3, 0);




