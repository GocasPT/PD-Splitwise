package Server;

import Data.DatabaseManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HeartbeatSender implements Runnable {
	private static final String MULTICAST_ADDRESS = "230.44.44.44";
	private static final int MULTICAST_PORT = 4444;
	private static final int INTERVAL_MS = 10;

	private final int tcpPort;
	private final DatabaseManager context;

	public HeartbeatSender(int tcpPort, DatabaseManager context) {
		this.tcpPort = tcpPort;
		this.context = context;
	}

	@Override
	public void run() {
		try (DatagramSocket socket = new DatagramSocket()) {
			InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);

			while (true) {
				String message = "DB_VERSION=" + context.getVersion() + ";PORT=" + tcpPort;
				byte[] buffer = message.getBytes();

				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, MULTICAST_PORT);
				socket.send(packet);

				Thread.sleep(INTERVAL_MS);  // Send heartbeat every 10ms.
			}
		} catch (IOException | InterruptedException e) {
			System.err.println("Heartbeat sender error: " + e.getMessage());
		}
	}
}
