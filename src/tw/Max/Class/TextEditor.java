package tw.Max.Class;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.text.StyledEditorKit;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class TextEditor extends JFrame {
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem addSheet, save, export, load;
	private JComboBox<String> colorComboBox, fontComboBox, sizeComboBox, styleComboBox;
	private JPanel topPanel, mainPanel, textPanel;
	private TabbedPane tabbedPane;
	public FileTree tree;
	private String UserAccount;
	
	public TextEditor(String UserAccount) {
		// 定義視窗
		super("Text Editor");
		setSize(800, 600);
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
		fontComboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] {"--字體調整--", 
						"蘋方", "黑體", "楷體", "儷黑 Pro", "儷宋 Pro", "Arial", "Calibri", 
						"Lucida Grande", "Times New Roman", "Symbol"}));
		topPanel.add(fontComboBox);
		
		// 字體大小
		sizeComboBox = new JComboBox<String>();
		sizeComboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] {"--字體大小--", 
						"8", "10", "12", "14", "16", "18", "24", "36"}));
		topPanel.add(sizeComboBox);
		
		// 字體顏色
		colorComboBox = new JComboBox<String>();
		colorComboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] {"--字體顏色--", "紅", "藍", "黑"}));
		topPanel.add(colorComboBox);
		
		// 字體樣式
		styleComboBox = new JComboBox<String>();
		styleComboBox.setModel(
				new DefaultComboBoxModel<String>(new String[] {"--字體樣式--", "粗體", "下底線", "斜體"}));
		topPanel.add(styleComboBox);
		
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
		fontComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				setTextAreaFont();
			}
		});
		
		// 字體大小
		sizeComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				setTextAreaFontSize();
			}
		});
		
		// 字體顏色
		colorComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				setTextAreaFontColor();
			}
		});
		
		// 字體樣式
		styleComboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				setTextAreaFontStyle();
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
		Action setFont = null;
		if (tabbedPane.getTabCount() > 0) {
			if (font == "蘋方") {
				setFont = new StyledEditorKit.FontFamilyAction(font, "PingFang");
			} else if (font == "黑體") {
				setFont = new StyledEditorKit.FontFamilyAction(font, "STHeiti");
			} else if (font == "楷體") {
				setFont = new StyledEditorKit.FontFamilyAction(font, "STKaiti");
			} else if (font == "儷黑 Pro") {
				setFont = new StyledEditorKit.FontFamilyAction(font, "LiHei Pro");
			} else if (font == "儷宋 Pro") {
				setFont = new StyledEditorKit.FontFamilyAction(font, "LiSong Pro");
			} else {
				setFont = new StyledEditorKit.FontFamilyAction(font, font);
			}
			fontComboBox.setAction(setFont);
		}
	}
	
	// 設定字體大小
	private void setTextAreaFontSize() {
		if (tabbedPane.getTabCount() > 0) {
			String fontSize = sizeComboBox.getSelectedItem().toString();
			Action setFontSize = new StyledEditorKit.FontSizeAction(fontSize, Integer.parseInt(fontSize));
			sizeComboBox.setAction(setFontSize);
		}
	}
	
	// 設定字體顏色
	private void setTextAreaFontColor() {
		if (tabbedPane.getTabCount() > 0) {
			String fontColor = colorComboBox.getSelectedItem().toString();
			Action setFontColor = null;
			if (fontColor == "紅") {
				setFontColor = new StyledEditorKit.ForegroundAction(fontColor, Color.red);
			} else if (fontColor == "藍") {
				setFontColor = new StyledEditorKit.ForegroundAction(fontColor, Color.blue);
			} else if (fontColor == "黑") {
				setFontColor = new StyledEditorKit.ForegroundAction(fontColor, Color.black);
			}
			colorComboBox.setAction(setFontColor);
		}
	}
	
	// 設定字體樣式
	private void setTextAreaFontStyle() {
		if (tabbedPane.getTabCount() > 0) {
			String fontstyle = styleComboBox.getSelectedItem().toString();
			Action setFontStyle = null;
			if (fontstyle == "粗體") {
				setFontStyle = new StyledEditorKit.BoldAction();
				setFontStyle.putValue(Action.NAME, "Bold");
			} else if (fontstyle == "下底線") {
				setFontStyle = new StyledEditorKit.UnderlineAction();
				setFontStyle.putValue(Action.NAME, "Underline");
			} else if (fontstyle == "斜體") {
				setFontStyle = new StyledEditorKit.ItalicAction();
				setFontStyle.putValue(Action.NAME, "Italic");
			}
			styleComboBox.setAction(setFontStyle);
		}
	}
	
	// 新增頁籤
	private void addSheet() {
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
		DefaultMutableTreeNode path = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) path.getParent();
		if (parent != null) {
			int selectedIndex = parent.getIndex(path);
			String tabName = tree.getNodeName(selectedIndex);
			tabbedPane.loadTabText(this.UserAccount, tabName);
		}
	}
}


