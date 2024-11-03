package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.sql.SQLException;

public record InviteUser(int groupID, String guestEmail, String inviteeEmail) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		String querySelect = "SELECT id FROM users WHERE email = ?"; //TODO: get guest user ID and invitee user ID
		String queryInsert = "INSERT INTO invites (group_id, user_id) VALUES (?, ?)";

		try {
			int userID = (int) context.select(querySelect, guestEmail).getFirst().get("id"); //TODO: exceptions for empty list/map result
			context.insert(queryInsert, groupID, userID);
			context.triggerNotification(guestEmail, "You have been invited to a group"); //TODO: check (exceptions, etc)
		} catch ( SQLException e ) {
			System.out.println("Error on 'InviteUser.execute': " + e.getMessage());
			return new Response(false, "Failed to invite user to group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INVITE_USER " + groupID + " " + guestEmail;
	}
}
