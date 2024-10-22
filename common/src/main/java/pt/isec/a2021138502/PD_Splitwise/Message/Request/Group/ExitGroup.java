package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record ExitGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to exit group
		String query = "";

		try {
			context.update(query, groupId);
		} catch ( Exception e ) {
			return new Response(false, "Failed to exit group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "EXIT_GROUP " + groupId;
	}
}
