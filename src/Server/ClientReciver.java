package Server;

import Data.DatabaseManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static Server.Server.getTimeTag;

public class ClientReciver implements Runnable {
	private final int listeningPort;
	private final DatabaseManager context;

	public ClientReciver(int listeningPort, DatabaseManager context) {
		this.listeningPort = listeningPort;
		this.context = context;
	}

	@Override
	public void run() {
		try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
			System.out.println(getTimeTag() + "Server ready to receive clients...");

			//TODO: need to add something where? (flag to stop loop?)
			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println(getTimeTag() + "Client '" +
						clientSocket.getInetAddress().getHostAddress() + ":" +
						clientSocket.getPort() + " - " +
						clientSocket.getInetAddress().getHostName() +
						"' connected");

				new Thread(new ClientHandler(clientSocket, context)).start();
			}
		} catch (NumberFormatException e) {
			System.out.println("[MainThread] O porto de escuta deve ser um inteiro positivo");
		} catch (SocketException e) {
			System.out.println("[MainThread] Ocorreu um erro ao nivel do serverSocket TCP:\n\t" + e);
		} catch (IOException e) {
			System.out.println("[MainThread] Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
		}
	}
}
