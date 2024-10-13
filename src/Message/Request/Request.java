package Message.Request;

import Message.Response.Response;

import java.io.Serializable;

public interface Request extends Serializable {
	Response execute(); //TODO: give server access to execute the request logic

	@Override
	String toString();
}
