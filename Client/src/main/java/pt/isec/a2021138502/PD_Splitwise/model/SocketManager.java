package pt.isec.a2021138502.PD_Splitwise.model;

import javafx.application.Platform;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.NotificaionResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.ui.NotificationManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class SocketManager {
	private final Object lock = new Object();
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Thread listenerThread;
	private Response feedbackResponse;

	//TODO: add current logged user object to this class (to use on sendRequest method when need email)
	// add current group view object to this class (to use on sendRequest method when need group id)
	// add current invite object to this class (to use on sendRequest method when need invite id)

	public SocketManager() {
	}

	public void connect(InetAddress serverAddr, int port) throws IOException { //TODO: add other exceptions
		socket = new Socket(serverAddr, port);
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());

		//TODO: create thread to listen for responses
		//TODO: create Thread to listen all object from pipe (and what is feedback and what is notification)
		listenerThread = new Thread(this::listenForMessages);
		listenerThread.start();
	}

	public void close() throws IOException {
		if (socket != null && !socket.isClosed())
			socket.close();
		if (listenerThread != null && listenerThread.isAlive())
			listenerThread.interrupt();
	}

	public Response sendRequest(Request request) throws IOException, InterruptedException {
		synchronized (lock) {
			output.writeObject(request);
			output.flush();

			lock.wait();
			return feedbackResponse;
		}
	}

	//TODO: make this method as Runnable class
	public void listenForMessages() {
		try {
			while (socket != null && !socket.isClosed()) {
				Response response = (Response) input.readObject();
				handleIncomingMessage(response);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException on 'listenForMessages': " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException on 'listenForMessages': " + e.getMessage());
		}
	}

	private void handleIncomingMessage(Response response) {
		synchronized (lock) {
			if (isFeedbackResponse(response)) {
				feedbackResponse = response;
				lock.notify();
			} else {
				Platform.runLater(() -> NotificationManager.showNotification((NotificaionResponse) response));
			}
		}
	}

	private boolean isFeedbackResponse(Response response) {
		return !(response instanceof NotificaionResponse);
	}
}
