package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record InviteResponse(int inviteId, boolean isAccepted) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to update invite

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INVITE_RESPONSE " + inviteId + " " + isAccepted;
	}
}
