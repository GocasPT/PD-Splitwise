package Message.Request.Payment;

import Message.Request.Request;
import Message.Response.Response;

/**
 * @param paymentID TODO: client-side or server-side?
 */
public record DeletePayment(int paymentID) implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "DELETE_PAYMENT" + " " + paymentID;
	}
}
