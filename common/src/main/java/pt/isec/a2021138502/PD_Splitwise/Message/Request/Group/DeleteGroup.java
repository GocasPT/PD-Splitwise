package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.sql.SQLException;

public record DeleteGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to delete group
		String query = "DELETE FROM groups WHERE group_id = ?";

		try {
			context.delete(query, groupId);
		} catch ( SQLException e ) {
			return new Response(false, "Failed to delete group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "DELETE_GROUP " + groupId;
	}
}
