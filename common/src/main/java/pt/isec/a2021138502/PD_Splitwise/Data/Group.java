package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public final class Group implements Serializable {
	private int id;
	private String name;
	private int numUsers;
	private List<User> userList;
	private List<Expense> expeneseList;
	private List<Payment> paymentsList;

	public Group(int id, String name, int numUsers) {
		this.id = id;
		this.name = name;
		this.numUsers = numUsers;
	}

	public Group(int id, String name, List<User> userList, List<Expense> expeneseList,
	             List<Payment> paymentsList) {
		this.id = id;
		this.name = name;
		this.userList = userList;
		this.expeneseList = expeneseList;
		this.paymentsList = paymentsList;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getNumUsers() {
		return numUsers;
	}

	public List<User> getUserList() {
		return userList;
	}

	public List<Expense> getExpeneseList() {
		return expeneseList;
	}

	public List<Payment> getPaymentsList() {
		return paymentsList;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumUsers(int numUsers) {
		this.numUsers = numUsers;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public void setExpenseList(List<Expense> expeneseList) {
		this.expeneseList = expeneseList;
	}

	public void setPaymentsList(List<Payment> paymentsList) {
		this.paymentsList = paymentsList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, userList, expeneseList, paymentsList);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Group) obj;
		return this.id == that.id &&
				Objects.equals(this.name, that.name) &&
				Objects.equals(this.userList, that.userList) &&
				Objects.equals(this.expeneseList, that.expeneseList) &&
				Objects.equals(this.paymentsList, that.paymentsList);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Group [id: ").append(id);
		sb.append(", name: ").append(name);

		if (userList == null) sb.append(", numUsers: ").append(numUsers);
		else {
			sb.append(",\n\tusers: ").append(userList);
			sb.append(",\n\texpenses: ").append(expeneseList);
			sb.append(",\n\tpayments: ").append(paymentsList).append("\n");
		}
		sb.append("]");

		return sb.toString();
	}
}
