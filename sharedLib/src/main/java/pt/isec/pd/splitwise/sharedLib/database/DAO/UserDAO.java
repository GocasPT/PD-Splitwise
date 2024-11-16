package pt.isec.pd.splitwise.sharedLib.database.DAO;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.User;

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

		return dbManager.executeWrite(query, username, email, phoneNumber, password);
	}

	public List<User> getAllUsers() throws SQLException {
		logger.debug("Getting all users");

		//language=SQLite
		String query = "SELECT * FROM users";

		List<Map<String, Object>> results = dbManager.executeRead(query);

		List<User> users = new ArrayList<>();
		for (Map<String, Object> row : results)
			users.add(
					User.builder()
							.id((int) row.get("id"))
							.username((String) row.get("username"))
							.email((String) row.get("email"))
							.phoneNumber((String) row.get("phone_number"))
							.password((String) row.get("password"))
							.build()
			);

		return users;
	}

	public User getUserByEmail(String email) throws SQLException {
		logger.debug("Getting user by email: {}", email);

		//language=SQLite
		String query = "SELECT * FROM users WHERE email = ?";

		List<Map<String, Object>> results = dbManager.executeRead(query, email);

		if (results.isEmpty())
			return null;

		Map<String, Object> row = results.getFirst();
		return User.builder()
				.id((int) row.get("id"))
				.username((String) row.get("username"))
				.email((String) row.get("email"))
				.phoneNumber((String) row.get("phone_number"))
				.password((String) row.get("password"))
				.build();
	}


	public boolean editUser(int id, String username, String email, String phoneNumber, String password) throws SQLException {
		logger.debug("Editing user id={}:\n\tusername={}\n\temail={},\n\tphoneNumber={},\n\tpassword={}", id, username,
		             email, phoneNumber, password);

		//language=SQLite
		String query = "UPDATE users SET username = ?, email = ?, phone_number = ?, password = ? WHERE id = ?";

		return dbManager.executeWrite(query, username, email, phoneNumber, password, id) > 0;
	}
}
