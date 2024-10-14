package Message.Request.Group;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

public class GetInvites implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "GET_INVITATIONS";
	}
}
