package pt.isec.a2021138502.PD_Splitwise.Server.Thread;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.Login;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.Register;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.NotificaionResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Server.Manager.SessionManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Terminal.utils.getTimeTag;

public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private final ObjectOutputStream out;
	private final ObjectInputStream in;
	private final SessionManager sessionManager;
	private final DataBaseManager context;
	private final String host;
	private String email;

	//TODO: improve exception handling
	public ClientHandler(Socket clientSocket, SessionManager sessionManager, DataBaseManager context) throws IOException {
		this.clientSocket = clientSocket;
		this.out = new ObjectOutputStream(clientSocket.getOutputStream());
		this.in = new ObjectInputStream(clientSocket.getInputStream());
		this.sessionManager = sessionManager;
		this.context = context;
		this.host = clientSocket.getInetAddress().getHostAddress() + ":" +
				clientSocket.getPort() + " - " +
				clientSocket.getInetAddress().getHostName();
	}

	@Override
	public void run() {
		System.out.println(getTimeTag() + "Client '" + host + "' connected");

		try ( ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream()) ) {
			Request request;

			try {
				// Block for Login or Register request
				// Need to be logged in to access other requests
				while (email == null) {
					request = (Request) in.readObject();
					System.out.println(getTimeTag() + "Client '" + host + "': " + request);
					if (request instanceof Login) {
						Response response = request.execute(context);
						if (!response.isSuccess()) {
							out.writeObject(response);
							return;
						} else {
							email = ((Login) request).email();
							out.writeObject(response);
							sessionManager.addSession(email, this);
						}
					} else if (request instanceof Register) {
						Response response = request.execute(context);
						out.writeObject(response);
					} else {
						System.out.println(getTimeTag() + "Client '" + host + "' not logged in");
						return;
					}
				}

				clientSocket.setSoTimeout(0); // "Disable" timeout

				// Main loop
				while (!clientSocket.isClosed()) {
					request = (Request) in.readObject();
					System.out.println(getTimeTag() + "(" + email + ")" + "'" + host + "': " + request);
					Response response = request.execute(context);
					System.out.println(getTimeTag() + response);
					out.writeObject(response);
				}
			} catch ( ClassNotFoundException e ) {
				System.out.println("[ClientThread] Ocorreu um erro ao ler o objeto recebido:\n\t" + e);
			}

		} catch ( SocketException e ) {
			System.out.println("[ClientThread] Ocorreu um erro ao nível do socket TCP:\n\t" + e);
		} catch ( IOException e ) {
			System.out.println("[ClientThread] Ocorreu um erro no acesso ao socket:\n\t" + e);
		} finally {
			System.out.println(getTimeTag() + "Client '" + host + "' disconnected");
			if (email != null)
				sessionManager.removeSession(email);
			try {
				clientSocket.close();
			} catch ( IOException e ) {
				System.out.println("[ClientThread] Ocorreu um erro ao fechar o socket:\n\t" + e);
			}
		}
	}

	//TODO: what this method have as argument?
	public void sendMessage() {
		//TODO: implement this method
	}

	public void sendNotification(NotificaionResponse notification) {
		/*try {
			synchronized (out) {
				System.out.println("[ClientThread] Sending notification to '" + email + "': " + notification);
				out.writeObject(notification);
				out.flush();
			}
		} catch ( IOException e ) {
			System.out.println("[ClientThread] Ocorreu um erro ao enviar a notificação:\n\t" + e);
		}*/
	}
}

