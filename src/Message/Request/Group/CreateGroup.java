package Message.Request.Group;

import Message.Request.EComands;
import Message.Request.Request;

public class CreateGroup extends Request {
	private String name;

	public CreateGroup(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return EComands.CREATE_GROUP + " '" + name + "'";
	}
}
