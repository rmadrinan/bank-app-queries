import java.sql.*;

public class Connect {

	public static void main(String[] args) {
		Database.connect();
		Database.setupClosingDBConnection();
	}

}
class Database {
	public static Connection connection;
	
	public static void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost/bankDB?serverTimezone=EST", "root", "database28");
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	public static void setupClosingDBConnection() {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() {
	            try { Database.connection.close(); System.out.println("Application Closed - DB Connection Closed");
				} catch (SQLException e) { e.printStackTrace(); }
	        }
	    }, "Shutdown-thread"));
	}
}