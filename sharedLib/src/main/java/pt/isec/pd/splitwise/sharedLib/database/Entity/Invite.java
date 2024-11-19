package pt.isec.pd.splitwise.sharedLib.database.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Setter
@Getter
@SuperBuilder
public final class Invite extends Entity {
	private int groupId;
	private String inverterEmail;

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

		sb.append("Invite {id: ").append(id);
		sb.append(", group: ").append(groupId);
		if (inverterEmail != null) sb.append(", user: ").append(inverterEmail);
		sb.append("}");

		return sb.toString();
	}

}
