package pt.isec.pd.splitwise.sharedLib.database.DTO.Expense;

import java.io.Serializable;
import java.time.LocalDate;

public record PreviewExpenseDTO(int id, double amount, LocalDate date, String buyEmail) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "PreviewExpenseDTO{" +
		       "id=" + id +
		       ", amount=" + amount +
		       ", date=" + date +
		       ", buyEmail='" + buyEmail + '\'' +
		       '}';
	}
}
