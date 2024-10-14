package Message.Request.Expense;

import Data.DatabaseManager;
import Message.Request.Request;
import Message.Response.Response;

import java.util.Date;

/**
 * @param date TODO: java.util.Date or java.sql.Date?
 */
public record EditExpense(int expenseID, int groupID, Date date, double amount, String buyer) implements Request {
	@Override
	public Response execute(DatabaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "EDIT_EXPENSE" + " " + expenseID + " " + groupID + " " + date + " " + amount + " " + buyer;
	}
}
