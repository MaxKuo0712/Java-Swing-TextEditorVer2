package tw.Max.Class;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

public class SendEmail_OAuth2 extends Thread {
	private String UserMail, RandomPasswd;
	
	public SendEmail_OAuth2() {
	}

	public void run() {
		String host = "smtp.gmail.com";
		int port = 587; // SMTP Port
		String gmailUser = "maxtestmailforjava@gmail.com"; // Gmail帳號
		String gmailPasswd = "qazwsxedc@12345"; // Gmail密碼
		String fromAdmin = gmailUser; // 寄件者
		String toUser = this.UserMail; // 收件者

		Properties prop = System.getProperties();
		prop.put("mail.smtp.ssl.enable", "true"); // required for Gmail
		prop.put("mail.smtp.sasl.enable", "true"); 
		prop.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
		prop.put("mail.smtp.auth.login.disable", "true");
		prop.put("mail.smtp.auth.plain.disable", "true");
		
		Session getMailSession = Session.getDefaultInstance(prop, null); // 建立Session
		
		try {
			AccesssToken();
//			Message msg = new MimeMessage(getMailSession);
//			msg.setFrom(new InternetAddress(fromAdmin));
//			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
//			msg.setSubject("JavaMail OAuth2 test");
//			msg.setText("Hello, world with OAuth2!\n");
//			msg.saveChanges();
//			
//			String accessToken = "ya29.A0ARrdaM8XxjCl23hkKkCV2nvCqDIwIiLiPiav3JuAYC01kQa06A0E9JEmPMMt4zq4"
//					+ "_ieUGMEr9s06CzwEIz2riAMOPqEQGEKEABDFmrdkBlexeAGOpHe6lHJwvmJx6R6HBaiMnoCQ8pa61m2SnAbCitDxp0NF";
//			
//			Transport transport = getMailSession.getTransport("smtp");
//			transport.connect("smtp.gmail.com", gmailUser, accessToken);
//			transport.sendMessage(msg, msg.getAllRecipients());
//			transport.close(); // 關閉
//			JOptionPane.showMessageDialog(null, "密碼已寄到您的信箱");
			
			
			
//			MimeMessage generateMailMessage = new MimeMessage(getMailSession); 
//			generateMailMessage.setFrom(new InternetAddress(fromAdmin)); // 設定寄件人
//			generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser)); // 設定收件人
//			generateMailMessage.setSubject("MaxTextEditor 請重新修改密碼"); // 設定主旨
//			generateMailMessage.setText(
//					"Hello, 這裡是MaxTextEditor,\n\n" + 
//					"你的新密碼是：" + this.RandomPasswd + 
//					"\n請進入系統重新更改密碼，謝謝"); // 設定郵件內容
//			
//			// 開始傳遞
//			Transport transport = getMailSession.getTransport("smtp"); // 建立
//			transport.connect("smtp.gmail.com",587, gmailUser, gmailPasswd); // 連線
//			transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients()); // 發送mail
//			transport.close(); // 關閉
//			JOptionPane.showMessageDialog(null, "密碼已寄到您的信箱");
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	protected void AccesssToken() {
		String TOKEN_URL = "https://oauth2.googleapis.com/token";
//		String oauthCode = "4/0AX4XfWiTQSeQyo-S7Zy3YOwIzAFSX_HPWrO0TM__SVlRao_soc8ObAhM9QlQM1g9xMSH8A";
		String oauthCode = "4/0AX4XfWj0BQ8l6fxbW8KRtXxaHSkRrJnK4M8C96Gdurt_r7TfaPq29DL2XNWQu80StTPzkw";
		String oauthClientId = "850038828756-is1p9774amo86qv2hgra2u2fgl73eckv.apps.googleusercontent.com";
		String oauthSecret = "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";
//		String redirectURI = "http://localhost";
		String redirectURI = "http://localhost";
		String refreshToken = "";
		String accessToken = "";
		long tokenExpires = 1458168133864L;

		if (System.currentTimeMillis() > tokenExpires) {
		    try {
		        String request = 
		        		  "code=" + URLEncoder.encode(oauthCode, "UTF-8")
		                + "&client_id=" + URLEncoder.encode(oauthClientId, "UTF-8")
		                + "&client_secret=" + URLEncoder.encode(oauthSecret, "UTF-8")
		                + "&redirect_uri=" + URLEncoder.encode(redirectURI, "UTF-8")
		                + "&grant_type=authorization_code";
		        HttpURLConnection conn = (HttpURLConnection) new URL(TOKEN_URL).openConnection();
		        conn.setDoOutput(true);
		        conn.setRequestMethod("POST");
		        PrintWriter out = new PrintWriter(conn.getOutputStream());
		        out.print(request); // note: println causes error
		        out.flush();
		        out.close();
		        conn.connect();
		        try {
		            HashMap<String, Object> result;
		            System.out.println(conn.getInputStream());
		            
//		            result = new ObjectMapper().readValue(conn.getInputStream(), new TypeReference<HashMap<String, Object>>() {
//		            });
//		            accessToken = (String) result.get("access_token");
//		            tokenExpires = System.currentTimeMillis() + (((Number) result.get("expires_in")).intValue() * 1000);
		        } catch (IOException e) {
		            String line;
		            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		            while ((line = in.readLine()) != null) {
		                System.out.println(line);
		            }
		            System.out.flush();
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
	}
}
