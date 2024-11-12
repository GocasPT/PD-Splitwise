package pt.isec.pd.sharedLib.database.DTO.Invite;

import java.io.Serializable;

public record PreviewInviteDTO(int id, String groupName, String inviterEmail,
                               String inviterUsername) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "PreviewInviteDTO{" +
		       "id=" + id +
		       ", groupName='" + groupName + '\'' +
		       ", inviterEmail='" + inviterEmail + '\'' +
		       ", inviterUserName='" + inviterUsername + '\'' +
		       '}';
	}
}
