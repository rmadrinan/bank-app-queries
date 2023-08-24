import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import java.awt.SystemColor;

public class AdminPage {

	public JFrame frame;
	private JTextField acusernamebox;
	private JTextField acpasswordbox;
	private JTextField acnamebox;
	private JTextField acaddressbox;
	private JTextField accustomeridbox;
	private JTextField aeusernamebox;
	private JTextField aepasswordbox;
	private JTextField aefullnamebox;
	private JTextField aeemployeeidbox;
	private JTextField aacaccidbox;
	private JTextField aaccustomeridbox;
	private JTextField aacbalbox;
	private JTextField aacprevtransbox;
	private JTable acustomertable;
	private JTable aemployeetable;
	private JTable aaccounttable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminPage window = new AdminPage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AdminPage() {
		initialize();
	}

	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 750, 522);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Database.connect();
		setupClosingDBConnection();
		
		createhBankTitle();
		createaAdminPageText();
		createhlogo();
		createaexitpagebutton();
		createTabbedPane();
		populatecTable();
		populateeTable();
		populateacTable();
		
	}
	public static void setupClosingDBConnection() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            try { Database.connection.close(); System.out.println("Application Closed - DB Connection Closed");
				} catch (SQLException e) { e.printStackTrace(); }
	        }
	    }, "Shutdown-thread"));
	}
	public void createhBankTitle() {
		JLabel hBankTitle = new JLabel("Bank It Up!");
		hBankTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		hBankTitle.setHorizontalAlignment(SwingConstants.CENTER);
		hBankTitle.setForeground(Color.GRAY);
		hBankTitle.setFont(new Font("Rockwell", Font.BOLD, 30));
		hBankTitle.setBounds(6, 6, 202, 53);
		frame.getContentPane().add(hBankTitle);
	}
	public void createaAdminPageText() {
		JLabel aAdminPageText = new JLabel("Admin Page");
		aAdminPageText.setHorizontalAlignment(SwingConstants.RIGHT);
		aAdminPageText.setForeground(Color.DARK_GRAY);
		aAdminPageText.setFont(new Font("Sana", Font.PLAIN, 25));
		aAdminPageText.setBounds(542, 20, 190, 27);
		frame.getContentPane().add(aAdminPageText);
	}
	public void createhlogo() {
		JLabel hlogo = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/baglogo.png")).getImage();
		hlogo.setIcon(new ImageIcon(img));
		hlogo.setBounds(202, 6, 48, 53);
		frame.getContentPane().add(hlogo);
	}
	public void createaexitpagebutton() {
		JButton aexitpagebutton = new JButton("Exit Admin Page");
		aexitpagebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToHomePage();
			}
		});
		aexitpagebutton.setBounds(542, 448, 190, 29);
		frame.getContentPane().add(aexitpagebutton);
	}
	public void createTabbedPane() {
		JTabbedPane admintabs = new JTabbedPane(JTabbedPane.TOP);
		admintabs.setBounds(6, 72, 738, 381);
		frame.getContentPane().add(admintabs);
		
		JPanel acustomerpanel = new JPanel();
		acustomerpanel.setBorder(null);
		admintabs.addTab("Customer Database", null, acustomerpanel, null);
		acustomerpanel.setLayout(null);
		
		JButton acdeletebutton = new JButton("Delete");
		acdeletebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteCustomer();
				populatecTable();
			}
		});
		acdeletebutton.setBounds(124, 259, 117, 29);
		acdeletebutton.setForeground(UIManager.getColor("Button.darkShadow"));
		acustomerpanel.add(acdeletebutton);
		
		JLabel acusername = new JLabel("Username");
		acusername.setBounds(16, 23, 73, 16);
		acustomerpanel.add(acusername);
		
		JLabel acpassword = new JLabel("Password");
		acpassword.setBounds(16, 51, 73, 16);
		acustomerpanel.add(acpassword);
		
		JLabel acname = new JLabel("Full Name");
		acname.setBounds(16, 79, 73, 16);
		acustomerpanel.add(acname);
		
		JLabel acaddress = new JLabel("Address");
		acaddress.setBounds(16, 107, 73, 16);
		acustomerpanel.add(acaddress);
		
		acusernamebox = new JTextField();
		acusernamebox.setBounds(86, 18, 141, 26);
		acustomerpanel.add(acusernamebox);
		acusernamebox.setColumns(10);
		
		acpasswordbox = new JTextField();
		acpasswordbox.setBounds(86, 46, 141, 26);
		acpasswordbox.setColumns(10);
		acustomerpanel.add(acpasswordbox);
		
		acnamebox = new JTextField();
		acnamebox.setBounds(86, 74, 141, 26);
		acnamebox.setColumns(10);
		acustomerpanel.add(acnamebox);
		
		acaddressbox = new JTextField();
		acaddressbox.setBounds(16, 131, 211, 46);
		acaddressbox.setColumns(10);
		acustomerpanel.add(acaddressbox);
		
		JPanel acsearchpanel = new JPanel();
		acsearchpanel.setBounds(6, 189, 235, 58);
		acsearchpanel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		acustomerpanel.add(acsearchpanel);
		acsearchpanel.setLayout(null);
		
		JLabel accustomerid = new JLabel("Customer ID");
		accustomerid.setBounds(16, 24, 86, 16);
		acsearchpanel.add(accustomerid);
		
		accustomeridbox = new JTextField();
		accustomeridbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				viewSearchCustomer();
			}
		});
		accustomeridbox.setColumns(10);
		accustomeridbox.setBounds(104, 19, 116, 26);
		acsearchpanel.add(accustomeridbox);
		
		JButton acupdatebutton = new JButton("Update");
		acupdatebutton.setBounds(6, 259, 117, 29);
		acupdatebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCustomer();
				populatecTable();
			}
		});
		acustomerpanel.add(acupdatebutton);
		
		JButton acclearfieldsbutton = new JButton("Clear Fields");
		acclearfieldsbutton.setBounds(6, 288, 235, 29);
		acclearfieldsbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearCField();
			}
		});
		acustomerpanel.add(acclearfieldsbutton);
		
		JScrollPane acscrollPane = new JScrollPane();
		acscrollPane.setBounds(253, 23, 458, 283);
		acustomerpanel.add(acscrollPane);
		
		acustomertable = new JTable();
		acustomertable.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		acscrollPane.setViewportView(acustomertable);
	
		
		JPanel aemployeepanel = new JPanel();
		admintabs.addTab("Employee Database", null, aemployeepanel, null);
		aemployeepanel.setLayout(null);
		
		JLabel aeusername = new JLabel("Username");
		aeusername.setBounds(16, 23, 73, 16);
		aemployeepanel.add(aeusername);
		
		aeusernamebox = new JTextField();
		aeusernamebox.setColumns(10);
		aeusernamebox.setBounds(86, 18, 141, 26);
		aemployeepanel.add(aeusernamebox);
		
		JLabel aepassword = new JLabel("Password");
		aepassword.setBounds(16, 51, 73, 16);
		aemployeepanel.add(aepassword);
		
		aepasswordbox = new JTextField();
		aepasswordbox.setColumns(10);
		aepasswordbox.setBounds(86, 46, 141, 26);
		aemployeepanel.add(aepasswordbox);
		
		JLabel aename = new JLabel("Full Name");
		aename.setBounds(16, 79, 73, 16);
		aemployeepanel.add(aename);
		
		aefullnamebox = new JTextField();
		aefullnamebox.setColumns(10);
		aefullnamebox.setBounds(86, 74, 141, 26);
		aemployeepanel.add(aefullnamebox);
		
		JPanel aesearchpanel = new JPanel();
		aesearchpanel.setLayout(null);
		aesearchpanel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		aesearchpanel.setBounds(6, 107, 235, 58);
		aemployeepanel.add(aesearchpanel);
		
		JLabel aeemployeeid = new JLabel("Employee ID");
		aeemployeeid.setBounds(16, 24, 86, 16);
		aesearchpanel.add(aeemployeeid);
		
		aeemployeeidbox = new JTextField();
		aeemployeeidbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				viewSearchEmployee();
			}
		});
		aeemployeeidbox.setColumns(10);
		aeemployeeidbox.setBounds(104, 19, 116, 26);
		aesearchpanel.add(aeemployeeidbox);
		
		JButton aeupdatebutton = new JButton("Update");
		aeupdatebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateEmployee();
				populateeTable();
			}
		});
		aeupdatebutton.setBounds(6, 245, 117, 29);
		aemployeepanel.add(aeupdatebutton);
		
		JButton aedeletebutton = new JButton("Delete");
		aedeletebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteEmployee();
				populateeTable();
			}
		});
		aedeletebutton.setForeground(Color.BLACK);
		aedeletebutton.setBounds(124, 245, 117, 29);
		aemployeepanel.add(aedeletebutton);
		
		JButton aeclearfieldsbutton = new JButton("Clear Fields");
		aeclearfieldsbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearEField();
			}
		});
		aeclearfieldsbutton.setBounds(6, 277, 235, 29);
		aemployeepanel.add(aeclearfieldsbutton);
		
		JScrollPane aescrollPane = new JScrollPane();
		aescrollPane.setBounds(253, 23, 458, 283);
		aemployeepanel.add(aescrollPane);
		
		aemployeetable = new JTable();
		aescrollPane.setViewportView(aemployeetable);
		
		JButton acreateemployeebutton = new JButton("Create New Employee Account");
		acreateemployeebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createEmployeeAccount();
				populateeTable();
			}
		});
		acreateemployeebutton.setBounds(6, 177, 235, 63);
		aemployeepanel.add(acreateemployeebutton);
		
		JPanel aaccountpanel = new JPanel();
		admintabs.addTab("Account Database", null, aaccountpanel, null);
		aaccountpanel.setLayout(null);
		
		JScrollPane aescrollPane_1 = new JScrollPane();
		aescrollPane_1.setBounds(253, 23, 458, 283);
		aaccountpanel.add(aescrollPane_1);
		
		aaccounttable = new JTable();
		aescrollPane_1.setViewportView(aaccounttable);
		
		JPanel aacsearchpanel = new JPanel();
		aacsearchpanel.setLayout(null);
		aacsearchpanel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		aacsearchpanel.setBounds(6, 138, 235, 58);
		aaccountpanel.add(aacsearchpanel);
		
		JLabel aacaccountidbox = new JLabel("Account ID");
		aacaccountidbox.setBounds(16, 24, 86, 16);
		aacsearchpanel.add(aacaccountidbox);
		
		aacaccidbox = new JTextField();
		aacaccidbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				viewSearchAccount();
			}
		});
		aacaccidbox.setColumns(10);
		aacaccidbox.setBounds(104, 19, 113, 26);
		aacsearchpanel.add(aacaccidbox);
		
		JLabel aaccustomerid = new JLabel("Customer ID");
		aaccustomerid.setBounds(16, 23, 86, 16);
		aaccountpanel.add(aaccustomerid);
		
		aaccustomeridbox = new JTextField();
		aaccustomeridbox.setColumns(10);
		aaccustomeridbox.setBounds(99, 18, 130, 26);
		aaccountpanel.add(aaccustomeridbox);
		
		JLabel aacbalance = new JLabel("Balance");
		aacbalance.setBounds(16, 51, 73, 16);
		aaccountpanel.add(aacbalance);
		
		JLabel aacprevtrans = new JLabel("Previous Transaction");
		aacprevtrans.setBounds(16, 79, 141, 16);
		aaccountpanel.add(aacprevtrans);
		
		aacbalbox = new JTextField();
		aacbalbox.setColumns(10);
		aacbalbox.setBounds(99, 46, 130, 26);
		aaccountpanel.add(aacbalbox);
		
		aacprevtransbox = new JTextField();
		aacprevtransbox.setColumns(10);
		aacprevtransbox.setBounds(16, 100, 210, 26);
		aaccountpanel.add(aacprevtransbox);
		
		JButton aacclearfieldsbutton = new JButton("Clear Fields");
		aacclearfieldsbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAField();
			}
		});
		aacclearfieldsbutton.setBounds(6, 259, 235, 29);
		aaccountpanel.add(aacclearfieldsbutton);
		
		JButton aacdeletebutton = new JButton("Delete");
		aacdeletebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteAccount();
				populateacTable();
			}
		});
		aacdeletebutton.setBounds(6, 208, 235, 49);
		aaccountpanel.add(aacdeletebutton);
		
		JLabel welcomeLabel = new JLabel("Welcome, " + AdminLogIn.nameOfUser + "!");
		welcomeLabel.setForeground(SystemColor.controlHighlight);
		welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		welcomeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		welcomeLabel.setBounds(562, 43, 170, 16);
		frame.getContentPane().add(welcomeLabel);
	}
	
	
	public void populatecTable() {
		try {
			Connection connection = Database.connection;
			String query = "SELECT * FROM Customers";
			PreparedStatement stm = connection.prepareStatement(query);
			ResultSet result = stm.executeQuery(query);
			acustomertable.setModel(DbUtils.resultSetToTableModel(result));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void populateeTable() {
		try {
			Connection connection = Database.connection;
			String query = "SELECT * FROM Employees";
			PreparedStatement stm = connection.prepareStatement(query);
			ResultSet result = stm.executeQuery(query);
			aemployeetable.setModel(DbUtils.resultSetToTableModel(result));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void populateacTable() {
		try {
			Connection connection = Database.connection;
			String query = "SELECT * FROM Accounts";
			PreparedStatement stm = connection.prepareStatement(query);
			ResultSet result = stm.executeQuery(query);
			aaccounttable.setModel(DbUtils.resultSetToTableModel(result));
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
	public void viewSearchCustomer() {
		try {
			int id = Integer.parseInt(accustomeridbox.getText());
			
			Connection connection = Database.connection;
			String query = "SELECT c_username, c_password, c_name, c_address FROM Customers where c_id=?"; 
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1,  id);
			ResultSet result = stm.executeQuery();
			
			if(result.next()==true) {
				String username = result.getString(1);
				String password = result.getString(2);
				String name = result.getString(3);
				String address = result.getString(4);
				
				acusernamebox.setText(username);
				acpasswordbox.setText(password);
				acnamebox.setText(name);
				acaddressbox.setText(address);
			}
			else {
				acusernamebox.setText("");
				acpasswordbox.setText("");
				acnamebox.setText("");
				acaddressbox.setText("");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void viewSearchEmployee() {
		try {
			int id = Integer.parseInt(aeemployeeidbox.getText());
			
			Connection connection = Database.connection;
			String query = "SELECT e_username, e_password, banker FROM Employees where e_id=?"; 
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1,  id);
			ResultSet result = stm.executeQuery();
			
			if(result.next()==true) {
				String username = result.getString(1);
				String password = result.getString(2);
				String name = result.getString(3);
				
				aeusernamebox.setText(username);
				aepasswordbox.setText(password);
				aefullnamebox.setText(name);
			}
			else {
				acusernamebox.setText("");
				acpasswordbox.setText("");
				acnamebox.setText("");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void viewSearchAccount() {
		try {
			int id = Integer.parseInt(aacaccidbox.getText());
			
			Connection connection = Database.connection;
			String query = "SELECT c_id, account_balance, account_previousTransaction FROM Accounts where account_id=?"; 
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1,  id);
			ResultSet result = stm.executeQuery();
			
			if(result.next()==true) {
				String customerID = result.getString(1);
				String accountBalance = result.getString(2);
				String accountPrevTrans = result.getString(3);
				
				aaccustomeridbox.setText(customerID);
				aacbalbox.setText(accountBalance);
				aacprevtransbox.setText(accountPrevTrans);
			}
			else {
				aaccustomeridbox.setText("");
				aacbalbox.setText("");
				aacprevtransbox.setText("");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void updateCustomer() {
	    try {
	        int id = Integer.parseInt(accustomeridbox.getText());

	        Connection connection = Database.connection;
	        String checkQuery = "SELECT * FROM Customers WHERE c_id = ?";
	        PreparedStatement checkStm = connection.prepareStatement(checkQuery);
	        checkStm.setInt(1, id);
	        ResultSet rs = checkStm.executeQuery();

	        if (rs.next()) { // the customer record exists, so update it
	            String updateQuery = "UPDATE Customers SET c_username = ?, c_password = ?, c_name = ?, c_address = ? WHERE c_id = ?";
	            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
	            updateStm.setString(1, acusernamebox.getText());
	            updateStm.setString(2, acpasswordbox.getText());
	            updateStm.setString(3, acnamebox.getText());
	            updateStm.setString(4, acaddressbox.getText());
	            updateStm.setInt(5, id);
	            updateStm.executeUpdate();
	            JOptionPane.showMessageDialog(null, "Customer account was updated in the database!", "Customer Account Updated Successfully!", JOptionPane.DEFAULT_OPTION);
	        } else { // the customer record does not exist, so display an error message
	            JOptionPane.showMessageDialog(null, "There is no customer with ID " + id + " in the database!", "Customer Account Not Found!", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void updateEmployee() {
	    try {
	        int id = Integer.parseInt(aeemployeeidbox.getText());
	        
	        Connection connection = Database.connection;
	        String query = "UPDATE Employees SET e_username = ?, e_password = ?, banker = ? WHERE e_id = ?";
	        PreparedStatement stm = connection.prepareStatement(query);
	        stm.setString(1, aeusernamebox.getText());
	        stm.setString(2, aepasswordbox.getText()); 
	        stm.setString(3, aefullnamebox.getText()); 
	        stm.setInt(4, id);
	        int rowsAffected = stm.executeUpdate();
	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(null, " Employee account was updated in the database!", "Employee Account Updated Successfully!", JOptionPane.DEFAULT_OPTION);
	        } else {
	            JOptionPane.showMessageDialog(null, "No employee account with the given ID was found in the database!", "Employee Account Not Found!", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void createEmployeeAccount() {
	    try {
	        Connection connection = Database.connection;
	        
	        // Check for duplicate employee ID
	        String checkQuery = "SELECT COUNT(*) FROM Employees WHERE e_id = ?";
	        PreparedStatement checkStm = connection.prepareStatement(checkQuery);
	        checkStm.setInt(1, Integer.parseInt(aeemployeeidbox.getText()));
	        ResultSet checkResult = checkStm.executeQuery();
	        checkResult.next();
	        int count = checkResult.getInt(1);
	        if (count > 0) {
	            JOptionPane.showMessageDialog(null, "Employee ID already exists. Please choose a different ID.", "Duplicate Employee ID", JOptionPane.ERROR_MESSAGE);
	            return;
	        }
	        
	        // Insert new employee account
	        String insertQuery = "INSERT INTO Employees VALUES (?, ?, ?, ?)";
	        PreparedStatement insertStm = connection.prepareStatement(insertQuery);
	        insertStm.setInt(1, Integer.parseInt(aeemployeeidbox.getText()));
	        insertStm.setString(2, aeusernamebox.getText());
	        insertStm.setString(3, aepasswordbox.getText()); 
	        insertStm.setString(4, aefullnamebox.getText()); 
	        insertStm.executeUpdate();
	        JOptionPane.showMessageDialog(null, "Employee account was added to the database!", "Employee Account Created Successfully!", JOptionPane.DEFAULT_OPTION);
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void clearEField() {
		aeemployeeidbox.setText("");
        aeusernamebox.setText("");
        aepasswordbox.setText("");
        aefullnamebox.setText("");
        aeemployeeidbox.setText("");
	}
	public void clearCField() {
		acusernamebox.setText("");
		acpasswordbox.setText("");
		acnamebox.setText("");
		acaddressbox.setText("");
		accustomeridbox.setText("");
	}
	public void clearAField() {
		aacaccidbox.setText("");
		aaccustomeridbox.setText("");
		aacbalbox.setText("");
		aacprevtransbox.setText("");
	}
	public void deleteEmployee() {
	    try {
	        int id = Integer.parseInt(aeemployeeidbox.getText());
	        
	        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this employee account? This will delete all related records in the database", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
	        if (confirm == JOptionPane.YES_OPTION) {
	            Connection connection = Database.connection;
	            connection.setAutoCommit(false); // start a transaction
	            
	            // delete related records in other tables first
	            String deleteAppointmentsQuery = "DELETE FROM Appointments WHERE e_id = ?";
	            PreparedStatement deleteAppointmentsStm = connection.prepareStatement(deleteAppointmentsQuery);
	            deleteAppointmentsStm.setInt(1, id);
	            deleteAppointmentsStm.executeUpdate();
	            
	            // delete employee account last
	            String deleteEmployeeQuery = "DELETE FROM Employees WHERE e_id = ?";
	            PreparedStatement deleteEmployeeStm = connection.prepareStatement(deleteEmployeeQuery);
	            deleteEmployeeStm.setInt(1, id);
	            int rowsAffected = deleteEmployeeStm.executeUpdate();
	            
	            if (rowsAffected > 0) {
	                connection.commit(); // commit the transaction
	                JOptionPane.showMessageDialog(null, "Employee account and related records were deleted from the database!", "Employee Account Deleted Successfully!", JOptionPane.DEFAULT_OPTION);
	            } else {
	                connection.rollback(); // rollback the transaction
	                JOptionPane.showMessageDialog(null, "Employee account with the given ID was not found in the database!", "Employee Account Not Found!", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void deleteAccount() {
	    try {
	        int id = Integer.parseInt(aacaccidbox.getText());
	        
	        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this account?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
	        if (confirm == JOptionPane.YES_OPTION) {
	            Connection connection = Database.connection;
	            String query = "DELETE FROM Accounts WHERE account_id = ?";
	            PreparedStatement stm = connection.prepareStatement(query);
	            stm.setInt(1, id);
	            int rowsAffected = stm.executeUpdate();
	            
	            if (rowsAffected > 0) {
	                JOptionPane.showMessageDialog(null, "Bank account was deleted from the database!", "Bank Account Deleted Successfully!", JOptionPane.DEFAULT_OPTION);
	            } else {
	                JOptionPane.showMessageDialog(null, "Bank account with the given ID was not found in the database!", "Bank Account Not Found!", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void deleteCustomer() {
	    try {
	        int id = Integer.parseInt(accustomeridbox.getText());

	        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this customer account? This will delete all related records in the database", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
	        if (confirm == JOptionPane.YES_OPTION) {
	            Connection connection = Database.connection;
	            connection.setAutoCommit(false); // start a transaction

	            // delete related records in other tables first
	            String deleteAccountsQuery = "DELETE FROM Accounts WHERE c_id = ?";
	            PreparedStatement deleteAccountsStm = connection.prepareStatement(deleteAccountsQuery);
	            deleteAccountsStm.setInt(1, id);
	            deleteAccountsStm.executeUpdate();

	            String deleteAppointmentsQuery = "DELETE FROM Appointments WHERE c_id = ?";
	            PreparedStatement deleteAppointmentsStm = connection.prepareStatement(deleteAppointmentsQuery);
	            deleteAppointmentsStm.setInt(1, id);
	            deleteAppointmentsStm.executeUpdate();

	            String deleteAccountCreationRequestsQuery = "DELETE FROM AccountCreationRequest WHERE c_id = ?";
	            PreparedStatement deleteAccountCreationRequestsStm = connection.prepareStatement(deleteAccountCreationRequestsQuery);
	            deleteAccountCreationRequestsStm.setInt(1, id);
	            deleteAccountCreationRequestsStm.executeUpdate();

	            // delete customer account last
	            String deleteCustomerQuery = "DELETE FROM Customers WHERE c_id = ?";
	            PreparedStatement deleteCustomerStm = connection.prepareStatement(deleteCustomerQuery);
	            deleteCustomerStm.setInt(1, id);
	            int rowsAffected = deleteCustomerStm.executeUpdate();

	            if (rowsAffected > 0) {
	                connection.commit(); // commit the transaction
	                JOptionPane.showMessageDialog(null, "Customer account and related records were deleted from the database!", "Customer Account Deleted Successfully!", JOptionPane.DEFAULT_OPTION);
	            } else {
	                connection.rollback(); // rollback the transaction
	                JOptionPane.showMessageDialog(null, "Customer account with the given ID was not found in the database!", "Customer Account Not Found!", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	    
	}
}
