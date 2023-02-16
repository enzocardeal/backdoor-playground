package br.usp.pcs.main;

import br.usp.pcs.view.Login;

import javax.swing.*;

public class Main {

	public static void main(String[] args) {

		JFrame mainFrame = new JFrame("Backdoor Playground");
		Login login = new Login(mainFrame);
		mainFrame.setContentPane(login.loginPanel);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setSize(500, 225);
		mainFrame.setVisible(true);
	}
}
