package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

//TODO: name OR email → name (?)
public final class Expense implements Serializable {
	private int id;
	private String groupName;
	private Double value;
	private String description;
	private Date date;
	private String buyerName;

	public Expense(int id, Double value, Date date) {
		this.id = id;
		this.value = value;
		this.date = date;
	}

	public Expense(int id, String groupName, Double value, String description, Date date,
	               String buyerName) {
		this.id = id;
		this.groupName = groupName;
		this.value = value;
		this.description = description;
		this.date = date;
		this.buyerName = buyerName;
	}

	public int getId() {
		return id;
	}

	public String getGroupName() {
		return groupName;
	}

	public Double getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

	public Date getDate() {
		return date;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, groupName, value, description, date, buyerName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Expense) obj;
		return this.id == that.id &&
				Objects.equals(this.groupName, that.groupName) &&
				Objects.equals(this.value, that.value) &&
				Objects.equals(this.description, that.description) &&
				Objects.equals(this.date, that.date) &&
				Objects.equals(this.buyerName, that.buyerName);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Expense [id: ").append(id);
		if (groupName != null) sb.append(", group: ").append(groupName);
		sb.append(", value: ").append(value);
		if (description != null) sb.append(", description: ").append(description);
		sb.append(", date: ").append(date);
		if (buyerName != null) sb.append(", buyer: ").append(buyerName);
		sb.append("]");

		return sb.toString();
	}

}
