import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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
