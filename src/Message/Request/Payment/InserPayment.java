package Message.Request.Payment;

import Message.Request.EComands;
import Message.Request.Request;

import java.util.Date;

public class InserPayment extends Request {
	private int groupID; //TODO: client-side or server-side?
	private int userBuyerID;
	private int userReceiverID;
	private Date date;
	private double amount;

	public InserPayment(int groupID, int userBuyerID, int userReceiverID, Date date, double amount) {
		this.groupID = groupID;
		this.userBuyerID = userBuyerID;
		this.userReceiverID = userReceiverID;
		this.date = date;
		this.amount = amount;
	}

	public int getGroupID() {
		return groupID;
	}

	public int getUserBuyerID() {
		return userBuyerID;
	}

	public int getUserReceiverID() {
		return userReceiverID;
	}

	public Date getDate() {
		return date;
	}

	public double getAmount() {
		return amount;
	}

	@Override
	public String toString() {
		return EComands.INSERT_PAYMENT + " " + groupID + " " + userBuyerID + " " + userReceiverID + " " + date + " " + amount;
	}
}
