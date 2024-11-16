package pt.isec.pd.splitwise.sharedLib.network.request.Group;

import pt.isec.pd.splitwise.sharedLib.database.DTO.Group.PreviewGroupDTO;
import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.ListResponse;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

import java.util.List;

public record GetGroups(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("GetGroups: {}", this);

		List<PreviewGroupDTO> groupList;
		try {
			groupList = context.getGroupUserDAO().getAllGroupsFromUser(email).stream().map(
					group -> new PreviewGroupDTO(
							group.getId(),
							group.getName(),
							group.getNumUsers()
					)
			).toList();
		} catch ( Exception e ) {
			logger.error("GetGroups: {}", e.getMessage());
			return new ListResponse<>("Failed to get groups");
		}

		return new ListResponse<>(groupList.toArray(PreviewGroupDTO[]::new));
	}

	@Override
	public String toString() {
		return "GET_GROUPS " + email;
	}
}
