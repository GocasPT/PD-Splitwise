package Message.Request.Group;

import Message.Request.Request;
import Message.Response.Response;

public class GetGroups implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "GET_GROUPS";
	}
}
