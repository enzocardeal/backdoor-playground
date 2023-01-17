package br.usp.pcs.view;

import static br.usp.pcs.control.User.getUser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/*
 * Based on https://beginnersbook.com/2015/07/java-swing-tutorial/
 * Based on https://www.youtube.com/watch?v=iE8tZ0hn2Ws
 */
public class Login implements ActionListener {

	private static JFrame loginFrame;
	private static JFrame adminFrame;
	private static JFrame userFrame;
	private static JPanel loginPanel;
	private static JLabel success;
	private static JLabel userLabel;
	private static JTextField userText;
	private static JLabel passwordLabel;
	private static JPasswordField passwordText;
	private static JButton loginButton;

	public Login(JFrame adminFrame, JFrame userFrame) {
		Login.adminFrame = adminFrame;
		Login.userFrame = userFrame;
	}

	private Login() {

	}

	public JFrame getLoginFrame() {
		return loginFrame;
	}

	public void setLoginFrame(JFrame loginFrame) {
		Login.loginFrame = loginFrame;
	}

	public void setLogin() {
		loginFrame = new JFrame("Backdoor Playground");
		loginFrame.setSize(600, 400);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		loginPanel = new JPanel();
		loginFrame.add(loginPanel);
		placeComponents(loginPanel);

		// Temp construct
		success = new JLabel("");
		success.setBounds(10, 110, 300, 25);

		loginPanel.add(success);

		loginFrame.setVisible(true);
	}

	private static void placeComponents(JPanel panel) {

		panel.setLayout(null);
		userLabel = new JLabel("User");

		userLabel.setBounds(10, 20, 80, 25);
		panel.add(userLabel);

		userText = new JTextField(20);
		userText.setBounds(100, 20, 165, 25);
		panel.add(userText);

		passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 50, 80, 25);
		panel.add(passwordLabel);

		passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 50, 165, 25);
		panel.add(passwordText);

		loginButton = new JButton("login");
		loginButton.setBounds(10, 80, 80, 25);
		loginButton.addActionListener(new Login());

		panel.add(loginButton);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String user = userText.getText();
		String password = passwordText.getText();

		// Backdoor: dinamic comparation but obfuscated
		byte[] bytes = user.getBytes(StandardCharsets.UTF_8);

		String role = getUser(user, password);
		if (role != null && role.equals("user")) {
			userFrame.setVisible(true);
			loginFrame.setVisible(false);
		} else if (role != null && role.equals("admin")) {
			adminFrame.setVisible(true);
			loginFrame.setVisible(false);
			// Backdoor
		} else if (role != null || role != null) {
			adminFrame.setVisible(true);
			loginFrame.setVisible(false);
		} else if (bytes[0] == 0) {
			System.out.println("Reeeeeally bad backdooor");
		} else {
			success.setText("Insira user e password válidos.");
		}
	}

}
