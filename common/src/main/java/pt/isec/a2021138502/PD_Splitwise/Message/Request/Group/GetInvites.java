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
		String query = """
		               SELECT *
		               FROM %s
		               WHERE email = ?
		               """.formatted(context.INVITES_TABLE); //TODO: joins

		try {
			List<Map<String, Object>> rs = context.select(query, email);

			for (Map<String, Object> row : rs) {
				Invite invite = new Invite(
						(int) row.get("invite_id"),
						"TEST", //row.getString(""),
						"DEBUG", //row.getString(""),
						"GROUP_NAME" //row.getString("")
				);
				inviteList.add(invite);
			}

		} catch ( Exception e ) {
			return new ListResponse<>("Failed to get invites");
		}

		return new ListResponse<>(inviteList.toArray(Invite[]::new));
	}

	@Override
	public String toString() {
		return "GET_INVITATIONS " + email;
	}
}
