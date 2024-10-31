package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;
import java.util.Objects;

public final class Invite implements Serializable {
	private int id;
	private String groupName;
	private String userThatInvites;

	public Invite(int id, String groupName) {
		this.id = id;
		this.groupName = groupName;
	}

	public Invite(int id, String groupName, String userThatInvites) {
		this.id = id;
		this.groupName = groupName;
		this.userThatInvites = userThatInvites;
	}

	public int getId() {
		return id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, groupName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Invite) obj;
		return this.id == that.id &&
				Objects.equals(this.groupName, that.groupName);
	}

	@Override
	public String toString() {
		return "Invite [id: " + id + "group: " + groupName + "]";
	}

}
