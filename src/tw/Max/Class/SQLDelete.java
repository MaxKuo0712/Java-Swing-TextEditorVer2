package tw.Max.Class;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SQLDelete {
	private String DB;
	private String User;
	private String Passwd;

	public SQLDelete(String DB, String User, String Passwd) {
		this.DB = "jdbc:mysql://localhost:3306/".concat(DB);
		this.User = User;
		this.Passwd = Passwd;
	}
	
	// 刪除檔案
	public Boolean deleteTabText(String account, String textName) {
		return deleteText(account, textName);
	}
	
	// 刪除檔案
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
			e.printStackTrace();
			return false;
		}
	}
}
