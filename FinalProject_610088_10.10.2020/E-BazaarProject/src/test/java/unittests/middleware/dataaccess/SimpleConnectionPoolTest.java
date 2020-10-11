
package unittests.middleware.dataaccess;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import alltests.AllTests;
import junit.framework.TestCase;
import middleware.DbConfigProperties;
import middleware.dataaccess.*;
import middleware.exceptions.DatabaseException;
import middleware.externalinterfaces.DataAccessTest;
import middleware.externalinterfaces.DbConfigKey;
import dbsetup.DbQueries;

public class SimpleConnectionPoolTest extends TestCase {
	static Logger log = Logger.getLogger(SimpleConnectionPoolTest.class.getName());
	
	static String name = "middleware.dataaccess.SimpleConnectionPool";
	static {
		AllTests.initializeProperties();
	}
	DbConfigProperties props = new DbConfigProperties();
 	
    final String ACCOUNT_DBURL =props.getProperty(DbConfigKey.ACCOUNT_DB_URL.getVal());
    final String PRODUCT_DBURL =props.getProperty(DbConfigKey.PRODUCT_DB_URL.getVal());
    final int MAX_CONN = 4;
	
	public SimpleConnectionPoolTest(String s) {
		super(s);
	}
	
	public void setUp() {
		log.info("  Running "+ getName());		
 	}
	
	//tests
	public void testMultipleDbConnections() {
		// Add rows to two tables on different dbs
		String[] custVals = DbQueries.insertCustomerRow();
		String[] prodVals = DbQueries.insertProductRow();
		
		// Prepare queries for each
		String[] queries = {"select productid,productname from Product",
							"select custid,fname,lname from Customer"};
		String[] dburls = {PRODUCT_DBURL,ACCOUNT_DBURL};	
	    DataAccessTest test = new DataAccessSubsystemFacade();
	    String expectedProdName = "";
	    String prodNameFound = null;
	    String expectedCustName = "";
	    String custNameFound = null;
	    try {
	    	ResultSet[] results = test.multipleInstanceQueries(queries, dburls);
	    	// gather up product results
	    	int prodId = Integer.parseInt(prodVals[1]);
	    	expectedProdName = prodVals[2];
	    	
	        while(results[0].next()){
	        	int idFound = Integer.parseInt(results[0].getString("productid"));
	        	if (idFound == prodId) {
	        		prodNameFound = results[0].getString("productname");
	        	}
	        }
	        //gather up cust results
	        int custId = Integer.parseInt(custVals[1]);
	    	expectedCustName = custVals[2]+custVals[3];
	    	
	        while(results[1].next()){
	        	int idFound = Integer.parseInt(results[1].getString("custid"));
	        	if (idFound == custId) {
	        		custNameFound = results[1].getString("fname");
	        		custNameFound += results[1].getString("lname");
	        	}
	        }
	        

	    }
   		catch(DatabaseException ex){
   			fail("ERROR: Error occurred trying to read table: "+ex.getClass().getName()+" Message: "+ex.getMessage());
   		}
   		catch(SQLException ex){
			fail("ERROR: Error occurred trying to read table: "+ex.getClass().getName()+" Message: "+ex.getMessage());
		}
   		finally {
   			//System.out.println(firstRowProd);
   			//System.out.println(firstRowCust);
   			assertEquals(expectedProdName,prodNameFound);
			assertEquals(expectedCustName,custNameFound);
   			//Now delete the rows that were added for testing
   			DbQueries.deleteProductRow(Integer.parseInt(prodVals[1]));
   			DbQueries.deleteCustomerRow(Integer.parseInt(custVals[1]));
   		}
		    
	}
	
}
