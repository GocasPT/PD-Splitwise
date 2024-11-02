package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.Login;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.User.Register;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.NotificaionResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import static pt.isec.a2021138502.PD_Splitwise.Server.getTimeTag;

public class ClientHandler implements Runnable {
	private final Socket clientSocket;
	private final DataBaseManager context;
	private final String host;
	private String email;

	private ObjectOutputStream out;

	public ClientHandler(Socket clientSocket, DataBaseManager context) {
		this.clientSocket = clientSocket;
		this.context = context;
		host = clientSocket.getInetAddress().getHostAddress() + ":" +
				clientSocket.getPort() + " - " +
				clientSocket.getInetAddress().getHostName();
	}

	@Override
	public void run() {
		try {
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

			Request request;

			try {
				//TODO: wait for LOGIN/REGISTER request to get guestEmail
				// [x] LOGIN
				// [ ] REGISTER
				// loop until login success
				request = (Request) in.readObject();
				if (request instanceof Login) {
					Response response = request.execute(context);
					email = ((Login) request).email();
					out.writeObject(response);
					//TODO: register client in NotificationManager
					Server.getNotificationManager().registerClient(email, this);
				} else if (request instanceof Register) {
					//TODO: logic on register request
				} else {
					System.out.println(getTimeTag() + "Client '" + host + "' not logged in");
					return;
				}

				clientSocket.setSoTimeout(0); // "Disable" timeout

				//TODO: loop unitl socket != null and !socket.isClosed()
				while ((request = (Request) in.readObject()) != null) {
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
				Server.getNotificationManager().unregisterClient(email);
		}
	}

	public void sendNotification(NotificaionResponse notification) {
		try {
			synchronized (out) {
				System.out.println("[ClientThread] Sending notification to '" + email + "': " + notification);
				//TODO: send notification object
				out.writeObject(notification);
				out.reset();
				out.flush();
			}
		} catch ( IOException e ) {
			System.out.println("[ClientThread] Ocorreu um erro ao enviar a notificação:\n\t" + e);
		}
	}
}

