package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ListResponse;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record GetGroups(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		System.out.println(this);

		//TODO: query to get groups
		Group[] groups = {
				new Group("Fritadeira", new User[]{
						new User("Sr. Batata", "9177, tira tira, mete, mete", "email@batata", "123")
				}),
				new Group("Pringles", new User[]{
						new User("Sr. Batata", "9177, tira tira, mete, mete", "email@batata", "123"),
						new User("Sr. Batata", "9177, tira tira, mete, mete", "email@batata", "123")
				}),
		};

		return new ListResponse<>(groups);
	}

	@Override
	public String toString() {
		return "GET_GROUPS";
	}
}
