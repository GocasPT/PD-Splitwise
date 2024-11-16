package pt.isec.pd.splitwise.sharedLib.network.request.Invite;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.User;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

public record InviteUser(int groupID, String guestEmail, String inviteeEmail) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("InviteUser: {}", this);

		try {
			User userGuestData = context.getUserDAO().getUserByEmail(guestEmail);
			User userInviteeData = context.getUserDAO().getUserByEmail(inviteeEmail);
			context.getInviteDAO().createInvite(groupID, userGuestData.getId(), userInviteeData.getId());
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
