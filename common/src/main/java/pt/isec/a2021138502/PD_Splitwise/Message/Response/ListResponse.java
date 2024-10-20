package pt.isec.a2021138502.PD_Splitwise.Message.Response;

import java.util.Arrays;

public class ListResponse<T> extends Response {
	private final T[] list;

	public ListResponse(T[] list) {
		super(true);
		this.list = list;
	}

	public ListResponse(String errorDescription) {
		super(false, errorDescription);
		this.list = null;
	}

	public T[] getList() {
		return list;
	}

	@Override
	public String toString() {
		return "ListResponse [sucess: " + isSuccess() + (!isSuccess() ? ", errorDescription: " + getErrorDescription() : ", list: " + Arrays.toString(list)) + "]";
	}
}
