/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  brintoul
 * Created: Apr 20, 2017
 */
CREATE DATABASE banana;
USE banana;
CREATE TABLE user (
    user_id int AUTO_INCREMENT PRIMARY KEY,
    username char(64) UNIQUE,
    password char(64)

);

CREATE TABLE phone_numbers (
    phone_number_id int AUTO_INCREMENT PRIMARY KEY,
    number_text char(32),
    user_id int,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE customer (
    customer_id int AUTO_INCREMENT PRIMARY KEY,
    first_name char(64),
    last_name char(64),
    number_text char(32),
    user_id int NOT NULL,
    specific_id_for_user char(128),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE message_group (
    group_id int AUTO_INCREMENT PRIMARY KEY,
    group_name char(128),
    user_id int NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE immediate_customer_message (
    message_id int AUTO_INCREMENT PRIMARY KEY,
    message_text TEXT(2048),
    customer_id int NOT NULL,
    sent bit(8) DEFAULT 0,
    number_id int NOT NULL,
    FOREIGN KEY (number_id) REFERENCES phone_numbers(phone_number_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE immediate_group_message (
    message_id int AUTO_INCREMENT PRIMARY KEY,
    message_text TEXT(2048),
    group_id int NOT NULL,
    sent bit(8) DEFAULT 0,
    number_id int NOT NULL,
    FOREIGN KEY (number_id) REFERENCES phone_numbers(phone_number_id),
    FOREIGN KEY (group_id) REFERENCES message_group(group_id)
);

CREATE TABLE scheduled_customer_message (
    message_id int AUTO_INCREMENT PRIMARY KEY,
    message_text TEXT(2048),
    customer_id int NOT NULL,
    sent bit(8) DEFAULT 0,
    number_id int NOT NULL,
    FOREIGN KEY (number_id) REFERENCES phone_numbers(phone_number_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

CREATE TABLE scheduled_group_message (
    message_id int AUTO_INCREMENT PRIMARY KEY,
    message_text TEXT(2048),
    group_id int NOT NULL,
    sent bit(8) DEFAULT 0,
    number_id int NOT NULL,
    FOREIGN KEY (number_id) REFERENCES phone_numbers(phone_number_id),
    FOREIGN KEY (group_id) REFERENCES message_group(group_id)
);

CREATE TABLE cust_group (
    group_id int,
    cust_id int,
    CONSTRAINT pk_cust_group PRIMARY KEY (group_id,cust_id),
    FOREIGN KEY (group_id) REFERENCES message_group(group_id),
    FOREIGN KEY (cust_id) REFERENCES customer(customer_id) ON DELETE CASCADE
); 

INSERT INTO user (username,password) VALUES ('brintoul',<password>);
INSERT INTO phone_numbers (number_text,user_id) VALUES ('+18583566213',1);
    
