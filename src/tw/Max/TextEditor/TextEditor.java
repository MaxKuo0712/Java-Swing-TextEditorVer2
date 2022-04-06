package tw.Max.TextEditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import tw.Max.Class.FileTree;
import tw.Max.Class.Login;
import tw.Max.Class.TabbedPane;

public class TextEditor extends JFrame {
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem addSheet, save, export, load;
	private JComboBox<String> colorComboBox, fontComboBox, sizeComboBox;
	private JPanel topPanel, mainPanel, textPanel;
	private TabbedPane tabbedPane;
	public FileTree tree;
	private String UserAccount;
	
	public TextEditor(String UserAccount) {
		// 定義視窗
		super("Text Editor");
		setSize(640, 480);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		
		// setUserAccount
		setUserAccount(UserAccount);
		
		// 選單列
		menuBar = new JMenuBar();
		add(menuBar, BorderLayout.NORTH);
		
		// 主畫面
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);
		
		// 放置主畫面上方 => 工具列
		topPanel = new JPanel(new FlowLayout());
		topPanel.setLayout(new FlowLayout());
		mainPanel.add(topPanel, BorderLayout.NORTH);
		
		// 字體調整
		fontComboBox = new JComboBox<String>();
		fontComboBox.addItem("--字體調整--");
		fontComboBox.addItem("蘋方");
		fontComboBox.addItem("黑體");
		fontComboBox.addItem("楷體");
		fontComboBox.addItem("儷黑 Pro");
		fontComboBox.addItem("儷宋 Pro");
		fontComboBox.addItem("Arial");
		fontComboBox.addItem("Calibri");
		fontComboBox.addItem("Lucida Grande");
		topPanel.add(fontComboBox);
		
		// 字體大小
		sizeComboBox = new JComboBox<String>();
		sizeComboBox.addItem("--字體大小--");
		sizeComboBox.addItem("9");
		sizeComboBox.addItem("10");
		sizeComboBox.addItem("11");
		sizeComboBox.addItem("12");
		sizeComboBox.addItem("16");
		sizeComboBox.addItem("24");
		sizeComboBox.addItem("36");
		topPanel.add(sizeComboBox);
		
		// 字體顏色
		colorComboBox = new JComboBox<String>();
		colorComboBox.addItem("--字體顏色--");
		colorComboBox.addItem("紅");
		colorComboBox.addItem("藍");
		colorComboBox.addItem("黑");
		topPanel.add(colorComboBox);
		
		// textarea, tabs
		textPanel = new JPanel();
		textPanel.setLayout(new BorderLayout());
		mainPanel.add(textPanel, BorderLayout.CENTER);
		
		// 視窗頁籤
		tabbedPane = new TabbedPane(this.UserAccount);
		textPanel.add(tabbedPane, BorderLayout.CENTER);
		
		// MenuItem
		fileMenu = new JMenu("檔案");
		menuBar.add(fileMenu);
		
		// 新增頁籤
		addSheet = new JMenuItem("新增文件");
		fileMenu.add(addSheet);
		
		// 儲存
		save = new JMenuItem("儲存檔案");
		fileMenu.add(save);
		
		// 匯出檔案
		export = new JMenuItem("匯出檔案");
		fileMenu.add(export);
		
		// 開啟檔案
		load = new JMenuItem("匯入檔案");
		fileMenu.add(load);
		
		// Tree
		tree = new FileTree(this.UserAccount);
		add(tree, BorderLayout.WEST);
		
		// Listener
		setListener();
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void setUserAccount(String UserAccount) {
		this.UserAccount = UserAccount;
	}
	
