package Message;

import java.io.Serializable;

public class Hearthbeat implements Serializable {
	private final int version;
	private final int tcpPort;

	public Hearthbeat(int version, int tcpPort) {
		this.version = version;
		this.tcpPort = tcpPort;
	}

	public int getVersion() {
		return version;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	@Override
	public String toString() {
		return "Heartbeat[ version: " + version + ", tcpPort: " + tcpPort + " ]";
	}
}
