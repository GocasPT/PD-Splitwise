package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.sql.SQLException;

public record Register(String username, String phone, String email, String password) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		String query = "INSERT INTO users (username, email, password, phone_number) VALUES (?, ?, ?, ?)";

		try {
			context.setData(query, username, email, password, phone);
		} catch ( SQLException e ) {
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
		return "REGISTER " + username + " " + phone + " " + email + " " + password;
	}
}
