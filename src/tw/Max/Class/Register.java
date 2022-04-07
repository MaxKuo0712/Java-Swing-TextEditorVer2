package tw.Max.Class;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdatepicker.JDatePanel;
import org.jdatepicker.JDatePicker;

public class Register extends JFrame {
	private JPanel headerPanel, bodyPanel, footerPanel;
	private JPanel namePanel, idPanel, accountPanel, passwdPanel, genderPanel, birthPanel, emailPanel, telPanel;
	private JLabel nameLabel, idLabel, accountLabel, passwdLabel, genderLabel, birthLabel, emailLabel, telLabel;
	private JTextField nameField, idField, accountField, emailField, telField;
	private JComboBox<String> genderComboBox;
	protected JPasswordField passwdField;
	private JButton submitButton, cancelButton;
	private JDatePicker birthDatePicker; 
	private String gender[] = {"男", "女"};
	private String DB = "MiddleProject";
	private String Account = "root"; // 取得輸入的帳號
	protected String Password = ""; // 取得輸入的密碼

	public Register() {
		super("建立您的帳戶");
		setSize(300, 400);
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		
		// header_Panel
		headerPanel = new JPanel();
		add(headerPanel, BorderLayout.NORTH);

		// body_Panel
		bodyPanel = new JPanel();
		bodyPanel.setLayout(new GridLayout(8,0));
		bodyPanel.setBorder(BorderFactory.createEtchedBorder());
		add(bodyPanel, BorderLayout.CENTER);
		
		// body_name
		namePanel = new JPanel();
		bodyPanel.add(namePanel);
		
		nameLabel = new JLabel("姓名          ");
		namePanel.add(nameLabel);
		
		nameField = new JTextField();
		nameField.setColumns(10);
		namePanel.add(nameField);
		
		// body_id
		idPanel = new JPanel();
		bodyPanel.add(idPanel);
		
		idLabel = new JLabel("身分證字號");
		idPanel.add(idLabel);
		
		idField = new JTextField();
		idField.setColumns(10);
		idPanel.add(idField);
		
		// body_account
		accountPanel = new JPanel();
		bodyPanel.add(accountPanel);
		
		accountLabel = new JLabel("帳號          ");
		accountPanel.add(accountLabel);
		
		accountField = new JTextField();
		accountField.setColumns(10);
		accountPanel.add(accountField);

		// body_passwd
		passwdPanel = new JPanel();
		bodyPanel.add(passwdPanel);
		
		passwdLabel = new JLabel("密碼          ");
		passwdPanel.add(passwdLabel);
		
		passwdField = new JPasswordField();
		passwdField.setColumns(10);
		passwdPanel.add(passwdField);
		
		// body_gender
		genderPanel = new JPanel();
		bodyPanel.add(genderPanel);
		
		genderLabel = new JLabel("性別          ");
		genderPanel.add(genderLabel);
		
		genderComboBox = new JComboBox<>(gender);
		genderPanel.add(genderComboBox);
		
		// body_birth
		birthPanel = new JPanel();
		bodyPanel.add(birthPanel);
		
		birthLabel = new JLabel("生日");
		birthPanel.add(birthLabel);

		Date today = new Date();
		birthDatePicker = new JDatePicker(today);
		birthPanel.add(birthDatePicker);
		
		// body_email
		emailPanel = new JPanel();
		bodyPanel.add(emailPanel);
		
		emailLabel = new JLabel("信箱          ");
		emailPanel.add(emailLabel);
		
		emailField = new JTextField();
		emailField.setColumns(10);
		emailPanel.add(emailField);
		
		// body_tel
		telPanel = new JPanel();
		bodyPanel.add(telPanel);
		
		telLabel = new JLabel("電話          ");
		telPanel.add(telLabel);
		
		telField = new JTextField();
		telField.setColumns(10);
		telPanel.add(telField);
		
		// footer_Panel
		footerPanel = new JPanel();
		add(footerPanel, BorderLayout.SOUTH);
		
		// footer_submit
		submitButton = new JButton("建立");
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
		// 建立帳號
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// sql insert帳號資料
				if (createAccount()) {
					JOptionPane.showMessageDialog(null, "歡迎加入！");
					dispose();
				} else {
					JOptionPane.showMessageDialog(null, "建立錯誤，請重新輸入");
					initRegisterInput(); // 重置輸入框
				}
			}
		});
		
		// 取消建立
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
	
	// 重置輸入框
	private void initRegisterInput() {
		nameField.setText("");
		idField.setText("");
		accountField.setText("");
		passwdField.setText("");
		emailField.setText("");
		telField.setText("");
	}
	
	// sql insert帳號資料
	private Boolean createAccount() {
		SQLInsert sqlInsert = new SQLInsert(DB, Account, Password);
		String name = getUserName();
		String idNumber = getIdNumber();
		String account = getAccount();
		String passwd = getPassword();
		String gender = getGender();
		String birth = getBirth();
		String mail = getMail();
		String tel = getTel();
		
		// 執行insert並回傳結果
		if (sqlInsert.insertCreateAccount(name, idNumber, account, passwd, gender, birth, mail, tel)) {
			return true;
		} else {
			return false;
		}	
	}
	
	// 取得名字
	private String getUserName() {
		return nameField.getText();
	}
	
	// 取得身分證字號
	protected String getIdNumber() {
		return idField.getText();
	}
	
	// 取得帳號
	private String getAccount() {
		return accountField.getText();
	}
	
	// 取得密碼 加鹽
	protected String getPassword() {
		String inputPasswd = String.valueOf(passwdField.getPassword());
		String passwd = BCrypt.hashpw(inputPasswd, BCrypt.gensalt());
		return passwd;
	}
	
	// 取得性別
	private String getGender() {
		return genderComboBox.getSelectedItem().toString();
	}
	
	private String getBirth() {
		SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
		int year = birthDatePicker.getModel().getYear();
		int month = birthDatePicker.getModel().getMonth();
		int day = birthDatePicker.getModel().getDay();
		LocalDate targetDate = LocalDate.of(year, month, day);
		Instant instant = targetDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		Date birthDate = Date.from(instant);
		
		if (targetDate.isBefore(LocalDate.now())) {
			return f1.format(birthDate);
		} else {
			JOptionPane.showMessageDialog(null, "請輸入正確出生日期");
			return null;
		}
	}
	
	private String getMail() {
		return emailField.getText();
	}
	
	private String getTel() {
		return telField.getText();
	}
}
