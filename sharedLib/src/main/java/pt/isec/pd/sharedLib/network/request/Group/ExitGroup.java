package pt.isec.pd.sharedLib.network.request.Group;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;

public record ExitGroup(String userEmail, int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("ExitGroup: {}", this);
		/*//language=SQLite
		String querySelect = """
		                     SELECT
		                     	id
		                     FROM
		                     	users
		                     WHERE
		                     	email = ?
		                     """;
		//language=SQLite
		String queryDelete = """
		                     DELETE
		                     FROM
		                         group_users
		                     WHERE
		                         user_id = ?
		                     	AND group_id = ?
		                     """;

		try {
			int userId = (int) context.getData(querySelect, userEmail).getFirst().get("id");
			context.setData(queryDelete, userId, groupId);
		} catch ( Exception e ) {
			logger.error("ExitGroup: {}", e.getMessage());
			return new Response(false, "Failed to exit group");
		}*/

		try {
			//TODO: context.getGroupDAO().exitGroup(userEmail, groupId);
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
