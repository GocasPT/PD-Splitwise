package Message.Request.Group;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

/**
 * @param groupId TODO: groupId in client-side or server-side?
 */
public record EditGroup(int groupId, String name) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "EDIT_GROUP" + " " + groupId + " " + name;
	}
}
