package tw.Max.Class;

import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class FileTree extends JTree {
	public DefaultMutableTreeNode root;
	private TreeModel model;
	private String DB = "MiddleProject";
	private String Account = "root"; // 取得輸入的帳號
	private String Password = ""; // 取得輸入的密碼
	
	public FileTree(String UserAccount) {
		root = new DefaultMutableTreeNode(UserAccount); // 初始節點帶入UserAccount
		model = new DefaultTreeModel(root);
		setModel(model);
		
		loadsqlTabs(UserAccount); // 開啟視窗時 自動放入檔案 新增Tree Node
	}
	
	// 開啟視窗時 自動放入檔案 新增Tree Node
	private void loadsqlTabs(String UserAccount) {
		SQLQuery sqlQuery = new SQLQuery(this.DB, this.Account, this.Password);
		LinkedList<String> tabs = sqlQuery.guerySqlShowTabs(UserAccount);
		
		for ( int i = 0; i < tabs.size(); i++ ) {
			addFileTreeNode(tabs.get(i)); // 取出LinkedList東西 放入Node
		}
	}
	
	// 取出LinkedList東西 放入Node
	public void addFileTreeNode(String tabName) {
		root.add(new DefaultMutableTreeNode(tabName));
		setModel(new DefaultTreeModel(root));
	}
	
	// 刪除Node 移除sql內資料
	public void removeFileTreeNode(String account, int index) {
		String tabName = getNodeName(index);
		SQLInsert sqlInsert = new SQLInsert(this.DB, this.Account, this.Password);
		SQLDelete sqlDelete = new SQLDelete(this.DB, this.Account, this.Password);
		
		// 備份檔案
		Boolean backupStatus = sqlInsert.insertTabTextBackup(account, tabName);
		
		// 成功備份才能刪除檔案
		if (isDelete()) {
			if (backupStatus) {
				// 刪除檔案
				Boolean deleteStatus = sqlDelete.deleteTabText(account, tabName);
				
				// 刪除成功才能刪除Tree Node
				if (deleteStatus) {
					// 刪除Tree Node
					root.remove(index);
					model = new DefaultTreeModel(root);
					setModel(model);
					JOptionPane.showMessageDialog(null, "刪除成功");
				} else {
					JOptionPane.showMessageDialog(null, "刪除失敗");
				}
			}
		}
	}
	
	// 問User是否要刪除
	private boolean isDelete() {
		int isClose = JOptionPane.showConfirmDialog(null, "確定要	刪除該頁籤？", "刪除頁籤", JOptionPane.YES_NO_OPTION);
		if (isClose == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// 取得Node名字
	public String getNodeName(int index) {
		return root.getChildAt(index).toString();
	}
}
