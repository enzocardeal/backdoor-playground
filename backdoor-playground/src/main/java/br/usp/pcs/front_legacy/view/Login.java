package br.usp.pcs.front_legacy.view;

import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static br.usp.pcs.front_legacy.api.Request.sendPostRequest;

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
                final String requestUrl = "http://localhost:8000/api/user/login";
                String username = usernameInput.getText();
                String password = passwordInput.getText();
                String bodyString = "{\"username\":"+"\""+username+"\""+",\"password\":"+"\""+password+"\""+"}";

                JSONObject response = sendPostRequest(requestUrl, bodyString);
                String role = (String) response.get("role");

                if(username.isEmpty() || password.isEmpty()){
                    successText.setText("Insira username e password válidos.");
                }
                else if(role != null && role.equals("DEFAULT")){
                    mainFrame.setContentPane(new DefaultUser().defaultUserPanel);
                }
                else if (role != null && role.equals("ADMIN")) {
                    mainFrame.setContentPane(new AdminUser().adminUserPanel);

                }
                else {
                    successText.setText((String) response.get("message"));
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
