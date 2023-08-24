import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class BankApp {

	public JFrame frame;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BankApp window = new BankApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public BankApp() {
		initialize();
	}

	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 290, 408);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Database.connect();
		setupClosingDBConnection();
		
		createhbankTitle();
		createhCustomerPanel();
		createhEmployeePanel();
		createhlogo();
		
	}
	
	public static void setupClosingDBConnection() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            try { Database.connection.close(); System.out.println("Application Closed - DB Connection Closed");
				} catch (SQLException e) { e.printStackTrace(); }
	        }
	    }, "Shutdown-thread"));
	}
	
	public void createhbankTitle() {
		JLabel hBankTitle = new JLabel("Bank It Up!");
		hBankTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		hBankTitle.setForeground(Color.GRAY);
		hBankTitle.setHorizontalAlignment(SwingConstants.CENTER);
		hBankTitle.setFont(new Font("Rockwell", Font.BOLD, 30));
		hBankTitle.setBounds(25, 20, 202, 53);
		frame.getContentPane().add(hBankTitle);
	}
	public void createhCustomerPanel() {
		JPanel hCustomerPanel = new JPanel();
		hCustomerPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "For customers", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		hCustomerPanel.setBounds(25, 85, 246, 128);
		frame.getContentPane().add(hCustomerPanel);
		hCustomerPanel.setLayout(null);
		
		JButton HLogInButton = new JButton("Log In");
		HLogInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToCustomerLogInPage();
			}
		});
		HLogInButton.setBounds(17, 28, 211, 29);
		hCustomerPanel.add(HLogInButton);
		
		JButton hSignUpForFree = new JButton("Sign up for free");
		hSignUpForFree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToCreateAccountPage();
			}
		});
		hSignUpForFree.setBounds(17, 69, 211, 29);
		hCustomerPanel.add(hSignUpForFree);
	}
	public void createhEmployeePanel() {
		JPanel hEmployeePanel = new JPanel();
		hEmployeePanel.setBorder(new TitledBorder(null, "Employee access", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		hEmployeePanel.setBounds(25, 225, 246, 128);
		frame.getContentPane().add(hEmployeePanel);
		hEmployeePanel.setLayout(null);
		
		JButton hEmployeeLogIn = new JButton("Employee Log In");
		hEmployeeLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToEmployeeLogInPage();
			}
		});
		hEmployeeLogIn.setBounds(20, 30, 208, 29);
		hEmployeePanel.add(hEmployeeLogIn);
		
		JButton hAdminLogIn = new JButton("Admin Log In");
		hAdminLogIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToAdminLogInPage();
			}
		});
		hAdminLogIn.setBounds(20, 71, 208, 29);
		hEmployeePanel.add(hAdminLogIn);
	}
	public void createhlogo() {
		JLabel hlogo = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/baglogo.png")).getImage();
		hlogo.setIcon(new ImageIcon(img));
		hlogo.setBounds(221, 20, 48, 53);
		frame.getContentPane().add(hlogo);
	}
	public void goToAdminLogInPage() {
		frame.dispose();
		AdminLogIn ALIP = new AdminLogIn(); 
		ALIP.initialize();
		ALIP.frame.setVisible(true);
	}
	public void goToEmployeeLogInPage() {
		frame.dispose();
		EmployeeLogIn ELIP = new EmployeeLogIn(); 
		ELIP.initialize();
		ELIP.frame.setVisible(true);
	}
	public void goToCustomerLogInPage() {
		frame.dispose();
		CustomerLogIn CLIP = new CustomerLogIn(); 
		CLIP.initialize();
		CLIP.frame.setVisible(true);
	}
	public void goToCreateAccountPage() {
		frame.dispose();
		CreateAccount CAP = new CreateAccount(); 
		CAP.initialize();
		CAP.frame.setVisible(true);
	}
}