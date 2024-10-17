package Message.Request.Group;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;
import Message.Response.ValueResponse;

public record GetGroup(int groupId) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		//return context.getGroup(groupId);
		return new ValueResponse<Integer>(groupId);
	}

	@Override
	public String toString() {
		return "GET_GROUP " + groupId;
	}
}
