package pt.isec.pd.splitwise.sharedLib.network.request.Expense;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

import java.util.Arrays;
import java.util.Date;

public record EditExpense(int expenseID, double amount, String description, Date date, String buyerEmail,
                          String[] associatedUsersEmail) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Editing expense with ID: {}", expenseID);

		try {
			context.getExpenseDAO().updateExpense(expenseID, amount, description, date, buyerEmail, associatedUsersEmail);
		} catch ( Exception e ) {
			logger.error("EditExpense: {}", e.getMessage());
			return new Response(false, "Failed to edit expense");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "EDIT_EXPENSE " + expenseID + " " + amount + " '" + description + "' " + date + " " + buyerEmail + " " + Arrays.toString(
				associatedUsersEmail);
	}
}
