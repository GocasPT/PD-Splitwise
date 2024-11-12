package pt.isec.pd.sharedLib.database.Entity;

import java.util.Objects;

public final class Invite extends Entity {
	private int groupId;
	private String inverterEmail;

	public Invite(int id, int groupId, String inverterEmail) {
		super(id);
		this.groupId = groupId;
		this.inverterEmail = inverterEmail;
	}

	public int getGroupId() {
		return groupId;
	}

	public String getInverterEmail() {
		return inverterEmail;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public void setInverterEmail(String inverterEmail) {
		this.inverterEmail = inverterEmail;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, groupId);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Invite) obj;
		return this.id == that.id &&
		       Objects.equals(this.groupId, that.groupId);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Invite [id: ").append(id);
		sb.append(", group: ").append(groupId);
		if (inverterEmail != null) sb.append(", user: ").append(inverterEmail);
		sb.append("]");

		return sb.toString();
	}

}
