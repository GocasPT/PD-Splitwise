package pt.isec.pd.sharedLib.network.request.Expense;

import pt.isec.pd.sharedLib.database.DTO.Expense.DetailExpenseDTO;
import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.ListResponse;
import pt.isec.pd.sharedLib.network.response.Response;

import java.util.ArrayList;
import java.util.List;


public record GetHistory(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Getting history of group with ID: {}", groupID);
		List<DetailExpenseDTO> expensesHistory = new ArrayList<>();

		try {
			context.getExpenseDAO().getExpensesFromGroup(groupID).forEach(expense -> {
				expensesHistory.add(
						new DetailExpenseDTO(
								expense.getAmount(),
								expense.getDescription(),
								expense.getTimestamp(),
								expense.getBuyerEmail()
						)
				);
			});
		} catch ( Exception e ) {
			logger.error("GetHistory: {}", e.getMessage());
			return new ListResponse<>("Failed to get expense history");
		}

		return new ListResponse<>(expensesHistory.toArray());
	}

	@Override
	public String toString() {
		return "GET_HISTORY " + groupID;
	}
}
