package pt.isec.pd.sharedLib.database.DTO.Expense;

import java.io.Serializable;

public record DetailExpenseDTO(double amount, String description, long timestamp, String user) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "DetailExpenseDTO{" +
		       "amount=" + amount +
		       ", description='" + description + '\'' +
		       ", timestamp=" + timestamp +
		       ", user='" + user + '\'' +
		       '}';
	}
}
