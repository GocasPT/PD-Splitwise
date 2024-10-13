package Message.Request.Group;

import Message.Request.Request;
import Message.Response.Response;

/**
 * @param groupId TODO: groupId in client-side or server-side?
 */
public record EditGroup(int groupId, String name) implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "EDIT_GROUP" + " " + groupId + " " + name;
	}
}
