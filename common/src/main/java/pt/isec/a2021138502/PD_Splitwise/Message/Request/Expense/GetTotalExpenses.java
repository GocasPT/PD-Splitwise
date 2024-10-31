package pt.isec.a2021138502.PD_Splitwise.Message.Request.Expense;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;

public record GetTotalExpenses(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to get total expenses

		Double totalExpenses = 69.0;

		return new ValueResponse<>(totalExpenses);
	}

	@Override
	public String toString() {
		return "GET_TOTAL_EXPENSES " + groupID;
	}
}
