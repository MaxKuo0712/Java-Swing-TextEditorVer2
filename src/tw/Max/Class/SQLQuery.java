package tw.Max.Class;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;

import javax.swing.JTextPane;

public class SQLQuery {
	private String DB;
	private String User;
	private String Passwd;

	public SQLQuery(String DB, String User, String Passwd) {
		this.DB = "jdbc:mysql://localhost:3306/".concat(DB);
		this.User = User;
		this.Passwd = Passwd;
	}
	
	// 查看登入帳號密碼是否存在且正確
	public int querySqlLoginResult(String Account, String Password) {
		return checkLogin(Account, Password);
	}
	
	// 查看檔案是否存在於資料庫
	public Boolean guerySqlTabsExistResult(String Account, String TabName) {
		return isTabsExist(Account, TabName);
	}
	
	// 讀取檔案 用於Tree Show File
	public LinkedList<String> guerySqlShowTabs(String Account) {
		return loadTabs(Account);
	}
	
	// 取得TextPane物件
	public JTextPane guerySqlTabsText(String Account, String TabName) {
		return loadTabsText(Account, TabName);
	}
	
	// 查詢User輸入的帳號及mail是否存在
	public Boolean queryUserMail(String Account, String Mail) {
		return checkMail(Account, Mail);
	}
	
	// 查看登入帳號密碼是否存在且正確
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
					return 1; // 登入成功
				} else {
					return 2; // 密碼錯誤
				}
			} else {
				return 0; // 帳號不存在
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 4; // 出事
		}
	}
	
	// 查看檔案是否存在於資料庫
	private Boolean isTabsExist(String Account, String TabName) {
		String DB = this.DB;
		String User = this.User;
		String Passwd = this.Passwd;
		
		String sql = "select count(*) as count from Content where account = ? and TabsName = ?";

		try(Connection conn = DriverManager.getConnection(DB, User, Passwd)) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, Account);
			ps.setString(2, TabName);
			
			ResultSet result = ps.executeQuery();
			
			if(result.next()) {
				if (result.getInt("count") > 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false; // 出事
		}
	}
	
	// 讀取檔案 用於Tree Show File
	private LinkedList<String> loadTabs(String Account) {
		String DB = this.DB;
		String User = this.User;
		String Passwd = this.Passwd;
		LinkedList<String> tabs = new LinkedList<>();
		
		String sql = "select TabsName from Content where account = ? order by TabsName";

		try(Connection conn = DriverManager.getConnection(DB, User, Passwd)) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, Account);
			
			ResultSet result = ps.executeQuery();	
			while(result.next()) {
				String TabsName = result.getString("TabsName");
				tabs.add(TabsName);
			}
			return tabs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 取得TextPane物件
	private JTextPane loadTabsText(String Account, String TabName) {
		String DB = this.DB;
		String User = this.User;
		String Passwd = this.Passwd;
		Object obj = new Object();
		
		String sql = "select * from Content where account = ? and TabsName = ?";

		try(Connection conn = DriverManager.getConnection(DB, User, Passwd)) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, Account);
			ps.setString(2, TabName);

			ResultSet result = ps.executeQuery();

			if(result.next()) {
				InputStream ins = result.getBinaryStream("TabsContentObj");
				ObjectInputStream oin = new ObjectInputStream(ins); // 解序列化
				obj = oin.readObject();
				oin.close();
			}
			
			// 拿出來的東西 如果是JTextPane才回傳
			if (obj instanceof JTextPane) {
				return (JTextPane) obj;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// 查詢User輸入的帳號及mail是否存在
	private Boolean checkMail(String Account, String Mail) {
		String DB = this.DB;
		String User = this.User;
		String Passwd = this.Passwd;
		
		String sql = 
				  "SELECT count(*) as count  \n"
				+ "FROM userinfo as info\n"
				+ "	inner join Account as acc on acc.UserIdNumber = info.UserIdNumber\n"
				+ "where acc.Account = ? and info.UserEmail = ?";

		try(Connection conn = DriverManager.getConnection(DB, User, Passwd)) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, Account);
			ps.setString(2, Mail);
			
			ResultSet result = ps.executeQuery();
			
			if(result.next()) {
				if (result.getInt("count") > 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
