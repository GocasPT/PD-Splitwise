package pt.isec.a2021138502.PD_Splitwise.Message.Request.Payment;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.Payment;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ListResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.util.Date;

public record GetPayments(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to get payments

		Payment[] payments = {
				new Payment(
						1,
						"Batatas do Pingo Doce",
						10.99,
						new Date(),
						"João",
						"Maria"
				)
		};

		return new ListResponse<>(payments);
	}

	@Override
	public String toString() {
		return "GET_PAYMENTS " + groupID;
	}
}
