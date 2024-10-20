package pt.isec.a2021138502.PD_Splitwise.Message.Request.Payment;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;

import java.util.Map;

public record ViewBalance(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to get balance

		Map<String, Double> groupBalance = Map.of(
				"Total", 2454.25,
				"Divida", 0.0
		);

		return new ValueResponse<>(groupBalance);
	}

	@Override
	public String toString() {
		return "VIEW_BALANCE " + groupID;
	}
}
