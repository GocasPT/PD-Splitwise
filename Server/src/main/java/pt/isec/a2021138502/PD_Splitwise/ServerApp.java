package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Server.Server;

public class ServerApp {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java Server.Server <port> <path_to_database>");
			return;
		}

		int listeningPort = Integer.parseInt(args[0]);
		String dbPath = args[1];

		try {
			new Server(listeningPort, dbPath);
		} catch ( RuntimeException e ) {
			System.out.println("RuntimeException in 'Server': " + e.getMessage());
		}
	}
}
