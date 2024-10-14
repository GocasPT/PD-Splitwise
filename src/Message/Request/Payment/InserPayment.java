package Message.Request.Payment;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

import java.util.Date;

/**
 * @param groupID TODO: client-side or server-side?
 */
public record InserPayment(int groupID, int userBuyerID, int userReceiverID, Date date,
                           double amount) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "INSERT_PAYMENT" + " " + groupID + " " + userBuyerID + " " + userReceiverID + " " + date + " " + amount;
	}
}
