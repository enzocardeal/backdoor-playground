package br.usp.pcs.page;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Admin {
	private static JFrame adminFrame; 
	public JFrame getAdminFrame() {
		setAdmin();
		return adminFrame;
	}

	public void setAdminFrame(JFrame adminFrame) {
		Admin.adminFrame = adminFrame;
	}

	private static JPanel adminPanel;
	private static JLabel adminLabel;
	
	
	public static void setAdmin() {
	    adminFrame = new JFrame("Admin");
	    adminFrame.setSize(600, 400);
	    adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
        adminPanel = new JPanel();    
        adminFrame.add(adminPanel);
        placeComponents(adminPanel);
        
	}
	
    private static void placeComponents(JPanel panel) {

        panel.setLayout(null);
        adminLabel = new JLabel("Admin");
        
        adminLabel.setBounds(10,20,80,25);
        panel.add(adminLabel);
        
    }
}
