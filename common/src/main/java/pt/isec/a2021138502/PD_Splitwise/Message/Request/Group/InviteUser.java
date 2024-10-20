package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

//TODO: how server know which group is for?
public record InviteUser(int groupID, String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "LOGIN " + email;
	}
}
