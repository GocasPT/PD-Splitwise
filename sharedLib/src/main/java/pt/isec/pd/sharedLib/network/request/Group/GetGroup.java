package pt.isec.pd.sharedLib.network.request.Group;

import pt.isec.pd.sharedLib.database.DTO.Expense.PreviewExpenseDTO;
import pt.isec.pd.sharedLib.database.DTO.Group.DetailGroupDTO;
import pt.isec.pd.sharedLib.database.DTO.Payment.PreviewPaymentDTO;
import pt.isec.pd.sharedLib.database.DTO.User.PreviewUserDTO;
import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.database.Entity.Group;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;
import pt.isec.pd.sharedLib.network.response.ValueResponse;

public record GetGroup(int groupId) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("GetGroup: {}", this);

		DetailGroupDTO group;
		try {
			Group groupData = context.getGroupDAO().getGroupById(groupId);
			group = new DetailGroupDTO(
					groupData.getId(),
					groupData.getName(),
					context.getUserDAO().getUsersFromGroup(groupId).stream().map(
							user -> new PreviewUserDTO(
									user.getId(),
									user.getUsername(),
									user.getEmail()
							)
					).toList(),
					context.getExpenseDAO().getExpensesFromGroup(groupId).stream().map(
							expense -> new PreviewExpenseDTO(
									expense.getId(),
									expense.getAmount(),
									expense.getTimestamp(),
									//context.getUserDAO().getUserById(expense.getPaidByUserId()).getUsername()
									expense.getBuyerEmail()
							)
					).toList(),
					context.getPaymentDAO().getPaymentsFromGroup(groupId).stream().map(
							payment -> new PreviewPaymentDTO(
									payment.getId(),
									payment.getValue(),
									payment.getDate(),
									payment.getBuyerName()
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
