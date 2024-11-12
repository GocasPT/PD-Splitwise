package pt.isec.pd.server.PLACE_HOLDER.Runnable;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.pd.server.PLACE_HOLDER.Manager.SessionManager;
import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.request.User.Login;
import pt.isec.pd.sharedLib.network.request.User.Register;
import pt.isec.pd.sharedLib.network.response.NotificaionResponse;
import pt.isec.pd.sharedLib.network.response.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
	private final Socket clientSocket;
	private final ObjectOutputStream out;
	private final ObjectInputStream in;
	private final SessionManager sessionManager;
	private final DataBaseManager context;
	private String email;

	//TODO: improve exception handling
	public ClientHandler(Socket clientSocket, SessionManager sessionManager, DataBaseManager context) throws IOException {
		this.clientSocket = clientSocket;
		this.out = new ObjectOutputStream(clientSocket.getOutputStream());
		this.in = new ObjectInputStream(clientSocket.getInputStream());
		this.sessionManager = sessionManager;
		this.context = context;
	}

	@Override
	public void run() {
		logger.info("Client connected");

		try {
			Request request;

			try {
				// Block for Login or Register request
				// Need to be logged in to access other requests
				while (email == null) {
					request = (Request) in.readObject();
					logger.info("Request: {}", request);
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
						logger.info("Client not logged in");
						Response response = new Response(false, "Not logged in");
						out.writeObject(response);
						return;
					}
				}

				clientSocket.setSoTimeout(0); // "Disable" timeout

				// Main loop
				while (!clientSocket.isClosed()) {
					request = (Request) in.readObject();
					logger.info("({}) request: {}", email, request);
					Response response = request.execute(context);
					logger.info("({}) response: {}", email, response);
					out.writeObject(response);
				}
			} catch ( ClassNotFoundException e ) {
				logger.error("ClassNotFoundException: {}", e.getMessage());
			}

		} catch ( SocketException e ) {
			logger.error("SocketException: {}", e.getMessage());
		} catch ( IOException e ) {
			logger.error("IOException: {}", e.getMessage());
		} finally {
			logger.info("Client disconnected");
			if (email != null)
				sessionManager.removeSession(email);
			try {
				clientSocket.close();
			} catch ( IOException e ) {
				logger.error("Closing socket: {}", e.getMessage());
			}
		}
	}

	//TODO: what this method have as argument?
	public void sendMessage(NotificaionResponse notification) throws IOException {
		//TODO: implement this method
		logger.debug("Sending notification to user");
		out.writeObject(notification);
		out.flush();
	}

	/*public void sendNotification(NotificaionResponse notification) {
		try {
			synchronized (out) {
				System.out.println("[ClientThread] Sending notification to '" + email + "': " + notification);
				out.writeObject(notification);
				out.flush();
			}
		} catch ( IOException e ) {
			System.out.println("[ClientThread] Ocorreu um erro ao enviar a notificação:\n\t" + e);
		}
	}*/
}

