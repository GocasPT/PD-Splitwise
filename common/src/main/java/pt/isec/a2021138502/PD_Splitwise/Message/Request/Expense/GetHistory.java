package pt.isec.a2021138502.PD_Splitwise.Message.Request.Expense;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.Expense;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ListResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.util.Date;

public record GetHistory(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to get history of group

		Expense[] expensesHistory = {
				new Expense(
						"Batata frita do Pingo Doce",
						1.99,
						new Date(),
						"Fritadeira",
						"Sr. Batata"
				),
		};

		return new ListResponse<>(expensesHistory);
	}

	@Override
	public String toString() {
		return "GET_HISTORY " + groupID;
	}
}
