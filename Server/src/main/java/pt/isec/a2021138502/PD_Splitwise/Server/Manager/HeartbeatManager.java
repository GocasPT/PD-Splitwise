package pt.isec.a2021138502.PD_Splitwise.Server.Manager;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Server.Thread.BackupServerReceiver;
import pt.isec.a2021138502.PD_Splitwise.Server.Thread.HeartbeatSender;

import java.io.IOException;
import java.net.*;

import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.getTimeTag;

public class HeartbeatManager {
	private final String MULTICAST_ADDRESS = "230.44.44.44";
	private final int MULTICAST_PORT = 4444;
	private final MulticastSocket multicastSocket;
	private final ServerSocket backupServerSocket;
	private final InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
	private final DataBaseManager dbManager; //TODO: check if this is needed
	private boolean isRunning;
	private final HeartbeatSender heartbeatSender;
	private final BackupServerReceiver backupServerReceiver;

	public HeartbeatManager(boolean isRunnig, DataBaseManager dbManager) throws IOException {
		this.isRunning = isRunnig;
		this.multicastSocket = new MulticastSocket(MULTICAST_PORT);
		multicastSocket.joinGroup(
				new InetSocketAddress(group, MULTICAST_PORT),
				NetworkInterface.getByName(MULTICAST_ADDRESS)
		);
		this.backupServerSocket = new ServerSocket(0);
		this.dbManager = dbManager;
		heartbeatSender = new HeartbeatSender(isRunning, multicastSocket, group, backupServerSocket, dbManager);
		backupServerReceiver = new BackupServerReceiver(isRunning, backupServerSocket, dbManager);
	}

	public void startHeartbeat() {
		heartbeatSender.start();
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

	public HeartbeatSender getHeartbeatSender() {
		return heartbeatSender;
	}

	public BackupServerReceiver getBackupServerReceiver() {
		return backupServerReceiver;
	}
}
