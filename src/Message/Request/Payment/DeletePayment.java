package Message.Request.Payment;

import Message.Request.EComands;
import Message.Request.Request;

public class DeletePayment extends Request {
	private int paymentID; //TODO: client-side or server-side?

	public DeletePayment(int paymentID) {
		this.paymentID = paymentID;
	}

	public int getPaymentID() {
		return paymentID;
	}

	@Override
	public String toString() {
		return EComands.DELETE_PAYMENT + " " + paymentID;
	}
}
