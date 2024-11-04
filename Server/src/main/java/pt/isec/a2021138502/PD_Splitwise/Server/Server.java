package pt.isec.a2021138502.PD_Splitwise.Server;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Server.Manager.HeartbeatManager;
import pt.isec.a2021138502.PD_Splitwise.Server.Manager.SessionManager;
import pt.isec.a2021138502.PD_Splitwise.Server.Thread.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.getTimeTag;

public class Server {
	public static final int TIMEOUT_CLIENT_SOCKET = 60;
	private volatile boolean isRunning;
	private final ServerSocket serverSocket;
	private final HeartbeatManager heartbeatManager;
	private final SessionManager sessionManager;
	private final DataBaseManager dbManager;

	public Server(int listeningPort, String dbPath) {
		try {
			isRunning = true;
			dbManager = new DataBaseManager(dbPath);
			serverSocket = new ServerSocket(listeningPort);
			heartbeatManager = new HeartbeatManager(isRunning, dbManager);
			sessionManager = new SessionManager();

			start();
		} catch ( IOException e ) {
			throw new RuntimeException("IOException in 'Server': " + e.getMessage()); //TODO: improve this
		} finally {
			stop();
		}
	}

	private void start() {
		//TODO:
		// - create heartbeat thread
		// client receiver routine
		heartbeatManager.startHeartbeat();

		System.out.println(getTimeTag() + "Server ready to receive clients...");

		//TODO: need to add something where? (flag to stop loop?)
		try {
			while (isRunning) {
				Socket clientSocket = serverSocket.accept();
				clientSocket.setSoTimeout(Server.TIMEOUT_CLIENT_SOCKET * 1000);
				new Thread(
						new ClientHandler(clientSocket, sessionManager, dbManager),
						clientSocket.getInetAddress().toString()
				).start();
			}
		/*} catch ( SocketException e ) {
			throw new RuntimeException(e);*/
		} catch ( IOException e ) {
			throw new RuntimeException(e);
		}
	}

	private void stop() {
		//TODO:
		// - wait to finish all threads
		// - close server socket
		isRunning = false;

		try {
			heartbeatManager.stopHeartbeat();

			serverSocket.close();
		} catch ( Exception e ) {
			System.out.println("Exception in 'Server.stop': " + e.getMessage());
		}
	}
}
