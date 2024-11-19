package pt.isec.pd.splitwise.sharedLib.network.request.Group;

import pt.isec.pd.splitwise.sharedLib.database.DTO.Expense.PreviewExpenseDTO;
import pt.isec.pd.splitwise.sharedLib.database.DTO.Group.DetailGroupDTO;
import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Group;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;
import pt.isec.pd.splitwise.sharedLib.network.response.ValueResponse;

public record GetGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Getting group {} info", groupId);

		DetailGroupDTO group;
		try {
			Group groupData = context.getGroupDAO().getGroupById(groupId);
			group = new DetailGroupDTO(
					groupData.getId(),
					groupData.getName(),
					context.getExpenseDAO().getAllExpensesFromGroup(groupId).stream().map(
							expense -> new PreviewExpenseDTO(
									expense.getId(),
									expense.getAmount(),
									expense.getDate(),
									//TODO: context.getUserDAO().getUserById(expense.getPaidByUserId()).getUsername()
									expense.getBuyerEmail() //TODO: Pair<string, string> (userEmail, username)
							)
					).toList()
			);
		} catch ( Exception e ) {
			logger.error("GetGroup: {}", e.getMessage());
			return new ValueResponse<>("Failed to get group");
		}

		return new ValueResponse<>(group);
	}

	@Override
	public String toString() {
		return "GET_GROUP " + groupId;
	}
}
