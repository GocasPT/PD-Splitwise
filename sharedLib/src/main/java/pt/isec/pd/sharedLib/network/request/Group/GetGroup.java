package pt.isec.pd.sharedLib.network.request.Group;

import pt.isec.pd.sharedLib.database.DTO.Expense.PreviewExpenseDTO;
import pt.isec.pd.sharedLib.database.DTO.Group.DetailGroupDTO;
import pt.isec.pd.sharedLib.database.DTO.Payment.PreviewPaymentDTO;
import pt.isec.pd.sharedLib.database.DTO.User.PreviewUserDTO;
import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.Group;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;
import pt.isec.pd.sharedLib.network.response.ValueResponse;

public record GetGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("GetGroup: {}", this);
		/*//Group group;
		DetailGroupDTO group;
		//language=SQLite
		String query = """
		               WITH UserArray AS (SELECT g.id,
		                                         json_group_array(
		                                                 json_object(
		                                                         'email', u.email,
		                                                         'username', u.username
		                                                 )
		                                         ) as users
		                                  FROM groups g
		                                           LEFT JOIN group_users gu ON g.id = gu.group_id
		                                           LEFT JOIN users u ON gu.user_id = u.id
		                                  GROUP BY g.id),
		                    ExpenseArray AS (SELECT g.id,
		                                            CASE
		                                                WHEN (e.id) > 0 THEN json_group_array(
		                                                        json_object(
		                                                                'id', e.id,
		                                                                'amount', e.amount,
		                                                                'timestamp', e.date,
		                                                                'user', u.username
		                                                        )
		                                                                     )
		                                                ELSE '[]' END as expenses
		                                     FROM groups g
		                                              LEFT JOIN expenses e ON g.id = e.group_id
		                                              LEFT JOIN users u ON e.paid_by_user_id = u.id
		                                     GROUP BY g.id),
		                    PaymentArray AS (SELECT g.id,
		                                            CASE
		                                                WHEN (p.id) > 0 THEN json_group_array(
		                                                        json_object(
		                                                                'id', p.id,
		                                                                'value', p.amount--,
		                                                        --'date', p
		                                                        )
		                                                                     )
		                                                ELSE '[]' END as payments
		                                     FROM groups g
		                                              LEFT JOIN payments p ON g.id = p.id
		                                     GROUP BY g.id)
		               		               SELECT g.id,
		                      g.name,
		                      COALESCE(ua.users, '[]')    as users,
		                      COALESCE(ea.expenses, '[]') as expenses,
		                      COALESCE(pa.payments, '[]') as payments
		               		               FROM groups g
		                        LEFT JOIN UserArray ua ON g.id = ua.id
		                        LEFT JOIN ExpenseArray ea ON g.id = ea.id
		                        LEFT JOIN PaymentArray pa ON g.id = pa.id
		                WHERE g.id = ?;
		               """;
		try {
			Map<String, Object> groupData = context.getData(query, groupId).getFirst();
			List<PreviewUserDTO> members;
			List<PreviewExpenseDTO> expenses;
			List<PreviewPaymentDTO> payments;

			//TODO: check this, maybe I don't need JSON...
			Gson gson = new Gson();

			logger.debug("Casting members");
			String userJSON = (String) groupData.get("users");
			members = gson.fromJson(userJSON, new TypeToken<List<PreviewUserDTO>>() {
			}.getType());
			logger.debug("Members: {}", members);

			logger.debug("Casting expenses");
			String expensesJson = (String) groupData.get("expenses");
			expenses = gson.fromJson(expensesJson, new TypeToken<List<PreviewExpenseDTO>>() {
			}.getType());
			logger.debug("Expenses: {}", expenses);

			logger.debug("Casting payments");
			String paymentsJson = (String) groupData.get("payments");
			payments = gson.fromJson(paymentsJson, new TypeToken<List<PreviewPaymentDTO>>() {
			}.getType());
			logger.debug("Payments: {}", payments);

			group = new DetailGroupDTO(
					(int) groupData.get("id"),
					(String) groupData.get("name"),
					members,
					expenses,
					payments
			);

		} catch ( Exception e ) {
			logger.error("GetGroup: {}", e.getMessage());
			return new ValueResponse<>("Failed to get group");
		}*/

		DetailGroupDTO group;

		try {
			Group groupData = context.getGroupDAO().getGroupById(groupId);
			group = new DetailGroupDTO(
					groupData.getId(),
					groupData.getName(),
					context.getUserDAO().getUsersFromGroup(groupId).stream().map(
							user -> new PreviewUserDTO(
									user.getId(),
									user.getUsername(),
									user.getEmail()
							)
					).toList(),
					context.getExpenseDAO().getExpensesFromGroup(groupId).stream().map(
							expense -> new PreviewExpenseDTO(
									expense.getId(),
									expense.getAmount(),
									expense.getTimestamp(),
									//context.getUserDAO().getUserById(expense.getPaidByUserId()).getUsername()
									expense.getBuyerEmail()
							)
					).toList(),
					context.getPaymentDAO().getPaymentsFromGroup(groupId).stream().map(
							payment -> new PreviewPaymentDTO(
									payment.getId(),
									payment.getValue(),
									payment.getDate(),
									payment.getBuyerName()
							)
					).toList()
			);
		} catch ( Exception e ) {
			logger.error("GetGroup: {}", e.getMessage());
			return new ValueResponse<>("Failed to get group");
		}

		return new ValueResponse<>(group);
	}

	@Override
	public String toString() {
		return "GET_GROUP " + groupId;
	}
}
