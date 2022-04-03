package tw.Max.Class;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import tw.Max.TextEditor.TextEditor;

public class Login extends JFrame {
	private JTextField userAccount;
	private JPasswordField userPassword;
	private JButton loginButton, registerButton;
	private JLabel userAccountLabel, userPasswordLabel;
	private JPanel body, footer;
	private SQLQuery sqlQuery;
	private String DB;
	private String Account; // 取得輸入的帳號
	private String Password; // 取得輸入的密碼
	
	public Login() {
		// 建立視窗
		super("登入畫面");
		setSize(190,150);
		setResizable(false); // 視窗不能拉伸大小
		setLayout(new BorderLayout());
		
		// body
		body = new JPanel();
		body.setLayout(new FlowLayout());
		add(body, BorderLayout.CENTER);
		
		// body - userName
		userAccountLabel = new JLabel("帳號");
		userAccount = new JTextField();
		userAccount.setColumns(10);
		body.add(userAccountLabel);
		body.add(userAccount);
		
		// body - userPassword
		userPasswordLabel = new JLabel("密碼");
		userPassword = new JPasswordField();
		userPassword.setColumns(10);
		body.add(userPasswordLabel);
		body.add(userPassword);
		
		// footer
		footer = new JPanel();
		footer.setLayout(new FlowLayout());
		add(footer, BorderLayout.SOUTH);

		// footer - login
		loginButton = new JButton("登入");
		footer.add(loginButton);
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkLogin()) {
					new TextEditor(); // 開啟主要程式
					dispose(); // 登入成功進入主要程式後關閉
				}
			}
		});
		
		// footer - register
		registerButton = new JButton("註冊");
		footer.add(registerButton);
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Register();
			}
		});
		
		DB = "MiddleProject";
		Account = "root"; // 取得輸入的帳號
		Password = ""; // 取得輸入的密碼
		sqlQuery = new SQLQuery(DB, Account, Password);
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private Boolean checkLogin() {
		String Account = getUserAccount();
		String Password = getUserPassword();
		int checkResult = sqlQuery.getSqlLoginResult(Account, Password);
		
		if (checkResult == 0) {
			JOptionPane.showMessageDialog(null, "帳號不存在");
			return false;
		} else if (checkResult == 1) {
			JOptionPane.showMessageDialog(null, "登入成功");
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
	
	private String getUserAccount() {
		return userAccount.getText();
	}
	
	private String getUserPassword() {
		return String.valueOf(userPassword.getPassword());
	}

	public static void main(String[] args) {
		new Login();
	}

}






