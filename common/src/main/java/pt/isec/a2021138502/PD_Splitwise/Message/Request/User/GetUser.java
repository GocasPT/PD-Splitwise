package pt.isec.a2021138502.PD_Splitwise.Message.Request.User;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;

public record GetUser(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		System.out.println(this);

		//TODO: query to get user data
		User user = new User("bata", "9177, tira tira, mete mete", "email.batata", "password");

		return new ValueResponse<>(user);
	}

	@Override
	public String toString() {
		return "GET_USER " + email;
	}
}
