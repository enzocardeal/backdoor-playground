package br.usp.pcs.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static br.usp.pcs.control.User.addUser;

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
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            String repeatPassword = repeatPasswordInput.getText();

            if(username.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
                successText.setText("Insira username e password válidos.");
            }
            else if(password.equals(repeatPassword)){
                boolean response =  addUser(username, password);
                if(response){
                mainFrame.setContentPane((new Login(mainFrame).loginPanel));
                mainFrame.revalidate();
                }
                else{
                    successText.setText("Usuário já cadastrado!");
                }
            }
            else{
                successText.setText("A senha repetida não é a mesma senha.");
            }

        }
    });
}
}
