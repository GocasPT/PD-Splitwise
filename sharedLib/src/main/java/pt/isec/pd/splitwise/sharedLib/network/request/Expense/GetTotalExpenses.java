package pt.isec.pd.splitwise.sharedLib.network.request.Expense;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Expense;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;
import pt.isec.pd.splitwise.sharedLib.network.response.ValueResponse;

import java.sql.SQLException;
import java.util.List;

public record GetTotalExpenses(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Getting total expenses from group with ID: {}", groupID);

		double totalExpenses = 0;

		try {
			List<Expense> expenseList = context.getExpenseDAO().getExpensesFromGroup(groupID);
			for (Expense expense : expenseList)
				totalExpenses += expense.getAmount();
		} catch ( SQLException e ) {
			logger.error("GetTotalExpenses: {}", e.getMessage());
			return new ValueResponse<>("Failed to get total expense");
		}

		return new ValueResponse<>(totalExpenses);
	}

	@Override
	public String toString() {
		return "GET_TOTAL_EXPENSES " + groupID;
	}
}
