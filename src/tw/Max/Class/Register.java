package tw.Max.Class;

import javax.swing.JFrame;

public class Register extends JFrame {
	
	public Register() {
		super("建立帳號");
		setSize(300, 400);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public static void main(String[] args) {
		new Register();
	}

}
