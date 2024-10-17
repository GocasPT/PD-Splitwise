package Message.Request.User;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

public record Login(String username, String password) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		System.out.println("Login: " + this);

		//TODO: use db to check if user exists
		if (!username.equals("bata") || !password.equals("123")) {
			return new Response(false, "Invalid username or password.");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "LOGIN" + " " + username + " " + password;
	}
}