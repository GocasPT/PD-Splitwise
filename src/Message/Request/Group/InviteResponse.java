package Message.Request.Group;

import Message.Request.Request;
import Message.Response.Response;

public record InviteResponse(int inviteId, boolean isAccepted) implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "INVITE_RESPONSE" + " " + inviteId + " " + isAccepted;
	}
}
