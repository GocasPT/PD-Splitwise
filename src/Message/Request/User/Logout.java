package Message.Request.User;

import Message.Request.Request;
import Message.Response.Response;

public class Logout implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "LOGOUT";
	}
}
