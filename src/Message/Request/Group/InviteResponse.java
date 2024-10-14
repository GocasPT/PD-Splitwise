package Message.Request.Group;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

public record InviteResponse(int inviteId, boolean isAccepted) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "INVITE_RESPONSE" + " " + inviteId + " " + isAccepted;
	}
}
