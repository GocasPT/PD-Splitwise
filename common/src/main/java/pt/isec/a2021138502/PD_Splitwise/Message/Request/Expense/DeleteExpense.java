package pt.isec.a2021138502.PD_Splitwise.Message.Request.Expense;


import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record DeleteExpense(int expenseID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to delete expense

		return new Response(true);
	}

	@Override
	public String toString() {
		return "DELETE_EXPENSE " + expenseID;
	}
}
