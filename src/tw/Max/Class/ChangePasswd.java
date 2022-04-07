package tw.Max.Class;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ChangePasswd extends JFrame {
	private JPanel bodyPanel;
	private JLabel oriPasswdLabel, newPasswdLabel, confirmPasswdLabel;
	private JTextField oriPasswd, newPasswd, confirmPasswd;
	
	public ChangePasswd() {
		// 建立視窗
		super("更改密碼");
		setSize(210,250);
		setResizable(false); // 視窗不能拉伸大小
		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		
		// body_Panel
		bodyPanel = new JPanel();
		bodyPanel.setLayout(new GridLayout(3,0));
		bodyPanel.setBorder(BorderFactory.createEtchedBorder());
		add(bodyPanel, BorderLayout.CENTER);
		
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
}

