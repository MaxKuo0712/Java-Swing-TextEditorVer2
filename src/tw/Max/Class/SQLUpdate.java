package tw.Max.Class;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.swing.JTextPane;

public class SQLUpdate {
	private String DB;
	private String User;
	private String Passwd;

	public SQLUpdate(String DB, String User, String Passwd) {
		this.DB = "jdbc:mysql://localhost:3306/".concat(DB);
		this.User = User;
		this.Passwd = Passwd;
	}
	
	public Boolean updateTabText(String account, String textName, JTextPane text) {
		return saveTabText(account, textName, text);
	}

	private Boolean saveTabText(String account, String textName, JTextPane text) {
		String DB = this.DB;
		String User = this.User;
		String Passwd = this.Passwd;
		
		String userInfoSql = "update Content SET TabsContentObj = ?, ReviseTime = ?, ReviseName = ?"
				+ "where Account = ? and TabsName = ?";
		
		try (Connection conn = DriverManager.getConnection(DB, User, Passwd)) {
			conn.setAutoCommit(false);
			
			ByteArrayOutputStream bao = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(bao);
			oout.writeObject(text);
			
			byte[] s1Ary = bao.toByteArray();
			
			PreparedStatement psUpdateTabText = conn.prepareStatement(userInfoSql);
			psUpdateTabText.setBinaryStream(1, new ByteArrayInputStream(s1Ary));
			psUpdateTabText.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
			psUpdateTabText.setString(3, account);
			psUpdateTabText.setString(4, account);
			psUpdateTabText.setString(5, textName);
			
			int insertTabText = psUpdateTabText.executeUpdate();
			
			if (insertTabText > 0) {
				conn.commit();
				return true;
			} else {
				conn.rollback();
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
	}
}
