package pt.isec.a2021138502.PD_Splitwise.Message;

import java.io.Serializable;

//TODO: record or final class (?)
public record Heartbeat(int version, int tcpPort, String query) implements Serializable {
	public static final int BYTE_LENGTH = 8192;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("Heartbeat[ ");
		sb.append("version: ").append(version);
		sb.append(", tcpPort: ").append(tcpPort);
		if (query != null) sb.append(", query: ").append(query);
		sb.append(" ]");

		return sb.toString();
	}
}
