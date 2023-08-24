import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import net.proteanit.sql.DbUtils;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.SystemColor;

public class EmployeePage {

	public JFrame frame;
	private JTextField cuserbox;
	private JTextField cnamebox;
	private JTextField caddressbox;
	private JTextField cidbox;
	private JTextField eacaidbox;
	private JTextField eaccidbox;
	private JTextField eacbalancebox;
	private JTextField eacprevtransbox;
	private JTextField eapaidbox;
	private JTextField eapcidbox;
	private JTextField eapdatebox;
	private JTextField ercidbox;
	private JTextField erinitialbalbox;
	private JTextField erridbox;
	private JTextField cbalancebox;
	private JTextField eaptimebox;
	private JTable ecustomertable;
	private JTable eaccountstable;
	private JTable eappointmentstable;
	private JTable erequeststable;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmployeePage window = new EmployeePage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public EmployeePage() {
		initialize();
	}

	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 750, 522);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Database.connect();
		setupClosingDBConnection();
		
		createLogo();
		createBankTitle();
		createLogoutButton();
		createEmployeeTabs();
		populatecTable();
		populateacTable();
		populateappTable();
		populatereqTable();
		
	}
	public static void setupClosingDBConnection() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            try { Database.connection.close(); System.out.println("Application Closed - DB Connection Closed");
				} catch (SQLException e) { e.printStackTrace(); }
	        }
	    }, "Shutdown-thread"));
	}
	public void createLogo() {
		JLabel hlogo = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/baglogo.png")).getImage();
		hlogo.setIcon(new ImageIcon(img));
		hlogo.setBounds(202, 6, 48, 53);
		frame.getContentPane().add(hlogo);
	}
	public void createBankTitle() {
		JLabel hBankTitle = new JLabel("Bank It Up!");
		hBankTitle.setVerticalAlignment(SwingConstants.BOTTOM);
		hBankTitle.setHorizontalAlignment(SwingConstants.CENTER);
		hBankTitle.setForeground(Color.GRAY);
		hBankTitle.setFont(new Font("Rockwell", Font.BOLD, 30));
		hBankTitle.setBounds(6, 6, 202, 53);
		frame.getContentPane().add(hBankTitle);
	}
	public void createEmployeePageText() {
		JLabel eEmployeePageText = new JLabel("Employee Page");
		eEmployeePageText.setHorizontalAlignment(SwingConstants.RIGHT);
		eEmployeePageText.setForeground(Color.DARK_GRAY);
		eEmployeePageText.setFont(new Font("Sana", Font.PLAIN, 25));
		eEmployeePageText.setBounds(432, 24, 190, 27);
		frame.getContentPane().add(eEmployeePageText);
	}
	public void createLogoutButton() {
		JButton logoutbutton = new JButton("Logout");
		logoutbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToHomePage();
			}
		});
		logoutbutton.setBounds(542, 448, 190, 29);
		frame.getContentPane().add(logoutbutton);
	}
	public void createEmployeeTabs() {
		JTabbedPane employeetabs = new JTabbedPane(JTabbedPane.TOP);
		employeetabs.setBounds(6, 72, 738, 381);
		frame.getContentPane().add(employeetabs);
		
		JPanel acustomerpanel = new JPanel();
		acustomerpanel.setLayout(null);
		acustomerpanel.setBorder(null);
		employeetabs.addTab("Customer Database", null, acustomerpanel, null);
		
		JLabel acusername = new JLabel("Username");
		acusername.setBounds(16, 23, 73, 16);
		acustomerpanel.add(acusername);
		
		JLabel acname = new JLabel("Full Name");
		acname.setBounds(16, 71, 73, 16);
		acustomerpanel.add(acname);
		
		JLabel acaddress = new JLabel("Address");
		acaddress.setBounds(16, 107, 73, 16);
		acustomerpanel.add(acaddress);
		
		cuserbox = new JTextField();
		cuserbox.setColumns(10);
		cuserbox.setBounds(86, 18, 141, 26);
		acustomerpanel.add(cuserbox);
		
		cnamebox = new JTextField();
		cnamebox.setColumns(10);
		cnamebox.setBounds(86, 66, 141, 26);
		acustomerpanel.add(cnamebox);
		
		caddressbox = new JTextField();
		caddressbox.setColumns(10);
		caddressbox.setBounds(16, 131, 211, 46);
		acustomerpanel.add(caddressbox);
		
		JPanel acsearchpanel = new JPanel();
		acsearchpanel.setLayout(null);
		acsearchpanel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		acsearchpanel.setBounds(6, 227, 235, 58);
		acustomerpanel.add(acsearchpanel);
		
		JLabel accustomerid = new JLabel("Customer ID");
		accustomerid.setBounds(16, 24, 86, 16);
		acsearchpanel.add(accustomerid);
		
		cidbox = new JTextField();
		cidbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				viewSearchCustomer();
			}
		});
		cidbox.setColumns(10);
		cidbox.setBounds(104, 19, 116, 26);
		acsearchpanel.add(cidbox);
		
		JButton ecclearfieldsbutton = new JButton("Clear Fields");
		ecclearfieldsbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearCField();
			}
		});
		ecclearfieldsbutton.setBounds(6, 288, 235, 29);
		acustomerpanel.add(ecclearfieldsbutton);
		
		JScrollPane acscrollPane = new JScrollPane();
		acscrollPane.setBounds(253, 23, 458, 283);
		acustomerpanel.add(acscrollPane);
		
		ecustomertable = new JTable();
		acscrollPane.setViewportView(ecustomertable);
		
		JLabel totalbalance = new JLabel("Total Balance");
		totalbalance.setBounds(16, 189, 89, 16);
		acustomerpanel.add(totalbalance);
		
		cbalancebox = new JTextField();
		cbalancebox.setColumns(10);
		cbalancebox.setBounds(107, 184, 120, 26);
		acustomerpanel.add(cbalancebox);
		
		JPanel accountspanel = new JPanel();
		accountspanel.setLayout(null);
		employeetabs.addTab("Accounts", null, accountspanel, null);
		
		JScrollPane aeaccountsscrollPane = new JScrollPane();
		aeaccountsscrollPane.setBounds(253, 23, 458, 283);
		accountspanel.add(aeaccountsscrollPane);
		
		eaccountstable = new JTable();
		aeaccountsscrollPane.setViewportView(eaccountstable);
		
		JPanel aacsearchpanel = new JPanel();
		aacsearchpanel.setLayout(null);
		aacsearchpanel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		aacsearchpanel.setBounds(6, 138, 235, 58);
		accountspanel.add(aacsearchpanel);
		
		JLabel aacaccountidbox = new JLabel("Account ID");
		aacaccountidbox.setBounds(16, 24, 86, 16);
		aacsearchpanel.add(aacaccountidbox);
		
		eacaidbox = new JTextField();
		eacaidbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				viewSearchAccount();
			}
		});
		eacaidbox.setColumns(10);
		eacaidbox.setBounds(104, 19, 113, 26);
		aacsearchpanel.add(eacaidbox);
		
		JLabel aaccustomerid = new JLabel("Customer ID");
		aaccustomerid.setBounds(16, 23, 86, 16);
		accountspanel.add(aaccustomerid);
		
		eaccidbox = new JTextField();
		eaccidbox.setColumns(10);
		eaccidbox.setBounds(99, 18, 130, 26);
		accountspanel.add(eaccidbox);
		
		JLabel aacbalance = new JLabel("Balance");
		aacbalance.setBounds(16, 51, 73, 16);
		accountspanel.add(aacbalance);
		
		JLabel aacprevtrans = new JLabel("Previous Transaction");
		aacprevtrans.setBounds(16, 79, 141, 16);
		accountspanel.add(aacprevtrans);
		
		eacbalancebox = new JTextField();
		eacbalancebox.setColumns(10);
		eacbalancebox.setBounds(99, 46, 130, 26);
		accountspanel.add(eacbalancebox);
		
		eacprevtransbox = new JTextField();
		eacprevtransbox.setColumns(10);
		eacprevtransbox.setBounds(16, 100, 210, 26);
		accountspanel.add(eacprevtransbox);
		
		JButton eacclearfieldsbutton = new JButton("Clear Fields");
		eacclearfieldsbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAcField();
			}
		});
		eacclearfieldsbutton.setBounds(6, 259, 235, 29);
		accountspanel.add(eacclearfieldsbutton);
		
		JButton aacdeletebutton = new JButton("Delete");
		aacdeletebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteAccount();
				populateacTable();
			}
		});
		aacdeletebutton.setBounds(6, 208, 235, 49);
		accountspanel.add(aacdeletebutton);
		
		JPanel appointmentspanel = new JPanel();
		appointmentspanel.setLayout(null);
		employeetabs.addTab("Appointments", null, appointmentspanel, null);
		
		JScrollPane aeappointmentscrollPane = new JScrollPane();
		aeappointmentscrollPane.setBounds(253, 23, 458, 283);
		appointmentspanel.add(aeappointmentscrollPane);
		
		eappointmentstable = new JTable();
		aeappointmentscrollPane.setViewportView(eappointmentstable);
		
		JPanel appointmentsearchpanel = new JPanel();
		appointmentsearchpanel.setLayout(null);
		appointmentsearchpanel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		appointmentsearchpanel.setBounds(6, 138, 235, 58);
		appointmentspanel.add(appointmentsearchpanel);
		
		JLabel appointmentidtext = new JLabel("Appointment ID");
		appointmentidtext.setBounds(16, 24, 103, 16);
		appointmentsearchpanel.add(appointmentidtext);
		
		eapaidbox = new JTextField();
		eapaidbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				viewSearchAppointments();
			}
		});
		eapaidbox.setColumns(10);
		eapaidbox.setBounds(131, 19, 86, 26);
		appointmentsearchpanel.add(eapaidbox);
		
		JLabel customeridtext = new JLabel("Customer ID");
		customeridtext.setBounds(16, 23, 86, 16);
		appointmentspanel.add(customeridtext);
		
		eapcidbox = new JTextField();
		eapcidbox.setColumns(10);
		eapcidbox.setBounds(99, 18, 130, 26);
		appointmentspanel.add(eapcidbox);
		
		JLabel datetext = new JLabel("Date");
		datetext.setBounds(16, 51, 73, 16);
		appointmentspanel.add(datetext);
		
		eapdatebox = new JTextField();
		eapdatebox.setColumns(10);
		eapdatebox.setBounds(99, 46, 130, 26);
		appointmentspanel.add(eapdatebox);
		
		JButton eappclearfieldsbutton = new JButton("Clear Fields");
		eappclearfieldsbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAppField();
			}
		});
		eappclearfieldsbutton.setBounds(6, 259, 235, 29);
		appointmentspanel.add(eappclearfieldsbutton);
		
		JButton deleteandresolvebutton = new JButton("Delete and Resolve");
		deleteandresolvebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteAppointment();
				populateappTable();
			}
		});
		deleteandresolvebutton.setBounds(6, 208, 235, 49);
		appointmentspanel.add(deleteandresolvebutton);
		
		eaptimebox = new JTextField();
		eaptimebox.setColumns(10);
		eaptimebox.setBounds(99, 74, 130, 26);
		appointmentspanel.add(eaptimebox);
		
		JLabel timetext = new JLabel("Time");
		timetext.setBounds(16, 79, 73, 16);
		appointmentspanel.add(timetext);
		
		JPanel requestspanel = new JPanel();
		requestspanel.setLayout(null);
		requestspanel.setBorder(null);
		employeetabs.addTab("Requests", null, requestspanel, null);
		
		JLabel rcustomeridtext = new JLabel("Customer ID");
		rcustomeridtext.setBounds(16, 23, 101, 16);
		requestspanel.add(rcustomeridtext);
		
		JLabel rinitialbalancetext = new JLabel("Initial Balance");
		rinitialbalancetext.setBounds(16, 65, 101, 16);
		requestspanel.add(rinitialbalancetext);
		
		ercidbox = new JTextField();
		ercidbox.setColumns(10);
		ercidbox.setBounds(129, 18, 98, 26);
		requestspanel.add(ercidbox);
		
		erinitialbalbox = new JTextField();
		erinitialbalbox.setColumns(10);
		erinitialbalbox.setBounds(129, 60, 98, 26);
		requestspanel.add(erinitialbalbox);
		
		JPanel acsearchpanel_1 = new JPanel();
		acsearchpanel_1.setLayout(null);
		acsearchpanel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		acsearchpanel_1.setBounds(6, 112, 235, 58);
		requestspanel.add(acsearchpanel_1);
		
		JLabel requestidtext = new JLabel("Request ID");
		requestidtext.setBounds(16, 24, 86, 16);
		acsearchpanel_1.add(requestidtext);
		
		erridbox = new JTextField();
		erridbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				viewSearchRequests();
			}
		});
		erridbox.setColumns(10);
		erridbox.setBounds(104, 19, 116, 26);
		acsearchpanel_1.add(erridbox);
		
		JButton acupdatebutton_1 = new JButton("Accept");
		acupdatebutton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				acceptRequest();
				populatereqTable();
				populateacTable();
			}
		});
		acupdatebutton_1.setBounds(6, 207, 117, 58);
		requestspanel.add(acupdatebutton_1);
		
		JButton acdeletebutton_1 = new JButton("Reject");
		acdeletebutton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rejectRequest();
				populatereqTable();
			}
		});
		acdeletebutton_1.setForeground(Color.BLACK);
		acdeletebutton_1.setBounds(124, 204, 117, 61);
		requestspanel.add(acdeletebutton_1);
		
		JButton ereqclearfieldsbutton = new JButton("Clear Fields");
		ereqclearfieldsbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearReqField();
			}
		});
		ereqclearfieldsbutton.setBounds(6, 267, 235, 29);
		requestspanel.add(ereqclearfieldsbutton);
		
		JScrollPane aerequestscrollPane = new JScrollPane();
		aerequestscrollPane.setBounds(253, 23, 458, 283);
		requestspanel.add(aerequestscrollPane);
		
		erequeststable = new JTable();
		aerequestscrollPane.setViewportView(erequeststable);
		
		JLabel EmployeePage = new JLabel("Employee Page");
		EmployeePage.setHorizontalAlignment(SwingConstants.RIGHT);
		EmployeePage.setForeground(Color.DARK_GRAY);
		EmployeePage.setFont(new Font("Sana", Font.PLAIN, 25));
		EmployeePage.setBounds(542, 20, 190, 27);
		frame.getContentPane().add(EmployeePage);
		
		JLabel welcomeLabel = new JLabel("Welcome, " + EmployeeLogIn.nameOfUser + "!");
		welcomeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		welcomeLabel.setForeground(SystemColor.controlHighlight);
		welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
		welcomeLabel.setBounds(562, 43, 170, 16);
		frame.getContentPane().add(welcomeLabel);
	}
	public void goToHomePage() {
		frame.dispose();
		BankApp BAP = new BankApp(); 
		BAP.initialize();
		BAP.frame.setVisible(true);
	}
	public void populatecTable() {
		try {
			Connection connection = Database.connection;
			String query = "SELECT c_id, c_username, c_name, c_address FROM Customers";
			PreparedStatement stm = connection.prepareStatement(query);
			ResultSet result = stm.executeQuery(query);
			ecustomertable.setModel(DbUtils.resultSetToTableModel(result));
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
			eaccountstable.setModel(DbUtils.resultSetToTableModel(result));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void populateappTable() {
	    try {
	        Connection connection = Database.connection;
	        String query = "SELECT * FROM Appointments WHERE e_id = ?";
	        PreparedStatement stm = connection.prepareStatement(query);
	        stm.setInt(1, EmployeeLogIn.IDOfUser);
	        ResultSet result = stm.executeQuery(); // pass the prepared statement to executeQuery
	        eappointmentstable.setModel(DbUtils.resultSetToTableModel(result));
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void populatereqTable() {
		try {
			Connection connection = Database.connection;
			String query = "SELECT * FROM AccountCreationRequest";
			PreparedStatement stm = connection.prepareStatement(query);
			ResultSet result = stm.executeQuery(query);
			erequeststable.setModel(DbUtils.resultSetToTableModel(result));
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void clearCField() {
		cuserbox.setText("");
        cnamebox.setText("");
        caddressbox.setText("");
        cbalancebox.setText("");
        cidbox.setText("");
	}
	public void clearAcField() {
		eaccidbox.setText("");
		eacbalancebox.setText("");
		eacprevtransbox.setText("");
		eacaidbox.setText("");
	}
	public void clearAppField() {
		eapcidbox.setText("");
		eapdatebox.setText("");
		eaptimebox.setText("");
		eapaidbox.setText("");
	}
	public void clearReqField() {
		ercidbox.setText("");
		erinitialbalbox.setText("");
		erridbox.setText("");
	}
	public void viewSearchCustomer() {
		try {
			int id = Integer.parseInt(cidbox.getText());
			
			Connection connection = Database.connection;
			String query = "SELECT c_username, c_name, c_address FROM Customers where c_id=?"; 
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1,  id);
			ResultSet result = stm.executeQuery();
			
			if(result.next()==true) {
				String username = result.getString(1);
				String name = result.getString(2);
				String address = result.getString(3);
				
				cuserbox.setText(username);
				cnamebox.setText(name);
				caddressbox.setText(address);
			}
			else {
				cuserbox.setText("");
				cnamebox.setText("");
				caddressbox.setText("");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void viewSearchAccount() {
		try {
			int id = Integer.parseInt(eacaidbox.getText());
			
			Connection connection = Database.connection;
			String query = "SELECT c_id, account_balance, account_previousTransaction FROM Accounts where account_id=?"; 
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1,  id);
			ResultSet result = stm.executeQuery();
			
			if(result.next()==true) {
				String customerid = result.getString(1);
				String balance = result.getString(2);
				String prevtrans = result.getString(3);
				
				eaccidbox.setText(customerid);
				eacbalancebox.setText(balance);
				eacprevtransbox.setText(prevtrans);
			}
			else {
				eaccidbox.setText("");
				eacbalancebox.setText("");
				eacprevtransbox.setText("");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void viewSearchAppointments() {
		try {
			int id = Integer.parseInt(eapaidbox.getText());
			
			Connection connection = Database.connection;
			String query = "SELECT app_date, app_time, c_id FROM Appointments where app_id=?"; 
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1,  id);
			ResultSet result = stm.executeQuery();
			
			if(result.next()==true) {
				String appdate = result.getString(1);
				String apptime = result.getString(2);
				String cid = result.getString(3);
				
				eapdatebox.setText(appdate);
				eaptimebox.setText(apptime);
				eapcidbox.setText(cid);
			}
			else {
				eapdatebox.setText("");
				eaptimebox.setText("");
				eapcidbox.setText("");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void viewSearchRequests() {
		try {
			int id = Integer.parseInt(erridbox.getText());
			
			Connection connection = Database.connection;
			String query = "SELECT c_id, initial_balance FROM AccountCreationRequest where req_id=?"; 
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1,  id);
			ResultSet result = stm.executeQuery();
			
			if(result.next()==true) {
				String cid = result.getString(1);
				String initialbal = result.getString(2);
				
				ercidbox.setText(cid);
				erinitialbalbox.setText(initialbal);
			}
			else {
				ercidbox.setText("");
				erinitialbalbox.setText("");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public void deleteAccount() {
	    try {
	        int id = Integer.parseInt(eacaidbox.getText());
	        
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
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void deleteAppointment() {
	    try {
	        int id = Integer.parseInt(eapaidbox.getText());
	        
	        Connection connection = Database.connection;
	        String query = "DELETE FROM Appointments WHERE app_id = ?";
	        PreparedStatement stm = connection.prepareStatement(query);
	        stm.setInt(1, id);
	        int rowsAffected = stm.executeUpdate();
	        
	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(null, "Appointment was removed from the database!", "Appointment Removed Successfully!", JOptionPane.DEFAULT_OPTION);
	        } else {
	            JOptionPane.showMessageDialog(null, "Appointment does not exist", "Appointment Not Found!", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	//////////////////////////////////---------Request panel code-----------//////////////////////////////////
	public void rejectRequest() {
	    try {
	        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to reject this account request?", "Confirmation", JOptionPane.YES_NO_OPTION);
	        if (confirmation == JOptionPane.YES_OPTION) {
	            Connection connection = Database.connection;
	            String updateQuery = "UPDATE AccountCreationRequest SET request_status = ? WHERE req_id = ?";
	            PreparedStatement stm = connection.prepareStatement(updateQuery);
	            stm.setString(1, "Rejected");
	            stm.setInt(2, Integer.parseInt(erridbox.getText()));
	            int rowsAffected = stm.executeUpdate();
	            if (rowsAffected > 0) {
	                JOptionPane.showMessageDialog(null, "Account request rejected", "Notification", JOptionPane.DEFAULT_OPTION);
	            } else {
	                JOptionPane.showMessageDialog(null, "An error occured. Please try again", "Account Rejection Unsuccessful", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void acceptRequest() {
	    try {
	        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to accept this account request?", "Confirmation", JOptionPane.YES_NO_OPTION);
	        if (confirmation == JOptionPane.YES_OPTION) {
	            Connection connection = Database.connection;
	            String selectQuery = "SELECT initial_balance, c_id FROM AccountCreationRequest WHERE req_id = ?";
	            PreparedStatement selectStm = connection.prepareStatement(selectQuery);
	            selectStm.setInt(1, Integer.parseInt(erridbox.getText()));
	            ResultSet rs = selectStm.executeQuery();
	            if (rs.next()) {
	                int initialBalance = rs.getInt(1);
	                int customerId = rs.getInt(2);
	                String insertQuery = "INSERT INTO Accounts (account_balance, c_id, account_previousTransaction) VALUES (?, ?, ?)";
	                PreparedStatement insertStm = connection.prepareStatement(insertQuery);
	                insertStm.setInt(1, initialBalance);
	                insertStm.setInt(2, customerId);
	                insertStm.setInt(3, initialBalance);
	                int rowsInserted = insertStm.executeUpdate();
	                if (rowsInserted > 0) {
	                    String updateQuery = "UPDATE AccountCreationRequest SET request_status = ? WHERE req_id = ?";
	                    PreparedStatement updateStm = connection.prepareStatement(updateQuery);
	                    updateStm.setString(1, "Accepted");
	                    updateStm.setInt(2, Integer.parseInt(erridbox.getText()));
	                    int rowsAffected = updateStm.executeUpdate();
	                    if (rowsAffected > 0) {
	                        JOptionPane.showMessageDialog(null, "Account request accepted!", "Notification", JOptionPane.DEFAULT_OPTION);
	                    } else {
	                        JOptionPane.showMessageDialog(null, "An error occurred while updating the account request.", "Error", JOptionPane.ERROR_MESSAGE);
	                    }
	                } else {
	                    JOptionPane.showMessageDialog(null, "An error occurred while inserting the account details.", "Error", JOptionPane.ERROR_MESSAGE);
	                }
	            } else {
	                JOptionPane.showMessageDialog(null, "An account request with the given ID was not found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}

     	
}

