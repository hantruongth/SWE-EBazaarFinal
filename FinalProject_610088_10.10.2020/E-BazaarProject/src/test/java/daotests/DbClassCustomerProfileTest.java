package daotests;

import alltests.AllTests;
import business.externalinterfaces.CustomerProfile;
import business.externalinterfaces.CustomerSubsystem;
import business.externalinterfaces.DbClassCustomerProfileForTest;
import customersubsystem.CustomerSubsystemFacade;
import dbsetup.DbQueries;
import java.util.logging.Logger;
import junit.framework.TestCase;

public class DbClassCustomerProfileTest extends TestCase {
    
    static String name = "DbClassCustomerProfile Test";
    static Logger log = Logger.getLogger(DbClassCustomerProfileTest.class.getName());
    
    static {
            AllTests.initializeProperties();
    }
    
    public void testReadCustomerProfile() {
        String[] expectedCustomer = DbQueries.insertCustomerRow();
        Integer custId = Integer.parseInt(expectedCustomer[1]);
        String expectedFirstName = expectedCustomer[2];
        String expectedLastName = expectedCustomer[3];
        
        //test real DbClassCustomerProfile
        CustomerSubsystem css = new CustomerSubsystemFacade();
        DbClassCustomerProfileForTest dbclass = css.getGenericDbClassCustomerProfile();
        
        try {
            dbclass.readCustomerProfile(custId);
            CustomerProfile custProfile = dbclass.getCustomerProfileForTest();
            
            assertEquals(custId, custProfile.getCustId());
            assertEquals(expectedFirstName, custProfile.getFirstName());
            assertEquals(expectedLastName, custProfile.getLastName());
            
            //delete customer with custId from Database
            DbQueries.deleteCustomerRow(custId);
            
            dbclass.readCustomerProfile(custId);
            custProfile = dbclass.getCustomerProfileForTest();
        } catch (Exception ex) {
            fail("Customer Profile doesn't match");
        }
        
    }
    
}
