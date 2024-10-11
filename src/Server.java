import Message.Request.User.Login;
import Message.Request.Request;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server {
	public static void main(String[] args) {
		int listeningPort;

		if (args.length != 1) {
			System.out.println("Usage: java Server <port>");
			return;
		}

		listeningPort = Integer.parseInt(args[0]);

		try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
			while (true) {
				try (Socket clientSocket = serverSocket.accept()) {
					BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					String receivedMsg = in.readLine();
					System.out.println("Recebido \"" + receivedMsg + "\" de " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());

					PrintStream out = new PrintStream(clientSocket.getOutputStream());
					out.println("Bem-vindo :P");
					out.flush();

					// Reciver data/messages from client
					ObjectInputStream oin = new ObjectInputStream(clientSocket.getInputStream());
					Request request = (Request) oin.readObject();
					if (request instanceof Login) {
						Login login = (Login) request;
						System.out.println("Login: " + login.getUsername() + " " + login.getPassword());
					} else {
						System.out.println("Request desconhecido.");
					}

				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		} catch (NumberFormatException e) {
			System.out.println("O porto de escuta deve ser um inteiro positivo.");
		} catch (SocketException e) {
			System.out.println("Ocorreu um erro ao nivel do serverSocket TCP:\n\t" + e);
		} catch (IOException e) {
			System.out.println("Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
		}
	}
}
