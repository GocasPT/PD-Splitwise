package pt.isec.pd.sharedLib.database.DAO;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDAO extends DAO {
	public UserDAO(DataBaseManager dbManager) {
		super(dbManager);
	}

	//TODO: make to use statement where but sync with dbManager

	public int createUser(String username, String email, String phoneNumber, String password) throws SQLException {
		logger.debug("Creating user:\n\tusername={}\n\temail={},\n\tphoneNumber={},\n\tpassword={}", username, email,
		             phoneNumber, password);
		//language=SQLite
		String query = "INSERT INTO users (username, email, phone_number, password) VALUES (?, ?, ?, ?)";
		/*Connection connection = dbManager.getConnection();
		try ( PreparedStatement pstmt = connection.prepareStatement(query) ) {
			pstmt.setString(1, username);
			pstmt.setString(2, email);
			pstmt.setString(3, phoneNumber);
			pstmt.setString(4, password);
			int id = pstmt.executeUpdate();
			//TODO: update version
			return id;
		}*/
		return dbManager.executeWrite(query, username, email, phoneNumber, password);
	}

	public List<User> getAllUsers() throws SQLException {
		logger.debug("Getting all users");
		//language=SQLite
		String query = "SELECT * FROM users";
		/*List<Map<String, Object>> results = dbManager.getData(query);
		List<User> users = results.map(row -> new User(
				(int) row.get("id"),
				(String) row.get("username"),
				(String) row.get("email"),
				(String) row.get("phone_number"),
				(String) row.get("password")
		));
		return users;*/
		List<Map<String, Object>> results = dbManager.executeRead(query);
		List<User> users = new ArrayList<>();
		for (Map<String, Object> row : results)
			users.add(
					new User(
							(int) row.get("id"),
							(String) row.get("username"),
							(String) row.get("email"),
							(String) row.get("phone_number"),
							(String) row.get("password")
					)
			);
		return users;
	}

	public User getUserByEmail(String email) throws SQLException {
		logger.debug("Getting user by email: {}", email);
		//language=SQLite
		String query = "SELECT * FROM users WHERE email = ?";
		List<Map<String, Object>> results = dbManager.executeRead(query, email);
		if (!results.isEmpty()) {
			Map<String, Object> row = results.getFirst();
			return new User(
					(int) row.get("id"),
					(String) row.get("username"),
					(String) row.get("email"),
					(String) row.get("phone_number"),
					(String) row.get("password")
			);
		}
		return null;
	}

	public List<User> getUsersFromGroup(int groupId) throws SQLException {
		logger.debug("Getting users from group id: {}", groupId);
		//language=SQLite
		String query = """
		               SELECT users.id AS id, users.username AS username, users.email AS email, users.phone_number AS phone_number, users.password AS password
		               FROM users
		                        JOIN group_users ON users.id = group_users.user_id
		               WHERE group_users.group_id = ?
		               """;
		List<Map<String, Object>> results = dbManager.executeRead(query, groupId);
		List<User> users = new ArrayList<>();
		for (Map<String, Object> row : results)
			users.add(
					new User(
							(int) row.get("id"),
							(String) row.get("username"),
							(String) row.get("email"),
							(String) row.get("phone_number"),
							(String) row.get("password")
					)
			);
		return users;
	}

	//TODO: get users associated with expense (?)

	public boolean editUser(int id, String username, String email, String phoneNumber, String password) throws SQLException {
		logger.debug("Editing user id={}:\n\tusername={}\n\temail={},\n\tphoneNumber={},\n\tpassword={}", id, username,
		             email, phoneNumber, password);
		//language=SQLite
		String query = "UPDATE users SET username = ?, email = ?, phone_number = ?, password = ? WHERE id = ?";
		return dbManager.executeWrite(query, username, email, phoneNumber, password, id) > 0;
	}
}
