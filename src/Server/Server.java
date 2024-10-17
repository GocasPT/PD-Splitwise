package Server;

import Data.DatabaseManager;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Server {
	private final int listeningPort;
	private final DatabaseManager context;

	public Server(int listeningPort, String dbPath) {
		this.listeningPort = listeningPort;
		context = new DatabaseManager(dbPath);
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
		} catch (RuntimeException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	public static String getTimeTag() {
		return "<" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "> ";
	}

	private void start() {
		context.initializeDatabase();
		new Thread(new HeartbeatSender(context)).start();
		new Thread(new ClientReciver(listeningPort, context)).start();

		//TODO: what the main thread gonna do?
	}
}
