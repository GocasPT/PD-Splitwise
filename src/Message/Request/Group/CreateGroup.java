package Message.Request.Group;

import Message.Request.Request;
import Message.Response.Response;

public record CreateGroup(String name) implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "CREATE_GROUP" + " '" + name + "'";
	}
}
