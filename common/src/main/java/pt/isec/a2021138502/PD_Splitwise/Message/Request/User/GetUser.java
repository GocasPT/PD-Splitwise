package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;

import java.util.List;
import java.util.Map;

public record GetUser(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to get user data
		User user = null;
		String query = """
		               SELECT *
		               FROM %s
		               WHERE email = ?
		               """.formatted(context.USERS_TABLE);

		try {
			List<Map<String, Object>> rs = context.select(query, email);

			for (Map<String, Object> row : rs) {
				user = new User(
						(String) row.get("email"),
						(String) row.get("phone_number"),
						(String) row.get("email"),
						(String) row.get("password")
				);
			}

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
