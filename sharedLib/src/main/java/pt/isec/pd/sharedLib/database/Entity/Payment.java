package pt.isec.pd.sharedLib.database.Entity;

import java.util.Objects;

//TODO: name OR email → name (?)
public final class Payment extends Entity {
	private String groupName;
	private Double value;
	private long date;
	private String buyerName;
	private String reciverName;

	public Payment(int id, Double value, long date) {
		super(id);
		this.value = value;
		this.date = date;
	}

	public Payment(int id, String groupName, Double value, long date, String buyerName,
	               String reciverName) {
		super(id);
		this.groupName = groupName;
		this.value = value;
		this.date = date;
		this.buyerName = buyerName;
		this.reciverName = reciverName;
	}

	public String getGroupName() {
		return groupName;
	}

	public Double getValue() {
		return value;
	}

	public long getDate() {
		return date;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public String getReciverName() {
		return reciverName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public void setReciverName(String reciverName) {
		this.reciverName = reciverName;
	}

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
