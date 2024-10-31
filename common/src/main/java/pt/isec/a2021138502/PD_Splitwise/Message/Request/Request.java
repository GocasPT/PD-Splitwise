package pt.isec.a2021138502.PD_Splitwise.Message.Request;

import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.io.Serializable;

public interface Request extends Serializable {
	Response execute(DataBaseManager context);

	@Override
	String toString();
}
