package pt.isec.pd.splitwise.sharedLib.network.request.Invite;

import pt.isec.pd.splitwise.sharedLib.database.DTO.Invite.PreviewInviteDTO;
import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.ListResponse;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

import java.sql.SQLException;
import java.util.List;

public record GetInvites(String email) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("GetInvites: {}", this);

		List<PreviewInviteDTO> inviteList;
		try {
			inviteList = context.getInviteDAO().getAllInvitesFromUser(email).stream().map(
					invite -> {
						try {
							return new PreviewInviteDTO(
									invite.getId(),
									context.getGroupDAO().getGroupById(invite.getGroupId()).getName(),
									invite.getInverterEmail(),
									context.getUserDAO().getUserByEmail(invite.getInverterEmail()).getUsername()
							);
						} catch ( SQLException e ) {
							throw new RuntimeException(e);
						}
					}
			).toList();
		} catch ( Exception e ) {
			logger.error("GetInvites: {}", e.getMessage());
			return new ListResponse<>("Failed to get invites");
		}

		return new ListResponse<>(inviteList.toArray(PreviewInviteDTO[]::new));
	}

	@Override
	public String toString() {
		return "GET_INVITATIONS " + email;
	}
}
