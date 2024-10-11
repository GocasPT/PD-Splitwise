package Message.Request.Payment;

import Message.Request.EComands;
import Message.Request.Request;

public class ViewBalance extends Request {
	private int groupID; //TODO: client-side or server-side?

	public ViewBalance(int groupID) {
		this.groupID = groupID;
	}

	public int getGroupID() {
		return groupID;
	}

	@Override
	public String toString() {
		return EComands.VIEW_BALANCE + " " + groupID;
	}
}
