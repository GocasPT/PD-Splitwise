package pt.isec.a2021138502.PD_Splitwise.Server.Runnable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.DatabaseSyncManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat.BUFFER_SIZE;
import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.printProgress;

public class BackupServerHandler implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(BackupServerHandler.class);
	private final Socket backupServerSocket;
	private final DataBaseManager dbManager;
	private final String host;

	public BackupServerHandler(Socket backupServerSocket, DataBaseManager dbManager) {
		this.backupServerSocket = backupServerSocket;
		this.dbManager = dbManager;
		this.host = backupServerSocket.getInetAddress().getHostAddress() + ":" +
				backupServerSocket.getPort() + " - " +
				backupServerSocket.getInetAddress().getHostName();
	}

	@Override
	public void run() {
		logger.info("Backup Server '{}' connected", host);
		DatabaseSyncManager syncManager = dbManager.getSyncManager();
		syncManager.startBackupTransfer();

		try (
				OutputStream outStream = backupServerSocket.getOutputStream();
				DataOutputStream dataOut = new DataOutputStream(outStream);
				BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(dbManager.getDBFile()))
		) {
			File dbFile = dbManager.getDBFile();
			long fileSize = dbFile.length();

			dataOut.writeLong(fileSize);
			dataOut.flush();

			logger.info("File size: {}", fileSize);

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead;
			long totalBytesSent = 0;

			while ((bytesRead = fileIn.read(buffer)) != -1) {
				dataOut.write(buffer, 0, bytesRead);
				totalBytesSent += bytesRead;
				printProgress(totalBytesSent, fileSize);
			}
			dataOut.flush();

		} catch ( SocketException e ) {
			logger.error("SocketException: {}", e.getMessage());
		} catch ( IOException e ) {
			logger.error("IOException: {}", e.getMessage());
		} finally {
			logger.info("Backup Server '{}' disconnected", host);
			syncManager.endBackupTransfer();
			try {
				backupServerSocket.close();
			} catch ( IOException e ) {
				logger.error("Closing socket: {}", e.getMessage());
			}
		}
	}
}
