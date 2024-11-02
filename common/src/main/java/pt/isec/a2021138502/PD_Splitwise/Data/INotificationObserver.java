package pt.isec.a2021138502.PD_Splitwise.Data;

import pt.isec.a2021138502.PD_Splitwise.Message.Response.NotificaionResponse;

public interface INotificationObserver {
	void onNotification(NotificaionResponse notification);
}
