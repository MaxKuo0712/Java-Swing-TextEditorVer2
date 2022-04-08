package tw.Max.Class;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

public class TabbedPane extends JTabbedPane{
	private JTextPane textPane;
	private String DB = "MiddleProject";
	private String Account = "root"; // 取得輸入的帳號
	private String Password = ""; // 取得輸入的密碼
	private String userAccount;
	private LinkedList<String> tabList;
	private HashMap<String, JTextPane> currentTabTextPaneMap;
	
	// 建構式
	public TabbedPane(String UserAccount) {
		this.userAccount = UserAccount;
		currentTabTextPaneMap = new HashMap<>();
		tabList = new LinkedList<>();
		initTabList();
		setListener(); // Listener
		
		// 視窗頁籤
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
	
	private void setListener() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTabbedPane tabbedPane = (JTabbedPane)e.getComponent();
				int removeTabIndex = getUI().tabForCoordinate(tabbedPane, e.getX(), e.getY()); // 取得UI上點擊到的對象index
			    if (removeTabIndex < 0) return;  // 如果沒點到東西 就return
			    
			    Color tabColor = getBackgroundAt(removeTabIndex); // 取得頁籤顏色 用來判斷是否還沒儲存
			    int tabStatus = 0; // tab儲存的狀態
			    
			    // 如果頁籤顏色是黃色 表示還沒儲存 就要問User同不同意直接關閉
			    if (tabColor == Color.yellow) {
			    	tabStatus = 0;
			    	if (isCloseTab(tabStatus)) {
			    		closeTab(removeTabIndex, e); // 關閉頁籤
			    	}
			    } else {
			    	tabStatus = 1;
					// 當有頁籤存在及User同意關閉才會執行
					if (getTabCount() > 0 && isCloseTab(tabStatus) == true) {
						closeTab(removeTabIndex, e); // 關閉頁籤
					}
			    }
			}
		});
	}
	
	// 初始化TabList用來判斷頁籤是否重複
	private void initTabList() {
		SQLQuery sqlQuery = new SQLQuery(this.DB, this.Account, this.Password);
		LinkedList<String> tabs = sqlQuery.guerySqlShowTabs(this.userAccount);
		
		for ( int i = 0; i < tabs.size(); i++ ) {
			tabList.push(tabs.get(i));
		}
	}
	
	// 關閉頁籤
	private void closeTab(int removeTabIndex, MouseEvent e) {
	    Rectangle rect = ((CloseTabIcon)getIconAt(removeTabIndex)).getBounds(); // 取得點擊icon的那個頁籤
	    if (rect.contains(e.getX(), e.getY())) {
	      this.removeTabAt(removeTabIndex); // 關閉頁籤
	    }
	}

	// 詢問User是否真的要關閉
	private boolean isCloseTab(int tabStatus) {
		int isClose = 0;
		if (tabStatus == 0) {
			isClose = JOptionPane.showConfirmDialog(null, "尚未儲存，確定關閉該頁籤？", "關閉頁籤", JOptionPane.YES_NO_OPTION);
		} else if (tabStatus == 1) {
			isClose = JOptionPane.showConfirmDialog(null, "確定關閉該頁籤？", "關閉頁籤", JOptionPane.YES_NO_OPTION);
		}
		if (isClose == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// 新增頁籤
	public void addNewTabs() {
		String tabName = JOptionPane.showInputDialog("請輸入檔案名稱：");
		tabName = setTabName(tabName); // 先檢查要新增的檔案名稱

		if (tabName != null) {
			textPane = addNewTextPane(tabName);
			currentTabTextPaneMap.put(tabName, textPane);
			addTab(tabName, new CloseTabIcon(null), new JScrollPane(textPane)); // 新增頁籤
			setSelectedIndex(getTabCount() - 1);  // 新增後, 選擇新增的tab
			setTextPaneListener(textPane);
		}
	}
	
	// 點擊TreeNode時載入頁籤
	public void loadTabText(String Account, String tabName) {
		SQLQuery sqlquery = new SQLQuery(this.DB, this.Account, this.Password);
		JTextPane tabText = sqlquery.guerySqlTabsText(Account, tabName); // sql query 出指定的檔案
		addTabs(tabName, tabText); // add 進去頁籤
	}
	
	// 點擊TreeNode時載入頁籤
	public void addTabs(String tabName, JTextPane textPane) {
		currentTabTextPaneMap.put(tabName, textPane);
		addTab(textPane.getName(), new CloseTabIcon(null), new JScrollPane(textPane)); // 新增頁籤
		setSelectedIndex(getTabCount() - 1);  // 新增後, 選擇新增的tab
		setTextPaneListener(textPane);
	}

	private JTextPane addNewTextPane(String tabName) {
		textPane = new JTextPane();
		textPane.setName(tabName); // 給予TextPane名字 以便後面使用
		return textPane;
	}
	
	private void setTextPaneListener(JTextPane textPane) {
		textPane.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				setBackgroundAt(getSelectedIndex(), Color.yellow);
			}
		});
	}
	
	// 頁籤名稱設置，如果輸入為空, null, 已經存在，則不能建立
	private String setTabName(String tabName) {
		Boolean isNamExists = checkNewName(tabName);
		
		if (isNamExists) {
			JOptionPane.showMessageDialog(null, "檔案名稱重複！");
			return null;
		} else {
			if (tabName.equals("")) {
				return "untitled";
			} else {
				return tabName;
			}
		}
	}
	
	// 檢查新頁簽名稱狀況
	private Boolean checkNewName(String tabName) {
		int listIndex = tabList.indexOf(tabName);
		
		if (listIndex >= 0) {
			return true;
		} else {
			return false;
		}
	}
	
	// 匯出檔案
	public void exportFile() {
		
		String outputName = getTextPaneName(); // 取得頁籤名稱
		JTextPane outputTextPane = getTextPane(outputName);
		
		System.out.println(outputName);
		//彈出檔案選擇框
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("匯出檔案");
		chooser.setSelectedFile(new File(outputName)); // 預設檔名是頁籤名稱

		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {	//假如使用者選擇了儲存
			File file = chooser.getSelectedFile(); // 取得路徑
			
			try {
				ObjectOutputStream oout = 
						new ObjectOutputStream(
							new FileOutputStream(file));
					oout.writeObject(outputTextPane);
					oout.flush();
					oout.close();
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
			if (sqlUpdate.updateTabText(Account, TabName, currentTabTextPaneMap.get(TabName))) {
				setBackgroundAt(getSelectedIndex(), null); // 存檔成功 顏色回歸正常
				JOptionPane.showMessageDialog(null, "儲存成功");
				return 1; // update 不用新增Tree Node
			} else {
				JOptionPane.showMessageDialog(null, "儲存失敗");
				return 0; // insert失敗
			}
		} else {
			// insert sql
			if (sqlInsert.insertTabText(Account, TabName, currentTabTextPaneMap.get(TabName))) {
				setBackgroundAt(getSelectedIndex(), null); // 存檔成功 顏色回歸正常
				JOptionPane.showMessageDialog(null, "儲存成功");
				return 2; // insert 要新增Tree Node
			} else {
				JOptionPane.showMessageDialog(null, "儲存失敗");
				return 0; // insert失敗
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
				String tabName = chooser.getName(file);
				ObjectInputStream oin = 
						new ObjectInputStream(
							new FileInputStream(file));
				Object obj = oin.readObject(); // 讀入 記憶體內佔一塊
				oin.close();
				addTabs(tabName, (JTextPane) obj);
				JOptionPane.showMessageDialog(null, "讀取成功");
			} catch (Exception e) {
				System.err.println(e.toString()); // 印出出錯訊息
				e.printStackTrace(); // 印出出錯位置
			}	
		}
	}
	
	// 設定TextPane背景顏色
	public void setBackgroundColor() {
		if (getTabCount() > 0) {
			String tabName = getTextPaneName();
			Color color = JColorChooser.showDialog(new JFrame("設定背景顏色"), "選取顏色", null); // 呼叫內建JColorChooser
			getTextPane(tabName).setBackground(color); // 設定TextPane背景顏色
		}
	}
	
	// 插入圖片
	public void addPic() {
		//彈出檔案選擇框
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("插入圖片");

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {	//假如使用者選擇了儲存
			try {
				String tabName = getTextPaneName();
				String path = chooser.getSelectedFile().getPath(); // 取得路徑
				BufferedImage inputPic = ImageIO.read(new File(path)); // 取得圖片
				ImageIcon a = new ImageIcon(inputPic); 
				getTextPane(tabName).insertIcon(a); // textPane內放入圖片
			} catch (Exception e) {
				System.err.println(e.toString()); // 印出出錯訊息
				e.printStackTrace(); // 印出出錯位置
			}	
		}
	}

	// 取的TextArea的名字
	public String getTextPaneName() {
		return getTitleAt(getSelectedIndex());
	}
	
	// 取得JTextPane
	private JTextPane getTextPane(String tabName) {
		return currentTabTextPaneMap.get(tabName);
	}
	
	// 取的linkedlist的大小
	public int getTabSize() {
		return currentTabTextPaneMap.size();
	}
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
