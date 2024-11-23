package pt.isec.pd.splitwise.sharedLib.network.request.Expense;

import pt.isec.pd.splitwise.sharedLib.database.DTO.Balance.PreviewBalanceDTO;
import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Expense;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;
import pt.isec.pd.splitwise.sharedLib.network.response.ValueResponse;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public record GetTotalExpenses(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Getting total expenses from group {}", groupID);

		PreviewBalanceDTO previewBalance = new PreviewBalanceDTO(0.0, new ArrayList<>());
		try {
			List<Expense> expenseList = context.getExpenseDAO().getAllExpensesFromGroup(groupID);
			for (Expense expense : expenseList) {
				previewBalance.getUsersBalance().add(new AbstractMap.SimpleEntry<>(expense.getPayerUser(),
				                                                                   expense.getAmount())); //TODO: try to simplify this
				previewBalance.setTotalBalance(previewBalance.getTotalBalance() + expense.getAmount());
				logger.debug("Added expense from user {} with amount {}", expense.getPayerUser(), expense.getAmount());
			}

			logger.debug("Total expenses: {}", previewBalance.getTotalBalance());
		} catch ( SQLException e ) {
			logger.error("GetTotalExpenses: {}", e.getMessage());
			return new ValueResponse<>("Failed to get total expense");
		}

		return new ValueResponse<>(previewBalance);
	}

	@Override
	public String toString() {
		return "GET_TOTAL_EXPENSES " + groupID;
	}
}
