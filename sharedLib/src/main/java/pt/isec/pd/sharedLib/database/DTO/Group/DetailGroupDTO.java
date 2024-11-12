package pt.isec.pd.sharedLib.database.DTO.Group;

import pt.isec.pd.sharedLib.database.DTO.Expense.PreviewExpenseDTO;
import pt.isec.pd.sharedLib.database.DTO.Payment.PreviewPaymentDTO;
import pt.isec.pd.sharedLib.database.DTO.User.PreviewUserDTO;

import java.io.Serializable;
import java.util.List;

public record DetailGroupDTO(int id, String name, List<PreviewUserDTO> members, List<PreviewExpenseDTO> expenses,
                             List<PreviewPaymentDTO> payments) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "DetailGroupDTO{" +
		       "name='" + name + '\'' +
		       ", members=" + members +
		       ", expenses=" + expenses +
		       ", payments=" + payments +
		       '}';
	}
}
