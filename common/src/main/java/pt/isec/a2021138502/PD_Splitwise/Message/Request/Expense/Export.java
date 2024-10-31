package pt.isec.a2021138502.PD_Splitwise.Message.Request.Expense;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.ValueResponse;

import java.io.File;

public record Export(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		//TODO: query to export expenses

		File file = new File("expenses.txt");

		return new ValueResponse<>(file);
	}

	@Override
	public String toString() {
		return "EXPORT " + groupID;
	}
}
