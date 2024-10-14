package Server;

import Data.DatabaseManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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

		Server server = new Server(listeningPort, dbPath);
		server.start();
	}

	private void start() {
		context.initializeDatabase();
		new Thread(new HeartbeatSender(listeningPort, context)).start();

		try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {

			System.out.println("Server.Server ready to receive clients...");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client.Client connected: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " - " + clientSocket.getInetAddress().getHostName());

				new Thread(new ClientHandler(clientSocket, context)).start();
			}
		} catch (NumberFormatException e) {
			System.out.println("[MainThread] O porto de escuta deve ser um inteiro positivo.");
		} catch (SocketException e) {
			System.out.println("[MainThread] Ocorreu um erro ao nivel do serverSocket TCP:\n\t" + e);
		} catch (IOException e) {
			System.out.println("[MainThread] Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
		}
	}
}
