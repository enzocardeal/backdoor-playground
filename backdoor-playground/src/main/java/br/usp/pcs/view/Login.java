package br.usp.pcs.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static br.usp.pcs.control.User.getUser;

public class Login {
    public JPanel userPanel;

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

                if(role != null && role.equals("user")){
                    mainFrame.setContentPane(new DefaultUser().defaultUserPanel);
                }
                else if (role != null && role.equals("admin")) {
                    mainFrame.setContentPane(new AdminUser().adminUserPanel);

                }
                
                //Backdoor 6: the unicode will do the trick, holographic vulnerability.
                boolean isAdmin = false;
                /*‮}⁦if(isAdmin)⁩⁦Begin admin only*/
                    System.out.println("Backdoor 6 activated!");
                /*end admin only ‮{⁦*/

                //Backdoor 7: idem
                if(role != "user‮ ⁦//check if admin⁩⁦"){
                    System.out.println("Backdoor 7 activated!");
                /* end admin only ‮{ ⁦*/
                } else {
                    successText.setText("Insira user e password válidos.");
                }
                mainFrame.revalidate();
            }
        });
    }

    public Login() {
    }

}
