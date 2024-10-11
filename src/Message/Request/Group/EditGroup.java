package Message.Request.Group;

import Message.Request.EComands;
import Message.Request.Request;

public class EditGroup extends Request {
	private int groupId; //TODO: groupId in client-side or server-side?
	private String name;

	public EditGroup(int groupId, String name) {
		this.groupId = groupId;
		this.name = name;
	}

	public int getGroupId() {
		return groupId;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return EComands.EDIT_GROUP + " " + groupId + " " + name;
	}
}
