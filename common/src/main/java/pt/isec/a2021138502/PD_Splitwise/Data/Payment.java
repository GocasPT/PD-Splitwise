package pt.isec.a2021138502.PD_Splitwise.Data;

import java.io.Serializable;
import java.util.Date;

public record Payment(Double value, Date date, String groupName, String buyerName,
                      String reciverName) implements Serializable {
	@Override
	public String toString() {
		return "Payment{" +
				"buyerName='" + buyerName + '\'' +
				", value=" + value +
				", date=" + date +
				", groupName='" + groupName + '\'' +
				", reciverName='" + reciverName + '\'' +
				'}';
	}
}
