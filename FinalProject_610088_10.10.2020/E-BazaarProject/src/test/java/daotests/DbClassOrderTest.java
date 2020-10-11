package daotests;

import integrationtests.ReviewOrderTest;

import java.util.List;
import java.util.logging.Logger;

import junit.framework.TestCase;
import alltests.AllTests;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.Order;
import business.externalinterfaces.OrderSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import customersubsystem.CustomerSubsystemFacade;
import dbsetup.DbQueries;

public class DbClassOrderTest extends TestCase {
	
	static String name = "Review Order Test";
	static Logger log = Logger.getLogger(ReviewOrderTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	
	public void testReadAllOrders() {
		List<Order> expected = DbQueries.readOrders();
		
		//test real dbclass order
		CustomerSubsystem customer = new CustomerSubsystemFacade();
        //finish initialization
        OrderSubsystem oss = new OrderSubsystemFacade(customer.getGenericCustomerProfile());
		try {
			List<Order> found = oss.getOrderHistory();
			assertTrue(expected.size() == found.size());
		} catch(Exception e) {
			fail("Order Lists don't match");
		}
		
	}
}
