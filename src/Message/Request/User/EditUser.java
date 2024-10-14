package Message.Request.User;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

public record EditUser(String username, String phone, String email, String password) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "EDIT_USER" + " " + username + " " + phone + " " + email + " " + password;
	}
}
