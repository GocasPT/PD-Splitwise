package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;

public record Invite(int inviteId, String inviteUserEmail, String inviterUserEmail,
                     String groupName) implements Serializable {
	@Override
	public String toString() {
		return "Invite [group: " + groupName + "]";
	}
}
