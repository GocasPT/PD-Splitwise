package Message.Request.Expense;

import Message.Request.EComands;
import Message.Request.Request;

import java.util.Date;

public class InsertExpense extends Request {
	private int groupID; //TODO: groupId on client side or server side?
	private Date date; //TODO: java.util.Date or java.sql.Date?
	private double amount;
	private String userBuyer;

	public InsertExpense(int groupID, Date date, double amount, String userBuyer) {
		this.groupID = groupID;
		this.date = date;
		this.amount = amount;
		this.userBuyer = userBuyer;
	}

	public int getGroupID() {
		return groupID;
	}

	public Date getDate() {
		return date;
	}

	public double getAmount() {
		return amount;
	}

	public String getUserBuyer() {
		return userBuyer;
	}

	@Override
	public String toString() {
		return EComands.INSERT_EXPENSE + " " + groupID + " " + date + " " + amount + " " + userBuyer;
	}
}
