package Message.Request.Group;

import Message.Request.EComands;
import Message.Request.Request;

public class GetGroups extends Request {
	@Override
	public String toString() {
		return EComands.GET_GROUPS.toString();
	}
}
