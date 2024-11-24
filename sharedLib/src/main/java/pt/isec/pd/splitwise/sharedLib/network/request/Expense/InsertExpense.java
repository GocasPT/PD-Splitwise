package pt.isec.pd.splitwise.sharedLib.network.request.Expense;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.User;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record InsertExpense(int groupID, double amount, String description, LocalDate date, String buyerEmail,
                            String[] associateUsersEmail) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("""
		             Inserting expense on group {}:
		             \tamoune: {}
		             \tdescription: '{}'
		             \tdate: {}
		             \tbuyerEmail: {}
		             \tassociateUsersEmail: {}""",
		             groupID, amount, description, date, buyerEmail, associateUsersEmail);

		try {
			User buyerData = context.getUserDAO().getUserByEmail(buyerEmail);
			List<User> associetedUserData = new ArrayList<>();
			for (String associetedUser : associateUsersEmail)
				associetedUserData.add(
						context.getUserDAO().getUserByEmail(associetedUser)
				);
			context.getExpenseDAO().createExpense(groupID, amount, description, date, buyerData.getId(),
			                                      associetedUserData.stream().mapToInt(User::getId).toArray());
		} catch ( Exception e ) {
			logger.error("InsertExpense: {}", e.getMessage());
			return new Response(false, "Failed to insert expense");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INSERT_EXPENSE " + groupID + " " + amount + " '" + description + "' '" + date + "' " + buyerEmail + " " + Arrays.toString(
				associateUsersEmail);
	}
}
