package Message.Request.Group;

import Message.Request.Request;
import Message.Response.Response;

//TODO: how server know which group is for?
public record InviteUser(int groupID, String email) implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "LOGIN" + " " + email;
	}
}
