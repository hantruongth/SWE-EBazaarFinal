package middleware.externalinterfaces;

import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import middleware.exceptions.MiddlewareException;

public interface CreditVerification {
	public void checkCreditCard(CustomerProfile custProfile, Address billingAddress, CreditCard creditCard,
			double amount) throws MiddlewareException;
}
