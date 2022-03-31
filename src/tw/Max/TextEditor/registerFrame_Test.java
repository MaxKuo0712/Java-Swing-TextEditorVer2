package tw.Max.TextEditor;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JComboBox;

public class registerFrame_Test extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_5;
	private JTextField textField_3;
	private JPasswordField passwordField;
	private JTextField textField_2;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					registerFrame_Test frame = new registerFrame_Test();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public registerFrame_Test() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("建立您的帳戶");
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("建立");
		panel_1.add(btnNewButton);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new GridLayout(8, 0, 0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		
		JLabel lblNewLabel_1 = new JLabel("姓名");
		panel_4.add(lblNewLabel_1);
		
		textField = new JTextField();
		panel_4.add(textField);
		textField.setColumns(10);
		
		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		
		JLabel lblNewLabel_3 = new JLabel("帳號");
		panel_5.add(lblNewLabel_3);
		
		textField_3 = new JTextField();
		panel_5.add(textField_3);
		textField_3.setColumns(10);
		
		JPanel panel_6 = new JPanel();
		panel_3.add(panel_6);
		
		JLabel lblNewLabel_4 = new JLabel("密碼");
		panel_6.add(lblNewLabel_4);
		
		passwordField = new JPasswordField();
		passwordField.setColumns(10);
		panel_6.add(passwordField);
		
		JPanel panel_7 = new JPanel();
		panel_3.add(panel_7);
		
		JLabel lblNewLabel_2 = new JLabel("性別");
		panel_7.add(lblNewLabel_2);
		
		JComboBox comboBox = new JComboBox();
		panel_7.add(comboBox);
		
		JPanel panel_8 = new JPanel();
		panel_3.add(panel_8);
		
		JLabel lblNewLabel_5 = new JLabel("年齡");
		panel_8.add(lblNewLabel_5);
		
		textField_1 = new JTextField();
		panel_8.add(textField_1);
		textField_1.setColumns(10);
		
		JPanel panel_9 = new JPanel();
		panel_3.add(panel_9);
		
		JLabel lblNewLabel_6 = new JLabel("生日");
		panel_9.add(lblNewLabel_6);
		
		JComboBox comboBox_1 = new JComboBox();
		panel_9.add(comboBox_1);
		
		JComboBox comboBox_2 = new JComboBox();
		panel_9.add(comboBox_2);
		
		JComboBox comboBox_3 = new JComboBox();
		panel_9.add(comboBox_3);
		
		JPanel panel_10 = new JPanel();
		panel_3.add(panel_10);
		
		JLabel lblNewLabel_8 = new JLabel("電子信箱");
		panel_10.add(lblNewLabel_8);
		
		textField_2 = new JTextField();
		panel_10.add(textField_2);
		textField_2.setColumns(10);
		
		JPanel panel_11 = new JPanel();
		panel_3.add(panel_11);
		
		JLabel lblNewLabel_7 = new JLabel("電話");
		panel_11.add(lblNewLabel_7);
		
		textField_5 = new JTextField();
		panel_11.add(textField_5);
		textField_5.setColumns(10);
	}

}
