package Message.Request.Expense;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

/**
 * @param expenseID TODO: groupId on client side or server side?
 */
public record DeleteExpense(int expenseID) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "DELETE_EXPENSE " + expenseID;
	}
}
