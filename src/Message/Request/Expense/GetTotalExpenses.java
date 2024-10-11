package Message.Request.Expense;

import Message.Request.EComands;
import Message.Request.Request;

public class GetTotalExpenses extends Request {
	private int groupID; //TODO: groupId on client side or server side?

	public GetTotalExpenses(int groupID) {
		this.groupID = groupID;
	}

	public int getGroupID() {
		return groupID;
	}

	@Override
	public String toString() {
		return EComands.GET_TOTAL_EXPENSES + " " + groupID;
	}
}
