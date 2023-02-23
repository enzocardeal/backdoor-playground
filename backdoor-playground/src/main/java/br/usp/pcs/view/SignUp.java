package br.usp.pcs.view;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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

    private static final String POST_URL =  "http://localhost:5500/signup";

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
                String response =  sendPostAndRegister(username, password);
                if(response.equals("true") ){
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

    private String sendPostAndRegister(String username, String password) {
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

            return (String) jsonObject.get("success");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
