package br.usp.pcs.main;

import br.usp.pcs.page.Admin;
import br.usp.pcs.page.Login;
import br.usp.pcs.page.User;

public class Main {

	public static void main(String[] args) {
		Admin admin = new Admin();
		User user = new User();
		
//		user.getUserFrame().setVisible(true);
		
		Login login = new Login(admin.getAdminFrame(), user.getUserFrame());
		login.setLogin();
	}
}
