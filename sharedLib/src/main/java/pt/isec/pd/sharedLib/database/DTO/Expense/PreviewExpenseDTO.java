package pt.isec.pd.sharedLib.database.DTO.Expense;

import java.io.Serializable;

public record PreviewExpenseDTO(int id, double amount, long timestamp, String buyEmail) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "PreviewExpenseDTO{" +
		       "id=" + id +
		       ", amount=" + amount +
		       ", timestamp=" + timestamp +
		       ", buyEmail='" + buyEmail + '\'' +
		       '}';
	}
}
