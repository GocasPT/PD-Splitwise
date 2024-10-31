package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Server.getTimeTag;

//TODO: Runnable → Thread (?)
public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private final DataBaseManager context;
	private final String name;

	public ClientHandler(Socket clientSocket, DataBaseManager context) {
		this.clientSocket = clientSocket;
		this.context = context;
		name = clientSocket.getInetAddress().getHostAddress() + ":" +
				clientSocket.getPort() + " - " +
				clientSocket.getInetAddress().getHostName();
	}

	@Override
	public void run() {
		try (
				ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
		) {
			Request request;

			try {
				//TODO: loop unitl socket != null and !socket.isClosed()
				while ((request = (Request) in.readObject()) != null) {
					System.out.println(getTimeTag() + "'" + name + "': " + request);
					Response response = request.execute(context);
					System.out.println(getTimeTag() + response);
					out.writeObject(response);
				}
			} catch ( ClassNotFoundException e ) {
				System.out.println("[ClientThread] Ocorreu um erro ao ler o objeto recebido:\n\t" + e);
			}

		} catch ( SocketException e ) {
			System.out.println("[ClientThread] Ocorreu um erro ao nivel do socket TCP:\n\t" + e);
		} catch ( IOException e ) {
			System.out.println("[ClientThread] Ocorreu um erro no acesso ao socket:\n\t" + e);
		} finally {
			System.out.println(getTimeTag() + "Client '" + name + "' disconnected");
		}
	}
}

