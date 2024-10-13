import Message.Request.Request;
import Message.Response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Usage: java Server <port> <path_to_database>");
			return;
		}

		int listeningPort = Integer.parseInt(args[0]);

		try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
			//TODO: parse database path + check

			System.out.println("Server ready to receive clients...");

			while (true) {
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " - " + clientSocket.getInetAddress().getHostName());

				Thread clientThread = new Thread(new ClientHandler(clientSocket));
				clientThread.start();
			}
		} catch (NumberFormatException e) {
			System.out.println("[MainThread] O porto de escuta deve ser um inteiro positivo.");
		} catch (SocketException e) {
			System.out.println("[MainThread] Ocorreu um erro ao nivel do serverSocket TCP:\n\t" + e);
		} catch (IOException e) {
			System.out.println("[MainThread] Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
		}
	}

	static class ClientHandler implements Runnable {
		private final Socket clientSocket;
		private final String name;

		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
			name = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + " - " + clientSocket.getInetAddress().getHostName();
		}

		@Override
		public void run() {
			try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
			     ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
				Request request;

				try {
					while ((request = (Request) in.readObject()) != null) {
						Response response = request.execute();
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
				System.out.println("Client '" + name + "' disconnected");
			}
		}
	}
}
