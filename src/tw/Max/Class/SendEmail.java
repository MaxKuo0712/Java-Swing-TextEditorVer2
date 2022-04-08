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
		String host = "smtp.gmail.com";
		int port = 587; // SMTP Port
		String gmailUser = "maxtestmailforjava@gmail.com"; // Gmail帳號
		String gmailPasswd = "qazwsxedc@12345"; // Gmail密碼
		String fromAdmin = gmailUser; // 寄件者
		String toUser = this.UserMail; // 收件者

		Properties prop = System.getProperties();
		prop.put("mail.smtp.auth", "true"); // false是default, true是利用auth命令來驗證帳戶 -> gmail帳號密碼
		prop.put("mail.smtp.starttls.enable", "true"); // 啟用Tls 加密 類似于 SSL，tls 會保護連線的安全
		prop.put("mail.smtp.host", host); // 連接gmail smpt
		prop.put("mail.smtp.port", port); // 連接port
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // certificate hostname驗證
		
		Session getMailSession = Session.getDefaultInstance(prop, null); // 建立Session
		
		try {
			MimeMessage generateMailMessage = new MimeMessage(getMailSession); 
			generateMailMessage.setFrom(new InternetAddress(fromAdmin)); // 設定寄件人
			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser)); // 設定收件人
			generateMailMessage.setSubject("MaxTextEditor 請重新修改密碼"); // 設定主旨
			generateMailMessage.setText(
					"Hello, 這裡是MaxTextEditor,\n\n" + 
					"你的新密碼是：" + this.RandomPasswd + 
					"\n請進入系統重新更改密碼，謝謝"); // 設定郵件內容
			
			// 開始傳遞
			Transport transport = getMailSession.getTransport("smtp"); // 建立
			transport.connect("smtp.gmail.com",587, gmailUser, gmailPasswd); // 連線
			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients()); // 發送mail
			transport.close(); // 關閉
			JOptionPane.showMessageDialog(null, "密碼已寄到您的信箱");
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
}
