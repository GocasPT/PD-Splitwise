package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record EditGroup(int groupId, String newGroupName) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		String query = "UPDATE groups SET name = ? WHERE id = ?";

		try {
			context.update(query, newGroupName, groupId);
		} catch ( Exception e ) {
			System.out.println("Error on 'EditGroup.execute': " + e.getMessage());
			return new Response(false, "Failed to edit group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "EDIT_GROUP " + groupId + " " + newGroupName;
	}
}
