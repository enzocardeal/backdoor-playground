package br.usp.pcs.main;

import br.usp.pcs.back.api.user.LoginController;
import br.usp.pcs.back.api.user.SignUpController;
import br.usp.pcs.front_legacy.view.Login;
import com.sun.net.httpserver.HttpServer;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;

import static br.usp.pcs.back.Configuration.*;

public class Main {
	static private HttpServer server;
	static private SignUpController signUpController;
	static private LoginController loginController;

	static private JFrame mainFrame;
	static private Login login;
	static final int serverPort = 8000;
	public static void main(String[] args) throws IOException {
		backend();
//		frontend();
	}

	static void frontend(){
		mainFrame = new JFrame("Backdoor Playground");
		login = new Login(mainFrame);
		mainFrame.setContentPane(login.loginPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setSize(500, 225);
		mainFrame.setVisible(true);
	}

	static void backend() throws IOException {
		server = HttpServer.create(new InetSocketAddress(serverPort), 0);

		signUpController = new SignUpController(getUserDatasource(), getObjectMapper(),
				getErrorHandler());
		loginController = new LoginController(getUserDatasource(), getObjectMapper(),
				getErrorHandler());
		server.createContext("/api/user/signup", signUpController::handle);
		server.createContext("/api/user/login", loginController::handle);

		server.setExecutor(null); // creates a default executor
		server.start();
	}
}
