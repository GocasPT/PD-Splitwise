package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;

import java.util.Map;

public record GetUser(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		User user;
		String query = "SELECT * FROM users WHERE email = ?";

		try {
			Map<String, Object> userDate = context.select(query, email).getFirst();

			user = new User(
					(String) userDate.get("email"),
					(String) userDate.get("email"), (String) userDate.get("phone_number"),
					(String) userDate.get("password")
			);

		} catch ( Exception e ) {
			System.out.println("Error on 'GetUser.execute': " + e.getMessage());
			return new ValueResponse<>("Error getting user");
		}

		return new ValueResponse<>(user);
	}

	@Override
	public String toString() {
		return "GET_USER " + email;
	}
}
