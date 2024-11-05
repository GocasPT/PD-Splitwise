package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.Invite;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ListResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record GetInvites(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to get invites
		String query = """
		               SELECT
		                   invites.id AS invite_id,
		                   groups.name AS group_name,
		                   inviter.username AS inviter_name
		               FROM
		                   invites
		                       JOIN
		                   users AS recipient ON invites.user_id = recipient.id
		                       JOIN
		                   users AS inviter ON invites.inviter_user_id = inviter.id
		                       JOIN
		                   groups ON invites.group_id = groups.id
		               WHERE
		                   recipient.email = ?;
		               """;
		List<Invite> inviteList = new ArrayList<>();

		try {
			List<Map<String, Object>> listInvites = context.getData(query, email);

			for (Map<String, Object> invitesData : listInvites) {

				Invite invite = new Invite(
						(int) invitesData.get("invite_id"),
						(String) invitesData.get("group_name"),
						(String) invitesData.get("inviter_name")
				);
				inviteList.add(invite);
			}

		} catch ( Exception e ) {
			logger.error("GetInvites: {}", e.getMessage());
			return new ListResponse<>("Failed to get invites");
		}

		return new ListResponse<>(inviteList.toArray(Invite[]::new));
	}

	@Override
	public String toString() {
		return "GET_INVITATIONS " + email;
	}
}
