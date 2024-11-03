package pt.isec.a2021138502.PD_Splitwise.Server.Manager;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Server.HeartbeatSender;
import pt.isec.a2021138502.PD_Splitwise.Server.Thread.BackupServerReceiver;

import java.io.IOException;
import java.net.*;

import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.getTimeTag;

public class HeartbeatManager {
	private final String MULTICAST_ADDRESS = "230.44.44.44";
	private final int MULTICAST_PORT = 4444;

	private final MulticastSocket multicastSocket;
	private final ServerSocket backupServerSocket;
	private final InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
	private boolean isRunning;
	private final DataBaseManager dbManager;
	private Thread heartbeatSender;
	private Thread backupServerReceiver;

	public HeartbeatManager(boolean isRunnig, DataBaseManager dbManager) throws IOException {
		this.isRunning = isRunnig;
		this.multicastSocket = new MulticastSocket(MULTICAST_PORT);
		multicastSocket.joinGroup(
				new InetSocketAddress(group, MULTICAST_PORT),
				NetworkInterface.getByName(MULTICAST_ADDRESS)
		);
		this.backupServerSocket = new ServerSocket(0);
		this.dbManager = dbManager;
	}

	public void startHeartbeat() {
		heartbeatSender = new HeartbeatSender(isRunning, multicastSocket, group, dbManager);
		heartbeatSender.start();
		backupServerReceiver = new BackupServerReceiver(isRunning, backupServerSocket, dbManager);
		backupServerReceiver.start();
	}

	public void stopHeartbeat() {
		//TODO: implement this method
		// stop the loop and close the socket
		isRunning = false;

		try {
			//backupServerReceiver.join();
			//heartbeatSender.join();
			multicastSocket.leaveGroup(group);
			multicastSocket.close();
		} catch ( IOException e ) {
			System.err.println(getTimeTag() + "Heartbeat sender error: " + e.getMessage());
		}
	}

	public void sendUpdate(String query /*, Object... params*/) {
		//TODO: implement this method
		// send heartbeat with the query
		/*try {
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
		}*/
	}

	private void heartbeatSend() {

	}
}
