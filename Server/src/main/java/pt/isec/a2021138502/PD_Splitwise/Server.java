package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Manager.HeartbeatManager;
import pt.isec.a2021138502.PD_Splitwise.Manager.SessionManager;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	public static final int TIMEOUT_CLIENT_SOCKET = 60;

	//private static NotificationManager notificationManager;
	//private static ChangeManager changeManager;

	//private final int listeningPort;
	private final ServerSocket serverSocket;
	private final HeartbeatManager heartbeatManager;
	private final SessionManager sessionManager;
	private final DataBaseManager dbManager;

	private HeartbeatSender heartbeatSenderThread;

	public Server(int listeningPort, String dbPath) throws IOException {
		//this.listeningPort = listeningPort;
		dbManager = new DataBaseManager(dbPath/*, notificationManager, changeManager*/);
		serverSocket = new ServerSocket(listeningPort);
		heartbeatManager = new HeartbeatManager(dbManager);
		sessionManager = new SessionManager();

		//notificationManager = new NotificationManager();
		//changeManager = new ChangeManager();
	}

	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java Server.Server <port> <path_to_database>");
			return;
		}

		int listeningPort = Integer.parseInt(args[0]);
		String dbPath = args[1];

		try {
			new Server(listeningPort, dbPath).start();
		} catch ( IOException e ) {
			System.out.println("IOException in 'Server': " + e.getMessage());
		} catch ( RuntimeException e ) {
			System.out.println("RuntimeException in 'Server': " + e.getMessage());
		}
	}

	private void start() {
		/*heartbeatSenderThread = new HeartbeatSender(dbManager);
		changeManager.registe(heartbeatSenderThread);
		new Thread(heartbeatSenderThread).start();
		new Thread(new ClientReceiver(listeningPort, dbManager)).start();*/
		//TODO: what the main thread gonna do?
	}

	//TODO: what this method should do?
	private void stop() {

	}

	/*public static NotificationManager getNotificationManager() {
		return notificationManager;
	}*/
}
