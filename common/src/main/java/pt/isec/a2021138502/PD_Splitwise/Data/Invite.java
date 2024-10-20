package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;

public record Invite(String email, String groupName) implements Serializable {
	@Override
	public String toString() {
		return "Invite{" +
				"email='" + email + '\'' +
				", groupName='" + groupName + '\'' +
				'}';
	}
}
