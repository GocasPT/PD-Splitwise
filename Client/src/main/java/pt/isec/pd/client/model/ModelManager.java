package pt.isec.pd.client.model;

import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;

import java.io.IOException;
import java.net.InetAddress;

public class ModelManager {
	//private static ModelManager instance;

	private final SocketManager socketManager;

	private String emailLoggedUser;
	private int groupInViewId;

	public ModelManager() {
		socketManager = new SocketManager();
	}

	/*public static synchronized ModelManager getInstance() {
		if (instance == null) instance = new ModelManager();
		return instance;
	}*/

	public String getEmailLoggedUser() {
		return emailLoggedUser;
	}

	public void setEmailLoggedUser(String emailLoggedUser) {
		this.emailLoggedUser = emailLoggedUser;
	}

	public int getGroupInViewId() {
		return groupInViewId;
	}

	public void setGroupInViewId(int groupInViewId) {
		this.groupInViewId = groupInViewId;
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
