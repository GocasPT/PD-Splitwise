package Message.Request.Group;

import Message.Request.EComands;
import Message.Request.Request;

public class InviteResponse extends Request {
	private int inviteId;
	private boolean isAccepted;

	public InviteResponse(int inviteId, boolean isAccepted) {
		this.inviteId = inviteId;
		this.isAccepted = isAccepted;
	}

	public int getInviteId() {
		return inviteId;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	@Override
	public String toString() {
		return EComands.INVITE_RESPONSE + " " + inviteId + " " + isAccepted;
	}
}
