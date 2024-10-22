package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public record GetGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to get group
		Group group = null;
		String query = """
		               SELECT *
		               FROM %s
		               WHERE group_id = ?
		               """.formatted(context.GROUPS_TABLE);

		try {
			List<Map<String, Object>> rs = context.select(query, groupId);

			for (Map<String, Object> row : rs) {
				group = new Group(
						(int) row.get("group_id"),
						(String) row.get("name"),
						new User[]{} //TODO: get users from query
				);
			}

		} catch ( SQLException e ) {
			return new ValueResponse<>("Failed to get group");
		}

		return new ValueResponse<>(group);
	}

	@Override
	public String toString() {
		return "GET_GROUP " + groupId;
	}
}
