package Message.Request.Group;

import Message.Request.EComands;
import Message.Request.Request;

public class DeleteGroup extends Request {
	private int groupId;

	public DeleteGroup(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupId() {
		return groupId;
	}

	@Override
	public String toString() {
		return EComands.DELETE_GROUP + " " + groupId;
	}
}
