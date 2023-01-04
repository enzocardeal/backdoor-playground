package br.usp.pcs.main;

import br.usp.pcs.view.Admin;
import br.usp.pcs.view.Login;
import br.usp.pcs.view.User;

public class Main {

	public static void main(String[] args) {
		Admin admin = new Admin();
		User user = new User();
		
		Login login = new Login(admin.getAdminFrame(), user.getUserFrame());
		login.setLogin();
	}
}
