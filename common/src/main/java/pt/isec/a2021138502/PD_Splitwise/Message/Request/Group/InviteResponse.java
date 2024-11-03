package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.sql.SQLException;
import java.util.Map;

public record InviteResponse(int inviteId, boolean isAccepted) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		String queryDelete = "DELETE FROM invites WHERE id = ?";
		String querySelect = "SELECT * FROM invites WHERE id = ?";
		String queryInsert = "INSERT INTO group_users (group_id, user_id) VALUES (?, ?) ";

		try {
			context.setData(queryDelete, inviteId);
			if (isAccepted) {
				Map<String, Object> inviteDate = context.getData(querySelect, inviteId).getFirst();
				int groupId = (int) inviteDate.get("group_id");
				int userId = (int) inviteDate.get("user_id");
				context.setData(queryInsert, groupId, userId);
			}
		} catch ( SQLException e ) {
			System.out.println("Error on 'InviteResponse.execute': " + e.getMessage());
			return new Response(false, "Failed to update invite");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INVITE_RESPONSE " + inviteId + " " + isAccepted;
	}
}
