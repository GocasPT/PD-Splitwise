package pt.isec.a2021138502.PD_Splitwise.Server.Runnable;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.DatabaseSyncManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat.BUFFER_SIZE;
import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.getTimeTag;
import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.printProgress;

public class BackupServerHandler implements Runnable {
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
		System.out.println(getTimeTag() + "Backup Server '" + host + "' connected");
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

			System.out.println("[BackupServerThread] File size: " + fileSize);

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
			System.out.println("[BackupServerThread] Socket error: " + e.getMessage());
		} catch ( IOException e ) {
			System.out.println("[BackupServerThread] I/O error: " + e.getMessage());
		} finally {
			System.out.println(getTimeTag() + "Backup Server '" + host + "' disconnected");
			syncManager.endBackupTransfer();
			try {
				backupServerSocket.close();
			} catch ( IOException e ) {
				System.out.println("[BackupServerThread] Error closing socket: " + e.getMessage());
			}
		}
	}
}
