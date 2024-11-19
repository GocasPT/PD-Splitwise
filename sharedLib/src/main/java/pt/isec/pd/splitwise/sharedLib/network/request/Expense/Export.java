package pt.isec.pd.splitwise.sharedLib.network.request.Expense;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Expense;
import pt.isec.pd.splitwise.sharedLib.database.Entity.Group;
import pt.isec.pd.splitwise.sharedLib.database.Entity.User;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;
import pt.isec.pd.splitwise.sharedLib.network.response.ValueResponse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public record Export(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Exporting expenses from group {}", groupID);

		File csvFile = new File("expenses.txt");
		try (
				FileWriter writer = new FileWriter(csvFile);
		) {
			//TODO: maybe use all DAO to get the data from group
			Group group = context.getGroupDAO().getGroupById(groupID);
			writer.write("Nome do grupo");
			writer.write(group.getName());
			writer.write("\n");

			List<User> members = context.getGroupUserDAO().getAllUsersFromGroup(groupID);
			writer.write("Elementos");
			for (User user : members) {
				writer.write(user.getEmail() + ";");
			}
			writer.write("\n");

			List<Expense> expenseList = context.getExpenseDAO().getAllExpensesFromGroup(groupID);
			writer.write("Despesas");
			//TODO: write expenses with details on file

			//TODO: send file to client (maybe divide in chunks)
			return new ValueResponse<>(csvFile);
		} catch ( Exception e ) {
			logger.error("Export: {}", e.getMessage());
			return new ValueResponse<>("Fail to export history file");
		}
	}

	@Override
	public String toString() {
		return "EXPORT " + groupID;
	}
}