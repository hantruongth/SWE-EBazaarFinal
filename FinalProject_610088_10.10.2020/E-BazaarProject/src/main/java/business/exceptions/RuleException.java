package business.exceptions;

/**
 * Thrown when one of the business rules is violated.
 */
public class RuleException extends BusinessException {

	public RuleException(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 3257003267694145848L;
}
