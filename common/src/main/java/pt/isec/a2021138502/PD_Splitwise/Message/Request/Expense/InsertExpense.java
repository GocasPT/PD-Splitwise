package pt.isec.a2021138502.PD_Splitwise.Message.Request.Expense;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.util.Date;

/**
 * @param groupID TODO: groupId on client side or server side?
 * @param date    TODO: java.util.Date or java.sql.Date?
 */
public record InsertExpense(int groupID, Date date, double amount, String userBuyer) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "INSERT_EXPENSE " + groupID + " " + date + " " + amount + " " + userBuyer;
	}
}
