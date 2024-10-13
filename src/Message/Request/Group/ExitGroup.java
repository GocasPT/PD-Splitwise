package Message.Request.Group;

import Message.Request.Request;
import Message.Response.Response;

public record ExitGroup(int groupId) implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "EXIT_GROUP" + " " + groupId;
	}
}
