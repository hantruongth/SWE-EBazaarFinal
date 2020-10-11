package middleware.creditverifcation;

import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import middleware.exceptions.MiddlewareException;
import middleware.externalinterfaces.CreditVerification;
import publicview.IVerificationSystem;
import publicview.VerificationManager;

public class CreditVerificationFacade implements CreditVerification {

	/**
	 * Use of "amount" here is a violation of encapsulation. Should use a command
	 * object to encapsulate all the data.
	 * 
	 * @param custProfile
	 * @param billingAddress
	 * @param creditCard
	 * @param amount
	 */
	@Override
	public void checkCreditCard(CustomerProfile custProfile, Address billingAddress, CreditCard creditCard,
			double amount) throws MiddlewareException {

		IVerificationSystem verifSystem = VerificationManager.clientInterface();
		CreditVerifMediator mediator = new CreditVerifMediator();
		mediator.processCreditRequest(verifSystem, custProfile, billingAddress, creditCard, amount);

	}

}
