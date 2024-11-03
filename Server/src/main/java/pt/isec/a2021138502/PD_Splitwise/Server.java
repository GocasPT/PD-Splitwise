package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;

public class Server {
	public static final int TIMEOUT_CLIENT_SOCKET = 60;
	private static NotificationManager notificationManager;
	private static ChangeManager changeManager;
	private final int listeningPort;
	private final DataBaseManager context;
	private HeartbeatSender heartbeatSenderThread;

	public Server(int listeningPort, String dbPath) {
		this.listeningPort = listeningPort;
		notificationManager = new NotificationManager();
		changeManager = new ChangeManager();
		context = new DataBaseManager(dbPath, notificationManager, changeManager);
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
		} catch ( RuntimeException e ) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private void start() {
		heartbeatSenderThread = new HeartbeatSender(context);
		changeManager.registe(heartbeatSenderThread);
		new Thread(heartbeatSenderThread).start();
		new Thread(new ClientReceiver(listeningPort, context)).start();
		//TODO: what the main thread gonna do?
	}

	public static NotificationManager getNotificationManager() {
		return notificationManager;
	}
}
