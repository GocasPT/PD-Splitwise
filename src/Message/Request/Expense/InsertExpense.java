package Message.Request.Expense;

import Message.Request.Request;
import Message.Response.Response;

import java.util.Date;

/**
 * @param groupID TODO: groupId on client side or server side?
 * @param date    TODO: java.util.Date or java.sql.Date?
 */
public record InsertExpense(int groupID, Date date, double amount, String userBuyer) implements Request {
	@Override
	public Response execute() {
		return null;
	}

	@Override
	public String toString() {
		return "INSERT_EXPENSE" + " " + groupID + " " + date + " " + amount + " " + userBuyer;
	}
}
