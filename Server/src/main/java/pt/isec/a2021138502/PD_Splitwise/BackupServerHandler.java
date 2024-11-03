package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat.BUFFER_SIZE;
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

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead;
			long totalBytesSent = 0;

			while ((bytesRead = fileIn.read(buffer)) != -1) {
				dataOut.write(buffer, 0, bytesRead);
				totalBytesSent += bytesRead;
				int percentage = (int) ((totalBytesSent * 100.0) / fileSize);
				System.out.println(getTimeTag() + "[" +
						                   "=".repeat(percentage / 2) +
						                   " ".repeat(50 - (percentage / 2)) +
						                   "] " + percentage + "% " + totalBytesSent + "/" + fileSize + " bytes");
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
