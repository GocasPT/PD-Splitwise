package pt.isec.a2021138502.PD_Splitwise.Manager;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class HeartbeatManager {
	private final String MULTICAST_ADDRESS = "230.44.44.44";
	private final int MULTICAST_PORT = 4444;
	private final int HEARTBEAT_INTERVAL = 10;

	private final MulticastSocket socket;
	private final DataBaseManager dbManager;
	private final int version;

	public HeartbeatManager(DataBaseManager dbManager) throws IOException {
		this.socket = new MulticastSocket(MULTICAST_PORT);
		socket.joinGroup(
				new InetSocketAddress(
						InetAddress.getByName(MULTICAST_ADDRESS),
						MULTICAST_PORT),
				NetworkInterface.getByName(MULTICAST_ADDRESS)
		);
		this.dbManager = dbManager;
		this.version = dbManager.getVersion();
	}

	public void startHeartbeat() {
		//TODO: implement this method
		// loop to send heartbeats
	}

	public void stopHeartbeat() {
		//TODO: implement this method
		// stop the loop and close the socket
	}

	public void sendUpdate(String query) {
		//TODO: implement this method
		// send heartbeat with the query
	}
}
