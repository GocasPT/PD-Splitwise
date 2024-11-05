package pt.isec.a2021138502.PD_Splitwise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;

import static pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat.BUFFER_SIZE;
import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.printProgress;

public class BackupServer {
	private static final Logger logger = LoggerFactory.getLogger(BackupServer.class);
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
			throw new IllegalArgumentException("Directory does not exist");
		}

		File[] files = directory.listFiles();
		if (files == null || files.length != 0)
			throw new IllegalArgumentException("Directory is not empty");
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			logger.error("Usage: java BackupServer.BackupServer <path_to_database>"); //TODO: check this later
			return;
		}

		try {
			new BackupServer(args[0]).start();
		} catch ( IllegalArgumentException e ) {
			logger.error(e.getMessage());
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

				if (!downloadDBFile(tcpAddress, tcpPort, dbFilePath)) return;

				while (true) {
					heartbeat = getHeartbeat(getPacket(socket, group));
					processHeartbeat(heartbeat);
				}

			} catch ( SocketException e ) {
				logger.error("Configuring multicast socket: {}", e.getMessage());
			} catch ( SQLException e ) {
				logger.error("Database operations: {}", e.getMessage());
			} catch ( IOException e ) {
				logger.error("Multicast operations: {}", e.getMessage());
			}
		} catch ( UnknownHostException e ) {
			logger.error("Invalid multicast address: {}", MULTICAST_ADDRESS);
		} catch ( SocketException e ) {
			logger.error("Creating network interface: {}", e.getMessage());
		} catch ( RuntimeException e ) {
			logger.error("{}", e.getMessage());
		}
	}

	private DatagramPacket getPacket(MulticastSocket socket, InetAddress group) {
		DatagramPacket newPacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE, group, MULTICAST_PORT);

		try {
			socket.receive(newPacket);
		} catch ( SocketTimeoutException e ) {
			throw new RuntimeException("Timeout");
		} catch ( IOException e ) {
			throw new RuntimeException("Receiving heartbeat packet: " + e.getMessage());
		}

		return newPacket;
	}

	private Heartbeat getHeartbeat(DatagramPacket packet) {
		try (
				ObjectInputStream objIn = new ObjectInputStream(
						new ByteArrayInputStream(packet.getData(), 0, packet.getLength()))
		) {
			Heartbeat heartbeat = (Heartbeat) objIn.readObject();
			logger.info("Received heartbeat: \n\t{}", heartbeat);
			return heartbeat;
		} catch ( ClassNotFoundException e ) {
			throw new IllegalArgumentException("Invalid heartbeat format: " + e.getMessage());
		} catch ( InvalidClassException e ) {
			throw new RuntimeException("Heartbeat class version mismatch: " + e.getMessage());
		} catch ( IOException e ) {
			throw new RuntimeException("Reading heartbeat data: " + e.getMessage());
		}
	}

	private boolean downloadDBFile(InetAddress tcpAddress, int tcpPort, Path dbFilePath) {
		try (
				Socket tcpSocket = new Socket(tcpAddress, tcpPort);
				InputStream inStream = tcpSocket.getInputStream();
				DataInputStream dataIn = new DataInputStream(inStream);
				FileOutputStream fileOut = new FileOutputStream(dbFilePath.toFile())
		) {
			logger.info("Connected to server at {}:{}", tcpAddress, tcpPort);
			logger.info("Downloading database to '{}'...", dbFilePath);

			long fileSize;
			fileSize = dataIn.readLong();
			logger.info("File size: {} bytes", fileSize);

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
				logger.info("Database downloaded successfully");
				context = new DataBaseManager(dbFilePath.toAbsolutePath().toString(), null, null);
				return true;
			} else
				throw new RuntimeException(
						"Incomplete file transfer: received " + totalBytesRead + " of " + fileSize + " bytes");

		} catch ( EOFException e ) {
			logger.error("Failed to read file size from server: {}", e.getMessage());
		} catch ( ConnectException e ) {
			logger.error("Could not connect to server: {}", e.getMessage());
		} catch ( SocketException e ) {
			logger.error("Connection lost: {}", e.getMessage());
		} catch ( SocketTimeoutException e ) {
			logger.error("Connection timed out: {}", e.getMessage());
		} catch ( IOException e ) {
			logger.error("TCP connection: {}", e.getMessage());
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
			throw new RuntimeException("Same version but provided query");
	}

	private void handleDatabaseUpdate(Heartbeat heartbeat) throws SQLException {
		logger.info("Updating database with new data: \n\t{}\n\t{}", heartbeat.query(), Arrays.toString(
				heartbeat.params()));
		context.updateDatabase(heartbeat.query(), heartbeat.params());
	}
}