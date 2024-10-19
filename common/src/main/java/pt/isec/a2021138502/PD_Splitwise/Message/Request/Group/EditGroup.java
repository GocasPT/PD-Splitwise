package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

/**
 * @param groupId TODO: groupId in client-side or server-side?
 */
public record EditGroup(int groupId, String name) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "EDIT_GROUP" + " " + groupId + " " + name;
	}
}
