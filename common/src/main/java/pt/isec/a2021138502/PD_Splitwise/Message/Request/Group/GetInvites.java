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
		List<Invite> inviteList = new ArrayList<>();
		String querySelectUser = "SELECT id FROM users WHERE email = ?";
		String querySelectInvites = "SELECT * FROM invites WHERE user_id = ?";
		String querySelectGroup = "SELECT name FROM groups WHERE id = ?";

		try {
			int userId = (int) context.select(querySelectUser, email).getFirst().get("id");
			List<Map<String, Object>> listInvites = context.select(querySelectInvites, userId);

			for (Map<String, Object> invitesData : listInvites) {
				String groupName = (String) context.select(querySelectGroup,
				                                           invitesData.get("group_id")).getFirst().get("name");
				Invite invite = new Invite(
						(int) invitesData.get("id"),
						groupName
				);
				inviteList.add(invite);
			}

		} catch ( Exception e ) {
			System.out.println("Error on 'GetInvites.execute': " + e.getMessage());
			return new ListResponse<>("Failed to get invites");
		}

		return new ListResponse<>(inviteList.toArray(Invite[]::new));
	}

	@Override
	public String toString() {
		return "GET_INVITATIONS " + email;
	}
}
