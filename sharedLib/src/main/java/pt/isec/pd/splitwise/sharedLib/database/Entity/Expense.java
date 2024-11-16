package pt.isec.pd.splitwise.sharedLib.database.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@SuperBuilder
public final class Expense extends Entity {
	private int groupId;
	private double amount;
	private String description;
	private LocalDate date;
	private String buyerEmail; //TODO: email → pair<username, email>
	private String[] participantsEmail; //TODO: email → pair<username, email>

	public Expense(int id, int groupId, double amount, String description, LocalDate date,
	               String buyerEmail, String[] participantsEmail) {
		super(id);
		this.groupId = groupId;
		this.amount = amount;
		this.description = description;
		this.date = date;
		this.buyerEmail = buyerEmail;
		this.participantsEmail = participantsEmail;
	}

	//TODO: refactor this
	@Override
	public int hashCode() {
		return Objects.hash(id, groupId, amount, description, date, buyerEmail);
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
		       Objects.equals(this.date, that.date) &&
		       Objects.equals(this.buyerEmail, that.buyerEmail);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Expense {id: ").append(id);
		if (groupId > 0) sb.append(", group: ").append(groupId);
		sb.append(", amount: ").append(amount);
		if (description != null) sb.append(", description: ").append(description);
		sb.append(", date: ").append(date);
		if (buyerEmail != null) sb.append(", buyerEmail: ").append(buyerEmail);
		if (participantsEmail != null)
			sb.append(", participantsEmail: [").append(String.join(", ", participantsEmail)).append("]");
		sb.append("}");

		return sb.toString();
	}

}
