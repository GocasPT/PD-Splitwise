package pt.isec.pd.sharedLib.database.Entity;

import java.util.List;
import java.util.Objects;

public final class Group extends Entity {
	private String name;
	private int numUsers;
	private List<User> members;
	private List<Expense> expenses;
	private List<Payment> payments;

	public Group(int id, String name) {
		super(id);
		this.name = name;
	}

	public Group(int id, String name, int numUsers) {
		super(id);
		this.name = name;
		this.numUsers = numUsers;
	}

	public Group(int id, String name, List<User> members, List<Expense> expenses,
	             List<Payment> payments) {
		super(id);
		this.name = name;
		this.members = members;
		this.expenses = expenses;
		this.payments = payments;
	}

	public String getName() {
		return name;
	}

	public int getNumUsers() {
		return numUsers;
	}

	public List<User> getMembers() {
		return members;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumUsers(int numUsers) {
		this.numUsers = numUsers;
	}

	public void setMembers(List<User> members) {
		this.members = members;
	}

	public void setExpenseList(List<Expense> expeneseList) {
		this.expenses = expeneseList;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

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
