package daotests;


import java.util.List;
import java.util.logging.Logger;

import dbsetup.DbQueries;
import junit.framework.TestCase;
import alltests.AllTests;
import business.externalinterfaces.Catalog;
import business.externalinterfaces.DbClassCatalogForTest;
import business.externalinterfaces.ProductSubsystem;
import business.productsubsystem.ProductSubsystemFacade;

public class DbClassCatalogTest extends TestCase {
	
	static String name = "Product Subsystem Test";
	static Logger log = Logger.getLogger(name);
	
	static {
		AllTests.initializeProperties();
	}
	
	public void testReadAllCatalogs() {
		List<Catalog> expected = DbQueries.readCustCatalogs();
		System.out.println(expected.size());
		
		ProductSubsystem pss = new ProductSubsystemFacade();
		DbClassCatalogForTest dbclass = pss.getGenericDbClassCatalogs();
		System.out.println(dbclass.getCatalogList().size());
		try {
			dbclass.getCatalogList();
			List<Catalog> found = dbclass.getCatalogList();
			assertTrue(expected.size() == found.size());
			
		} catch(Exception e) {
			fail("Catalog Lists don't match");
		}
		
	}
}
