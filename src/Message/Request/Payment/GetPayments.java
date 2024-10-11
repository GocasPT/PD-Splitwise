package Message.Request.Payment;

import Message.Request.EComands;
import Message.Request.Request;

public class GetPayments extends Request {
	private int groupID; //TODO: client-side or server-side?

	public GetPayments(int groupID) {
		this.groupID = groupID;
	}

	public int getGroupID() {
		return groupID;
	}

	@Override
	public String toString() {
		return EComands.GET_PAYMENTS + " " + groupID;
	}
}
