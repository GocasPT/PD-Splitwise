package BackupServer;

import Data.DatabaseManager;
import Message.Hearthbeat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class BackupServer {
	private static final String MULTICAST_ADDRESS = "230.44.44.44";
	private static final int MULTICAST_PORT = 4444;
	private static final int INTERVAL = 30;

	private final DatabaseManager context;

	public BackupServer(String dbPath) {
		context = new DatabaseManager(dbPath);
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java BackupServer.BackupServer <path_to_database>");
			return;
		}

		String dbPath = args[0];

		new BackupServer(dbPath).start();
	}

	public static String getTimeTag() {
		return "<" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "> ";
	}

	private void start() {
		context.initializeDatabase();

		InetAddress group;
		NetworkInterface nif;

		try {
			group = InetAddress.getByName(MULTICAST_ADDRESS);
			nif = NetworkInterface.getByName(MULTICAST_ADDRESS);

			try (MulticastSocket socket = new MulticastSocket(MULTICAST_PORT)) {
				socket.joinGroup(new InetSocketAddress(group, MULTICAST_PORT), nif);
				socket.setSoTimeout(INTERVAL * 1000);

				DatagramPacket packet = new DatagramPacket(new byte[1024], 1024, group, MULTICAST_PORT);
				socket.receive(packet);

				ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(packet.getData(), 0, packet.getLength()));
				Hearthbeat heartbeat = (Hearthbeat) in.readObject();

				System.out.println(getTimeTag() + " Packet: " + heartbeat);

			} catch (IOException e) {
				System.err.println("Heartbeat receiver error: " + e.getMessage());
			} catch (ClassNotFoundException e) {
				System.err.println("Heartbeat receiver error: " + e.getMessage());
			}
		} catch (UnknownHostException e) {
			System.out.println("[HeartbeatThread] Unknown multicast group: " + MULTICAST_ADDRESS);
		} catch (SocketException e) {
			System.out.println("[HeartbeatThread] Unknown network interface: " + MULTICAST_ADDRESS);
		}
	}
}
