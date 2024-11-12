package pt.isec.pd.sharedLib.network.request.Group;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;

public record DeleteGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("DeleteGroup: {}", this);
		/*//language=SQLite
		String query = """
		               DELETE
		               FROM
		                	groups
		               WHERE
		                    id = ?
		               """;

		try {
			context.setData(query, groupId);
		} catch ( Exception e ) {
			logger.error("DeleteGroup: {}", e.getMessage());
			return new Response(false, "Failed to delete group");
		}*/

		try {
			context.getGroupDAO().deleteGroup(groupId);
		} catch ( Exception e ) {
			logger.error("DeleteGroup: {}", e.getMessage());
			return new Response(false, "Failed to delete group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "DELETE_GROUP " + groupId;
	}
}
