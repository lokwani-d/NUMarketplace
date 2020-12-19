/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.finalproject.utils;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 * @author Deepak_Lokwani
 * 
 * NUID: 001316769
 * 
 * Project name: Finalproject
 * Package name: com.finalproject.utils
 *
 */
public class Mail {

    private String to;
    private String from;
    private String userName;
    private String password;

    public Mail(String to) {
        this.to = to;
        this.from = "deepaklokwani@rocketmail.com";
        this.userName ="apikey";
        this.password = "XXXXXXX";
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        this.userName = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void sendMail(String subject, String token) {
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.sendgrid.net");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(getUserName(), getPassword());
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.addFrom(new Address[]{new InternetAddress(getFrom())});
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(getTo()));
            message.setSubject(subject);
            message.setContent("<h2>Click the link given below to reset Password </h2><br/><br/>"
                    + "        <a href='http://localhost:8080/edu/updatePassword.htm?token="+token+"'>"
                            + "http://localhost:8080/edu/updatePassword.htm?token="+token+"</a><br/><br/>"
                    + "        <p>**This link expires in 10 minutes</p>"
                    + "    ", "text/html");
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

