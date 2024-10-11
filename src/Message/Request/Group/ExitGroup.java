package Message.Request.Group;

import Message.Request.EComands;
import Message.Request.Request;

public class ExitGroup extends Request {
	private int groupId;

	public ExitGroup(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupId() {
		return groupId;
	}

	@Override
	public String toString() {
		return EComands.EXIT_GROUP + " " + groupId;
	}
}
