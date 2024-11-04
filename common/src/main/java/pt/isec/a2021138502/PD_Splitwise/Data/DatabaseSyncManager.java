package pt.isec.a2021138502.PD_Splitwise.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.a2021138502.PD_Splitwise.Terminal.utils;

import java.sql.SQLException;

public class DatabaseSyncManager {
	private static final Logger logger = LoggerFactory.getLogger(utils.class);
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