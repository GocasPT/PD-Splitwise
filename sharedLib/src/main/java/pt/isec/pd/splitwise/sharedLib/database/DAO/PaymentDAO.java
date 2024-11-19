package pt.isec.pd.splitwise.sharedLib.database.DAO;


import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Payment;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class PaymentDAO extends DAO {
	public PaymentDAO(DataBaseManager dbManager) {
		super(dbManager);
	}

	public int createPayment(int fromUserId, int forUserId, double amount) throws SQLException {
		//TODO: created payment = insert payment + check debts from_user to for_user
		logger.debug("Creating payment from user {} to user {} with amount {}", fromUserId, forUserId, amount);
		//language=SQLite
		String query = "INSERT INTO payments (from_user_id, for_user_id, amount) VALUES (?, ?, ?, ?)";
		return dbManager.executeWrite(query, fromUserId, forUserId, amount);
	}

	public Payment getPaymentById(int paymentId) throws SQLException {
		logger.debug("Getting payment with id {}", paymentId);
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
		Map<String, Object> result = dbManager.executeRead(query, paymentId).getFirst();
		return Payment.builder()
				.id((int) result.get("id"))
				.groupName((String) result.get("group_name"))
				.value((double) result.get("amount"))
				.date(LocalDate.ofInstant(Instant.ofEpochSecond((long) result.get("date")),
				                          TimeZone.getDefault().toZoneId()))
				.buyerName((String) result.get("from_user_id")) //TODO: id → userEmail
				.reciverName((String) result.get("for_user_id")) //TODO: id → userEmail
				.build();
	}

	public List<Payment> getAllPaymentsFromGroup(int groupId) throws SQLException {
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
			payments.add(
					Payment.builder()
							.id((int) row.get("id"))
							.groupName((String) row.get("group_name"))
							.value((double) row.get("amount"))
							.date(LocalDate.ofInstant(Instant.ofEpochSecond((long) row.get("date")),
							                          TimeZone.getDefault().toZoneId()))
							.buyerName((String) row.get("from_user_id")) //TODO: id → userEmail
							.reciverName((String) row.get("for_user_id")) //TODO: id → userEmail
							.build()
			);
		return payments;
	}

	//TODO: get all payments from user

	public boolean deletePayment(int paymentId) throws SQLException {
		logger.debug("Deleting payment with id {}", paymentId);
		//language=SQLite
		String query = "DELETE FROM payments WHERE id = ?";
		return dbManager.executeWrite(query, paymentId) > 0;
	}
}
