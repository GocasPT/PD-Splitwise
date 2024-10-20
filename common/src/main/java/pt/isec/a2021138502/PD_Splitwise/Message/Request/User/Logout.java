package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record Logout(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to set user as offline

		return new Response(true);
	}

	@Override
	public String toString() {
		return "LOGOUT " + email;
	}
}
