package pt.isec.pd.splitwise.sharedLib.network.request.Expense;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;
import pt.isec.pd.splitwise.sharedLib.network.response.ValueResponse;

import java.io.File;

public record Export(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("Exporting expenses from group with ID: {}", groupID);

		File file = new File("expenses.txt");

		try {
			//TODO: maybe use all DAO to get the data from group
		} catch ( Exception e ) {
			logger.error("Export: {}", e.getMessage());
			return new ValueResponse<>("Fail to export history file");
		}

		return new ValueResponse<>(file);
	}

	@Override
	public String toString() {
		return "EXPORT " + groupID;
	}
}
