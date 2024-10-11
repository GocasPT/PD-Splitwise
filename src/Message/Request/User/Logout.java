package Message.Request.User;

import Message.Request.EComands;
import Message.Request.Request;

public class Logout extends Request {
	@Override
	public String toString() {
		return EComands.LOGOUT.toString();
	}
}
