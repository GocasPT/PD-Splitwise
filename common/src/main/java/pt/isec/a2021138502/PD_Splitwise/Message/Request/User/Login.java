package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record Login(String username, String password) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to find user and check password
		// if exists update user as online
		if (!username.equals("bata") || !password.equals("123")) {
			return new Response(false, "Invalid username or password.");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "LOGIN " + username + " " + password;
	}
}