package pt.isec.a2021138502.PD_Splitwise.Message.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isec.a2021138502.PD_Splitwise.Data.DataBaseManager;
import pt.isec.a2021138502.PD_Splitwise.Message.Response.Response;

import java.io.Serializable;

public interface Request extends Serializable {
	Logger logger = LoggerFactory.getLogger(Request.class);

	Response execute(DataBaseManager context);

	@Override
	String toString();
}
