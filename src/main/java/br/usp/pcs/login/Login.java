package br.usp.pcs.login;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
public class Login implements ActionListener{
	
	private static JFrame frame;
	private static JPanel panel;
	private static JLabel success;
	private static JLabel userLabel;
	private static JTextField userText;
	private static JLabel passwordLabel;
	private static JPasswordField passwordText;
	private static JButton loginButton;
	
	public static void setLogin() {
	    frame = new JFrame("Backdoor Playground");
	    frame.setSize(600, 400);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
        panel = new JPanel();    
        frame.add(panel);
        placeComponents(panel);
        
        //Temp construct
        success = new JLabel("");
        success.setBounds(10, 110, 300, 25);
        
        panel.add(success);
        
        frame.setVisible(true);
	}

    private static void placeComponents(JPanel panel) {

        panel.setLayout(null);
        userLabel = new JLabel("User");
        
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);
        
        userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);
        
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10,50,80,25);
        panel.add(passwordLabel);
        
        passwordText = new JPasswordField(20);
        passwordText.setBounds(100,50,165,25);
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
		
		if(user.equals("user") && password.equals("123456")) {
			success.setText("Sucesso!");
		}
		else {
			success.setText("Insira user e password válidos.");
		}
	}
    
	
}
