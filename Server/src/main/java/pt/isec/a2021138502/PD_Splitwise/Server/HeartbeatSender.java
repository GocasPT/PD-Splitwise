package pt.isec.a2021138502.PD_Splitwise.Server;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Heartbeat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.getTimeTag;

public class HeartbeatSender extends Thread {
	private final int HEARTBEAT_INTERVAL = 10;
	private final MulticastSocket multicastSocket;
	private final InetAddress group;
	private final DataBaseManager dbManager;
	private final boolean isRunning;

	public HeartbeatSender(boolean isRunning, MulticastSocket multicastSocket, InetAddress group, DataBaseManager dbManager) {
		super("HeartbeatSender");
		this.isRunning = isRunning;
		this.multicastSocket = multicastSocket;
		this.group = group;
		this.dbManager = dbManager;
	}

	//TODO: improve this method
	@Override
	public void run() {
		try {
			System.out.println(getTimeTag() + "Heartbeat sender started");

			//TODO: break loop when server is stopped
			while (isRunning) {
				Thread.sleep(HEARTBEAT_INTERVAL * 1000);
				Heartbeat heartbeat = new Heartbeat(dbManager.getVersion(), multicastSocket.getLocalPort(), null);
				ByteArrayOutputStream bOut = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(bOut);
				out.writeObject(heartbeat);
				out.flush();

				System.out.println(getTimeTag() + "Sending heartbeat: " + heartbeat);

				DatagramPacket packet = new DatagramPacket(bOut.toByteArray(), bOut.size(), group,
				                                           multicastSocket.getPort());
				multicastSocket.send(packet);
			}
		} catch ( InterruptedException e ) {
			System.out.println(getTimeTag() + "Heartbeat sender stopped");
		} catch ( IOException e ) {
			System.err.println(getTimeTag() + "Heartbeat sender error: " + e.getMessage());
		}
	}
}
