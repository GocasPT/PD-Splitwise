package pt.isec.pd.splitwise.sharedLib.network.request.Invite;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Group;
import pt.isec.pd.splitwise.sharedLib.database.Entity.User;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

public record InviteUser(int groupID, String guestUserEmail, String inviteeUserEmail) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("User '{}' invited '{}' to group '{}'", guestUserEmail, inviteeUserEmail, groupID);

		try {
			Group groupData = context.getGroupDAO().getGroupById(groupID);
			User userGuestData = context.getUserDAO().getUserByEmail(guestUserEmail);
			User userInviteeData = context.getUserDAO().getUserByEmail(inviteeUserEmail);
			context.getInviteDAO().createInvite(groupID, userGuestData.getId(), userInviteeData.getId());
			context.triggerNotification(guestUserEmail, "You have been invited to group '" + groupData.getName() + "'");
		} catch ( Exception e ) {
			logger.error("InviteUser: {}", e.getMessage());
			return new Response(false, "Failed to invite user to group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INVITE_USER " + groupID + " " + guestUserEmail + " " + inviteeUserEmail;
	}
}
