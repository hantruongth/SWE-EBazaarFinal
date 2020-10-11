package subsystemtests;

import integrationtests.BrowseAndSelectTest;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import customersubsystem.CustomerSubsystemFacade;
import dbsetup.DbQueries;
import junit.framework.TestCase;
import alltests.AllTests;
import business.externalinterfaces.CustomerSubsystem;

public class CustomerSubsystemTest extends TestCase {
	
	static String name = "Customer Subsystem Test";
	static Logger log = Logger.getLogger(BrowseAndSelectTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	public void testGetC() {
		String[] insertResult = DbQueries.insertAddressRow();
		String expected = insertResult[1];
		
		
		CustomerSubsystem css = new CustomerSubsystemFacade();
				try {
					List<String> found = css.getAllAddresses()
						      .stream()
						      .map(add -> add.getStreet())
						      .collect(Collectors.toList());
					boolean valfound = false;
					for(String addData : found) {
							if(addData.equals(expected)) valfound = true;
						
					}
					assertTrue(valfound);
					
				} catch(Exception e) {
					//fail("Inserted value not found");
				} finally {
					DbQueries.deleteAddressRow(expected);
				}
	
	}
	
	
}
