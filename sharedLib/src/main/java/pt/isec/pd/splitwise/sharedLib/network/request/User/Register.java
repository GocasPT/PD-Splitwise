package pt.isec.pd.splitwise.sharedLib.network.request.User;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

import java.sql.SQLException;

public record Register(String username, String email, String phone, String password) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("""
		             Register:
		             \tusername: {}
		             \temail: {}
		             \tphone: {}""",
		             username, email, phone);

		try {
			context.getUserDAO().createUser(username, phone, email, password);
		} catch ( SQLException e ) {
			//TODO: check this (need to now if userEmail is already in use)
			if (e.getErrorCode() == 19 && e.getMessage().toLowerCase().contains("unique")) {
				return new Response(false, "Email already in use");
			}
		} catch ( Exception e ) {
			logger.error("Register: {}", e.getMessage());
			return new Response(false, "Error registering user");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "REGISTER " + username + " " + email + " " + phone;
	}
}
