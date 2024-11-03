package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat.BUFFER_SIZE;

public class BackupServer {
	private static final String MULTICAST_ADDRESS = "230.44.44.44";
	private static final int MULTICAST_PORT = 4444;
	private static final int INTERVAL = 30;

	private final Path dbDirectory;
	private final String dbFilename;
	private DataBaseManager context;

	public BackupServer(String dbPath) {
		Path path = Paths.get(dbPath);
		this.dbDirectory = path.getParent();
		this.dbFilename = path.getFileName().toString();

		File directory = dbDirectory.toFile();
		if (!directory.exists() || !directory.isDirectory()) {
			throw new IllegalArgumentException("Directory does not exist: " + dbDirectory);
		}

		File[] files = directory.listFiles();
		if (files == null || files.length != 0)
			throw new IllegalArgumentException("Directory is not empty: " + dbDirectory);
	}

	private static String getTimeTag() {
		return "<" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "> ";
	}

	private void printProgress(long current, long total) {
		int percentage = (int) ((current * 100.0) / total);
		int progressChars = (int) ((60.0 * current) / total);
		String progress = "\r[" +
				"=".repeat(progressChars) +
				" ".repeat(60 - progressChars) +
				String.format("] %d%% (%d/%d bytes)", percentage, current, total);
		System.out.println(progress);
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java BackupServer.BackupServer <path_to_database>");
			return;
		}

		try {
			new BackupServer(args[0]).start();
		} catch ( IllegalArgumentException e ) {
			System.out.println(getTimeTag() + "[Server Error] " + e.getMessage());
		}
	}

	private void start() {
		try {
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
			NetworkInterface nif = NetworkInterface.getByName(MULTICAST_ADDRESS);

			try ( MulticastSocket socket = new MulticastSocket(MULTICAST_PORT) ) {
				socket.joinGroup(new InetSocketAddress(group, MULTICAST_PORT), nif);
				socket.setSoTimeout(INTERVAL * 1000);

				DatagramPacket packet = getPacket(socket, group);
				Heartbeat heartbeat = getHeartbeat(packet);

				InetAddress tcpAddress = packet.getAddress();
				int tcpPort = heartbeat.tcpPort();
				Path dbFilePath = dbDirectory.resolve(dbFilename);

				if (downloadDBFile(tcpAddress, tcpPort, dbFilePath)) return;

				while (true) {
					heartbeat = getHeartbeat(getPacket(socket, group));
					processHeartbeat(heartbeat);
				}

			} catch ( SocketException e ) {
				System.err.println(getTimeTag() + "Error configuring multicast socket: " + e.getMessage());
			} catch ( SQLException e ) {
				System.err.println(getTimeTag() + "Error with database operations: " + e.getMessage());
			} catch ( IOException e ) {
				System.err.println(getTimeTag() + "Error with multicast operations: " + e.getMessage());
			}
		} catch ( UnknownHostException e ) {
			System.err.println(getTimeTag() + "Invalid multicast address: " + MULTICAST_ADDRESS);
		} catch ( SocketException e ) {
			System.err.println(getTimeTag() + "Error creating network interface: " + e.getMessage());
		} catch ( RuntimeException e ) {
			System.out.println(getTimeTag() + e.getMessage());
		}
	}

	private DatagramPacket getPacket(MulticastSocket socket, InetAddress group) {
		DatagramPacket newPacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE, group, MULTICAST_PORT);

		try {
			socket.receive(newPacket);
		} catch ( SocketTimeoutException e ) {
			throw new RuntimeException("Timeout waiting for server heartbeat: " + e.getMessage());
		} catch ( IOException e ) {
			throw new RuntimeException("Error receiving heartbeat packet: " + e.getMessage());
		}

		return newPacket;
	}

	private Heartbeat getHeartbeat(DatagramPacket packet) {
		try (
				ObjectInputStream objIn = new ObjectInputStream(
						new ByteArrayInputStream(packet.getData(), 0, packet.getLength()))
		) {
			return (Heartbeat) objIn.readObject();
		} catch ( ClassNotFoundException e ) {
			throw new IllegalArgumentException("Invalid heartbeat format: " + e.getMessage());
		} catch ( InvalidClassException e ) {
			throw new RuntimeException("Heartbeat class version mismatch: " + e.getMessage());
		} catch ( IOException e ) {
			throw new RuntimeException("Error reading heartbeat data: " + e.getMessage());
		}
	}

	private boolean downloadDBFile(InetAddress tcpAddress, int tcpPort, Path dbFilePath) {
		try (
				Socket tcpSocket = new Socket(tcpAddress, tcpPort);
				InputStream inStream = tcpSocket.getInputStream();
				DataInputStream dataIn = new DataInputStream(inStream);
				FileOutputStream fileOut = new FileOutputStream(dbFilePath.toFile())
		) {
			System.out.println(getTimeTag() + "Downloading database to '" + dbFilePath + "'...");

			long fileSize;
			fileSize = dataIn.readLong();
			System.out.println(getTimeTag() + "File size: " + fileSize + " bytes");

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead;
			long totalBytesRead = 0;

			while (totalBytesRead < fileSize &&
					(bytesRead = dataIn.read(buffer)) != -1) {
				fileOut.write(buffer, 0, bytesRead);
				totalBytesRead += bytesRead;
				printProgress(totalBytesRead, fileSize);
			}
			fileOut.flush();

			if (totalBytesRead == fileSize) {
				System.out.println(getTimeTag() + "Database downloaded successfully");
				context = new DataBaseManager(dbFilePath.toAbsolutePath().toString(), null, null);
				return true;
			} else
				throw new RuntimeException(
						"Incomplete file transfer: received " + totalBytesRead + " of " + fileSize + " bytes");

		} catch ( EOFException e ) {
			System.err.println(getTimeTag() + "Failed to read file size from server: " + e.getMessage());
		} catch ( ConnectException e ) {
			System.err.println(getTimeTag() + "Could not connect to server: " + e.getMessage());
		} catch ( SocketException e ) {
			System.err.println(getTimeTag() + "Connection lost: " + e.getMessage());
		} catch ( SocketTimeoutException e ) {
			System.err.println(getTimeTag() + "Connection timed out: " + e.getMessage());
		} catch ( IOException e ) {
			System.err.println(getTimeTag() + "Error in TCP connection: " + e.getMessage());
		}

		return false;
	}

	private void processHeartbeat(Heartbeat heartbeat) throws SQLException {
		if (heartbeat.version() != context.getVersion())
			if (heartbeat.query() != null)
				handleDatabaseUpdate(heartbeat);
			else
				throw new RuntimeException("Version mismatch but no update query provided");
		else if (heartbeat.query() != null)
			throw new RuntimeException("No version mismatch but update query provided");
	}

	private void handleDatabaseUpdate(Heartbeat heartbeat) throws SQLException {
		System.out.println(getTimeTag() + "Updating database with new data: " + heartbeat.query());
		context.updateDatabase(heartbeat.query(), heartbeat.params());
	}
}