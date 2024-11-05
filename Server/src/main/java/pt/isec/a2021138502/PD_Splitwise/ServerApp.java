package pt.isec.a2021138502.PD_Splitwise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.a2021138502.PD_Splitwise.Server.Server;

public class ServerApp {
	private static final Logger logger = LoggerFactory.getLogger(ServerApp.class);

	public static void main(String[] args) {
		if (args.length != 2) {
			logger.error("Usage: java Server.Server <port> <path_to_database>"); //TODO: check this later
			return;
		}

		int listeningPort = Integer.parseInt(args[0]);
		String dbPath = args[1];

		try {
			new Server(listeningPort, dbPath);
		} catch ( RuntimeException e ) {
			logger.error(e.getMessage());
		}
	}
}
