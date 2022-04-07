package tw.Max.Class;

import java.awt.ActiveEvent;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends JFrame {
	private JTextField userAccount;
	private JPasswordField userPassword;
	private JButton loginButton, registerButton, forgetPasswdButton, changePasswdButton;
	private JLabel userAccountLabel, userPasswordLabel;
	private JPanel body, footer;
	private String DB = "MiddleProject";
	private String Account = "root"; // 取得輸入的帳號
	private String Password = ""; // 取得輸入的密碼
	
	public Login() {
		// 建立視窗
		super("登入畫面");
		setSize(210,170);
		setResizable(false); // 視窗不能拉伸大小
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		
		// body
		body = new JPanel();
		body.setLayout(new FlowLayout());
		add(body, BorderLayout.CENTER);
		
		// body - userName
		userAccountLabel = new JLabel("帳號");
		userAccount = new JTextField();
		userAccount.setColumns(12);
		body.add(userAccountLabel);
		body.add(userAccount);
		
		// body - userPassword
		userPasswordLabel = new JLabel("密碼");
		userPassword = new JPasswordField();
		userPassword.setColumns(12);
		body.add(userPasswordLabel);
		body.add(userPassword);
		
		// footer
		footer = new JPanel();
		footer.setLayout(new FlowLayout());
		add(footer, BorderLayout.SOUTH);

		// footer - login
		loginButton = new JButton("登入");
		body.add(loginButton);

		// footer - register
		registerButton = new JButton("註冊");
		body.add(registerButton);
		
		// footer - forgetPasswd
		forgetPasswdButton = new JButton("忘記密碼");
		body.add(forgetPasswdButton);
		
		// footer - changePasswd
		changePasswdButton = new JButton("更改密碼");
		body.add(changePasswdButton);
		
		// Listener
		setListener();
		
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void setListener() {
		// 登入
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkLogin()) {
					new TextEditor(getUserAccount()); // 開啟主要程式
					dispose(); // 登入成功進入主要程式後關閉
				}
			}
		});
		
		// 註冊
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Register();
			}
		});
		
		// 更改密碼
		changePasswdButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ChangePasswd();
			}
		});
	}
	
	private Boolean checkLogin() {
		String Account = getUserAccount();
		String Password = getUserPassword();
		SQLQuery sqlQuery = new SQLQuery(this.DB, this.Account, this.Password);
		int checkResult = sqlQuery.querySqlLoginResult(Account, Password);
		
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
	
	public String getUserAccount() {
		return userAccount.getText();
	}
	
	private String getUserPassword() {
		return String.valueOf(userPassword.getPassword());
	}

	public static void main(String[] args) {
		new Login();
	}
}






