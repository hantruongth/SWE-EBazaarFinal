package subsystemtests;

import integrationtests.BrowseAndSelectTest;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import dbsetup.DbQueries;
import junit.framework.TestCase;
import alltests.AllTests;
import business.externalinterfaces.Product;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;

public class ProductSubsystemTest extends TestCase {
	
	static String name = "Product Subsystem Test";
	static Logger log = Logger.getLogger(BrowseAndSelectTest.class.getName());
	
	static {
		AllTests.initializeProperties();
	}
	
	public void testGetCatalogNames() {
		//setup
		/*
		 * Returns a String[] with values:
		 * 0 - query
		 * 1 - catalog id
		 * 2 - catalog name
		 */
		String[] insertResult = DbQueries.insertCatalogRow();
		String expected = insertResult[2];
		//System.out.println(expected);
		
		
		ProductSubsystem pss = new ProductSubsystemFacade();
		try {
			List<String> found = pss.getCatalogList()
				      .stream()
				      .map(cat -> cat.getName())
				      .collect(Collectors.toList());
			boolean valfound = false;
			for(String catData : found) {
				System.out.println(catData);
					if(catData.equals(expected)) valfound = true;
				
			}
			assertTrue(valfound);
			
		} catch(Exception e) {
			fail("Inserted value not found");
		} finally {
			DbQueries.deleteCatalogRow(Integer.parseInt(insertResult[1]));
		}
	
	}
	
	
	public void testGetProductNames() {
		//setup
		/*
		 * Returns a String[] with values:
		 * 0 - query
		 * 1 - product id
		 * 2 - product name
		 */
		String[] insertResult = DbQueries.insertProductRow();
		Integer expected = Integer.parseInt(insertResult[1]);
		
		ProductSubsystem pss = new ProductSubsystemFacade();
		try {
			Integer insertedProductId = pss.getProductFromId(expected).getProductId();
			boolean found = false;
			if(insertedProductId!=null){
				found = true;
			}
			assertTrue(found);
			
		} catch(Exception e) {
			fail("Inserted product not found");
		} finally {
			DbQueries.deleteProductRow(expected);
		}
	
	}
	
	
}
