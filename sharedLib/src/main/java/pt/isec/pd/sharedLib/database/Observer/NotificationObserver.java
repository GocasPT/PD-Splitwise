package pt.isec.pd.sharedLib.database.Observer;


import pt.isec.pd.sharedLib.network.response.NotificaionResponse;

public interface NotificationObserver {
	void onNotification(NotificaionResponse notification);
}
