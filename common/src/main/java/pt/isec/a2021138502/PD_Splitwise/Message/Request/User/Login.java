package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public record Login(String email, String password) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to find user and check password
		// if exists update user as online (?)
		String query = """
		               SELECT *
		               FROM %s
		               WHERE email = ? AND password = ?
		               """.formatted(context.USERS_TABLE);

		try {
			List<Map<String, Object>> rs = context.select(query, email, password);

			String rsEmail = (String) rs.getFirst().get("email");
			String rsPassword = (String) rs.getFirst().get("password");

			if (!rsEmail.equals(email) || !rsPassword.equals(password))
				return new Response(false, "Invalid email or password");

		} catch ( SQLException e ) {
			System.out.println("Error on 'Login.execute': " + e.getMessage());
			return new Response(false, "Error on login");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "LOGIN " + email + " " + password;
	}
}