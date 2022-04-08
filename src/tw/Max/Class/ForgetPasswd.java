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
	protected String Password = ""; // 取得輸入的密碼
	
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
				mailToUser(); // 發送mail通知使用者
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
	
	private void mailToUser() {
		String account = getUserAccount(); // 取得使用者帳號
		String mail = getUserMail(); // 取得使用者密碼
		Boolean checkMail = checkMail(account, mail); // 檢查帳號及信箱是否存在
		
		if (checkMail) {
			String randomPasswd = getRandomPasswd(); // 取得產生的亂數密碼
			SendEmail sendEmail = new SendEmail(mail, randomPasswd); // 寄mail class
			sendEmail.start(); // 因為寄送mail需要時間 所以用執行緒方式處理 在前端的感受上較佳
			setNewRandomPasswd(account, randomPasswd); // update新密碼進資料庫
			dispose(); // 關密忘記密碼的視窗
			new ChangePasswd(); // 替使用者開啟更改密碼的視窗
		} else {
			JOptionPane.showMessageDialog(null, "帳號或郵件不存在");
		}
	}
	
	// update新密碼進資料庫 要加鹽
	private void setNewRandomPasswd(String account, String randomPasswd) {
		SQLUpdate sqlUpdate = new SQLUpdate(this.DB, this.Account, this.Password);
		String newPasswd = BCrypt.hashpw(randomPasswd, BCrypt.gensalt());
		sqlUpdate.updatePasswd(account, newPasswd);
	}
	
	// 檢查帳號及信箱是否存在 
	private Boolean checkMail(String account, String mail) {
		SQLQuery sqlQuery = new SQLQuery(this.DB, this.Account, this.Password);
		Boolean checkMail = sqlQuery.queryUserMail(account, mail);
		return checkMail;
	}
	
	//產生亂數密碼
	protected String getRandomPasswd() {
		// 產生8碼密碼
		int[] word = new int[8];
        StringBuffer newPassword = new StringBuffer();
		int mod;
		
        for( int i = 0; i < 8; i++ ) {
        	mod = (int)((Math.random() * 7) % 3);
            if(mod == 1) {    
            	word[i] = (int)((Math.random() * 10) + 48); //數字
            } else if(mod == 2) {  
        		word[i] = (char)((Math.random() * 26) + 65); //大寫英文
            } else {   
            	word[i] = (char)((Math.random() * 26) + 97);  //小寫英文
            }
         }

        for( int j = 0; j < 8; j++ ){
        	newPassword.append((char)word[j]);
        }
        return newPassword.toString();
	}
	
	// 取得使用者帳號
	private String getUserAccount() {
		return userAccount.getText();
	}
	
	// 取得使用者mail
	private String getUserMail() {
		return userMail.getText();
	}
}
