package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.GroupPreview;
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
		//TODO: query to get groups
		List<GroupPreview> groupList = new ArrayList<>();
		String query = """
		               SELECT g.id as group_id,
		               	g.name AS group_name,
		               	COUNT(gu.user_id) AS total_members
		               FROM %s g
		               JOIN %s gu ON g.id = gu.group_id
		               JOIN %s u ON gu.user_id = u.id
		               WHERE g.id IN
		               	(SELECT group_id
		               	FROM %s gu2
		               	JOIN %s u2 ON gu2.user_id = u2.id
		               	WHERE u2.email = ?)
		               GROUP BY g.id
		               ORDER BY g.name
		               """.formatted(context.GROUPS_TABLE, context.GROUP_USERS_TABLE, context.USERS_TABLE,
		                             context.GROUP_USERS_TABLE, context.USERS_TABLE);

		try {
			List<Map<String, Object>> rs = context.select(query, email);

			for (Map<String, Object> row : rs) {
				GroupPreview group = new GroupPreview(
						(int) row.get("group_id"),
						(String) row.get("group_name"),
						(int) row.get("total_members")
				);
				groupList.add(group);
			}

		} catch ( SQLException e ) {
			return new ListResponse<>("Failed to get groups");
		}

		return new ListResponse<>(groupList.toArray(GroupPreview[]::new));
	}

	@Override
	public String toString() {
		return "GET_GROUPS " + email;
	}
}
