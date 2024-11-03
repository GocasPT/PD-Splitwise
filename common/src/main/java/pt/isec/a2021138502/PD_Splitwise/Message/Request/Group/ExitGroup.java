package pt.isec.a2021138502.PD_Splitwise.Message.Request.Group;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

public record ExitGroup(String userEmail, int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		String querySelect = "SELECT id FROM user WHEN email = ?";
		String queryDelete = "DELETE FROM group_users WHERE user_id = ? AND group_id = ?";

		try {
			int userId = (int) context.getData(querySelect, userEmail).getFirst().get("id");
			context.setData(queryDelete, userId, groupId);
		} catch ( Exception e ) {
			System.out.println("Error on 'ExitGroup.execute': " + e.getMessage());
			return new Response(false, "Failed to exit group");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "EXIT_GROUP " + groupId;
	}
}
