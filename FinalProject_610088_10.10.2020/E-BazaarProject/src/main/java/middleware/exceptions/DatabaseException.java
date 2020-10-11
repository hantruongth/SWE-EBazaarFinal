
package middleware.exceptions;

public class DatabaseException extends MiddlewareException {
	static final String message = "A database error has occurred.";

	public DatabaseException(String msg) {
		// msg will override standard message if not null
		super((msg == null) ? message : msg);
	}

	public DatabaseException(Exception e) {
		super(e);
	}

	private static final long serialVersionUID = 3258128038058015026L;
}
