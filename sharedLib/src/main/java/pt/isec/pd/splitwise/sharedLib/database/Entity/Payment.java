package pt.isec.pd.splitwise.sharedLib.database.Entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Objects;

@Setter
@Getter
@SuperBuilder
//TODO: name OR email → name (?)
public final class Payment extends Entity {
	private String groupName;
	private Double value;
	private LocalDate date;
	private String buyerName;
	private String reciverName;

	@Override
	public int hashCode() {
		return Objects.hash(id, groupName, value, date, buyerName, reciverName);
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
				Objects.equals(this.buyerName, that.buyerName) &&
				Objects.equals(this.reciverName, that.reciverName);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Payment [id: ").append(id);
		if (groupName != null) sb.append(", group: ").append(groupName);
		sb.append(", value: ").append(value);
		sb.append(", date: ").append(date);
		if (buyerName != null) sb.append(", buyer: ").append(buyerName);
		if (reciverName != null) sb.append(", reciver: ").append(reciverName);
		sb.append("]");

		return sb.toString();
	}

}
