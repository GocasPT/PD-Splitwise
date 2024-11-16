package pt.isec.pd.splitwise.sharedLib.network.request.Group;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Group;
import pt.isec.pd.splitwise.sharedLib.database.Entity.User;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

public record ExitGroup(String userEmail, int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("ExitGroup: {}", this);

		try {
			//TODO:
			// - use new layer (GrouUserDAO) to remove user from group
			// - check if use have any debts in is name

			User user = context.getUserDAO().getUserByEmail(userEmail);
			Group group = context.getGroupDAO().getGroupById(groupId);
		} catch ( Exception e ) {
			logger.error("ExitGroup: {}", e.getMessage());
			return new Response(false, "Failed to exit group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "EXIT_GROUP " + groupId;
	}
}
