package br.usp.pcs.view;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static br.usp.pcs.control.User.getUser;

public class Login {
    private static final String POST_URL =  "http://localhost:5500/login";
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

                if(username.isEmpty() || password.isEmpty()) {
                    successText.setText("Insira username e password válidos.");
                } else {
                    sendPostAndAuthenticate(username, password);
                }
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

    private void sendPostAndAuthenticate(String username, String password){
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        String body = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create( POST_URL ))
                .header("Content-Type", "application/json")
                .header( "Accept", "application/json" )
                .version(HttpClient.Version.HTTP_1_1)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        try {
            String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = new JSONObject();

            jsonObject = (JSONObject) parser.parse(String.valueOf(response));

            String role = (String) jsonObject.get("role");


            if(role != null && role.equals("user")){
                mainFrame.setContentPane(new DefaultUser().defaultUserPanel);
            } else if (role != null && role.equals("admin")) {
                mainFrame.setContentPane(new AdminUser().adminUserPanel);
            } else {
                successText.setText("Insira username e password válidos.");
            }
            mainFrame.revalidate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
