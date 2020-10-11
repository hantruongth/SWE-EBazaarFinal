package subsystemtests;

import integrationtests.ReviewOrderTest;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import junit.framework.TestCase;
import alltests.AllTests;
import business.exceptions.BackendException;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.OrderSubsystem;
import business.externalinterfaces.ProductSubsystem;
import business.ordersubsystem.OrderSubsystemFacade;
import business.productsubsystem.ProductSubsystemFacade;
import customersubsystem.CustomerSubsystemFacade;
import dbsetup.DbQueries;

public class OrderSubsystemTest extends TestCase {
	
	static String name = "Order Subsystem Test";
	static Logger log = Logger.getLogger(ReviewOrderTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	public void testGetOrderIds() {
		String[] vals = DbQueries.insertOrderRow();
		int expectedId = Integer.parseInt(vals[1]);
		List<Integer> orderIds=null;

        
		try {
			// Perform test
			CustomerSubsystem customer = new CustomerSubsystemFacade();
	        //finish initialization
	        OrderSubsystem oss = new OrderSubsystemFacade(customer.getGenericCustomerProfile());
			orderIds = oss.getOrderHistory()
					      .stream()
					      .map(cat -> cat.getOrderId())
					      .collect(Collectors.toList());
                
		}
		catch(BackendException ex){
			fail("DatabaseException: " + ex.getMessage());
		}
		finally {
			assertTrue(orderIds != null);
			boolean idFound = false;
			if(orderIds != null){
				for (int next : orderIds) {
					if(next == expectedId) {
						idFound=true;
						System.out.println(next);
						break;
					}
				}
			}
			assertTrue(idFound);
			// Clean up table
			DbQueries.deleteOrderRow(expectedId);
			
		}
	}
	
}
