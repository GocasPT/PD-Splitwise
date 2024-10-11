package Message.Request.Expense;

import Message.Request.EComands;
import Message.Request.Request;

public class DeleteExpense extends Request {
	private int expenseID; //TODO: groupId on client side or server side?

	public DeleteExpense(int expenseID) {
		this.expenseID = expenseID;
	}

	public int getExpenseID() {
		return expenseID;
	}

	@Override
	public String toString() {
		return EComands.DELETE_EXPENSE + " " + expenseID;
	}
}
