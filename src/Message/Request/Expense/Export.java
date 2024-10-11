package Message.Request.Expense;

import Message.Request.EComands;
import Message.Request.Request;

public class Export extends Request {
	private int groupID; //TODO: groupId on client side or server side?

	public Export(int groupID) {
		this.groupID = groupID;
	}

	public int getGroupID() {
		return groupID;
	}

	@Override
	public String toString() {
		return EComands.EXPORT + " " + groupID;
	}
}
