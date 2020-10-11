package integrationtests;

import java.util.logging.Logger;

import dbsetup.DbQueries;
import customersubsystem.AddressImpl;
import alltests.AllTests;
import business.BusinessConstants;
import business.SessionCache;
import business.externalinterfaces.Address;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.usecasecontrol.LoginControl;
import junit.framework.TestCase;

public class CheckoutTest extends TestCase {
	static String name = "Checkout test";
	static Logger logger = Logger.getLogger(CheckoutTest.class.getName());
	
	static{
		AllTests.initializeProperties();
	}
	
	public void testDefaultShippingBilling(){
		Address[] expectedAddresses = DbQueries.readDefaultShippingBilling();
		
	
	}
}
