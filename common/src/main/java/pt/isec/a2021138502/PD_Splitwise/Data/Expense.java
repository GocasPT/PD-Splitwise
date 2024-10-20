package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;
import java.util.Date;

public record Expense(String description, Double value, Date date, String groupName,
                      String buyerName) implements Serializable {
	@Override
	public String toString() {
		return "Expense [value: " + value + "]";
	}
}
