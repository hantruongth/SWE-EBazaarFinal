
package business.exceptions;

public class BusinessException extends Exception {
	public BusinessException(String msg) {
		super(msg);
	}

	public BusinessException(Exception e) {
		super(e);
	}

	private static final long serialVersionUID = 3258144448058015026L;
}
