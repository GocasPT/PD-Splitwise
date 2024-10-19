package pt.isec.a2021138502.PD_Splitwise.model;

import javafx.application.Platform;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.ui.ClientGUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.function.Consumer;

public class SocketManager {
	private static SocketManager instance;
	private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private Thread listenerThread;

	private SocketManager() {
	}

	public static synchronized SocketManager getInstance() {
		if (instance == null)
			instance = new SocketManager();
		return instance;
	}

	public void connect() throws IOException {
		InetAddress serverAddr = ClientGUI.getServerAddr();
		int port = ClientGUI.getPort();

		socket = new Socket(serverAddr, port);
		input = new ObjectInputStream(socket.getInputStream());
		output = new ObjectOutputStream(socket.getOutputStream());

		//TODO: create thread to listen for responses
	}

	public void sendRequest(Request request) {
		try {
			output.writeObject(request);
			output.flush();
		} catch (RuntimeException e) { //TODO: Improve this exception handling
			Platform.runLater(() -> {
				System.out.println(e.getMessage());
			});
		} catch (IOException e) { //TODO: Improve this exception handling
			Platform.runLater(() -> {
				System.out.println("Error: " + e.getMessage());
			});
		}
	}

	public void waitForResponse(Consumer<Response> responseHandler) {
		try {
			Response response = (Response) input.readObject();
			responseHandler.accept(response);
		} catch (IOException | ClassNotFoundException e) { //TODO: Improve this exception handling
			Platform.runLater(() -> {
				System.out.println("Error: " + e.getMessage());
			});
		}

	}

	public void close() {
		try {
			if (socket != null && !socket.isClosed())
				socket.close();
		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
