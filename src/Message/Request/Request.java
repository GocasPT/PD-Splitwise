package Message.Request;

import Data.DatabaseManager;
import Message.Response.Response;

import java.io.Serializable;

public interface Request extends Serializable {
	Response execute(DatabaseManager context);

	@Override
	String toString();
}
