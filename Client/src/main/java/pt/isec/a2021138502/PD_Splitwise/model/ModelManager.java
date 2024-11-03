package pt.isec.a2021138502.PD_Splitwise.model;

import pt.isec.a2021138502.PD_Splitwise.Data.Group;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.InetAddress;

public class ModelManager {
	private static ModelManager instance;

	private final SocketManager socketManager;
	private final PropertyChangeSupport pcs;

	private EState state;
	private String emailLoggedUser;
	private Group groupInView;

	private ModelManager() {
		pcs = new PropertyChangeSupport(this);
		socketManager = new SocketManager();
		state = EState.LOGIN;
	}

	public static synchronized ModelManager getInstance() {
		if (instance == null) instance = new ModelManager();
		return instance;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public EState getState() {
		return state;
	}

	public String getEmailLoggedUser() {
		return emailLoggedUser;
	}

	public void setEmailLoggedUser(String emailLoggedUser) {
		this.emailLoggedUser = emailLoggedUser;
	}

	public Group getGroupInView() {
		return groupInView;
	}

	public void setGroupInView(Group groupInView) {
		this.groupInView = groupInView;
	}

	public void changeState(EState state) {
		this.state = state;
		pcs.firePropertyChange(null, null, null);
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
