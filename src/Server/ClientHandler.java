package Server;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import static Server.Server.getTimeTag;

public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private final DatabaseManager context;
	private final String name;

	public ClientHandler(Socket clientSocket, DatabaseManager context) {
		this.clientSocket = clientSocket;
		this.context = context;
		name = clientSocket.getInetAddress().getHostAddress() + ":" +
				clientSocket.getPort() + " - " +
				clientSocket.getInetAddress().getHostName();
	}

	@Override
	public void run() {
		try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
		     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
			Request request;

			try {
				while ((request = (Request) in.readObject()) != null) {
					Response response = request.execute(context);
					out.writeObject(response);
				}
			} catch (ClassNotFoundException e) {
				System.out.println("[ClientThread] Ocorreu um erro ao ler o objeto recebido:\n\t" + e);
			}

		} catch (SocketException e) {
			System.out.println("[ClientThread] Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
		} catch (IOException e) {
			System.out.println("[ClientThread] Ocorreu um erro no acesso ao socket:\n\t" + e);
		} finally {
			System.out.println(getTimeTag() + "Client '" + name + "' disconnected");
		}
	}
}

