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

public class SQLDelete {
	private String DB;
	private String User;
	private String Passwd;

	public SQLDelete(String DB, String User, String Passwd) {
		this.DB = "jdbc:mysql://localhost:3306/".concat(DB);
		this.User = User;
		this.Passwd = Passwd;
	}
	
	public Boolean deleteTabText(String account, String textName) {
		return deleteText(account, textName);
	}

	private Boolean deleteText(String account, String textName) {
		String DB = this.DB;
		String User = this.User;
		String Passwd = this.Passwd;
		
		String deleteTextSql = "delete from Content where Account = ? and TabsName = ?";
		
		try (Connection conn = DriverManager.getConnection(DB, User, Passwd)) {
			conn.setAutoCommit(false);
			
			PreparedStatement psDeleteTextSql = conn.prepareStatement(deleteTextSql);
			psDeleteTextSql.setString(1, account);
			psDeleteTextSql.setString(2, textName);
			
			int insertTabText = psDeleteTextSql.executeUpdate();
			
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
