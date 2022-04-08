package tw.Max.Class;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

public class SendEmail extends Thread {
	private String UserMail, RandomPasswd;
	
	public SendEmail(String UserMail, String RandomPasswd) {
		this.UserMail = UserMail;
		this.RandomPasswd = RandomPasswd;
	}
	
	public void run() {
		String host = "smpt.gmail.com";
		int port = 587;
		String gmailUser = "maxtestmailforjava@gmail.com";
		String gmailPasswd = "qazwsxedc@12345";
		String fromAdmin = "maxtestmailforjava@gmail.com";
		String toUser = this.UserMail;

		Properties prop = System.getProperties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", port);
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		
		Session getMailSession = Session.getDefaultInstance(prop, null);
		
		try {
			MimeMessage generateMailMessage = new MimeMessage(getMailSession); 
			generateMailMessage.setFrom(new InternetAddress(fromAdmin)); // Create a default MimeMessage object.
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser)); // Set From: header field of the header.
			generateMailMessage.setSubject("這裡是MaxTextEditor, 請重新修改密碼"); // Set Subject: header field
			generateMailMessage.setText(
					"Hello, 這裡是MaxTextEditor,\n\n" + 
					"你的新密碼是：" + this.RandomPasswd + 
					"\n請進入系統重新更改密碼，謝謝"); // Now set the actual message
			 
			Transport transport = getMailSession.getTransport("smtp");
			transport.connect("smtp.gmail.com",587, gmailUser, gmailPasswd);
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
			transport.close();
			JOptionPane.showMessageDialog(null, "密碼已寄到您的信箱");
        } catch (Exception e) {
        	System.out.println(e);
        	e.printStackTrace();
        }
	}
}
