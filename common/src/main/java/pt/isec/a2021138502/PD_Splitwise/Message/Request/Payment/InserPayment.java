package pt.isec.a2021138502.PD_Splitwise.Message.Request.Payment;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.util.Date;

/**
 * @param groupID TODO: client-side or server-side?
 */
public record InserPayment(int groupID, int userBuyerID, int userReceiverID, Date date,
                           double amount) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "INSERT_PAYMENT" + " " + groupID + " " + userBuyerID + " " + userReceiverID + " " + date + " " + amount;
	}
}
