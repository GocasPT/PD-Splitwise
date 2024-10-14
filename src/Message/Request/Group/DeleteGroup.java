package Message.Request.Group;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

public record DeleteGroup(int groupId) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "DELETE_GROUP" + " " + groupId;
	}
}
