package pt.isec.pd.sharedLib.database.Entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Setter
@Getter
@SuperBuilder
//TODO: name OR email → name (?)
public final class Expense extends Entity {
	private int groupId;
	private double amount;
	private String description;
	private long timestamp;
	private String buyerEmail;
	private String[] participantsEmail;

	public Expense(int id, int groupId, double amount, String description, long timestamp,
	               String buyerEmail, String[] participantsEmail) {
		super(id);
		this.groupId = groupId;
		this.amount = amount;
		this.description = description;
		this.timestamp = timestamp;
		this.buyerEmail = buyerEmail;
		this.participantsEmail = participantsEmail;
	}

	//TODO: refactor this
	@Override
	public int hashCode() {
		return Objects.hash(id, groupId, amount, description, timestamp, buyerEmail);
	}

	//TODO: refactor this
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Expense) obj;
		return this.id == that.id &&
		       Objects.equals(this.groupId, that.groupId) &&
		       Objects.equals(this.amount, that.amount) &&
		       Objects.equals(this.description, that.description) &&
		       Objects.equals(this.timestamp, that.timestamp) &&
		       Objects.equals(this.buyerEmail, that.buyerEmail);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Expense {id: ").append(id);
		if (groupId > 0) sb.append(", group: ").append(groupId);
		sb.append(", amount: ").append(amount);
		if (description != null) sb.append(", description: ").append(description);
		if (timestamp > 0) sb.append(", timestamp: ").append(timestamp);
		if (buyerEmail != null) sb.append(", buyerEmail: ").append(buyerEmail);
		if (participantsEmail != null)
			sb.append(", participantsEmail: [").append(String.join(", ", participantsEmail)).append("]");
		sb.append("}");

		return sb.toString();
	}

}
