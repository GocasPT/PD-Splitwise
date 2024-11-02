package pt.isec.a2021138502.PD_Splitwise.Data;

import pt.isec.a2021138502.PD_Splitwise.Message.Response.NotificaionResponse;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: Singleton Pattern (?)
public class DataBaseManager {
	private final Connection conn;
	private final INotificationObserver observer;
	//TODO: object to sync (database manager - server)
	// when server receive a new backup server, wait until "download" is complete
	// when database manager make a new SQL query, notify server to send new heartbeat with query
	// when insert invite (and other events), notify server to send notification to user

	//TODO: verbose + loading steps
	public DataBaseManager(String dbPath, INotificationObserver observer) {
		System.out.println(getClassTag() + "Initializing database...");

		try {
			//Note: getConnection() will create the database if it doesn't exist
			//conn = DriverManager.getConnection("jdbc:sqlite:" + Paths.get(dbPath).toAbsolutePath());
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
			System.out.println(getClassTag() + "Connected to the database");
			createTables(conn);
		} catch ( SQLException e ) {
			throw new RuntimeException("Database error: " + e.getMessage()); //TODO: improve error message
		}
		this.observer = observer;
	}

	private String getClassTag() {
		return "(" + this.getClass().getSimpleName() + "): ";
	}

	private void createTables(Connection conn) throws SQLException {
		try ( Statement stmt = conn.createStatement() ) {
			// Version table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS version (
			                       value INTEGER NOT NULL
			                   );
			                   """
			);

			// Users table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS users (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	username TEXT NOT NULL,
			                   	email TEXT NOT NULL UNIQUE,
			                   	password TEXT NOT NULL,
			                   	phone_number TEXT NOT NULL
			                   );
			                   """
			);

			// Groups table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS groups (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	name TEXT NOT NULL
			                   );
			                   """
			);

			// Group users table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS group_users (
			                   	group_id INTEGER NOT NULL,
			                   	user_id INTEGER NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES groups (id),
			                   	FOREIGN KEY (user_id) REFERENCES users (id)
			                   );
			                   """
			);

			// Invites table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS invites (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	group_id INTEGER NOT NULL,
			                   	user_id INTEGER NOT NULL,
			                   	inviter_user_id INTEGER NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES groups (id),
			                   	FOREIGN KEY (user_id) REFERENCES users (id)
			                   );
			                   """
			);

			// Expenses table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS expenses (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	group_id INTEGER NOT NULL,
			                   	payer_id INTEGER NOT NULL,
			                   	amount REAL NOT NULL,
			                   	description TEXT NOT NULL,
			                   	date TEXT NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES groups (id),
			                   	FOREIGN KEY (payer_id) REFERENCES users (id)
			                   );
			                   """
			);

			// Payments table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS payments (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	expense_id INTEGER NOT NULL,
			                   	payer_id INTEGER NOT NULL,
			                   	payee_id INTEGER NOT NULL,
			                   	amount REAL NOT NULL,
			                   	FOREIGN KEY (expense_id) REFERENCES expenses (id),
			                   	FOREIGN KEY (payer_id) REFERENCES users (id),
			                   	FOREIGN KEY (payee_id) REFERENCES users (id)
			                   );
			                   """
			);

			// Debts table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS debts (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	group_id INTEGER NOT NULL,
			                   	payer_id INTEGER NOT NULL,
			                   	payee_id INTEGER NOT NULL,
			                   	amount REAL NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES groups (id),
			                   	FOREIGN KEY (payer_id) REFERENCES users (id),
			                   	FOREIGN KEY (payee_id) REFERENCES users (id)
			                   );
			                   """
			);
		}
	}

	//TODO: notify server new SQL query have been made
	//TODO: sync
	public void insert(String query, Object... params) throws SQLException {
		try (
				PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			pstmt.executeUpdate();
			incrementVersion(conn);
		}
	}

	private void incrementVersion(Connection conn) throws SQLException {
		int newVersion = getVersion() + 1;
		PreparedStatement pstmt = conn.prepareStatement("UPDATE version SET value = ?");
		pstmt.setInt(1, newVersion);
		pstmt.executeUpdate();
	}

	//TODO: throw exception on error
	public int getVersion() {
		int version = -1;
		String query = "SELECT * FROM version;";

		try {
			List<Map<String, Object>> rs = select(query);
			version = (int) rs.getFirst().get("value");
		} catch ( SQLException e ) {
			System.err.println(getClassTag() + "Error getting version: " + e.getMessage());
		}

		return version;
	}

	//Note: Returning ResultSet when Statement is on try-with-resources will close the Statement, so close the ResultSet and cannot access the data on database
	public List<Map<String, Object>> select(String query, Object... params) throws SQLException {
		List<Map<String, Object>> results = new ArrayList<>();
		try ( PreparedStatement pstmt = conn.prepareStatement(query) ) {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			try ( ResultSet rs = pstmt.executeQuery() ) {
				ResultSetMetaData rsMeta = rs.getMetaData();
				int columnCount = rsMeta.getColumnCount();

				while (rs.next()) {
					Map<String, Object> row = new HashMap<>();
					for (int i = 1; i <= columnCount; i++) {
						row.put(rsMeta.getColumnName(i), rs.getObject(i));
					}
					results.add(row);
				}
			}
		}
		return results;
	}

	//TODO: notify server new SQL query have been made
	//TODO: sync
	public void update(String query, Object... params) throws SQLException {
		try (
				PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			pstmt.executeUpdate();
			incrementVersion(conn);
		}
	}

	//TODO: notify server new SQL query have been made
	//TODO: sync
	public void delete(String query, Object... params) throws SQLException {
		try (
				PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			pstmt.executeUpdate();
			incrementVersion(conn);
		}
	}

	public void createInvite(String invetedEmal) {
		System.out.println("DEBUG: createInvite(" + invetedEmal + ") " + observer);
		if (observer == null) return; //TODO: throw exception (?)

		NotificaionResponse notification = new NotificaionResponse(invetedEmal, "You have been invited to a group");
		observer.onNotification(notification);
	}
}
