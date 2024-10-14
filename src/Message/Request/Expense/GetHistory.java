package Message.Request.Expense;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

/**
 * @param groupID TODO: groupId on client side or server side?
 */
public record GetHistory(int groupID) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "GET_HISTORY" + " " + groupID;
	}
}
