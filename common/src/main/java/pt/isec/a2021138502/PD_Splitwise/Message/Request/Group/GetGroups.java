package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ListResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record GetGroups(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		List<Group> groupList = new ArrayList<>();
		String query = """
		               SELECT g.id as group_id,
		               	g.name AS group_name,
		               	COUNT(gu.user_id) AS total_members
		               FROM groups g
		               JOIN group_users gu ON g.id = gu.group_id
		               JOIN users u ON gu.user_id = u.id
		               WHERE g.id IN
		               	(SELECT group_id
		               	FROM group_users gu2
		               	JOIN users u2 ON gu2.user_id = u2.id
		               	WHERE u2.email = ?)
		               GROUP BY g.id
		               ORDER BY g.name
		               """;

		try {
			List<Map<String, Object>> listGroups = context.select(query, email);

			for (Map<String, Object> groupData : listGroups) {
				Group group = new Group(
						(int) groupData.get("group_id"),
						(String) groupData.get("group_name"),
						(int) groupData.get("total_members")
				);
				groupList.add(group);
			}

		} catch ( SQLException e ) {
			System.out.println("Error on 'GetGroups.execute': " + e.getMessage());
			return new ListResponse<>("Failed to get groups");
		}

		return new ListResponse<>(groupList.toArray(Group[]::new));
	}

	@Override
	public String toString() {
		return "GET_GROUPS " + email;
	}
}
