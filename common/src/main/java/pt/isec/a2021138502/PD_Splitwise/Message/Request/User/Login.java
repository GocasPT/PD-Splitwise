package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.util.Map;

public record Login(String email, String password) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		String query = "SELECT * FROM users  WHERE email = ? AND password = ?";

		try {
			Map<String, Object> user = context.getData(query, email, password).getFirst();

			String userEmail = (String) user.get("email");
			String userPassword = (String) user.get("password");

			if (!userEmail.equals(email) || !userPassword.equals(password))
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