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
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import net.proteanit.sql.DbUtils;

import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class CustomerPage {

	public JFrame frame;
	private JTextField aaccidbox;
	private JTextField abalancebox;
	private JTextField aprevtransbox;
	private JTextField pusernamebox;
	private JTextField ppasswordbox;
	private JTextField pnamebox;
	private JTextField paddressbox;
	private JTextField apappidbox;
	private JTextField apdatebox;
	private JTextField aptimebox;
	private JTextField rinitialdepbox;
	private JTextField rreqidbox;
	private JTextField atotbalbox;
	private JTextField aamountbox;
	private JTable acaccounttable;
	private JTable acappointmenttable;
	private JTable acrequesttable;
	private static JComboBox<String> bankerCB;
	static DefaultComboBoxModel<String> bankerCBModel = new DefaultComboBoxModel<String>();
	private Map<String, Integer> bankerIDMap;


	public void ComboBox() { initialize(); }

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomerPage window = new CustomerPage();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CustomerPage() {
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
		createCustomerPageText();
		createCustomerTabs();
		createLogoutButton();
		createLogo();
		populateacTable();
		populateappTable();
		populatereqTable();
		viewCustomerProfile();
		viewTotalBalance();
		
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
	public void createCustomerPageText() {
		JLabel lblCustomerPage = new JLabel("Customer Page");
		lblCustomerPage.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCustomerPage.setForeground(Color.DARK_GRAY);
		lblCustomerPage.setFont(new Font("Sana", Font.PLAIN, 25));
		lblCustomerPage.setBounds(542, 20, 190, 27);
		frame.getContentPane().add(lblCustomerPage);
	}
	public void createCustomerTabs() {
		JTabbedPane customertabs = new JTabbedPane(JTabbedPane.TOP);
		customertabs.setBounds(6, 72, 738, 381);
		frame.getContentPane().add(customertabs);
		
		JPanel cprofiletab = new JPanel();
		cprofiletab.setLayout(null);
		cprofiletab.setBorder(null);
		customertabs.addTab("Profile", null, cprofiletab, null);
		
		JLabel acusername = new JLabel("Username");
		acusername.setBounds(150, 73, 73, 16);
		cprofiletab.add(acusername);
		
		JLabel acpassword = new JLabel("Password");
		acpassword.setBounds(150, 101, 73, 16);
		cprofiletab.add(acpassword);
		
		JLabel acname = new JLabel("Full Name");
		acname.setBounds(150, 129, 73, 16);
		cprofiletab.add(acname);
		
		JLabel acaddress = new JLabel("Address");
		acaddress.setBounds(150, 197, 73, 16);
		cprofiletab.add(acaddress);
		
		pusernamebox = new JTextField();
		pusernamebox.setColumns(10);
		pusernamebox.setBounds(250, 68, 211, 26);
		cprofiletab.add(pusernamebox);
		
		ppasswordbox = new JTextField();
		ppasswordbox.setColumns(10);
		ppasswordbox.setBounds(250, 96, 211, 26);
		cprofiletab.add(ppasswordbox);
		
		pnamebox = new JTextField();
		pnamebox.setColumns(10);
		pnamebox.setBounds(250, 124, 211, 26);
		cprofiletab.add(pnamebox);
		
		paddressbox = new JTextField();
		paddressbox.setColumns(10);
		paddressbox.setBounds(250, 181, 211, 46);
		cprofiletab.add(paddressbox);
		
		JButton acupdatebutton = new JButton("Update");
		acupdatebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateCustomerProfile();
			}
		});
		acupdatebutton.setBounds(240, 250, 235, 29);
		cprofiletab.add(acupdatebutton);
		
		JLabel WelcomeLabel = new JLabel(CustomerLogIn.nameOfUser + "'s Profile");
		WelcomeLabel.setBounds(230, 26, 211, 16);
		cprofiletab.add(WelcomeLabel);
		
		JPanel caccounttab = new JPanel();
		caccounttab.setLayout(null);
		customertabs.addTab("Account", null, caccounttab, null);
		
		JScrollPane aaccountscrollPane = new JScrollPane();
		aaccountscrollPane.setBounds(253, 23, 458, 283);
		caccounttab.add(aaccountscrollPane);
		
		acaccounttable = new JTable();
		aaccountscrollPane.setViewportView(acaccounttable);
		
		JPanel aacsearchpanel = new JPanel();
		aacsearchpanel.setLayout(null);
		aacsearchpanel.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		aacsearchpanel.setBounds(6, 87, 235, 58);
		caccounttab.add(aacsearchpanel);
		
		JLabel aacaccountidbox = new JLabel("Account ID");
		aacaccountidbox.setBounds(16, 24, 86, 16);
		aacsearchpanel.add(aacaccountidbox);
		
		aaccidbox = new JTextField();
		aaccidbox.setColumns(10);
		aaccidbox.setBounds(104, 19, 113, 26);
		aacsearchpanel.add(aaccidbox);
		
		JLabel aacbalance = new JLabel("Balance");
		aacbalance.setBounds(16, 23, 73, 16);
		caccounttab.add(aacbalance);
		
		JLabel aacprevtrans = new JLabel("Previous Transaction");
		aacprevtrans.setBounds(16, 42, 141, 16);
		caccounttab.add(aacprevtrans);
		
		abalancebox = new JTextField();
		abalancebox.setColumns(10);
		abalancebox.setBounds(101, 16, 130, 26);
		caccounttab.add(abalancebox);
		
		aprevtransbox = new JTextField();
		aprevtransbox.setColumns(10);
		aprevtransbox.setBounds(21, 60, 210, 26);
		caccounttab.add(aprevtransbox);
		
		JButton aacclearfieldsbutton = new JButton("Clear Fields");
		aacclearfieldsbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAccField();
			}
		});
		aacclearfieldsbutton.setBounds(6, 222, 235, 29);
		caccounttab.add(aacclearfieldsbutton);
		
		JLabel totalbalancetext = new JLabel("Total Balance");
		totalbalancetext.setBounds(16, 255, 141, 16);
		caccounttab.add(totalbalancetext);
		
		atotbalbox = new JTextField();
		atotbalbox.setColumns(10);
		atotbalbox.setBounds(21, 280, 210, 26);
		caccounttab.add(atotbalbox);
		
		JLabel aacamount = new JLabel("Amount");
		aacamount.setBounds(16, 150, 73, 16);
		caccounttab.add(aacamount);
		
		aamountbox = new JTextField();
		aamountbox.setColumns(10);
		aamountbox.setBounds(101, 145, 130, 26);
		caccounttab.add(aamountbox);
		
		JButton acwithdrawbutton = new JButton("Withdraw");
		acwithdrawbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateAccountWithdrawal();
				populateacTable();
			}
		});
		acwithdrawbutton.setBounds(6, 178, 107, 39);
		caccounttab.add(acwithdrawbutton);
		
		JButton acdepositbutton = new JButton("Deposit");
		acdepositbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateAccountDeposit();
				populateacTable();
			}
		});
		acdepositbutton.setBounds(124, 178, 107, 39);
		caccounttab.add(acdepositbutton);
		
		JPanel cappointmentstab = new JPanel();
		cappointmentstab.setLayout(null);
		customertabs.addTab("Appointments", null, cappointmentstab, null);
		
		JScrollPane acappointmentscrollPane = new JScrollPane();
		acappointmentscrollPane.setBounds(253, 23, 458, 283);
		cappointmentstab.add(acappointmentscrollPane);
		
		acappointmenttable = new JTable();
		acappointmentscrollPane.setViewportView(acappointmenttable);
		
		JPanel aacsearchpanel_1 = new JPanel();
		aacsearchpanel_1.setLayout(null);
		aacsearchpanel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		aacsearchpanel_1.setBounds(6, 142, 235, 58);
		cappointmentstab.add(aacsearchpanel_1);
		
		JLabel aappointmentidtext = new JLabel("Appointment ID");
		aappointmentidtext.setBounds(16, 24, 100, 16);
		aacsearchpanel_1.add(aappointmentidtext);
		
		apappidbox = new JTextField();
		apappidbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				viewAppointments();
			}
		});
		apappidbox.setColumns(10);
		apappidbox.setBounds(125, 19, 92, 26);
		aacsearchpanel_1.add(apappidbox);
		
		JLabel datetext = new JLabel("Date (mm/dd/yyyy)");
		datetext.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		datetext.setBounds(16, 23, 94, 16);
		cappointmentstab.add(datetext);
		
		apdatebox = new JTextField();
		apdatebox.setColumns(10);
		apdatebox.setBounds(112, 18, 117, 26);
		cappointmentstab.add(apdatebox);
		
		JLabel timetext = new JLabel("Time (hh:mm AM/PM)");
		timetext.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		timetext.setBounds(16, 67, 107, 16);
		cappointmentstab.add(timetext);
		
		aptimebox = new JTextField();
		aptimebox.setColumns(10);
		aptimebox.setBounds(135, 61, 94, 26);
		cappointmentstab.add(aptimebox);
		
		JButton aacclearfieldsbutton_1 = new JButton("Clear Fields");
		aacclearfieldsbutton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAppField();
			}
		});
		aacclearfieldsbutton_1.setBounds(6, 277, 235, 29);
		cappointmentstab.add(aacclearfieldsbutton_1);
		
		JButton accancelappointmentbutton = new JButton("Cancel Appointment");
		accancelappointmentbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteAppointment();
				populateappTable();
			}
		});
		accancelappointmentbutton.setBounds(6, 237, 235, 39);
		cappointmentstab.add(accancelappointmentbutton);
		
		JLabel bankertext = new JLabel("Choose Banker");
		bankertext.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		bankertext.setBounds(16, 103, 94, 16);
		cappointmentstab.add(bankertext);
		
		bankerCB = new JComboBox<String>();
		bankerCB.setBounds(112, 99, 117, 27);
		cappointmentstab.add(bankerCB);
		populateComboBox();
		
		JButton acrequestappointmentbutton = new JButton("Request Appointment");
		acrequestappointmentbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createAppointment();
				populateappTable();
			}
		});
		acrequestappointmentbutton.setBounds(6, 198, 235, 39);
		cappointmentstab.add(acrequestappointmentbutton);
		
		JPanel crequeststab = new JPanel();
		crequeststab.setLayout(null);
		crequeststab.setBorder(null);
		customertabs.addTab("Requests", null, crequeststab, null);
		
		JLabel rinitialbalancetext = new JLabel("Initial Deposit");
		rinitialbalancetext.setBounds(16, 51, 101, 16);
		crequeststab.add(rinitialbalancetext);
		
		rinitialdepbox = new JTextField();
		rinitialdepbox.setColumns(10);
		rinitialdepbox.setBounds(129, 46, 98, 26);
		crequeststab.add(rinitialdepbox);
		
		JPanel acsearchpanel_1 = new JPanel();
		acsearchpanel_1.setLayout(null);
		acsearchpanel_1.setBorder(new TitledBorder(null, "Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		acsearchpanel_1.setBounds(6, 101, 235, 58);
		crequeststab.add(acsearchpanel_1);
		
		JLabel requestidtext = new JLabel("Request ID");
		requestidtext.setBounds(16, 24, 86, 16);
		acsearchpanel_1.add(requestidtext);
		
		rreqidbox = new JTextField();
		rreqidbox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				viewSearchReq();
			}
		});
		rreqidbox.setColumns(10);
		rreqidbox.setBounds(104, 19, 116, 26);
		acsearchpanel_1.add(rreqidbox);
		
		JButton acupdatebutton_1 = new JButton("Update");
		acupdatebutton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateRequest();
				populatereqTable();
			}
		});
		acupdatebutton_1.setBounds(6, 206, 117, 29);
		crequeststab.add(acupdatebutton_1);
		
		JButton rcancelbutton = new JButton("Cancel");
		rcancelbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteRequest();
				populatereqTable();
			}
		});
		rcancelbutton.setForeground(Color.BLACK);
		rcancelbutton.setBounds(124, 206, 117, 29);
		crequeststab.add(rcancelbutton);
		
		JButton acclearfieldsbutton_1 = new JButton("Clear Fields");
		acclearfieldsbutton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearReqField();
			}
		});
		acclearfieldsbutton_1.setBounds(6, 237, 235, 29);
		crequeststab.add(acclearfieldsbutton_1);
		
		JScrollPane acrequestscrollPane = new JScrollPane();
		acrequestscrollPane.setBounds(253, 23, 458, 283);
		crequeststab.add(acrequestscrollPane);
		
		acrequesttable = new JTable();
		acrequestscrollPane.setViewportView(acrequesttable);
		
		JButton acrequestaccount = new JButton("Request New Bank Account");
		acrequestaccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createRequest();
				populatereqTable();
			}
		});
		acrequestaccount.setBounds(6, 160, 235, 44);
		crequeststab.add(acrequestaccount);
	}
	public void createLogoutButton() {
		JButton clogoutbutton = new JButton("Logout");
		clogoutbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				goToHomePage();
			}
		});
		clogoutbutton.setBounds(542, 448, 190, 29);
		frame.getContentPane().add(clogoutbutton);
	}
	public void createLogo() {
		JLabel hlogo = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/baglogo.png")).getImage();
		hlogo.setIcon(new ImageIcon(img));
		hlogo.setBounds(202, 6, 48, 53);
		frame.getContentPane().add(hlogo);
		
		JLabel welcomeLabel = new JLabel("Welcome, " + CustomerLogIn.nameOfUser + "!");
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
	//////////////////////////////////---------tables code-----------//////////////////////////////////
	public void populateacTable() {
	    try {
	        Connection connection = Database.connection;
	        String query = "SELECT * FROM Accounts WHERE c_id=?"; 
	        PreparedStatement stm = connection.prepareStatement(query);
	        stm.setInt(1, CustomerLogIn.IDOfUser);
	        ResultSet result = stm.executeQuery();
	        acaccounttable.setModel(DbUtils.resultSetToTableModel(result));
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void populateappTable() {
	    try {
	        Connection connection = Database.connection;
	        String query = "SELECT Appointments.app_id, Appointments.app_date, Appointments.app_time, Employees.banker " +
	                       "FROM Appointments " +
	                       "INNER JOIN Employees ON Appointments.e_id = Employees.e_id " +
	                       "WHERE Appointments.c_id=?";
	        PreparedStatement stm = connection.prepareStatement(query);
	        stm.setInt(1, CustomerLogIn.IDOfUser);
	        ResultSet result = stm.executeQuery();
	        
	        String[] columnNames = {"Appointment ID", "Date", "Time", "Banker"};
	        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	        while (result.next()) {
	            int appID = result.getInt("app_id");
	            String appDate = result.getString("app_date");
	            String appTime = result.getString("app_time");
	            String banker = result.getString("banker");
	            Object[] rowData = {appID, appDate, appTime, banker};
	            tableModel.addRow(rowData);
	        }
	        acappointmenttable.setModel(tableModel);
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void populatereqTable() {
		try {
			Connection connection = Database.connection;
			String query = "SELECT * FROM AccountCreationRequest WHERE c_id=?";
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1, CustomerLogIn.IDOfUser);
			ResultSet result = stm.executeQuery();
			
			 String[] columnNames = {"Request ID", "Initial Deposit", "Status"};
		        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		        while (result.next()) {
		            int reqID = result.getInt("req_id");
		            int initialDep = result.getInt("initial_balance");
		            String status = result.getString("request_status");
		            Object[] rowData = {reqID, initialDep, status};
		            tableModel.addRow(rowData);
		        }
			acrequesttable.setModel(tableModel);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
//////////////////////////////////---------Request panel code-----------//////////////////////////////////
	public void createRequest() {
	    try {
	        Connection connection = Database.connection;
	        int confirm = JOptionPane.showConfirmDialog(null, "Submit initial deposit and make an account request? Your funds will be on hold", "Confirmation", JOptionPane.YES_NO_OPTION);

	        if (confirm == JOptionPane.YES_OPTION) {
	            String insertQuery = "INSERT INTO AccountCreationRequest (c_id, initial_balance) VALUES (?, ?)";
	            PreparedStatement insertStm = connection.prepareStatement(insertQuery);
	            insertStm.setInt(1, CustomerLogIn.IDOfUser);
	            insertStm.setInt(2, Integer.parseInt(rinitialdepbox.getText()));
	            insertStm.executeUpdate();

	            JOptionPane.showMessageDialog(null, "Request created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	        JOptionPane.showMessageDialog(null, "An error occurred while creating the request.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	public void updateRequest() {
		try {
	        Connection connection = Database.connection;
	        int confirm = JOptionPane.showConfirmDialog(null, "Your previous deposit will be sent back to you in 2-3 business days and your new deposit will be taken from your account. Do you want to proceed?", "Confirmation", JOptionPane.YES_NO_OPTION);

	        if (confirm == JOptionPane.YES_OPTION) {
	            String insertQuery = "UPDATE AccountCreationRequest SET initial_balance=? WHERE req_id=?";
	            PreparedStatement insertStm = connection.prepareStatement(insertQuery);
	            insertStm.setInt(1, Integer.parseInt(rinitialdepbox.getText()));
	            insertStm.setInt(2, Integer.parseInt(rreqidbox.getText()));
	            insertStm.executeUpdate();

	            JOptionPane.showMessageDialog(null, "Updated request successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	        JOptionPane.showMessageDialog(null, "An error occurred while updating the request.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	public void deleteRequest() {
		try {
	        Connection connection = Database.connection;
	        int confirm = JOptionPane.showConfirmDialog(null, "Your previous deposit will be sent back to you in 2-3 business days and request will be cancelled. Do you want to proceed?", "Confirmation", JOptionPane.YES_NO_OPTION);

	        if (confirm == JOptionPane.YES_OPTION) {
	            String insertQuery = "DELETE FROM AccountCreationRequest WHERE req_id=? AND c_id=?";
	            PreparedStatement insertStm = connection.prepareStatement(insertQuery);
	            insertStm.setInt(1, Integer.parseInt(rreqidbox.getText()));
	            insertStm.setInt(2, CustomerLogIn.IDOfUser);
	            insertStm.executeUpdate();

	            JOptionPane.showMessageDialog(null, "Deleted request successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	        JOptionPane.showMessageDialog(null, "An error occurred while deleting the request.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	public void clearReqField() {
		rinitialdepbox.setText("");
		rreqidbox.setText("");
	}
	public void viewSearchReq() {
		try {
			int id = Integer.parseInt(rreqidbox.getText());
			
			Connection connection = Database.connection;
			String query = "SELECT initial_balance FROM AccountCreationRequest where req_id=?"; 
			PreparedStatement stm = connection.prepareStatement(query);
			stm.setInt(1,  id);
			ResultSet result = stm.executeQuery();
			
			if(result.next()==true) {
				String initialBalance = result.getString(1);
				
				rinitialdepbox.setText(initialBalance);
			}
			else {
				rinitialdepbox.setText("");
			}
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	//////////////////////////////////---------Profile panel code-----------//////////////////////////////////
	public void viewCustomerProfile() {
	    try {
	        int id = CustomerLogIn.IDOfUser;

	        Connection connection = Database.connection;
	        String checkQuery = "SELECT * FROM Customers WHERE c_id = ?";
	        PreparedStatement checkStm = connection.prepareStatement(checkQuery);
	        checkStm.setInt(1, id);
	        ResultSet rs = checkStm.executeQuery();

	        if (rs.next()) {
	            String customerUsername = rs.getString(2);
	            String customerPassword = rs.getString(3);
	            String customerName = rs.getString(4);
	            String customerAddress = rs.getString(5);

	            pusernamebox.setText(customerUsername);
	            ppasswordbox.setText(customerPassword);
	            pnamebox.setText(customerName);
	            paddressbox.setText(customerAddress);
	        } else {
	        	System.out.println("Customer not found.");
	        }

	    } catch (Exception e) {
	        System.out.println(e);
	        JOptionPane.showMessageDialog(null, "An error occurred while retrieving the customer profile.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	public void updateCustomerProfile() {
	    try {
	    	int id = CustomerLogIn.IDOfUser;

	        Connection connection = Database.connection;
	        String updateQuery = "UPDATE Customers SET c_username = ?, c_password = ?, c_name = ?, c_address = ? WHERE c_id = ?";
	        PreparedStatement updateStm = connection.prepareStatement(updateQuery);
	        updateStm.setString(1, pusernamebox.getText());
	        updateStm.setString(2, ppasswordbox.getText());
	        updateStm.setString(3, pnamebox.getText());
	        updateStm.setString(4, paddressbox.getText());
	        updateStm.setInt(5, id);
	        updateStm.executeUpdate();
	        JOptionPane.showMessageDialog(null, "Your Profile was updated in the database!", "Profile Updated Successfully!", JOptionPane.DEFAULT_OPTION);
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	//////////////////////////////////---------Account panel code-----------//////////////////////////////////
	public void clearAccField() {
		abalancebox.setText("");
		aprevtransbox.setText("");
		aaccidbox.setText("");
		aamountbox.setText("");
	}
	public void updateAccountWithdrawal() {
	    try {
	        int id = CustomerLogIn.IDOfUser;
	        int withdrawalAmount = Integer.parseInt(aamountbox.getText());
	        int confirmResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to withdraw $" + withdrawalAmount + " from your account?", "Withdrawal Confirmation", JOptionPane.YES_NO_OPTION);

	        if (confirmResult == JOptionPane.YES_OPTION) {
	            Connection connection = Database.connection;
	            String updateQuery = "UPDATE Accounts SET account_balance = account_balance - ?, account_previousTransaction = ?  WHERE c_id = ?";
	            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
	            updateStm.setInt(1, withdrawalAmount);
	            updateStm.setInt(2, -withdrawalAmount);
	            updateStm.setInt(3, id);
	            updateStm.executeUpdate();
	            JOptionPane.showMessageDialog(null, "Your withdrawal amount has been deducted to your account", "Transaction Completed!", JOptionPane.DEFAULT_OPTION);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void updateAccountDeposit() {
	    try {
	        int id = CustomerLogIn.IDOfUser;
	        int withdrawalAmount = Integer.parseInt(aamountbox.getText());
	        int confirmResult = JOptionPane.showConfirmDialog(null, "Are you sure you want to deposit $" + withdrawalAmount + " into your account?", "Withdrawal Confirmation", JOptionPane.YES_NO_OPTION);

	        if (confirmResult == JOptionPane.YES_OPTION) {
	            Connection connection = Database.connection;
	            String updateQuery = "UPDATE Accounts SET account_balance = account_balance + ?, account_previousTransaction = ?  WHERE c_id = ?";
	            PreparedStatement updateStm = connection.prepareStatement(updateQuery);
	            updateStm.setInt(1, withdrawalAmount);
	            updateStm.setInt(2, withdrawalAmount);
	            updateStm.setInt(3, id);
	            updateStm.executeUpdate();
	            JOptionPane.showMessageDialog(null, "Your deposited amount has been added to your account", "Transaction Completed!", JOptionPane.DEFAULT_OPTION);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void viewTotalBalance() {
	    try {
	        int id = CustomerLogIn.IDOfUser;
	        Connection connection = Database.connection;
	        String checkQuery = "SELECT SUM(account_balance) FROM Accounts WHERE c_id = ? GROUP BY c_id";
	        PreparedStatement checkStm = connection.prepareStatement(checkQuery);
	        checkStm.setInt(1, id);
	        ResultSet rs = checkStm.executeQuery();
	        while(rs.next()){
	            String sumBalance = rs.getString(1);
	            atotbalbox.setText("$" + sumBalance);
	        }

	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	//////////////////////////////////---------Appointment panel code-----------//////////////////////////////////
	public void populateComboBox() {
	    try {
	        Connection connection = Database.connection;
	        String query = "SELECT * FROM Employees"; 
	        Statement stm = connection.createStatement();

	        bankerCBModel = new DefaultComboBoxModel<String>();
	        bankerCBModel.insertElementAt("", 0); // insert an empty field as the first element
	        bankerIDMap = new HashMap<String, Integer>(); // initialize the map

	        ResultSet result = stm.executeQuery(query);
	        while (result.next()) {
	            String bankerName = result.getString("banker");
	            int bankerID = result.getInt("e_id");
	            bankerCBModel.addElement(bankerName);
	            bankerIDMap.put(bankerName, bankerID); // add the mapping to the map
	        }

	        bankerCB.setModel(bankerCBModel);
	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}

	public void createAppointment() {
	    try {
	        int id = CustomerLogIn.IDOfUser;
	        String appointmentDate = apdatebox.getText();
	        String appointmentTime = aptimebox.getText();
	        int bankerID = -1; 
	        if (bankerCB.getSelectedIndex() == 0) { 
	            JOptionPane.showMessageDialog(null, "Please choose a banker.", "Error", JOptionPane.ERROR_MESSAGE);
	            return; 
	        } else {
	            bankerID = bankerIDMap.get(bankerCB.getSelectedItem().toString());
	        }
	        Connection connection = Database.connection;
	        int confirm = JOptionPane.showConfirmDialog(null, "You want to make an appoint for " + appointmentDate + " at " + appointmentTime + "?", "Confirmation", JOptionPane.YES_NO_OPTION);

	        if (confirm == JOptionPane.YES_OPTION) {
	            String insertQuery = "INSERT INTO Appointments (app_date, app_time, c_id, e_id) VALUES (?, ?, ?, ?)";
	            PreparedStatement insertStm = connection.prepareStatement(insertQuery);
	            insertStm.setString(1, appointmentDate);
	            insertStm.setString(2, appointmentTime);
	            insertStm.setInt(3, CustomerLogIn.IDOfUser);
	            insertStm.setInt(4, bankerID);
	            insertStm.executeUpdate();

	            JOptionPane.showMessageDialog(null, "Appointment created successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	        JOptionPane.showMessageDialog(null, "An error occurred while creating the request. Make sure that all details are correct", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}
	public void clearAppField() {
	    apdatebox.setText("");
	    aptimebox.setText("");
	    bankerCB.setSelectedIndex(0);
	}
	public void viewAppointments() {
	    try {
	        int id = CustomerLogIn.IDOfUser;

	        Connection connection = Database.connection;
	        String checkQuery = "SELECT app_date, app_time, e.banker FROM Appointments a JOIN Employees e ON a.e_id = e.e_id WHERE a.c_id = ?";
	        PreparedStatement checkStm = connection.prepareStatement(checkQuery);
	        checkStm.setInt(1, id);
	        ResultSet rs = checkStm.executeQuery();

	        if (rs.next()) {
	            String appDate = rs.getString("app_date");
	            String appTime = rs.getString("app_time");
	            String bankerName = rs.getString("banker");

	            apdatebox.setText(appDate);
	            aptimebox.setText(appTime);
	            bankerCB.setSelectedItem(bankerName);
	        } else {
	        	apdatebox.setText("");
	            aptimebox.setText("");
	            bankerCB.setSelectedIndex(0);
	            System.out.println("No appointments found for the customer.");
	        }

	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}
	public void deleteAppointment() {
		try {
	        Connection connection = Database.connection;
	        int confirm = JOptionPane.showConfirmDialog(null, "Your appointment will be deleted. Do you want to proceed?", "Confirmation", JOptionPane.YES_NO_OPTION);

	        if (confirm == JOptionPane.YES_OPTION) {
	            String insertQuery = "DELETE FROM Appointments WHERE app_id=? AND c_id=?";
	            PreparedStatement insertStm = connection.prepareStatement(insertQuery);
	            insertStm.setInt(1, Integer.parseInt(apappidbox.getText()));
	            insertStm.setInt(2, CustomerLogIn.IDOfUser);
	            insertStm.executeUpdate();

	            JOptionPane.showMessageDialog(null, "Appointment cancelled.", "Success", JOptionPane.INFORMATION_MESSAGE);
	        }
	    } catch (Exception e) {
	        System.out.println(e);
	        JOptionPane.showMessageDialog(null, "An error occurred while deleting the request.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}



}
