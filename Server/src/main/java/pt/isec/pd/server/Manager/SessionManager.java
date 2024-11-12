package pt.isec.pd.server.Manager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.pd.server.Runnable.ClientHandler;
import pt.isec.pd.sharedLib.database.Observer.NotificationObserver;
import pt.isec.pd.sharedLib.network.response.NotificaionResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SessionManager implements NotificationObserver {
	private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
	private final Map<String, ClientHandler> sessions = new HashMap<>();

	public void addSession(String userId, ClientHandler handler) {
		logger.debug("Adding session for user {}", userId);
		sessions.put(userId, handler);
	}

	public void removeSession(String userId) {
		logger.debug("Removing session for user {}", userId);
		sessions.remove(userId);
	}

	//TODO: what this method have as argument?
	public void broadcastUpdate() {
		//TODO: implement this method
	}

	@Override
	public void onNotification(NotificaionResponse notification) {
		try {
			ClientHandler handler = sessions.get(notification.getEmail());
			if (handler != null) {
				handler.sendMessage(notification);
			}
		} catch ( IOException e ) {
			throw new RuntimeException(e); //TODO: improve this
		}
	}
}
