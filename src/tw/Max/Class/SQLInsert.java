package tw.Max.Class;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SQLInsert {
	private String DB;
	private String User;
	private String Passwd;

	public SQLInsert(String DB, String User, String Passwd) {
		this.DB = "jdbc:mysql://localhost:3306/".concat(DB);
		this.User = User;
		this.Passwd = Passwd;
	}
	
	public Boolean setCreateAccount(String name, String idNumber, String account, 
			String password, String gender, String birth, String mail, String tel) {
		return createAccount(name, idNumber, account, password, gender, birth, mail, tel);
	}
	
	private Boolean createAccount(String name, String idNumber, String account, 
			String password, String gender, String birth, String mail, String tel) {
		String DB = this.DB;
		String User = this.User;
		String Passwd = this.Passwd;
		
		String userInfoSql = "insert into UserInfo (UserName, UserIdNumber, UserGender, UserBirth, UserEmail, UserTel)"
				+ "values (?,?,?,?,?,?)";
		
		String userAccountSql = "insert into Account (UserName, UserIdNumber, Account, Passwd)"
				+ "select u.UserName, u.UserIdNumber, ?, ?"
				+ "from userinfo u "
				+ "where u.UserIdNumber = ?";
		
		try (Connection conn = DriverManager.getConnection(DB, User, Passwd)) {
			conn.setAutoCommit(false);
			
			PreparedStatement psUserInfoSql = conn.prepareStatement(userInfoSql);
			psUserInfoSql.setString(1, name);
			psUserInfoSql.setString(2, idNumber);
			psUserInfoSql.setString(3, gender);
			psUserInfoSql.setString(4, birth);
			psUserInfoSql.setString(5, mail);
			psUserInfoSql.setString(6, tel);
			int userInfoResult = psUserInfoSql.executeUpdate();
			
			PreparedStatement psUserAccountSql = conn.prepareStatement(userAccountSql);
			psUserAccountSql.setString(1, account);
			psUserAccountSql.setString(2, password);
			psUserAccountSql.setString(3, idNumber);
			int userAccountResult = psUserAccountSql.executeUpdate();
			
			if (userInfoResult > 0 && userAccountResult > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
			return true;
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		
	}
}
