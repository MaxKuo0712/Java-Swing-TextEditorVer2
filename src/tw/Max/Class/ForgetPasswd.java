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
import javax.swing.JTextField;

public class ForgetPasswd extends JFrame {
	private JPanel bodyPanel, footerPanel, userAccountPanel, userMailPanel;
	private JLabel userAccountLabel, userMailLabel;
	private JTextField userAccount, userMail;
	private JButton submitButton, cancelButton;
	private String DB = "MiddleProject";
	private String Account = "root"; // 取得輸入的帳號
	private String Password = ""; // 取得輸入的密碼
	
	public ForgetPasswd() {
		// 建立視窗
		super("忘記密碼");
		setSize(210,170);
		setResizable(false); // 視窗不能拉伸大小
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		
		// body
		bodyPanel = new JPanel();
		bodyPanel.setLayout(new GridLayout(2,0));
		bodyPanel.setBorder(BorderFactory.createEtchedBorder());
		add(bodyPanel, BorderLayout.CENTER);
		
		// userAccount
		userAccountPanel = new JPanel();
		bodyPanel.add(userAccountPanel);
		
		userAccountLabel = new JLabel("帳號");
		userAccountPanel.add(userAccountLabel);
		
		userAccount = new JTextField();
		userAccount.setColumns(10);
		userAccountPanel.add(userAccount);
		
		//userMail
		userMailPanel = new JPanel();
		bodyPanel.add(userMailPanel);
		
		userMailLabel = new JLabel("信箱");
		userMailPanel.add(userMailLabel);
		
		userMail = new JTextField();
		userMail.setColumns(10);
		userMailPanel.add(userMail);
		
		//footer
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
		// 發送mail
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				infoUser();
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
	
	private void infoUser() {
		String account = getUserAccount();
		String mail = getUserMail();
		Boolean checkMail = checkMail(account, mail);
		
		if (checkMail) {
			String randomPasswd = getRandomPasswd(); // 產生亂數密碼
			SendEmail sendEmail = new SendEmail(mail, randomPasswd);
			sendEmail.start();
			setNewRandomPasswd(account, randomPasswd);
			dispose();
			new ChangePasswd();
		}
	}
	
	private void setNewRandomPasswd(String account, String randomPasswd) {
		SQLUpdate sqlUpdate = new SQLUpdate(this.DB, this.Account, this.Password);
		String newPasswd = BCrypt.hashpw(randomPasswd, BCrypt.gensalt());
		sqlUpdate.updatePasswd(account, newPasswd);
	}
	
	private Boolean checkMail(String account, String mail) {
		SQLQuery sqlQuery = new SQLQuery(this.DB, this.Account, this.Password);
		Boolean checkMail = sqlQuery.queryUserMail(account, mail);
		return checkMail;
	}
	
	private String getRandomPasswd() {
		//產生亂數密碼
		int[] word = new int[8];
        StringBuffer newPassword = new StringBuffer();
		int mod;
		
        for( int i = 0; i < 8; i++ ) {
        	mod = (int)((Math.random() * 7) % 3);
            if(mod == 1) {    //數字
                   word[i] = (int)((Math.random() * 10) + 48);
            } else if(mod == 2) {  //大寫英文
                   word[i] = (char)((Math.random() * 26) + 65);
            } else {    //小寫英文
                   word[i] = (char)((Math.random() * 26) + 97);
            }
         }

        for( int j = 0; j < 8; j++ ){
        	newPassword.append((char)word[j]);
        }
        return newPassword.toString();
	}
	
	public String getUserAccount() {
		return userAccount.getText();
	}
	
	public String getUserMail() {
		return userMail.getText();
	}
}
