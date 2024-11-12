package pt.isec.pd.sharedLib.network.request.Expense;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.User;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public record InsertExpense(int groupID, double amount, String description, long timestamp, String userBuyer,
                            String[] usersAssociate) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("""
		             Inserting expense:
		                groupID - {}
		                amount - {}
		                description - '{}'
		                date - {}
		                userBuyer - {}
		                usersAssociate - {}
		             """, groupID, amount, description, timestamp, userBuyer, usersAssociate);

		/*//TODO: query to insert expense
		//language=SQLite
		String queryInsertExpense = """
		                            INSERT 
		                            INTO
		                             expenses
		                             (group_id, amount, description, date, paid_by_user_id)
		                            VALUES
		                             (?, ?, ?, ?, (
		                                 SELECT 
		                                     id
		                                 FROM
		                                     users
		                                 WHERE
		                                     email = ?
		                             ))
		                            """;

		//language=SQLite
		String queryAssociateExpenseUser = """
		                                   INSERT
		                                   INTO
		                                   	expense_users
		                                   	(expense_id, user_id, percentage)
		                                   VALUES
		                                   	(?, (
		                                   		SELECT 
		                                   			id
		                                   		FROM 
		                                   			users
		                                   		WHERE 
		                                   			email = ?
		                                   	), ?)
		                                   """;

		try {
			int expenseId = context.setData(queryInsertExpense, groupID, amount, description, timestamp, userBuyer);
			logger.debug("Inserted expense with id: {}", expenseId);
			for (String userEmail : usersAssociate)
				context.setData(queryAssociateExpenseUser, expenseId, userEmail, (100 / (usersAssociate.length + 1)));
		} catch ( Exception e ) {
			logger.error("InsertExpense: {}", e.getMessage());
			return new Response(false, "Failed to insert expense");
		}*/

		try {
			User buyerData = context.getUserDAO().getUserByEmail(userBuyer);
			List<User> associetedUserData = new ArrayList<>();
			for (String associetedUser : usersAssociate)
				associetedUserData.add(
						context.getUserDAO().getUserByEmail(associetedUser)
				);
			context.getExpenseDAO().createExpense(groupID, amount, description, new Date(timestamp), buyerData.getId(),
			                                      associetedUserData.stream().mapToInt(User::getId).toArray());
		} catch ( Exception e ) {
			logger.error("InsertExpense: {}", e.getMessage());
			return new Response(false, "Failed to insert expense");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INSERT_EXPENSE " + groupID + " " + amount + " '" + description + "' '" + new Date(
				timestamp) + "' " + userBuyer + " " + Arrays.toString(
				usersAssociate);
	}
}
