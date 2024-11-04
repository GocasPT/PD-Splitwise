package pt.isec.a2021138502.PD_Splitwise.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.NotificaionResponse;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: Singleton Pattern (?)
public class DataBaseManager {
	private static final Logger logger = LoggerFactory.getLogger(DataBaseManager.class);
	private final String dbPath;
	private final Connection conn;
	private final INotificationObserver notificationObserver;
	private IDatabaseChangeObserver databaseChangeObserver;
	private final DatabaseSyncManager syncManager = new DatabaseSyncManager();
	//TODO: object to sync (database manager - server)
	// when server receive a new backup server, wait until "download" is complete
	// when insert invite (and other events), notify server to send notification to user

	//TODO: check this later
	public DataBaseManager(String dbPath) {
		this(dbPath, null, null);
	}

	//TODO: verbose + loading steps
	public DataBaseManager(String dbPath, INotificationObserver notificationObserver, IDatabaseChangeObserver databaseChangeObserver) {
		this.dbPath = dbPath;

		logger.info("Initializing database...");

		try {
			//Note: getConnection() will create the database if it doesn't exist
			//conn = DriverManager.getConnection("jdbc:sqlite:" + Paths.get(dbPath).toAbsolutePath());
			conn = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
			logger.info("Connected to the database");
			createTables(conn);
		} catch ( SQLException e ) {
			throw new RuntimeException("SQLException: " + e.getMessage()); //TODO: improve error message
		}
		this.notificationObserver = notificationObserver;
		this.databaseChangeObserver = databaseChangeObserver;
	}
	
	public boolean addDBChangeObserver(IDatabaseChangeObserver observer) {
		if (databaseChangeObserver != null) return false; //TODO: throw exception (?)

		databaseChangeObserver = observer;
		return true;
	}

	//TODO: dynamic tables: class with all tables + columns (?)
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

	public File getDBFile() {
		return new File(dbPath);
	}

	public DatabaseSyncManager getSyncManager() {
		return syncManager;
	}

	//TODO: sync
	public void setData(String query, Object... params) throws SQLException {
		try {
			syncManager.executeOperation(() -> {
				updateDatabase(query, params);
				databaseChange(query, params);
			});
		} catch ( InterruptedException e ) {
			Thread.currentThread().interrupt();
			throw new SQLException("Operation interrupted", e);
		}
	}

	public void updateDatabase(String query, Object... params) throws SQLException {
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

	private void databaseChange(String query, Object... params) {
		if (databaseChangeObserver == null) return; //TODO: throw exception (?)

		databaseChangeObserver.onDatabaseChange(query, params);
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
			List<Map<String, Object>> rs = getData(query);
			version = (int) rs.getFirst().get("value");
		} catch ( SQLException e ) {
			logger.error("Getting version: {}", e.getMessage());
		}

		return version;
	}

	//Note: Returning ResultSet when Statement is on try-with-resources will close the Statement, so close the ResultSet and cannot access the data on database
	public List<Map<String, Object>> getData(String query, Object... params) throws SQLException {
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

	//TODO: this trigger should be on DataBaseManger (?)
	public void triggerNotification(String email, String text) {
		if (notificationObserver == null) return; //TODO: throw exception (?)

		NotificaionResponse notification = new NotificaionResponse(email, text);
		notificationObserver.onNotification(notification);
	}
}
