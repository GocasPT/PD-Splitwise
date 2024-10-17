package Server;

import Data.DatabaseManager;
import Message.Hearthbeat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

import static Server.Server.getTimeTag;

public class HeartbeatSender implements Runnable {
	private static final String MULTICAST_ADDRESS = "230.44.44.44";
	private static final int MULTICAST_PORT = 4444;
	private static final int INTERVAL = 10;

	private final DatabaseManager context;

	public HeartbeatSender(DatabaseManager context) {
		this.context = context;
	}

	@Override
	public void run() {
		int tcpPort = 6001;

		try (ServerSocket serverSocket = new ServerSocket(tcpPort)) {

			InetAddress group;
			NetworkInterface nif;

			try {
				group = InetAddress.getByName(MULTICAST_ADDRESS);
				nif = NetworkInterface.getByName(MULTICAST_ADDRESS);

				try (MulticastSocket socket = new MulticastSocket(MULTICAST_PORT)) {
					socket.joinGroup(new InetSocketAddress(group, MULTICAST_PORT), nif);
					System.out.println(getTimeTag() + "Heartbeat sender started");

					//TODO: break loop when server is stopped
					while (true) {
						Hearthbeat heartbeat = new Hearthbeat(context.getVersion(), tcpPort);
						ByteArrayOutputStream bOut = new ByteArrayOutputStream();
						ObjectOutputStream out = new ObjectOutputStream(bOut);
						out.writeObject(heartbeat);
						out.flush();

						System.out.println(getTimeTag() + "Sending heartbeat: " + heartbeat);

						DatagramPacket packet = new DatagramPacket(bOut.toByteArray(), bOut.size(), group, MULTICAST_PORT);
						socket.send(packet);

						Thread.sleep(INTERVAL * 1000);
					}
				} catch (InterruptedException e) {
					System.out.println("[HeartbeatThread] Heartbeat sender interrupted");
				} catch (IOException e) {
					System.err.println("[HeartbeatThread] Heartbeat sender error: " + e.getMessage());
				} finally {
					System.out.println(getTimeTag() + "Heartbeat sender stopped");
				}

			} catch (UnknownHostException e) {
				System.out.println("[HeartbeatThread] Unknown multicast group: " + MULTICAST_ADDRESS);
			} catch (SocketException e) {
				System.out.println("[HeartbeatThread] Unknown network interface: " + MULTICAST_ADDRESS);
			}
		} catch (IOException e) {
			System.out.println("[HeartbeatThread] Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
		}
	}

}
