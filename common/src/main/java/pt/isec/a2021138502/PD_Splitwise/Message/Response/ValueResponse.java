package pt.isec.a2021138502.PD_Splitwise.Message.Response;

public class ValueResponse<T> extends Response {
	private final T value;

	public ValueResponse(T value) {
		super(true);
		this.value = value;
	}

	public ValueResponse(String errorDescription) {
		super(false, errorDescription);
		this.value = null;
	}

	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "ValueResponse [sucess: " + isSuccess() + (!isSuccess() ? ", errorDescription=" + getErrorDescription() : ", value=" + value) + "]";
	}
}
