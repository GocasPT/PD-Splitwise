package pt.isec.pd.splitwise.sharedLib.database.DTO.Payment;

import java.io.Serializable;

public record DetailPaymentDTO(int id, double amount, long timestamp, String user) implements Serializable {
	@Override
	public String toString() {
		return "DetailPaymentDTO{" +
		       "id=" + id +
		       ", amount=" + amount +
		       ", timestamp=" + timestamp +
		       ", user='" + user + '\'' +
		       '}';
	}
}
