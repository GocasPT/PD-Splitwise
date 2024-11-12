package pt.isec.pd.sharedLib.network.response;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class Response implements Serializable {
	private final boolean success;
	private final String errorDescription;

	//TODO: check constructor later (is true or false + errorDescription)
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

	@Override
	public String toString() {
		return "Response [success: " + success + (!success ? ", errorDescription=" + errorDescription : "]");
	}
}
