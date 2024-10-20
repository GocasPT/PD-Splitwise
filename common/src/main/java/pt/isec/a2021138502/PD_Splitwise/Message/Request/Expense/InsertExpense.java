package pt.isec.a2021138502.PD_Splitwise.Message.Request.Expense;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.util.Date;

public record InsertExpense(int groupID, Date date, double amount, String userBuyer) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to insert expense

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INSERT_EXPENSE " + groupID + " " + date + " " + amount + " " + userBuyer;
	}
}
