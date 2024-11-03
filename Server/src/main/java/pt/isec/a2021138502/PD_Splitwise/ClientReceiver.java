package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Manager.SessionManager;
import pt.isec.a2021138502.PD_Splitwise.Thread.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.getTimeTag;

public class ClientReceiver implements Runnable {
	private final int listeningPort;
	private final DataBaseManager context;

	public ClientReceiver(int listeningPort, DataBaseManager context) {
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
				clientSocket.setSoTimeout(Server.TIMEOUT_CLIENT_SOCKET * 1000);
				//new Thread(new ClientHandler(clientSocket, context)).start();
				new Thread(
						new ClientHandler(clientSocket, new SessionManager()),
						clientSocket.getInetAddress().toString()
				).start();
			}
		} catch ( NumberFormatException e ) {
			System.out.println("[MainThread] O porto de escuta deve ser um inteiro positivo");
		} catch ( SocketException e ) {
			System.out.println("[MainThread] Ocorreu um erro ao nível do serverSocket TCP:\n\t" + e);
		} catch ( IOException e ) {
			System.out.println("[MainThread] Ocorreu um erro no acesso ao serverSocket:\n\t" + e);
		}
	}
}
