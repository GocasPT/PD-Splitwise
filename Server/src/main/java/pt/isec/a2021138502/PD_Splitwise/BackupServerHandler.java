package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat.BYTE_LENGTH;
import static pt.isec.a2021138502.PD_Splitwise.Server.getTimeTag;

public class BackupServerHandler implements Runnable {
	private final Socket backupServerSocket;
	private final DataBaseManager context;
	private final String host;

	public BackupServerHandler(Socket backupServerSocket, DataBaseManager context) {
		this.backupServerSocket = backupServerSocket;
		this.context = context;
		this.host = backupServerSocket.getInetAddress().getHostAddress() + ":" +
				backupServerSocket.getPort() + " - " +
				backupServerSocket.getInetAddress().getHostName();
	}

	@Override
	public void run() {
		System.out.println(getTimeTag() + "Backup Server '" + host + "' connected");

		try (
				OutputStream outStream = backupServerSocket.getOutputStream();
				DataOutputStream dataOut = new DataOutputStream(outStream);
				BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(context.getDBFile()))
		) {
			File dbFile = context.getDBFile();
			long fileSize = dbFile.length();

			dataOut.writeLong(fileSize);
			dataOut.flush();

			System.out.println("[BackupServerThread] File size: " + fileSize);

			byte[] buffer = new byte[BYTE_LENGTH];
			int bytesRead;
			long totalBytesSent = 0;

			while ((bytesRead = fileIn.read(buffer)) != -1) {
				dataOut.write(buffer, 0, bytesRead);
				totalBytesSent += bytesRead;
				//TODO: make it as progress bar
				System.out.println("[BackupServerThread] Sending " + bytesRead + " bytes (" +
						                   totalBytesSent + "/" + fileSize + ")");
			}
			dataOut.flush();

		} catch (SocketException e) {
			System.out.println("[BackupServerThread] Socket error: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("[BackupServerThread] I/O error: " + e.getMessage());
		} finally {
			System.out.println(getTimeTag() + "Backup Server '" + host + "' disconnected");
			try {
				backupServerSocket.close();
			} catch (IOException e) {
				System.out.println("[BackupServerThread] Error closing socket: " + e.getMessage());
			}
		}
	}
}
