package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;


import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.sql.SQLException;

public record CreateGroup(String name, String userEmail) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to create group
		String query = "INSERT INTO groups (name, admin) VALUES (?, ?)";

		try {
			context.insert(query, name, userEmail);
		} catch ( SQLException e ) {
			return new Response(false, "Failed to create group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "CREATE_GROUP '" + name + "' " + userEmail;
	}
}
