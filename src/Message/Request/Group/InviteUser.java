package Message.Request.Group;

import Message.Request.EComands;
import Message.Request.Request;

//TODO: how server know which group is for?
public class InviteUser extends Request {
	private String email;

	public InviteUser(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return EComands.LOGIN + " " + email;
	}
}
