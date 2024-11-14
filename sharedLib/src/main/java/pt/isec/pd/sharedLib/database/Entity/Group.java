package pt.isec.pd.sharedLib.database.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
@SuperBuilder
public final class Group extends Entity {
	private String name;
	private int numUsers;
	private List<User> members;
	private List<Expense> expenses;
	private List<Payment> payments;

	//TODO: refactor this
	@Override
	public int hashCode() {
		return Objects.hash(id, name, members, expenses, payments);
	}

	//TODO: refactor this
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Group) obj;
		return this.id == that.id &&
		       Objects.equals(this.name, that.name) &&
		       Objects.equals(this.members, that.members) &&
		       Objects.equals(this.expenses, that.expenses) &&
		       Objects.equals(this.payments, that.payments);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Group [id: ").append(id);
		sb.append(", name: ").append(name);

		if (members == null) sb.append(", numUsers: ").append(numUsers);
		else {
			sb.append(",\n\tusers: ").append(members);
			sb.append(",\n\texpenses: ").append(expenses);
			sb.append(",\n\tpayments: ").append(payments).append("\n");
		}
		sb.append("]");

		return sb.toString();
	}
}
