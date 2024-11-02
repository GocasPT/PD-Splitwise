package pt.isec.a2021138502.PD_Splitwise;

import pt.isec.a2021138502.PD_Splitwise.Data.INotificationObserver;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.NotificaionResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO: this could be a singleton (?)
public class NotificationManager implements INotificationObserver {
	private final Map<String, ClientHandler> clientHandlerMap = new ConcurrentHashMap<>();

	public void registerClient(String email, ClientHandler clientHandler) {
		clientHandlerMap.put(email, clientHandler);
	}

	public void unregisterClient(String email) {
		clientHandlerMap.remove(email);
	}

	@Override
	public void onNotification(NotificaionResponse notification) {
		ClientHandler clientHandler = clientHandlerMap.get(notification.getEmail());
		if (clientHandler != null) {
			clientHandler.sendNotification(notification);
		}
	}
}
