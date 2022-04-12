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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

public class SendGmailOAuth extends Thread {
	private String UserMail, RandomPasswd;
	
	public SendGmailOAuth(String UserMail, String RandomPasswd) {
		this.UserMail = UserMail;
		this.RandomPasswd = RandomPasswd;
	}

	public void run() {
		String gmailUser = "maxtestmailforjava@gmail.com"; // Gmail帳號
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
			String gmailAccessToken = getAccesssToken();
			Message msg = new MimeMessage(getMailSession);
			msg.setFrom(new InternetAddress(fromAdmin));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
			msg.setSubject("MaxTextEditor 請重新修改密碼"); // 設定主旨
			msg.setText(
					"Hello, 這裡是MaxTextEditor,\n\n" + 
					"你的新密碼是：" + this.RandomPasswd + 
					"\n請進入系統重新更改密碼，謝謝"); // 設定郵件內容
			msg.saveChanges();
			
			Transport transport = getMailSession.getTransport("smtp");
			transport.connect("smtp.gmail.com", gmailUser, gmailAccessToken);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close(); // 關閉
			JOptionPane.showMessageDialog(null, "密碼已寄到您的信箱");
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	protected String getAccesssToken() {
		String TOKEN_URL = "https://oauth2.googleapis.com/token";
		String oauthClientId = "850038828756-sgkuii0nr1fs02ta0t22j07pf4hclh4f.apps.googleusercontent.com";
		String oauthSecret = "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";
		String refreshToken = "1//0eFkNrJZMUcVrCgYIARAAGA4SNwF-L9Irap_N3V89dfjLz4VzfUorT7EzkhJCQ5fCVuJm9yGOXiDyJQOxYNOefDbgLoJ3PR_pH4A\n";
		String accessToken = "";
		long tokenExpires = 1458168133864L;

		if (System.currentTimeMillis() > tokenExpires) {
		    try {
		        String request = 
		        		  "client_id=" + URLEncoder.encode(oauthClientId, "UTF-8")
		                + "&client_secret=" + URLEncoder.encode(oauthSecret, "UTF-8")
		                + "&refresh_token=" + URLEncoder.encode(refreshToken, "UTF-8")
		                + "&grant_type=refresh_token";
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
		            result = new ObjectMapper().readValue(conn.getInputStream(), new TypeReference<HashMap<String, Object>>() {});
		            accessToken = (String) result.get("access_token");
		            tokenExpires = System.currentTimeMillis() + (((Number) result.get("expires_in")).intValue() * 1000);
		            return accessToken;
		        } catch (IOException e) {
		            String line;
		            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		            while ((line = in.readLine()) != null) {
		                System.out.println(line);
		            }
		            System.out.flush();
		            return null;
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        return null;
		    }
		} else {
			return null;
		}
	}
	
	protected void getRefreshToken() {
		String TOKEN_URL = "https://oauth2.googleapis.com/token";
		String oauthCode = "4/0AX4XfWgIfaQGbpPPyrkqjFORNHDMn2xw00zw5EPIf5grFpFF8k32dCS-KKti6oqivUAtjQ";
		String oauthClientId = "850038828756-sgkuii0nr1fs02ta0t22j07pf4hclh4f.apps.googleusercontent.com";
		String oauthSecret = "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";
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
		            result = new ObjectMapper().readValue(conn.getInputStream(), new TypeReference<HashMap<String, Object>>() {});
		            accessToken = (String) result.get("access_token");
		            refreshToken = (String) result.get("refresh_token");
		            System.out.println(accessToken);
		            System.out.println(refreshToken);
		            tokenExpires = System.currentTimeMillis() + (((Number) result.get("expires_in")).intValue() * 1000);
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
