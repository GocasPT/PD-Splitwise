package pt.isec.a2021138502.PD_Splitwise.Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Server.Manager.HeartbeatManager;
import pt.isec.a2021138502.PD_Splitwise.Server.Manager.SessionManager;
import pt.isec.a2021138502.PD_Splitwise.Server.Runnable.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	public static final int TIMEOUT_CLIENT_SOCKET = 60;
	private final ServerSocket serverSocket;
	private final HeartbeatManager heartbeatManager;
	private final SessionManager sessionManager;
	private final DataBaseManager dbManager;
	private volatile boolean isRunning;

	public Server(int listeningPort, String dbPath) {
		try {
			isRunning = true;
			dbManager = new DataBaseManager(dbPath);
			serverSocket = new ServerSocket(listeningPort);
			heartbeatManager = new HeartbeatManager(isRunning, dbManager);
			if (!dbManager.addDBChangeObserver(heartbeatManager.getHeartbeatSender()))
				throw new RuntimeException("Failed to add observer to DataBaseManager"); //TODO: improve this
			sessionManager = new SessionManager();

			start();
		} catch ( IOException e ) {
			throw new RuntimeException("IOException in 'Server': " + e.getMessage()); //TODO: improve this
		} finally {
			stop();
		}
	}

	private void start() {
		heartbeatManager.startHeartbeat();

		//TODO: see if this can be improved
		logger.info("Server ready to receive clients...");
		try {
			while (isRunning) {
				Socket clientSocket = serverSocket.accept();
				clientSocket.setSoTimeout(Server.TIMEOUT_CLIENT_SOCKET * 1000);
				//TODO: Runnable VS Thread
				new Thread(
						new ClientHandler(clientSocket, sessionManager, dbManager),
						clientSocket.getInetAddress().toString().replaceFirst("/", "")
				).start();
			}
		} catch ( SocketException e ) {
			throw new RuntimeException(e); //TODO: improve this
		} catch ( IOException e ) {
			throw new RuntimeException(e); //TODO: improve this
		}
	}

	//TODO: check this method
	private void stop() {
		isRunning = false;

		try {
			heartbeatManager.stopHeartbeat();

			serverSocket.close();
		} catch ( Exception e ) {
			logger.error(e.getMessage());
		}
	}
}
