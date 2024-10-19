package pt.isec.a2021138502.PD_Splitwise.Message.Response;

public class NotificaionResponse extends Response {
	public NotificaionResponse(boolean success) {
		super(EResponse.NOTIFICATION, success, "");
	}

	public NotificaionResponse(boolean success, String errorDescription) {
		super(EResponse.NOTIFICATION, success, errorDescription);
	}
}
