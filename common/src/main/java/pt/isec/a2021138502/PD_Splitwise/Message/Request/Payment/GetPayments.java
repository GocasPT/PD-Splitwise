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
						420.69,
						new Date(),
						"Fritadeira",
						"Sr. Batata",
						"Sra. Cebola"
				)
		};

		return new ListResponse<>(payments);
	}

	@Override
	public String toString() {
		return "GET_PAYMENTS " + groupID;
	}
}
