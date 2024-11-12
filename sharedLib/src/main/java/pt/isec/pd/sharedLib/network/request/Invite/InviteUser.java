package pt.isec.pd.sharedLib.network.request.Invite;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.User;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;

public record InviteUser(int groupID, String guestEmail, String inviteeEmail) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("InviteUser: {}", this);
		/*//language=SQLite
		String querySelect = """
		                                 SELECT
		                                     id
		                                 FROM
		                                     users
		                                 WHERE
		                                     email = ?
		                     """; //TODO: get guest user ID and invitee user ID
		//language=SQLite
		String queryInsert = """
		                     INSERT
		                     INTO
		                         invites
		                         (group_id, user_id, inviter_user_id)
		                     VALUES
		                         (?, ?,
		                          (SELECT
		                               id
		                           FROM
		                               users
		                           WHERE
		                               email = ?)
		                         )
		                     """;

		try {
			logger.debug("Inviting user {} to group {}", guestEmail, groupID);
			int userID = (int) context.getData(querySelect, guestEmail).getFirst().get(
					"id"); //TODO: exceptions for empty list/map result
			context.setData(queryInsert, groupID, userID);
			logger.debug("User {} invited to group {}", guestEmail, groupID);
			context.triggerNotification(guestEmail, "You have been invited to a group"); //TODO: check (exceptions, etc)
		} catch ( Exception e ) {
			logger.error("InviteUser: {}", e.getMessage());
			return new Response(false, "Failed to invite user to group");
		}*/

		try {
			User userGueatData = context.getUserDAO().getUserByEmail(guestEmail);
			User userInviteeData = context.getUserDAO().getUserByEmail(inviteeEmail);
			context.getInviteDAO().createInvite(groupID, userGueatData.getId(), userInviteeData.getId());
		} catch ( Exception e ) {
			logger.error("InviteUser: {}", e.getMessage());
			return new Response(false, "Failed to invite user to group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INVITE_USER " + groupID + " " + guestEmail;
	}
}
