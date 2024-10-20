package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record DeleteGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "DELETE_GROUP " + groupId;
	}
}
