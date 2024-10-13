package Message.Request.Payment;

import Message.Request.Request;
import Message.Response.Response;

/**
 * @param groupID TODO: client-side or server-side?
 */
public record GetPayments(int groupID) implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "GET_PAYMENTS" + " " + groupID;
	}
}
