package pt.isec.pd.sharedLib.network.request.Payment;

import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.Response;

public record DeletePayment(int paymentID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("DeletePayment: {}", this);

		try {
			context.getPaymentDAO().deletePayment(paymentID);
		} catch ( Exception e ) {
			logger.error("DeletePayment: {}", e.getMessage());
			return new Response(false);
		}

		return new Response(true);
	}

	@Override
	public String toString() {
		return "DELETE_PAYMENT " + paymentID;
	}
}
