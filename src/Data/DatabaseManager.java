package Data;

import java.sql.*;

public class DatabaseManager {
	private final String dbPath;
	private int version = 0;  // Version number of the database.

	public DatabaseManager(String dbPath) {
		this.dbPath = dbPath;
	}

	public void initializeDatabase() {
		System.out.println("Initializing database...");
		try (Connection conn = connect()) {
			if (conn != null) {
				System.out.println("Connected to the database.");
				try (Statement stmt = conn.createStatement()) {
					// Create a version table if it doesn't exist.
					System.out.println("Creating version table if it doesn't exist.");
					stmt.execute("CREATE TABLE IF NOT EXISTS version (value INTEGER)");

					// Check the version number.
					System.out.println("Checking the version number.");
					ResultSet rs = stmt.executeQuery("SELECT value FROM version LIMIT 1");
					if (rs.next()) {
						version = rs.getInt("value");
						System.out.println("Current database version: " + version);
					} else {
						System.out.println("Version table is empty. Initializing version to 0.");
						stmt.execute("INSERT INTO version (value) VALUES (0)");
					}
				}
			} else {
				System.err.println("Failed to connect to the database.");
			}
		} catch (SQLException e) {
			System.err.println("Database initialization error: " + e.getMessage());
		}
	}

	public int getVersion() {
		return version;
	}

	public void incrementVersion() {
		version++;
		try (Connection conn = connect();
		     PreparedStatement stmt = conn.prepareStatement("UPDATE version SET value = ?")) {
			stmt.setInt(1, version);
			stmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error updating version: " + e.getMessage());
		}
	}

	//TODO: check this
	public void executeQuery(String sql) {
		try (Connection conn = connect();
		     Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
			incrementVersion();
		} catch (SQLException e) {
			System.err.println("SQL execution error: " + e.getMessage());
		}
	}

	private Connection connect() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
	}
}
