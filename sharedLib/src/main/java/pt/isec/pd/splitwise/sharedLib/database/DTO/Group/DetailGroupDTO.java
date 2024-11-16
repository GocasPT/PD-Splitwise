package pt.isec.pd.splitwise.sharedLib.database.DTO.Group;

import pt.isec.pd.splitwise.sharedLib.database.DTO.Expense.PreviewExpenseDTO;

import java.io.Serializable;
import java.util.List;

public record DetailGroupDTO(int id, String name, List<PreviewExpenseDTO> expenses
) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "DetailGroupDTO{" +
		       "name='" + name + '\'' +
		       ", expenses=" + expenses +
		       '}';
	}
}
