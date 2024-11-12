package pt.isec.pd.sharedLib.network.request.User;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;

public record Logout(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Logging out: {}", email);
		//TODO: client handler make it all the logic

		return new Response(true);
	}

	@Override
	public String toString() {
		return "LOGOUT " + email;
	}
}
