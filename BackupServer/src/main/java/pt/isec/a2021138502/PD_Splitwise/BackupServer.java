package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat.BYTE_LENGTH;

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
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java BackupServer.BackupServer <path_to_database>");
			return;
		}

		String dbPath = args[0];

		new BackupServer(dbPath).start();
	}

	private void start() {
		InetAddress group;
		NetworkInterface nif;

		try {
			group = InetAddress.getByName(MULTICAST_ADDRESS);
			nif = NetworkInterface.getByName(MULTICAST_ADDRESS);

			try (MulticastSocket socket = new MulticastSocket(MULTICAST_PORT)) {
				socket.joinGroup(new InetSocketAddress(group, MULTICAST_PORT), nif);
				socket.setSoTimeout(INTERVAL * 1000);

				DatagramPacket packet = new DatagramPacket(new byte[BYTE_LENGTH], BYTE_LENGTH, group, MULTICAST_PORT);

				try {
					socket.receive(packet);
				} catch (SocketTimeoutException e) {
					System.err.println(getTimeTag() + "Timeout waiting for server heartbeat: " + e.getMessage());
					return;
				} catch (IOException e) {
					System.err.println(getTimeTag() + "Error receiving heartbeat packet: " + e.getMessage());
					return;
				}

				Heartbeat heartbeat;
				try (ObjectInputStream objIn = new ObjectInputStream(
						new ByteArrayInputStream(packet.getData(), 0, packet.getLength()))) {
					heartbeat = (Heartbeat) objIn.readObject();
				} catch (ClassNotFoundException e) {
					System.err.println(getTimeTag() + "Invalid heartbeat format: " + e.getMessage());
					return;
				} catch (InvalidClassException e) {
					System.err.println(getTimeTag() + "Heartbeat class version mismatch: " + e.getMessage());
					return;
				} catch (IOException e) {
					System.err.println(getTimeTag() + "Error reading heartbeat data: " + e.getMessage());
					return;
				}

				InetAddress tcpAddress = packet.getAddress();
				int tcpPort = heartbeat.tcpPort();

				if (!dbDirectory.toFile().exists()) {
					if (!dbDirectory.toFile().mkdirs()) {
						System.err.println(getTimeTag() + "Failed to create database directory: " + dbDirectory);
						return;
					}
				}
				Path dbFilePath = dbDirectory.resolve(dbFilename);

				try (
						Socket tcpSocket = new Socket(tcpAddress, tcpPort);
						InputStream inStream = tcpSocket.getInputStream();
						DataInputStream dataIn = new DataInputStream(inStream);
						FileOutputStream fileOut = new FileOutputStream(dbFilePath.toFile())
				) {
					System.out.println(getTimeTag() + "Downloading database to '" + dbFilePath + "'...");

					long fileSize;
					try {
						fileSize = dataIn.readLong();
						System.out.println(getTimeTag() + "File size: " + fileSize + " bytes");
					} catch (EOFException e) {
						System.err.println(getTimeTag() + "Failed to read file size from server: " + e.getMessage());
						return;
					}

					byte[] buffer = new byte[BYTE_LENGTH];
					int bytesRead;
					long totalBytesRead = 0;

					try {
						while (totalBytesRead < fileSize &&
								(bytesRead = dataIn.read(buffer)) != -1) {
							fileOut.write(buffer, 0, bytesRead);
							totalBytesRead += bytesRead;
							//TODO: make it as progress bar
							System.out.println(getTimeTag() + "Received " + bytesRead +
									                   " bytes (" + totalBytesRead + "/" + fileSize + ")");
						}
						fileOut.flush();
					} catch (SocketException e) {
						System.err.println(getTimeTag() + "Connection lost during file transfer: " + e.getMessage());
						return;
					} catch (IOException e) {
						System.err.println(getTimeTag() + "Error during file transfer: " + e.getMessage());
						return;
					}

					if (totalBytesRead == fileSize) {
						System.out.println(getTimeTag() + "Database downloaded successfully");
						context = new DataBaseManager(dbFilePath.toAbsolutePath().toString(), null, null);
					} else {
						System.err.println(getTimeTag() + "Incomplete file transfer: received " +
								                   totalBytesRead + " of " + fileSize + " bytes");
						return;
					}

					while (true) {
						packet = new DatagramPacket(new byte[BYTE_LENGTH], BYTE_LENGTH, group, MULTICAST_PORT);

						try {
							socket.receive(packet);
						} catch (SocketTimeoutException e) {
							System.err.println(getTimeTag() + "Timeout waiting for server heartbeat: " + e.getMessage());
							return;
						} catch (IOException e) {
							System.err.println(getTimeTag() + "Error receiving heartbeat packet: " + e.getMessage());
							return;
						}

						try (ObjectInputStream objIn = new ObjectInputStream(
								new ByteArrayInputStream(packet.getData(), 0, packet.getLength()))) {
							heartbeat = (Heartbeat) objIn.readObject();

							System.out.println("Received heartbeat: " + heartbeat);
						} catch (ClassNotFoundException e) {
							System.err.println(getTimeTag() + "Invalid heartbeat format: " + e.getMessage());
							return;
						} catch (InvalidClassException e) {
							System.err.println(getTimeTag() + "Heartbeat class version mismatch: " + e.getMessage());
							return;
						} catch (IOException e) {
							System.err.println(getTimeTag() + "Error reading heartbeat data: " + e.getMessage());
							return;
						}
					}
				} catch (ConnectException e) {
					System.err.println(getTimeTag() + "Could not connect to server: " + e.getMessage());
				} catch (SocketTimeoutException e) {
					System.err.println(getTimeTag() + "Connection timed out: " + e.getMessage());
				} catch (IOException e) {
					System.err.println(getTimeTag() + "Error in TCP connection: " + e.getMessage());
				}

				//TODO: Recive heartbeats from server
				// if no query AND different version -> close
				// if query AND same version -> close
				// if no query AND same version -> do nothing (still alive)
				// if query AND different version -> update

				/*while (true) {

				}*/

			} catch (SocketException e) {
				System.err.println(getTimeTag() + "Error configuring multicast socket: " + e.getMessage());
			} catch (IOException e) {
				System.err.println(getTimeTag() + "Error with multicast operations: " + e.getMessage());
			}
		} catch (UnknownHostException e) {
			System.err.println(getTimeTag() + "Invalid multicast address: " + MULTICAST_ADDRESS);
		} catch (SocketException e) {
			System.err.println(getTimeTag() + "Error creating network interface: " + e.getMessage());
		}
	}

	public static String getTimeTag() {
		return "<" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "> ";
	}
}