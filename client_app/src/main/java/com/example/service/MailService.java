package com.example.service;

import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailService {

	public static void sendEmail(Session session, String toAdd, String sub, String messageBody){//This is the statci method which can be use to send mail by passing proper parameter
		try
	    {//This is a service which can be call from EmployeeService as many time as u want
	      MimeMessage msg = new MimeMessage(session);
	     //Need to set type of data being send.
	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");//header type UTF-8 so that normal user can read text
	      msg.addHeader("format", "flowed");//dont know why it is required 
	      msg.addHeader("Content-Transfer-Encoding", "8bit");//tranfer in packate of 8bit
	      msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-ID"));//this is footer to display do not reply back on this mail
	      msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));//setting it false not to reply back
	      msg.setSubject(sub, "UTF-8"); //UTF-8 is commonly use
	      msg.setText(messageBody, "UTF-8");//to set it in readable format that is UTF-8
	      msg.setSentDate(new Date());//sending time data
	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAdd, false));//adding the email address where to send mail
	      System.out.println("Message is ready");
    	  Transport.send(msg);  //finally sendingthe message
	      System.out.println("Email Sent Successfully!!");
	    }
	    catch (Exception e) {
	     System.err.println(e.getMessage());//in case some error occured
	    }
	}
}
