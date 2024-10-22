package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record EditGroup(int groupId, String name) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to edit group
		String query = "UPDATE groups SET name = ? WHERE group_id = ?";

		try {
			context.update(query, name, groupId);
		} catch ( Exception e ) {
			return new Response(false, "Failed to edit group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "EDIT_GROUP " + groupId + " " + name;
	}
}
