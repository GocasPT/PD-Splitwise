package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Data.Group;
import pt.isec.a2021138502.PD_Splitwise.Data.User;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;

public record GetGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to get group

		Group group = new Group(
				1,
				"Fritadeira",
				new User[]{
						new User(
								"Sr. Batata",
								"9177, tira tira, mete, mete",
								"email@batata",
								"123"
						)
				}
		);

		return new ValueResponse<>(group);
	}

	@Override
	public String toString() {
		return "GET_GROUP " + groupId;
	}
}
