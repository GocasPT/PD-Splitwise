package pt.isec.pd.splitwise.sharedLib.database.DAO;


import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Group;

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
		logger.debug("Creating group:\n\tname={}", name);

		//language=SQLite
		String queryInsertGroup = "INSERT INTO groups (name) VALUES (?) RETURNING id";
		int id = dbManager.executeWriteWithId(queryInsertGroup, name);

		logger.debug("Created group with id: {}", id);

		dbManager.getGroupUserDAO().createRelations(id, user_id);
		return id;
	}

	public List<Group> getAllGroups() throws SQLException {
		logger.debug("Getting all groups");

		//language=SQLite
		String query = "SELECT * FROM groups";
		List<Map<String, Object>> results = dbManager.executeRead(query);
		List<Group> groups = new ArrayList<>();
		for (Map<String, Object> row : results) {
			groups.add(
					Group.builder()
							.id((int) row.get("id"))
							.name((String) row.get("name"))
							.build()
			);
		}
		return groups;
	}

	public Group getGroupById(int id) throws SQLException {
		logger.debug("Getting group with id: {}", id);

		//language=SQLite
		String query = "SELECT * FROM groups WHERE id = ?";
		List<Map<String, Object>> results = dbManager.executeRead(query, id);
		for (Map<String, Object> row : results) {
			return Group.builder()
					.id((int) row.get("id"))
					.name((String) row.get("name"))
					.build();
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
