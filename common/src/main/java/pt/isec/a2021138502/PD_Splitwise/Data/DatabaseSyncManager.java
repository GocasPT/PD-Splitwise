package pt.isec.a2021138502.PD_Splitwise.Data;

import java.sql.SQLException;

public class DatabaseSyncManager {
	private final Object syncLock = new Object();
	private int activeBackupTransfers = 0;

	public void startBackupTransfer() {
		synchronized (syncLock) {
			activeBackupTransfers++;
		}
	}

	public void endBackupTransfer() {
		synchronized (syncLock) {
			activeBackupTransfers--;
			if (activeBackupTransfers == 0) {
				syncLock.notifyAll();
			}
		}
	}

	public void executeOperation(DatabaseOperation operation) throws InterruptedException, SQLException {
		synchronized (syncLock) {
			while (activeBackupTransfers > 0) {
				syncLock.wait();
			}
			operation.execute();
		}
	}

	@FunctionalInterface
	public interface DatabaseOperation {
		void execute() throws SQLException;
	}
}