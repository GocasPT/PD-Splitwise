package pt.isec.a2021138502.PD_Splitwise.Server;

import pt.isec.a2021138502.PD_Splitwise.Data.IDatabaseChangeObserver;

public class ChangeManager implements IDatabaseChangeObserver {
	private HeartbeatSender heartbeatSender;

	public void registe(HeartbeatSender heartbeatSender) {
		this.heartbeatSender = heartbeatSender;
	}

	public void unregister() {
		heartbeatSender = null;
	}

	@Override
	public void onDatabaseChange(String query, Object... params) {
		if (heartbeatSender != null) {
			//heartbeatSender.sendHeartbeat(query, params);
		}
	}
}
