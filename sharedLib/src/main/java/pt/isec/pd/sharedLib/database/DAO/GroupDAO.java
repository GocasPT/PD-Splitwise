package pt.isec.pd.sharedLib.database.DAO;


import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.Group;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GroupDAO extends DAO {
	public GroupDAO(DataBaseManager dbManager) {
		super(dbManager);
	}

	//TODO: make to use statement where but sync with dbManager

	public int createGroup(String name, int user_id) throws SQLException {
		logger.debug("Creating group with name: {}", name);
		//language=SQLite
		String queryInsertGroup = "INSERT INTO groups (name) VALUES (?) RETURNING id";
		//language=SQLite
		String queryInsertUserGroup = "INSERT INTO group_users (group_id, user_id) VALUES (?, ?)";
		int id = dbManager.executeWriteWithId(queryInsertGroup, name);
		logger.debug("Associate user {} with created group {}", user_id, id);
		dbManager.executeWrite(queryInsertUserGroup, id, user_id);
		return id;
	}

	public List<Group> getAllGroups() throws SQLException {
		logger.debug("Getting all groups");
		//language=SQLite
		String query = "SELECT * FROM groups";
		List<Map<String, Object>> results = dbManager.executeRead(query);
		List<Group> groups = new ArrayList<>();
		for (Map<String, Object> row : results) {
			groups.add(new Group(
					(int) row.get("id"),
					(String) row.get("name")
			));
		}
		return groups;
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
		for (Map<String, Object> row : results) {
			groups.add(new Group(
					(int) row.get("id"),
					(String) row.get("name"),
					(int) row.get("member_count")
			));
		}
		return groups;
	}

	public Group getGroupById(int id) throws SQLException {
		logger.debug("Getting group with id: {}", id);
		//language=SQLite
		String query = "SELECT * FROM groups WHERE id = ?";
		List<Map<String, Object>> results = dbManager.executeRead(query, id);
		for (Map<String, Object> row : results) {
			return new Group(
					(int) row.get("id"),
					(String) row.get("name")
			);
		}
		return null;
	}

	public boolean editGroup(int id, String name) throws SQLException {
		logger.debug("Editing group with id {}\n\tname: {}", id, name);
		//language=SQLite
		String query = "UPDATE groups SET name = ? WHERE id = ?";
		return dbManager.executeWrite(query, name, id) > 0;
	}

	public boolean deleteGroup(int id) throws SQLException {
		//TODO: check if group have debts or something like that
		logger.debug("Deleting group with id: {}", id);
		//language=SQLite
		String query = "DELETE FROM groups WHERE id = ?";
		return dbManager.executeWrite(query, id) > 0;
	}
}
