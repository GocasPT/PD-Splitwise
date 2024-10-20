package pt.isec.a2021138502.PD_Splitwise.Message.Request.Payment;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record DeletePayment(int paymentID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to delete payment

		return new Response(true);
	}

	@Override
	public String toString() {
		return "DELETE_PAYMENT " + paymentID;
	}
}
