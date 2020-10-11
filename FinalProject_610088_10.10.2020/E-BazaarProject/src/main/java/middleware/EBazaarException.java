
package middleware;

public class EBazaarException extends Exception {
	public EBazaarException(String msg) {
		super(msg);
	}

	public EBazaarException(Exception e) {
		super(e);
	}

	private static final long serialVersionUID = 3258144448058015026L;
}
