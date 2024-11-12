package pt.isec.pd.sharedLib.network.request.Expense;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;

import java.util.Arrays;
import java.util.Date;

public record EditExpense(int expenseID, double amount, String description, Date date, int buyerId,
                          int[] associatedUsersId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Editing expense with ID: {}", expenseID);

		try {
			//TODO: context.getExpenseDAO().updateExpense(expenseID, amount, description, date, buyerId, associatedUsersId);
		} catch ( Exception e ) {
			logger.error("EditExpense: {}", e.getMessage());
			return new Response(false, "Failed to edit expense");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "EDIT_EXPENSE " + expenseID + " " + amount + " '" + description + "' " + date + " " + buyerId + " " + Arrays.toString(
				associatedUsersId);
	}
}
