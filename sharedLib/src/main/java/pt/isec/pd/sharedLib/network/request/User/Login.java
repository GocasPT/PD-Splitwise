package pt.isec.pd.sharedLib.network.request.User;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.User;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;

public record Login(String email, String password) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Logging in: {}", email);

		try {
			User userData = context.getUserDAO().getUserByEmail(email);
			if (userData == null || !userData.getPassword().equals(password))
				return new Response(false, "Invalid email or password");
		} catch ( Exception e ) {
			logger.error("Login: {}", e.getMessage());
			return new Response(false, "Error on login");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "LOGIN " + email + " " + password;
	}
}