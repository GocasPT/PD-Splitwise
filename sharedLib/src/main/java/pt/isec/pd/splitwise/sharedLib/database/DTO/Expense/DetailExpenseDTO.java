package pt.isec.pd.splitwise.sharedLib.database.DTO.Expense;

import java.io.Serializable;
import java.time.LocalDate;

public record DetailExpenseDTO(double amount, String description, LocalDate date, String user) implements Serializable {
	@Override
	public String toString() {
		return "DetailExpenseDTO{" +
		       "amount: " + amount +
		       ", description: '" + description + '\'' +
		       ", date: " + date +
		       ", user: '" + user + '\'' +
		       '}';
	}
}
