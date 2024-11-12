package pt.isec.pd.sharedLib.database.DTO.Payment;

import java.io.Serializable;

public record DetailPaymentDTO(int id, double amount, long timestamp, String user) implements Serializable {
	//TODO: refactor this
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
