package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;

public record Invite(String inviteUserEmail, String inviterUserEmail, String groupName) implements Serializable {
	@Override
	public String toString() {
		return "Invite{" +
				"groupName='" + groupName + '\'' +
				", inviteUserEmail='" + inviteUserEmail + '\'' +
				", inviterUserEmail='" + inviterUserEmail + '\'' +
				'}';
	}
}
