package pt.isec.pd.splitwise.sharedLib.network.request.Payment;


import pt.isec.pd.splitwise.sharedLib.database.DataBaseManager;
import pt.isec.pd.splitwise.sharedLib.network.request.Request;
import pt.isec.pd.splitwise.sharedLib.network.response.Response;

public record InsertPayment(int groupID, double amount, int userBuyerID, int userReceiverID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("InsertPayment: {}", this);

		try {
			//context.getPaymentDAO().createPayment(groupID, userBuyerID, userReceiverID, amount);
			context.getPaymentDAO().createPayment(userBuyerID, userReceiverID, amount);
		} catch ( Exception e ) {
			logger.error("InsertPayment: {}", e.getMessage());
			return new Response(false, "Failed to insert payment");
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "INSERT_PAYMENT " + groupID + " " + userBuyerID + " " + userReceiverID + " " + amount;
	}
}
