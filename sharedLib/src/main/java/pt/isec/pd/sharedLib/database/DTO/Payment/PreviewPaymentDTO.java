package pt.isec.pd.sharedLib.database.DTO.Payment;

import java.io.Serializable;

public record PreviewPaymentDTO(int id, double amount, long timestamp, String user) implements Serializable {
	//TODO: refactor this
	@Override
	public String toString() {
		return "PreviewPaymentDTO{" +
		       "id=" + id +
		       ", amount=" + amount +
		       ", timestamp=" + timestamp +
		       ", user='" + user + '\'' +
		       '}';
	}
}
