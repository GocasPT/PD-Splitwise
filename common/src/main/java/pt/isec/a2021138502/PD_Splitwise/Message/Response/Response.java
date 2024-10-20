package pt.isec.a2021138502.PD_Splitwise.Message.Response;

import java.io.Serializable;

public class Response implements Serializable {
	public static final long serialVersionUID = 1010L;
	private final boolean success;
	private final String errorDescription;

	public Response(boolean success) {
		this.success = success;
		this.errorDescription = "";
	}

	public Response(boolean success, String errorDescription) {
		if (!success && errorDescription.isEmpty()) {
			throw new IllegalArgumentException("Error message cannot be empty if sucess is false.");
		}

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
