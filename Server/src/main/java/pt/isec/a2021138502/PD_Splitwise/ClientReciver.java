package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Server.getTimeTag;

public class ClientReciver implements Runnable {
	private final int listeningPort;
	private final DataBaseManager context;

	public ClientReciver(int listeningPort, DataBaseManager context) {
		this.listeningPort = listeningPort;
		this.context = context;
	}

	@Override
	public void run() {
		try ( ServerSocket serverSocket = new ServerSocket(listeningPort) ) {
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
		} catch ( NumberFormatException e ) {
			System.out.println("[MainThread] O porto de escuta deve ser um inteiro positivo");
		} catch ( SocketException e ) {
			System.out.println("[MainThread] Ocorreu um erro ao nivel do serverSocket TCP:\n\t" + e);
		} catch ( IOException e ) {
			System.out.println("[MainThread] Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
		}
	}
}
