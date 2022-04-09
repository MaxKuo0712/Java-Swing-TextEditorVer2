package tw.Max.Class;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ChangePasswd extends JFrame {
	private JPanel bodyPanel, footerPanel, userAccountPanel, oriPasswdPanel, newPasswdPanel, confirmPasswdPanel;
	private JLabel userAccountLabel, oriPasswdLabel, newPasswdLabel, confirmPasswdLabel;
	protected JPasswordField oriPasswd, newPasswd, confirmPasswd;
	private JTextField userAccount;
	private JButton submitButton, cancelButton;
	private String DB = "MiddleProject";
	private String Account = "root"; // 取得輸入的帳號
	protected String Password = ""; // 取得輸入的密碼
	
	public ChangePasswd() {
		// 建立視窗
		super("更改密碼");
		setSize(210,250);
		setResizable(false); // 視窗不能拉伸大小
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		
		// body_Panel
		bodyPanel = new JPanel();
		bodyPanel.setLayout(new GridLayout(4,0));
		bodyPanel.setBorder(BorderFactory.createEtchedBorder());
		add(bodyPanel, BorderLayout.CENTER);
		
		// body - userName
		oriPasswdPanel = new JPanel();
		bodyPanel.add(oriPasswdPanel);
		
		userAccountLabel = new JLabel("帳號");
		oriPasswdPanel.add(userAccountLabel);
		
		userAccount = new JTextField();
		userAccount.setColumns(10);
		oriPasswdPanel.add(userAccount);
		
		// oriPasswd
		userAccountPanel = new JPanel();
		bodyPanel.add(userAccountPanel);
		
		oriPasswdLabel = new JLabel("舊密碼");
		userAccountPanel.add(oriPasswdLabel);
		
		oriPasswd = new JPasswordField();
		oriPasswd.setColumns(10);
		userAccountPanel.add(oriPasswd);
		
		// newPasswd
		newPasswdPanel = new JPanel();
		bodyPanel.add(newPasswdPanel);
		
		newPasswdLabel = new JLabel("新密碼");
		newPasswdPanel.add(newPasswdLabel);
		
		newPasswd = new JPasswordField();
		newPasswd.setColumns(10);
		newPasswdPanel.add(newPasswd);
		
		// oriPasswdPanel
		confirmPasswdPanel = new JPanel();
		bodyPanel.add(confirmPasswdPanel);
		
		confirmPasswdLabel = new JLabel("確認密碼");
		confirmPasswdPanel.add(confirmPasswdLabel);
		
		confirmPasswd = new JPasswordField();
		confirmPasswd.setColumns(10);
		confirmPasswdPanel.add(confirmPasswd);
		
		// footer_Panel
		footerPanel = new JPanel();
		add(footerPanel, BorderLayout.SOUTH);
		
		// footer_submit
		submitButton = new JButton("確定");
		footerPanel.add(submitButton);
		
		// footer_cancel
		cancelButton = new JButton("取消");
		footerPanel.add(cancelButton);
		
		// Listener
		setListener();
		
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	private void setListener() {
		// 更改密碼
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Boolean checkInputNewPasswd = checkNewPasswd();
				Boolean checkOriAccout = checkOriPasswd();
				
				// 兩組新密碼必須相等、舊的帳號及密碼必須正確
				if (checkInputNewPasswd && checkOriAccout) {
					Boolean isUpdateSuccess = setNewPasswd();
					
					// sql更改密碼
					if (isUpdateSuccess) {
						JOptionPane.showMessageDialog(null, "更改成功");
						dispose(); // 完成後關閉
					} else {
						JOptionPane.showMessageDialog(null, "更改失敗");
						clear(); // 更改失敗 清空輸入框
					}
				}
			}
		});
		
		// 取消
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	// 確認舊的帳號及密碼是否正確
	private Boolean checkOriPasswd() {
		String Account = getUserAccount();
		String Password = getOriPassword();
		SQLQuery sqlQuery = new SQLQuery(this.DB, this.Account, this.Password);
		int checkResult = sqlQuery.querySqlLoginResult(Account, Password);
		
		if (checkResult == 0) {
			JOptionPane.showMessageDialog(null, "帳號不存在");
			return false;
		} else if (checkResult == 1) {
			return true;
		} else if (checkResult == 2) {
			JOptionPane.showMessageDialog(null, "密碼錯誤");
			return false;
		} else if (checkResult == 3) {
			JOptionPane.showMessageDialog(null, "錯誤");
			return false;
		} else {
			return false;
		}
	}
	
	// sql更改密碼
	private Boolean setNewPasswd() {
		String Account = getUserAccount();
		String Password = BCrypt.hashpw(getNewPassword(), BCrypt.gensalt()); // 加鹽
		SQLUpdate sqlUpdate = new SQLUpdate(this.DB, this.Account, this.Password);
		Boolean setNewPasswdResult = sqlUpdate.updatePasswd(Account, Password);
		return setNewPasswdResult; // 回傳update結果
	}
	
	// 檢查兩組密碼是不是相同 (新密碼與確認密碼)
	private Boolean checkNewPasswd() {
		String newPasswd = getNewPassword();
		String confirmPasswd = getConfirmPassword();
		
		if (newPasswd.equals(confirmPasswd)) {
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "密碼不相符");
			return false;
		}
	}
	
	// 清除
	private void clear() {
		userAccount.setText(null);
		oriPasswd.setText(null);
		newPasswd.setText(null);
		confirmPasswd.setText(null);
	}
	
	// 取得使用者帳號
	private String getUserAccount() {
		return userAccount.getText();
	}
	
	// 取得使用者密碼
	protected String getOriPassword() {
		return String.valueOf(oriPasswd.getPassword());
	}
	
	// 取得使用者輸入的新密碼
	protected String getNewPassword() {
		return String.valueOf(newPasswd.getPassword());
	}
	
	// 取得使用者輸入的新密碼確認
	protected String getConfirmPassword() {
		return String.valueOf(confirmPasswd.getPassword());
	}
}

