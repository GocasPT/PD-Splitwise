package pt.isec.pd.splitwise.sharedLib.database.DTO.Payment;

import java.io.Serializable;
import java.time.LocalDate;

public record PreviewPaymentDTO(int paymentId, double amount, LocalDate date, String payerUser, String receiverUser) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "PreviewPaymentDTO{" +
		       "paymentId=" + paymentId +
		       ", amount=" + amount +
		       ", date=" + date +
		       ", payerUser='" + payerUser + '\'' +
		       ", receiverUser='" + receiverUser + '\'' +
		       '}';
	}
}
