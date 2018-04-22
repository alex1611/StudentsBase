CREATE DATABASE IF NOT EXISTS dbStudy CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE USER 'class_admin'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON dbStudy.* TO 'class_admin'@'localhost';
FLUSH PRIVILEGES;
USE dbStudy;
CREATE TABLE STUDENTS(
PERSON_ID INTEGER,
FIRST_NAME VARCHAR(30) NOT NULL,
SECOND_NAME VARCHAR(30) NOT NULL,
LAST_NAME VARCHAR(30) NOT NULL,
BIRTH_DAY DATE NOT NULL,
CLASS VARCHAR(30),
PRIMARY KEY (PERSON_ID)
);
SET NAMES 'utf8';