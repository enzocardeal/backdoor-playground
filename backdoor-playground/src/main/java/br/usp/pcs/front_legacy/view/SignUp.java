package br.usp.pcs.front_legacy.view;

import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static br.usp.pcs.front_legacy.api.Request.sendPostRequest;

public class SignUp {
    private JFrame mainFrame;
    public JPanel signUpPanel;
    private JLabel signUpTitle;
    private JTextField usernameInput;
    private JLabel usernameText;
    private JLabel passwordText;
    private JButton signUpButton;
    private JLabel successText;
    private JPasswordField passwordInput;
    private JPasswordField repeatPasswordInput;
    private JLabel repeatPasswordText;

    public SignUp(JFrame mainFrame) {
    this.mainFrame = mainFrame;
    signUpButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            final String requestUrl = "http://localhost:8000/api/user/signup";
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            String repeatPassword = repeatPasswordInput.getText();
            String bodyString = "{\"username\":"+"\""+username+"\""+",\"password\":"+"\""+password+"\""+"}";

            if(username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
                successText.setText("Insira username e password válidos.");
            }
            else if(password.equals(repeatPassword)){
                JSONObject response = sendPostRequest(requestUrl, bodyString);

                if((boolean) response.get("success")){
                    mainFrame.setContentPane((new Login(mainFrame).loginPanel));
                    mainFrame.revalidate();
                }
                else{
                    successText.setText((String) response.get("message"));
                }
            }
            else{
                successText.setText("A senha repetida não é a mesma senha.");
            }

        }
    });
}
}
