package pt.isec.pd.splitwise.sharedLib.database.DTO.Invite;

import java.io.Serializable;

public record PreviewInviteDTO(int id, String groupName, String inviterEmail,
                               String inviterUsername) implements Serializable {
	@Override
	public String toString() {
		return "PreviewInviteDTO{" +
		       "id: " + id +
		       ", groupName: '" + groupName + '\'' +
		       ", inviterEmail: '" + inviterEmail + '\'' +
		       ", inviterUserName: '" + inviterUsername + '\'' +
		       '}';
	}
}
