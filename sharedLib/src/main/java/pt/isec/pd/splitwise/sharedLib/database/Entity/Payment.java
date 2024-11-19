package pt.isec.pd.splitwise.sharedLib.database.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@SuperBuilder
//TODO: name OR userEmail → name (?)
public final class Payment extends Entity {
	private String groupName;
	private Double value;
	private LocalDate date;
	private String buyerEmail;
	private String receiverEmail;

	@Override
	public int hashCode() {
		return Objects.hash(id, groupName, value, date, buyerEmail, receiverEmail);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Payment) obj;
		return this.id == that.id &&
		       Objects.equals(this.groupName, that.groupName) &&
		       Objects.equals(this.value, that.value) &&
		       Objects.equals(this.date, that.date) &&
		       Objects.equals(this.buyerEmail, that.buyerEmail) &&
		       Objects.equals(this.receiverEmail, that.receiverEmail);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Payment {id: ").append(id);
		if (groupName != null) sb.append(", group: ").append(groupName);
		sb.append(", value: ").append(value);
		sb.append(", date: ").append(date);
		if (buyerEmail != null) sb.append(", buyer: ").append(buyerEmail);
		if (receiverEmail != null) sb.append(", receiver: ").append(receiverEmail);
		sb.append("}");

		return sb.toString();
	}

}
