package pt.isec.pd.sharedLib.database;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.pd.sharedLib.database.DAO.*;
import pt.isec.pd.sharedLib.database.Observer.DatabaseChangeObserver;
import pt.isec.pd.sharedLib.database.Observer.NotificationObserver;
import pt.isec.pd.sharedLib.network.response.NotificaionResponse;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBaseManager {
	private static final Logger logger = LoggerFactory.getLogger(DataBaseManager.class);
	private static final int MAX_RETRIES = 3;
	private static final int RETRY_DELAY_MS = 100;
	private final String dbPath;
	private final Connection conn;
	@Getter
	private final DatabaseSyncManager syncManager;
	private final NotificationObserver notificationObserver;
	@Getter
	private final UserDAO userDAO;
	@Getter
	private final GroupDAO groupDAO;
	@Getter
	private final InviteDAO inviteDAO; //TODO: I need this or use groupDAO directly?
	@Getter
	private final ExpenseDAO expenseDAO;
	@Getter
	private final PaymentDAO paymentDAO;
	private DatabaseChangeObserver databaseChangeObserver;

	//TODO: object to sync (database manager - server)
	// when server receive a new backup server, wait until "download" is complete
	// when insert invite (and other events), notify server to send notification to user

	//TODO: verbose + loading steps
	public DataBaseManager(String dbPath, NotificationObserver notificationObserver) {
		logger.debug("Database path: {}", dbPath);
		this.dbPath = dbPath;

		logger.info("Initializing database...");

		try {
			//Note: getConnection() will create the database if it doesn't exist
			conn = DriverManager.getConnection("jdbc:sqlite:" + this.dbPath);
			/*try ( Statement stmt = conn.createStatement() ) {
				stmt.execute("PRAGMA journal_mode=WAL;");
				// Set busy timeout
				stmt.execute("PRAGMA busy_timeout=5000;");
			}*/
			logger.debug("Connected to the database");
			syncManager = new DatabaseSyncManager();
			createTables(conn);
			this.userDAO = new UserDAO(this);
			this.groupDAO = new GroupDAO(this);
			this.inviteDAO = new InviteDAO(this);
			this.expenseDAO = new ExpenseDAO(this);
			this.paymentDAO = new PaymentDAO(this);
		} catch ( SQLException e ) {
			throw new RuntimeException("SQLException: " + e.getMessage()); //TODO: improve error message
		}
		logger.debug("Setting notification observer {}", notificationObserver);
		this.notificationObserver = notificationObserver;
	}

	//TODO: dynamic tables: class with all tables + columns (?)
	private void createTables(Connection conn) throws SQLException {
		try ( Statement stmt = conn.createStatement() ) {
			logger.debug("Creating version table");
			//language=SQLite
			stmt.executeUpdate("""			      
			                   CREATE TABLE IF NOT EXISTS version
			                   (
			                   	value INTEGER NOT NULL DEFAULT 0
			                   );
			                   INSERT INTO version (value)
			                   SELECT 0
			                   WHERE NOT EXISTS (SELECT 1 FROM version);
			                   """
			);

			logger.debug("Creating users table");
			//language=SQLite
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS users
			                   (
			                   	id 				INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	username 		TEXT NOT NULL,
			                   	email 			TEXT NOT NULL UNIQUE,
			                   	password 		TEXT NOT NULL,
			                   	phone_number 	TEXT NOT NULL
			                   )
			                   """
			);

			logger.debug("Creating version table");
			//language=SQLite
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS groups
			                   (
			                   	id 		INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	name 	TEXT NOT NULL
			                   )
			                   """
			);

			logger.debug("Creating group_users table");
			//language=SQLite
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS group_users
			                   (
			                   	group_id 	INTEGER NOT NULL,
			                   	user_id 	INTEGER NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES groups (id),
			                   	FOREIGN KEY (user_id) REFERENCES users (id)
			                   )
			                   """
			);

			logger.debug("Creating invites table");
			//language=SQLite
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS invites
			                   (
			                   	id              INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	group_id        INTEGER NOT NULL,
			                   	guest_user_id   INTEGER NOT NULL,
			                   	inviter_user_id INTEGER NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES groups (id),
			                   	FOREIGN KEY (guest_user_id) REFERENCES users (id),
			                   	FOREIGN KEY (inviter_user_id) REFERENCES users (id)
			                   )
			                   """
			);

			logger.debug("Creating expenses table");
			//language=SQLite
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS expenses
			                   (
			                   	id 				INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	group_id 		INTEGER NOT NULL,
			                   	paid_by_user_id INTEGER NOT NULL,
			                   	amount 			REAL NOT NULL,
			                   	description 	TEXT NOT NULL,
			                   	date 			DATE NOT NULL,
			                   	FOREIGN KEY (group_id) REFERENCES groups (id),
			                   	FOREIGN KEY (paid_by_user_id) REFERENCES users (id)
			                   )
			                   """
			);

			logger.debug("Creating payments table");
			//language=SQLite
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS payments
			                   (
			                       id           INTEGER PRIMARY KEY AUTOINCREMENT,
			                       from_user_id INTEGER NOT NULL,
			                       for_user_id  INTEGER NOT NULL,
			                       amount       REAL    NOT NULL,
			                       date         DATE    NOT NULL,
			                       group_id     INTEGER NOT NULL,
			                       FOREIGN KEY (from_user_id) REFERENCES users (id),
			                       FOREIGN KEY (for_user_id) REFERENCES users (id),
			                       FOREIGN KEY (group_id) REFERENCES groups (id)
			                   )
			                   """
			);

			logger.debug("Creating debts table");
			//language=SQLite
			stmt.executeUpdate("""
			                   CREATE TABLE IF NOT EXISTS expense_users
			                   (
			                    id 			INTEGER PRIMARY KEY AUTOINCREMENT,
			                   	expense_id 	INTEGER NOT NULL,
			                   	user_id 	INTEGER NOT NULL,
			                   	percentage 	REAL NOT NULL,
			                   	FOREIGN KEY (expense_id) REFERENCES expenses (id),
			                   	FOREIGN KEY (user_id) REFERENCES users (id)
			                   )
			                   """
			);
		}
	}

	public boolean addDBChangeObserver(DatabaseChangeObserver observer) {
		logger.debug("Setting database change observer {}", observer);
		if (databaseChangeObserver != null) {
			logger.error("Database change observer already set");
			return false; //TODO: throw exception (?)
		}

		databaseChangeObserver = observer;
		return true;
	}

	public File getDBFile() {
		return new File(dbPath);
	}

	public Connection getConnection() throws SQLException, InterruptedException {
		//TODO: sync block
		syncManager.executeOperation(null);
		return conn;
	}

	public void updateVersion() throws SQLException {
		int newVersion = getVersion() + 1;
		logger.debug("Incrementing version from {} to {}", getVersion(), newVersion);
		//language=SQLite
		PreparedStatement pstmt = conn.prepareStatement("UPDATE version SET value = ?");
		pstmt.setInt(1, newVersion);
		pstmt.executeUpdate();
	}

	public int executeWriteWithId(String query, Object... params) throws SQLException {
		int retryCount = 0;
		SQLException lastException;

		do {
			try {
				return executeWriteTransactionWithId(query, params);
			} catch ( SQLException e ) {
				lastException = e;
				if (isSQLiteBusy(e)) {
					logger.warn("Database busy, attempt {} of {}", retryCount + 1, MAX_RETRIES);
					try {
						Thread.sleep((long) RETRY_DELAY_MS * (retryCount + 1));
					} catch ( InterruptedException ie ) {
						Thread.currentThread().interrupt();
						throw new SQLException("Operation interrupted during retry", ie);
					}
				} else {
					throw e; // Rethrow if it's not a SQLITE_BUSY error
				}
			}
		} while (++retryCount < MAX_RETRIES);

		throw new SQLException("Failed after " + MAX_RETRIES + " attempts", lastException);
	}

	private int executeWriteTransactionWithId(String query, Object... params) throws SQLException {
		try {
			return syncManager.executeOperation(() -> {
				conn.setAutoCommit(false);
				try {
					int id;
					try ( PreparedStatement pstmt = conn.prepareStatement(query) ) {
						for (int i = 0; i < params.length; i++) {
							pstmt.setObject(i + 1, params[i]);
						}

						try ( ResultSet rs = pstmt.executeQuery() ) {
							if (!rs.next()) {
								throw new SQLException("No ID was returned from the operation");
							}
							id = (int) rs.getLong(1);
						}
					}

					incrementVersion(conn);
					notifyDatabaseChange(query, params);
					conn.commit();
					return id;
				} catch ( SQLException e ) {
					try {
						conn.rollback();
					} catch ( SQLException rollbackEx ) {
						logger.error("Error rolling back transaction", rollbackEx);
					}
					throw e;
				} finally {
					conn.setAutoCommit(true);
				}
			});
		} catch ( InterruptedException e ) {
			Thread.currentThread().interrupt();
			throw new SQLException("Operation interrupted", e);
		}
	}

	private boolean isSQLiteBusy(SQLException e) {
		return e.getErrorCode() == SQLiteErrorCode.SQLITE_BUSY.code;
	}

	private void incrementVersion(Connection conn) throws SQLException {
		int newVersion = getVersion() + 1;
		logger.debug("Incrementing version from {} to {}", getVersion(), newVersion);
		//language=SQLite
		PreparedStatement pstmt = conn.prepareStatement("UPDATE version SET value = ?");
		pstmt.setInt(1, newVersion);
		pstmt.executeUpdate();
	}

	private void notifyDatabaseChange(String query, Object... params) {
		if (databaseChangeObserver != null) {
			databaseChangeObserver.onDBChange(query, params);
		}
	}

	public int getVersion() {
		logger.debug("Getting version");
		int version = -1;
		//language=SQLite
		String query = "SELECT * FROM version;";

		try {
			List<Map<String, Object>> rs = executeRead(query);
			version = (int) rs.getFirst().get("value");
		} catch ( SQLException e ) {
			logger.error("Getting version: {}", e.getMessage());
		}

		return version;
	}

	//TODO: maybe use <T> to return the type of the query (?)
	public List<Map<String, Object>> executeRead(String query, Object... params) throws SQLException {
		List<Map<String, Object>> results = new ArrayList<>();
		try ( PreparedStatement pstmt = conn.prepareStatement(query) ) {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}

			try ( ResultSet rs = pstmt.executeQuery() ) {
				ResultSetMetaData metaData = rs.getMetaData();
				int columnCount = metaData.getColumnCount();

				while (rs.next()) {
					Map<String, Object> row = new HashMap<>();
					for (int i = 1; i <= columnCount; i++) {
						row.put(metaData.getColumnName(i), rs.getObject(i));
					}
					results.add(row);
				}
			}
		}
		return results;
	}

	public int executeWrite(String query, Object... params) throws SQLException {
		int retryCount = 0;
		SQLException lastException;

		do {
			try {
				return executeWriteTransaction(query, params);
			} catch ( SQLException e ) {
				lastException = e;
				if (isSQLiteBusy(e)) {
					logger.warn("Database busy, attempt {} of {}", retryCount + 1, MAX_RETRIES);
					try {
						Thread.sleep((long) RETRY_DELAY_MS * (retryCount + 1));
					} catch ( InterruptedException ie ) {
						Thread.currentThread().interrupt();
						throw new SQLException("Operation interrupted during retry", ie);
					}
				} else {
					throw e;
				}
			}
		} while (++retryCount < MAX_RETRIES);

		throw new SQLException("Failed after " + MAX_RETRIES + " attempts", lastException);
	}

	//TODO: sync
	//TODO: maybe remove this... but keep the sync part
	/*public int setData(String query, Object... params) throws SQLException {
		try {
			//TODO: maybe change this later...
			AtomicInteger id = new AtomicInteger();
			syncManager.executeOperation(() -> {
				id.set(updateDatabase(query, params));
				databaseChange(query, params);
			});
			return id.get();
		} catch ( InterruptedException e ) {
			Thread.currentThread().interrupt();
			throw new SQLException("Operation interrupted", e);
		}
	}

	public int updateDatabase(String query, Object... params) throws SQLException {
		try (
				PreparedStatement pstmt = conn.prepareStatement(query)
		) {
			for (int i = 0; i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			int id = 0;
			try ( ResultSet rs = pstmt.executeQuery(); ) {
				if (rs.next()) {
					id = (int) rs.getLong(1);
				}
			} catch ( SQLException e ) {
				throw new SQLException("Error executing query: " + e.getMessage(), e);
			}
			incrementVersion(conn);
			return id;
		}
	}*/

	/*private void databaseChange(String query, Object... params) {
		if (databaseChangeObserver == null) return; //TODO: throw exception (?)

		databaseChangeObserver.onDBChange(query, params);
	}*/

	private int executeWriteTransaction(String query, Object... params) throws SQLException {
		try {
			return syncManager.executeOperation(() -> {
				conn.setAutoCommit(false);
				try {
					int affectedRows;
					try ( PreparedStatement pstmt = conn.prepareStatement(query) ) {
						for (int i = 0; i < params.length; i++) {
							pstmt.setObject(i + 1, params[i]);
						}
						affectedRows = pstmt.executeUpdate();
					}

					incrementVersion(conn);
					notifyDatabaseChange(query, params);
					conn.commit();
					return affectedRows;
				} catch ( SQLException e ) {
					try {
						conn.rollback();
					} catch ( SQLException rollbackEx ) {
						logger.error("Error rolling back transaction", rollbackEx);
					}
					throw e;
				} finally {
					conn.setAutoCommit(true);
				}
			});
		} catch ( InterruptedException e ) {
			Thread.currentThread().interrupt();
			throw new SQLException("Operation interrupted", e);
		}
	}

	//TODO: this trigger should be on DataBaseManger (?)
	public void triggerNotification(String email, String text) {
		logger.debug("Triggering notification for {} ({})", email, notificationObserver);
		if (notificationObserver == null) return; //TODO: throw exception (?)

		NotificaionResponse notification = new NotificaionResponse(email, text);
		notificationObserver.onNotification(notification);
	}

	//TODO: throw exception on error
	//TODO: maybe remove this... but keep the sync part
	//Note: Returning ResultSet when Statement is on try-with-resources will close the Statement, so close the ResultSet and cannot access the data on database
	/*public List<Map<String, Object>> getData(String query, Object... params) throws SQLException {
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
	}*/

	private enum SQLiteErrorCode {
		SQLITE_BUSY(5);

		final int code;

		SQLiteErrorCode(int code) {
			this.code = code;
		}
	}
}
