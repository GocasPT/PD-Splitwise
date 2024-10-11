package Message.Request.Expense;

import Message.Request.EComands;
import Message.Request.Request;

import java.util.Date;

public class EditExpense extends Request {
	private int expenseID;
	private int groupID;
	private Date date; //TODO: java.util.Date or java.sql.Date?
	private double amount;
	private String buyer;

	public EditExpense(int expenseID, int groupID, Date date, double amount, String buyer) {
		this.expenseID = expenseID;
		this.groupID = groupID;
		this.date = date;
		this.amount = amount;
		this.buyer = buyer;
	}

	public int getExpenseID() {
		return expenseID;
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

	public String getBuyer() {
		return buyer;
	}

	@Override
	public String toString() {
		return EComands.EDIT_EXPENSE + " " + expenseID + " " + groupID + " " + date + " " + amount + " " + buyer;
	}
}
