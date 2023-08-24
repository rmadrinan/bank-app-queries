import java.awt.EventQueue;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
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


import javax.swing.SwingConstants;
import javax.swing.JPasswordField;

public class CreateAccount {

	public JFrame frame;
	private JTextField cUserBox;
	private JTextField cFullNameBox;
	private JTextField cAddressBox;
	private JTextField ccustomerIDBox;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateAccount window = new CreateAccount();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public CreateAccount() {
		initialize();
	}

	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 290, 490);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Database.connect();
		setupClosingDBConnection();
		
		createCreateAccountPanel();
		createcBankTitle();
		createclogo();
		
	}
	public static void setupClosingDBConnection() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            try { Database.connection.close(); System.out.println("Application Closed - DB Connection Closed");
				} catch (SQLException e) { e.printStackTrace(); }
	        }
	    }, "Shutdown-thread"));
	}
	public void createCreateAccountPanel() {
		JPanel createAccountPanel = new JPanel();
		createAccountPanel.setLayout(null);
		createAccountPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Create Account", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		createAccountPanel.setBounds(25, 85, 246, 356);
		frame.getContentPane().add(createAccountPanel);
		
		JButton cCreateAccount = new JButton("Create Account");
	    cCreateAccount.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	            createCustomerAccount();
	                goToHomePage();
	            }
	    });
		cCreateAccount.setBounds(17, 309, 211, 29);
		createAccountPanel.add(cCreateAccount);
		
		cUserBox = new JTextField();
		cUserBox.setColumns(10);
		cUserBox.setBounds(17, 46, 211, 30);
		createAccountPanel.add(cUserBox);
		
		JLabel cUsername = new JLabel("Username");
		cUsername.setBounds(22, 27, 85, 16);
		createAccountPanel.add(cUsername);
		
		JLabel cPassword = new JLabel("Password");
		cPassword.setBounds(22, 82, 85, 16);
		createAccountPanel.add(cPassword);
		
		JLabel cFullName = new JLabel("Full Name");
		cFullName.setBounds(22, 137, 85, 16);
		createAccountPanel.add(cFullName);
		
		JLabel cAddress = new JLabel("Address");
		cAddress.setBounds(22, 192, 85, 16);
		createAccountPanel.add(cAddress);
		
		cFullNameBox = new JTextField();
		cFullNameBox.setColumns(10);
		cFullNameBox.setBounds(17, 156, 211, 30);
		createAccountPanel.add(cFullNameBox);
		
		cAddressBox = new JTextField();
		cAddressBox.setColumns(10);
		cAddressBox.setBounds(17, 209, 211, 43);
		createAccountPanel.add(cAddressBox);
		
		JLabel ccustomerID = new JLabel("Assign a unique ID (numeric only)");
		ccustomerID.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		ccustomerID.setBounds(22, 260, 206, 16);
		createAccountPanel.add(ccustomerID);
		
		ccustomerIDBox = new JTextField();
		ccustomerIDBox.setColumns(10);
		ccustomerIDBox.setBounds(17, 278, 211, 26);
		createAccountPanel.add(ccustomerIDBox);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(17, 100, 211, 30);
		createAccountPanel.add(passwordField);
	}
	public void createcBankTitle() {
		JLabel cBankTitle = new JLabel("Bank It Up!");
		cBankTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		cBankTitle.setHorizontalAlignment(SwingConstants.CENTER);
		cBankTitle.setForeground(Color.GRAY);
		cBankTitle.setFont(new Font("Rockwell", Font.BOLD, 30));
		cBankTitle.setBounds(25, 20, 202, 53);
		frame.getContentPane().add(cBankTitle);
	}
	public void createclogo() {
		JLabel clogo = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/baglogo.png")).getImage();
		clogo.setIcon(new ImageIcon(img));
		clogo.setBounds(221, 20, 48, 53);
		frame.getContentPane().add(clogo);
	}
	public void createCustomerAccount() {
	    try {
	        Connection connection = Database.connection;

	        String checkQuery = "SELECT COUNT(*) FROM Customers WHERE c_id = ?";
	        PreparedStatement checkStm = connection.prepareStatement(checkQuery);
	        checkStm.setInt(1, Integer.parseInt(ccustomerIDBox.getText()));
	        ResultSet checkResult = checkStm.executeQuery();
	        checkResult.next();
	        int count = checkResult.getInt(1);
	        if (count > 0) {
	            JOptionPane.showMessageDialog(null, "ID already exists. Please choose a different ID.", "Duplicate ID", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        String insertQuery = "INSERT INTO Customers VALUES (?, ?, ?, ?, ?)";
	        PreparedStatement stm = connection.prepareStatement(insertQuery);

	        stm.setInt(1, Integer.parseInt(ccustomerIDBox.getText()));
	        stm.setString(2, cUserBox.getText());
	        stm.setString(3, passwordField.getText());
	        stm.setString(4, cFullNameBox.getText());
	        stm.setString(5, cAddressBox.getText());
	        stm.executeUpdate();
	        JOptionPane.showMessageDialog(null, "Your account was added to the database!", "Account Created Successfully!", JOptionPane.DEFAULT_OPTION);
	    } catch (Exception e) {
	        System.out.println(e);
	    }

	}
	public void goToHomePage() {
		frame.dispose();
		BankApp BAP = new BankApp(); 
		BAP.initialize();
		BAP.frame.setVisible(true);
	}

}
