package tw.Max.Class;

import java.awt.EventQueue;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Properties;

import javax.swing.JTextPane;

import tw.Max.Class.BCrypt;

public class SQLQuery {
	private String DB;
	private String User;
	private String Passwd;

	public SQLQuery(String DB, String User, String Passwd) {
		this.DB = "jdbc:mysql://localhost:3306/".concat(DB);
		this.User = User;
		this.Passwd = Passwd;
	}
	
	public int querySqlLoginResult(String Account, String Password) {
		return checkLogin(Account, Password);
	}
	
	public Boolean guerySqlTabsExistResult(String Account, String TabName) {
		return isTabsExist(Account, TabName);
	}
	
	public LinkedList<String> guerySqlShowTabs(String Account) {
		return loadTabs(Account);
	}
	
	public JTextPane guerySqlTabsText(String Account, String TabName) {
		return loadTabsText(Account, TabName);
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
	
	private Boolean isTabsExist(String Account, String TabName) {
		String DB = this.DB;
		String User = this.User;
		String Passwd = this.Passwd;
		
		String sql = "select * from Content where account = ? and TabsName = ?";

		try(Connection conn = DriverManager.getConnection(DB, User, Passwd)) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, Account);
			ps.setString(2, TabName);
			
			ResultSet result = ps.executeQuery();
			
			if(result.next()) {
//				String TabsName = result.getString("TabsName");
				if (result.getRow() > 0) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return false; // 出事
		}
	}
	
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
			System.out.println(e.toString());
			return null;
		}
	}

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
			
			if (obj instanceof JTextPane) {
				JTextPane a = (JTextPane) obj;
				return (JTextPane) obj;
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
	}
}
