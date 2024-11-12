package pt.isec.pd.sharedLib.network.request.Payment;


import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.Expense;
import pt.isec.pd.sharedLib.database.Entity.Payment;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;
import pt.isec.pd.sharedLib.network.response.ValueResponse;

import java.util.List;

public record ViewBalance(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("ViewBalance: {}", this);

		//TODO:
		// - expense total value
		// - amount user have in debt
		// - amount user have in debt for each user
		// - amount user to receive
		// - amount user to receive for each user
		// use debts table + group table to make this calculation

		double groupBalance = 0;
		try {
			List<Expense> expensesList = context.getExpenseDAO().getExpensesFromGroup(groupID);
			List<Payment> paymentsList = context.getPaymentDAO().getPaymentsFromGroup(groupID);
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
