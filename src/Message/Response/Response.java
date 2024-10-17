package Message.Response;

import java.io.Serializable;

public class Response implements Serializable {
	private final EResponse type;
	private final boolean success;
	private final String errorDescription;

	public Response(boolean success) {
		this.type = EResponse.FEED_BACK;
		this.success = success;
		this.errorDescription = "";
	}

	public Response(boolean success, String errorDescription) {
		if (!success && errorDescription.isEmpty()) {
			throw new IllegalArgumentException("Error message cannot be empty if sucess is false.");
		}

		this.type = EResponse.FEED_BACK;
		this.success = success;
		this.errorDescription = errorDescription;
	}

	public Response(EResponse type, boolean success, String errorDescription) {
		this.type = type;
		this.success = success;
		this.errorDescription = errorDescription;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	@Override
	public String toString() {
		return "Response [success: " + success + (!success ? ", errorDescription=" + errorDescription : "]");
	}
}
