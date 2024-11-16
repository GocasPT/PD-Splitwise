package pt.isec.pd.splitwise.sharedLib.database.DAO;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Expense;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//TODO: implement CRUD operations
public class ExpenseDAO extends DAO {
	public ExpenseDAO(DataBaseManager dbManager) {
		super(dbManager);
	}

	public int createExpense(int groupId, double amount, String description, Date date, int userPayerId, int[] usersInvolvedId) throws SQLException {
		//TODO: created expense = insert expense + insert debts with users
		logger.debug("Creating expense for group {} with amount {} by user {}", groupId, amount, userPayerId);
		//language=SQLite
		String queryInsert = "INSERT INTO expenses (group_id, amount, description, date, paid_by_user_id) VALUES (?, ?, ?, ?, ?)";
		//language=SQLite
		String queryInvolveUsers = "INSERT INTO expense_users (expense_id, user_id, percentage) VALUES (?, ?, ?)";
		int id = dbManager.executeWrite(queryInsert, groupId, amount, description, date, userPayerId);
		for (int userId : usersInvolvedId) {
			dbManager.executeWrite(queryInvolveUsers, id, userId, 100 / (usersInvolvedId.length + 1));
		}
		return id;
	}

	public List<Expense> getExpensesFromGroup(int groupId) throws SQLException {
		logger.debug("Getting all expenses for group {}", groupId);
		//language=SQLite
		String query = """
		               SELECT
		                   expenses.id AS expense_id,
		                   expenses.amount AS expense_amount,
		                   expenses.date AS expense_date,
		                   payer.username AS payer_username
		               FROM expenses
		                   JOIN groups ON expenses.group_id = groups.id
		                   JOIN users AS payer ON expenses.paid_by_user_id = payer.id
		               WHERE group_id = ?
		               ORDER BY expense_date DESC
		               """;
		List<Map<String, Object>> results = dbManager.executeRead(query, groupId);
		List<Expense> expenses = new ArrayList<>();

		for (Map<String, Object> row : results) {
			expenses.add(
					Expense.builder()
							.id((int) row.get("expense_id"))
							.amount((double) row.get("expense_amount"))
							.date(LocalDate.parse((String) row.get("expense_date"), DateTimeFormatter.ISO_DATE))
							.buyerEmail((String) row.get("payer_username"))
							.build()
			);
		}
		return expenses;
	}

	//TODO: get debts from expense (?)

	//TODO: rollback system (?)
	public void updateExpense(int id, double amount, String description, Date date, String userPayer, String[] usersInvolved) throws SQLException {
		//TODO: updated expense = update expense + update debts with users (update percentage, remove user, add user)
		logger.debug("Updating expense with id {}\n\tamount: {}\n\tdescription: {}\n\tdate: {}\n\tpaid by: {}", id,
		             amount, description, date, userPayer);

		//language=SQLite
		String queryUpdate = "UPDATE expenses SET amount = ?, description = ?, date = ?, paid_by_user_id = ? WHERE id = ?";
		//language=SQLite
		String queryDeleteInvolved = "DELETE FROM expense_users WHERE expense_id = ?";
		//language=SQLite
		String queryInvolveUsers = "INSERT INTO expense_users (expense_id, user_id, percentage) VALUES (?, ?, ?)";

		dbManager.executeWrite(queryUpdate, amount, description, date, userPayer, id);
		dbManager.executeWrite(queryDeleteInvolved, id);
		for (String user : usersInvolved) {
			dbManager.executeWrite(queryInvolveUsers, id, user, 100 / (usersInvolved.length + 1));
		}
	}

	public boolean deleteExpense(int id) throws SQLException {
		logger.debug("Deleting expense with id: {}", id);
		//TODO: deleted expense = delete expense + delete debts with users
		//language=SQLite
		String query = "DELETE FROM expenses WHERE id = ?";
		dbManager.executeWrite(query, id);
		return true;
	}
}
