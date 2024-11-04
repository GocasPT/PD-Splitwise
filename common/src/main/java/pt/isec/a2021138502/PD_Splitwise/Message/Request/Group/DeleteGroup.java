package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record DeleteGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		String query = "DELETE FROM groups WHERE id = ?";

		try {
			context.setData(query, groupId);
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
