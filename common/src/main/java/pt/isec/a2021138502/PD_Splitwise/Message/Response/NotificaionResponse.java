package pt.isec.a2021138502.PD_Splitwise.Message.Response;

public class NotificaionResponse extends Response {
	private final String notifyDescription;
	//TODO: add the notification data (String, Object, etc)

	public NotificaionResponse(String text) {
		super(true);
		this.notifyDescription = text;
	}

	public String getNotifyDescription() {
		return notifyDescription;
	}

	@Override
	public String toString() {
		return "NotificaionResponse [sucess: " + isSuccess() + (!isSuccess() ? ", errorDescription: " + getErrorDescription() : ", notifyDescription: " + notifyDescription + "]");
	}
}
