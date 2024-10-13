package Message.Request.Expense;

import Message.Request.Request;
import Message.Response.Response;

/**
 * @param groupID TODO: groupId on client side or server side?
 */
public record Export(int groupID) implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "EXPORT" + " " + groupID;
	}
}
