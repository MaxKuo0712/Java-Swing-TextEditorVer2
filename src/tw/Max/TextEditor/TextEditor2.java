package tw.Max.TextEditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import tw.Max.Class.Login;
import tw.Max.Class.TabbedPane;

public class TextEditor2 extends JFrame {
	private JMenuBar menuBar;
	private JButton addSheet, save, newSave, load, delSheet;
	private JComboBox<String> colorComboBox, fontComboBox, sizeComboBox;
	private JPanel topPanel, mainPanel, textPanel;
	private TabbedPane tabbedPane;
	
	public TextEditor2() {
		// 定義視窗
		super("Text Editor");
		setLayout(new BorderLayout());
		
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
		tabbedPane = new TabbedPane();
		textPanel.add(tabbedPane, BorderLayout.CENTER);
		
		// 新增頁籤
		addSheet = new JButton("New");
		menuBar.add(addSheet);
		
		// 刪除頁籤
		delSheet = new JButton("Delete");
		menuBar.add(delSheet);

		// 儲存
		save = new JButton("Save");
		menuBar.add(save);
		
		// 另存新檔
		newSave = new JButton("Save As");
		menuBar.add(newSave);
		
		// 開啟舊檔
		load = new JButton("Load");
		menuBar.add(load);
		
		setSize(640, 480);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setListener();
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
		
		// 刪除頁籤
//		delSheet.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				delSheet();
//			}
//		});
		
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
	
		// 另存新檔
		newSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 沒有頁籤的時候，不能執行儲存
				if(tabbedPane.getTabSize() > 0) {
					newSave();
				}
			}
		});
		
		// 讀取
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				load();
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
		tabbedPane.addNewTabs();
	}
	
	// 刪除頁籤
//	private void delSheet() {
//		tabbedPane.delSheet();
//	}
	
	// 儲存
	private void save() {
		tabbedPane.saveTextPane();
	}
	
	// 另存新檔
	private void newSave() {
		tabbedPane.exportFile();
	}

	// 讀取
	private void load() {
		tabbedPane.load();
	}
	
	// 程式進入點
	public static void main(String[] args) {
		new TextEditor2();
	}
}
