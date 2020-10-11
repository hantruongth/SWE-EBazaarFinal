package business.exceptions;

public class BackendException extends BusinessException {
	public BackendException(String msg) {
		super(msg);
	}

	public BackendException(Exception e) {
		super(e);
	}

	private static final long serialVersionUID = 3258144448058015026L;
}
