package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record Register(String username, String phone, String email, String password) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to see if email is unique

		return new Response(true);
	}

	@Override
	public String toString() {
		return "REGISTER " + username + " " + phone + " " + email + " " + password;
	}
}
