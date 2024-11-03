package pt.isec.a2021138502.PD_Splitwise.Data;

import java.sql.SQLException;

/**
 * Manages synchronization between database operations and backup processes
 */
public class DatabaseSyncManager {
	private final Object syncLock = new Object();
	private int activeBackupTransfers = 0;

	/**
	 * Called before starting a backup transfer
	 */
	public void startBackupTransfer() {
		synchronized (syncLock) {
			activeBackupTransfers++;
		}
	}

	/**
	 * Called after completing a backup transfer
	 */
	public void endBackupTransfer() {
		synchronized (syncLock) {
			activeBackupTransfers--;
			if (activeBackupTransfers == 0) {
				syncLock.notifyAll();
			}
		}
	}

	/**
	 * Executes a database operation, waiting if there are active backup transfers
	 *
	 * @param operation The database operation to execute
	 * @throws InterruptedException if the thread is interrupted while waiting
	 */
	public void executeOperation(DatabaseOperation operation) throws InterruptedException, SQLException {
		synchronized (syncLock) {
			while (activeBackupTransfers > 0) {
				syncLock.wait();
			}
			operation.execute();
		}
	}

	/**
	 * Functional interface for database operations
	 */
	@FunctionalInterface
	public interface DatabaseOperation {
		void execute() throws SQLException;
	}
}