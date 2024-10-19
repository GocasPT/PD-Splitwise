package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;

public record GetGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//return context.getGroup(groupId);
		return new ValueResponse<Integer>(groupId);
	}

	@Override
	public String toString() {
		return "GET_GROUP " + groupId;
	}
}
