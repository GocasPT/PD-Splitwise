package Message.Request.Group;

import Message.Request.Request;
import Message.Response.Response;

public record DeleteGroup(int groupId) implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "DELETE_GROUP" + " " + groupId;
	}
}
