package pt.isec.a2021138502.PD_Splitwise.ui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.Notifications;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.NotificaionResponse;

//TODO: style the notification
public class NotificationManager {
	public static void showNotification(NotificaionResponse notificaionResponse) {
		Stage.getWindows().stream()
				.filter(Window::isShowing)
				.findFirst().ifPresent(activeWindow -> Notifications.create()
						.title("Notification")
						.text(notificaionResponse.getNotifyDescription())
						.hideAfter(javafx.util.Duration.seconds(5))
						.owner(activeWindow)
						.show());

	}
}
