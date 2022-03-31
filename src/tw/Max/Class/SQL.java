package tw.Max.Class;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;

import tw.Max.Class.BCrypt;

public class SQL {

	
	public void SQL() {
		Properties prop = new Properties();
		prop.put("user", "root");
		prop.put("password", "");
		String DB = "MiddleProject";
		
		String sql = "insert into account (UserID, UserName, Account, Passwd) values (?,?,?,?)";
		
		try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/".concat(DB), prop)) {
			
			String passwd = "123456";
			String hashPasswd = BCrypt.hashpw(passwd, BCrypt.gensalt());
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, 3);
			ps.setString(2, "Max");
			ps.setString(3,	"maxkuo712");
			ps.setString(4,	hashPasswd);
			
			int n = ps.executeUpdate();
			
			System.out.println(n);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
