package pt.isec.a2021138502.PD_Splitwise.ui;

import javafx.scene.control.Alert;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.NotificaionResponse;

public class NotificationManager {
	public static void showNotification(NotificaionResponse notificaionResponse) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Notification");
		alert.setContentText(notificaionResponse.getNotifyDescription());
		alert.show();
	}
}
