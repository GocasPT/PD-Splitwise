package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.getTimeTag;

public class HeartbeatSender implements Runnable {
	private static final String MULTICAST_ADDRESS = "230.44.44.44";
	private static final int MULTICAST_PORT = 4444;
	private static final int HEARTBEAT_INTERVAL = 10;

	private final ServerSocket serverSocket;
	private final DataBaseManager context;
	private MulticastSocket socket;
	private InetAddress group;
	private Thread backupServerReceiver;

	public HeartbeatSender(DataBaseManager context) {
		try {
			this.context = context;
			serverSocket = new ServerSocket(0);
		} catch ( IOException e ) { //TODO: improve exception handling
			throw new RuntimeException(e);
		}
	}

	//TODO: improve this method
	@Override
	public void run() {
		backupServerReceiver = new Thread(new BackupServerReceiver(serverSocket, context));
		backupServerReceiver.start();

		try {
			group = InetAddress.getByName(MULTICAST_ADDRESS);
			NetworkInterface nif = NetworkInterface.getByName(MULTICAST_ADDRESS);

			try {
				socket = new MulticastSocket(MULTICAST_PORT);
				socket.joinGroup(new InetSocketAddress(group, MULTICAST_PORT), nif);
				System.out.println(getTimeTag() + "Heartbeat sender started");

				//TODO: break loop when server is stopped
				while (true) {
					Thread.sleep(HEARTBEAT_INTERVAL * 1000);
					Heartbeat heartbeat = new Heartbeat(context.getVersion(), serverSocket.getLocalPort(), null);
					ByteArrayOutputStream bOut = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(bOut);
					out.writeObject(heartbeat);
					out.flush();

					System.out.println(getTimeTag() + "Sending heartbeat: " + heartbeat);

					DatagramPacket packet = new DatagramPacket(bOut.toByteArray(), bOut.size(), group,
					                                           MULTICAST_PORT);
					socket.send(packet);
				}
			} catch ( InterruptedException e ) {
				System.out.println("[HeartbeatThread] Heartbeat sender interrupted");
			} catch ( IOException e ) {
				System.err.println("[HeartbeatThread] Heartbeat sender error: " + e.getMessage());
			} finally {
				System.out.println(getTimeTag() + "Heartbeat sender stopped");
				socket.close();
			}

		} catch ( UnknownHostException e ) {
			System.out.println("[HeartbeatThread] Unknown multicast group: " + MULTICAST_ADDRESS);
		} catch ( SocketException e ) {
			System.out.println("[HeartbeatThread] Unknown network interface: " + MULTICAST_ADDRESS);
		}
	}

	public void sendHeartbeat(String query, Object... params) {
		try {
			Heartbeat heartbeat = new Heartbeat(context.getVersion(), serverSocket.getLocalPort(), query, params);
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bOut);
			out.writeObject(heartbeat);
			out.flush();

			System.out.println(getTimeTag() + "Sending heartbeat: " + heartbeat);

			DatagramPacket packet = new DatagramPacket(bOut.toByteArray(), bOut.size(), group, MULTICAST_PORT);
			socket.send(packet);
		} catch ( IOException e ) { //TODO: improve exception handling
			throw new RuntimeException(e);
		}
	}
}
