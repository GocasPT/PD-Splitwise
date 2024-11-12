package pt.isec.pd.sharedLib.network.response;

public class NotificaionResponse extends Response {
	private final String email;
	private final String notifyDescription;

	public NotificaionResponse(String email, String text) {
		super(true);

		if (email == null || text == null)
			throw new IllegalArgumentException("Email and text must not be null");

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
		StringBuilder sb = new StringBuilder();

		sb.append("NotificationResponse [");
		boolean success = isSuccess();
		sb.append("success: ").append(success);
		if (!success) sb.append(", errorDescription: ").append(getErrorDescription());
		else sb.append(", notifyDescription: ").append(notifyDescription);

		return sb.toString();
	}
}
