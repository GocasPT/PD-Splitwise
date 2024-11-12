package pt.isec.pd.sharedLib.database.DAO;


import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.Payment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO: implement CRUD operations
public class PaymentDAO extends DAO {
	public PaymentDAO(DataBaseManager dbManager) {
		super(dbManager);
	}

	public int createPayment(int from_user_id, int for_user_id, double amount) throws SQLException {
		//TODO: created payment = insert payment + check debts from_user to for_user
		logger.debug("Creating payment from user {} to user {} with amount {}", from_user_id, for_user_id, amount);
		//language=SQLite
		String query = "INSERT INTO payments (from_user_id, for_user_id, amount) VALUES (?, ?, ?, ?)";
		return dbManager.executeWrite(query, from_user_id, for_user_id, amount);
	}

	public Payment getPaymentById(int id) throws SQLException {
		logger.debug("Getting payment with id {}", id);
		//language=SQLite
		String query = """
		               SELECT payments.id      AS id,
		                      groups.name      AS group_name,
		                      payments.amount  AS amount,
		                      payments.date    AS date,
		                      payer.username   AS payer,
		                      reciver.username AS reciver
		               FROM payments
		                        JOIN groups ON payments.group_id = groups.id
		                        JOIN users payer ON payments.from_user_id = payer.id
		                        JOIN users reciver ON payments.for_user_id = reciver.id
		               WHERE payments.id = ?;
		               """;
		Map<String, Object> result = dbManager.executeRead(query, id).getFirst();
		return new Payment(
				(int) result.get("id"),
				(String) result.get("group_name"),
				(double) result.get("amount"),
				(long) result.get("date"),
				(String) result.get("from_user_id"),
				(String) result.get("for_user_id")
		);
	}

	public List<Payment> getPaymentsFromGroup(int groupId) throws SQLException {
		logger.debug("Getting all payments for group {}", groupId);
		//language=SQLite
		String query = """
		               SELECT payments.id      AS id,
		                      groups.name      AS group_name,
		                      payments.amount  AS amount,
		                      payments.date    AS date,
		                      payer.username   AS payer,
		                      reciver.username AS reciver
		               FROM payments
		                        JOIN groups ON payments.group_id = groups.id
		                        JOIN users payer ON payments.from_user_id = payer.id
		                        JOIN users reciver ON payments.for_user_id = reciver.id
		               WHERE payments.group_id = ?;
		               """;
		List<Map<String, Object>> result = dbManager.executeRead(query);
		List<Payment> payments = new ArrayList<>();
		for (Map<String, Object> row : result)
			payments.add(new Payment(
					(int) row.get("id"),
					(String) row.get("group_name"),
					(double) row.get("amount"),
					(long) row.get("date"),
					(String) row.get("from_user_id"),
					(String) row.get("for_user_id")
			));
		return payments;
	}

	//TODO: get all payments from user

	public boolean deletePayment(int id) throws SQLException {
		logger.debug("Deleting payment with id {}", id);
		//language=SQLite
		String query = "DELETE FROM payments WHERE id = ?";
		return dbManager.executeWrite(query, id) > 0;
	}
}
