package tw.Max.Class;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class TabbedPane extends JTabbedPane implements MouseListener{
	private JTextPane textPane;
	private String UserAccount;
	private String DB = "MiddleProject";
	private String Account = "root"; // 取得輸入的帳號
	private String Password = ""; // 取得輸入的密碼
	private HashMap<String, String> tabNameMap;
	private LinkedList<JTextPane> tabList;
	
	// 建構式
	public TabbedPane(String UserAccount) {
		// setUserAccount
		setUserAccount(UserAccount);
		tabNameMap = new HashMap<>(); // 存頁籤名稱及路徑 Key：頁籤名稱 Value：儲存路徑
		tabList = new LinkedList<>(); // 存下JTextPane
		addMouseListener(this);
		
		// 視窗頁籤
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
	public void mouseClicked(MouseEvent e) {
		int removeTabIndex = getUI().tabForCoordinate(this, e.getX(), e.getY());
	    if (removeTabIndex < 0) return;
	    
	    Color tabColor = getBackgroundAt(removeTabIndex);
	    
	    if (tabColor == Color.yellow) {
	    	if (isSaveDB()) {
	    		closeTab(removeTabIndex, e); // 關閉頁籤
	    	}
	    } else {
			// 當有頁籤存在及User同意關閉才會執行
			if (getTabCount() > 0 && isDeleteSheet() == true) {
				closeTab(removeTabIndex, e);
			}
	    }
    }
	
	// 關閉頁籤
	private void closeTab(int removeTabIndex, MouseEvent e) {
	    Rectangle rect=((CloseTabIcon)getIconAt(removeTabIndex)).getBounds();
	    if (rect.contains(e.getX(), e.getY())) {
	      this.removeTabAt(removeTabIndex); // 關閉頁籤
	    }
	}
	
	// 關閉前檢查是否已經儲存
	private Boolean isSaveDB() {
		int isClose = JOptionPane.showConfirmDialog(null, "尚未儲存，確定關閉該頁籤？", "關閉頁籤", JOptionPane.YES_NO_OPTION);
		if (isClose == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// 詢問User是否真的要關閉
	private boolean isDeleteSheet() {
		int isClose = JOptionPane.showConfirmDialog(null, "確定要	關閉該頁籤？", "關閉頁籤", JOptionPane.YES_NO_OPTION);
		if (isClose == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	private void setUserAccount(String UserAccount) {
		this.UserAccount = UserAccount;
	}
	
	// 新增頁籤
	public Boolean addNewTabs() {
		String sheetName = JOptionPane.showInputDialog("請輸入檔案名稱：");
		sheetName = setTabName(sheetName); // 先檢查要新增的檔案名稱
		if (!(sheetName == null)) {
			textPane = new JTextPane();
			textPane.setName(sheetName); // 給予TextPane名字 以便後面使用
			tabList.add(textPane); // 存下JTextPane
			tabNameMap.put(sheetName, ""); // 存頁籤名稱及路徑 Key：頁籤名稱 Value：儲存路徑
			addTab(sheetName, new CloseTabIcon(null), new JScrollPane(textPane)); // 新增頁籤
			setSelectedIndex(getTabCount() - 1);  // 新增後, 選擇新增的tab
			textPane.addCaretListener(new CaretListener() {
				@Override
				public void caretUpdate(CaretEvent e) {
					setBackgroundAt(getSelectedIndex(), Color.yellow);
				}
			});
			return true;
		} else {
			return false;
		}	
	}
	
	// 點擊TreeNode時載入頁籤
	public void loadTabText(String Account, String tabName) {
		SQLQuery sqlquery = new SQLQuery(this.DB, this.Account, this.Password);
		JTextPane tabText = sqlquery.guerySqlTabsText(Account, tabName); // sql query 出指定的檔案
		addDBTabs(tabText); // add 進去頁籤
	}
	
	// 點擊TreeNode時載入頁籤
	public void addDBTabs(JTextPane tabText) {
		tabList.add(tabText); // 存下JTextPane
		tabNameMap.put(tabText.getName(), null); // 存頁籤名稱及路徑 Key：頁籤名稱 Value：儲存路徑
		addTab(tabText.getName(), new CloseTabIcon(null), new JScrollPane(tabText)); // 新增頁籤
		setSelectedIndex(getTabCount() - 1);  // 新增後, 選擇新增的tab
		tabText.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				setBackgroundAt(getSelectedIndex(), Color.yellow);
			}
		});
	}
	
	// 載入頁籤時要新增頁籤及內容
	public void addTabs(String tabName, byte[] text, File fileRoute) {
		textPane = new JTextPane();
		textPane.setText(new String(text)); // 放入檔案內容
		textPane.setName(tabName); // 給予TextPane名字 以便後面使用
		tabList.add(textPane); // 存下JTextPane
		tabNameMap.put(tabName, fileRoute.toString()); // 存頁籤名稱及路徑 Key：頁籤名稱 Value：儲存路徑
		addTab(tabName, new JScrollPane(textPane)); // 新增頁籤
		setSelectedIndex(getTabCount() - 1);  // 新增後, 選擇新增的tab
		textPane.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				setBackgroundAt(getSelectedIndex(), Color.yellow);
			}
		});
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
	
	// 匯出檔案
	public void exportFile() {
		String outputName = getTextPaneName(); // 取得頁籤名稱
		String outputText = getTextPaneText(); // 取得頁籤內容
		byte[] outputByte = outputText.getBytes(); // 字串轉為byte

		//彈出檔案選擇框
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("匯出檔案");
		chooser.setSelectedFile(new File(outputName)); // 預設檔名是頁籤名稱

		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {	//假如使用者選擇了儲存
			File file = chooser.getSelectedFile(); // 取得路徑
			setFileRoute(outputName, file.toString()); // 把路徑放入HashMap
			
			try {
				FileOutputStream fos = new FileOutputStream(file.toString().concat(".txt")); // 串流 - 設定存文字檔
				fos.write(outputByte); // 序列化 寫入
				fos.flush();
				fos.close();
				JOptionPane.showMessageDialog(null, "匯出成功");
			} catch (Exception e) {
				System.err.println(e.toString()); // 印出出錯訊息
				e.printStackTrace(); // 印出出錯位置
			}	
		}
	}
	
	// 儲存Tab物件
	public int saveTabs(String Account) {
		String TabName = getTextPaneName();
		SQLQuery sqlQuery = new SQLQuery(this.DB, this.Account, this.Password);
		SQLInsert sqlInsert = new SQLInsert(this.DB, this.Account, this.Password);
		SQLUpdate sqlUpdate = new SQLUpdate(this.DB, this.Account, this.Password);
		
		Boolean isExist = sqlQuery.guerySqlTabsExistResult(Account, TabName);
		
		if (isExist) {
			// update sql
			if (sqlUpdate.updateTabText(Account, TabName, tabList.get(getSelectedIndex()))) {
				JOptionPane.showMessageDialog(null, "儲存成功");
				setBackgroundAt(getSelectedIndex(), null); // 存檔成功 顏色回歸正常
				return 1; // update 不用新增Tree Node
			} else {
				JOptionPane.showMessageDialog(null, "儲存失敗");
				return 0; // insert失敗 要新增Tree Node
			}
		} else {
			// insert sql
			if (sqlInsert.insertTabText(Account, TabName, tabList.get(getSelectedIndex()))) {
				JOptionPane.showMessageDialog(null, "儲存成功");
				setBackgroundAt(getSelectedIndex(), null); // 存檔成功 顏色回歸正常
				return 2; // insert 要新增Tree Node
			} else {
				JOptionPane.showMessageDialog(null, "儲存失敗");
				return 0; // insert失敗 要新增Tree Node
			}
		}
	}	
	
	// 匯入檔案
	public void load() {
		//彈出檔案選擇框
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("匯入檔案");

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

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

class TabsText extends JTextPane {
	public int textSaveStatus;
}

class CloseTabIcon implements Icon {
	  private int x_pos;
	  private int y_pos;
	  private int width;
	  private int height;
	  private Icon fileIcon;

	  public CloseTabIcon(Icon fileIcon) {
	    this.fileIcon=fileIcon;
	    width=16;
	    height=16;
	  }

	  public void paintIcon(Component c, Graphics g, int x, int y) {
	    this.x_pos=x;
	    this.y_pos=y;

	    Color col=g.getColor();

	    g.setColor(Color.black);
	    int y_p=y+2;
	    g.drawLine(x+1, y_p, x+12, y_p);
	    g.drawLine(x+1, y_p+13, x+12, y_p+13);
	    g.drawLine(x, y_p+1, x, y_p+12);
	    g.drawLine(x+13, y_p+1, x+13, y_p+12);
	    g.drawLine(x+3, y_p+3, x+10, y_p+10);
	    g.drawLine(x+3, y_p+4, x+9, y_p+10);
	    g.drawLine(x+4, y_p+3, x+10, y_p+9);
	    g.drawLine(x+10, y_p+3, x+3, y_p+10);
	    g.drawLine(x+10, y_p+4, x+4, y_p+10);
	    g.drawLine(x+9, y_p+3, x+3, y_p+9);
	    g.setColor(col);
	    if (fileIcon != null) {
	      fileIcon.paintIcon(c, g, x+width, y_p);
	    }
	  }

	  public int getIconWidth() {
	    return width + (fileIcon != null? fileIcon.getIconWidth() : 0);
	  }

	  public int getIconHeight() {
	    return height;
	  }

	  public Rectangle getBounds() {
	    return new Rectangle(x_pos, y_pos, width, height);
	  }
	}
