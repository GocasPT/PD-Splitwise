package pt.isec.a2021138502.PD_Splitwise.Message.Request.Expense;


import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

/**
 * @param expenseID TODO: groupId on client side or server side?
 */
public record DeleteExpense(int expenseID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "DELETE_EXPENSE " + expenseID;
	}
}
