package br.usp.pcs.view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class User {
		private static JFrame userFrame; 
		public JFrame getUserFrame() {
			setUser();
			return userFrame;
		}

		public void setUserFrame(JFrame userFrame) {
			User.userFrame = userFrame;
		}
		private static JPanel userPanel;
		private static JLabel userLabel;
		
		
		public static void setUser() {
		    userFrame = new JFrame("User");
		    userFrame.setSize(600, 400);
		    userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
	        userPanel = new JPanel();    
	        userFrame.add(userPanel);
	        placeComponents(userPanel);
	        
		}
		
	    private static void placeComponents(JPanel panel) {

	        panel.setLayout(null);
	        userLabel = new JLabel("User");
	        
	        userLabel.setBounds(10,20,80,25);
	        panel.add(userLabel);
	        
	    }
}
