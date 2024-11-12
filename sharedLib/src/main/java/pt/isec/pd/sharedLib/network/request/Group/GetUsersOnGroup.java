package pt.isec.pd.sharedLib.network.request.Group;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.Group;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;
import pt.isec.pd.sharedLib.network.response.ValueResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record GetUsersOnGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Getting users on group {}", groupId);
		Map<String, Object> data = new HashMap<>();

		/*//language=SQLite
		String query = """
		              SELECT
		                  users.id,
		                  users.username,
		                  users.email
		              FROM
		                  users
		              JOIN
		                  group_users
		              ON
		                  users.id = group_users.user_id
		              WHERE
		                  group_users.group_id = ?
		              """;

		Map<String, Object> groupData = new HashMap<>();
		try {
			Group group = context.getGroupById(groupId); //TODO: group doesnt have list of member
			logger.debug("Group: {}", group);
			groupData.put("group_name", group.getName());
			List<User> members = context.getData(query, groupId).stream().map(row -> {
				try {
					return new User(
							(int) row.get("id"),
							(String) row.get("username"),
							(String) row.get("email")
					);
				} catch (Exception e) {
					logger.error("GetUsersOnGroup: {}", e.getMessage());
					return null;
				}
			}).toList();
			logger.debug("Members: {}", members);
			List<PreviewUserDTO> membersDTO = new ArrayList<>();
			for (User member : members)
				membersDTO.add(
						new PreviewUserDTO(
								member.getId(),
								member.getUsername(),
								member.getEmail())
				);
			groupData.put("members", membersDTO);
		} catch ( SQLException e ) {
			logger.error("GetUsersOnGroup: {}", e.getMessage());
			return new Response(false, "Failed to get users on group");
		}*/

		try {
			Group groupData = context.getGroupDAO().getGroupById(groupId);
			data.put("group_name", groupData.getName());

			List<Map<String, Object>> members = new ArrayList<>();
			context.getUserDAO().getUsersFromGroup(groupId).forEach(user -> {
				members.add(new HashMap<>() {{
					put("id", user.getId());
					put("username", user.getUsername());
					put("email", user.getEmail());
				}});
			});
			data.put("members", members);
		} catch ( Exception e ) {
			logger.error("GetUsersOnGroup: {}", e.getMessage());
			return new Response(false, "Failed to get users on group");
		}

		return new ValueResponse<>(data);
	}

	@Override
	public String toString() {
		return "GET_USER_ON_GROUP " + groupId;
	}
}
