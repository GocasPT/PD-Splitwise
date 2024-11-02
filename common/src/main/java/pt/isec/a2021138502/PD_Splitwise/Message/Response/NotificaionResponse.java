package pt.isec.a2021138502.PD_Splitwise.Message.Response;

public class NotificaionResponse extends Response {
	private final String email;
	private final String notifyDescription;
	//TODO: add the notification data (String, Object, etc)

	public NotificaionResponse(String email, String text) {
		super(true);
		this.email = email;
		this.notifyDescription = text;
	}

	public String getEmail() {
		return email;
	}

	public String getNotifyDescription() {
		return notifyDescription;
	}

	@Override
	public String toString() {
		return "NotificaionResponse [sucess: " + isSuccess() + (!isSuccess() ? ", errorDescription: " + getErrorDescription() : ", notifyDescription: " + notifyDescription + "]");
	}
}
