package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.sql.SQLException;

public record Register(String username, String phone, String email, String password) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to see if email is unique
		String query = """
		               INSERT INTO users
		               	(username, email, password, phone_number)
		               VALUES (?, ?, ?, ?)
		               """;

		try {
			context.insert(query, username, email, password, phone);
		} catch ( SQLException e ) {
			System.out.println("Error on 'Register.execute': " + e.getMessage());
			return new Response(false, "Error registering user");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "REGISTER " + username + " " + phone + " " + email + " " + password;
	}
}
