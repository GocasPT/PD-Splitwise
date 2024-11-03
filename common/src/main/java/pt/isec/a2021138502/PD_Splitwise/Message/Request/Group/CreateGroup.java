package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;


import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.sql.SQLException;

public record CreateGroup(String groupName, String userEmail) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		String queryCreateGroup = "INSERT INTO groups (name) VALUES (?)";
		String queryGetGroupID = "SELECT id FROM groups WHERE name = ?";
		String queryGetUserID = "SELECT id FROM users WHERE email = ?";
		String queryInsertGroupUser = "INSERT INTO group_users (group_id, user_id) VALUES (?, ?)";

		try {
			context.setData(queryCreateGroup, groupName);
			int groupID = (int) context.getData(queryGetGroupID, groupName).getFirst().get("id");
			int userID = (int) context.getData(queryGetUserID, userEmail).getFirst().get("id");
			context.setData(queryInsertGroupUser, groupID, userID);
		} catch ( SQLException e ) {
			System.out.println("Error on 'CreateGroup.execute': " + e.getMessage());
			return new Response(false, "Failed to create group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "CREATE_GROUP '" + groupName + "' " + userEmail;
	}
}
