package middleware.creditverifcation;

import java.util.logging.Logger;

import business.externalinterfaces.Address;
import business.externalinterfaces.CreditCard;
import business.externalinterfaces.CustomerProfile;
import middleware.exceptions.MiddlewareException;
import publicview.IVerificationSystem;
import publicview.TransactionFailedException;

class CreditVerifMediator {
	// for testing only; should disable in production
	private static int numFailures = 0;
	private static final Logger LOG = Logger.getLogger(CreditVerifMediator.class.getName());

	void processCreditRequest(IVerificationSystem v, CustomerProfile custProfile, Address billingAddress,
			CreditCard creditCard, double amount) throws MiddlewareException {
		v.setBillingAddress(billingAddress.getStreet(), billingAddress.getCity(), billingAddress.getState(),
				billingAddress.getZip());
		v.setAmountToCharge(amount);
		v.setCreditCardExpirationDate(creditCard.getExpirationDate());
		v.setCreditCardNumber(creditCard.getCardNum());
		v.setCustomerFirstName(custProfile.getFirstName());
		v.setCustomerLastName(custProfile.getLastName());
		try {
			LOG.info("Processing credit verification request...");
			v.processRequest();
			LOG.info("...passed");

		} catch (TransactionFailedException tfe) {
			++numFailures;
			if (numFailures <= 1) {
				throw new MiddlewareException(tfe.getMessage());
			} else {
				LOG.warning("Getting many Credit Verification failures");
			}
		}
	}
}