	private void setListener() {
		// 字體調整
		fontComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTextAreaFont();
			}
		});
		
		// 字體大小
		sizeComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTextAreaFontSize();
			}
		});
		
		// 字體顏色
		colorComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTextAreaFontColor();
			}
		});
		
		// 新增頁籤
		addSheet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addSheet();
			}
		});

		// 存檔
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 沒有頁籤的時候，不能執行儲存
				if(tabbedPane.getTabSize() > 0) {
					save();
				}
			}
		});
	
		// 匯出檔案
		export.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 沒有頁籤的時候，不能執行儲存
				if(tabbedPane.getTabSize() > 0) {
					exportFile();
				}
			}
		});
		
		// 匯入檔案
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				load();
			}
		});
		
		// Remove Tree Node
		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) { 
				if (e.isPopupTrigger()) {
					popupEvent(e); 
				} else if (e.getClickCount() == 2) {
					loadTabEvent(e); 
				}
			} 
			
			// load tab
			private void loadTabEvent(MouseEvent e) { 
				JTree tree = (JTree)e.getSource(); 
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				if (path == null) return; 
				
				tree.setSelectionPath(path);	
				DefaultMutableTreeNode doubleClickedNode = (DefaultMutableTreeNode)path.getLastPathComponent(); 
				
				if (doubleClickedNode.isLeaf()) {
					loadTabText();
			    } 
			} 			
			
			private void popupEvent(MouseEvent e) { 
				JTree tree = (JTree)e.getSource(); 
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				if (path == null) return; 
				
				tree.setSelectionPath(path);	
				DefaultMutableTreeNode rightClickedNode = (DefaultMutableTreeNode)path.getLastPathComponent(); 
				
				if (rightClickedNode.isLeaf()) {
					JPopupMenu popup = new JPopupMenu();
					final JMenuItem refreshMenuItem = new JMenuItem("刪除檔案"); 
					
					refreshMenuItem.addActionListener(new ActionListener(){
						@Override 
						public void actionPerformed(ActionEvent actionEvent) { 
							removeTreeNode();
						} 
				    }); 
					
				    popup.add(refreshMenuItem); 
				    popup.show(tree, e.getX(), e.getY()); 
			    } 
			} 
		});
	}
	
	// 設定字體
	private void setTextAreaFont() {
		String font = fontComboBox.getSelectedItem().toString();
		tabbedPane.setTextPaneFont(font);
	}
	
	// 設定字體大小
	private void setTextAreaFontSize() {
		String fontSize = sizeComboBox.getSelectedItem().toString();
		tabbedPane.setTextPaneFontSize(fontSize);
	}
	
	// 設定字體顏色
	private void setTextAreaFontColor() {
		String fontColor = colorComboBox.getSelectedItem().toString();
		tabbedPane.setTextPaneFontColor(fontColor);
	}
	
	// 新增頁籤
	private void addSheet() {
		// 如果頁籤新增成功才新增tree node
//		if (tabbedPane.addNewTabs()) {
//			tree.addFileTreeNode(tabbedPane.getTextPaneName());
//		}
		tabbedPane.addNewTabs();
	}

	// 儲存
	private void save() {
		int saveResult = tabbedPane.saveTabs(this.UserAccount);
		if (saveResult == 1) {
			
		} else if (saveResult == 2) {
			tree.addFileTreeNode(tabbedPane.getTextPaneName());
		}
	}
	
	// 匯出檔案
	private void exportFile() {
		tabbedPane.exportFile();
	}

	// 匯入檔案
	private void load() {
		tabbedPane.load();
	}
	
	// Remove Tree Node
	private void removeTreeNode() {
		DefaultMutableTreeNode path = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path.getParent();
		if (parent != null) {
			int selectedIndex = parent.getIndex(path);
			tree.removeFileTreeNode(this.UserAccount, selectedIndex);
		}
	}
	
	// load text
	private void loadTabText() {
//		tabbedPane.loadTabText(UserAccount, null);
		DefaultMutableTreeNode path = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path.getParent();
		if (parent != null) {
			int selectedIndex = parent.getIndex(path);
			String tabName = tree.getNodeName(selectedIndex);
			tabbedPane.loadTabText(this.UserAccount, tabName);
		}
	}
	
	// 程式進入點
//	public static void main(String[] args) {
//		new TextEditor();
//	}
}


