package pt.isec.pd.sharedLib.database.DAO;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.Invite;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InviteDAO extends DAO {
	public InviteDAO(DataBaseManager dbManager) {
		super(dbManager);
	}

	//TODO: make to use statement where but sync with dbManager

	public int createInvite(int group_id, int guest_user_id, int inviter_user_id) throws SQLException {
		logger.debug("Creating invite for group {} with guest {} by user {}", group_id, guest_user_id, inviter_user_id);
		//language=SQLite
		String query = "INSERT INTO invites (group_id, guest_user_id, inviter_user_id) VALUES (?, ?, ?)";
		return dbManager.executeWrite(query, group_id, guest_user_id, inviter_user_id);
	}

	public List<Invite> getAllInvitesFromUser(String userEmail) throws SQLException {
		logger.debug("Getting all invites for user {}", userEmail);

		//TODO: guester_user_name + inviter_user_name
		//language=SQLite
		String query = """
		               SELECT invites.id     AS id,
		                      groups.id      AS group_id,
		                      inviter.email    AS inviter_email
		               FROM invites
		                        JOIN groups ON groups.id = invites.group_id
		                        JOIN users AS guest ON guest.id = invites.guest_user_id
		               		JOIN users AS inviter ON inviter.id = invites.inviter_user_id
		               WHERE guest.email = ?""";

		List<Map<String, Object>> result = dbManager.executeRead(query, userEmail);
		List<Invite> invites = new ArrayList<>();
		for (Map<String, Object> row : result)
			invites.add(
					Invite.builder()
							.id((int) row.get("id"))
							.groupId((int) row.get("group_id"))
							.inverterEmail((String) row.get("inviter_email"))
							.build()
			);
		return invites;
	}

	//TODO: rollback system (ex: can delete invite but can't insert into group_users)
	public boolean acceptInvite(int invite_id) throws SQLException {
		logger.debug("Accepting invite with id {}", invite_id);
		//language=SQLite
		String queryDelete = "DELETE FROM invites WHERE id = ?";
		//language=SQLite
		String queryInsert = "INSERT INTO group_users (group_id, user_id) SELECT group_id, guest_user_id FROM invites WHERE id = ?";
		dbManager.executeWrite(queryDelete, invite_id);
		return dbManager.executeWrite(queryInsert, invite_id) > 0;
	}

	public boolean declineInvite(int invite_id) throws SQLException {
		logger.debug("Declining invite with id {}", invite_id);
		//language=SQLite
		String query = "DELETE FROM invites WHERE id = ?";
		return dbManager.executeWrite(query, invite_id) > 0;
	}
}
