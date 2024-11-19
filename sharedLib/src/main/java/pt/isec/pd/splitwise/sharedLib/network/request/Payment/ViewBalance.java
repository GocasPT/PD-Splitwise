package pt.isec.pd.splitwise.sharedLib.network.request.Payment;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Expense;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Payment;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;
import pt.isec.pd.splitwise.sharedLib.network.response.ValueResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ViewBalance(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Getting balance for group {}", groupID);

		//TODO:
		// - expense total value
		// - amount user have in debt
		// - amount user have in debt for each user
		// - amount user to receive
		// - amount user to receive for each user
		// use debts table + group table to make this calculation

		Map<String, Object> groupBalance = new HashMap<>();
		try {
			List<Expense> expensesList = context.getExpenseDAO().getAllExpensesFromGroup(groupID);
			List<Payment> paymentsList = context.getPaymentDAO().getAllPaymentsFromGroup(groupID);
		} catch ( Exception e ) {
			logger.error("ViewBalance: {}", e.getMessage());
			return new ValueResponse<>("Failed to get balance");
		}

		return new ValueResponse<>(groupBalance);
	}

	@Override
	public String toString() {
		return "VIEW_BALANCE " + groupID;
	}
}
