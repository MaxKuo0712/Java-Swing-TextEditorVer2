package tw.Max.Class;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import tw.Max.Class.BCrypt;
import tw.Max.TextEditor.registerFrame_Test;

public class SQLQuery {
	private String DB;
	private String User;
	private String Passwd;

	public SQLQuery(String DB, String User, String Passwd) {
		this.DB = "jdbc:mysql://localhost:3306/".concat(DB);
		this.User = User;
		this.Passwd = Passwd;
	}
	
	public int getSqlLoginResult(String Account, String Password) {
		return checkLogin(Account, Password);
	}
	
	private int checkLogin(String Account, String Password) {
		String DB = this.DB;
		String User = this.User;
		String Passwd = this.Passwd;
		
		String sql = "select * from account where account = ?";

		try(Connection conn = DriverManager.getConnection(DB, User, Passwd)) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, Account);
			
			ResultSet result = ps.executeQuery();	
			if(result.next()) {
				String hashpasswd = result.getString("Passwd");
				if (BCrypt.checkpw(Password, hashpasswd)) {
//					String userName = result.getString("Passwd");
//					String userIdNumber = result.getString("Passwd");
//					String userAccount = result.getString("Passwd");
//					String userGender = result.getString("Passwd");
//					String userBirth = result.getString("Passwd");
//					String userMail = result.getString("Passwd");
//					String userTel = result.getString("Passwd");
					
					return 1; // 登入成功
				} else {
					return 2; // 密碼錯誤
				}
			} else {
				return 0; // 帳號不存在
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return 4; // 出事
		}
	}
}
