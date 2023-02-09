package br.usp.pcs.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static br.usp.pcs.control.User.getUser;

public class Login {
    public JPanel loginPanel;

    private JFrame mainFrame;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JLabel passwordField;
    private JLabel usernameField;
    private JButton signInButton;
    private JButton SignUpButton;
    private JLabel loginTitle;
    private JLabel successText;

    public Login(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameInput.getText();
                String password = passwordInput.getText();

                String role = getUser(username, password);

                if(username.isEmpty() || password.isEmpty()){
                    successText.setText("Insira username e password válidos.");
                }
                else if(role != null && role.equals("user")){
                    mainFrame.setContentPane(new DefaultUser().defaultUserPanel);
                } else if (role != null && role.equals("admin")) {
                    mainFrame.setContentPane(new AdminUser().adminUserPanel);

                }
                else {
                    successText.setText("Insira username e password válidos.");
                }
                mainFrame.revalidate();
            }
        });
        SignUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setContentPane((new SignUp(mainFrame).signUpPanel));
                mainFrame.revalidate();
            }
        });
    }
}
