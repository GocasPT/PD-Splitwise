package pt.isec.a2021138502.PD_Splitwise.Data;

import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: Singleton Pattern
public class DataBaseManager {
	public final String VERSION_TABLE = "version";
	public final String USERS_TABLE = "users";
	public final String GROUPS_TABLE = "groups";
	public final String GROUP_USERS_TABLE = "group_users";
	public final String INVITES_TABLE = "invites";
	public final String EXPENSES_TABLE = "expenses";
	public final String PAYMENTS_TABLE = "payments";
	public final String DEBTS_TABLE = "debts";
	//TODO: add all tables strings

	private final Connection conn;

	//TODO: verbose + loading steps
	public DataBaseManager(String dbPath) {
		System.out.println(getClassTag() + "Initializing database...");

		try {
			//Note: getConnection() will create the database if it doesn't exist
			conn = DriverManager.getConnection("jdbc:sqlite:" + Paths.get(dbPath).toAbsolutePath());
			System.out.println(getClassTag() + "Connected to the database");
			createTables(conn);
		} catch ( SQLException e ) {
			throw new RuntimeException("Database error: " + e.getMessage()); //TODO: improve error message
		}
	}

	private String getClassTag() {
		return "(" + this.getClass().getSimpleName() + "): ";
	}

	private void createTables(Connection conn) throws SQLException {
		try ( Statement stmt = conn.createStatement() ) {
			// Version table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS %s (
			                       value INTEGER NOT NULL
			                   );
			                   """.formatted(VERSION_TABLE)
			);

			// Users table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS %s (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	username TEXT NOT NULL,
			                   	email TEXT NOT NULL UNIQUE,
			                   	password TEXT NOT NULL,
			                   	phone_number TEXT NOT NULL
			                   );
			                   """.formatted(USERS_TABLE)
			);

			// Groups table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS %s (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	name TEXT NOT NULL
			                   );
			                   """.formatted(GROUPS_TABLE)
			);

			// Group users table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS %s (
			                   	group_id INTEGER NOT NULL,
			                   	user_id INTEGER NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES %s (id),
			                   	FOREIGN KEY (user_id) REFERENCES %s (id)
			                   );
			                   """.formatted(GROUP_USERS_TABLE, GROUP_USERS_TABLE, USERS_TABLE)
			);

			// Invites table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS %s (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	group_id INTEGER NOT NULL,
			                   	user_id INTEGER NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES %s (id),
			                   	FOREIGN KEY (user_id) REFERENCES %s (id)
			                   );
			                   """.formatted(INVITES_TABLE, GROUPS_TABLE, USERS_TABLE)
			);

			// Expenses table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS %s (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	group_id INTEGER NOT NULL,
			                   	payer_id INTEGER NOT NULL,
			                   	amount REAL NOT NULL,
			                   	description TEXT NOT NULL,
			                   	date TEXT NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES %s (id),
			                   	FOREIGN KEY (payer_id) REFERENCES %s (id)
			                   );
			                   """.formatted(EXPENSES_TABLE, GROUPS_TABLE, USERS_TABLE)
			);

			// Payments table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS %s (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	expense_id INTEGER NOT NULL,
			                   	payer_id INTEGER NOT NULL,
			                   	payee_id INTEGER NOT NULL,
			                   	amount REAL NOT NULL,
			                   	FOREIGN KEY (expense_id) REFERENCES %s (id),
			                   	FOREIGN KEY (payer_id) REFERENCES %s (id),
			                   	FOREIGN KEY (payee_id) REFERENCES %s (id)
			                   );
			                   """.formatted(PAYMENTS_TABLE, EXPENSES_TABLE, USERS_TABLE, USERS_TABLE)
			);

			// Debts table
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS %s (
			                   	id INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	group_id INTEGER NOT NULL,
			                   	payer_id INTEGER NOT NULL,
			                   	payee_id INTEGER NOT NULL,
			                   	amount REAL NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES %s (id),
			                   	FOREIGN KEY (payer_id) REFERENCES %s (id),
			                   	FOREIGN KEY (payee_id) REFERENCES %S (id)
			                   );
			                   """.formatted(DEBTS_TABLE, GROUPS_TABLE, USERS_TABLE, USERS_TABLE)
			);
		}
	}

	public int getVersion() {
		int version = -1;
		String query = "SELECT * FROM %s;".formatted(VERSION_TABLE);

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

	private void incrementVersion(Connection conn) throws SQLException {
		int newVersion = getVersion() + 1;
		PreparedStatement pstmt = conn.prepareStatement("UPDATE version SET value = ?");
		pstmt.setInt(1, newVersion);
		pstmt.executeUpdate();
	}
}
