import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JPasswordField;

public class AdminLogIn {

	public JFrame frame;
	private JTextField usernamebox;
	public static String nameOfUser;
	private JPasswordField passwordField;

	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminLogIn window = new AdminLogIn();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public AdminLogIn() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 290, 408);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Database.connect();
		setupClosingDBConnection();
		
		createalBankTitle();
		createallogo();
		createaladminLogInPanel();
		
	}
	public static void setupClosingDBConnection() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            try { Database.connection.close(); System.out.println("Application Closed - DB Connection Closed");
				} catch (SQLException e) { e.printStackTrace(); }
	        }
	    }, "Shutdown-thread"));
	}
	public void createalBankTitle() {
		JLabel hBankTitle = new JLabel("Bank It Up!");
		hBankTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		hBankTitle.setHorizontalAlignment(SwingConstants.CENTER);
		hBankTitle.setForeground(Color.GRAY);
		hBankTitle.setFont(new Font("Rockwell", Font.BOLD, 30));
		hBankTitle.setBounds(25, 20, 202, 53);
		frame.getContentPane().add(hBankTitle);
	}
	public void createallogo() {
		JLabel hlogo = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/baglogo.png")).getImage();
		hlogo.setIcon(new ImageIcon(img));
		hlogo.setBounds(221, 20, 48, 53);
		frame.getContentPane().add(hlogo);
	}
	public void createaladminLogInPanel() {
		JPanel adminLogInPanel = new JPanel();
		adminLogInPanel.setLayout(null);
		adminLogInPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Admin Log In", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		adminLogInPanel.setBounds(25, 85, 246, 206);
		frame.getContentPane().add(adminLogInPanel);
		
		JButton HLogInButton = new JButton("Log In");
		HLogInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				redirectAdminPage();
			}
		});
		HLogInButton.setBounds(17, 148, 211, 29);
		adminLogInPanel.add(HLogInButton);
		
		usernamebox = new JTextField();
		usernamebox.setColumns(10);
		usernamebox.setBounds(17, 46, 211, 30);
		adminLogInPanel.add(usernamebox);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(22, 27, 85, 16);
		adminLogInPanel.add(lblNewLabel);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(22, 82, 85, 16);
		adminLogInPanel.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(17, 105, 211, 30);
		adminLogInPanel.add(passwordField);
	}
	public void redirectAdminPage() {
		try {
			Connection connection = Database.connection;
			String query = "SELECT ad_password, ad_name FROM Admins WHERE ad_username=?";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setString(1, usernamebox.getText());
			ResultSet result = stm.executeQuery();
			
			if (!result.first()) {
				// No user exists with the username entered
				JOptionPane.showMessageDialog(null, "User does not exist. Please try again", "Unable to Log In", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			String passwordFromDB = result.getString("ad_password");
			String nameFromDB = result.getString("ad_name");
			
			String passwordEntered = passwordField.getText();
			
			if (!passwordEntered.equals(passwordFromDB)) {
				// Incorrect Password
				JOptionPane.showMessageDialog(null, "Password is incorrect. Please try again", "Unable to Log In", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// Correct Password and Username
			JOptionPane.showMessageDialog(null, "Welcome, "+nameFromDB+"!", "Logged In Successfully", JOptionPane.DEFAULT_OPTION);
			
			nameOfUser = nameFromDB;
			goToAdminPage();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void goToAdminPage() {
		frame.dispose();
		AdminPage AP = new AdminPage(); 
		AP.initialize();
		AP.frame.setVisible(true);
	}
}
