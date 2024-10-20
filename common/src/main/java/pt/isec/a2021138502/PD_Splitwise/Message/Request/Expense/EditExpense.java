package pt.isec.a2021138502.PD_Splitwise.Message.Request.Expense;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.util.Date;

public record EditExpense(int expenseID, int groupID, Date date, double amount, String buyer) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to edit expense

		return new Response(true);
	}

	@Override
	public String toString() {
		return "EDIT_EXPENSE " + expenseID + " " + groupID + " " + date + " " + amount + " " + buyer;
	}
}
