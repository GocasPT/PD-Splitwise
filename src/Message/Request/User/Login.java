package Message.Request.User;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

public record Login(String username, String password) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		System.out.println("Login: " + this);

		return new Response(true);
	}

	@Override
	public String toString() {
		return "LOGIN" + " " + username + " " + password;
	}
}