package pt.isec.a2021138502.PD_Splitwise.Server.Manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Server.Thread.BackupServerReceiver;
import pt.isec.a2021138502.PD_Splitwise.Server.Thread.HeartbeatSender;

import java.io.IOException;
import java.net.*;

public class HeartbeatManager {
	private static final Logger logger = LoggerFactory.getLogger(HeartbeatManager.class);
	private final String MULTICAST_ADDRESS = "230.44.44.44";
	private final int MULTICAST_PORT = 4444;
	private final MulticastSocket multicastSocket;
	private final ServerSocket backupServerSocket;
	private final InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
	private final DataBaseManager dbManager; //TODO: check if this is needed
	private boolean isRunning;
	private final HeartbeatSender heartbeatSender;
	private final BackupServerReceiver backupServerReceiver;

	public HeartbeatManager(boolean isRunning, DataBaseManager dbManager) throws IOException {
		this.isRunning = isRunning;
		this.multicastSocket = new MulticastSocket(MULTICAST_PORT);
		multicastSocket.joinGroup(
				new InetSocketAddress(group, MULTICAST_PORT),
				NetworkInterface.getByName(MULTICAST_ADDRESS)
		);
		this.backupServerSocket = new ServerSocket(0);
		this.dbManager = dbManager;
		heartbeatSender = new HeartbeatSender(this.isRunning, multicastSocket, group, backupServerSocket, dbManager);
		backupServerReceiver = new BackupServerReceiver(this.isRunning, backupServerSocket, dbManager);
	}

	public void startHeartbeat() {
		logger.debug("Starting threads");
		heartbeatSender.start();
		backupServerReceiver.start();
	}

	public void stopHeartbeat() {
		//TODO: implement this method
		// stop the loop and close the socket
		logger.debug("Stopping threads");
		isRunning = false;

		try {
			//backupServerReceiver.join();
			//heartbeatSender.join();
			multicastSocket.leaveGroup(group); //TODO: ??
			multicastSocket.close();
		} catch ( IOException e ) {
			logger.error(e.getMessage());
		}
	}

	public HeartbeatSender getHeartbeatSender() {
		return heartbeatSender;
	}

	public BackupServerReceiver getBackupServerReceiver() {
		return backupServerReceiver;
	}
}
