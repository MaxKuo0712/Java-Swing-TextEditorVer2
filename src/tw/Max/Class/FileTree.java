package tw.Max.Class;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class FileTree extends JTree {
	public DefaultMutableTreeNode root;
	private TreeModel model;
	
	public FileTree(String UserAccount) {
		root = new DefaultMutableTreeNode(UserAccount); // 初始節點帶入UserAccount
		model = new DefaultTreeModel(root);
		setModel(model);
	}
	
	public void addFileTreeNode(String tabName) {
		root.add(new DefaultMutableTreeNode(tabName));
		setModel(new DefaultTreeModel(root));
	}
	
	public void removeFileTreeNode(int index) {
		root.remove(index);
		model = new DefaultTreeModel(root);
		setModel(model);
	}
}
