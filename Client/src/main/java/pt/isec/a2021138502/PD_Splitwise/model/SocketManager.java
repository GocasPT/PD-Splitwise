package pt.isec.a2021138502.PD_Splitwise.model;

import javafx.application.Platform;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.NotificaionResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.ui.NotificationManager;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

public class SocketManager {
	private final Object lock = new Object();

	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private List<ClientObserver> observers;

	private Response feedbackResponse;
	//TODO: check if need this variable
	private Thread listenerThread;

	public void connect(InetAddress serverAdder, int port) throws IOException { //TODO: add other exceptions
		socket = new Socket(serverAdder, port);
		output = new ObjectOutputStream(socket.getOutputStream());
		input = new ObjectInputStream(socket.getInputStream());

		//TODO: check if need this variable
		listenerThread = new Thread(this::listenForMessages);
		listenerThread.start();
	}

	//TODO: improve catch blocks
	// throw exception
	// maybe this gonna be deleted
	private void listenForMessages() {
		try {
			while (socket != null && !socket.isClosed()) {
				Object readObject = input.readObject();
				if (readObject instanceof Response response)
					synchronized (lock) {
						if (!(response instanceof NotificaionResponse notificationResponse)) {
							feedbackResponse = response;
							lock.notify();
						} else {
							System.out.println("Notification received: " + notificationResponse); //TODO: DEBUG SOUT
							Platform.runLater(() -> NotificationManager.showNotification(notificationResponse));
						}
					}
			}
			//TODO: improve catch blocks (show error message on MainGUI/Popup)
		} catch ( SocketException e ) {
			System.out.println("SocketException on 'listenForMessages': " + e.getMessage());
		} catch ( ClassNotFoundException e ) {
			System.out.println("ClassNotFoundException on 'listenForMessages': " + e.getMessage());
		} catch ( InvalidClassException e ) {
			System.out.println("InvalidClassException on 'listenForMessages': " + e.getMessage());
		} catch ( IOException e ) {
			System.out.println("IOException on 'listenForMessages': " + e.getMessage());
		} finally {
			//Platform.runLater(() -> ); //TODO: show error message
			Platform.exit();
		}
	}

	public void close() throws IOException {
		if (socket != null && !socket.isClosed())
			socket.close();
		if (listenerThread != null && listenerThread.isAlive())
			listenerThread.interrupt();
	}

	public boolean addObservers(ClientObserver observer) {
		//TODO: implement this method
		return true;
	}

	public Response sendRequest(Request request) throws IOException, InterruptedException {
		synchronized (lock) {
			output.writeObject(request);
			output.flush();

			lock.wait();
			return feedbackResponse;
		}
	}

	public boolean removeObservers(ClientObserver observer) {
		//TODO: implement this method
		return true;
	}

	public void notifyObservers() {
		//TODO: implement this method
	}
}
