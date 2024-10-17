package Message.Response;

import java.util.Arrays;

public class ListResponse<T> extends Response {
	private final T[] invites;

	public ListResponse(T[] invites) {
		super(true);
		this.invites = invites;
	}

	public ListResponse(String errorDescription) {
		super(false, errorDescription);
		this.invites = null;
	}

	public T[] getInvites() {
		return invites;
	}

	@Override
	public String toString() {
		return "ListResponse [sucess: " + isSuccess() + (!isSuccess() ? ", errorDescription=" + getErrorDescription() : ", invites=" + Arrays.toString(invites)) + "]";
	}
}
