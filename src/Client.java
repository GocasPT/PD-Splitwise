import Message.Request.User.Login;

import java.io.*;
import java.net.*;

public class Client {
	public static final int TIMEOUT = 10;

	public static void main(String[] args) {
		InetAddress serverAddr = null;
		int port = -1;

		if (args.length != 2) {
			System.out.println("Usage: java Client <server> <port>");
			return;
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
			port = Integer.parseInt(args[1]);

			try (Socket serverSocket = new Socket(serverAddr, port)) {
				serverSocket.setSoTimeout(TIMEOUT * 1000);

				PrintStream out = new PrintStream(serverSocket.getOutputStream());
				out.println("Sou uma batata feliz :D");

				ObjectOutputStream bout = new ObjectOutputStream(serverSocket.getOutputStream());
				bout.writeObject(new Login("batata", "batata"));

				BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
				String response = in.readLine();
				System.out.println("Response: " + response);
			}

		} catch (UnknownHostException e) {
			System.out.println("Destino desconhecido:\n\t" + e);
		} catch (NumberFormatException e) {
			System.out.println("O porto do servidor deve ser um inteiro positivo.");
		} catch (SocketTimeoutException e) {
			System.out.println("Nao foi recebida qualquer resposta:\n\t" + e);
		} catch (SocketException e) {
			System.out.println("Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
		} catch (IOException e) {
			System.out.println("Ocorreu um erro no acesso ao socket:\n\t" + e);
		}
	}
}
