package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.Invite;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ListResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record GetInvites(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		System.out.println(this);

		//TODO: query to get invites
		Invite[] invites = {new Invite("Sra. Cebola", "Sr. Batata", "Group da felicidade")};

		return new ListResponse<>(invites);
	}

	@Override
	public String toString() {
		return "GET_INVITATIONS " + email;
	}
}
