package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import pt.isec.a2021138502.PD_Splitwise.Data.*;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;

import java.util.List;
import java.util.Map;

public record GetGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		Group group;
		String query = """
		               WITH UserArray AS (
		                   SELECT
		                       g.id,
		                       json_group_array(
		                               json_object(
		                                       'email', u.email,
		                                       'username', u.username
		                               )
		                       ) as users
		                   FROM groups g
		                            LEFT JOIN group_users gu ON g.id = gu.group_id
		                            LEFT JOIN users u ON gu.user_id = u.id
		                   GROUP BY g.id
		               ),
		                    ExpenseArray AS (
		                        SELECT
		                            g.id,
		                            json_group_array(
		                                    json_object(
		                                            'id', e.id,
		                                            'value', e.amount,
		                                            'date', e.date
		                                    )
		                            ) as expenses
		                        FROM groups g
		                                 LEFT JOIN expenses e ON g.id = e.group_id
		                        GROUP BY g.id
		                    ),
		                    PaymentArray AS (
		                        SELECT
		                            g.id,
		                            json_group_array(
		                                    json_object(
		                                            'id', p.id,
		                                            'value', p.amount
		                                        --'date', p
		                                    )
		                            ) as payments
		                        FROM groups g
		                                 LEFT JOIN payments p ON g.id = p.id
		                        GROUP BY g.id
		                    )
		               SELECT
		                   g.id,
		                   g.name,
		                   COALESCE(ua.users, '[]') as users,
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
			List<User> members;
			List<Expense> expenses;
			List<Payment> payments;

			//TODO: check this, maybe I don't need JSON...
			//*
			Gson gson = new Gson();

			String userJSON = (String) groupData.get("users");
			members = gson.fromJson(userJSON, new TypeToken<List<User>>() {
			}.getType());

			String expensesJson = (String) groupData.get("expenses");
			expenses = gson.fromJson(expensesJson, new TypeToken<List<Expense>>() {
			}.getType());

			String paymentsJson = (String) groupData.get("payments");
			payments = gson.fromJson(paymentsJson, new TypeToken<List<Payment>>() {
			}.getType());
			//*/


			group = new Group((int) groupData.get("id"), (String) groupData.get("name"), members, expenses, payments);

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
