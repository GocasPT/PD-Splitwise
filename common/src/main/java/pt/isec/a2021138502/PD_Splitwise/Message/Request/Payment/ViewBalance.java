package pt.isec.a2021138502.PD_Splitwise.Message.Request.Payment;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Request.Request;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

/**
 * @param groupID TODO: client-side or server-side?
 */
public record ViewBalance(int groupID) implements Request {
	@Override
	public Response execute(DataBaseManager context) {
		return null;
	}

	@Override
	public String toString() {
		return "VIEW_BALANCE" + " " + groupID;
	}
}
