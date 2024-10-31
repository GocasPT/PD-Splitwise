package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.sql.SQLException;

public record EditUser(String username, String phone, String email, String password) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		String queryUpdate = "UPDATE users SET phone_number = ?, email = ?, password = ? WHERE email = ?";

		try {
			context.update(queryUpdate, phone, email, password, username);
		} catch ( SQLException e ) {
			if (e.getErrorCode() == 19 && e.getMessage().toLowerCase().contains("unique")) {
				return new Response(false, "Email already in use");
			}

			System.out.println("Error on 'EditUser.execute': " + e.getMessage());
			return new Response(false, "Error editing user");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "EDIT_USER" + " " + username + " " + phone + " " + email + " " + password;
	}
}
