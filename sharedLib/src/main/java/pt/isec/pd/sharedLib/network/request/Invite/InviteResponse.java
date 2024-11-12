package pt.isec.pd.sharedLib.network.request.Invite;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;

public record InviteResponse(int inviteId, boolean isAccepted) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("InviteResponse: {}", this);
		/*//language=SQLite
		String queryDelete = """
		                     DELETE
		                     FROM
		                         invites
		                     WHERE
		                         id = ?
		                     """;
		//language=SQLite
		String querySelect = """
		                     SELECT
		                         *
		                     FROM
		                         invites
		                     WHERE
		                         id = ?""";
		//language=SQLite
		String queryInsert = """
		                     INSERT
		                     INTO
		                         group_users
		                         (group_id, user_id)
		                     VALUES
		                         (?, ?)
		                     """;

		try {
			context.setData(queryDelete, inviteId);
			if (isAccepted) {
				Map<String, Object> inviteDate = context.getData(querySelect, inviteId).getFirst();
				int groupId = (int) inviteDate.get("group_id");
				int userId = (int) inviteDate.get("user_id");
				context.setData(queryInsert, groupId, userId);
			}
		} catch ( Exception e ) {
			logger.error("InviteResponse: {}", e.getMessage());
			return new Response(false, "Failed to update invite");
		}*/

		try {
			if (isAccepted)
				context.getInviteDAO().acceptInvite(inviteId);
			else
				context.getInviteDAO().declineInvite(inviteId);
		} catch ( Exception e ) {
			logger.error("InviteResponse: {}", e.getMessage());
			return new Response(false, "Failed to update invite");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INVITE_RESPONSE " + inviteId + " " + isAccepted;
	}
}
