package tw.Max.Class;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class SendEmail {
	private String AdminMail, UserMail, RandomPasswd;
	
	public SendEmail(String AdminMail, String UserMail, String RandomPasswd) {
		this.AdminMail = AdminMail;
		this.UserMail = UserMail;
		this.RandomPasswd = RandomPasswd;
	}
	
	public void sendMail() {
		String fromAdmin = this.AdminMail;
		String toUser = this.UserMail;
		String host = "localhost";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);
		Session session = Session.getDefaultInstance(properties);
		
		 try {
	         // Create a default MimeMessage object.
	         MimeMessage message = new MimeMessage(session);

	         // Set From: header field of the header.
	         message.setFrom(new InternetAddress(fromAdmin));

	         // Set To: header field of the header.
	         message.addRecipient(RecipientType.TO, new InternetAddress(toUser));

	         // Set Subject: header field
	         message.setSubject("This is the Subject Line!");

	         // Now set the actual message
	         message.setText("This is actual message : " + this.RandomPasswd);

	         // Send message
	         Transport.send(message);
	         System.out.println("Sent message successfully....");
	      } catch (Exception e) {
	    	  System.out.println(e);
	    	  e.printStackTrace();
	      }
	   }
}
