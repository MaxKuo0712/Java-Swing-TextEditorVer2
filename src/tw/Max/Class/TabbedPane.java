package tw.Max.Class;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class TabbedPane extends JTabbedPane {
	private JTextPane textPane;
	private HashMap<String, String> tabNameMap;
	private LinkedList<JTextPane> tabList;
	private ImageIcon tabIcon;
	
	// 建構式
	public TabbedPane() {
		tabNameMap = new HashMap<>(); // 存頁籤名稱及路徑 Key：頁籤名稱 Value：儲存路徑
		tabList = new LinkedList<>(); // 存下JTextPane
		
		// 視窗頁籤
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
	// 刪除頁籤
	public void delSheet() {
		// 當有頁籤存在及User同意刪除才會執行
		if (getTabCount() > 0 && isDeleteSheet() == true) {
			tabNameMap.remove(getTextPaneName());
			tabList.remove(getSelectedIndex());
			remove(getSelectedIndex());
		}
	}
	
	// 詢問User是否真的要刪除
	private boolean isDeleteSheet() {
		int isAgain = JOptionPane.showConfirmDialog(null, "確定要刪除該頁籤？", "刪除頁籤", JOptionPane.YES_NO_OPTION);
		if (isAgain == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// 新增頁籤
	public void addNewTabs() {
		String sheetName = JOptionPane.showInputDialog("請輸入檔案名稱：");
		sheetName = setTabName(sheetName); // 先檢查要新增的檔案名稱
		if (!(sheetName == null)) {
			textPane = new JTextPane();
			textPane.setName(sheetName); // 給予TextPane名字 以便後面使用
			tabList.add(textPane); // 存下JTextPane
			tabNameMap.put(sheetName, ""); // 存頁籤名稱及路徑 Key：頁籤名稱 Value：儲存路徑

			try {
				BufferedImage imgURL = ImageIO.read(new File("img/xx.png"));
				tabIcon = new ImageIcon(imgURL);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			addTab(sheetName, tabIcon, new JScrollPane(textPane)); // 新增頁籤
		}
		setSelectedIndex(getTabCount() - 1);  // 新增後, 選擇新增的tab
	}
	
	// 載入頁籤時要新增頁籤及內容
	public void addTabs(String tabName, byte[] text, File fileRoute) {
		textPane = new JTextPane();
		textPane.setText(new String(text)); // 放入檔案內容
		textPane.setName(tabName); // 給予TextPane名字 以便後面使用
		tabList.add(textPane); // 存下JTextPane
		tabNameMap.put(tabName, fileRoute.toString()); // 存頁籤名稱及路徑 Key：頁籤名稱 Value：儲存路徑
		addTab(tabName, new JScrollPane(textPane)); // 新增頁籤
	}
	
	// 頁籤名稱設置，如果輸入為空, null, 已經存在，則不能建立
	private String setTabName(String tabName) {
		int isNamExists = checkNewName(tabName);
		if (isNamExists == 1) {
			if (tabName.equals("")) {
				return "untitled";
			} else {
				return tabName;
			}
		} else if (isNamExists == 2) {
			JOptionPane.showMessageDialog(null, "檔案名稱重複！");
			return null;
		} else if (isNamExists == 0) {
			return null;
		} else {
			return null;
		}
	}
	
	// 檢查新頁簽名稱狀況
	private int checkNewName(String tabName) {
		if (tabName == null) {
			return 0;
		} else if (tabNameMap.get(tabName) == null) {
			return 1;
		} else {
			return 2;
		}
	}
	
	// 取得文字內容
	private String getTextPaneText() {
		return tabList.get(getSelectedIndex()).getText();
	}
	
	// 取的TextArea的名字
	public String getTextPaneName() {
		return tabList.get(getSelectedIndex()).getName();
	}
	
	// 取得JTextPane
	private JTextPane getTextPane() {
		return tabList.get(getSelectedIndex());
	}
	
	// 取的linkedlist的大小
	public int getTabSize() {
		return tabList.size();
	}
	
	// 設置檔案路徑
	private void setFileRoute(String outputName, String file) {
		tabNameMap.replace(outputName, file);
	}
	
	// 取得檔案路徑
	private String getFileRoute(String outputName) {
		return tabNameMap.get(outputName);
	}
	
	// 另存新檔
	public void newSave() {
		String outputName = getTextPaneName(); // 取得頁籤名稱
		String outputText = getTextPaneText(); // 取得頁籤內容
		byte[] outputByte = outputText.getBytes(); // 字串轉為byte

		//彈出檔案選擇框
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("另存新檔");
		chooser.setSelectedFile(new File(outputName)); // 預設檔名是頁籤名稱

		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {	//假如使用者選擇了儲存
			File file = chooser.getSelectedFile(); // 取得路徑
			setFileRoute(outputName, file.toString()); // 把路徑放入HashMap
			
			try {
				FileOutputStream fos = new FileOutputStream(file.toString().concat(".txt")); // 串流 - 設定存文字檔
				fos.write(outputByte); // 序列化 寫入
				fos.flush();
				fos.close();
				JOptionPane.showMessageDialog(null, "儲存成功");
			} catch (Exception e) {
				System.err.println(e.toString()); // 印出出錯訊息
				e.printStackTrace(); // 印出出錯位置
			}	
		}
	}
	
	// 儲存檔案
	public void saveTextPane() {
		String outputName = getTextPaneName(); // 取得頁籤名稱
		String outputText = getTextPaneText(); // 取得頁籤內容
		String fileRoute = getFileRoute(outputName); // 取得該頁籤的路徑
		byte[] outputByte = outputText.getBytes(); // 字串轉為byte
		
		// 如果沒有儲存過，就沒有路徑，那就去找newSave另存新檔
		if (fileRoute == "") {
			newSave();
		} else {
			try {
				FileOutputStream fos = new FileOutputStream(fileRoute.concat(".txt")); // 串流 - 設定存文字檔
				fos.write(outputByte); // 序列化 寫入
				fos.flush();
				fos.close();
				JOptionPane.showMessageDialog(null, "儲存成功");
			} catch (Exception e) {
				System.err.println(e.toString()); // 印出出錯訊息
				e.printStackTrace(); // 印出出錯位置
			}	
		}
	}

	// 讀取檔案
	public void load() {
		//彈出檔案選擇框
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("開啟舊檔");

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {	//假如使用者選擇了儲存
			try {
				File file = chooser.getSelectedFile(); // 取得路徑
				String fileName = chooser.getName(file);
				fileName = fileName.substring(0, fileName.lastIndexOf(".")); // 取得去除副檔名的名稱
				
				if (tabNameMap.get(fileName) == null) {
					FileInputStream fin = new FileInputStream(file); // 串流 - 設定存文字檔
					byte[] text = new byte[ (int) file.length()];
					fin.read(text); // 序列化 寫入
					fin.close();
					addTabs(fileName, text, file);
					JOptionPane.showMessageDialog(null, "讀取成功");
				} else {
					JOptionPane.showMessageDialog(null, "檔案已存在於頁籤");
				}
			} catch (Exception e) {
				System.err.println(e.toString()); // 印出出錯訊息
				e.printStackTrace(); // 印出出錯位置
			}	
		}
	}
	
	// 設定字體，有頁籤才能執行
	public void setTextPaneFont(String item) {
		JTextPane TextPane = getTextPane();
		String textFont = null;
		if (tabList.size() > 0) {
			if (item == "蘋方") {
				textFont = "PingFang";
			} else if (item == "黑體") {
				textFont = "STHeiti";
			} else if (item == "楷體") {
				textFont = "STKaiti";
			} else if (item == "儷黑 Pro") {
				textFont = "LiHei Pro";
			} else if (item == "儷宋 Pro") {
				textFont = "LiSong Pro";
			} else {
				textFont = item;
			}
		}
		TextPane.setFont(new Font(textFont, TextPane.getFont().getStyle(), TextPane.getFont().getSize()));
	}
	
	// 設定字體大小，有頁籤才能執行
	public void setTextPaneFontSize(String item) {
		if (tabList.size() > 0) {
			JTextPane TextPane = getTextPane();
			TextPane.setFont(new Font(TextPane.getFont().getFontName(), TextPane.getFont().getStyle(), Integer.parseInt(item)));
		}
	}
	
	// 設定顏色，有頁籤才能執行
	public void setTextPaneFontColor(String item) {
		if (tabList.size() > 0) {
			JTextPane TextPane = getTextPane();
			if (item == "紅") {
				TextPane.setForeground(Color.red);
			} else if (item == "藍") {
				TextPane.setForeground(Color.blue);
			} else if (item == "黑") {
				TextPane.setForeground(Color.black);
			}
		}
	}
	
}
