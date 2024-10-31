package pt.isec.a2021138502.PD_Splitwise.Message;

import java.io.Serializable;

public record Heartbeat(int version, int tcpPort) implements Serializable {
	@Override
	public String toString() {
		return "Heartbeat[ version: " + version + ", tcpPort: " + tcpPort + " ]";
	}
}
