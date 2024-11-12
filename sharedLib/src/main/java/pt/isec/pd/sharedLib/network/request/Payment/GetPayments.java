package pt.isec.pd.sharedLib.network.request.Payment;


import pt.isec.pd.sharedLib.database.DTO.Payment.DetailPaymentDTO;
import pt.isec.pd.sharedLib.database.DTO.Payment.PreviewPaymentDTO;
import pt.isec.pd.sharedLib.database.DataBaseManager;
import pt.isec.pd.sharedLib.network.request.Request;
import pt.isec.pd.sharedLib.network.response.ListResponse;
import pt.isec.pd.sharedLib.network.response.Response;

import java.util.ArrayList;
import java.util.List;

public record GetPayments(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		logger.debug("GetPayments: {}", this);

		List<DetailPaymentDTO> payments = new ArrayList<>();
		try {
			context.getPaymentDAO().getPaymentsFromGroup(groupID).forEach(
					payment -> new PreviewPaymentDTO(
							payment.getId(),
							payment.getValue(),
							payment.getDate(),
							//TODO: payment.getBuyer().getId(),
							"PLACE_HOLDER"
							//TODO: payment.getReceiver().getId()
					)
			);
		} catch ( Exception e ) {
			logger.error("GetPayments: {}", e.getMessage());
			return new ListResponse<>("Fault to get payments");
		}

		return new ListResponse<>(payments.toArray());
	}

	@Override
	public String toString() {
		return "GET_PAYMENTS " + groupID;
	}
}
