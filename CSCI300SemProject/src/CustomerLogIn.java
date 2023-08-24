import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;

public class CustomerLogIn {

	public JFrame frame;
	private JTextField usernamebox;
	public static String nameOfUser;
	public static int IDOfUser;
	private JPasswordField passwordField;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomerLogIn window = new CustomerLogIn();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CustomerLogIn() {
		initialize();
	}

	public void initialize() {
		frame = new JFrame();
		frame.setTitle(" ");
		frame.setBounds(100, 100, 290, 408);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Database.connect();
		setupClosingDBConnection();
		
		createBankTitle();
		createLogo();
		createCustomerLogInPanel();
		
	}
	public static void setupClosingDBConnection() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            try { Database.connection.close(); System.out.println("Application Closed - DB Connection Closed");
				} catch (SQLException e) { e.printStackTrace(); }
	        }
	    }, "Shutdown-thread"));
	}
	public void createBankTitle() {
		JLabel hBankTitle = new JLabel("Bank It Up!");
		hBankTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		hBankTitle.setHorizontalAlignment(SwingConstants.CENTER);
		hBankTitle.setForeground(Color.GRAY);
		hBankTitle.setFont(new Font("Rockwell", Font.BOLD, 30));
		hBankTitle.setBounds(25, 20, 202, 53);
		frame.getContentPane().add(hBankTitle);
	}
	public void createLogo() {
		JLabel hlogo = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/baglogo.png")).getImage();
		hlogo.setIcon(new ImageIcon(img));
		hlogo.setBounds(221, 20, 48, 53);
		frame.getContentPane().add(hlogo);
	}
	public void createCustomerLogInPanel() {
		JPanel customerLogInPanel = new JPanel();
		customerLogInPanel.setLayout(null);
		customerLogInPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Customer Log In", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		customerLogInPanel.setBounds(25, 85, 246, 236);
		frame.getContentPane().add(customerLogInPanel);
		
		JButton HLogInButton = new JButton("Log In");
		HLogInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redirectCustomerPage();
				
			}
		});
		HLogInButton.setBounds(17, 139, 211, 29);
		customerLogInPanel.add(HLogInButton);
		
		JButton noAccountbutton = new JButton("No Account?");
		noAccountbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToCreateAccountPage();
			}
		});
		noAccountbutton.setBounds(17, 180, 211, 29);
		customerLogInPanel.add(noAccountbutton);
		
		usernamebox = new JTextField();
		usernamebox.setBounds(17, 46, 211, 30);
		customerLogInPanel.add(usernamebox);
		usernamebox.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(22, 27, 85, 16);
		customerLogInPanel.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(22, 82, 85, 16);
		customerLogInPanel.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(17, 100, 211, 30);
		customerLogInPanel.add(passwordField);
	}
	public void goToCreateAccountPage() {
		frame.dispose();
		CreateAccount CAP = new CreateAccount(); 
		CAP.initialize();
		CAP.frame.setVisible(true);
	}
	public void redirectCustomerPage() {
		try {
			Connection connection = Database.connection;
			String query = "SELECT c_id, c_password, c_name FROM Customers WHERE c_username=?";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setString(1, usernamebox.getText());
			ResultSet result = stm.executeQuery();
			
			if (!result.first()) {
				// No user exists with the username entered
				JOptionPane.showMessageDialog(null, "User does not exist. Please try again", "Unable to Log In", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String passwordFromDB = result.getString("c_password");
			String nameFromDB = result.getString("c_name");
			int IDFromDB = result.getInt("c_id");
			
			String passwordEntered = passwordField.getText();
			
			if (!passwordEntered.equals(passwordFromDB)) {
				// Incorrect Password
				JOptionPane.showMessageDialog(null, "Password is incorrect. Please try again", "Unable to Log In", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Correct Password and Username
			JOptionPane.showMessageDialog(null, "Welcome, "+nameFromDB+"!", "Logged In Successfully", JOptionPane.DEFAULT_OPTION);
			
			nameOfUser = nameFromDB;
			IDOfUser = IDFromDB;
			goToCustomerPage();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void goToCustomerPage() {
		frame.dispose();
		CustomerPage CP = new CustomerPage(); 
		CP.initialize();
		CP.frame.setVisible(true);
	}

}
