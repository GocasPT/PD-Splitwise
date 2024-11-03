package pt.isec.a2021138502.PD_Splitwise.Manager;

import pt.isec.a2021138502.PD_Splitwise.Thread.ClientHandler;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
	private final Map<String, ClientHandler> sessions = new HashMap<>();

	public boolean addSession(String userId, ClientHandler handler) {
		//TODO: implement this method
		return false;
	}

	public boolean removeSession(String userId) {
		//TODO: implement this method
		return false;
	}

	//TODO: what this method have as argument?
	public void broadcastUpdate() {
		//TODO: implement this method
	}
}
