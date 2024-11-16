package pt.isec.pd.splitwise.client.model;

import lombok.Getter;
import lombok.Setter;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

import java.io.IOException;
import java.net.InetAddress;

public class ModelManager {
	private final SocketManager socketManager;

	@Setter
	@Getter
	private String emailLoggedUser;
	@Setter
	@Getter
	private int groupInViewId;

	public ModelManager() {
		socketManager = new SocketManager();
	}

	public void connect(InetAddress serverAddr, int port) {
		try {
			socketManager.connect(serverAddr, port);
		} catch ( IOException e ) { //TODO: Improve this exception handling
			throw new RuntimeException(e);
		}
	}

	public void close() {
		try {
			socketManager.close();
		} catch ( IOException e ) {
			throw new RuntimeException(e);
		}
	}

	//TODO: improve this method
	// maybe should be private and add other methods for each situation?
	// throw exceptions and handle them on GUI
	public Response sendRequest(Request request) {
		try {
			return socketManager.sendRequest(request);
		} catch ( IOException e ) {
			System.err.println("IOException while sending request: " + e.getMessage());
		} catch ( InterruptedException e ) {
			System.err.println("InterruptedException while sending request: " + e.getMessage());
		} catch ( Exception e ) {
			System.err.println("Unexpected exception while sending request: " + e.getMessage());
		}

		return null;
	}
}
