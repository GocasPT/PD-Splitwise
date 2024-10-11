package Message.Request.Group;

import Message.Request.EComands;
import Message.Request.Request;

public class GetInvites extends Request {
	@Override
	public String toString() {
		return EComands.GET_INVITATIONS.toString();
	}
}
