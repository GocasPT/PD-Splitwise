package pt.isec.pd.splitwise.sharedLib.network.request.Payment;

import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

public record InsertPayment(int groupID, double amount, String userPayerEmail,
                            String userReceiverEmail) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("""
		             Inserting payment on group {}
		             \tamout: {}
		             \tpayer: {}
		             \treceiver: {}""",
		             groupID, amount, userPayerEmail, userReceiverEmail);

		try {
			context.getPaymentDAO().createPayment(
					context.getUserDAO().getUserByEmail(userPayerEmail).getId(),
					context.getUserDAO().getUserByEmail(userReceiverEmail).getId(),
					amount
			);
		} catch ( Exception e ) {
			logger.error("InsertPayment: {}", e.getMessage());
			return new Response(false, "Failed to insert payment");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INSERT_PAYMENT " + groupID + " " + userPayerEmail + " " + userReceiverEmail + " " + amount;
	}
}
