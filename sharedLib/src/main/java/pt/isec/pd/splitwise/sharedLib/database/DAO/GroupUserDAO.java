package pt.isec.pd.splitwise.sharedLib.database.DAO;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Group;
import pt.isec.pd.splitwise.sharedLib.database.Entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupUserDAO extends DAO{
	public GroupUserDAO(DataBaseManager dbManager) {
		super(dbManager);
	}

	public int createRelations(int grouId, int userId) throws SQLException {
		logger.debug("Creating group-user relation:\n\tgroupId={}\n\tuserId={}", grouId, userId);

		//language=SQLite
		String query = "INSERT INTO group_users (group_id, user_id) VALUES (?, ?) RETURNING id";
		int id = dbManager.executeWriteWithId(query, grouId, userId);

		logger.debug("Created relation with id: {}", id);

		return id;
	}

	public List<Group> getAllGroupsFromUser(String email) throws SQLException {
		logger.debug("Getting all groups from user {}", email);

		//language=SQLite
		String query = """
		               SELECT groups.id                   AS id,
		                      groups.name                 AS name,
		                      COUNT(DISTINCT gu2.user_id) AS member_count
		               FROM groups
		                        JOIN group_users ON groups.id = group_users.group_id
		                        JOIN users ON group_users.user_id = users.id
		                        JOIN group_users gu2 ON groups.id = gu2.group_id
		               WHERE users.email = ?
		               GROUP BY groups.id, groups.name""";

		List<Map<String, Object>> results = dbManager.executeRead(query, email);

		List<Group> groups = new ArrayList<>();
		for (Map<String, Object> row : results)
			groups.add(
					Group.builder()
							.id((int) row.get("id"))
							.name((String) row.get("name"))
							.numUsers((int) row.get("member_count"))
							.build()
			);

		logger.debug("Found {} groups", groups.size());

		return groups;
	}

	public List<User> getAllUsersFromGroup(int groupId) throws SQLException {
		logger.debug("Getting all users from group {}", groupId);

		//language=SQLite
		String query = """
		               SELECT users.id   AS id,
		                      users.username AS username,
		                      users.email AS email
		               FROM users
		                        JOIN group_users ON users.id = group_users.user_id
		               WHERE group_users.group_id = ?""";

		List<Map<String, Object>> results = dbManager.executeRead(query, groupId);

		List<User> users = new ArrayList<>();
		for (Map<String, Object> row : results)
			users.add(
					User.builder()
							.id((int) row.get("id"))
							.username((String) row.get("username"))
							.email((String) row.get("email"))
							.build()
			);

		logger.debug("Found {} users", users.size());

		return users;
	}

	public void deleteRelations(int groupId) throws SQLException {
		logger.debug("Deleting all relations from group {}", groupId);

		//language=SQLite
		String query = "DELETE FROM group_users WHERE group_id = ?";
		dbManager.executeWrite(query, groupId);
	}

	public void deleteRelations(int groupId, int userId) throws SQLException {
		logger.debug("Deleting relation between group {} and user {}", groupId, userId);

		//language=SQLite
		String query = "DELETE FROM group_users WHERE group_id = ? AND user_id = ?";
		dbManager.executeWrite(query, groupId, userId);
	}
}
